package org.kgusarov.krono.common

import org.kgusarov.krono.KronoDecimalTimeUnits
import org.kgusarov.krono.KronoUnit
import org.kgusarov.krono.extensions.get
import java.math.BigDecimal

typealias ParseNumberPattern = (String) -> BigDecimal?

typealias ParseYear = (String) -> Int

typealias ParseOrdinalNumberPattern = (String) -> Int

internal fun parseTimeUnits(
    timeUnitText: String,
    singleTimeUnitRegex: Regex,
    timeUnitDictionary: Map<String, KronoUnit>,
    parseNumberPattern: ParseNumberPattern,
): KronoDecimalTimeUnits {
    val fragments: KronoDecimalTimeUnits = mutableMapOf()
    var remainingText = timeUnitText
    var match = singleTimeUnitRegex.find(remainingText)

    while (match != null) {
        collectDateTimeFragment(fragments, match, timeUnitDictionary, parseNumberPattern)
        remainingText = remainingText.substring(match.range.last + 1).trim()
        match = singleTimeUnitRegex.find(remainingText)
    }

    return fragments
}

private val ALPHA = Regex("^[a-zA-Z]+$")

internal fun collectDateTimeFragment(
    fragments: KronoDecimalTimeUnits,
    match: MatchResult,
    timeUnitDictionary: Map<String, KronoUnit>,
    parseNumberPattern: ParseNumberPattern,
) {
    if (match[0].matches(ALPHA)) {
        return
    }

    val num = parseNumberPattern(match[1])
    val unit = timeUnitDictionary[match[2].lowercase()]!!
    if (num != null) {
        fragments[unit] = num
    }
}
