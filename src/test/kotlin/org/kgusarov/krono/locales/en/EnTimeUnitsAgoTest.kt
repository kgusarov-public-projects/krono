package org.kgusarov.krono.locales.en

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.Krono
import org.kgusarov.krono.ParsingOption
import org.kgusarov.krono.testUnexpectedResult
import org.kgusarov.krono.testWithExpectedDate
import java.util.stream.Stream

internal class EnTimeUnitsAgoTest {
    @ParameterizedTest
    @MethodSource("singleExpressionArgs")
    internal fun `single expression`(
        text: String,
        refDate: String,
        expectedDate: String
    ) {
        testWithExpectedDate(
            Krono.enCasual,
            text,
            refDate,
            expectedDate,
        ).apply {
            assertThat(tags()).contains("result/relativeDate")
            if (start.certainTime()) {
                assertThat(tags()).contains("result/relativeDateAndTime")
            }
        }
    }

    @ParameterizedTest
    @MethodSource("singleExpressionCasualArgs")
    internal fun `single expression - casual`(
        text: String,
        refDate: String,
        expectedDate: String
    ) {
        testWithExpectedDate(
            Krono.enCasual,
            text,
            refDate,
            expectedDate,
        )
    }

    @ParameterizedTest
    @MethodSource("nestedTimeAgoArgs")
    internal fun `nested time ago`(
        text: String,
        refDate: String,
        expectedDate: String
    ) {
        testWithExpectedDate(
            Krono.enCasual,
            text,
            refDate,
            expectedDate,
        )
    }

    @ParameterizedTest
    @MethodSource("beforeWithReferenceArgs")
    internal fun `before with reference`(
        text: String,
        refDate: String,
        expectedDate: String
    ) {
        testWithExpectedDate(
            Krono.enCasual,
            text,
            refDate,
            expectedDate,
        )
    }

    @Test
    internal fun `negative cases`() {
        testUnexpectedResult(Krono.enStrict, "5m ago")
        testUnexpectedResult(Krono.enStrict, "5hr before")
        testUnexpectedResult(Krono.enStrict, "5 h ago")

        testUnexpectedResult(Krono.enCasual, "15 hours 29 min")
        testUnexpectedResult(Krono.enCasual, "a few hour")
        testUnexpectedResult(Krono.enCasual, "5 days")

        testUnexpectedResult(Krono.enCasual, "am ago");
        testUnexpectedResult(Krono.enCasual, "them ago");
    }

    @Test
    internal fun `strict mode`() {
        testWithExpectedDate(
            Krono.enStrict,
            "5 minutes ago",
            "2012-08-10T12:14:00",
            "2012-08-10T12:09:00",
        )
    }

    @Test
    internal fun `forward date`() {
        val refDate = "2024-09-10T12:00:00"
        val parsingOption = ParsingOption(forwardDate = true)

        testWithExpectedDate(
            Krono.enCasual,
            "2 days ago",
            refDate,
            "2024-09-08T12:00:00",
            parsingOption,
        )

        testWithExpectedDate(
            Krono.enCasual,
            "2 weeks ago",
            refDate,
            "2024-08-27T12:00:00",
            parsingOption,
        )

        testWithExpectedDate(
            Krono.enCasual,
            "2 months ago",
            refDate,
            "2024-07-10T12:00:00",
            parsingOption,
        )

        testWithExpectedDate(
            Krono.enCasual,
            "2 years ago",
            refDate,
            "2022-09-10T12:00:00",
            parsingOption,
        )
    }

    companion object {
        @JvmStatic
        fun singleExpressionArgs(): Stream<Arguments>
            = Stream.of(
                Arguments.of("5 days ago, we did something", "2012-08-10T12:00:00", "2012-08-05T12:00:00"),
                Arguments.of("10 days ago, we did something", "2012-08-10T12:00:00", "2012-07-31T12:00:00"),
                Arguments.of("15 minute ago", "2012-08-10T12:14:00", "2012-08-10T11:59:00"),
                Arguments.of("15 minute earlier", "2012-08-10T12:14:00", "2012-08-10T11:59:00"),
                Arguments.of("15 minute before", "2012-08-10T12:14:00", "2012-08-10T11:59:00"),
                Arguments.of("   12 hours ago", "2012-08-10T12:14:00", "2012-08-10T00:14:00"),
                Arguments.of("1h ago", "2012-08-10T12:14:00", "2012-08-10T11:14:00"),
                Arguments.of("1hr ago", "2012-08-10T12:14:00", "2012-08-10T11:14:00"),
                Arguments.of("   half an hour ago", "2012-08-10T12:14:00", "2012-08-10T11:44:00"),
                Arguments.of("12 hours ago I did something", "2012-08-10T12:14:00", "2012-08-10T00:14:00"),
                Arguments.of("12 seconds ago I did something", "2012-08-10T12:14:00", "2012-08-10T12:13:48"),
                Arguments.of("three seconds ago I did something", "2012-08-10T12:14:00", "2012-08-10T12:13:57"),
                Arguments.of("5 Days ago, we did something", "2012-08-10T12:00:00", "2012-08-05T12:00:00"),
                Arguments.of("   half An hour ago", "2012-08-10T12:14:00", "2012-08-10T11:44:00"),
                Arguments.of("A days ago, we did something", "2012-08-10T12:00:00", "2012-08-09T12:00:00"),
                Arguments.of("a min before", "2012-08-10T12:14:00", "2012-08-10T12:13:00"),
                Arguments.of("the min before", "2012-08-10T12:14:00", "2012-08-10T12:13:00"),
            )

        @JvmStatic
        fun singleExpressionCasualArgs(): Stream<Arguments>
            = Stream.of(
                Arguments.of("5 months ago, we did something", "2012-10-10T12:00:00", "2012-05-10T12:00:00"),
                Arguments.of("5 years ago, we did something", "2012-08-10T12:00:00", "2007-08-10T12:00:00"),
                Arguments.of("a week ago, we did something", "2012-08-03T12:00:00", "2012-07-27T12:00:00"),
                Arguments.of("a few days ago, we did something", "2012-08-03T12:00:00", "2012-07-31T12:00:00"),
            )

        @JvmStatic
        fun nestedTimeAgoArgs(): Stream<Arguments>
            = Stream.of(
                Arguments.of("15 hours 29 min ago", "2012-08-10T22:30:00", "2012-08-10T07:01:00"),
                Arguments.of("1 day 21 hours ago", "2012-08-10T22:30:00", "2012-08-09T01:30:00"),
                Arguments.of("1d 21 h 25m ago", "2012-08-10T22:30:00", "2012-08-09T01:05:00"),
                Arguments.of("3 min 49 sec ago", "2012-08-10T22:30:00", "2012-08-10T22:26:11"),
                Arguments.of("3m 49s ago", "2012-08-10T22:30:00", "2012-08-10T22:26:11"),
            )

        @JvmStatic
        fun beforeWithReferenceArgs(): Stream<Arguments>
            = Stream.of(
                Arguments.of("2 day before today", "2012-08-10T12:00:00", "2012-08-08T12:00:00"),
                Arguments.of("the day before yesterday", "2012-08-10T12:00:00", "2012-08-08T12:00:00"),
                Arguments.of("2 day before yesterday", "2012-08-10T12:00:00", "2012-08-07T12:00:00"),
                Arguments.of("a week before yesterday", "2012-08-10T12:00:00", "2012-08-02T12:00:00"),
            )
    }
}
