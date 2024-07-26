package org.kgusarov.krono.common

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.ParsingResult
import org.kgusarov.krono.Refiner

@SuppressFBWarnings("BC_BAD_CAST_TO_ABSTRACT_COLLECTION")
abstract class Filter : Refiner {
    override fun invoke(
        context: ParsingContext,
        results: Array<ParsingResult>,
    ): Array<ParsingResult> =
        results.filter {
            isValid(context, it)
        }.toTypedArray()

    abstract fun isValid(
        context: ParsingContext,
        result: ParsingResult,
    ): Boolean
}

abstract class MergingRefiner : Refiner {
    override fun invoke(
        context: ParsingContext,
        results: Array<ParsingResult>,
    ): Array<ParsingResult> {
        val length = results.count()
        if (length < 2) {
            return results
        }

        val mergedResults = mutableListOf<ParsingResult>()
        var currentResult = results[0]
        var nextResult: ParsingResult?

        for (i in 1..<length) {
            nextResult = results[i]

            val textBetween = context.text.substring(currentResult.index + currentResult.text.length, nextResult.index)
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
        return mergedResults.toTypedArray()
    }

    abstract fun shouldMergeResults(
        textBetween: String,
        currentResult: ParsingResult,
        nextResult: ParsingResult,
        context: ParsingContext,
    ): Boolean

    abstract fun mergeResults(
        textBetween: String,
        currentResult: ParsingResult,
        nextResult: ParsingResult,
        context: ParsingContext,
    ): ParsingResult
}
