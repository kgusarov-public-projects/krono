package org.kgusarov.krono.extensions

import org.kgusarov.krono.KronoUnit

fun <T> Map<String, T>.contains(unit: KronoUnit): Boolean where T : Number =
    unit.prettyNames()
        .any {
            containsKey(it)
        }
