package org.kgusarov.krono.common.refiners

import org.kgusarov.krono.ParsedResult
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.calculation.mergeDateTimeResult
import org.kgusarov.krono.common.MergingRefiner

abstract class AbstractMergeDateTimeRefiner : MergingRefiner() {
    override fun shouldMergeResults(
        textBetween: String,
        currentResult: ParsedResult,
        nextResult: ParsedResult,
        context: ParsingContext,
    ): Boolean {
        val crs = currentResult.start
        val nrs = nextResult.start

        return ((crs.onlyDate && nrs.onlyTime) || (nrs.onlyDate && crs.onlyTime)) &&
            patternBetween().matches(textBetween)
    }

    override fun mergeResults(
        textBetween: String,
        currentResult: ParsedResult,
        nextResult: ParsedResult,
        context: ParsingContext,
    ): ParsedResult {
        val result =
            if (currentResult.start.onlyDate) {
                mergeDateTimeResult(currentResult, nextResult)
            } else {
                mergeDateTimeResult(nextResult, currentResult)
            }

        result.index = currentResult.index
        result.text = currentResult.text + textBetween + nextResult.text
        return result
    }

    abstract fun patternBetween(): Regex
}
