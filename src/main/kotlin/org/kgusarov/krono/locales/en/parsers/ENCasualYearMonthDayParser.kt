package org.kgusarov.krono.locales.en.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.parsers.AbstractParserWithWordBoundaryChecking
import org.kgusarov.krono.extensions.compareTo
import org.kgusarov.krono.locales.en.EnConstants
import org.kgusarov.krono.utils.matchAnyPattern

@SuppressFBWarnings("EI_EXPOSE_REP")
class ENCasualYearMonthDayParser : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult? {
        val month =
            match.getIntOrNull(MONTH_NUMBER_GROUP) ?: EnConstants.MONTH_DICTIONARY[match[MONTH_NAME_GROUP]!!.lowercase()]

        if (month < 1 || month > 12) {
            return null
        }

        val year = match.getInt(YEAR_NUMBER_GROUP)
        val day = match.getInt(DATE_NUMBER_GROUP)

        return ParserResultFactory(
            KronoComponents.Day to day,
            KronoComponents.Month to month,
            KronoComponents.Year to year,
        )
    }

    @Suppress("RegExpRedundantEscape")
    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "([0-9]{4})[\\.\\/\\s]" +
                    "(?:(${matchAnyPattern(EnConstants.MONTH_DICTIONARY)})|([0-9]{1,2}))[\\.\\/\\s]" +
                    "([0-9]{1,2})" +
                    "(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )

        private const val YEAR_NUMBER_GROUP = 1
        private const val MONTH_NAME_GROUP = 2
        private const val MONTH_NUMBER_GROUP = 3
        private const val DATE_NUMBER_GROUP = 4
    }
}
