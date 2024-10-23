package org.kgusarov.krono.locales.fr

import org.kgusarov.krono.KronoDecimalTimeUnits
import org.kgusarov.krono.extensions.safeParseBigDecimal
import org.kgusarov.krono.extensions.safeParseInt
import java.math.BigDecimal

object Fr {
    @JvmStatic
    val configuration = FrDefaultConfiguration()

    @JvmStatic
    val casual = configuration.createCasualConfiguration()

    @JvmStatic
    val strict = configuration.createConfiguration(strictMode = true)
}

private val SOME = Regex("quelques?", RegexOption.IGNORE_CASE)
private val HALF = Regex("demi-?", RegexOption.IGNORE_CASE)

internal fun parseNumberPattern(match: String): BigDecimal? {
    val num = match.lowercase()
    return when {
        FrConstants.INTEGER_WORD_DICTIONARY[num] != null -> FrConstants.INTEGER_WORD_DICTIONARY[num]!!.toBigDecimal()
        num == "un" || num == "une" -> BigDecimal.ONE
        num.contains(SOME) -> BigDecimal(3)
        num.contains(HALF) -> BigDecimal(0.5)
        else -> num.safeParseBigDecimal()
    }
}

@Suppress("RegExpUnnecessaryNonCapturingGroup")
private val ORDINAL_SUFFIX = Regex("(?:er)$", RegexOption.IGNORE_CASE)

internal fun parseOrdinalNumberPattern(match: String): Int {
    val num = match.lowercase()
    return num.replace(ORDINAL_SUFFIX, "").safeParseInt()!!
}

private val AC = Regex("AC", RegexOption.IGNORE_CASE)
private val BC = Regex("BC", RegexOption.IGNORE_CASE)
private val AD = Regex("AD", RegexOption.IGNORE_CASE)
private val C = Regex("C", RegexOption.IGNORE_CASE)

@Suppress("RegExpSimplifiable")
private val DIGIT = Regex("[^\\d]+", RegexOption.IGNORE_CASE)

internal fun parseYear(match: String): Int {
    if (match.contains(AC)) {
        return -match.replace(BC, "").safeParseInt()!!
    }

    if (match.contains(AD) || match.contains(C)) {
        return match.replace(DIGIT, "").safeParseInt()!!
    }

    val yearNumber = match.safeParseInt()!!
    return if (yearNumber < 100) {
        if (yearNumber > 50) {
            yearNumber + 1900
        } else {
            yearNumber + 2000
        }
    } else {
        yearNumber
    }
}

internal fun parseTimeUnits(timeUnitText: String): KronoDecimalTimeUnits =
    org.kgusarov.krono.common.parseTimeUnits(
        timeUnitText,
        FrConstants.SINGLE_TIME_UNIT_REGEX,
        FrConstants.TIME_UNIT_DICTIONARY,
        ::parseNumberPattern,
    )
