package org.kgusarov.krono

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.extensions.multipliedBy
import java.math.BigDecimal
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.time.temporal.IsoFields

@SuppressFBWarnings("BC_BAD_CAST_TO_ABSTRACT_COLLECTION", "EI_EXPOSE_REP")
enum class KronoUnit(
    private val duration: Duration,
    private val prettyNames: Set<String>,
) {
    Day(ChronoUnit.DAYS.duration, "d", "D", "day", "days"),
    Month(ChronoUnit.MONTHS.duration, "M", "month", "months"),
    Year(ChronoUnit.YEARS.duration, "y", "year", "years"),
    Hour(ChronoUnit.HOURS.duration, "h", "hour", "hours"),
    Minute(ChronoUnit.MINUTES.duration, "m", "minute", "minutes"),
    Second(ChronoUnit.SECONDS.duration, "s", "second", "seconds"),
    Millisecond(ChronoUnit.MILLIS.duration, "ms", "millisecond", "milliseconds"),
    Week(ChronoUnit.WEEKS.duration, "w", "week", "weeks"),
    Quarter(IsoFields.QUARTER_YEARS.duration, "Q", "quarter", "quarters"),
    ;

    constructor(
        duration: Duration,
        vararg prettyNames: String,
    ) : this(duration, setOf(*prettyNames))

    operator fun times(value: BigDecimal) = duration.multipliedBy(value)

    fun prettyNames() = prettyNames

    fun firstPrettyName() = prettyNames.first()

    companion object {
        @JvmStatic
        private val prettyNameToUnit: Map<String, KronoUnit> =
            entries.flatMap {
                it.prettyNames.map { key ->
                    key to it
                }
            }.toMap()

        operator fun get(name: String) = prettyNameToUnit[name]
    }
}
