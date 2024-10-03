package org.kgusarov.krono.locales.es

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

internal class EsCasualTest {
    @Test
    internal fun `random negative text`() {
        testUnexpectedResult(Krono.esCasual, "nohoy")
        testUnexpectedResult(Krono.esCasual, "hymañana")
        testUnexpectedResult(Krono.esCasual, "xayer")
        testUnexpectedResult(Krono.esCasual, "porhora")
        testUnexpectedResult(Krono.esCasual, "ahoraxsd")
    }

    @ParameterizedTest
    @MethodSource("singleExpressionArgs")
    internal fun `single expression`(text: String, refDate: String, expectedDate: String) {
        testSingleCase(Krono.esCasual, text, refDate) {
            it.start.assertDate(expectedDate)
        }
    }

    @Test
    internal fun `combined expression`() {
        testSingleCase(Krono.esCasual, "La fecha límite es hoy a las 5PM", "2012-08-10T12:00:00") {
            assertThat(it.text).isEqualTo("hoy a las 5PM")
            assertThat(it.index).isEqualTo(19)

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
        testSingleCase(Krono.esCasual, text, refDate) {
            it.start.assertDate(expectedDate)
        }
    }

    companion object {
        @JvmStatic
        fun singleExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("La fecha límite es ahora", "2012-08-10T08:09:10.011", "2012-08-10T08:09:10.011"),
            Arguments.of("La fecha límite es hoy", "2012-08-10T12:00", "2012-08-10T12:00"),
            Arguments.of("La fecha límite es Mañana", "2012-08-10T12:00", "2012-08-11T12:00"),
            Arguments.of("La fecha límite es mañana", "2012-08-10T12:00", "2012-08-11T12:00"),
            Arguments.of("La fecha límite fue ayer", "2012-08-10T12:00", "2012-08-09T12:00"),
            Arguments.of("La fecha límite fue ayer de noche", "2012-08-10T12:00", "2012-08-09T22:00"),
            Arguments.of("La fecha límite fue esta mañana", "2012-08-10T12:00", "2012-08-10T06:00"),
            Arguments.of("La fecha límite fue esta tarde", "2012-08-10T12:00", "2012-08-10T15:00"),
        )

        @JvmStatic
        fun `randomTextArgs`(): Stream<Arguments> = Stream.of(
            Arguments.of("esta noche", "2012-01-01T12:00:00", "2012-01-01T22:00:00"),
            Arguments.of("esta noche 8pm", "2012-01-01T12:00:00", "2012-01-01T20:00:00"),
            Arguments.of("esta noche a las 8", "2012-01-01T12:00:00", "2012-01-01T20:00:00"),
            Arguments.of("jueves", "2012-01-01T12:00:00", "2011-12-29T12:00"),
            Arguments.of("viernes", "2012-01-01T12:00:00", "2011-12-30T12:00"),
            Arguments.of("el mediodía", "2020-09-01T11:00:00", "2020-09-01T12:00:00"),
            Arguments.of("la medianoche", "2020-09-01T11:00:00", "2020-09-02T00:00:00"),
        )
    }
}
