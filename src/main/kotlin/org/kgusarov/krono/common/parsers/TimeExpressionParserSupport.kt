package org.kgusarov.krono.common.parsers

import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.KronoMeridiem
import org.kgusarov.krono.ParsedComponents
import org.kgusarov.krono.Parser
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.extensions.compareTo
import org.kgusarov.krono.extensions.plus

abstract class TimeExpressionParserSupport : Parser {
    protected fun processTimeComponents(
        extractingComponents: ParsedComponents,
        match: RegExpMatchArray,
        inHour: Int?,
        minute: Int?,
        inMeridiem: Int?,
        amPmHourGroup: Int,
    ): ParsedComponents? {
        var hour = inHour
        var meridiem = inMeridiem

        if (!match[amPmHourGroup].isNullOrEmpty()) {
            if (hour > 12) {
                return null
            }

            val ampm = match[amPmHourGroup]!![0].lowercase()
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

        processMeridiem(extractingComponents, hour, minute, meridiem)
        return extractingComponents
    }

    companion object {
        fun processMeridiem(
            extractingComponents: ParsedComponents,
            hour: Int?,
            minute: Int?,
            meridiem: Int?,
        ) {
            extractingComponents.assign(KronoComponents.Hour, hour)
            extractingComponents.assign(KronoComponents.Minute, minute)

            if (meridiem != null) {
                extractingComponents.assign(KronoComponents.Meridiem, meridiem)
            } else {
                if (hour < 12) {
                    extractingComponents.imply(KronoComponents.Meridiem, KronoMeridiem.AM)
                } else {
                    extractingComponents.imply(KronoComponents.Meridiem, KronoMeridiem.PM)
                }
            }
        }
    }
}
