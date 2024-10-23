package org.kgusarov.krono.locales.ja.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.ComponentsInputFactory
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.Parser
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.calculation.findYearClosestToRef
import org.kgusarov.krono.extensions.plus
import org.kgusarov.krono.extensions.safeParseInt
import org.kgusarov.krono.locales.ja.toHankaku

@SuppressFBWarnings("EI_EXPOSE_REP")
class JaStandardParser : Parser {
    override fun pattern(context: ParsingContext) = PATTERN

    override fun invoke(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult {
        val month = toHankaku(match[MONTH_GROUP]!!).safeParseInt()!!
        val day = toHankaku(match[DAY_GROUP]!!).safeParseInt()!!
        val components =
            context.createParsingComponents(
                ComponentsInputFactory(
                    KronoComponents.Day to day,
                    KronoComponents.Month to month,
                ),
            )

        if (
            !match[SPECIAL_YEAR_GROUP].isNullOrEmpty() &&
            SPECIAL_YEAR_PATTERN.containsMatchIn(match[SPECIAL_YEAR_GROUP]!!)
        ) {
            val year = context.instant.year
            components.assign(KronoComponents.Year, year)
        }

        if (!match[TYPICAL_YEAR_GROUP].isNullOrEmpty()) {
            val year =
                when (val yearNumText = match[YEAR_NUMBER_GROUP]!!) {
                    "元" -> 1
                    else -> toHankaku(yearNumText).safeParseInt()
                }

            when (match[ERA_GROUP]) {
                "令和" -> components.assign(KronoComponents.Year, year + 2018)
                "平成" -> components.assign(KronoComponents.Year, year + 1988)
                "昭和" -> components.assign(KronoComponents.Year, year + 1925)
                else -> components.assign(KronoComponents.Year, year)
            }
        } else {
            val year = findYearClosestToRef(context.instant, day, month)
            components.imply(KronoComponents.Year, year)
        }

        return ParserResultFactory(components)
    }

    @Suppress("RegExpSingleCharAlternation")
    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "(?:(?:([同今本])|((昭和|平成|令和)?([0-9０-９]{1,4}|元)))年\\s*)?([0-9０-９]{1,2})月\\s*([0-9０-９]{1,2})日",
                RegexOption.IGNORE_CASE,
            )

        @JvmStatic
        private val SPECIAL_YEAR_PATTERN = Regex("同|今|本")

        private const val SPECIAL_YEAR_GROUP = 1
        private const val TYPICAL_YEAR_GROUP = 2
        private const val ERA_GROUP = 3
        private const val YEAR_NUMBER_GROUP = 4
        private const val MONTH_GROUP = 5
        private const val DAY_GROUP = 6
    }
}
