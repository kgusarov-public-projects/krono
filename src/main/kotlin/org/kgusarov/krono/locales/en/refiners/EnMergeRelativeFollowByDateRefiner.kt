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

private val IMPLIED_EARLIER_REFERENCE_DATE =
    Regex(
        "\\s+(before|from)$",
        RegexOption.IGNORE_CASE,
    )

private val IMPLIED_LATER_REFERENCE_DATE =
    Regex(
        "\\s+(after|since)$",
        RegexOption.IGNORE_CASE,
    )

private fun hasImpliedEarlierReferenceDate(result: ParsedResult): Boolean = IMPLIED_EARLIER_REFERENCE_DATE.containsMatchIn(result.text)

private fun hasImpliedLaterReferenceDate(result: ParsedResult): Boolean = IMPLIED_LATER_REFERENCE_DATE.containsMatchIn(result.text)

@SuppressFBWarnings("EI_EXPOSE_REP")
class EnMergeRelativeFollowByDateRefiner : MergingRefiner() {
    override fun shouldMergeResults(
        textBetween: String,
        currentResult: ParsedResult,
        nextResult: ParsedResult,
        context: ParsingContext,
    ): Boolean {
        if (!EXPECTED_TEXT_BETWEEN.containsMatchIn(textBetween)) {
            return false
        }

        if (!hasImpliedEarlierReferenceDate(currentResult) && !hasImpliedLaterReferenceDate(currentResult)) {
            return false
        }

        return nextResult.start.day() != null && nextResult.start.month() != null && nextResult.start.year() != null
    }

    override fun mergeResults(
        textBetween: String,
        currentResult: ParsedResult,
        nextResult: ParsedResult,
        context: ParsingContext,
    ): ParsedResult {
        var timeUnits = parseTimeUnits(currentResult.text) ?: return currentResult
        if (hasImpliedEarlierReferenceDate(currentResult)) {
            timeUnits = reverseDecimalTimeUnits(timeUnits)
        }

        val components =
            ParsingComponents.createRelativeFromDecimalReference(
                ReferenceWithTimezone(nextResult.start.instant()),
                timeUnits,
            )

        return ParsingResult(
            nextResult.reference,
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
