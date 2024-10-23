package org.kgusarov.krono.common.refiners

import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.KronoUnit
import org.kgusarov.krono.ParsedResult
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.ParsingResult
import org.kgusarov.krono.common.MergingRefiner
import org.kgusarov.krono.extensions.add
import kotlin.math.min

abstract class AbstractMergeDateRangeRefiner : MergingRefiner() {
    override fun shouldMergeResults(
        textBetween: String,
        currentResult: ParsedResult,
        nextResult: ParsedResult,
        context: ParsingContext,
    ): Boolean =
        (currentResult.end == null) &&
            (nextResult.end == null) &&
            patternBetween().matches(textBetween)

    override fun mergeResults(
        textBetween: String,
        currentResult: ParsedResult,
        nextResult: ParsedResult,
        context: ParsingContext,
    ): ParsedResult {
        var fromResult = currentResult
        var toResult = nextResult

        if (!fromResult.start.onlyWeekday() && !toResult.start.onlyWeekday()) {
            toResult.start.certainComponents().forEach {
                if (!fromResult.start.isCertain(it)) {
                    fromResult.start.imply(it, toResult.start[it])
                }
            }

            fromResult.start.certainComponents().forEach {
                if (!toResult.start.isCertain(it)) {
                    toResult.start.imply(it, fromResult.start[it])
                }
            }
        }

        if (fromResult.start.instant().isAfter(toResult.start.instant())) {
            var fromMoment = fromResult.start.instant()
            var toMoment = toResult.start.instant()

            if (toResult.start.onlyWeekday() && toMoment.add(KronoUnit.Day, 7).isAfter(fromMoment)) {
                toMoment = toMoment.add(KronoUnit.Day, 7)

                toResult.start.imply(KronoComponents.Day, toMoment.dayOfMonth)
                toResult.start.imply(KronoComponents.Month, toMoment.monthValue)
                toResult.start.imply(KronoComponents.Year, toMoment.year)
            } else if (fromResult.start.onlyWeekday() && fromMoment.add(KronoUnit.Day, -7).isBefore(toMoment)) {
                fromMoment = fromMoment.add(KronoUnit.Day, -7)

                fromResult.start.imply(KronoComponents.Day, fromMoment.dayOfMonth)
                fromResult.start.imply(KronoComponents.Month, fromMoment.monthValue)
                fromResult.start.imply(KronoComponents.Year, fromMoment.year)
            } else if (
                toResult.start.dateWithUnknownYear() &&
                toMoment.add(KronoUnit.Year, 1).isAfter(fromMoment)
            ) {
                toMoment = toMoment.add(KronoUnit.Year, 1)
                toResult.start.imply(KronoComponents.Year, toMoment.year)
            } else if (
                fromResult.start.dateWithUnknownYear() &&
                fromMoment.add(KronoUnit.Year, -1).isBefore(toMoment)
            ) {
                fromMoment = fromMoment.add(KronoUnit.Year, -1)
                fromResult.start.imply(KronoComponents.Year, fromMoment.year)
            } else {
                toResult = fromResult.also { fromResult = toResult }
            }
        }

        val result = fromResult.copy<ParsingResult>()
        result.start = fromResult.start
        result.end = toResult.start
        result.index = min(fromResult.index, toResult.index)

        if (fromResult.index < toResult.index) {
            result.text = fromResult.text + textBetween + toResult.text
        } else {
            result.text = toResult.text + textBetween + fromResult.text
        }

        return result
    }

    abstract fun patternBetween(): Regex
}
