package org.kgusarov.krono.locales.de

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.Krono
import org.kgusarov.krono.testSingleCase

internal class DeTimeUnitsCasualRelativeTest {
    @Test
    internal fun `relative date - kommende Woche`() {
        testSingleCase(Krono.deCasual, "kommende Woche", "2017-05-12T12:00:00") {
            assertThat(it.text).isEqualTo("kommende Woche")
            with (it.start) {
                assertThat(year()).isEqualTo(2017)
                assertThat(month()).isEqualTo(5)
                assertThat(day()).isEqualTo(19)
            }
        }
    }

    @Test
    internal fun `relative date - in 2 Wochen`() {
        testSingleCase(Krono.deCasual, "in 2 Wochen", "2017-05-12T18:11:00") {
            assertThat(it.text).isEqualTo("in 2 Wochen")
            with (it.start) {
                assertThat(year()).isEqualTo(2017)
                assertThat(month()).isEqualTo(5)
                assertThat(day()).isEqualTo(26)
                assertThat(hour()).isEqualTo(18)
                assertThat(minute()).isEqualTo(11)
            }
        }
    }

    @Test
    internal fun `relative date - in drei Wochen`() {
        testSingleCase(Krono.deCasual, "in drei Wochen", "2017-05-12T12:00:00") {
            assertThat(it.text).isEqualTo("in drei Wochen")
            with (it.start) {
                assertThat(year()).isEqualTo(2017)
                assertThat(month()).isEqualTo(6)
                assertThat(day()).isEqualTo(2)
            }
        }
    }

    @Test
    internal fun `relative date - letzten Monat`() {
        testSingleCase(Krono.deCasual, "letzten Monat", "2017-05-12T12:00:00") {
            assertThat(it.text).isEqualTo("letzten Monat")
            with (it.start) {
                assertThat(year()).isEqualTo(2017)
                assertThat(month()).isEqualTo(4)
                assertThat(day()).isEqualTo(12)
            }
        }
    }

    @Test
    internal fun `relative date - in den 30 vorangegangenen Tagen`() {
        testSingleCase(Krono.deCasual, "in den 30 vorangegangenen Tagen", "2017-05-12T12:00:00") {
            assertThat(it.text).isEqualTo("30 vorangegangenen Tagen")
            with (it.start) {
                assertThat(year()).isEqualTo(2017)
                assertThat(month()).isEqualTo(4)
                assertThat(day()).isEqualTo(12)
            }
        }
    }

    @Test
    internal fun `relative date - die vergangenen 24 Stunden`() {
        testSingleCase(Krono.deCasual, "die vergangenen 24 Stunden", "2017-05-12T11:27:00") {
            assertThat(it.text).isEqualTo("vergangenen 24 Stunden")
            with (it.start) {
                assertThat(year()).isEqualTo(2017)
                assertThat(month()).isEqualTo(5)
                assertThat(day()).isEqualTo(11)
                assertThat(hour()).isEqualTo(11)
                assertThat(minute()).isEqualTo(27)
            }
        }
    }

    @Test
    internal fun `relative date - in den folgenden 90 sekunden`() {
        testSingleCase(Krono.deCasual, "in den folgenden 90 sekunden", "2017-05-12T11:27:03") {
            assertThat(it.text).isEqualTo("folgenden 90 sekunden")
            with (it.start) {
                assertThat(year()).isEqualTo(2017)
                assertThat(month()).isEqualTo(5)
                assertThat(day()).isEqualTo(12)
                assertThat(hour()).isEqualTo(11)
                assertThat(minute()).isEqualTo(28)
                assertThat(second()).isEqualTo(33)
            }
        }
    }

    @Test
    internal fun `relative date - die letzten acht Minuten`() {
        testSingleCase(Krono.deCasual, "die letzten acht Minuten", "2017-05-12T11:27:00") {
            assertThat(it.text).isEqualTo("letzten acht Minuten")
            with (it.start) {
                assertThat(year()).isEqualTo(2017)
                assertThat(month()).isEqualTo(5)
                assertThat(day()).isEqualTo(12)
                assertThat(hour()).isEqualTo(11)
                assertThat(minute()).isEqualTo(19)
                assertThat(second()).isEqualTo(0)
            }
        }
    }

    @Test
    internal fun `relative date - letztes Quartal`() {
        testSingleCase(Krono.deCasual, "letztes Quartal", "2017-05-12T11:27:00") {
            assertThat(it.text).isEqualTo("letztes Quartal")
            with (it.start) {
                assertThat(year()).isEqualTo(2017)
                assertThat(month()).isEqualTo(2)
                assertThat(day()).isEqualTo(12)
                assertThat(hour()).isEqualTo(11)
                assertThat(minute()).isEqualTo(27)
                assertThat(second()).isEqualTo(0)
                assertThat(certainMonth()).isFalse()
                assertThat(certainDay()).isFalse()
                assertThat(certainHour()).isFalse()
                assertThat(certainMinute()).isFalse()
                assertThat(certainSecond()).isFalse()
            }
        }
    }

    @Test
    internal fun `relative date - kommendes Jahr`() {
        testSingleCase(Krono.deCasual, "kommendes Jahr", "2017-05-12T11:27:00") {
            assertThat(it.text).isEqualTo("kommendes Jahr")
            with (it.start) {
                assertThat(year()).isEqualTo(2018)
                assertThat(month()).isEqualTo(5)
                assertThat(day()).isEqualTo(12)
                assertThat(hour()).isEqualTo(11)
                assertThat(minute()).isEqualTo(27)
                assertThat(second()).isEqualTo(0)
                assertThat(certainMonth()).isFalse()
                assertThat(certainDay()).isFalse()
                assertThat(certainHour()).isFalse()
                assertThat(certainMinute()).isFalse()
                assertThat(certainSecond()).isFalse()
            }
        }
    }
}
