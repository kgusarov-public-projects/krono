package org.kgusarov.krono.locales.en

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.Krono
import org.kgusarov.krono.KronoDate
import org.kgusarov.krono.KronoMeridiem
import org.kgusarov.krono.ParsingOption
import org.kgusarov.krono.ReferenceWithTimezone
import org.kgusarov.krono.TimezoneAbbreviations
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase
import org.kgusarov.krono.testUnexpectedResult
import org.kgusarov.krono.testWithExpectedDate
import org.kgusarov.krono.testWithExpectedRange
import java.util.stream.Stream

internal class EnTimeExpressionTest {
    @Test
    internal fun `text offset`() {
        testSingleCase(Krono.enCasual, "  11 AM ", "2016-10-01T08:00:00") {
            assertThat(it.index).isEqualTo(2)
            assertThat(it.text).isEqualTo("11 AM")
        }

        testSingleCase(Krono.enCasual, "2020 at  11 AM ", "2016-10-01T08:00:00") {
            assertThat(it.index).isEqualTo(5)
            assertThat(it.text).isEqualTo("at  11 AM")
        }
    }

    @Test
    internal fun `time expression`() {
        testSingleCase(Krono.enCasual, "20:32:13", "2016-10-01T08:00:00") {
            assertThat(it.text).isEqualTo("20:32:13")
            assertThat(it.tags()).contains("parser/ENTimeExpressionParser")

            with(it.start) {
                assertThat(hour()).isEqualTo(20)
                assertThat(minute()).isEqualTo(32)
                assertThat(second()).isEqualTo(13)
                assertThat(meridiem()).isEqualTo(KronoMeridiem.PM)
                assertThat(tags()).contains("parser/ENTimeExpressionParser")
            }
        }
    }

    @ParameterizedTest
    @MethodSource("singleExpressionArgs")
    internal fun `single expression`(text: String, refDate: String, expectedDate: String, expectedMeridiem: Int) {
        testSingleCase(Krono.enCasual, text, refDate) {
            assertThat(it.text).isEqualTo(text)
            assertThat(it.tags()).contains("parser/ENTimeExpressionParser")

            with(it.start) {
                assertThat(meridiem()).isEqualTo(expectedMeridiem)
                assertDate(expectedDate)
                assertThat(tags()).contains("parser/ENTimeExpressionParser")
            }
        }
    }

    @ParameterizedTest
    @MethodSource("rangeExpressionArgs")
    internal fun `range expression`(
        text: String,
        refDate: String,
        expectedStartDate: String,
        expectedStartMeridiem: Int,
        expectedEndDate: String,
        expectedEndMeridiem: Int,
    ) {
        testSingleCase(Krono.enCasual, text, refDate) {
            assertThat(it.text).isEqualTo(text)
            assertThat(it.tags()).contains("parser/ENTimeExpressionParser")

            with(it.start) {
                assertThat(meridiem()).isEqualTo(expectedStartMeridiem)
                assertDate(expectedStartDate)
                assertThat(tags()).contains("parser/ENTimeExpressionParser")
            }

            with(it.end!!) {
                assertThat(meridiem()).isEqualTo(expectedEndMeridiem)
                assertDate(expectedEndDate)
                assertThat(tags()).contains("parser/ENTimeExpressionParser")
            }
        }
    }

    @Test
    internal fun `non time-range expression`() {
        testSingleCase(Krono.enCasual, "10:00:00 - 15/15", "2016-10-01T08:00:00") {
            assertThat(it.text).isEqualTo("10:00:00")
            assertThat(it.end).isNull()

            with(it.start) {
                assertThat(hour()).isEqualTo(10)
                assertThat(minute()).isEqualTo(0)
                assertThat(second()).isEqualTo(0)
                assertThat(meridiem()).isEqualTo(KronoMeridiem.AM)
            }
        }
    }

    @Test
    internal fun `causal positive cases`() {
        testSingleCase(Krono.enCasual, "at 1") {
            assertThat(it.text).isEqualTo("at 1")
            assertThat(it.start.hour()).isEqualTo(1)
        }

        testSingleCase(Krono.enCasual, "at 12") {
            assertThat(it.text).isEqualTo("at 12")
            assertThat(it.start.hour()).isEqualTo(12)
        }

        testSingleCase(Krono.enCasual, "at 12.30") {
            assertThat(it.text).isEqualTo("at 12.30")
            assertThat(it.start.hour()).isEqualTo(12)
            assertThat(it.start.minute()).isEqualTo(30)
        }
    }

    @ParameterizedTest
    @MethodSource("negativeCasesArgs")
    internal fun `negative cases`(text: String) {
        testUnexpectedResult(Krono.enCasual, text)
    }

    @ParameterizedTest
    @MethodSource("negativeStrictCasesArgs")
    internal fun `strinct negative cases`(text: String) {
        testUnexpectedResult(Krono.enStrict, text)
    }

    @Test
    internal fun `forward dates`() {
        testSingleCase(
            Krono.enCasual,
            "1am",
            ReferenceWithTimezone(
                KronoDate.parse("2022-05-26T01:57:00"),
                TimezoneAbbreviations.TIMEZONE_ABBR_MAP["CDT"],
            ),
            ParsingOption(forwardDate = true),
        ) {
            assertThat(it.start.year()).isEqualTo(2022)
            assertThat(it.start.month()).isEqualTo(5)
            assertThat(it.start.day()).isEqualTo(27)
            assertThat(it.start.hour()).isEqualTo(1)
        }

        testWithExpectedDate(
            Krono.enCasual,
            "11am",
            "2016-10-01T12:00:00",
            "2016-10-02T11:00:00",
            ParsingOption(forwardDate = true),
        )

        testWithExpectedRange(
            Krono.enCasual,
            "  11am to 1am  ",
            "2016-10-01T12:00:00",
            "2016-10-02T11:00:00",
            "2016-10-03T01:00:00",
            ParsingOption(forwardDate = true),
        )

        testWithExpectedRange(
            Krono.enCasual,
            "  10am to 12pm  ",
            "2016-10-01T11:00:00",
            "2016-10-02T10:00:00",
            "2016-10-02T12:00:00",
            ParsingOption(forwardDate = true),
        )
    }

    companion object {
        @JvmStatic
        fun singleExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("05/31/2024 14:15", "2016-10-01T08:00:00", "2024-05-31T14:15:00", KronoMeridiem.PM),
            Arguments.of("05/31/2024.14:15", "2016-10-01T08:00:00", "2024-05-31T14:15:00", KronoMeridiem.PM),
            Arguments.of("05/31/2024:14:15", "2016-10-01T08:00:00", "2024-05-31T14:15:00", KronoMeridiem.PM),
            Arguments.of("05/31/2024-14:15", "2016-10-01T08:00:00", "2024-05-31T14:15:00", KronoMeridiem.PM),
            Arguments.of("14:15 05/31/2024", "2016-10-01T08:00:00", "2024-05-31T14:15:00", KronoMeridiem.PM),
            Arguments.of("8:23 AM, Jul 9", "2016-10-01T08:00:00", "2016-07-09T08:23:00", KronoMeridiem.AM),
            Arguments.of("8:23 AM ∙ Jul 9", "2016-10-01T08:00:00", "2016-07-09T08:23:00", KronoMeridiem.AM),
            Arguments.of("11 at night", "2016-10-01T08:00:00", "2016-10-01T23:00:00", KronoMeridiem.PM),
            Arguments.of("11 tonight", "2016-10-01T08:00:00", "2016-10-01T23:00:00", KronoMeridiem.PM),
            Arguments.of("6 in the morning", "2016-10-01T08:00:00", "2016-10-01T06:00:00", KronoMeridiem.AM),
            Arguments.of("6 in the afternoon", "2016-10-01T08:00:00", "2016-10-01T18:00:00", KronoMeridiem.PM),
        )

        @JvmStatic
        fun rangeExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "10:00:00 - 21:45:00",
                "2016-10-01T08:00:00",
                "2016-10-01T10:00:00",
                KronoMeridiem.AM,
                "2016-10-01T21:45:00",
                KronoMeridiem.PM,
            ),
            Arguments.of(
                "10:00:00 until 21:45:00",
                "2016-10-01T08:00:00",
                "2016-10-01T10:00:00",
                KronoMeridiem.AM,
                "2016-10-01T21:45:00",
                KronoMeridiem.PM,
            ),
            Arguments.of(
                "10:00:00 till 21:45:00",
                "2016-10-01T08:00:00",
                "2016-10-01T10:00:00",
                KronoMeridiem.AM,
                "2016-10-01T21:45:00",
                KronoMeridiem.PM,
            ),
            Arguments.of(
                "10:00:00 through 21:45:00",
                "2016-10-01T08:00:00",
                "2016-10-01T10:00:00",
                KronoMeridiem.AM,
                "2016-10-01T21:45:00",
                KronoMeridiem.PM,
            ),
            Arguments.of(
                "December 31, 2022 10:00 pm - 1:00 am",
                "2017-07-07T00:00:00",
                "2022-12-31T22:00:00",
                KronoMeridiem.PM,
                "2023-01-01T01:00:00",
                KronoMeridiem.AM,
            ),
            Arguments.of(
                "December 31, 2022 10:00 pm - 12:00 am",
                "2017-07-07T00:00:00",
                "2022-12-31T22:00:00",
                KronoMeridiem.PM,
                "2023-01-01T00:00:00",
                KronoMeridiem.AM,
            ),
            Arguments.of(
                "10 - 11 at night",
                "2016-10-01T08:00:00",
                "2016-10-01T22:00:00",
                KronoMeridiem.PM,
                "2016-10-01T23:00:00",
                KronoMeridiem.PM,
            ),
            Arguments.of(
                "8pm - 11",
                "2016-10-01T08:00:00",
                "2016-10-01T20:00:00",
                KronoMeridiem.PM,
                "2016-10-01T23:00:00",
                KronoMeridiem.PM,
            ),
            Arguments.of(
                "8 - 11pm",
                "2016-10-01T08:00:00",
                "2016-10-01T20:00:00",
                KronoMeridiem.PM,
                "2016-10-01T23:00:00",
                KronoMeridiem.PM,
            ),
            Arguments.of(
                "7 - 8",
                "2016-10-01T08:00:00",
                "2016-10-01T07:00:00",
                KronoMeridiem.AM,
                "2016-10-01T08:00:00",
                KronoMeridiem.AM,
            ),
        )

        @JvmStatic
        fun negativeCasesArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("2020"),
            Arguments.of("2020  "),
            Arguments.of("I'm at 101,194 points!"),
            Arguments.of("I'm at 101 points!"),
            Arguments.of("I'm at 10.1"),
            Arguments.of("I'm at 10.1 - 10.12"),
            Arguments.of("I'm at 10 - 10.1"),
        )

        @JvmStatic
        fun negativeStrictCasesArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("I'm at 101,194 points!"),
            Arguments.of("I'm at 101 points!"),
            Arguments.of("I'm at 10.1"),
            Arguments.of("I'm at 10"),
            Arguments.of("2020"),
            Arguments.of("I'm at 10.1 - 10.12"),
            Arguments.of("I'm at 10 - 10.1"),
            Arguments.of("I'm at 10 - 20"),
            Arguments.of("7-730"),
        )
    }
}
