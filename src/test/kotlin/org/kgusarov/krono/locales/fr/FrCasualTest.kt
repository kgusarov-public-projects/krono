package org.kgusarov.krono.locales.fr

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.Krono
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase
import java.util.stream.Stream

internal class FrCasualTest {
    @ParameterizedTest
    @MethodSource("singleExpressionArgs")
    internal fun `single expression`(text: String, refDate: String, expectedDate: String) {
        testSingleCase(Krono.frCasual, text, refDate) {
            it.start.assertDate(expectedDate)
        }
    }

    @Test
    internal fun `range expression`() {
        testSingleCase(Krono.frCasual, "Du 24 août 2023 au 26 août 2023", "2012-08-10T12:00:00") {
            assertThat(it.text).isEqualTo("24 août 2023 au 26 août 2023")

            with(it.start) {
                 assertDate("2023-08-24T12:00:00")
            }

            with(it.end!!) {
                assertDate("2023-08-26T12:00:00")
            }
        }
    }

    companion object {
        @JvmStatic
        fun singleExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("La deadline est maintenant", "2012-08-10T08:09:10.011", "2012-08-10T08:09:10.011"),
            Arguments.of("La deadline est aujourd'hui", "2012-08-10T12:00", "2012-08-10T12:00"),
            Arguments.of("La deadline est demain", "2012-08-10T12:00", "2012-08-11T12:00"),
            Arguments.of("La deadline était hier", "2012-08-10T12:00", "2012-08-09T12:00"),
            Arguments.of("La deadline était la veille", "2012-08-10T12:00", "2012-08-09T00:00"),
            Arguments.of("La deadline est ce matin", "2012-08-10T12:00", "2012-08-10T08:00"),
            Arguments.of("La deadline est cet après-midi", "2012-08-10T12:00", "2012-08-10T14:00"),
            Arguments.of("La deadline est cet après-midi", "2012-08-10T12:00", "2012-08-10T14:00"),
            Arguments.of("La deadline est cet aprem", "2012-08-10T12:00", "2012-08-10T14:00"),
            Arguments.of("La deadline est ce soir", "2012-08-10T12:00", "2012-08-10T18:00"),
            Arguments.of("a midi", "2012-08-10T12:00", "2012-08-10T12:00"),
            Arguments.of("à minuit", "2012-08-10T12:00", "2012-08-10T00:00"),
            Arguments.of("La deadline est aujourd'hui 17:00", "2012-08-10T12:00", "2012-08-10T17:00"),
            Arguments.of("La deadline est demain 17:00", "2012-08-10T12:00", "2012-08-11T17:00"),
            Arguments.of("La deadline est demain matin 11h", "2012-08-10T12:00", "2012-08-11T11:00"),
        )
    }
}
