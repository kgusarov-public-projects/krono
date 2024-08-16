package org.kgusarov.krono.common.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.ComponentsInputFactory
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.KronoMeridiem
import org.kgusarov.krono.ParsedComponents
import org.kgusarov.krono.ParsedResult
import org.kgusarov.krono.Parser
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.TextOrEndIndexInputFactory
import org.kgusarov.krono.extensions.compareTo
import org.kgusarov.krono.extensions.get
import org.kgusarov.krono.extensions.minus
import org.kgusarov.krono.extensions.plus
import org.kgusarov.krono.extensions.safeParseInt
import org.kgusarov.krono.utils.implyNextDay

@Suppress("RegExpRedundantEscape", "RegExpSimplifiable", "RegExpSingleCharAlternation")
private fun primaryTimePattern(
    leftBoundary: String,
    primaryPrefix: String,
    primarySuffix: String,
    flags: Set<RegexOption>,
) = Regex(
    leftBoundary +
        primaryPrefix +
        "(\\d{1,4})" +
        "(?:" +
        "(?:\\.|:|：)" +
        "(\\d{1,2})" +
        "(?:" +
        "(?::|：)" +
        "(\\d{2})" +
        "(?:\\.(\\d{1,6}))?" +
        ")?" +
        ")?" +
        "(?:\\s*(a\\.m\\.|p\\.m\\.|am?|pm?))?" +
        primarySuffix,
    flags,
)

@Suppress("RegExpRedundantEscape", "RegExpSimplifiable", "RegExpSingleCharAlternation")
private fun followingTimePattern(
    followingPhase: String,
    followingSuffix: String,
) = Regex(
    "^($followingPhase)" +
        "(\\d{1,4})" +
        "(?:" +
        "(?:\\.|\\:|\\：)" +
        "(\\d{1,2})" +
        "(?:" +
        "(?:\\.|\\:|\\：)" +
        "(\\d{1,2})(?:\\.(\\d{1,6}))?" +
        ")?" +
        ")?" +
        "(?:\\s*(a\\.m\\.|p\\.m\\.|am?|pm?))?" +
        followingSuffix,
    RegexOption.IGNORE_CASE,
)

@SuppressFBWarnings("EI_EXPOSE_REP")
abstract class AbstractTimeExpressionParser(
    private val strictMode: Boolean = false,
) : Parser {
    private var cachedPrimaryPrefix: String? = null
    private var cachedPrimarySuffix: String? = null
    private var cachedPrimaryTimePattern: Regex? = null
    private var cachedFollowingPhase: String? = null
    private var cachedFollowingSuffix: String? = null
    private var cachedFollowingTimePattern: Regex? = null

    override fun pattern(context: ParsingContext) = getPrimaryTimePatternThroughCache()

    override fun invoke(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult? {
        val startComponents = extractPrimaryTimeComponents(context, match)
        if (startComponents == null) {
            if (YEAR_LIKE.containsMatchIn(match[0]!!)) {
                match.index += 4
                return null
            }

            match.index += match[0]!!.length
            return null
        }

        val index = match.index + match[1]!!.length
        val text = match[0]!!.substring(match[1]!!.length)
        val result =
            context.createParsingResult(
                index,
                TextOrEndIndexInputFactory(text),
                ComponentsInputFactory(startComponents),
            )

        match.index += match[0]!!.length
        val remainingText = context.text.substring(match.index!!)
        val followingPattern = getFollowingTimePatternThroughCache()
        val followingMatch = followingPattern.find(remainingText)

        if (text.matches(THREE_OR_FOUR_DIGITS) && followingMatch != null) {
            if (followingMatch[0].matches(YEAR_MONTH_LIKE)) {
                return null
            }

            if (followingMatch[0].matches(YEAR_MONTH_COLON_LIKE)) {
                return null
            }
        }

        if (followingMatch == null || followingMatch[0].matches(TIMEZONE_OFFSET_LIKE)) {
            return checkAndReturnWithoutFollowingPattern(result)
        }

        result.end = this.extractFollowingTimeComponents(context, RegExpMatchArray(followingMatch), result)
        if (result.end != null) {
            result.text += followingMatch[0]
        }

        return checkAndReturnWithFollowingPattern(result)
    }

    protected open fun primaryPatternLeftBoundary() = PRIMARY_PATTERN_LEFT_BOUNDARY

    protected open fun patternFlags() = PATTERN_FLAGS

    protected open fun primarySuffix() = PRIMARY_SUFFIX

    protected open fun followingSuffix() = FOLLOWING_SUFFIX

    private fun getPrimaryTimePatternThroughCache(): Regex {
        val primaryPrefix = primaryPrefix()
        val primarySuffix = primarySuffix()

        if (cachedPrimaryPrefix == primaryPrefix && cachedPrimarySuffix == primarySuffix) {
            return cachedPrimaryTimePattern!!
        }

        cachedPrimaryTimePattern =
            primaryTimePattern(
                primaryPatternLeftBoundary(),
                primaryPrefix,
                primarySuffix,
                patternFlags(),
            )

        cachedPrimaryPrefix = primaryPrefix
        cachedPrimarySuffix = primarySuffix
        return cachedPrimaryTimePattern!!
    }

    private fun getFollowingTimePatternThroughCache(): Regex {
        val followingPhase = followingPhase()
        val followingSuffix = followingSuffix()

        if (cachedFollowingPhase == followingPhase && cachedFollowingSuffix == followingSuffix) {
            return cachedFollowingTimePattern!!
        }

        cachedFollowingTimePattern = followingTimePattern(followingPhase, followingSuffix)
        cachedFollowingPhase = followingPhase
        cachedFollowingSuffix = followingSuffix
        return cachedFollowingTimePattern!!
    }

    @Suppress("KotlinConstantConditions")
    protected open fun extractFollowingTimeComponents(
        context: ParsingContext,
        match: RegExpMatchArray,
        result: ParsedResult,
    ): ParsedComponents? {
        val components = context.createParsingComponents(ComponentsInputFactory(mapOf()))
        if (!assignSecondsAndMillis(match, components)) {
            return null
        }

        var hour = match.getInt(HOUR_GROUP)
        var minute = 0
        var meridiem = -1

        if (!match[MINUTE_GROUP].isNullOrEmpty()) {
            minute = match.getInt(MINUTE_GROUP)
        } else if (hour > 100) {
            minute = hour % 100
            hour /= 100
        }

        hour = fixHour(hour)
        if (minute >= 60 || hour > 23) {
            return null
        }

        if (hour >= 12) {
            meridiem = KronoMeridiem.PM
        }

        if (!match[AM_PM_HOUR_GROUP].isNullOrEmpty()) {
            if (hour > 12) {
                return null
            }

            val ampm = match[AM_PM_HOUR_GROUP]!![0].lowercase()
            if (ampm == "a") {
                meridiem = KronoMeridiem.AM
                if (hour == 12) {
                    hour = 0
                    if (!components.certainDay()) {
                        implyNextDay(components)
                    }
                }
            }

            if (ampm == "p") {
                meridiem = KronoMeridiem.PM
                if (hour != 12) {
                    hour += 12
                }
            }

            if (!result.start.certainMeridiem()) {
                components.assign(KronoComponents.Meridiem, meridiem)

                if (meridiem == KronoMeridiem.AM) {
                    result.start.imply(KronoComponents.Meridiem, KronoMeridiem.AM)

                    if (result.start.hour() == 12) {
                        result.start.assign(KronoComponents.Hour, 0)
                    }
                } else {
                    result.start.imply(KronoComponents.Meridiem, KronoMeridiem.PM)

                    if (result.start.hour() != 12) {
                        result.start.assign(KronoComponents.Hour, result.start.hour() + 12)
                    }
                }
            }
        }

        components.assign(KronoComponents.Hour, hour)
        components.assign(KronoComponents.Minute, minute)

        if (meridiem >= 0) {
            components.assign(KronoComponents.Meridiem, meridiem)
        } else {
            val startAtPm = result.start.isCertain(KronoComponents.Meridiem) && result.start.hour() > 12
            if (startAtPm) {
                if (result.start.hour() - 12 > hour) {
                    components.imply(KronoComponents.Meridiem, KronoMeridiem.AM)
                } else if (hour <= 12) {
                    components.assign(KronoComponents.Hour, hour + 12)
                    components.assign(KronoComponents.Meridiem, KronoMeridiem.PM)
                }
            } else if (hour > 12) {
                components.imply(KronoComponents.Meridiem, KronoMeridiem.PM)
            } else {
                components.imply(KronoComponents.Meridiem, KronoMeridiem.AM)
            }
        }

        if (components.instant() < result.start.instant()) {
            implyNextDay(components)
        }

        return components
    }

    protected open fun extractPrimaryTimeComponents(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParsedComponents? {
        val components = context.createParsingComponents(ComponentsInputFactory(mapOf()))
        var minute = 0
        var meridiem: Int? = null

        var hour = match.getInt(HOUR_GROUP)
        if (hour > 100) {
            if (strictMode || !match[MINUTE_GROUP].isNullOrEmpty()) {
                return null
            }

            minute = hour % 100
            hour /= 100
        }

        hour = fixHour(hour)
        if (hour > 23) {
            return null
        }

        if (!match[MINUTE_GROUP].isNullOrEmpty()) {
            if (match[MINUTE_GROUP]!!.length == 1 && match[AM_PM_HOUR_GROUP] == null) {
                return null
            }

            minute = match.getInt(MINUTE_GROUP)
        }

        if (minute >= 60) {
            return null
        }

        if (hour > 12) {
            meridiem = KronoMeridiem.PM
        }

        if (!match[AM_PM_HOUR_GROUP].isNullOrEmpty()) {
            if (hour > 12) {
                return null
            }

            val ampm = match[AM_PM_HOUR_GROUP]!![0].lowercase()
            if (ampm == "a") {
                meridiem = KronoMeridiem.AM
                if (hour == 12) {
                    hour = 0
                }
            }

            if (ampm == "p") {
                meridiem = KronoMeridiem.PM
                if (hour != 12) {
                    hour += 12
                }
            }
        }

        components.assign(KronoComponents.Hour, hour)
        components.assign(KronoComponents.Minute, minute)

        if (meridiem != null) {
            components.assign(KronoComponents.Meridiem, meridiem)
        } else {
            if (hour < 12) {
                components.imply(KronoComponents.Meridiem, KronoMeridiem.AM)
            } else {
                components.imply(KronoComponents.Meridiem, KronoMeridiem.PM)
            }
        }

        return if (!assignSecondsAndMillis(match, components)) {
            null
        } else {
            components
        }
    }

    private fun checkAndReturnWithFollowingPattern(result: ParsedResult): ParserResult? {
        if (FOLLOWING_PATTERN_CANCEL.matches(result.text)) {
            return null
        }

        val endingWithNumbers = ENDING_WITH_NUMBERS.find(result.text)
        if (endingWithNumbers != null) {
            if (strictMode) {
                return null
            }

            val startingNumbers = endingWithNumbers[1]
            val endingNumbers = endingWithNumbers[2]

            if (endingNumbers.contains(".") && !endingNumbers.matches(NOT_DOT_WITH_SINGLE_DIGIT)) {
                return null
            }

            val endingNumberVal = fixHour(endingNumbers.toInt())
            val startingNumberVal = fixHour(startingNumbers.toInt())
            if (endingNumberVal > 23 || startingNumberVal > 23) {
                return null
            }
        }

        return ParserResultFactory(result)
    }

    private fun checkAndReturnWithoutFollowingPattern(result: ParsedResult): ParserResult? {
        if (result.text.matches(SINGLE_DIGIT)) {
            return null
        }

        if (result.text.matches(THREE_OR_MORE_DIGITS)) {
            return null
        }

        if (result.text.matches(UNLIKELY_MERIDIEM)) {
            return null
        }

        val endingWithNumbers = ENDING_WITH_NUMBERS_OR_DOTS.find(result.text)
        if (endingWithNumbers != null) {
            val endingNumbers = endingWithNumbers[1]

            if (strictMode) {
                return null
            }

            if (endingNumbers.contains(".") && !NOT_DOT_WITH_SINGLE_DIGIT.containsMatchIn(endingNumbers)) {
                return null
            }

            val endingNumberVal = endingNumbers.safeParseInt()
            if (endingNumberVal > 23) {
                return null
            }
        }

        return ParserResultFactory(result)
    }

    private fun assignSecondsAndMillis(
        match: RegExpMatchArray,
        components: ParsedComponents,
    ): Boolean {
        if (!match[MILLI_SECOND_GROUP].isNullOrEmpty()) {
            val millisecond = match[MILLI_SECOND_GROUP]!!.substring(0, 3).toInt()
            if (millisecond >= 1000) {
                return false
            }

            components.assign(KronoComponents.Millisecond, millisecond)
        }

        if (!match[SECOND_GROUP].isNullOrEmpty()) {
            val second = match.getInt(SECOND_GROUP)
            if (second >= 60) {
                return false
            }

            components.assign(KronoComponents.Second, second)
        }

        return true
    }

    private fun fixHour(hour: Int): Int = if (hour == 24) 0 else hour

    abstract fun primaryPrefix(): String

    abstract fun followingPhase(): String

    companion object {
        const val HOUR_GROUP = 2
        const val MINUTE_GROUP = 3
        const val SECOND_GROUP = 4
        const val MILLI_SECOND_GROUP = 5
        const val AM_PM_HOUR_GROUP = 6

        @JvmStatic
        private val PRIMARY_PATTERN_LEFT_BOUNDARY = "(^|\\s|T|\\b)"

        @JvmStatic
        private val PATTERN_FLAGS = setOf(RegexOption.IGNORE_CASE)

        @JvmStatic
        private val PRIMARY_SUFFIX = "(?!/)(?=\\W|$)"

        @JvmStatic
        private val FOLLOWING_SUFFIX = "(?!/)(?=\\W|$)"

        @JvmStatic
        private val SINGLE_DIGIT = Regex("^\\d$")

        @JvmStatic
        private val THREE_OR_MORE_DIGITS = Regex("^\\d\\d\\d+$")

        @JvmStatic
        private val UNLIKELY_MERIDIEM = Regex("\\d[apAP]$")

        @JvmStatic
        private val FOLLOWING_PATTERN_CANCEL = Regex("^\\d+-\\d+$")

        @JvmStatic
        private val ENDING_WITH_NUMBERS = Regex("[^\\d:.](\\d[\\d.]+)\\s*-\\s*(\\d[\\d.]+)")

        @JvmStatic
        private val ENDING_WITH_NUMBERS_OR_DOTS = Regex("[^\\d:.](\\d[\\d.]+)$")

        @JvmStatic
        private val NOT_DOT_WITH_SINGLE_DIGIT = Regex("\\d(\\.\\d{2})+$")

        @JvmStatic
        private val YEAR_LIKE = Regex("^\\d{4}")

        @JvmStatic
        private val THREE_OR_FOUR_DIGITS = Regex("^\\d{3,4}$")

        @JvmStatic
        private val YEAR_MONTH_LIKE = Regex("^\\s*([+-])\\s*\\d{2,4}$")

        @JvmStatic
        private val YEAR_MONTH_COLON_LIKE = Regex("^\\s*([+-])\\s*\\d{2}\\W\\d{2}")

        @JvmStatic
        private val TIMEZONE_OFFSET_LIKE = Regex("^\\s*([+-])\\s*\\d{3,4}$")
    }
}
