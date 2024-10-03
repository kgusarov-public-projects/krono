package org.kgusarov.krono

import org.assertj.core.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class KronoTest {
    @ParameterizedTest
    @MethodSource("dateParsingArgs")
    internal fun dateParsing(value: String, expected: String) {
        testSingleCase(Krono.enCasual, value) {
            Assertions.assertThat(it.text).isEqualTo(value)
            Assertions.assertThat(it.index).isEqualTo(0)

            with(it.start) {
                assertOffsetDate(expected)
            }
        }
    }

    companion object {
        @JvmStatic
        fun dateParsingArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("1994-11-05T13:15:30Z", "1994-11-05T13:15:30Z"),
            Arguments.of("1994-11-05T08:15:30-05:30", "1994-11-05T08:15:30-05:30"),
            Arguments.of("1994-11-05T08:15:30+11:30", "1994-11-05T08:15:30+11:30"),
            Arguments.of("2014-11-30T08:15:30-05:30", "2014-11-30T08:15:30-05:30"),
            Arguments.of("Sat, 21 Feb 2015 11:50:48 -0500", "2015-02-21T11:50:48-05:00"),
            Arguments.of("22 Feb 2015 04:12:00 -0000", "2015-02-22T04:12:00-00:00"),
            Arguments.of("1900-01-01T00:00:00-01:00", "1900-01-01T00:00:00-01:00"),
            Arguments.of("1900-01-01T00:00:00-00:00", "1900-01-01T00:00:00-00:00"),
            Arguments.of("9999-12-31T23:59:00-00:00", "9999-12-31T23:59:00-00:00"),
            Arguments.of("09/25/2017 10:31:50.522 PM", "2017-09-25T22:31:50.522Z"),
            Arguments.of("Sat Nov 05 1994 22:45:30 GMT+0900 (JST)", "1994-11-05T22:45:30+09:00"),
            Arguments.of("Fri, 31 Mar 2000 07:00:00 UTC", "2000-03-31T07:00:00Z"),
            Arguments.of("2014-12-14T18:22:14.759Z", "2014-12-14T18:22:14.759Z"),
            Arguments.of("2024-01-01T00:00", "2024-01-01T00:00:00Z"),
        )
    }
}
