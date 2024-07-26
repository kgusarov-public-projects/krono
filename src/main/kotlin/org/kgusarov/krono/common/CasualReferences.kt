package org.kgusarov.krono.common

import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.KronoMeridiem
import org.kgusarov.krono.KronoTimezone
import org.kgusarov.krono.KronoUnit
import org.kgusarov.krono.ParsedComponents
import org.kgusarov.krono.ParsingComponents
import org.kgusarov.krono.ReferenceWithTimezone
import org.kgusarov.krono.extensions.add
import org.kgusarov.krono.extensions.assignSimilarDate
import org.kgusarov.krono.extensions.assignSimilarTime
import org.kgusarov.krono.extensions.implySimilarTime
import org.kgusarov.krono.extensions.implyTheNextDay

internal fun now(reference: ReferenceWithTimezone): ParsedComponents {
    val targetDate = reference.instant.atZone(KronoTimezone.systemDefault())
    val component = ParsingComponents(reference)

    targetDate.toLocalDateTime().assignSimilarDate(component)
    targetDate.toLocalDateTime().assignSimilarTime(component)

    if (reference.timezone != null) {
        component.assign(KronoComponents.Offset, targetDate.offset.totalSeconds)
    }

    return component.addTag("casualReference/now")
}

internal fun today(reference: ReferenceWithTimezone): ParsedComponents {
    val targetDate = reference.instant
    val component = ParsingComponents(reference)

    targetDate.assignSimilarDate(component)
    targetDate.implySimilarTime(component)

    return component.addTag("casualReference/today")
}

internal fun theDayAfter(
    reference: ReferenceWithTimezone,
    days: Int,
): ParsedComponents {
    var targetDate = reference.instant
    val component = ParsingComponents(reference)
    targetDate = targetDate.add(KronoUnit.Day, days)
    targetDate.assignSimilarDate(component)
    targetDate.implySimilarTime(component)
    return component
}

internal fun theDayBefore(
    reference: ReferenceWithTimezone,
    days: Int,
): ParsedComponents = theDayAfter(reference, -days)

internal fun yesterday(reference: ReferenceWithTimezone): ParsedComponents =
    theDayBefore(reference, 1)
        .addTags("casualReference/yesterday")

internal fun tomorrow(reference: ReferenceWithTimezone): ParsedComponents =
    theDayAfter(reference, 1)
        .addTags("casualReference/tomorrow")

internal fun tonight(
    reference: ReferenceWithTimezone,
    implyHour: Int = 22,
): ParsedComponents {
    val targetDate = reference.instant
    val component = ParsingComponents(reference)

    targetDate.assignSimilarDate(component)
    component.imply(KronoComponents.Hour, implyHour)
    component.imply(KronoComponents.Meridiem, KronoMeridiem.PM)

    return component.addTag("casualReference/tonight")
}

internal fun lastNight(
    reference: ReferenceWithTimezone,
    implyHour: Int = 0,
): ParsedComponents {
    var targetDate = reference.instant
    val component = ParsingComponents(reference)

    if (targetDate.hour < 6) {
        targetDate = targetDate.add(KronoUnit.Day, -1)
    }

    targetDate.assignSimilarDate(component)
    return component.imply(KronoComponents.Hour, implyHour)
}

internal fun evening(
    reference: ReferenceWithTimezone,
    implyHour: Int = 20,
): ParsedComponents {
    val component = ParsingComponents(reference)
    component.imply(KronoComponents.Meridiem, KronoMeridiem.PM)
    component.imply(KronoComponents.Hour, implyHour)
    return component.addTag("casualReference/evening")
}

internal fun yesterdayEvening(
    reference: ReferenceWithTimezone,
    implyHour: Int = 20,
): ParsedComponents {
    var targetDate = reference.instant
    val component = ParsingComponents(reference)

    targetDate = targetDate.add(KronoUnit.Day, -1)
    targetDate.assignSimilarDate(component)

    component.imply(KronoComponents.Hour, implyHour)
    component.imply(KronoComponents.Meridiem, KronoMeridiem.PM)
    return component.addTags("casualReference/yesterday", "casualReference/evening")
}

internal fun midnight(reference: ReferenceWithTimezone): ParsedComponents {
    val component = ParsingComponents(reference)
    val targetDate = reference.instant
    if (targetDate.hour > 2) {
        targetDate.implyTheNextDay(component)
    }

    component.assign(KronoComponents.Hour, 0)
    component.imply(KronoComponents.Minute, 0)
    component.imply(KronoComponents.Second, 0)
    component.imply(KronoComponents.Millisecond, 0)

    return component.addTag("casualReference/midnight")
}

internal fun morning(
    reference: ReferenceWithTimezone,
    implyHour: Int = 6,
): ParsedComponents {
    val component = ParsingComponents(reference)

    component.imply(KronoComponents.Meridiem, KronoMeridiem.AM)
    component.imply(KronoComponents.Hour, implyHour)
    component.imply(KronoComponents.Minute, 0)
    component.imply(KronoComponents.Second, 0)
    component.imply(KronoComponents.Millisecond, 0)

    return component.addTag("casualReference/morning")
}

internal fun afternoon(
    reference: ReferenceWithTimezone,
    implyHour: Int = 15,
): ParsedComponents {
    val component = ParsingComponents(reference)

    component.imply(KronoComponents.Meridiem, KronoMeridiem.PM)
    component.imply(KronoComponents.Hour, implyHour)
    component.imply(KronoComponents.Minute, 0)
    component.imply(KronoComponents.Second, 0)
    component.imply(KronoComponents.Millisecond, 0)

    return component.addTag("casualReference/afternoon")
}

internal fun noon(reference: ReferenceWithTimezone): ParsedComponents {
    val component = ParsingComponents(reference)

    component.imply(KronoComponents.Meridiem, KronoMeridiem.AM)
    component.imply(KronoComponents.Hour, 12)
    component.imply(KronoComponents.Minute, 0)
    component.imply(KronoComponents.Second, 0)
    component.imply(KronoComponents.Millisecond, 0)

    return component.addTag("casualReference/noon")
}
