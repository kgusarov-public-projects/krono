package org.kgusarov.krono.extensions

import java.time.DayOfWeek

operator fun DayOfWeek.minus(other: DayOfWeek): Int = value - other.value
