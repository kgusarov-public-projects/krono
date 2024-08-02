package org.kgusarov.krono.locales.en

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.time.DayOfWeek

@Suppress("RegExpUnnecessaryNonCapturingGroup")
@SuppressFBWarnings("MS_EXPOSE_REP")
internal object EnConstants {
    @JvmStatic
    val WEEKDAY_DICTIONARY: Map<String, DayOfWeek> =
        mapOf(
            "sunday" to DayOfWeek.SUNDAY,
            "sun" to DayOfWeek.SUNDAY,
            "sun." to DayOfWeek.SUNDAY,
            "monday" to DayOfWeek.MONDAY,
            "mon" to DayOfWeek.MONDAY,
            "mon." to DayOfWeek.MONDAY,
            "tuesday" to DayOfWeek.TUESDAY,
            "tue" to DayOfWeek.TUESDAY,
            "tue." to DayOfWeek.TUESDAY,
            "wednesday" to DayOfWeek.WEDNESDAY,
            "wed" to DayOfWeek.WEDNESDAY,
            "wed." to DayOfWeek.WEDNESDAY,
            "thursday" to DayOfWeek.THURSDAY,
            "thurs" to DayOfWeek.THURSDAY,
            "thurs." to DayOfWeek.THURSDAY,
            "thur" to DayOfWeek.THURSDAY,
            "thur." to DayOfWeek.THURSDAY,
            "thu" to DayOfWeek.THURSDAY,
            "thu." to DayOfWeek.THURSDAY,
            "friday" to DayOfWeek.FRIDAY,
            "fri" to DayOfWeek.FRIDAY,
            "fri." to DayOfWeek.FRIDAY,
            "saturday" to DayOfWeek.SATURDAY,
            "sat" to DayOfWeek.SATURDAY,
            "sat." to DayOfWeek.SATURDAY,
        )

    @JvmStatic
    val FULL_MONTH_NAME_DICTIONARY: Map<String, Int> =
        mapOf(
            "january" to 1,
            "february" to 2,
            "march" to 3,
            "april" to 4,
            "may" to 5,
            "june" to 6,
            "july" to 7,
            "august" to 8,
            "september" to 9,
            "october" to 10,
            "november" to 11,
            "december" to 12,
        )

    @JvmStatic
    val MONTH_DICTIONARY: Map<String, Int> =
        mapOf(
            "jan" to 1,
            "jan." to 1,
            "feb" to 2,
            "feb." to 2,
            "mar" to 3,
            "mar." to 3,
            "apr" to 4,
            "apr." to 4,
            "jun" to 6,
            "jun." to 6,
            "jul" to 7,
            "jul." to 7,
            "aug" to 8,
            "aug." to 8,
            "sep" to 9,
            "sep." to 9,
            "sept" to 9,
            "sept." to 9,
            "oct" to 10,
            "oct." to 10,
            "nov" to 11,
            "nov." to 11,
            "dec" to 12,
            "dec." to 12,
        ) + FULL_MONTH_NAME_DICTIONARY

    @JvmStatic
    val YEAR_PATTERN = Regex("(?:[1-9][0-9]{0,3}\\s{0,2}(?:BE|AD|BC|BCE|CE)|[1-2][0-9]{3}|[5-9][0-9]|2[0-5])")
}
