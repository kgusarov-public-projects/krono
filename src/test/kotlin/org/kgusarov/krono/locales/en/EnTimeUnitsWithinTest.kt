package org.kgusarov.krono.locales.en

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.Krono
import org.kgusarov.krono.ParsingOption
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase
import org.kgusarov.krono.testUnexpectedResult
import org.kgusarov.krono.testWithExpectedDate
import java.util.stream.Stream

internal class EnTimeUnitsWithinTest {
    @ParameterizedTest
    @MethodSource("singleExpressionArgs")
    internal fun `single expression`(
        text: String,
        refDate: String,
        expectedDate: String,
    ) {
        testWithExpectedDate(
            Krono.enCasual,
            text,
            refDate,
            expectedDate,
        )
    }

    @Test
    internal fun `single implied expression`() {
        testSingleCase(Krono.enCasual, "within 30 days", "2012-08-10T12:14:00") {
            with(it.start) {
                assertThat(certainYear()).isTrue()
                assertThat(certainMonth()).isTrue()
                assertThat(certainDay()).isTrue()
                assertThat(certainHour()).isFalse()
                assertThat(certainMinute()).isFalse()
                assertThat(certainSecond()).isFalse()
            }
        }
    }

    @Test
    internal fun `in 2 minute`() {
        testSingleCase(Krono.enCasual, "in 2 minute", "2016-10-01T14:52:00") {
            with(it.start) {
                assertThat(certainYear()).isTrue()
                assertThat(certainMonth()).isTrue()
                assertThat(certainDay()).isTrue()
                assertThat(certainHour()).isTrue()
                assertThat(certainMinute()).isTrue()
                assertDate("2016-10-01T14:54:00")
            }
        }
    }

    @Test
    internal fun `in 2hour`() {
        testSingleCase(Krono.enCasual, "in 2hour", "2016-10-01T14:52:00") {
            with(it.start) {
                assertThat(certainYear()).isTrue()
                assertThat(certainMonth()).isTrue()
                assertThat(certainDay()).isTrue()
                assertThat(certainHour()).isTrue()
                assertThat(certainMinute()).isTrue()
                assertDate("2016-10-01T16:52:00")
            }
        }
    }

    @Test
    internal fun `in a few year`() {
        testSingleCase(Krono.enCasual, "in a few year", "2016-10-01T14:52:00") {
            with(it.start) {
                assertThat(certainMonth()).isFalse()
                assertThat(certainDay()).isFalse()
                assertThat(certainHour()).isFalse()
                assertThat(certainMinute()).isFalse()
                assertDate("2019-10-01T14:52:00")
            }
        }
    }

    @Test
    internal fun `within 12 month`() {
        testSingleCase(Krono.enCasual, "within 12 month", "2016-10-01T14:52:00") {
            with(it.start) {
                assertThat(certainYear()).isTrue()
                assertThat(certainMonth()).isTrue()
                assertThat(certainDay()).isFalse()
                assertThat(certainHour()).isFalse()
                assertThat(certainMinute()).isFalse()
                assertDate("2017-10-01T14:52:00")
            }
        }
    }

    @Test
    internal fun `within 3 days`() {
        testSingleCase(Krono.enCasual, "within 3 days", "2016-10-01T14:52:00") {
            with(it.start) {
                assertThat(certainYear()).isTrue()
                assertThat(certainMonth()).isTrue()
                assertThat(certainDay()).isTrue()
                assertThat(certainHour()).isFalse()
                assertThat(certainMinute()).isFalse()
                assertDate("2016-10-04T14:52:00")
            }
        }
    }

    @Test
    internal fun `give it 2 months`() {
        testSingleCase(
            Krono.enCasual,
            "give it 2 months",
            "2016-10-01T14:52:00",
            ParsingOption(forwardDate = true),
        ) {
            with(it.start) {
                assertThat(certainYear()).isTrue()
                assertThat(certainMonth()).isTrue()
                assertThat(certainDay()).isFalse()
                assertThat(certainHour()).isFalse()
                assertThat(certainMinute()).isFalse()
                assertDate("2016-12-01T14:52:00")
            }
        }
    }

    @Test
    internal fun `strict mode`() {
        testSingleCase(Krono.enStrict, "in 2hour", "2016-10-01T14:52:00") {
            with(it.start) {
                assertThat(hour()).isEqualTo(16)
                assertThat(minute()).isEqualTo(52)
            }
        }

        testUnexpectedResult(Krono.enStrict, "in 15m")
        testUnexpectedResult(Krono.enStrict, "within 5hr")
    }

    @Test
    internal fun `forward date`() {
        testSingleCase(Krono.enCasual, "1 hour", "2012-08-10T12:14:00", ParsingOption(forwardDate = true)) {
            with(it.start) {
                assertThat(hour()).isEqualTo(13)
                assertThat(minute()).isEqualTo(14)
            }
        }

        testSingleCase(Krono.enCasual, "1 month", "2016-10-01T14:52:00", ParsingOption(forwardDate = true)) {
            with(it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(11)
                assertThat(day()).isEqualTo(1)
            }
        }

        testSingleCase(Krono.enCasual, "in 1 month", "2016-10-01T14:52:00", ParsingOption(forwardDate = true)) {
            with(it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(11)
                assertThat(day()).isEqualTo(1)
            }
        }
    }

    @Test
    internal fun `negative cases`() {
        testUnexpectedResult(Krono.enCasual, "in am");
        testUnexpectedResult(Krono.enCasual, "in them");
    }

    companion object {
        @JvmStatic
        fun singleExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("we have to make something in 5 days", "2012-08-10T00:00:00", "2012-08-15T00:00:00"),
            Arguments.of("we have to make something in five days", "2012-08-10T00:00:00", "2012-08-15T00:00:00"),
            Arguments.of("we have to make something within 10 day", "2012-08-10T00:00:00", "2012-08-20T00:00:00"),
            Arguments.of("in 5 minutes", "2012-08-10T12:14:00", "2012-08-10T12:19:00"),
            Arguments.of("wait for 5 minutes", "2012-08-10T12:14:00", "2012-08-10T12:19:00"),
            Arguments.of("within 1 hour", "2012-08-10T12:14:00", "2012-08-10T13:14:00"),
            Arguments.of("In 5 minutes I will go home", "2012-08-10T12:14:00", "2012-08-10T12:19:00"),
            Arguments.of("In 5 minutes A car need to move", "2012-08-10T12:14:00", "2012-08-10T12:19:00"),
            Arguments.of("In 5 seconds A car need to move", "2012-08-10T12:14:00", "2012-08-10T12:14:05"),
            Arguments.of("within half an hour", "2012-08-10T12:14:00", "2012-08-10T12:44:00"),
            Arguments.of("within two weeks", "2012-08-10T12:14:00", "2012-08-24T12:14:00"),
            Arguments.of("within a month", "2012-08-10T12:14:00", "2012-09-10T12:14:00"),
            Arguments.of("within a few months", "2012-07-10T12:14:00", "2012-10-10T12:14:00"),
            Arguments.of("within one year", "2012-08-10T12:14:00", "2013-08-10T12:14:00"),
            Arguments.of("within one Year", "2012-08-10T12:14:00", "2013-08-10T12:14:00"),
            Arguments.of("within One year", "2012-08-10T12:14:00", "2013-08-10T12:14:00"),
            Arguments.of("In 5 Minutes A car need to move", "2012-08-10T12:14:00", "2012-08-10T12:19:00"),
            Arguments.of("In 5 mins a car need to move", "2012-08-10T12:14:00", "2012-08-10T12:19:00"),
            Arguments.of("in a week", "2016-10-01T00:00:00", "2016-10-08T00:00:00"),
            Arguments.of("In around 5 hours", "2016-10-01T13:00:00", "2016-10-01T18:00:00"),
            Arguments.of("In about ~5 hours", "2016-10-01T13:00:00", "2016-10-01T18:00:00"),
            Arguments.of("in 1 month", "2016-10-01T14:52:00", "2016-11-01T14:52:00"),
            Arguments.of("set a timer for 5 minutes 30 seconds", "2012-08-10T12:14:00", "2012-08-10T12:19:30"),
            Arguments.of("set a timer for 5 minutes, 30 seconds", "2012-08-10T12:14:00", "2012-08-10T12:19:30"),
            Arguments.of("set a timer for 1 hour, 5 minutes, 30 seconds", "2012-08-10T12:14:00", "2012-08-10T13:19:30"),
            Arguments.of("set a timer for 5 minutes and 30 seconds", "2012-08-10T12:14:00", "2012-08-10T12:19:30"),
            Arguments.of(
                "set a timer for 1 hour, 5 minutes, and 30 seconds",
                "2012-08-10T12:14:00",
                "2012-08-10T13:19:30"
            ),
            Arguments.of("In  about 5 hours", "2012-08-10T12:49:00", "2012-08-10T17:49:00"),
            Arguments.of("within around 3 hours", "2012-08-10T12:49:00", "2012-08-10T15:49:00"),
            Arguments.of("In several hours", "2012-08-10T12:49:00", "2012-08-10T19:49:00"),
            Arguments.of("In a couple of days", "2012-08-10T12:49:00", "2012-08-12T12:49:00"),
            Arguments.of("in 24 hours", "2020-07-10T12:14:00", "2020-07-11T12:14:00"),
            Arguments.of("in one day", "2020-07-10T12:14:00", "2020-07-11T12:14:00"),
        )
    }
}
