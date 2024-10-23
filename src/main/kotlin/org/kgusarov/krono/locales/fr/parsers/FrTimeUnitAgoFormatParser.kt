package org.kgusarov.krono.locales.fr.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingComponents
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.parsers.AbstractParserWithWordBoundaryChecking
import org.kgusarov.krono.locales.fr.FrConstants
import org.kgusarov.krono.locales.fr.parseTimeUnits
import org.kgusarov.krono.utils.reverseDecimalTimeUnits

@SuppressFBWarnings("EI_EXPOSE_REP")
class FrTimeUnitAgoFormatParser : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult {
        val timeUnits = parseTimeUnits(match[1]!!)
        val outputTimeUnits = reverseDecimalTimeUnits(timeUnits)
        val components = ParsingComponents.createRelativeFromDecimalReference(context.reference, outputTimeUnits)

        return ParserResultFactory(components)
    }

    @Suppress("RegExpUnnecessaryNonCapturingGroup")
    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "il y a\\s*(${FrConstants.TIME_UNITS_PATTERN})(?=(?:\\W|$))",
                RegexOption.IGNORE_CASE,
            )
    }
}
