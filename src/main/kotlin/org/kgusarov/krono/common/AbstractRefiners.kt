package org.kgusarov.krono.common

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.ParsedResult
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.Refiner

@SuppressFBWarnings("BC_BAD_CAST_TO_ABSTRACT_COLLECTION")
abstract class Filter : Refiner {
    override fun invoke(
        context: ParsingContext,
        results: MutableList<ParsedResult>,
    ): MutableList<ParsedResult> =
        results.filter {
            isValid(context, it)
        }.toMutableList()

    abstract fun isValid(
        context: ParsingContext,
        result: ParsedResult,
    ): Boolean
}

abstract class MergingRefiner : Refiner {
    override fun invoke(
        context: ParsingContext,
        results: MutableList<ParsedResult>,
    ): MutableList<ParsedResult> {
        val length = results.count()
        if (length < 2) {
            return results
        }

        val mergedResults = mutableListOf<ParsedResult>()
        var currentResult = results[0]
        var nextResult: ParsedResult?

        for (i in 1..<length) {
            nextResult = results[i]

            val textBetween =
                context.text.substring(
                    currentResult.index + currentResult.text.length,
                    nextResult.index,
                )

            if (!shouldMergeResults(textBetween, currentResult, nextResult, context)) {
                mergedResults += currentResult
                currentResult = nextResult
            } else {
                val mergedResult = mergeResults(textBetween, currentResult, nextResult, context)
                context {
                    "${javaClass.simpleName} merged $currentResult and $nextResult into $mergedResult"
                }

                currentResult = mergedResult
            }
        }

        mergedResults += currentResult
        return mergedResults
    }

    abstract fun shouldMergeResults(
        textBetween: String,
        currentResult: ParsedResult,
        nextResult: ParsedResult,
        context: ParsingContext,
    ): Boolean

    abstract fun mergeResults(
        textBetween: String,
        currentResult: ParsedResult,
        nextResult: ParsedResult,
        context: ParsingContext,
    ): ParsedResult
}
