package org.kgusarov.krono.extensions

import kotlin.math.min

operator fun String?.not() = isNullOrEmpty()

fun String.substr(
    index: Int,
    length: Int,
) = substring(index, min(this.length, index + length))

fun String.safeSubstring(startIndex: Int) = if (startIndex < this.length) this.substring(startIndex) else ""
