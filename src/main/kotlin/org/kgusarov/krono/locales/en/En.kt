package org.kgusarov.krono.locales.en

import org.kgusarov.krono.KronoDecimalTimeUnits
import org.kgusarov.krono.calculation.findMostLikelyADYear
import org.kgusarov.krono.extensions.get
import org.kgusarov.krono.locales.en.EnConstants.INTEGER_WORD_DICTIONARY
import org.kgusarov.krono.locales.en.EnConstants.ORDINAL_WORD_DICTIONARY
import org.kgusarov.krono.locales.en.EnConstants.SINGLE_TIME_UNIT_REGEX
import org.kgusarov.krono.locales.en.EnConstants.TIME_UNIT_DICTIONARY
import java.math.BigDecimal

object En {
    @JvmStatic
    val configuration = EnDefaultConfiguration()

    @JvmStatic
    val casual = configuration.createCasualConfiguration(false)

    @JvmStatic
    val strict = configuration.createConfiguration(strictMode = true, littleEndian = false)

    @JvmStatic
    val gb = configuration.createCasualConfiguration(true)
}

internal fun parseTimeUnits(timeUnitText: String): KronoDecimalTimeUnits {
    val fragments: KronoDecimalTimeUnits = mutableMapOf()
    var remainingText = timeUnitText
    var match = SINGLE_TIME_UNIT_REGEX.find(remainingText)

    while (match != null) {
        collectDateTimeFragment(fragments, match)
        remainingText = remainingText.substring(match.range.last + 1).trim()
        match = SINGLE_TIME_UNIT_REGEX.find(remainingText)
    }

    return fragments
}

internal fun collectDateTimeFragment(
    fragments: KronoDecimalTimeUnits,
    match: MatchResult,
) {
    val num = parseNumberPattern(match[1])
    val unit = TIME_UNIT_DICTIONARY[match[2].lowercase()]!!
    fragments[unit] = num
}

internal fun parseNumberPattern(match: String): BigDecimal {
    val num = match.lowercase()
    return when {
        INTEGER_WORD_DICTIONARY[num] != null -> INTEGER_WORD_DICTIONARY[num]!!.toBigDecimal()
        num == "a" || num == "an" || num == "the" -> BigDecimal.ONE
        num.contains("few") -> BigDecimal(3)
        num.contains("half") -> BigDecimal(0.5)
        num.contains("couple") -> BigDecimal(2)
        num.contains("several") -> BigDecimal(7)
        else -> num.toBigDecimal()
    }
}

@Suppress("RegExpUnnecessaryNonCapturingGroup")
private val ORDINAL_SUFFIX_REGEX =
    Regex(
        "(?:st|nd|rd|th)",
        RegexOption.IGNORE_CASE,
    )

internal fun parseOrdinalNumberPattern(match: String): Int {
    var num = match.lowercase()
    if (ORDINAL_WORD_DICTIONARY[num] != null) {
        return ORDINAL_WORD_DICTIONARY[num]!!
    }

    num = num.replace(ORDINAL_SUFFIX_REGEX, "")
    return if (num.isEmpty()) 0 else num.toInt()
}

private val BE = Regex("BE", RegexOption.IGNORE_CASE)

private val BCE = Regex("BCE?", RegexOption.IGNORE_CASE)

private val AD = Regex("(AD|CE)", RegexOption.IGNORE_CASE)

internal fun parseYear(match: String): Int {
    if (BE.containsMatchIn(match)) {
        return match.replace(BE, "").trim().toInt() - 543
    }

    if (BCE.containsMatchIn(match)) {
        return -match.replace(BCE, "").trim().toInt()
    }

    if (AD.containsMatchIn(match)) {
        return match.replace(AD, "").trim().toInt()
    }

    val rawYearNumber = match.trim().toInt()
    return findMostLikelyADYear(rawYearNumber)
}
