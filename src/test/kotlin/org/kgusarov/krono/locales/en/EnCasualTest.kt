package org.kgusarov.krono.locales.en

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.Krono
import org.kgusarov.krono.KronoDate
import org.kgusarov.krono.ParsingOption
import org.kgusarov.krono.ReferenceWithTimezone
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.assertOffsetDate
import org.kgusarov.krono.testSingleCase
import org.kgusarov.krono.testUnexpectedResult
import org.kgusarov.krono.testWithExpectedDate
import org.kgusarov.krono.testWithExpectedRange
import java.util.stream.Stream

internal class EnCasualTest {
    @Test
    fun `random negative text`() {
        testUnexpectedResult(Krono.enCasual, "notoday")
        testUnexpectedResult(Krono.enCasual, "tdtmr")
        testUnexpectedResult(Krono.enCasual, "xyesterday")
        testUnexpectedResult(Krono.enCasual, "nowhere")
        testUnexpectedResult(Krono.enCasual, "noway")
        testUnexpectedResult(Krono.enCasual, "knowledge")
        testUnexpectedResult(Krono.enCasual, "mar")
        testUnexpectedResult(Krono.enCasual, "jan")
        testUnexpectedResult(Krono.enCasual, "do I have the money")
        testUnexpectedResult(Krono.enCasual, "do I have the money")
        testUnexpectedResult(Krono.enGb, "do I have the money")
    }

    @Test
    fun `The Deadline is now, without implicit local timezone`() {
        testSingleCase(
            Krono.enCasual,
            "The Deadline is now, without implicit local timezone",
            ReferenceWithTimezone(KronoDate.parse("2012-08-10T08:09:10.011"), null)
        ) {
            assertThat(it.text).isEqualTo("now")

            with(it.start) {
                assertDate("2012-08-10T08:09:10.011")
                assertThat(certainOffset()).isFalse()
            }
        }
    }

    @Test
    fun `The Deadline was midnight - forward`() {
        testSingleCase(
            Krono.enCasual,
            "The Deadline was midnight",
            "2012-08-10T01:00:00",
            ParsingOption(forwardDate = true)
        ) {
            assertThat(it.text).isEqualTo("midnight")
            with(it.start) {
                assertDate("2012-08-11T00:00:00.000")
            }
        }
    }

    @ParameterizedTest
    @MethodSource("singleExpressionArgs")
    fun `single expression`(text: String, refDate: String, expectedDate: String) {
        testWithExpectedDate(Krono.enCasual, text, refDate, expectedDate)
    }

    @ParameterizedTest
    @MethodSource("combinedExpressionArgs")
    fun `combined expression`(text: String, refDate: String, expectedDate: String) {
        testWithExpectedDate(Krono.enCasual, text, refDate, expectedDate)
    }

    @ParameterizedTest
    @MethodSource("casualDateRangeArgs")
    fun `casual date range`(text: String, refDate: String, expectedStartDate: String, expectedEndDate: String) {
        testWithExpectedRange(Krono.enCasual, text, refDate, expectedStartDate, expectedEndDate)
    }

    @ParameterizedTest
    @MethodSource("casualTimeImplicationArgs")
    fun `casual time implication`(text: String, refDate: String, expectedStartDate: String, expectedEndDate: String) {
        val result = testWithExpectedRange(Krono.enCasual, text, refDate, expectedStartDate, expectedEndDate)
        assertThat(result.start.certainHour()).isFalse()
        assertThat(result.end!!.certainHour()).isFalse()
    }

    @ParameterizedTest
    @MethodSource("randomTextArgs")
    fun `random text`(text: String, refDate: String, expectedDate: String) {
        testWithExpectedDate(Krono.enCasual, text, refDate, expectedDate)
    }

    @Test
    fun `casual time with timezone`() {
        testSingleCase(
            Krono.enCasual,
            "Jan 1, 2020 Morning UTC"
        ) {
            assertThat(it.text).isEqualTo("Jan 1, 2020 Morning UTC")
            with(it.start) {
                assertThat(year()).isEqualTo(2020)
                assertThat(month()).isEqualTo(1)
                assertThat(day()).isEqualTo(1)
                assertThat(hour()).isEqualTo(6)
                assertThat(offsetMinutes()).isEqualTo(0)

                assertOffsetDate("2020-01-01T06:00:00.000Z")
            }
        }

        testSingleCase(
            Krono.enCasual,
            "Jan 1, 2020 Evening JST"
        ) {
            assertThat(it.text).isEqualTo("Jan 1, 2020 Evening JST")
            with(it.start) {
                assertThat(year()).isEqualTo(2020)
                assertThat(month()).isEqualTo(1)
                assertThat(day()).isEqualTo(1)
                assertThat(hour()).isEqualTo(20)
                assertThat(offsetMinutes()).isEqualTo(540)

                assertOffsetDate("2020-01-01T20:00:00.000+09:00")
            }
        }
    }

    companion object {
        @JvmStatic
        fun singleExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("The Deadline is now", "2012-08-10T08:09:10.011", "2012-08-10T08:09:10.011"),
            Arguments.of("The Deadline is today", "2012-08-10T08:09:10.011", "2012-08-10T08:09:10.011"),
            Arguments.of("The Deadline is Tomorrow", "2012-08-10T08:09:10.011", "2012-08-11T08:09:10.011"),
            Arguments.of("The Deadline is tomorrow", "2012-08-10T01:00:00", "2012-08-11T01:00:00"),
            Arguments.of("The Deadline was yesterday", "2012-08-10T12:00:00", "2012-08-09T12:00:00"),
            Arguments.of("The Deadline was last night", "2012-08-10T12:00:00", "2012-08-09T00:00:00"),
            Arguments.of("The Deadline was this morning", "2012-08-10T12:00:00", "2012-08-10T06:00:00"),
            Arguments.of("The Deadline was this afternoon", "2012-08-10T12:00:00", "2012-08-10T15:00:00"),
            Arguments.of("The Deadline was this evening", "2012-08-10T12:00:00", "2012-08-10T20:00:00"),
            Arguments.of("The Deadline is midnight", "2012-08-10T12:00:00", "2012-08-11T00:00:00"),
            Arguments.of("The Deadline was midnight", "2012-08-10T01:00:00", "2012-08-10T00:00:00"),
        )

        @JvmStatic
        fun combinedExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("The Deadline is today 5PM", "2012-08-10T12:00:00", "2012-08-10T17:00:00"),
            Arguments.of("Tomorrow at noon", "2012-08-10T14:00:00", "2012-08-11T12:00:00"),
        )

        @JvmStatic
        fun casualDateRangeArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "The event is today - next friday", "2012-08-04T12:00:00", "2012-08-04T12:00:00", "2012-08-10T12:00:00"
            ),
        )

        @JvmStatic
        fun casualTimeImplicationArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "annual leave from today morning to tomorrow", "2012-08-04T12:00:00", "2012-08-04T06:00:00", "2012-08-05T12:00:00"
            ),
            Arguments.of(
                "annual leave from today to tomorrow afternoon", "2012-08-04T12:00:00", "2012-08-04T12:00:00", "2012-08-05T15:00:00"
            ),
        )

        @JvmStatic
        fun randomTextArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("tonight", "2012-01-01T12:00:00", "2012-01-01T22:00:00"),
            Arguments.of("tonight 8pm", "2012-01-01T12:00:00", "2012-01-01T20:00:00"),
            Arguments.of("tonight at 8", "2012-01-01T12:00:00", "2012-01-01T20:00:00"),
            Arguments.of("tomorrow before 4pm", "2012-01-01T12:00:00", "2012-01-02T16:00:00"),
            Arguments.of("tomorrow after 4pm", "2012-01-01T12:00:00", "2012-01-02T16:00:00"),
            Arguments.of("thurs", "2012-01-01T12:00:00", "2011-12-29T12:00:00"),
            Arguments.of("this evening", "2016-10-01T12:00:00", "2016-10-01T20:00:00"),
            Arguments.of("yesterday afternoon", "2016-10-01T12:00:00", "2016-09-30T15:00:00"),
            Arguments.of("tomorrow morning", "2016-10-01T08:00:00", "2016-10-02T06:00:00"),
            Arguments.of("this afternoon at 3", "2016-10-01T08:00:00", "2016-10-01T15:00:00"),
            Arguments.of("at midnight on 12th August", "2012-08-10T15:00:00", "2012-08-12T00:00:00"),
        )
    }
}
