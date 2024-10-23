package org.kgusarov.krono.locales.fr

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoUnit
import org.kgusarov.krono.utils.matchAnyPattern
import org.kgusarov.krono.utils.repeatedTimeUnitPattern
import java.time.DayOfWeek

@Suppress("RegExpUnnecessaryNonCapturingGroup")
@SuppressFBWarnings("MS_EXPOSE_REP")
internal object FrConstants {
    @JvmStatic
    val WEEKDAY_DICTIONARY: Map<String, DayOfWeek> =
        mapOf(
            "dimanche" to DayOfWeek.SUNDAY,
            "dim" to DayOfWeek.SUNDAY,
            "lundi" to DayOfWeek.MONDAY,
            "lun" to DayOfWeek.MONDAY,
            "mardi" to DayOfWeek.TUESDAY,
            "mar" to DayOfWeek.TUESDAY,
            "mercredi" to DayOfWeek.WEDNESDAY,
            "mer" to DayOfWeek.WEDNESDAY,
            "jeudi" to DayOfWeek.THURSDAY,
            "jeu" to DayOfWeek.THURSDAY,
            "vendredi" to DayOfWeek.FRIDAY,
            "ven" to DayOfWeek.FRIDAY,
            "samedi" to DayOfWeek.SATURDAY,
            "sam" to DayOfWeek.SATURDAY,
        )

    @JvmStatic
    val MONTH_DICTIONARY: Map<String, Int> =
        mapOf(
            "janvier" to 1,
            "jan" to 1,
            "jan." to 1,
            "février" to 2,
            "fév" to 2,
            "fév." to 2,
            "fevrier" to 2,
            "fev" to 2,
            "fev." to 2,
            "mars" to 3,
            "mar" to 3,
            "mar." to 3,
            "avril" to 4,
            "avr" to 4,
            "avr." to 4,
            "mai" to 5,
            "juin" to 6,
            "jun" to 6,
            "juillet" to 7,
            "juil" to 7,
            "jul" to 7,
            "jul." to 7,
            "août" to 8,
            "aout" to 8,
            "septembre" to 9,
            "sep" to 9,
            "sep." to 9,
            "sept" to 9,
            "sept." to 9,
            "octobre" to 10,
            "oct" to 10,
            "oct." to 10,
            "novembre" to 11,
            "nov" to 11,
            "nov." to 11,
            "décembre" to 12,
            "decembre" to 12,
            "dec" to 12,
            "dec." to 12,
        )

    @JvmStatic
    val INTEGER_WORD_DICTIONARY: Map<String, Int> =
        mapOf(
            "un" to 1,
            "deux" to 2,
            "trois" to 3,
            "quatre" to 4,
            "cinq" to 5,
            "six" to 6,
            "sept" to 7,
            "huit" to 8,
            "neuf" to 9,
            "dix" to 10,
            "onze" to 11,
            "douze" to 12,
            "treize" to 13,
        )

    @JvmStatic
    val TIME_UNIT_DICTIONARY: Map<String, KronoUnit> =
        mapOf(
            "sec" to KronoUnit.Second,
            "seconde" to KronoUnit.Second,
            "secondes" to KronoUnit.Second,
            "min" to KronoUnit.Minute,
            "mins" to KronoUnit.Minute,
            "minute" to KronoUnit.Minute,
            "minutes" to KronoUnit.Minute,
            "h" to KronoUnit.Hour,
            "hr" to KronoUnit.Hour,
            "hrs" to KronoUnit.Hour,
            "heure" to KronoUnit.Hour,
            "heures" to KronoUnit.Hour,
            "jour" to KronoUnit.Day,
            "jours" to KronoUnit.Day,
            "semaine" to KronoUnit.Week,
            "semaines" to KronoUnit.Week,
            "mois" to KronoUnit.Month,
            "trimestre" to KronoUnit.Quarter,
            "trimestres" to KronoUnit.Quarter,
            "ans" to KronoUnit.Year,
            "année" to KronoUnit.Year,
            "années" to KronoUnit.Year,
        )

    @JvmStatic
    val NUMBER_PATTERN =
        "(?:${matchAnyPattern(INTEGER_WORD_DICTIONARY)}|[0-9]+|[0-9]+\\.[0-9]+|une?\\b|quelques?|demi-?)"

    @JvmStatic
    val ORDINAL_NUMBER_PATTERN = "(?:[0-9]{1,2}(?:er)?)"

    @JvmStatic
    val YEAR_PATTERN = "(?:[1-9][0-9]{0,3}\\s*(?:AC|AD|p\\.\\s*C(?:hr?)?\\.\\s*n\\.)|[1-2][0-9]{3}|[5-9][0-9])"

    @JvmStatic
    val SINGLE_TIME_UNIT_PATTERN = "(${NUMBER_PATTERN})\\s{0,5}(${matchAnyPattern(TIME_UNIT_DICTIONARY)})\\s{0,5}"

    @JvmStatic
    val SINGLE_TIME_UNIT_REGEX = Regex(SINGLE_TIME_UNIT_PATTERN, RegexOption.IGNORE_CASE)

    @JvmStatic
    val TIME_UNITS_PATTERN = repeatedTimeUnitPattern("", SINGLE_TIME_UNIT_PATTERN)
}
