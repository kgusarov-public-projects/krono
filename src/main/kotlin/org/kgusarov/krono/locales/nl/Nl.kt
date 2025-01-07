package org.kgusarov.krono.locales.nl

import org.kgusarov.krono.KronoDecimalTimeUnits
import org.kgusarov.krono.calculation.findMostLikelyADYear
import org.kgusarov.krono.extensions.safeParseInt
import org.kgusarov.krono.extensions.unaryMinus
import java.math.BigDecimal

object Nl {
    @JvmStatic
    val configuration = NlDefaultConfiguration()

    @JvmStatic
    val casual = configuration.createCasualConfiguration()

    @JvmStatic
    val strict = configuration.createConfiguration(strictMode = true)
}

internal fun parseNumberPattern(match: String): BigDecimal {
    val num = match.lowercase()
    return when {
        NlConstants.INTEGER_WORD_DICTIONARY[num] != null -> NlConstants.INTEGER_WORD_DICTIONARY[num]!!.toBigDecimal()
        num == "paar" -> BigDecimal(2)
        num == "half" || num.contains("halve") -> BigDecimal(0.5)
        else -> num.replace(",", ".").toBigDecimal()
    }
}

@Suppress("RegExpUnnecessaryNonCapturingGroup")
private val ORDINAL_SUFFIX_REGEX =
    Regex(
        "(?:ste|de)$",
        RegexOption.IGNORE_CASE,
    )

internal fun parseOrdinalNumberPattern(match: String): Int {
    var num = match.lowercase()
    if (NlConstants.ORDINAL_WORD_DICTIONARY[num] !== null) {
        return NlConstants.ORDINAL_WORD_DICTIONARY[num]!!
    }

    num = num.replace(ORDINAL_SUFFIX_REGEX, "")
    return if (num.isEmpty()) 0 else num.toInt()
}

private val BEFORE_CHRIST = Regex("voor Christus", RegexOption.IGNORE_CASE)
private val AFTER_CHRIST = Regex("na Christus", RegexOption.IGNORE_CASE)

internal fun parseYear(match: String): Int {
    return when {
        BEFORE_CHRIST.containsMatchIn(match) -> {
            -match.replace(BEFORE_CHRIST, "").safeParseInt()
        }

        AFTER_CHRIST.containsMatchIn(match) -> {
            match.replace(AFTER_CHRIST, "").safeParseInt() ?: 0
        }

        else -> {
            val rawYearNumber = match.safeParseInt() ?: 0
            return findMostLikelyADYear(rawYearNumber)
        }
    }
}

internal fun parseTimeUnits(timeUnitText: String): KronoDecimalTimeUnits =
    org.kgusarov.krono.common.parseTimeUnits(
        timeUnitText,
        NlConstants.SINGLE_TIME_UNIT_REGEX,
        NlConstants.TIME_UNIT_DICTIONARY,
        ::parseNumberPattern,
    )
