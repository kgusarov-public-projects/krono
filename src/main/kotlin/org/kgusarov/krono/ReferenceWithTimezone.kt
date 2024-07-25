package org.kgusarov.krono

import java.time.ZoneOffset

data class ReferenceWithTimezone(
    val instant: KronoDate = KronoDate.now(),
    val timezone: KronoTimezone? = null,
) {
    constructor(ref: ParsingReference) : this(ref.instant, ref.timezone)

    fun withAdjustedTimezone(): KronoDate = withAdjustedTimezone(instant)

    internal fun withAdjustedTimezone(
        date: KronoDate?,
        offsetSeconds: Int? = null,
    ): KronoDate {
        val ref = date ?: KronoDate.now()
        val targetOffset: KronoTimezone =
            when (offsetSeconds) {
                null -> timezone ?: KronoTimezone.systemDefault()
                else -> ZoneOffset.ofTotalSeconds(offsetSeconds)
            }

        return ref.atZone(targetOffset).toLocalDateTime()
    }
}
