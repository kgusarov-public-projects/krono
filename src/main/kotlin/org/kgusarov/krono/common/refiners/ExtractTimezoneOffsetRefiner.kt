package org.kgusarov.krono.common.refiners

import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.ParsedResult
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.Refiner
import org.kgusarov.krono.extensions.get
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class ExtractTimezoneOffsetRefiner : Refiner {
    override fun invoke(
        context: ParsingContext,
        results: MutableList<ParsedResult>,
    ): MutableList<ParsedResult> {
        results.forEach {
            if (it.start.isCertain(KronoComponents.Offset)) {
                return@forEach
            }

            val suffix = context.text.substring(it.index + it.text.length)
            val match = TIMEZONE_OFFSET_PATTERN.find(suffix) ?: return@forEach

            context {
                "${javaClass.simpleName} extracted timezone offset '${match.value}'"
            }

            val hourOffset = match[TIMEZONE_OFFSET_HOUR_OFFSET_GROUP].toInt()
            val minuteOffsetString = match.groupValues.getOrNull(TIMEZONE_OFFSET_MINUTE_OFFSET_GROUP) ?: ""
            val minuteOffset =
                if (minuteOffsetString.isNotEmpty()) {
                    minuteOffsetString.toInt()
                } else {
                    0
                }

            var timezoneOffset =
                TimeUnit.HOURS.toSeconds(hourOffset.toLong()) +
                    TimeUnit.MINUTES.toSeconds(minuteOffset.toLong())

            if (timezoneOffset > OFFSET_LIMIT.seconds) {
                return@forEach
            }

            if (match[TIMEZONE_OFFSET_SIGN_GROUP] == "-") {
                timezoneOffset = -timezoneOffset
            }

            if (it.end != null) {
                it.end!!.assign(KronoComponents.Offset, timezoneOffset.toInt())
            }

            it.start.assign(KronoComponents.Offset, timezoneOffset.toInt())
            it.text += match.value
        }

        return results
    }

    companion object {
        @JvmStatic
        private val TIMEZONE_OFFSET_PATTERN =
            Regex(
                "^\\s*(?:\\(?(?:GMT|UTC)\\s?)?([+-])(\\d{1,2})(?::?(\\d{2}))?\\)?",
                RegexOption.IGNORE_CASE,
            )

        private const val TIMEZONE_OFFSET_SIGN_GROUP = 1
        private const val TIMEZONE_OFFSET_HOUR_OFFSET_GROUP = 2
        private const val TIMEZONE_OFFSET_MINUTE_OFFSET_GROUP = 3

        private val OFFSET_LIMIT = Duration.of(14, ChronoUnit.HOURS)
    }
}
