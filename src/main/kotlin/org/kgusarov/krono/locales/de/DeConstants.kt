package org.kgusarov.krono.locales.de

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoUnit
import org.kgusarov.krono.utils.matchAnyPattern
import org.kgusarov.krono.utils.repeatedTimeUnitPattern
import java.time.DayOfWeek

@Suppress("RegExpUnnecessaryNonCapturingGroup")
@SuppressFBWarnings("MS_EXPOSE_REP")
internal object DeConstants {
    @JvmStatic
    val WEEKDAY_DICTIONARY: Map<String, DayOfWeek> =
        mapOf(
            "sonntag" to DayOfWeek.SUNDAY,
            "so" to DayOfWeek.SUNDAY,
            "montag" to DayOfWeek.MONDAY,
            "mo" to DayOfWeek.MONDAY,
            "dienstag" to DayOfWeek.TUESDAY,
            "di" to DayOfWeek.TUESDAY,
            "mittwoch" to DayOfWeek.WEDNESDAY,
            "mi" to DayOfWeek.WEDNESDAY,
            "donnerstag" to DayOfWeek.THURSDAY,
            "do" to DayOfWeek.THURSDAY,
            "freitag" to DayOfWeek.FRIDAY,
            "fr" to DayOfWeek.FRIDAY,
            "samstag" to DayOfWeek.SATURDAY,
            "sa" to DayOfWeek.SATURDAY,
        )

    @JvmStatic
    val MONTH_DICTIONARY: Map<String, Int> =
        mapOf(
            "januar" to 1,
            "jänner" to 1,
            "janner" to 1,
            "jan" to 1,
            "jan." to 1,
            "februar" to 2,
            "feber" to 2,
            "feb" to 2,
            "feb." to 2,
            "märz" to 3,
            "maerz" to 3,
            "mär" to 3,
            "mär." to 3,
            "mrz" to 3,
            "mrz." to 3,
            "april" to 4,
            "apr" to 4,
            "apr." to 4,
            "mai" to 5,
            "juni" to 6,
            "jun" to 6,
            "jun." to 6,
            "juli" to 7,
            "jul" to 7,
            "jul." to 7,
            "august" to 8,
            "aug" to 8,
            "aug." to 8,
            "september" to 9,
            "sep" to 9,
            "sep." to 9,
            "sept" to 9,
            "sept." to 9,
            "oktober" to 10,
            "okt" to 10,
            "okt." to 10,
            "november" to 11,
            "nov" to 11,
            "nov." to 11,
            "dezember" to 12,
            "dez" to 12,
            "dez." to 12,
        )

    @JvmStatic
    val INTEGER_WORD_DICTIONARY: Map<String, Int> =
        mapOf(
            "eins" to 1,
            "eine" to 1,
            "einem" to 1,
            "einen" to 1,
            "einer" to 1,
            "zwei" to 2,
            "drei" to 3,
            "vier" to 4,
            "fünf" to 5,
            "fuenf" to 5,
            "sechs" to 6,
            "sieben" to 7,
            "acht" to 8,
            "neun" to 9,
            "zehn" to 10,
            "elf" to 11,
            "zwölf" to 12,
            "zwoelf" to 12,
        )

    @JvmStatic
    val TIME_UNIT_DICTIONARY: Map<String, KronoUnit> =
        mapOf(
            "sek" to KronoUnit.Second,
            "sekunde" to KronoUnit.Second,
            "sekunden" to KronoUnit.Second,
            "min" to KronoUnit.Minute,
            "minute" to KronoUnit.Minute,
            "minuten" to KronoUnit.Minute,
            "h" to KronoUnit.Hour,
            "std" to KronoUnit.Hour,
            "stunde" to KronoUnit.Hour,
            "stunden" to KronoUnit.Hour,
            "tag" to KronoUnit.Day,
            "tage" to KronoUnit.Day,
            "tagen" to KronoUnit.Day,
            "woche" to KronoUnit.Week,
            "wochen" to KronoUnit.Week,
            "monat" to KronoUnit.Month,
            "monate" to KronoUnit.Month,
            "monaten" to KronoUnit.Month,
            "monats" to KronoUnit.Month,
            "quartal" to KronoUnit.Quarter,
            "quartals" to KronoUnit.Quarter,
            "quartale" to KronoUnit.Quarter,
            "quartalen" to KronoUnit.Quarter,
            "a" to KronoUnit.Year,
            "j" to KronoUnit.Year,
            "jr" to KronoUnit.Year,
            "jahr" to KronoUnit.Year,
            "jahre" to KronoUnit.Year,
            "jahren" to KronoUnit.Year,
            "jahres" to KronoUnit.Year,
        )

    @JvmStatic
    val NUMBER_PATTERN =
        "(?:${matchAnyPattern(INTEGER_WORD_DICTIONARY)}|[0-9]+|[0-9]+\\.[0-9]+|halb?|halbe?|einigen?|wenigen?|mehreren?)"

    @JvmStatic
    val YEAR_PATTERN =
        "(?:[0-9]{1,4}(?:\\s*[vn]\\.?\\s*(?:C(?:hr)?|(?:u\\.?|d\\.?(?:\\s*g\\.?)?)?\\s*Z)\\.?|\\s*(?:u\\.?|d\\.?(?:\\s*g\\.)?)\\s*Z\\.?)?)"

    @JvmStatic
    val SINGLE_TIME_UNIT_PATTERN = "(${NUMBER_PATTERN})\\s{0,5}(${matchAnyPattern(TIME_UNIT_DICTIONARY)})\\s{0,5}"

    @JvmStatic
    val SINGLE_TIME_UNIT_REGEX = Regex(SINGLE_TIME_UNIT_PATTERN, RegexOption.IGNORE_CASE)

    @JvmStatic
    val TIME_UNITS_PATTERN = repeatedTimeUnitPattern("", SINGLE_TIME_UNIT_PATTERN)
}
