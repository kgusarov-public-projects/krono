package org.kgusarov.krono.locales.es

import org.kgusarov.krono.KronoDecimalTimeUnits
import org.kgusarov.krono.extensions.safeParseInt
import org.kgusarov.krono.extensions.unaryMinus
import java.math.BigDecimal

object Es {
    @JvmStatic
    val configuration = EsDefaultConfiguration()

    @JvmStatic
    val casual = configuration.createCasualConfiguration()

    @JvmStatic
    val strict = configuration.createConfiguration(strictMode = true)
}

private val SOME = Regex("algunos?", RegexOption.IGNORE_CASE)
private val FEW = Regex("unos?", RegexOption.IGNORE_CASE)
private val HALF = Regex("media?", RegexOption.IGNORE_CASE)

internal fun parseNumberPattern(match: String): BigDecimal {
    val num = match.lowercase()
    return when {
        EsConstants.INTEGER_WORD_DICTIONARY[num] != null -> EsConstants.INTEGER_WORD_DICTIONARY[num]!!.toBigDecimal()
        num == "un" || num == "una" || num == "uno" -> BigDecimal.ONE
        num.contains(SOME) -> BigDecimal(3)
        num.contains(FEW) -> BigDecimal(3)
        num.contains(HALF) -> BigDecimal(0.5)
        else -> num.toBigDecimal()
    }
}

private val YEAR = Regex("^[0-9]{1,4}$")
private val AC = Regex("a\\.?\\s*c\\.?", RegexOption.IGNORE_CASE)

internal fun parseYear(match: String): Int {
    return when {
        match.matches(YEAR) -> {
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

        match.contains(AC) -> {
            -match.replace(AC, "").safeParseInt()
        }

        else -> match.safeParseInt() ?: 0
    }
}

internal fun parseTimeUnits(timeUnitText: String): KronoDecimalTimeUnits =
    org.kgusarov.krono.common.parseTimeUnits(
        timeUnitText,
        EsConstants.SINGLE_TIME_UNIT_REGEX,
        EsConstants.TIME_UNIT_DICTIONARY,
        ::parseNumberPattern,
    )
