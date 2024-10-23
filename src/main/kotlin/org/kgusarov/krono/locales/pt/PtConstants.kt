package org.kgusarov.krono.locales.pt

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.time.DayOfWeek

@Suppress("RegExpUnnecessaryNonCapturingGroup", "RegExpSimplifiable")
@SuppressFBWarnings("MS_EXPOSE_REP")
internal object PtConstants {
    @JvmStatic
    val WEEKDAY_DICTIONARY: Map<String, DayOfWeek> =
        mapOf(
            "domingo" to DayOfWeek.SUNDAY,
            "dom" to DayOfWeek.SUNDAY,
            "segunda" to DayOfWeek.MONDAY,
            "segunda-feira" to DayOfWeek.MONDAY,
            "seg" to DayOfWeek.MONDAY,
            "terça" to DayOfWeek.TUESDAY,
            "terça-feira" to DayOfWeek.TUESDAY,
            "ter" to DayOfWeek.TUESDAY,
            "quarta" to DayOfWeek.WEDNESDAY,
            "quarta-feira" to DayOfWeek.WEDNESDAY,
            "qua" to DayOfWeek.WEDNESDAY,
            "quinta" to DayOfWeek.THURSDAY,
            "quinta-feira" to DayOfWeek.THURSDAY,
            "qui" to DayOfWeek.THURSDAY,
            "sexta" to DayOfWeek.FRIDAY,
            "sexta-feira" to DayOfWeek.FRIDAY,
            "sex" to DayOfWeek.FRIDAY,
            "sábado" to DayOfWeek.SATURDAY,
            "sabado" to DayOfWeek.SATURDAY,
            "sab" to DayOfWeek.SATURDAY,
        )

    @JvmStatic
    val MONTH_DICTIONARY: Map<String, Int> =
        mapOf(
            "janeiro" to 1,
            "jan" to 1,
            "jan." to 1,
            "fevereiro" to 2,
            "fev" to 2,
            "fev." to 2,
            "março" to 3,
            "mar" to 3,
            "mar." to 3,
            "abril" to 4,
            "abr" to 4,
            "abr." to 4,
            "maio" to 5,
            "mai" to 5,
            "mai." to 5,
            "junho" to 6,
            "jun" to 6,
            "jun." to 6,
            "julho" to 7,
            "jul" to 7,
            "jul." to 7,
            "agosto" to 8,
            "ago" to 8,
            "ago." to 8,
            "setembro" to 9,
            "set" to 9,
            "set." to 9,
            "outubro" to 10,
            "out" to 10,
            "out." to 10,
            "novembro" to 11,
            "nov" to 11,
            "nov." to 11,
            "dezembro" to 12,
            "dez" to 12,
            "dez." to 12,
        )

    @JvmStatic
    val YEAR_PATTERN = "[0-9]{1,4}(?![^\\s]\\d)(?:\\s*[a|d]\\.?\\s*c\\.?|\\s*a\\.?\\s*d\\.?)?"
}
