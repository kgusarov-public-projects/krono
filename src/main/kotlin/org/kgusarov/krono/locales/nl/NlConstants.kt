package org.kgusarov.krono.locales.nl

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoUnit
import org.kgusarov.krono.utils.matchAnyPattern
import org.kgusarov.krono.utils.repeatedTimeUnitPattern
import java.time.DayOfWeek

@Suppress("RegExpUnnecessaryNonCapturingGroup", "RegExpSimplifiable")
@SuppressFBWarnings("MS_EXPOSE_REP")
internal object NlConstants {
    @JvmStatic
    val WEEKDAY_DICTIONARY: Map<String, DayOfWeek> =
        mapOf(
            "zondag" to DayOfWeek.SUNDAY,
            "zon" to DayOfWeek.SUNDAY,
            "zon." to DayOfWeek.SUNDAY,
            "zo" to DayOfWeek.SUNDAY,
            "zo." to DayOfWeek.SUNDAY,
            "maandag" to DayOfWeek.MONDAY,
            "ma" to DayOfWeek.MONDAY,
            "ma." to DayOfWeek.MONDAY,
            "dinsdag" to DayOfWeek.TUESDAY,
            "din" to DayOfWeek.TUESDAY,
            "din." to DayOfWeek.TUESDAY,
            "di" to DayOfWeek.TUESDAY,
            "di." to DayOfWeek.TUESDAY,
            "woensdag" to DayOfWeek.WEDNESDAY,
            "woe" to DayOfWeek.WEDNESDAY,
            "woe." to DayOfWeek.WEDNESDAY,
            "wo" to DayOfWeek.WEDNESDAY,
            "wo." to DayOfWeek.WEDNESDAY,
            "donderdag" to DayOfWeek.THURSDAY,
            "dond" to DayOfWeek.THURSDAY,
            "dond." to DayOfWeek.THURSDAY,
            "do" to DayOfWeek.THURSDAY,
            "do." to DayOfWeek.THURSDAY,
            "vrijdag" to DayOfWeek.FRIDAY,
            "vrij" to DayOfWeek.FRIDAY,
            "vrij." to DayOfWeek.FRIDAY,
            "vr" to DayOfWeek.FRIDAY,
            "vr." to DayOfWeek.FRIDAY,
            "zaterdag" to DayOfWeek.SATURDAY,
            "zat" to DayOfWeek.SATURDAY,
            "zat." to DayOfWeek.SATURDAY,
            "za" to DayOfWeek.SATURDAY,
            "za." to DayOfWeek.SATURDAY,
        )

    @JvmStatic
    val MONTH_DICTIONARY: Map<String, Int> =
        mapOf(
            "januari" to 1,
            "jan" to 1,
            "jan." to 1,
            "februari" to 2,
            "feb" to 2,
            "feb." to 2,
            "maart" to 3,
            "mar" to 3,
            "mar." to 3,
            "mrt" to 3,
            "mrt." to 3,
            "april" to 4,
            "apr" to 4,
            "apr." to 4,
            "mei" to 5,
            "juni" to 6,
            "jun" to 6,
            "jun." to 6,
            "juli" to 7,
            "jul" to 7,
            "jul." to 7,
            "augustus" to 8,
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
            "december" to 12,
            "dec" to 12,
            "dec." to 12,
        )

    @JvmStatic
    val INTEGER_WORD_DICTIONARY: Map<String, Int> =
        mapOf(
            "een" to 1,
            "twee" to 2,
            "drie" to 3,
            "vier" to 4,
            "vijf" to 5,
            "zes" to 6,
            "zeven" to 7,
            "acht" to 8,
            "negen" to 9,
            "tien" to 10,
            "elf" to 11,
            "twaalf" to 12,
        )

    @JvmStatic
    val ORDINAL_WORD_DICTIONARY: Map<String, Int> =
        mapOf(
            "eerste" to 1,
            "tweede" to 2,
            "derde" to 3,
            "vierde" to 4,
            "vijfde" to 5,
            "zesde" to 6,
            "zevende" to 7,
            "achtste" to 8,
            "negende" to 9,
            "tiende" to 10,
            "elfde" to 11,
            "twaalfde" to 12,
            "dertiende" to 13,
            "veertiende" to 14,
            "vijftiende" to 15,
            "zestiende" to 16,
            "zeventiende" to 17,
            "achttiende" to 18,
            "negentiende" to 19,
            "twintigste" to 20,
            "eenentwintigste" to 21,
            "tweeëntwintigste" to 22,
            "drieentwintigste" to 23,
            "vierentwintigste" to 24,
            "vijfentwintigste" to 25,
            "zesentwintigste" to 26,
            "zevenentwintigste" to 27,
            "achtentwintigste" to 28,
            "negenentwintigste" to 29,
            "dertigste" to 30,
            "eenendertigste" to 31,
        )

    @JvmStatic
    val TIME_UNIT_DICTIONARY: Map<String, KronoUnit> =
        mapOf(
            "sec" to KronoUnit.Second,
            "second" to KronoUnit.Second,
            "seconden" to KronoUnit.Second,
            "min" to KronoUnit.Minute,
            "mins" to KronoUnit.Minute,
            "minute" to KronoUnit.Minute,
            "minuut" to KronoUnit.Minute,
            "minuten" to KronoUnit.Minute,
            "minuutje" to KronoUnit.Minute,
            "h" to KronoUnit.Hour,
            "hr" to KronoUnit.Hour,
            "hrs" to KronoUnit.Hour,
            "uur" to KronoUnit.Hour,
            "u" to KronoUnit.Hour,
            "uren" to KronoUnit.Hour,
            "dag" to KronoUnit.Day,
            "dagen" to KronoUnit.Day,
            "week" to KronoUnit.Week,
            "weken" to KronoUnit.Week,
            "maand" to KronoUnit.Month,
            "maanden" to KronoUnit.Month,
            "jaar" to KronoUnit.Year,
            "jr" to KronoUnit.Year,
            "jaren" to KronoUnit.Year,
        )

    @JvmStatic
    val NUMBER_PATTERN = "(?:${matchAnyPattern(INTEGER_WORD_DICTIONARY)}|[0-9]+|[0-9]+[\\.,][0-9]+|halve?|half|paar)"

    @JvmStatic
    val ORDINAL_NUMBER_PATTERN = "(?:${matchAnyPattern(ORDINAL_WORD_DICTIONARY)}|[0-9]{1,2}(?:ste|de)?)"

    @JvmStatic
    val YEAR_PATTERN = "(?:[1-9][0-9]{0,3}\\s*(?:voor Christus|na Christus)|[1-2][0-9]{3}|[5-9][0-9])"

    @JvmStatic
    val SINGLE_TIME_UNIT_PATTERN = "(${NUMBER_PATTERN})\\s{0,5}(${matchAnyPattern(TIME_UNIT_DICTIONARY)})\\s{0,5}"

    @JvmStatic
    val SINGLE_TIME_UNIT_REGEX = Regex(SINGLE_TIME_UNIT_PATTERN, RegexOption.IGNORE_CASE)

    @JvmStatic
    val TIME_UNITS_PATTERN = repeatedTimeUnitPattern("(?:(?:binnen|in)\\s*)?", SINGLE_TIME_UNIT_PATTERN)
}
