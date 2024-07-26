package org.kgusarov.krono.utils

import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.KronoTimeUnits
import org.kgusarov.krono.KronoUnit
import org.kgusarov.krono.ParsedComponents
import org.kgusarov.krono.extensions.add
import org.kgusarov.krono.extensions.unaryMinus

internal fun reverseTimeUnits(timeUnits: KronoTimeUnits): KronoTimeUnits =
    timeUnits.entries.associate {
        it.key to -it.value
    }

internal fun addImpliedTimeUnits(
    components: ParsedComponents,
    timeUnits: KronoTimeUnits,
): ParsedComponents {
    val result = components.copy()
    var date = components.instant()

    timeUnits.forEach {
        date = date.add(it.key, it.value)
    }

    if (
        (KronoUnit.Day in timeUnits) ||
        (KronoUnit.Week in timeUnits) ||
        (KronoUnit.Month in timeUnits) ||
        (KronoUnit.Year in timeUnits)
    ) {
        result.imply(KronoComponents.Day, date.dayOfMonth)
        result.imply(KronoComponents.Month, date.monthValue)
        result.imply(KronoComponents.Year, date.year)
    }

    if (
        (KronoUnit.Second in timeUnits) ||
        (KronoUnit.Minute in timeUnits) ||
        (KronoUnit.Hour in timeUnits)
    ) {
        result.imply(KronoComponents.Second, date.second)
        result.imply(KronoComponents.Minute, date.minute)
        result.imply(KronoComponents.Hour, date.hour)
    }

    return result
}
