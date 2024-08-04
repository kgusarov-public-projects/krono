package org.kgusarov.krono

import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoField
import java.time.temporal.TemporalField

typealias KronoDate = LocalDateTime
typealias KronoTimezone = ZoneId
typealias KronoComponent = TemporalField
typealias KronoTimeUnits = MutableMap<KronoUnit, Int?>
typealias KronoDecimalTimeUnits = MutableMap<KronoUnit, BigDecimal>

object KronoComponents {
    val Year = ChronoField.YEAR
    val Month = ChronoField.MONTH_OF_YEAR
    val Day = ChronoField.DAY_OF_MONTH
    val Hour = ChronoField.HOUR_OF_DAY
    val Minute = ChronoField.MINUTE_OF_HOUR
    val Second = ChronoField.SECOND_OF_MINUTE
    val Millisecond = ChronoField.MILLI_OF_SECOND
    val Offset = ChronoField.OFFSET_SECONDS
    val Weekday = ChronoField.DAY_OF_WEEK
    val Meridiem = ChronoField.AMPM_OF_DAY
}

object KronoMeridiem {
    @JvmStatic
    val AM: Int = 0

    @JvmStatic
    val PM: Int = 1
}
