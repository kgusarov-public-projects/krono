package org.kgusarov.krono.locales.es

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoUnit
import org.kgusarov.krono.utils.matchAnyPattern
import org.kgusarov.krono.utils.repeatedTimeUnitPattern
import java.time.DayOfWeek

@Suppress("RegExpUnnecessaryNonCapturingGroup")
@SuppressFBWarnings("MS_EXPOSE_REP")
internal object EsConstants {
    @JvmStatic
    val WEEKDAY_DICTIONARY: Map<String, DayOfWeek> =
        mapOf(
            "domingo" to DayOfWeek.SUNDAY,
            "dom" to DayOfWeek.SUNDAY,
            "lunes" to DayOfWeek.MONDAY,
            "lun" to DayOfWeek.MONDAY,
            "martes" to DayOfWeek.TUESDAY,
            "mar" to DayOfWeek.TUESDAY,
            "miércoles" to DayOfWeek.WEDNESDAY,
            "miercoles" to DayOfWeek.WEDNESDAY,
            "mié" to DayOfWeek.WEDNESDAY,
            "mie" to DayOfWeek.WEDNESDAY,
            "jueves" to DayOfWeek.THURSDAY,
            "jue" to DayOfWeek.THURSDAY,
            "viernes" to DayOfWeek.FRIDAY,
            "vie" to DayOfWeek.FRIDAY,
            "sábado" to DayOfWeek.SATURDAY,
            "sabado" to DayOfWeek.SATURDAY,
            "sáb" to DayOfWeek.SATURDAY,
            "sab" to DayOfWeek.SATURDAY,
        )

    @JvmStatic
    val MONTH_DICTIONARY: Map<String, Int> =
        mapOf(
            "enero" to 1,
            "ene" to 1,
            "ene." to 1,
            "feb" to 2,
            "feb." to 2,
            "febrero" to 2,
            "marzo" to 3,
            "mar" to 3,
            "mar." to 3,
            "abril" to 4,
            "abr" to 4,
            "abr." to 4,
            "mayo" to 5,
            "may" to 5,
            "may." to 5,
            "junio" to 6,
            "jun" to 6,
            "jun." to 6,
            "julio" to 7,
            "jul" to 7,
            "jul." to 7,
            "agosto" to 8,
            "ago" to 8,
            "ago." to 8,
            "setiembre" to 9,
            "septiembre" to 9,
            "sep" to 9,
            "sep." to 9,
            "octubre" to 10,
            "oct" to 10,
            "oct." to 10,
            "noviembre" to 11,
            "nov" to 11,
            "nov." to 11,
            "diciembre" to 12,
            "dic" to 12,
            "dic." to 12,
        )

    @JvmStatic
    val INTEGER_WORD_DICTIONARY: Map<String, Int> =
        mapOf(
            "uno" to 1,
            "dos" to 2,
            "tres" to 3,
            "cuatro" to 4,
            "cinco" to 5,
            "seis" to 6,
            "siete" to 7,
            "ocho" to 8,
            "nueve" to 9,
            "diez" to 10,
            "once" to 11,
            "doce" to 12,
            "trece" to 13,
        )

    @JvmStatic
    val TIME_UNIT_DICTIONARY: Map<String, KronoUnit> =
        mapOf(
            "sec" to KronoUnit.Second,
            "segundo" to KronoUnit.Second,
            "segundos" to KronoUnit.Second,
            "min" to KronoUnit.Minute,
            "mins" to KronoUnit.Minute,
            "minuto" to KronoUnit.Minute,
            "minutos" to KronoUnit.Minute,
            "h" to KronoUnit.Hour,
            "hr" to KronoUnit.Hour,
            "hrs" to KronoUnit.Hour,
            "hora" to KronoUnit.Hour,
            "horas" to KronoUnit.Hour,
            "día" to KronoUnit.Day,
            "días" to KronoUnit.Day,
            "semana" to KronoUnit.Week,
            "semanas" to KronoUnit.Week,
            "mes" to KronoUnit.Month,
            "meses" to KronoUnit.Month,
            "cuarto" to KronoUnit.Quarter,
            "cuartos" to KronoUnit.Quarter,
            "año" to KronoUnit.Year,
            "años" to KronoUnit.Year,
        )

    @JvmStatic
    val NUMBER_PATTERN =
        "(?:${matchAnyPattern(INTEGER_WORD_DICTIONARY)}|[0-9]+|[0-9]+\\.[0-9]+|un?|uno?|una?|algunos?|unos?|demi-?)"

    @JvmStatic
    val YEAR_PATTERN = "[0-9]{1,4}(?![^\\s]\\d)(?:\\s*[a|d]\\.?\\s*c\\.?|\\s*a\\.?\\s*d\\.?)?"

    @JvmStatic
    val SINGLE_TIME_UNIT_PATTERN = "($NUMBER_PATTERN)\\s{0,5}(${matchAnyPattern(TIME_UNIT_DICTIONARY)})\\s{0,5}"

    @JvmStatic
    val SINGLE_TIME_UNIT_REGEX = Regex(SINGLE_TIME_UNIT_PATTERN, RegexOption.IGNORE_CASE)

    @JvmStatic
    val TIME_UNITS_PATTERN = repeatedTimeUnitPattern("", SINGLE_TIME_UNIT_PATTERN)
}
