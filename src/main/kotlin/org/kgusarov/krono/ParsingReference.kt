package org.kgusarov.krono

data class ParsingReference(
    /**
     * Reference date. The instant when the input is written or mention.
     * This effect date/time implication (e.g. weekday or time mentioning).
     * (default = now)
     */
    val instant: KronoDate = KronoDate.now(),
    /**
     * Reference timezone. The timezone where the input is written or mention.
     * Date/time implication will account the difference between input timezone and the current system timezone.
     * (default = current timezone)
     */
    val timezone: KronoTimezone = KronoTimezone.systemDefault(),
) {
    constructor(instant: KronoDate, timezone: String) : this(instant, KronoTimezone.of(timezone))
}
