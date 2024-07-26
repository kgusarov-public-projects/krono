package org.kgusarov.krono.locales.en

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.time.DayOfWeek

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
}
