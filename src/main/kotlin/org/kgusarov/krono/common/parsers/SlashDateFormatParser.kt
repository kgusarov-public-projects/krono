package org.kgusarov.krono.common.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.ParsedResult
import org.kgusarov.krono.Parser
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.TextOrEndIndexInputFactory
import org.kgusarov.krono.calculation.findMostLikelyADYear
import org.kgusarov.krono.calculation.findYearClosestToRef
import org.kgusarov.krono.extensions.not
import org.kgusarov.krono.extensions.plus

@SuppressFBWarnings("EI_EXPOSE_REP")
class SlashDateFormatParser(
    private val groupNumberMonth: Int,
    private val groupNumberDay: Int,
) : Parser {
    constructor(littleEndian: Boolean) : this(
        if (littleEndian) SECOND_NUMBERS_GROUP else FIRST_NUMBERS_GROUP,
        if (littleEndian) FIRST_NUMBERS_GROUP else SECOND_NUMBERS_GROUP,
    )

    override fun pattern(context: ParsingContext) = PATTERN

    override fun invoke(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult? {
        val index = match.index + match[OPENING_GROUP]!!.length
        val indexEnd = match.index + match[0]!!.length - match[ENDING_GROUP]!!.length
        if (index > 0) {
            val textBefore = context.text.substring(0, index)
            if (BEFORE_PATTERN.containsMatchIn(textBefore)) {
                return null
            }
        }

        if (indexEnd < context.text.length) {
            val textAfter = context.text.substring(indexEnd)
            if (AFTER_PATTERN.containsMatchIn(textAfter)) {
                return null
            }
        }

        val text = context.text.substring(index, indexEnd)
        if (text.matches(VERSION_PATTERN1) || text.matches(VERSION_PATTERN2)) {
            return null
        }

        if (!match[YEAR_GROUP] && text.indexOf('/') < 0) {
            return null
        }

        val result = context.createParsingResult(index, TextOrEndIndexInputFactory(text))
        var month = match.getInt(groupNumberMonth)
        var day = match.getInt(groupNumberDay)

        if (month < 1 || month > 12) {
            if (month > 12) {
                if (day in 1..12 && month <= 31) {
                    month = day.also { day = month }
                } else {
                    return null
                }
            }
        }

        if (day < 1 || day > 31) {
            return null
        }

        result.start.assign(KronoComponents.Day, day)
        result.start.assign(KronoComponents.Month, month)

        if (!match[YEAR_GROUP]) {
            val year = findYearClosestToRef(context.instant, day, month)
            result.start.imply(KronoComponents.Year, year)
        } else {
            val rawYearNumber = match.getInt(YEAR_GROUP)
            val year = findMostLikelyADYear(rawYearNumber)
            result.start.assign(KronoComponents.Year, year)
        }

        return ParserResultFactory(result.addTag<ParsedResult>("parser/SlashDateFormatParser"))
    }

    @Suppress("RegExpRedundantEscape", "RegExpSimplifiable")
    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "([^\\d]|^)" +
                    "([0-3]{0,1}[0-9]{1})[\\/\\.\\-]([0-3]{0,1}[0-9]{1})" +
                    "(?:[\\/\\.\\-]([0-9]{4}|[0-9]{2}))?" +
                    "(\\W|$)",
                RegexOption.IGNORE_CASE,
            )

        @JvmStatic
        private val VERSION_PATTERN1 = Regex("^\\d\\.\\d\$")

        @JvmStatic
        private val VERSION_PATTERN2 = Regex("^\\d\\.\\d{1,2}\\.\\d{1,2}\\s*\$")

        @JvmStatic
        private val BEFORE_PATTERN = Regex("\\d/?$")

        @JvmStatic
        private val AFTER_PATTERN = Regex("^/?\\d")

        private const val OPENING_GROUP = 1
        private const val ENDING_GROUP = 5

        private const val FIRST_NUMBERS_GROUP = 2
        private const val SECOND_NUMBERS_GROUP = 3

        private const val YEAR_GROUP = 4
    }
}
