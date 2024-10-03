package org.kgusarov.krono.common.calculation

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.KronoDate
import org.kgusarov.krono.ReferenceWithTimezone
import java.time.DayOfWeek
import java.util.stream.Stream

internal class WeekdaysTest {
    @ParameterizedTest
    @MethodSource("thisWeekdayArgs")
    internal fun thisWeekday(ref: String, weekday: DayOfWeek, expected: String) {
        val reference = ReferenceWithTimezone(KronoDate.parse(ref))
        val output = createParsingComponentsAtWeekday(reference, weekday, "this")
        assertThat(output.instant()).isEqualTo(KronoDate.parse(expected))
    }

    @ParameterizedTest
    @MethodSource("lastWeekdayArgs")
    internal fun lastWeekday(ref: String, weekday: DayOfWeek, expected: String) {
        val reference = ReferenceWithTimezone(KronoDate.parse(ref))
        val output = createParsingComponentsAtWeekday(reference, weekday, "last")
        assertThat(output.instant()).isEqualTo(KronoDate.parse(expected))
    }

    @ParameterizedTest
    @MethodSource("nextWeekdayArgs")
    internal fun nextWeekday(ref: String, weekday: DayOfWeek, expected: String) {
        val reference = ReferenceWithTimezone(KronoDate.parse(ref))
        val output = createParsingComponentsAtWeekday(reference, weekday, "next")
        assertThat(output.instant()).isEqualTo(KronoDate.parse(expected))
    }

    @Test
    internal fun nextWeekInvalidModifier() {
        val reference = ReferenceWithTimezone(KronoDate.parse("2022-08-21T12:00:00"))
        val output = createParsingComponentsAtWeekday(reference, DayOfWeek.MONDAY, "some of them")
        assertThat(output.instant()).isEqualTo(KronoDate.parse("2022-08-22T12:00:00"))
    }

    @ParameterizedTest
    @MethodSource("closestWeekdayArgs")
    internal fun closestWeekday(ref: String, weekday: DayOfWeek, expected: Int) {
        val refDate = KronoDate.parse(ref)
        assertThat(getDaysToWeekday(refDate, weekday)).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun thisWeekdayArgs(): Stream<Arguments> =
            Stream.of(
                Arguments.of("2022-08-20T12:00:00", DayOfWeek.MONDAY, "2022-08-22T12:00:00"),
                Arguments.of("2022-08-21T12:00:00", DayOfWeek.FRIDAY, "2022-08-26T12:00:00"),
                Arguments.of("2022-08-02T12:00:00", DayOfWeek.SUNDAY, "2022-08-07T12:00:00"),
            )

        @JvmStatic
        fun lastWeekdayArgs(): Stream<Arguments> =
            Stream.of(
                Arguments.of("2022-08-20T12:00:00", DayOfWeek.FRIDAY, "2022-08-19T12:00:00"),
                Arguments.of("2022-08-20T12:00:00", DayOfWeek.MONDAY, "2022-08-15T12:00:00"),
                Arguments.of("2022-08-20T12:00:00", DayOfWeek.SUNDAY, "2022-08-14T12:00:00"),
                Arguments.of("2022-08-20T12:00:00", DayOfWeek.SATURDAY, "2022-08-13T12:00:00"),
            )

        @JvmStatic
        fun nextWeekdayArgs(): Stream<Arguments> =
            Stream.of(
                Arguments.of("2022-08-21T12:00:00", DayOfWeek.MONDAY, "2022-08-22T12:00:00"),
                Arguments.of("2022-08-21T12:00:00", DayOfWeek.SATURDAY, "2022-08-27T12:00:00"),
                Arguments.of("2022-08-21T12:00:00", DayOfWeek.SUNDAY, "2022-08-28T12:00:00"),
                Arguments.of("2022-08-20T12:00:00", DayOfWeek.FRIDAY, "2022-08-26T12:00:00"),
                Arguments.of("2022-08-20T12:00:00", DayOfWeek.SATURDAY, "2022-08-27T12:00:00"),
                Arguments.of("2022-08-20T12:00:00", DayOfWeek.SUNDAY, "2022-08-28T12:00:00"),
                Arguments.of("2022-08-02T12:00:00", DayOfWeek.MONDAY, "2022-08-08T12:00:00"),
                Arguments.of("2022-08-02T12:00:00", DayOfWeek.FRIDAY, "2022-08-12T12:00:00"),
                Arguments.of("2022-08-02T12:00:00", DayOfWeek.SUNDAY, "2022-08-14T12:00:00"),
            )

        @JvmStatic
        fun closestWeekdayArgs(): Stream<Arguments> =
            Stream.of(
                Arguments.of("2022-08-20T12:00:00", DayOfWeek.SUNDAY, 1),
                Arguments.of("2022-08-20T12:00:00", DayOfWeek.MONDAY, 2),
                Arguments.of("2022-08-20T12:00:00", DayOfWeek.TUESDAY, 3),
                Arguments.of("2022-08-20T12:00:00", DayOfWeek.SATURDAY, 0),
                Arguments.of("2022-08-20T12:00:00", DayOfWeek.FRIDAY, -1),
                Arguments.of("2022-08-20T12:00:00", DayOfWeek.THURSDAY, -2),
                Arguments.of("2022-08-20T12:00:00", DayOfWeek.WEDNESDAY, -3),
            )
    }
}
