package org.kgusarov.krono

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.ThrowingConsumer
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.temporal.ChronoUnit
import java.time.temporal.IsoFields
import java.time.temporal.TemporalUnit
import java.util.stream.Stream

internal class KronoUnitTest {
    @ParameterizedTest
    @MethodSource("verifyUnitArgs")
    fun verifyUnit(name: String, unit: TemporalUnit) {
        val kronoUnit = KronoUnit[name]
        assertThat(kronoUnit).isNotNull.satisfies(ThrowingConsumer {
            assertThat(it!!()).isEqualTo(unit.duration)
        })
    }

    companion object {
        @JvmStatic
        fun verifyUnitArgs(): Stream<Arguments> =
            Stream.of(
                Arguments.of("d", ChronoUnit.DAYS),
                Arguments.of("D", ChronoUnit.DAYS),
                Arguments.of("day", ChronoUnit.DAYS),
                Arguments.of("days", ChronoUnit.DAYS),

                Arguments.of("M", ChronoUnit.MONTHS),
                Arguments.of("month", ChronoUnit.MONTHS),
                Arguments.of("months", ChronoUnit.MONTHS),

                Arguments.of("y", ChronoUnit.YEARS),
                Arguments.of("year", ChronoUnit.YEARS),
                Arguments.of("years", ChronoUnit.YEARS),

                Arguments.of("h", ChronoUnit.HOURS),
                Arguments.of("hour", ChronoUnit.HOURS),
                Arguments.of("hours", ChronoUnit.HOURS),

                Arguments.of("m", ChronoUnit.MINUTES),
                Arguments.of("minute", ChronoUnit.MINUTES),
                Arguments.of("minutes", ChronoUnit.MINUTES),

                Arguments.of("s", ChronoUnit.SECONDS),
                Arguments.of("second", ChronoUnit.SECONDS),
                Arguments.of("seconds", ChronoUnit.SECONDS),

                Arguments.of("ms", ChronoUnit.MILLIS),
                Arguments.of("millisecond", ChronoUnit.MILLIS),
                Arguments.of("milliseconds", ChronoUnit.MILLIS),

                Arguments.of("w", ChronoUnit.WEEKS),
                Arguments.of("week", ChronoUnit.WEEKS),
                Arguments.of("weeks", ChronoUnit.WEEKS),

                Arguments.of("Q", IsoFields.QUARTER_YEARS),
                Arguments.of("quarter", IsoFields.QUARTER_YEARS),
                Arguments.of("quarters", IsoFields.QUARTER_YEARS),
            )
    }
}
