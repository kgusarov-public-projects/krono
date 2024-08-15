package org.kgusarov.krono.calculation

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.KronoDate
import java.util.stream.Stream

internal class YearsTest {
    @ParameterizedTest
    @MethodSource("mostLikelyADYearArgs")
    fun mostLikelyADYear(input: Int, output: Int) {
        assertThat(findMostLikelyADYear(input)).isEqualTo(output)
    }

    @ParameterizedTest
    @MethodSource("yearClosestToRefArgs")
    fun yearClosestToRef(refDate: KronoDate, day: Int, month: Int, expected: Int) {
        assertThat(findYearClosestToRef(refDate, day, month)).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun mostLikelyADYearArgs(): Stream<Arguments> =
            Stream.of(
                Arguments.of(1997, 1997),
                Arguments.of(97, 1997),
                Arguments.of(12, 2012),
                Arguments.of(50, 2050),
                Arguments.of(51, 1951),
                Arguments.of(49, 2049),
            )

        @JvmStatic
        fun yearClosestToRefArgs(): Stream<Arguments> =
            Stream.of(
                Arguments.of(KronoDate.parse("2010-12-31T23:59:00"), 1, 12, 2010),
                Arguments.of(KronoDate.parse("2010-12-31T23:59:00"), 1, 1, 2011),
                Arguments.of(KronoDate.parse("2010-12-31T23:59:00"), 31, 12, 2010),
                Arguments.of(KronoDate.parse("2010-06-01T00:00:00"), 1, 12, 2009),
                Arguments.of(KronoDate.parse("2013-08-10T00:00:00"), 29, 2, 2013),
                Arguments.of(KronoDate.parse("2013-08-10T00:00:00"), -1, 2, 2013),
            )
    }
}
