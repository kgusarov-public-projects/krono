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

@SuppressFBWarnings("EI_EXPOSE_REP")
class EnTimeUnitLaterFormatParser(
    private val strictMode: Boolean,
) : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = if (strictMode) STRICT_PATTERN else PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult? {
        val timeUnits = parseTimeUnits(match[GROUP_NUM_TIMEUNITS]!!) ?: return null
        val components = ParsingComponents.createRelativeFromDecimalReference(context.reference, timeUnits)

        return ParserResultFactory(components)
    }

    @Suppress("RegExpUnnecessaryNonCapturingGroup")
    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "(${EnConstants.TIME_UNITS_PATTERN})\\s{0,5}(?:later|after|from now|henceforth|forward|out)(?=(?:\\W|$))",
                RegexOption.IGNORE_CASE,
            )

        @JvmStatic
        private val STRICT_PATTERN =
            Regex(
                "(${EnConstants.TIME_UNITS_NO_ABBR_PATTERN})\\s{0,5}(later|after|from now)(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )

        private const val GROUP_NUM_TIMEUNITS = 1
    }
}
