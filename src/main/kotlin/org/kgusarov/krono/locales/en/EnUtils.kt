package org.kgusarov.krono.locales.en

import org.kgusarov.krono.calculation.findMostLikelyADYear

object EnUtils {
    @JvmStatic
    private val BE = Regex("BE", RegexOption.IGNORE_CASE)

    @JvmStatic
    private val BCE = Regex("BCE?", RegexOption.IGNORE_CASE)

    @JvmStatic
    private val AD = Regex("(AD|CE)", RegexOption.IGNORE_CASE)

    fun parseYear(match: String): Int {
        if (BE.containsMatchIn(match)) {
            return match.replace(BE, "").toInt() - 543
        }

        if (BCE.containsMatchIn(match)) {
            return -match.replace(BCE, "").toInt()
        }

        if (AD.containsMatchIn(match)) {
            return match.replace(AD, "").toInt()
        }

        val rawYearNumber = match.toInt()
        return findMostLikelyADYear(rawYearNumber)
    }
}
