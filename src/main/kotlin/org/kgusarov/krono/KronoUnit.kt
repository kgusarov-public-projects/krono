package org.kgusarov.krono

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

@SuppressFBWarnings("BC_BAD_CAST_TO_ABSTRACT_COLLECTION", "EI_EXPOSE_REP")
enum class KronoUnit(
    private val prettyNames: Set<String>,
) {
    Day("d", "D", "day", "days"),
    Month("M", "month", "months"),
    Year("y", "year", "years"),
    Hour("h", "hour", "hours"),
    Minute("m", "minute", "minutes"),
    Second("s", "second", "seconds"),
    Millisecond("ms", "millisecond", "milliseconds"),
    Week("w", "week", "weeks"),
    Quarter("Q", "quarter", "quarters"),
    ;

    constructor(
        vararg prettyNames: String,
    ) : this(setOf(*prettyNames))

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
