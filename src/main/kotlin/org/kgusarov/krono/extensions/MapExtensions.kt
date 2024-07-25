package org.kgusarov.krono.extensions

import org.kgusarov.krono.KronoUnit

fun Map<String, Int>.contains(unit: KronoUnit) =
    unit.prettyNames()
        .any {
            containsKey(it)
        }
