package org.kgusarov.krono

import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.abs

interface ParsedComponents {
    fun isCertain(component: KronoComponent): Boolean

    operator fun get(component: KronoComponent): Int?

    fun instant(): KronoDate

    fun tags(): Set<String>

    fun copy(): ParsedComponents

    fun imply(
        component: KronoComponent,
        value: Int?,
    ): ParsedComponents

    fun assign(
        component: KronoComponent,
        value: Int?,
    ): ParsedComponents

    fun addTag(tag: String): ParsedComponents

    fun addTags(vararg values: String): ParsedComponents

    fun addTags(values: Set<String>): ParsedComponents

    fun isValidDate(): Boolean

    fun certainComponents(): Array<KronoComponent>

    fun year() = get(KronoComponents.Year)

    fun certainYear() = isCertain(KronoComponents.Year)

    fun month() = get(KronoComponents.Month)

    fun certainMonth() = isCertain(KronoComponents.Month)

    fun day() = get(KronoComponents.Day)

    fun certainDay() = isCertain(KronoComponents.Day)

    fun hour() = get(KronoComponents.Hour)

    fun certainHour() = isCertain(KronoComponents.Hour)

    fun minute() = get(KronoComponents.Minute)

    fun certainMinute() = isCertain(KronoComponents.Minute)

    fun second() = get(KronoComponents.Second)

    fun certainSecond() = isCertain(KronoComponents.Second)

    fun millisecond() = get(KronoComponents.Millisecond)

    fun certainMillisecond() = isCertain(KronoComponents.Millisecond)

    fun offset() = get(KronoComponents.Offset)

    fun offsetMinutes() = offset()?.let { it / 60 }

    fun certainOffset() = isCertain(KronoComponents.Offset)

    fun weekday() = get(KronoComponents.Weekday)

    fun certainWeekday() = isCertain(KronoComponents.Weekday)

    fun meridiem() = get(KronoComponents.Meridiem)

    fun certainMeridiem() = isCertain(KronoComponents.Meridiem)

    fun onlyDate() = !certainHour() && !certainMinute() && !certainSecond()

    fun onlyTime() = !certainWeekday() && !certainDay() && !certainMonth()

    fun onlyWeekday() = certainWeekday() && !certainDay() && !certainMonth()

    fun dateWithUnknownYear() = certainMonth() && !certainYear()

    fun asString(): String {
        val year = getComponentString(KronoComponents.Year)
        val month = getComponentString(KronoComponents.Month)
        val day = getComponentString(KronoComponents.Day)
        val hour = getComponentString(KronoComponents.Hour)
        val minute = getComponentString(KronoComponents.Minute)
        val second = getComponentString(KronoComponents.Second)
        val millisecond = getComponentString(KronoComponents.Millisecond)
        val offset = getComponentString(KronoComponents.Offset)
        val weekday = getComponentString(KronoComponents.Weekday)
        val meridiem = getComponentString(KronoComponents.Meridiem)

        return "($weekday, $year-$month-${day}T$hour:$minute:$second.$millisecond$offset ($meridiem))"
    }

    fun getComponentString(component: KronoComponent): String =
        when (component) {
            KronoComponents.Year -> getComponentString(year(), certainYear(), 4)
            KronoComponents.Month -> getComponentString(month(), certainMonth())
            KronoComponents.Day -> getComponentString(day(), certainDay())
            KronoComponents.Hour -> getComponentString(hour(), certainHour())
            KronoComponents.Minute -> getComponentString(minute(), certainMinute())
            KronoComponents.Second -> getComponentString(second(), certainSecond())
            KronoComponents.Millisecond -> getComponentString(millisecond(), certainMillisecond(), 3)
            KronoComponents.Weekday -> {
                val value = weekday()
                if (value == null) {
                    "⯑⯑⯑"
                } else {
                    val displayValue =
                        DayOfWeek.of(value).getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault())
                    if (certainWeekday()) {
                        displayValue
                    } else {
                        "$displayValue?"
                    }
                }
            }

            KronoComponents.Meridiem -> {
                val value = meridiem()
                if (value == null) {
                    "⯑⯑⯑"
                } else {
                    val displayValue = if (value == KronoMeridiem.AM) "AM" else "PM"
                    if (certainMeridiem()) {
                        displayValue
                    } else {
                        "$displayValue?"
                    }
                }
            }

            else -> {
                val value = offsetMinutes()
                if (value == null) {
                    "⯑⯑⯑⯑⯑"
                } else {
                    val sign = if (value < 0) '-' else '+'
                    val displayValue = "$sign%04d".format(abs(value))
                    if (certainOffset()) {
                        displayValue
                    } else {
                        "$displayValue?"
                    }
                }
            }
        }

    fun getComponentString(
        value: Int?,
        certain: Boolean,
        length: Int = 2,
    ): String =
        when {
            value == null -> "⯑".repeat(length)
            certain -> value.toString().padStart(length, '0')
            else -> value.toString().padStart(length, '0') + '?'
        }
}
