package org.kgusarov.krono.locales.en.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.TextOrEndIndexInputFactory
import org.kgusarov.krono.calculation.findYearClosestToRef
import org.kgusarov.krono.common.parsers.AbstractParserWithWordBoundaryChecking
import org.kgusarov.krono.locales.en.EnConstants
import org.kgusarov.krono.locales.en.parseOrdinalNumberPattern
import org.kgusarov.krono.locales.en.parseYear
import org.kgusarov.krono.utils.matchAnyPattern

@SuppressFBWarnings("EI_EXPOSE_REP")
class ENMonthNameLittleEndianParser : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult? {
        val result = context.createParsingResult(match.index!!, TextOrEndIndexInputFactory(match[0]!!))
        val month = EnConstants.MONTH_DICTIONARY[match[MONTH_NAME_GROUP]!!.lowercase()]
        val day = parseOrdinalNumberPattern(match[DATE_GROUP]!!)

        if (day > 31) {
            match.index = match.index!! + match[DATE_GROUP]!!.length
            return null
        }

        result.start.assign(KronoComponents.Month, month)
        result.start.assign(KronoComponents.Day, day)

        if (match[YEAR_GROUP] != null) {
            val yearNumber = parseYear(match[YEAR_GROUP]!!)
            result.start.assign(KronoComponents.Year, yearNumber)
        } else {
            val year = findYearClosestToRef(context.instant, day, month!!)
            result.start.imply(KronoComponents.Year, year)
        }

        if (match[DATE_TO_GROUP] != null) {
            val endDate = parseOrdinalNumberPattern(match[DATE_TO_GROUP]!!)
            result.end = result.start.copy()
            result.end!!.assign(KronoComponents.Day, endDate)
        }

        return ParserResultFactory(result)
    }

    @Suppress("RegExpRedundantEscape")
    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "(?:on\\s{0,3})?" +
                    "(${EnConstants.ORDINAL_NUMBER_PATTERN})" +
                    "(?:" +
                    "\\s{0,3}(?:to|\\-|\\–|until|through|till)?\\s{0,3}" +
                    "(${EnConstants.ORDINAL_NUMBER_PATTERN})" +
                    ")?" +
                    "(?:-|/|\\s{0,3}(?:of)?\\s{0,3})" +
                    "(${matchAnyPattern(EnConstants.MONTH_DICTIONARY)})" +
                    "(?:" +
                    "(?:-|/|,?\\s{0,3})" +
                    "(${EnConstants.YEAR_PATTERN}(?!\\w))" +
                    ")?" +
                    "(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )

        private const val DATE_GROUP = 1
        private const val DATE_TO_GROUP = 2
        private const val MONTH_NAME_GROUP = 3
        private const val YEAR_GROUP = 4
    }
}
