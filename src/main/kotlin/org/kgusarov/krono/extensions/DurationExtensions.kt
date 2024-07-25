package org.kgusarov.krono.extensions

import java.time.Duration

fun Duration.multipliedBy(value: Int) = multipliedBy(value.toLong())
