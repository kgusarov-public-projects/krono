package org.kgusarov.krono.extensions

import org.kgusarov.krono.KronoDate
import org.kgusarov.krono.KronoUnit
import org.kgusarov.krono.ParsingComponents
import java.time.temporal.ChronoField

fun KronoDate.add(
    unit: KronoUnit,
    value: Int,
): KronoDate = plus(unit().multipliedBy(value))

fun KronoDate.add(
    unit: String,
    value: Int,
): KronoDate =
    when (val kronoUnit = KronoUnit[unit]) {
        null -> this
        else -> add(kronoUnit, value)
    }

fun KronoDate.assignSimilarDate(components: ParsingComponents) {
    components.assign(ChronoField.DAY_OF_MONTH, dayOfMonth)
    components.assign(ChronoField.MONTH_OF_YEAR, monthValue)
    components.assign(ChronoField.YEAR, year)
}

fun KronoDate.millis() = get(ChronoField.MILLI_OF_SECOND)

fun KronoDate.meridiem() = get(ChronoField.AMPM_OF_DAY)

fun KronoDate.assignSimilarTime(components: ParsingComponents) {
    components.assign(ChronoField.HOUR_OF_DAY, hour)
    components.assign(ChronoField.MINUTE_OF_HOUR, minute)
    components.assign(ChronoField.SECOND_OF_MINUTE, second)
    components.assign(ChronoField.MILLI_OF_SECOND, millis())
    components.assign(ChronoField.AMPM_OF_DAY, meridiem())
}

fun KronoDate.assignTheNextDay(component: ParsingComponents) {
    val target = plusDays(1)
    target.assignSimilarTime(component)
    target.implySimilarTime(component)
}

fun KronoDate.implyTheNextDay(component: ParsingComponents) {
    val target = plusDays(1)
    target.implySimilarDate(component)
    target.implySimilarTime(component)
}

fun KronoDate.implySimilarDate(component: ParsingComponents) {
    component.imply(ChronoField.DAY_OF_MONTH, dayOfMonth)
    component.imply(ChronoField.MONTH_OF_YEAR, monthValue)
    component.imply(ChronoField.YEAR, year)
}

fun KronoDate.implySimilarTime(component: ParsingComponents) {
    component.imply(ChronoField.HOUR_OF_DAY, hour)
    component.imply(ChronoField.MINUTE_OF_HOUR, minute)
    component.imply(ChronoField.SECOND_OF_MINUTE, second)
    component.imply(ChronoField.MILLI_OF_SECOND, millis())
}
