package org.kgusarov.krono

data class ParsingReference(
    val instant: KronoDate = KronoDate.now(),
    val timezone: KronoTimezone = KronoTimezone.systemDefault(),
) {
    constructor(instant: KronoDate, timezone: String) : this(instant, KronoTimezone.of(timezone))
}
