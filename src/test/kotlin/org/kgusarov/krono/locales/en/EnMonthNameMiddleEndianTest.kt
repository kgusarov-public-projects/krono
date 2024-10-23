package org.kgusarov.krono.locales.en

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.Krono
import org.kgusarov.krono.ParsingOption
import org.kgusarov.krono.testUnexpectedResult
import org.kgusarov.krono.testWithExpectedDate
import org.kgusarov.krono.testWithExpectedRange
import java.util.stream.Stream

internal class EnMonthNameMiddleEndianTest {
    @ParameterizedTest
    @MethodSource("singleExpressionArgs")
    internal fun `single expression`(text: String, refDate: String, expectedDate: String) {
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

    @Test
    internal fun `impossible dates - strict mode`() {
        testUnexpectedResult(Krono.enStrict, "August 32, 2014", "2012-08-10T00:00:00")
        testUnexpectedResult(Krono.enStrict, "February 29, 2014", "2012-08-10T00:00:00")
        testUnexpectedResult(Krono.enStrict, "August 32", "2012-08-10T00:00:00")
        testUnexpectedResult(Krono.enStrict, "February 29", "2013-08-10T00:00:00")
        testUnexpectedResult(Krono.enStrict, "February 151998", "2012-08-10T00:00:00")
    }

    @Test
    internal fun `forward option`() {
        testWithExpectedDate(
            Krono.enCasual,
            "January 1st",
            "2016-02-15T00:00:00",
            "2016-01-01T12:00:00",
        )

        testWithExpectedDate(
            Krono.enCasual,
            "January 1st",
            "2016-02-15T00:00:00",
            "2017-01-01T12:00:00",
            ParsingOption(forwardDate = true)
        )
    }

    @Test
    internal fun `year 90's parsing`() {
        testWithExpectedDate(
            Krono.enCasual,
            "Aug 9, 96",
            "2012-08-10T00:00:00",
            "1996-08-09T12:00:00",
        )

        testWithExpectedDate(
            Krono.enCasual,
            "Aug 9 96",
            "2012-08-10T00:00:00",
            "1996-08-09T12:00:00",
        )
    }

    @Test
    internal fun `skip year-like on little-endian configuration`() {
        val middleEndianConfig = En.configuration.createCasualConfiguration(false)
        val littleEndianConfig = En.configuration.createCasualConfiguration(true)

        var krono = Krono(middleEndianConfig)
        testWithExpectedDate(
            krono,
            "Dec. 21",
            "2023-12-10T00:00:00",
            "2023-12-21T12:00:00",
        )

        krono = Krono(littleEndianConfig)
        testWithExpectedDate(
            krono,
            "Dec. 21",
            "2023-12-10T00:00:00",
            "2021-12-01T12:00:00",
        )
    }

    companion object {
        @JvmStatic
        fun singleExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("She is getting married soon (July 2017).", "2012-08-10T00:00:00", "2017-07-01T12:00:00"),
            Arguments.of("She is leaving in August.", "2012-08-10T00:00:00", "2012-08-01T12:00:00"),
            Arguments.of("I am arriving sometime in August, 2012, probably.", "2012-08-10T00:00:00", "2012-08-01T12:00:00"),
            Arguments.of("August 10, 2012", "2012-08-10T00:00:00", "2012-08-10T12:00:00"),
            Arguments.of("Nov 12, 2011", "2012-08-10T00:00:00", "2011-11-12T12:00:00"),
            Arguments.of("The Deadline is August 10", "2012-08-10T00:00:00", "2012-08-10T12:00:00"),
            Arguments.of("The Deadline is August 10, 2555 BE", "2012-08-10T00:00:00", "2012-08-10T12:00:00"),
            Arguments.of("The Deadline is August 10, 345 BC", "2012-08-10T00:00:00", "-0345-08-10T12:00:00"),
            Arguments.of("The Deadline is August 10, 8 AD", "2012-08-10T00:00:00", "0008-08-10T12:00:00"),
            Arguments.of("The Deadline is Tuesday, January 10", "2012-08-10T00:00:00", "2013-01-10T12:00:00"),
            Arguments.of("Sun, Mar. 6, 2016", "2012-08-10T00:00:00", "2016-03-06T12:00:00"),
            Arguments.of("Sun, March 6, 2016", "2012-08-10T00:00:00", "2016-03-06T12:00:00"),
            Arguments.of("Sun., March 6, 2016", "2012-08-10T00:00:00", "2016-03-06T12:00:00"),
            Arguments.of("Sunday, March 6, 2016", "2012-08-10T00:00:00", "2016-03-06T12:00:00"),
            Arguments.of("Sunday, March, 6th 2016", "2012-08-10T00:00:00", "2016-03-06T12:00:00"),
            Arguments.of("Wed, Jan 20th, 2016             ", "2012-08-10T00:00:00", "2016-01-20T12:00:00"),
            Arguments.of("Dec. 21", "2012-08-10T00:00:00", "2012-12-21T12:00:00"),
            Arguments.of("August-10, 2012", "2012-08-10T00:00:00", "2012-08-10T12:00:00"),
            Arguments.of("August/10, 2012", "2012-08-10T00:00:00", "2012-08-10T12:00:00"),
            Arguments.of("August/10/2012", "2012-08-10T00:00:00", "2012-08-10T12:00:00"),
            Arguments.of("August-10-2012", "2012-08-10T00:00:00", "2012-08-10T12:00:00"),
            Arguments.of("May eighth, 2010", "2012-08-10T00:00:00", "2010-05-08T12:00:00"),
            Arguments.of("May twenty-fourth", "2012-08-10T00:00:00", "2012-05-24T12:00:00"),
        )

        @JvmStatic
        fun rangeExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "May eighth - tenth, 2010", "2012-08-10T00:00:00", "2010-05-08T12:00:00", "2010-05-10T12:00:00",
            ),
            Arguments.of(
                "August 10 - 22, 2012", "2012-08-10T00:00:00", "2012-08-10T12:00:00", "2012-08-22T12:00:00",
            ),
            Arguments.of(
                "August 10 to 22, 2012", "2012-08-10T00:00:00", "2012-08-10T12:00:00", "2012-08-22T12:00:00",
            ),
            Arguments.of(
                "August 10 - November 12", "2012-08-10T00:00:00", "2012-08-10T12:00:00", "2012-11-12T12:00:00",
            ),
            Arguments.of(
                "Aug 10 to Nov 12", "2012-08-10T00:00:00", "2012-08-10T12:00:00", "2012-11-12T12:00:00",
            ),
            Arguments.of(
                "Aug 10 - Nov 12, 2013", "2012-08-10T00:00:00", "2013-08-10T12:00:00", "2013-11-12T12:00:00",
            ),
            Arguments.of(
                "Aug 10 - Nov 12, 2011", "2012-08-10T00:00:00", "2011-08-10T12:00:00", "2011-11-12T12:00:00",
            ),
        )
    }
}
