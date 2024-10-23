package org.kgusarov.krono.extensions

import java.math.BigDecimal
import kotlin.math.min

operator fun String?.not() = isNullOrEmpty()

fun String.substr(
    index: Int,
    length: Int,
) = substring(index, min(this.length, index + length))

fun String.safeSubstring(startIndex: Int) = if (startIndex < this.length) this.substring(startIndex) else ""

fun String.safeParseBigDecimal(): BigDecimal? {
    val trimmed = this.trim()

    return try {
        trimmed.toBigDecimal()
    } catch (_: NumberFormatException) {
        null
    }
}

fun String.safeParseInt(): Int? {
    val trimmed = this.trim()

    return try {
        trimmed.toInt()
    } catch (_: NumberFormatException) {
        val len = trimmed.length
        for (trimEnd in len downTo 1) {
            try {
                val bd = trimmed.substr(0, trimEnd).toBigDecimal()
                return bd.toInt()
            } catch (_: NumberFormatException) {
            }
        }

        null
    }
}
