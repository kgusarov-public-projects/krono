package org.kgusarov.krono.extensions

import java.util.concurrent.TimeUnit

fun TimeUnit.toNanosInt(duration: Int) = toNanos(duration.toLong()).toInt()

fun TimeUnit.toSecondsInt(duration: Int) = toSeconds(duration.toLong()).toInt()
