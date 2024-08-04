package org.kgusarov.krono.locales.en.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingComponents
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.parsers.AbstractParserWithWordBoundaryChecking
import org.kgusarov.krono.locales.en.EnConstants
import org.kgusarov.krono.locales.en.parseTimeUnits
import org.kgusarov.krono.utils.reverseDecimalTimeUnits

@SuppressFBWarnings("EI_EXPOSE_REP")
class ENTimeUnitCasualRelativeFormatParser(
    private val allowAbbreviations: Boolean = true,
) : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = if (allowAbbreviations) PATTERN else PATTERN_NO_ABBR

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult {
        val prefix = match[1]!!.lowercase()
        var timeUnits = parseTimeUnits(match[2]!!)
        when (prefix) {
            "last", "past", "-" -> timeUnits = reverseDecimalTimeUnits(timeUnits)
        }

        val components = ParsingComponents.createRelativeFromDecimalReference(context.reference, timeUnits)
        return ParserResultFactory(components)
    }

    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "(this|last|past|next|after|\\+|-)\\s*(${EnConstants.TIME_UNITS_PATTERN})(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )

        @JvmStatic
        private val PATTERN_NO_ABBR =
            Regex(
                "(this|last|past|next|after|\\+|-)\\s*(${EnConstants.TIME_UNITS_NO_ABBR_PATTERN})(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )
    }
}
