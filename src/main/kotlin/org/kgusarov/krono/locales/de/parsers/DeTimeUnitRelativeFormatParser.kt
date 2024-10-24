package org.kgusarov.krono.locales.de.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoDecimalTimeUnits
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingComponents
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.parsers.AbstractParserWithWordBoundaryChecking
import org.kgusarov.krono.locales.de.DeConstants
import org.kgusarov.krono.locales.de.parseNumberPattern
import org.kgusarov.krono.utils.matchAnyPattern
import org.kgusarov.krono.utils.reverseDecimalTimeUnits
import java.math.BigDecimal

private val REVERSE_MODIFIER = Regex("vor|letzte|vergangen")

@SuppressFBWarnings("EI_EXPOSE_REP")
class DeTimeUnitRelativeFormatParser : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult? {
        val num =
            if (match[2].isNullOrEmpty()) {
                BigDecimal.ONE
            } else {
                parseNumberPattern(match[2]!!)
            }

        val unit = DeConstants.TIME_UNIT_DICTIONARY[match[4]!!.lowercase()]!!
        var timeUnits: KronoDecimalTimeUnits = mutableMapOf(unit to num)

        val modifier =
            when {
                !match[1].isNullOrEmpty() -> match[1]!!.lowercase()
                !match[3].isNullOrEmpty() -> match[3]!!.lowercase()
                else -> ""
            }

        if (modifier.isEmpty()) {
            return null
        }

        if (REVERSE_MODIFIER.containsMatchIn(modifier)) {
            timeUnits = reverseDecimalTimeUnits(timeUnits)
        }

        val result = ParsingComponents.createRelativeFromDecimalReference(context.reference, timeUnits)
        return ParserResultFactory(result)
    }

    @Suppress("RegExpSingleCharAlternation")
    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "(?:\\s*((?:nächste|kommende|folgende|letzte|vergangene|vorige|vor(?:her|an)gegangene)(?:s|n|m|r)?|vor|in)\\s*)?" +
                    "(${DeConstants.NUMBER_PATTERN})?" +
                    "(?:\\s*(nächste|kommende|folgende|letzte|vergangene|vorige|vor(?:her|an)gegangene)(?:s|n|m|r)?)?" +
                    "\\s*(${matchAnyPattern(DeConstants.TIME_UNIT_DICTIONARY)})",
                RegexOption.IGNORE_CASE,
            )
    }
}
