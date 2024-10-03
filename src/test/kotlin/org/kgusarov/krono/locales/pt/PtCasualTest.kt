package org.kgusarov.krono.locales.pt

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.Krono
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase
import org.kgusarov.krono.testUnexpectedResult
import java.util.stream.Stream

internal class PtCasualTest {
    @ParameterizedTest
    @MethodSource("singleExpressionArgs")
    internal fun `single expression`(text: String, refDate: String, expectedDate: String) {
        testSingleCase(Krono.ptCasual, text, refDate) {
            it.start.assertDate(expectedDate)
        }
    }
    
    @Test
    internal fun `random negative text`() {
        testUnexpectedResult(Krono.ptCasual, "naohoje")
        testUnexpectedResult(Krono.ptCasual, "hyamanhã")
        testUnexpectedResult(Krono.ptCasual, "xontem")
        testUnexpectedResult(Krono.ptCasual, "porhora")
        testUnexpectedResult(Krono.ptCasual, "agoraxsd")
    }

    @Test
    internal fun `combined expression`() {
        testSingleCase(Krono.ptCasual, "O prazo é hoje às 5PM", "2012-08-10T12:00:00") {
            assertThat(it.text).isEqualTo("hoje às 5PM")
            assertThat(it.index).isEqualTo(10)

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(10)
                assertThat(hour()).isEqualTo(17)

                assertDate("2012-08-10T17:00:00")
            }
        }
    }

    @ParameterizedTest
    @MethodSource("randomTextArgs")
    internal fun `random text`(text: String, refDate: String, expectedDate: String) {
        testSingleCase(Krono.ptCasual, text, refDate) {
            it.start.assertDate(expectedDate)
        }
    }

    companion object {
        @JvmStatic
        fun singleExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("O prazo é agora", "2012-08-10T08:09:10.011", "2012-08-10T08:09:10.011"),
            Arguments.of("O prazo é hoje", "2012-08-10T12:00", "2012-08-10T12:00"),
            Arguments.of("O prazo é Amanhã", "2012-08-10T12:00", "2012-08-11T12:00"),
            Arguments.of("O prazo foi ontem", "2012-08-10T12:00", "2012-08-09T12:00"),
            Arguments.of("O prazo foi ontem à noite", "2012-08-10T12:00", "2012-08-09T22:00"),
            Arguments.of("O prazo foi esta manhã", "2012-08-10T12:00", "2012-08-10T06:00"),
            Arguments.of("O prazo foi esta tarde", "2012-08-10T12:00", "2012-08-10T15:00"),
        )

        @JvmStatic
        fun `randomTextArgs`(): Stream<Arguments> = Stream.of(
            Arguments.of("esta noite", "2012-01-01T12:00:00", "2012-01-01T22:00:00"),
            Arguments.of("esta noite 8pm", "2012-01-01T12:00:00", "2012-01-01T20:00:00"),
            Arguments.of("esta noite às 8", "2012-01-01T12:00:00", "2012-01-01T20:00:00"),
            Arguments.of("quinta", "2012-01-01T12:00:00", "2011-12-29T12:00:00"),
            Arguments.of("sexta", "2012-01-01T12:00:00", "2011-12-30T12:00:00"),
            Arguments.of("ao meio-dia", "2020-09-01T11:00:00", "2020-09-01T12:00:00"),
            Arguments.of("a meia-noite", "2020-09-01T11:00:00", "2020-09-02T00:00:00"),
        )
    }
}

