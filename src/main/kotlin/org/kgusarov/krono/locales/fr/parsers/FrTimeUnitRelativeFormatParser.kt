package org.kgusarov.krono.locales.fr.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingComponents
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.parsers.AbstractParserWithWordBoundaryChecking
import org.kgusarov.krono.locales.fr.FrConstants
import org.kgusarov.krono.locales.fr.parseNumberPattern
import org.kgusarov.krono.utils.matchAnyPattern
import org.kgusarov.krono.utils.reverseDecimalTimeUnits
import java.math.BigDecimal

private val LAST = Regex("derni[eè]re?s?")
private val PAST = Regex("pass[ée]e?s?")
private val PREVIOUS = Regex("pr[ée]c[ée]dents?")

@SuppressFBWarnings("EI_EXPOSE_REP")
class FrTimeUnitRelativeFormatParser : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult? {
        val num = match[1]?.let { parseNumberPattern(it) } ?: BigDecimal.ONE
        val unit = FrConstants.TIME_UNIT_DICTIONARY[match[3]?.lowercase()]!!
        var timeUnits = mutableMapOf(unit to num)

        val modifier = (match[2] ?: match[4] ?: "").lowercase()
        if (modifier.isEmpty()) {
            return null
        }

        if (LAST.containsMatchIn(modifier) || PAST.containsMatchIn(modifier) || PREVIOUS.containsMatchIn(modifier)) {
            timeUnits = reverseDecimalTimeUnits(timeUnits)
        }

        val components = ParsingComponents.createRelativeFromDecimalReference(context.reference, timeUnits)
        return ParserResultFactory(components)
    }

    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "(?:les?|la|l'|du|des?)\\s*" +
                    "(${FrConstants.NUMBER_PATTERN})?" +
                    "(?:\\s*(prochaine?s?|derni[eè]re?s?|pass[ée]e?s?|pr[ée]c[ée]dents?|suivante?s?))?" +
                    "\\s*(${matchAnyPattern(FrConstants.TIME_UNIT_DICTIONARY)})" +
                    "(?:\\s*(prochaine?s?|derni[eè]re?s?|pass[ée]e?s?|pr[ée]c[ée]dents?|suivante?s?))?",
                RegexOption.IGNORE_CASE,
            )
    }
}
