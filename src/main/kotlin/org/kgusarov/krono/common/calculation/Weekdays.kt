package org.kgusarov.krono.common.calculation

import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.KronoDate
import org.kgusarov.krono.KronoUnit
import org.kgusarov.krono.ParsedComponents
import org.kgusarov.krono.ParsingComponents
import org.kgusarov.krono.ReferenceWithTimezone
import org.kgusarov.krono.extensions.minus
import org.kgusarov.krono.utils.addImpliedTimeUnits
import java.time.DayOfWeek

internal fun createParsingComponentsAtWeekday(
    reference: ReferenceWithTimezone,
    weekday: DayOfWeek,
    modifier: String?,
): ParsedComponents {
    val refDate = reference.withAdjustedTimezone()
    val daysToWeekday = getDaysToWeekday(refDate, weekday, modifier)

    var components: ParsedComponents = ParsingComponents(reference)
    components =
        addImpliedTimeUnits(
            components,
            mutableMapOf(
                KronoUnit.Day to daysToWeekday,
            ),
        )

    return components.assign(KronoComponents.Weekday, weekday.value)
}

internal fun getDaysToWeekday(
    refDate: KronoDate,
    weekday: DayOfWeek,
    modifier: String? = null,
): Int {
    val refWeekday = refDate.dayOfWeek

    when (modifier) {
        "this" -> return getDaysForwardToWeekday(refDate, weekday)
        "last" -> return getBackwardDaysToWeekday(refDate, weekday)
        "next" -> {
            if (refWeekday == DayOfWeek.SUNDAY) {
                return if (weekday == DayOfWeek.SUNDAY) 7 else weekday.value
            }

            if (refWeekday == DayOfWeek.SATURDAY) {
                return when (weekday) {
                    DayOfWeek.SATURDAY -> 7
                    DayOfWeek.SUNDAY -> 8
                    else -> 1 + weekday.value
                }
            }

            return if (
                (weekday < refWeekday) &&
                (weekday != DayOfWeek.SUNDAY)
            ) {
                getDaysForwardToWeekday(refDate, weekday)
            } else {
                getDaysForwardToWeekday(refDate, weekday) + 7
            }
        }

        else -> return getDaysToWeekdayClosest(refDate, weekday)
    }
}

internal fun getDaysForwardToWeekday(
    refDate: KronoDate,
    weekday: DayOfWeek,
): Int {
    val refWeekday = refDate.dayOfWeek
    var forwardCount = weekday - refWeekday
    if (forwardCount < 0) {
        forwardCount += 7
    }
    return forwardCount
}

internal fun getBackwardDaysToWeekday(
    refDate: KronoDate,
    weekday: DayOfWeek,
): Int {
    val refWeekday = refDate.dayOfWeek
    var backwardCount = weekday - refWeekday
    if (backwardCount >= 0) {
        backwardCount -= 7
    }
    return backwardCount
}

internal fun getDaysToWeekdayClosest(
    refDate: KronoDate,
    weekday: DayOfWeek,
): Int {
    val backward = getBackwardDaysToWeekday(refDate, weekday)
    val forward = getDaysForwardToWeekday(refDate, weekday)
    return if (forward < -backward) forward else backward
}
