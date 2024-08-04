package org.kgusarov.krono.locales.en

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoUnit
import org.kgusarov.krono.utils.matchAnyPattern
import org.kgusarov.krono.utils.repeatedTimeUnitPattern
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

    @JvmStatic
    val TIME_UNIT_DICTIONARY_NO_ABBR: Map<String, KronoUnit> =
        mapOf(
            "second" to KronoUnit.Second,
            "seconds" to KronoUnit.Second,
            "minute" to KronoUnit.Minute,
            "minutes" to KronoUnit.Minute,
            "hour" to KronoUnit.Hour,
            "hours" to KronoUnit.Hour,
            "day" to KronoUnit.Day,
            "days" to KronoUnit.Day,
            "week" to KronoUnit.Week,
            "weeks" to KronoUnit.Week,
            "month" to KronoUnit.Month,
            "months" to KronoUnit.Month,
            "quarter" to KronoUnit.Quarter,
            "quarters" to KronoUnit.Quarter,
            "year" to KronoUnit.Year,
            "years" to KronoUnit.Year,
        )

    @JvmStatic
    val TIME_UNIT_DICTIONARY: Map<String, KronoUnit> =
        mapOf(
            "s" to KronoUnit.Second,
            "sec" to KronoUnit.Second,
            "second" to KronoUnit.Second,
            "seconds" to KronoUnit.Second,
            "m" to KronoUnit.Minute,
            "min" to KronoUnit.Minute,
            "mins" to KronoUnit.Minute,
            "minute" to KronoUnit.Minute,
            "minutes" to KronoUnit.Minute,
            "h" to KronoUnit.Hour,
            "hr" to KronoUnit.Hour,
            "hrs" to KronoUnit.Hour,
            "hour" to KronoUnit.Hour,
            "hours" to KronoUnit.Hour,
            "d" to KronoUnit.Day,
            "day" to KronoUnit.Day,
            "days" to KronoUnit.Day,
            "w" to KronoUnit.Week,
            "week" to KronoUnit.Week,
            "weeks" to KronoUnit.Week,
            "mo" to KronoUnit.Month,
            "mon" to KronoUnit.Month,
            "mos" to KronoUnit.Month,
            "month" to KronoUnit.Month,
            "months" to KronoUnit.Month,
            "qtr" to KronoUnit.Quarter,
            "quarter" to KronoUnit.Quarter,
            "quarters" to KronoUnit.Quarter,
            "y" to KronoUnit.Year,
            "yr" to KronoUnit.Year,
            "year" to KronoUnit.Year,
            "years" to KronoUnit.Year,
        ) + TIME_UNIT_DICTIONARY_NO_ABBR

    @JvmStatic
    val INTEGER_WORD_DICTIONARY: Map<String, Int> =
        mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9,
            "ten" to 10,
            "eleven" to 11,
            "twelve" to 12,
        )

    @JvmStatic
    val ORDINAL_WORD_DICTIONARY: Map<String, Int> =
        mapOf(
            "first" to 1,
            "second" to 2,
            "third" to 3,
            "fourth" to 4,
            "fifth" to 5,
            "sixth" to 6,
            "seventh" to 7,
            "eighth" to 8,
            "ninth" to 9,
            "tenth" to 10,
            "eleventh" to 11,
            "twelfth" to 12,
            "thirteenth" to 13,
            "fourteenth" to 14,
            "fifteenth" to 15,
            "sixteenth" to 16,
            "seventeenth" to 17,
            "eighteenth" to 18,
            "nineteenth" to 19,
            "twentieth" to 20,
            "twenty first" to 21,
            "twenty-first" to 21,
            "twenty second" to 22,
            "twenty-second" to 22,
            "twenty third" to 23,
            "twenty-third" to 23,
            "twenty fourth" to 24,
            "twenty-fourth" to 24,
            "twenty fifth" to 25,
            "twenty-fifth" to 25,
            "twenty sixth" to 26,
            "twenty-sixth" to 26,
            "twenty seventh" to 27,
            "twenty-seventh" to 27,
            "twenty eighth" to 28,
            "twenty-eighth" to 28,
            "twenty ninth" to 29,
            "twenty-ninth" to 29,
            "thirtieth" to 30,
            "thirty first" to 31,
            "thirty-first" to 31,
        )

    @JvmStatic
    val NUMBER_PATTERN =
        "(?:${matchAnyPattern(INTEGER_WORD_DICTIONARY)}" +
            "|[0-9]+|[0-9]+\\.[0-9]+" +
            "|half(?:\\s{0,2}an?)?" +
            "|an?\\b(?:\\s{0,2}few)?" +
            "|few" +
            "|several" +
            "|the" +
            "|a?\\s{0,2}couple\\s{0,2}(?:of)?)"

    @JvmStatic
    val SINGLE_TIME_UNIT_PATTERN = "($NUMBER_PATTERN)\\s{0,3}(${matchAnyPattern(TIME_UNIT_DICTIONARY)})"

    @JvmStatic
    val SINGLE_TIME_UNIT_REGEX =
        Regex(
            SINGLE_TIME_UNIT_PATTERN,
            RegexOption.IGNORE_CASE,
        )

    @JvmStatic
    val SINGLE_TIME_UNIT_NO_ABBR_PATTERN = "($NUMBER_PATTERN)\\s{0,3}(${matchAnyPattern(TIME_UNIT_DICTIONARY_NO_ABBR)})"

    @JvmStatic
    val TIME_UNIT_CONNECTOR_PATTERN = "\\s{0,5},?(?:\\s*and)?\\s{0,5}"

    @JvmStatic
    val TIME_UNITS_PATTERN =
        repeatedTimeUnitPattern(
            "(?:(?:about|around)\\s{0,3})?",
            SINGLE_TIME_UNIT_PATTERN,
            TIME_UNIT_CONNECTOR_PATTERN,
        )

    @JvmStatic
    val TIME_UNITS_NO_ABBR_PATTERN =
        repeatedTimeUnitPattern(
            "(?:(?:about|around)\\s{0,3})?",
            SINGLE_TIME_UNIT_NO_ABBR_PATTERN,
            TIME_UNIT_CONNECTOR_PATTERN,
        )

    @JvmStatic
    val ORDINAL_NUMBER_PATTERN = "(?:${matchAnyPattern(ORDINAL_WORD_DICTIONARY)}|[0-9]{1,2}(?:st|nd|rd|th)?)"
}
