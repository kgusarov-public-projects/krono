package org.kgusarov.krono.locales.en

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
import org.kgusarov.krono.testWithExpectedRange
import java.util.stream.Stream

internal class EnMonthNameLittleEndianTest {
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

    @ParameterizedTest
    @MethodSource("singleExpressionWithSeparatorsArgs")
    internal fun `single expression with separators`(
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

    @ParameterizedTest
    @MethodSource("rangeExpressionArgs")
    internal fun `range expression`(
        text: String,
        refDate: String,
        expectedStartDate: String,
        expectedEndDate: String,
    ) {
        testWithExpectedRange(
            Krono.enCasual,
            text,
            refDate,
            expectedStartDate,
            expectedEndDate,
        )
    }

    @ParameterizedTest
    @MethodSource("combinedExpressionArgs")
    internal fun `combined expression`(
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
    internal fun `ordinal words`() {
        testWithExpectedDate(
            Krono.enCasual,
            "Twenty-fourth of May",
            "2012-08-10T00:00:00",
            "2012-05-24T12:00:00"
        )

        testWithExpectedRange(
            Krono.enCasual,
            "Eighth to eleventh May 2010",
            "2012-08-10T00:00:00",
            "2010-05-08T12:00:00",
            "2010-05-11T12:00:00",
        )
    }

    @ParameterizedTest
    @MethodSource("littleEndianFollowedByTimeArgs")
    internal fun `little endian followed by time`(
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

    @ParameterizedTest
    @MethodSource("year90sParsingArgs")
    internal fun `year 90's parsing`(
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
    internal fun `impossible dates - strict mode`() {
        testUnexpectedResult(Krono.enStrict, "32 August 2014", "2012-08-10T00:00:00")
        testUnexpectedResult(Krono.enStrict, "29 February 2014", "2012-08-10T00:00:00")
        testUnexpectedResult(Krono.enStrict, "32 August", "2012-08-10T00:00:00")
        testUnexpectedResult(Krono.enStrict, "29 February", "2013-08-10T00:00:00")
    }

    @Test
    internal fun `forward option`() {
        testWithExpectedRange(
            Krono.enCasual,
            "22-23 Feb at 7pm",
            "2016-03-15T00:00:00",
            "2016-02-22T19:00:00",
            "2016-02-23T19:00:00",
        )

        testWithExpectedRange(
            Krono.enCasual,
            "22-23 Feb at 7pm",
            "2016-03-15T00:00:00",
            "2017-02-22T19:00:00",
            "2017-02-23T19:00:00",
            ParsingOption(forwardDate = true),
        )

        testWithExpectedRange(
            Krono.enCasual,
            "17 August 2013 - 19 August 2013",
            "2012-08-10T00:00:00",
            "2013-08-17T12:00:00",
            "2013-08-19T12:00:00",
        )
    }

    companion object {
        @JvmStatic
        fun singleExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("10 August 2012", "2012-08-10T00:00:00", "2012-08-10T12:00:00"),
            Arguments.of("3rd Feb 82", "2012-08-10T00:00:00", "1982-02-03T12:00:00"),
            Arguments.of("Sun 15Sep", "2013-08-10T00:00:00", "2013-09-15T12:00:00"),
            Arguments.of("SUN 15SEP", "2013-08-10T00:00:00", "2013-09-15T12:00:00"),
            Arguments.of("The Deadline is 10 August", "2012-08-10T00:00:00", "2012-08-10T12:00:00"),
            Arguments.of("The Deadline is Tuesday, 10 January", "2012-08-10T00:00:00", "2013-01-10T12:00:00"),
            Arguments.of("The Deadline is Tue, 10 January", "2012-08-10T00:00:00", "2013-01-10T12:00:00"),
            Arguments.of("31st March, 2016", "2012-08-10T00:00:00", "2016-03-31T12:00:00"),
            Arguments.of("23rd february, 2016", "2012-08-10T00:00:00", "2016-02-23T12:00:00"),
        )

        @JvmStatic
        fun singleExpressionWithSeparatorsArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("10-August 2012", "2012-08-10T00:00:00", "2012-08-10T12:00:00"),
            Arguments.of("10-August-2012", "2012-08-10T00:00:00", "2012-08-10T12:00:00"),
            Arguments.of("10/August 2012", "2012-08-10T00:00:00", "2012-08-10T12:00:00"),
            Arguments.of("10/August/2012", "2012-08-10T00:00:00", "2012-08-10T12:00:00"),
        )

        @JvmStatic
        fun rangeExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "10 - 22 August 2012", "2012-08-10T00:00:00", "2012-08-10T12:00:00", "2012-08-22T12:00:00"
            ),
            Arguments.of(
                "10 to 22 August 2012", "2012-08-10T00:00:00", "2012-08-10T12:00:00", "2012-08-22T12:00:00"
            ),
            Arguments.of(
                "10 August - 12 September", "2012-08-10T00:00:00", "2012-08-10T12:00:00", "2012-09-12T12:00:00"
            ),
            Arguments.of(
                "10 August - 12 September 2013", "2012-08-10T00:00:00", "2013-08-10T12:00:00", "2013-09-12T12:00:00"
            ),
            Arguments.of(
                "17 August 2013 to 19 August 2013", "2012-08-10T00:00:00", "2013-08-17T12:00:00", "2013-08-19T12:00:00"
            ),
        )

        @JvmStatic
        fun combinedExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("12th of July at 19:00", "2012-08-10T00:00:00", "2012-07-12T19:00:00"),
            Arguments.of("5 May 12:00", "2012-08-10T00:00:00", "2012-05-05T12:00:00"),
            Arguments.of("7 May 11:00", "2012-08-10T00:00:00", "2012-05-07T11:00:00"),
        )

        @JvmStatic
        fun littleEndianFollowedByTimeArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("24th October, 9 am", "2017-07-07T15:00:00", "2017-10-24T09:00:00"),
            Arguments.of("24th October, 9 pm", "2017-07-07T15:00:00", "2017-10-24T21:00:00"),
            Arguments.of("24 October, 9 pm", "2017-07-07T15:00:00", "2017-10-24T21:00:00"),
            Arguments.of("24 October, 9 p.m.", "2017-07-07T15:00:00", "2017-10-24T21:00:00"),
            Arguments.of("24 October 10 o clock", "2017-07-07T15:00:00", "2017-10-24T10:00:00"),
        )

        @JvmStatic
        fun year90sParsingArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("03 Aug 96", "2012-08-10T00:00:00", "1996-08-03T12:00:00"),
            Arguments.of("3 Aug 96", "2012-08-10T00:00:00", "1996-08-03T12:00:00"),
            Arguments.of("9 Aug 96", "2012-08-10T00:00:00", "1996-08-09T12:00:00"),
        )
    }
}
