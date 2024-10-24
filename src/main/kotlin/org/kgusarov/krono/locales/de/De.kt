package org.kgusarov.krono.locales.de

import org.kgusarov.krono.KronoDecimalTimeUnits
import org.kgusarov.krono.calculation.findMostLikelyADYear
import org.kgusarov.krono.extensions.safeParseBigDecimal
import org.kgusarov.krono.extensions.safeParseInt
import org.kgusarov.krono.extensions.unaryMinus
import org.kgusarov.krono.locales.es.parseNumberPattern
import java.math.BigDecimal

object De {
    @JvmStatic
    val configuration = DeDefaultConfiguration()

    @JvmStatic
    val casual = configuration.createCasualConfiguration()

    @JvmStatic
    val strict = configuration.createConfiguration(strictMode = true)
}

private val TWO = Regex("wenigen", RegexOption.IGNORE_CASE)
private val HALF1 = Regex("halb", RegexOption.IGNORE_CASE)
private val HALF2 = Regex("halben", RegexOption.IGNORE_CASE)
private val FEW = Regex("einigen", RegexOption.IGNORE_CASE)
private val SOME = Regex("mehreren", RegexOption.IGNORE_CASE)

private val ONE = arrayOf("ein", "einer", "einem", "einen", "eine")

internal fun parseNumberPattern(match: String): BigDecimal {
    val num = match.lowercase()
    return when {
        DeConstants.INTEGER_WORD_DICTIONARY[num] != null -> DeConstants.INTEGER_WORD_DICTIONARY[num]!!.toBigDecimal()
        ONE.contains(num) -> BigDecimal.ONE
        num.contains(TWO) -> BigDecimal(2)
        num.contains(HALF1) || num.contains(HALF2) -> BigDecimal(0.5)
        num.contains(FEW) -> BigDecimal(3)
        num.contains(SOME) -> BigDecimal(7)
        else -> num.safeParseBigDecimal() ?: BigDecimal.ZERO
    }
}

private val V_CHR = Regex("v", RegexOption.IGNORE_CASE)
private val N_CHR = Regex("n", RegexOption.IGNORE_CASE)
private val Z_CHR = Regex("z", RegexOption.IGNORE_CASE)
private val DIGITS = Regex("[^0-9]+", RegexOption.IGNORE_CASE)

internal fun parseYear(match: String): Int {
    return when {
        V_CHR.containsMatchIn(match) -> {
            -match.replace(DIGITS, "").safeParseInt()
        }

        N_CHR.containsMatchIn(match) || Z_CHR.containsMatchIn(match) -> {
            match.replace(DIGITS, "").safeParseInt()!!
        }

        else -> {
            val rawYearNumber = match.safeParseInt() ?: 0
            findMostLikelyADYear(rawYearNumber)
        }
    }
}

internal fun parseTimeUnits(timeUnitText: String): KronoDecimalTimeUnits =
    org.kgusarov.krono.common.parseTimeUnits(
        timeUnitText,
        DeConstants.SINGLE_TIME_UNIT_REGEX,
        DeConstants.TIME_UNIT_DICTIONARY,
        ::parseNumberPattern,
    )
