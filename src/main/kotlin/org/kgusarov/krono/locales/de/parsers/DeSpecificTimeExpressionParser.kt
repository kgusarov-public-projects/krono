package org.kgusarov.krono.locales.de.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.KronoMeridiem
import org.kgusarov.krono.ParsedComponents
import org.kgusarov.krono.Parser
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.TextOrEndIndexInputFactory
import org.kgusarov.krono.common.parsers.TimeExpressionParserSupport.Companion.processMeridiem
import org.kgusarov.krono.extensions.compareTo
import org.kgusarov.krono.extensions.plus
import org.kgusarov.krono.extensions.safeParseInt

private val AM = Regex("morgen|vormittag")
private val PM = Regex("nachmittag|abend")
private val NIGHT = Regex("nacht")
private val YEAR = Regex("^\\d{4}$")

@Suppress("RegExpSingleCharAlternation", "RegExpUnnecessaryNonCapturingGroup", "RegExpRedundantEscape")
@SuppressFBWarnings("EI_EXPOSE_REP")
class DeSpecificTimeExpressionParser : Parser {
    override fun pattern(context: ParsingContext): Regex = FIRST_REG_PATTERN

    override fun invoke(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult? {
        val result =
            context.createParsingResult(
                match.index!! + match[1]!!.length,
                TextOrEndIndexInputFactory(match[0]!!.substring(match[1]!!.length)),
            )

        if (result.text.matches(YEAR)) {
            match.index += match[0]!!.length
            return null
        }

        val newStart = extractTimeComponent(result.start.copy(), match)
        if (newStart == null) {
            match.index += match[0]!!.length
            return null
        } else {
            result.start = newStart
        }

        val remainingText = context.text.substring(match.index!! + match[0]!!.length)
        val secondMatch = SECOND_REG_PATTERN.find(remainingText)
        if (secondMatch != null) {
            val secondMatchResult = RegExpMatchArray(secondMatch)
            val newEnd = extractTimeComponent(result.start.copy(), secondMatchResult)
            if (newEnd != null) {
                result.end = newEnd
                result.text += secondMatchResult[0]!!
            }
        }

        return ParserResultFactory(result)
    }

    companion object {
        @JvmStatic
        private val FIRST_REG_PATTERN =
            Regex(
                "(^|\\s|T)" +
                    "(?:(?:um|von)\\s*)?" +
                    "(\\d{1,2})(?:h|:)?" +
                    "(?:(\\d{1,2})(?:m|:)?)?" +
                    "(?:(\\d{1,2})(?:s)?)?" +
                    "(?:\\s*Uhr)?" +
                    "(?:\\s*(morgens|vormittags|nachmittags|abends|nachts|am\\s+" +
                    "(?:Morgen|Vormittag|Nachmittag|Abend)|in\\s+der\\s+Nacht))?" +
                    "(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )

        @JvmStatic
        private val SECOND_REG_PATTERN =
            Regex(
                "^\\s*(\\-|\\–|\\~|\\〜|bis(?:\\s+um)?|\\?)\\s*" +
                    "(\\d{1,2})(?:h|:)?" +
                    "(?:(\\d{1,2})(?:m|:)?)?" +
                    "(?:(\\d{1,2})(?:s)?)?" +
                    "(?:\\s*Uhr)?" +
                    "(?:\\s*(morgens|vormittags|nachmittags|abends|nachts|am\\s+" +
                    "(?:Morgen|Vormittag|Nachmittag|Abend)|in\\s+der\\s+Nacht))?" +
                    "(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )

        private const val HOUR_GROUP = 2
        private const val MINUTE_GROUP = 3
        private const val SECOND_GROUP = 4
        private const val AM_PM_HOUR_GROUP = 5

        fun extractTimeComponent(
            extractingComponents: ParsedComponents,
            match: RegExpMatchArray,
        ): ParsedComponents? {
            var hour: Int? = match[HOUR_GROUP]!!.safeParseInt()
            var minute: Int? = 0
            var meridiem: Int? = null

            if (!match[MINUTE_GROUP].isNullOrEmpty()) {
                minute = match[MINUTE_GROUP]!!.safeParseInt()
            }

            if (minute >= 60 || hour > 24) {
                return null
            }

            if (hour >= 12) {
                meridiem = KronoMeridiem.PM
            }

            if (!match[AM_PM_HOUR_GROUP].isNullOrEmpty()) {
                if (hour > 12) {
                    return null
                }

                val ampm = match[AM_PM_HOUR_GROUP]!!.lowercase()
                if (ampm.contains(AM)) {
                    meridiem = KronoMeridiem.AM
                    if (hour == 12) {
                        hour = 0
                    }
                }

                if (ampm.contains(PM)) {
                    meridiem = KronoMeridiem.PM
                    if (hour != 12) {
                        hour += 12
                    }
                }

                if (ampm.contains(NIGHT)) {
                    if (hour == 12) {
                        meridiem = KronoMeridiem.AM
                        hour = 0
                    } else if (hour < 6) {
                        meridiem = KronoMeridiem.AM
                    } else {
                        meridiem = KronoMeridiem.PM
                        hour += 12
                    }
                }
            }

            processMeridiem(extractingComponents, hour, minute, meridiem)

            if (!match[SECOND_GROUP].isNullOrEmpty()) {
                val second = match[SECOND_GROUP]!!.safeParseInt()
                if (second >= 60) {
                    return null
                }

                extractingComponents.assign(KronoComponents.Second, second)
            }

            return extractingComponents
        }
    }
}
