package org.kgusarov.krono.common.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.TextOrEndIndexInputFactory
import org.kgusarov.krono.calculation.findYearClosestToRef
import org.kgusarov.krono.common.ParseOrdinalNumberPattern
import org.kgusarov.krono.common.ParseYear

@SuppressFBWarnings("EI_EXPOSE_REP")
abstract class CommonMonthNameLittleEndianParser : AbstractParserWithWordBoundaryChecking() {
    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult? {
        val parseOrdinalNumberPattern = getParseOrdinalNumberPattern()
        val monthDictionary = getMonthDictionary()
        val parseYear = getParseYear()
        val monthNameGroup = getMonthNameGroup()
        val yearGroup = getYearGroup()
        val dateGroup = getDateGroup()
        val dateToGroup = getDateToGroup()

        val result = context.createParsingResult(match.index!!, TextOrEndIndexInputFactory(match[0]!!))
        val month = monthDictionary[match[monthNameGroup]!!.lowercase()]
        val day = parseOrdinalNumberPattern(match[dateGroup]!!)

        if (day > 31) {
            match.index = match.index!! + match[dateGroup]!!.length
            return null
        }

        result.start.assign(KronoComponents.Month, month)
        result.start.assign(KronoComponents.Day, day)

        if (!match[yearGroup].isNullOrEmpty()) {
            val yearNumber = parseYear(match[yearGroup]!!)
            result.start.assign(KronoComponents.Year, yearNumber)
        } else {
            val year = findYearClosestToRef(context.instant, day, month!!)
            result.start.imply(KronoComponents.Year, year)
        }

        if (!match[dateToGroup].isNullOrEmpty()) {
            val endDate = parseOrdinalNumberPattern(match[dateToGroup]!!)
            result.end = result.start.copy()
            result.end!!.assign(KronoComponents.Day, endDate)
        }

        return ParserResultFactory(result)
    }

    abstract fun getMonthDictionary(): Map<String, Int>

    abstract fun getParseOrdinalNumberPattern(): ParseOrdinalNumberPattern

    abstract fun getParseYear(): ParseYear

    abstract fun getMonthNameGroup(): Int

    abstract fun getYearGroup(): Int

    abstract fun getDateGroup(): Int

    abstract fun getDateToGroup(): Int
}
