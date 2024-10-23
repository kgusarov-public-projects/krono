package org.kgusarov.krono.common.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.TextOrEndIndexInputFactory
import org.kgusarov.krono.calculation.findYearClosestToRef
import org.kgusarov.krono.common.ParseYear
import org.kgusarov.krono.extensions.compareTo
import org.kgusarov.krono.extensions.safeParseInt
import org.kgusarov.krono.utils.matchAnyPattern

@SuppressFBWarnings("EI_EXPOSE_REP")
abstract class IberoRomanMonthNameLittleEndianParser : AbstractParserWithWordBoundaryChecking() {
    @Suppress("RegExpSingleCharAlternation", "RegExpRedundantEscape")
    override fun innerPattern(context: ParsingContext) =
        Regex(
            "([0-9]{1,2})(?:º|ª|°)?" +
                "(?:\\s*(?:desde|de|\\-|\\–|ao?|\\s)\\s*([0-9]{1,2})(?:º|ª|°)?)?\\s*(?:de)?\\s*" +
                "(?:-|/|\\s*(?:de|,)?\\s*)" +
                "(${matchAnyPattern(getMonthDictionary())})" +
                "(?:\\s*(?:de|,)?\\s*(${getYearPattern()}))?" +
                "(?=\\W|$)",
            RegexOption.IGNORE_CASE,
        )

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult? {
        val result = context.createParsingResult(match.index!!, TextOrEndIndexInputFactory(match[0]!!))
        val month = getMonthDictionary()[match[MONTH_NAME_GROUP]!!.lowercase()]
        val day = match[DATE_GROUP]!!.safeParseInt()

        if (day > 31) {
            match.index = match.index!! + match[DATE_GROUP]!!.length
            return null
        }

        result.start.assign(KronoComponents.Month, month)
        result.start.assign(KronoComponents.Day, day)

        if (!match[YEAR_GROUP].isNullOrEmpty()) {
            val yearNumber = getParseYear()(match[YEAR_GROUP]!!)
            result.start.assign(KronoComponents.Year, yearNumber)
        } else {
            val year = findYearClosestToRef(context.instant, day!!, month!!)
            result.start.imply(KronoComponents.Year, year)
        }

        if (!match[DATE_TO_GROUP].isNullOrEmpty()) {
            val endDate = match[DATE_TO_GROUP]!!.safeParseInt()
            result.end = result.start.copy()
            result.end!!.assign(KronoComponents.Day, endDate)
        }

        return ParserResultFactory(result)
    }

    abstract fun getMonthDictionary(): Map<String, Int>

    abstract fun getYearPattern(): String

    abstract fun getParseYear(): ParseYear

    companion object {
        private const val DATE_GROUP = 1
        private const val DATE_TO_GROUP = 2
        private const val MONTH_NAME_GROUP = 3
        private const val YEAR_GROUP = 4
    }
}
