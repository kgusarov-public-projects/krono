package org.kgusarov.krono

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.Month
import java.util.stream.Stream

internal class TimezonesTest {
    @ParameterizedTest
    @MethodSource("checkNthWeekdayOfMonthArgs")
    fun checkNthWeekdayOfMonth(
        year: Int,
        month: Month,
        weekday: DayOfWeek,
        n: Int,
        hour: Int,
        expected: LocalDateTime,
    ) {
        val actual = getNthWeekdayOfMonth(year, month, weekday, n, hour)
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @MethodSource("checkLastWeekdayOfMonth")
    fun checkLastWeekdayOfMonth(
        year: Int,
        month: Month,
        weekday: DayOfWeek,
        hour: Int,
        expected: LocalDateTime,
    ) {
        val actual = getLastWeekdayOfMonth(year, month, weekday, hour)
        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun checkNthWeekdayOfMonthArgs(): Stream<Arguments> =
            Stream.of(
                Arguments.of(
                    2024, Month.JULY, DayOfWeek.MONDAY, 1, 0, LocalDateTime.of(2024, Month.JULY, 1, 0, 0)
                ),
                Arguments.of(
                    2024, Month.JUNE, DayOfWeek.WEDNESDAY, 2, 9, LocalDateTime.of(2024, Month.JUNE, 12, 9, 0)
                )
            )

        @JvmStatic
        fun checkLastWeekdayOfMonth(): Stream<Arguments> =
            Stream.of(
                Arguments.of(
                    2024, Month.JULY, DayOfWeek.MONDAY, 0, LocalDateTime.of(2024, Month.JULY, 29, 0, 0)
                ),
                Arguments.of(
                    2024, Month.JULY, DayOfWeek.FRIDAY, 8, LocalDateTime.of(2024, Month.JULY, 26, 8, 0)
                ),
                Arguments.of(
                    2024, Month.JUNE, DayOfWeek.MONDAY, 9, LocalDateTime.of(2024, Month.JUNE, 24, 9, 0)
                )
            )
    }
}
