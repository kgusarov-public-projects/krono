package org.kgusarov.krono.locales.es

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.Krono
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase
import org.kgusarov.krono.testUnexpectedResult
import java.util.stream.Stream

internal class EsMonthNameLittleEndianTest {
    @ParameterizedTest
    @MethodSource("singleExpressionArgs")
    internal fun `single expression`(text: String, refDate: String, expectedDate: String) {
        testSingleCase(Krono.esCasual, text, refDate) {
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
        testSingleCase(Krono.esCasual, text, refDate) {
            it.start.assertDate(expectedStartDate)
            it.end!!.assertDate(expectedEndDate)
        }
    }
    
    @Test
    internal fun `impossible dates in strict mode`() {
        testUnexpectedResult(Krono.esStrict, "32 Agosto 2014")
        testUnexpectedResult(Krono.esStrict, "29 Febrero 2014")
        testUnexpectedResult(Krono.esStrict, "32 Agosto")
    }

    @Test
    internal fun `combined expression`() {
        testSingleCase(Krono.esCasual, "12 de julio a las 19:00", "2012-07-10T00:00:00") {
            it.start.assertDate("2012-07-12T19:00:00")
        }
    }

    companion object {
        @JvmStatic
        fun singleExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("10 Agosto 2012", "2012-08-10T12:00:00", "2012-08-10T12:00:00"),
            Arguments.of("10 Agosto 234 AC", "-0234-08-10T12:00:00", "-0234-08-10T12:00:00"),
            Arguments.of("10 Agosto 88 d. C.", "0088-08-10T12:00:00", "0088-08-10T12:00:00"),
            Arguments.of("Dom 15Sep", "2013-09-15T12:00:00", "2013-09-15T12:00:00"),
            Arguments.of("DOM 15SEP", "2013-09-15T12:00:00", "2013-09-15T12:00:00"),
            Arguments.of("La fecha límite es 10 Agosto", "2012-08-10T12:00:00", "2012-08-10T12:00:00"),
            Arguments.of("La fecha límite es el martes, 10 de enero", "2012-08-10T12:00:00", "2013-01-10T12:00:00"),
            Arguments.of("La fecha límite es el miércoles, 10 de enero", "2012-08-10T12:00:00", "2013-01-10T12:00:00"),
            Arguments.of("10 de Agosto de 2012", "2010-01-01T12:00:00", "2012-08-10T12:00:00"),
        )

        @JvmStatic
        fun rangeExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "10 - 22 Agosto 2012", "2012-08-10T12:00:00", "2012-08-10T12:00:00", "2012-08-22T12:00:00"
            ),
            Arguments.of(
                "10 a 22 Agosto 2012", "2012-08-10T12:00:00", "2012-08-10T12:00:00", "2012-08-22T12:00:00"
            ),
            Arguments.of(
                "10 Agosto - 12 Septiembre", "2012-08-10T12:00:00", "2012-08-10T12:00:00", "2012-09-12T12:00:00"
            ),
            Arguments.of(
                "10 Agosto - 12 Septiembre 2013", "2012-08-10T12:00:00", "2013-08-10T12:00:00", "2013-09-12T12:00:00"
            ),
        )
    }
}
