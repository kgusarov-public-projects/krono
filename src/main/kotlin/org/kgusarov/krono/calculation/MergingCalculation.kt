package org.kgusarov.krono.calculation

import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.KronoMeridiem
import org.kgusarov.krono.KronoUnit
import org.kgusarov.krono.ParsedComponents
import org.kgusarov.krono.ParsedResult
import org.kgusarov.krono.ParsingResult
import org.kgusarov.krono.extensions.add
import org.kgusarov.krono.extensions.assignSimilarDate
import org.kgusarov.krono.extensions.compareTo
import org.kgusarov.krono.extensions.implySimilarDate
import org.kgusarov.krono.extensions.plus

internal fun mergeDateTimeResult(
    dateResult: ParsedResult,
    timeResult: ParsedResult,
): ParsingResult {
    val result = dateResult.copy<ParsingResult>()
    val beginDate = dateResult.start
    val beginTime = timeResult.start
    result.start = mergeDateTimeComponent(beginDate, beginTime)

    if (dateResult.end != null || timeResult.end != null) {
        val endDate = dateResult.end ?: dateResult.start
        val endTime = timeResult.end ?: timeResult.start
        val endDateTime = mergeDateTimeComponent(endDate, endTime)

        if (
            (dateResult.end == null) &&
            (endDateTime.instant() < result.start.instant())
        ) {
            val nextDay = endDateTime.instant().add(KronoUnit.Day, 1)
            if (endDateTime.isCertain(KronoComponents.Day)) {
                nextDay.assignSimilarDate(endDateTime)
            } else {
                nextDay.implySimilarDate(endDateTime)
            }
        }

        result.end = endDateTime
    }

    return result
}

internal fun mergeDateTimeComponent(
    dateComponent: ParsedComponents,
    timeComponent: ParsedComponents,
): ParsedComponents {
    val dateTimeComponent = dateComponent.copy()
    if (timeComponent.isCertain(KronoComponents.Hour)) {
        dateTimeComponent.assign(KronoComponents.Hour, timeComponent[KronoComponents.Hour])
        dateTimeComponent.assign(KronoComponents.Minute, timeComponent[KronoComponents.Minute])

        if (timeComponent.isCertain(KronoComponents.Second)) {
            dateTimeComponent.assign(KronoComponents.Second, timeComponent[KronoComponents.Second])

            if (timeComponent.isCertain(KronoComponents.Millisecond)) {
                dateTimeComponent.assign(KronoComponents.Millisecond, timeComponent[KronoComponents.Millisecond])
            } else {
                dateTimeComponent.imply(KronoComponents.Millisecond, timeComponent[KronoComponents.Millisecond])
            }
        } else {
            dateTimeComponent.imply(KronoComponents.Second, timeComponent[KronoComponents.Second])
            dateTimeComponent.imply(KronoComponents.Millisecond, timeComponent[KronoComponents.Millisecond])
        }
    } else {
        dateTimeComponent.imply(KronoComponents.Hour, timeComponent[KronoComponents.Hour])
        dateTimeComponent.imply(KronoComponents.Minute, timeComponent[KronoComponents.Minute])
        dateTimeComponent.imply(KronoComponents.Second, timeComponent[KronoComponents.Second])
        dateTimeComponent.imply(KronoComponents.Millisecond, timeComponent[KronoComponents.Millisecond])
    }

    if (timeComponent.isCertain(KronoComponents.Offset)) {
        dateTimeComponent.assign(KronoComponents.Offset, timeComponent[KronoComponents.Offset])
    }

    if (timeComponent.isCertain(KronoComponents.Meridiem)) {
        dateTimeComponent.assign(KronoComponents.Meridiem, timeComponent[KronoComponents.Meridiem])
    } else if (
        (timeComponent[KronoComponents.Meridiem] != null) &&
        (dateTimeComponent[KronoComponents.Meridiem] == null)
    ) {
        dateTimeComponent.imply(KronoComponents.Meridiem, timeComponent[KronoComponents.Meridiem])
    }

    if (
        (dateTimeComponent[KronoComponents.Meridiem] == KronoMeridiem.PM) &&
        (dateTimeComponent[KronoComponents.Hour] < 12)
    ) {
        if (timeComponent.isCertain(KronoComponents.Hour)) {
            dateTimeComponent.assign(KronoComponents.Hour, dateTimeComponent[KronoComponents.Hour] + 12)
        } else {
            dateTimeComponent.imply(KronoComponents.Hour, dateTimeComponent[KronoComponents.Hour] + 12)
        }
    }

    dateTimeComponent.addTags(dateComponent.tags())
    dateTimeComponent.addTags(timeComponent.tags())
    return dateTimeComponent
}
