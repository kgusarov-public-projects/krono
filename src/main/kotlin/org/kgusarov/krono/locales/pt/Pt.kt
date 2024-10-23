package org.kgusarov.krono.locales.pt

import org.kgusarov.krono.extensions.safeParseInt
import org.kgusarov.krono.extensions.unaryMinus

object Pt {
    @JvmStatic
    val configuration = PtDefaultConfiguration()

    @JvmStatic
    val casual = configuration.createCasualConfiguration()

    @JvmStatic
    val strict = configuration.createConfiguration(strictMode = true)
}

private val NUMERIC_YEAR = Regex("^[0-9]{1,4}$")

private val BC_YEAR = Regex("a\\.?\\s*c\\.?", RegexOption.IGNORE_CASE)

internal fun parseYear(match: String): Int {
    return when {
        match.matches(NUMERIC_YEAR) -> {
            var yearNumber = match.toInt()
            if (yearNumber < 100) {
                yearNumber =
                    if (yearNumber > 50) {
                        yearNumber + 1900
                    } else {
                        yearNumber + 2000
                    }
            }
            yearNumber
        }

        match.contains(BC_YEAR) -> {
            -match.replace(BC_YEAR, "").safeParseInt()
        }

        else -> match.safeParseInt() ?: 0
    }
}
