package org.kgusarov.krono.common.refiners

import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.ParsedResult
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.common.MergingRefiner

class MergeWeekdayComponentRefiner : MergingRefiner() {
    override fun shouldMergeResults(
        textBetween: String,
        currentResult: ParsedResult,
        nextResult: ParsedResult,
        context: ParsingContext,
    ): Boolean {
        val weekdayThenNormalDate =
            currentResult.start.onlyWeekday &&
                !currentResult.start.isCertain(KronoComponents.Hour) &&
                nextResult.start.isCertain(KronoComponents.Day)

        return weekdayThenNormalDate && textBetween.matches(Regex("^,?\\s*$"))
    }

    override fun mergeResults(
        textBetween: String,
        currentResult: ParsedResult,
        nextResult: ParsedResult,
        context: ParsingContext,
    ): ParsedResult {
        val newResult = nextResult.copy<ParsedResult>()
        newResult.index = currentResult.index
        newResult.text = currentResult.text + textBetween + newResult.text

        newResult.start.assign(KronoComponents.Weekday, currentResult.start[KronoComponents.Weekday])
        if (newResult.end != null) {
            newResult.end!!.assign(KronoComponents.Weekday, currentResult.start[KronoComponents.Weekday])
        }

        return newResult
    }
}
