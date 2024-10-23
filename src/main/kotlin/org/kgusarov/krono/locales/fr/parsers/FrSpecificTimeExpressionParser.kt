package org.kgusarov.krono.locales.fr.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.KronoMeridiem
import org.kgusarov.krono.ParsedComponents
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.TextOrEndIndexInputFactory
import org.kgusarov.krono.common.parsers.TimeExpressionParserSupport
import org.kgusarov.krono.extensions.compareTo
import org.kgusarov.krono.extensions.plus
import org.kgusarov.krono.extensions.safeParseInt

private val YEAR_LIKE = Regex("^\\d{4}$")

@SuppressFBWarnings("EI_EXPOSE_REP", "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
class FrSpecificTimeExpressionParser : TimeExpressionParserSupport() {
    override fun pattern(context: ParsingContext) = FIRST_REG_PATTERN

    override fun invoke(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult? {
        val index = match.index + match[1]!!.length
        val text = match[0]!!.substring(match[1]!!.length)
        val result = context.createParsingResult(index, TextOrEndIndexInputFactory(text))

        if (text.matches(YEAR_LIKE)) {
            match.index += match[0]!!.length
            return null
        }

        val extracted = extractTimeComponent(result.start.copy(), match)
        if (extracted == null) {
            match.index += match[0]!!.length
            return null
        } else {
            result.start = extracted
        }

        val remainingText = context.text.substring(match.index + match[0]!!.length)
        val secondMatchResult = SECOND_REG_PATTERN.find(remainingText)
        if (secondMatchResult != null) {
            val secondMatch = RegExpMatchArray(secondMatchResult)
            result.end = extractTimeComponent(result.start.copy(), secondMatch)
            if (result.end != null) {
                result.text += secondMatch[0]!!
            }
        }

        return ParserResultFactory(result)
    }

    private fun extractTimeComponent(
        extractingComponents: ParsedComponents,
        match: RegExpMatchArray,
    ): ParsedComponents? {
        val hour = match[HOUR_GROUP]!!.safeParseInt()
        var minute: Int? = 0
        var meridiem: Int? = null

        if (!match[MINUTE_GROUP].isNullOrEmpty()) {
            minute = match[MINUTE_GROUP]!!.safeParseInt()
        }

        if (minute!! >= 60 || hour > 24) {
            return null
        }

        if (hour >= 12) {
            meridiem = KronoMeridiem.PM
        }

        if (processTimeComponents(extractingComponents, match, hour, minute, meridiem, AM_PM_HOUR_GROUP) == null) {
            return null
        }

        if (!match[SECOND_GROUP].isNullOrEmpty()) {
            val second = match[SECOND_GROUP]!!.safeParseInt()
            if (second >= 60) {
                return null
            }

            extractingComponents.assign(KronoComponents.Second, second)
        }

        return extractingComponents
    }

    @Suppress(
        "RegExpSingleCharAlternation",
        "RegExpUnnecessaryNonCapturingGroup",
        "RegExpRedundantEscape",
    )
    companion object {
        @JvmStatic
        private val FIRST_REG_PATTERN =
            Regex(
                "(^|\\s|T)" +
                    "(?:(?:[àa])\\s*)?" +
                    "(\\d{1,2})(?:h|:)?" +
                    "(?:(\\d{1,2})(?:m|:)?)?" +
                    "(?:(\\d{1,2})(?:s|:)?)?" +
                    "(?:\\s*(A\\.M\\.|P\\.M\\.|AM?|PM?))?" +
                    "(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )

        @JvmStatic
        private val SECOND_REG_PATTERN =
            Regex(
                "^\\s*(\\-|\\–|\\~|\\〜|[àa]|\\?)\\s*" +
                    "(\\d{1,2})(?:h|:)?" +
                    "(?:(\\d{1,2})(?:m|:)?)?" +
                    "(?:(\\d{1,2})(?:s|:)?)?" +
                    "(?:\\s*(A\\.M\\.|P\\.M\\.|AM?|PM?))?" +
                    "(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )

        private const val HOUR_GROUP = 2
        private const val MINUTE_GROUP = 3
        private const val SECOND_GROUP = 4
        private const val AM_PM_HOUR_GROUP = 5
    }
}
