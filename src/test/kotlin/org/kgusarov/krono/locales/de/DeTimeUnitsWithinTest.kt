package org.kgusarov.krono.locales.de

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.Krono
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase

internal class DeTimeUnitsWithinTest {
    @Test
    internal fun `wir mussen etwas in 5 tagen erledigen`() {
        testSingleCase(Krono.deCasual, "Wir müssen etwas in 5 Tagen erledigen.", "2012-08-10T12:00:00") {
            assertThat(it.index).isEqualTo(17)
            assertThat(it.text).isEqualTo("in 5 Tagen")
            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(15)
            }
        }
    }

    @Test
    internal fun `wir mussen etwas in funf tagen erledigen`() {
        testSingleCase(Krono.deCasual, "Wir müssen etwas in fünf Tagen erledigen.", "2012-08-10T11:12:00") {
            assertThat(it.index).isEqualTo(17)
            assertThat(it.text).isEqualTo("in fünf Tagen")
            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(15)
                assertThat(hour()).isEqualTo(11)
                assertThat(minute()).isEqualTo(12)
            }
        }
    }

    @Test
    internal fun `in 5 minuten`() {
        testSingleCase(Krono.deCasual, "in 5 Minuten", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("in 5 Minuten")
            it.start.assertDate("2012-08-10T12:19:00")
        }
    }

    @Test
    internal fun `fur 5 minuten`() {
        testSingleCase(Krono.deCasual, "für 5 Minuten", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("für 5 Minuten")
            it.start.assertDate("2012-08-10T12:19:00")
        }
    }

    @Test
    internal fun `in einer stunde`() {
        testSingleCase(Krono.deCasual, "in einer Stunde", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("in einer Stunde")
            it.start.assertDate("2012-08-10T13:14:00")
        }
    }

    @Test
    internal fun `starte einen timer fur 5 minuten`() {
        testSingleCase(Krono.deCasual, "starte einen Timer für 5 Minuten", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(19)
            assertThat(it.text).isEqualTo("für 5 Minuten")
            it.start.assertDate("2012-08-10T12:19:00")
        }
    }

    @Test
    internal fun `in 5 minuten gehe ich nach hause`() {
        testSingleCase(Krono.deCasual, "In 5 Minuten gehe ich nach Hause", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("In 5 Minuten")
            it.start.assertDate("2012-08-10T12:19:00")
        }
    }

    @Test
    internal fun `in 5 sekunden wird ein auto fahren`() {
        testSingleCase(Krono.deCasual, "In 5 Sekunden wird ein Auto fahren", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("In 5 Sekunden")
            it.start.assertDate("2012-08-10T12:14:05")
        }
    }

    @Test
    internal fun `in zwei wochen`() {
        testSingleCase(Krono.deCasual, "in zwei Wochen", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("in zwei Wochen")
            it.start.assertDate("2012-08-24T12:14:00")
        }
    }

    @Test
    internal fun `in einem monat`() {
        testSingleCase(Krono.deCasual, "in einem Monat", "2012-08-10T07:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("in einem Monat")
            it.start.assertDate("2012-09-10T07:14:00")
        }
    }

    @Test
    internal fun `in einigen monaten`() {
        testSingleCase(Krono.deCasual, "in einigen Monaten", "2012-07-10T22:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("in einigen Monaten")
            it.start.assertDate("2012-10-10T22:14:00")
        }
    }

    @Test
    internal fun `in einem jahr`() {
        testSingleCase(Krono.deCasual, "in einem Jahr", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("in einem Jahr")
            it.start.assertDate("2013-08-10T12:14:00")
        }
    }

    @Test
    internal fun `in 20 jahren`() {
        testSingleCase(Krono.deCasual, "in 20 Jahren", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("in 20 Jahren")
            it.start.assertDate("2032-08-10T12:14:00")
        }
    }

    @Test
    internal fun `in 5 minuten wird ein auto fahren`() {
        testSingleCase(Krono.deCasual, "In 5 Minuten wird ein Auto fahren", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("In 5 Minuten")
            it.start.assertDate("2012-08-10T12:19:00")
        }
    }

    @Test
    internal fun `in 5 min wird ein auto fahren`() {
        testSingleCase(Krono.deCasual, "In 5 Min wird ein Auto fahren", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("In 5 Min")
            it.start.assertDate("2012-08-10T12:19:00")
        }
    }
}
