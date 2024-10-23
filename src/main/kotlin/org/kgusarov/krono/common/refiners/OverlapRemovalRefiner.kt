package org.kgusarov.krono.common.refiners

import org.kgusarov.krono.ParsedResult
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.Refiner

class OverlapRemovalRefiner : Refiner {
    override fun invoke(
        context: ParsingContext,
        results: MutableList<ParsedResult>,
    ): MutableList<ParsedResult> {
        if (results.size < 2) {
            return results
        }

        val filteredResults = mutableListOf<ParsedResult>()
        var prevResult = results[0]

        for (i in 1 until results.size) {
            val result = results[i]
            if (result.index >= prevResult.index + prevResult.text.length) {
                filteredResults.add(prevResult)
                prevResult = result
                continue
            }

            val (kept, removed) =
                if (result.text.length > prevResult.text.length) {
                    result to prevResult
                } else {
                    prevResult to result
                }

            context {
                "${javaClass.simpleName} removed $removed in favor of $kept"
            }

            prevResult = kept
        }

        filteredResults.add(prevResult)
        return filteredResults
    }
}
