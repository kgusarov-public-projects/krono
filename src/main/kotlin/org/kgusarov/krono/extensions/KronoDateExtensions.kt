package org.kgusarov.krono.extensions

import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.KronoDate
import org.kgusarov.krono.KronoUnit
import org.kgusarov.krono.ParsedComponents
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

fun KronoDate.add(
    unit: KronoUnit,
    value: Long,
): KronoDate =
    when (unit) {
        KronoUnit.Day -> plusDays(value)
        KronoUnit.Month -> plusMonths(value)
        KronoUnit.Year -> plusYears(value)
        KronoUnit.Hour -> plusHours(value)
        KronoUnit.Minute -> plusMinutes(value)
        KronoUnit.Second -> plusSeconds(value)
        KronoUnit.Millisecond -> plusNanos(TimeUnit.MILLISECONDS.toNanos(value))
        KronoUnit.Week -> plusWeeks(value)
        KronoUnit.Quarter -> plusMonths(3 * value)
    }

fun KronoDate.add(
    unit: KronoUnit,
    value: Int,
): KronoDate = add(unit, value.toLong())

fun KronoDate.add(
    unit: KronoUnit,
    value: Int?,
): KronoDate = add(unit, value?.toLong() ?: 0)

fun KronoDate.add(
    unit: KronoUnit,
    value: BigDecimal?,
): KronoDate =
    when {
        value == null -> this
        value.stripTrailingZeros().scale() <= 0 -> add(unit, value.toLong())
        else -> plus(unit * value)
    }

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
    target.assignSimilarDate(component)
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
