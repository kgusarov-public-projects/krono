package org.kgusarov.krono.locales.en.refiners

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.ParsedResult
import org.kgusarov.krono.ParsingComponents
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.ParsingResult
import org.kgusarov.krono.ReferenceWithTimezone
import org.kgusarov.krono.common.MergingRefiner
import org.kgusarov.krono.locales.en.parseTimeUnits
import org.kgusarov.krono.utils.reverseDecimalTimeUnits

private val POSITIVE_FOLLOWING_REFERENCE =
    Regex(
        "^[+-]",
        RegexOption.IGNORE_CASE,
    )

private val NEGATIVE_FOLLOWING_REFERENCE =
    Regex(
        "^-",
        RegexOption.IGNORE_CASE,
    )

private fun isPositiveFollowingReference(result: ParsedResult) = POSITIVE_FOLLOWING_REFERENCE.containsMatchIn(result.text)

private fun isNegativeFollowingReference(result: ParsedResult) = NEGATIVE_FOLLOWING_REFERENCE.containsMatchIn(result.text)

@SuppressFBWarnings("EI_EXPOSE_REP")
class EnMergeRelativeAfterDateRefiner : MergingRefiner() {
    override fun shouldMergeResults(
        textBetween: String,
        currentResult: ParsedResult,
        nextResult: ParsedResult,
        context: ParsingContext,
    ): Boolean {
        if (!EXPECTED_TEXT_BETWEEN.containsMatchIn(textBetween)) {
            return false
        }

        return isPositiveFollowingReference(nextResult) || isNegativeFollowingReference(nextResult)
    }

    override fun mergeResults(
        textBetween: String,
        currentResult: ParsedResult,
        nextResult: ParsedResult,
        context: ParsingContext,
    ): ParsedResult {
        var timeUnits = parseTimeUnits(nextResult.text)
        if (isNegativeFollowingReference(nextResult)) {
            timeUnits = reverseDecimalTimeUnits(timeUnits)
        }

        val components =
            ParsingComponents.createRelativeFromDecimalReference(
                ReferenceWithTimezone(currentResult.start.instant()),
                timeUnits,
            )

        return ParsingResult(
            currentResult.reference,
            currentResult.index,
            "${currentResult.text}${textBetween}${nextResult.text}",
            components,
            null,
        )
    }

    companion object {
        @JvmStatic
        private val EXPECTED_TEXT_BETWEEN =
            Regex(
                "^\\s*$",
                RegexOption.IGNORE_CASE,
            )
    }
}
