package org.kgusarov.krono.calculation

import org.kgusarov.krono.KronoDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.abs

internal fun findMostLikelyADYear(yearNumber: Int): Int =
    when {
        yearNumber < 100 ->
            when {
                yearNumber > 50 -> yearNumber + 1900
                else -> yearNumber + 2000
            }

        else -> yearNumber
    }

internal fun findYearClosestToRef(
    refDate: KronoDate,
    day: Int,
    month: Int,
): Int {
    val dateMoment = tryCreateDateMoment(day, refDate, month)

    val nextYear = dateMoment.plusYears(1)
    val lastYear = dateMoment.minusYears(1)
    val refDiff = abs(ChronoUnit.MILLIS.between(dateMoment, refDate))
    val nextDiff = abs(ChronoUnit.MILLIS.between(nextYear, refDate))
    val lastDiff = abs(ChronoUnit.MILLIS.between(lastYear, refDate))

    val closest =
        when {
            nextDiff < refDiff -> nextYear
            lastDiff < refDiff -> lastYear
            else -> dateMoment
        }

    return closest.year
}

private fun tryCreateDateMoment(
    day: Int,
    refDate: KronoDate,
    month: Int,
): LocalDateTime {
    var dayValue = day
    while (day > 0) {
        try {
            return refDate
                .withMonth(month)
                .withDayOfMonth(dayValue)
                .withYear(refDate.year)
        } catch (_: Exception) {
            dayValue--
        }
    }

    return refDate
        .withMonth(month)
        .withYear(refDate.year)
}
