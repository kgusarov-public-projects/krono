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
class ENTimeUnitWithinFormatParser(
    private val strictMode: Boolean,
) : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext): Regex {
        return when {
            strictMode -> PATTERN_WITH_PREFIX_STRICT
            context.option.forwardDate -> PATTERN_WITH_OPTIONAL_PREFIX
            else -> PATTERN_WITH_PREFIX
        }
    }

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult? {
        if (match[0]!!.matches(EXCLUDE_PATTERN)) {
            return null
        }

        val timeUnits = parseTimeUnits(match[1]!!)
        val components = ParsingComponents.createRelativeFromDecimalReference(context.reference, timeUnits)
        return ParserResultFactory(components)
    }

    companion object {
        @JvmStatic
        private val EXCLUDE_PATTERN = Regex("^for\\s*the\\s*\\w+", RegexOption.IGNORE_CASE)

        @JvmStatic
        private val PATTERN_WITH_OPTIONAL_PREFIX =
            Regex(
                "(?:(?:within|in|for)\\s*)?" +
                    "(?:(?:about|around|roughly|approximately|just)\\s*(?:~\\s*)?)?" +
                    "(${EnConstants.TIME_UNITS_PATTERN})(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )

        @JvmStatic
        private val PATTERN_WITH_PREFIX =
            Regex(
                "(?:within|in|for)\\s*" +
                    "(?:(?:about|around|roughly|approximately|just)\\s*(?:~\\s*)?)?" +
                    "(${EnConstants.TIME_UNITS_PATTERN})(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )

        @JvmStatic
        private val PATTERN_WITH_PREFIX_STRICT =
            Regex(
                "(?:within|in|for)\\s*" +
                    "(?:(?:about|around|roughly|approximately|just)\\s*(?:~\\s*)?)?" +
                    "(${EnConstants.TIME_UNITS_NO_ABBR_PATTERN})(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )
    }
}
