package org.kgusarov.krono.locales.es

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.Krono
import org.kgusarov.krono.ParsingOption
import org.kgusarov.krono.TimezoneAbbreviations
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.assertOffsetDate
import org.kgusarov.krono.testSingleCase

internal class EsTimeUnitsWithinTest {
    @Test
    fun `en 5 días`() {
        testSingleCase(Krono.esCasual, "Tenemos que hacer algo en 5 días.", "2012-08-10T00:00:00") {
            assertThat(it.index).isEqualTo(23)
            assertThat(it.text).isEqualTo("en 5 días")

            with (it.start) {
                assertDate("2012-08-15T00:00:00")
            }
        }
    }

    @Test
    fun `en cinco días`() {
        testSingleCase(Krono.esCasual, "Tenemos que hacer algo en cinco días.", "2012-08-10T00:00:00") {
            assertThat(it.index).isEqualTo(23)
            assertThat(it.text).isEqualTo("en cinco días")

            with (it.start) {
                assertDate("2012-08-15T00:00:00")
            }
        }
    }

    @Test
    fun `en 5 minutos - case 1`() {
        testSingleCase(Krono.esCasual, "en 5 minutos", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("en 5 minutos")

            with (it.start) {
                assertDate("2012-08-10T12:19:00")
            }
        }
    }

    @Test
    fun `por 5 minutos`() {
        testSingleCase(Krono.esCasual, "por 5 minutos", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("por 5 minutos")

            with (it.start) {
                assertDate("2012-08-10T12:19:00")
            }
        }
    }

    @Test
    fun `en 1 hora`() {
        testSingleCase(Krono.esCasual, "en 1 hora", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("en 1 hora")

            with (it.start) {
                assertDate("2012-08-10T13:14:00")
            }
        }
    }

    @Test
    fun `de 5 minutos`() {
        testSingleCase(Krono.esCasual, "establecer un temporizador de 5 minutos", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(27)
            assertThat(it.text).isEqualTo("de 5 minutos")

            with (it.start) {
                assertDate("2012-08-10T12:19:00")
            }
        }
    }

    @Test
    fun `en 5 minutos - case 2`() {
        testSingleCase(Krono.esCasual, "En 5 minutos me voy a casa", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("En 5 minutos")

            with (it.start) {
                assertDate("2012-08-10T12:19:00")
            }
        }
    }

    @Test
    fun `en 5 segundos`() {
        testSingleCase(Krono.esCasual, "En 5 segundos un auto se moverá.", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("En 5 segundos")

            with (it.start) {
                assertDate("2012-08-10T12:14:05")
            }
        }
    }

    @Test
    fun `en dos semanas`() {
        testSingleCase(Krono.esCasual, "en dos semanas", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("en dos semanas")

            with (it.start) {
                assertDate("2012-08-24T12:14:00")
            }
        }
    }

    @Test
    fun `dentro de un mes`() {
        testSingleCase(Krono.esCasual, "dentro de un mes", "2012-08-10T07:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("dentro de un mes")

            with (it.start) {
                assertDate("2012-09-10T07:14:00")
            }
        }
    }

    @Test
    fun `en algunos meses`() {
        testSingleCase(Krono.esCasual, "en algunos meses", "2012-07-10T22:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("en algunos meses")

            with (it.start) {
                assertDate("2012-10-10T22:14:00")
            }
        }
    }

    @Test
    fun `en un año`() {
        testSingleCase(Krono.esCasual, "en un año", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("en un año")

            with (it.start) {
                assertDate("2013-08-10T12:14:00")
            }
        }
    }

    @Test
    fun `dentro de un año`() {
        testSingleCase(Krono.esCasual, "dentro de un año", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("dentro de un año")

            with (it.start) {
                assertDate("2013-08-10T12:14:00")
            }
        }
    }

    @Test
    fun `en 5 minutos - case 3`() {
        testSingleCase(Krono.esCasual, "En 5 Minutos hay que mover un coche", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("En 5 Minutos")

            with (it.start) {
                assertDate("2012-08-10T12:19:00")
            }
        }
    }

    @Test
    fun `en 5 minutos - case 4`() {
        testSingleCase(Krono.esCasual, "En 5 minutos hay que mover un coche.", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("En 5 minutos")

            with (it.start) {
                assertDate("2012-08-10T12:19:00")
            }
        }
    }

    @Test
    fun `en 5 minutos - ambigious timezone map`() {
        testSingleCase(
            Krono.esCasual,
            "En 5 minutos hay que mover un coche.",
            "2012-08-10T12:14:00",
            ParsingOption(abbreviationMap = TimezoneAbbreviations.AMBIGIOUS_TIMEZONE_ABBR_MAP)
        ) {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("En 5 minutos hay")

            with (it.start) {
                assertOffsetDate("2012-08-10T12:19:00-08:00")
            }
        }
    }
}
