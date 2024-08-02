package org.kgusarov.krono.common.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.Parser
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.TextOrEndIndexInputFactory
import org.kgusarov.krono.calculation.findMostLikelyADYear
import org.kgusarov.krono.extensions.compareTo
import org.kgusarov.krono.extensions.minus
import org.kgusarov.krono.extensions.not
import org.kgusarov.krono.extensions.plus
import org.kgusarov.krono.extensions.substr

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
        val openingGroup = match[OPENING_GROUP]!!
        val matchIndex = match.index
        if (openingGroup.isEmpty() && matchIndex > 0 && matchIndex < context.text.length) {
            val previousChar = context.text[matchIndex - 1]
            if (previousChar in '0'..'9') {
                return null
            }
        }

        val text =
            match[0]!!.substr(
                openingGroup.length,
                match[0]!!.length - openingGroup.length - match[ENDING_GROUP]!!.length,
            )

        if (text.matches(VERSION_PATTERN1) || text.matches(VERSION_PATTERN2)) {
            return null
        }

        if (!match[YEAR_GROUP] && text.indexOf('/') < 0) {
            return null
        }

        val index = matchIndex + openingGroup.length
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
            val year = context.instant.year
            result.start.imply(KronoComponents.Year, year)
        } else {
            val rawYearNumber = match.getInt(YEAR_GROUP)
            val year = findMostLikelyADYear(rawYearNumber)
            result.start.assign(KronoComponents.Year, year)
        }

        return ParserResultFactory(result)
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

        private const val OPENING_GROUP = 1
        private const val ENDING_GROUP = 5

        private const val FIRST_NUMBERS_GROUP = 2
        private const val SECOND_NUMBERS_GROUP = 3

        private const val YEAR_GROUP = 4
    }
}
