package org.kgusarov.krono.locales.de

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.Krono
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase
import org.kgusarov.krono.testUnexpectedResult
import java.util.stream.Stream

internal class DeMonthNameLittleEndianTest {
    @ParameterizedTest
    @MethodSource("singleExpressionArgs")
    internal fun `single expression`(text: String, refDate: String, expectedDate: String) {
        testSingleCase(Krono.deCasual, text, refDate) {
            it.start.assertDate(expectedDate)
        }
    }

    @ParameterizedTest
    @MethodSource("rangeExpressionArgs")
    internal fun `range expression`(
        text: String,
        refDate: String,
        expectedStartDate: String,
        expectedEndDate: String,
    ) {
        testSingleCase(Krono.deCasual, text, refDate) {
            it.start.assertDate(expectedStartDate)
            it.end!!.assertDate(expectedEndDate)
        }
    }

    @Test
    internal fun `impossible dates`() {
        testUnexpectedResult(Krono.deCasual, "32. Oktober 2015")
    }

    companion object {
        @JvmStatic
        fun singleExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("10. August 2012", "2012-08-10T12:00:00", "2012-08-10T12:00:00"),
            Arguments.of("10. August 113 v. Chr.", "2012-08-10T12:00:00", "-0113-08-10T12:00:00"),
            Arguments.of("10. August 85 n. Chr.", "2012-08-10T12:00:00", "0085-08-10T12:00:00"),
            Arguments.of("So 15.Sep", "2013-08-10T12:00:00", "2013-09-15T12:00:00"),
            Arguments.of("SO 15.SEPT", "2013-08-10T12:00:00", "2013-09-15T12:00:00"),
            Arguments.of("Die Deadline ist am 10. August", "2012-08-10T12:00:00", "2012-08-10T12:00:00"),
            Arguments.of("Die Deadline ist am Dienstag, den 10. Januar", "2012-08-10T12:00:00", "2013-01-10T12:00:00"),
            Arguments.of("Die Deadline ist Di, 10. Januar", "2012-08-10T12:00:00", "2013-01-10T12:00:00"),
            Arguments.of("31. März 2016", "2012-08-10T12:00:00", "2016-03-31T12:00:00"),
            Arguments.of("31.Maerz 2016", "2012-08-10T12:00:00", "2016-03-31T12:00:00"),
            Arguments.of("10. jänner 2012", "2012-08-10T12:00:00", "2012-01-10T12:00:00"),
            Arguments.of("12. Juli um 19:00", "2012-08-10T12:00:00", "2012-07-12T19:00:00"),
            Arguments.of("12. Juli um 19 Uhr", "2012-08-10T12:00:00", "2012-07-12T19:00:00"),
            Arguments.of("12. Juli um 19:53 Uhr", "2012-08-10T12:00:00", "2012-07-12T19:53:00"),
            Arguments.of("5. Juni 12:00", "2012-08-10T12:00:00", "2012-06-05T12:00:00"),
        )

        @JvmStatic
        fun rangeExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("10. - 22. August 2012", "2012-08-10T12:00:00", "2012-08-10T12:00:00", "2012-08-22T12:00:00"),
            Arguments.of("10. bis 22. Oktober 2012", "2012-08-10T12:00:00", "2012-10-10T12:00:00", "2012-10-22T12:00:00"),
            Arguments.of("10. Oktober - 12. Dezember", "2012-08-10T12:00:00", "2012-10-10T12:00:00", "2012-12-12T12:00:00"),
            Arguments.of("10. August - 12. Oktober 2013", "2012-08-10T12:00:00", "2013-08-10T12:00:00", "2013-10-12T12:00:00"),
        )
    }
}
