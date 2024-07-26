package org.kgusarov.krono.extensions

import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.KronoDate
import org.kgusarov.krono.KronoUnit
import org.kgusarov.krono.ParsedComponents

fun KronoDate.add(
    unit: KronoUnit,
    value: Int,
): KronoDate = plus(unit().multipliedBy(value))

fun KronoDate.add(
    unit: KronoUnit,
    value: Int?,
): KronoDate = plus(unit().multipliedBy(value ?: 0))

fun KronoDate.add(
    unit: String,
    value: Int,
): KronoDate =
    when (val kronoUnit = KronoUnit[unit]) {
        null -> this
        else -> add(kronoUnit, value)
    }

fun KronoDate.assignSimilarDate(components: ParsedComponents) {
    components.assign(KronoComponents.Day, dayOfMonth)
    components.assign(KronoComponents.Month, monthValue)
    components.assign(KronoComponents.Year, year)
}

fun KronoDate.millis() = get(KronoComponents.Millisecond)

fun KronoDate.meridiem() = get(KronoComponents.Meridiem)

fun KronoDate.assignSimilarTime(components: ParsedComponents) {
    components.assign(KronoComponents.Hour, hour)
    components.assign(KronoComponents.Minute, minute)
    components.assign(KronoComponents.Second, second)
    components.assign(KronoComponents.Millisecond, millis())
    components.assign(KronoComponents.Meridiem, meridiem())
}

fun KronoDate.assignTheNextDay(component: ParsedComponents) {
    val target = plusDays(1)
    target.assignSimilarTime(component)
    target.implySimilarTime(component)
}

fun KronoDate.implyTheNextDay(component: ParsedComponents) {
    val target = plusDays(1)
    target.implySimilarDate(component)
    target.implySimilarTime(component)
}

fun KronoDate.implySimilarDate(component: ParsedComponents) {
    component.imply(KronoComponents.Day, dayOfMonth)
    component.imply(KronoComponents.Month, monthValue)
    component.imply(KronoComponents.Year, year)
}

fun KronoDate.implySimilarTime(component: ParsedComponents) {
    component.imply(KronoComponents.Hour, hour)
    component.imply(KronoComponents.Minute, minute)
    component.imply(KronoComponents.Second, second)
    component.imply(KronoComponents.Millisecond, millis())
}
