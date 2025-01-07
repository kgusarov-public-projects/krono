package org.kgusarov.krono.common.parsers

import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.ParsedComponents
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.TextOrEndIndexInputFactory
import org.kgusarov.krono.calculation.findYearClosestToRef
import org.kgusarov.krono.common.ParseOrdinalNumberPattern
import org.kgusarov.krono.common.ParseYear

abstract class AbstractMonthNameMiddleEndianParser : AbstractParserWithWordBoundaryChecking() {
    protected fun parse(
        context: ParsingContext,
        match: RegExpMatchArray,
        components: ParsedComponents,
        yearGroup: Int,
        dateToGroup: Int,
        day: Int,
        month: Int?,
        parseOrdinalNumberPattern: ParseOrdinalNumberPattern,
        parseYear: ParseYear,
    ): ParserResult {
        if (!match[yearGroup].isNullOrEmpty()) {
            val year = parseYear(match[yearGroup]!!)
            components.assign(KronoComponents.Year, year)
        } else {
            val year = findYearClosestToRef(context.instant, day, month!!)
            components.imply(KronoComponents.Year, year)
        }

        if (match[dateToGroup].isNullOrEmpty()) {
            return ParserResultFactory(components)
        }

        val endDate = parseOrdinalNumberPattern(match[dateToGroup]!!)
        val result =
            context.createParsingResult(
                match.index!!,
                TextOrEndIndexInputFactory(match[0]!!),
            )

        result.start = components
        result.end = components.copy()
        result.end!!.assign(KronoComponents.Day, endDate)

        return ParserResultFactory(result)
    }
}
