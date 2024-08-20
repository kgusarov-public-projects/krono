package org.kgusarov.krono.locales.pt

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.Krono
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase
import org.kgusarov.krono.testUnexpectedResult
import java.util.stream.Stream

internal class PtMonthNameLittleEndianTest {
    @ParameterizedTest
    @MethodSource("singleExpressionArgs")
    fun `single expression`(text: String, refDate: String, expectedDate: String) {
        testSingleCase(Krono.ptCasual, text, refDate) {
            it.start.assertDate(expectedDate)
        }
    }

    @ParameterizedTest
    @MethodSource("rangeExpressionArgs")
    fun `range expression`(
        text: String,
        refDate: String,
        expectedStartDate: String,
        expectedEndDate: String,
    ) {
        testSingleCase(Krono.ptCasual, text, refDate) {
            it.start.assertDate(expectedStartDate)
            it.end!!.assertDate(expectedEndDate)
        }
    }
    
    @Test
    fun `impossible dates in strict mode`() {
        testUnexpectedResult(Krono.ptStrict, "32 Agosto 2014")
        testUnexpectedResult(Krono.ptStrict, "29 Fevereiro 2014")
        testUnexpectedResult(Krono.ptStrict, "32 Agosto")
    }

    @Test
    fun `combined expression`() {
        testSingleCase(Krono.ptCasual, "12 de Julho às 19:00", "2012-07-10T00:00:00") {
            it.start.assertDate("2012-07-12T19:00:00")
        }
    }

    companion object {
        @JvmStatic
        fun singleExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("10 Agosto 2012", "2012-08-10T12:00:00", "2012-08-10T12:00:00"),
            Arguments.of("10 Agosto 234 AC", "-0234-08-10T12:00:00", "-0234-08-10T12:00:00"),
            Arguments.of("10 Agosto 88 d. C.", "0088-08-10T12:00:00", "0088-08-10T12:00:00"),
            Arguments.of("Dom 15Set", "2013-09-15T12:00:00", "2013-09-15T12:00:00"),
            Arguments.of("DOM 15SET", "2013-09-15T12:00:00", "2013-09-15T12:00:00"),
            Arguments.of("O prazo é 10 Agosto", "2012-08-10T12:00:00", "2012-08-10T12:00:00"),
            Arguments.of("O prazo é terça-feira, 10 de janeiro", "2013-01-10T12:00:00", "2013-01-10T12:00:00"),
            Arguments.of("O prazo é Qua, 10 Janeiro", "2013-01-10T12:00:00", "2013-01-10T12:00:00"),
            Arguments.of("10 de Agosto de 2012", "2012-08-10T12:00:00", "2012-08-10T12:00:00"),
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
                "10 Agosto - 12 Setembro", "2012-08-10T12:00:00", "2012-08-10T12:00:00", "2012-09-12T12:00:00"
            ),
            Arguments.of(
                "10 Agosto - 12 Setembro 2013", "2012-08-10T12:00:00", "2013-08-10T12:00:00", "2013-09-12T12:00:00"
            ),
        )
    }
}
