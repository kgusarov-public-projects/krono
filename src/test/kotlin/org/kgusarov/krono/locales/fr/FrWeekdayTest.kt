package org.kgusarov.krono.locales.fr

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.Krono
import org.kgusarov.krono.ParsingOption
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase

internal class FrWeekdayTest {
    @Test
    internal fun `lundi`() {
        testSingleCase(Krono.frCasual, "Lundi", "2012-08-09T00:00:00") {
            assertThat(it.text).isEqualTo("Lundi")
            assertThat(it.index).isEqualTo(0)

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(6)
                assertThat(weekday()).isEqualTo(1)

                assertThat(certainDay()).isFalse()
                assertThat(certainMonth()).isFalse()
                assertThat(certainYear()).isFalse()
                assertThat(certainWeekday()).isTrue()

                assertDate("2012-08-06T12:00:00")
            }
        }
    }

    @Test
    internal fun `lundi - forward dates only`() {
        testSingleCase(Krono.frCasual, "Lundi", "2012-08-09T00:00:00", ParsingOption(forwardDate = true)) {
            assertThat(it.text).isEqualTo("Lundi")
            assertThat(it.index).isEqualTo(0)

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(13)
                assertThat(weekday()).isEqualTo(1)

                assertThat(certainDay()).isFalse()
                assertThat(certainMonth()).isFalse()
                assertThat(certainYear()).isFalse()
                assertThat(certainWeekday()).isTrue()

                assertDate("2012-08-13T12:00:00")
            }
        }
    }

    @Test
    internal fun `jeudi`() {
        testSingleCase(Krono.frCasual, "Jeudi", "2012-08-09T00:00:00") {
            assertThat(it.text).isEqualTo("Jeudi")
            assertThat(it.index).isEqualTo(0)

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(9)
                assertThat(weekday()).isEqualTo(4)

                assertThat(certainDay()).isFalse()
                assertThat(certainMonth()).isFalse()
                assertThat(certainYear()).isFalse()
                assertThat(certainWeekday()).isTrue()

                assertDate("2012-08-09T12:00:00")
            }
        }
    }

    @Test
    internal fun `dimanche`() {
        testSingleCase(Krono.frCasual, "Dimanche", "2012-08-09T00:00:00") {
            assertThat(it.text).isEqualTo("Dimanche")
            assertThat(it.index).isEqualTo(0)

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(12)
                assertThat(weekday()).isEqualTo(7)

                assertThat(certainDay()).isFalse()
                assertThat(certainMonth()).isFalse()
                assertThat(certainYear()).isFalse()
                assertThat(certainWeekday()).isTrue()

                assertDate("2012-08-12T12:00:00")
            }
        }
    }

    @Test
    internal fun `vendredi dernier`() {
        testSingleCase(Krono.frCasual, "la deadline était vendredi dernier...", "2012-08-09T00:00:00") {
            assertThat(it.text).isEqualTo("vendredi dernier")
            assertThat(it.index).isEqualTo(18)

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(3)
                assertThat(weekday()).isEqualTo(5)

                assertThat(certainDay()).isFalse()
                assertThat(certainMonth()).isFalse()
                assertThat(certainYear()).isFalse()
                assertThat(certainWeekday()).isTrue()

                assertDate("2012-08-03T12:00:00")
            }
        }
    }

    @Test
    internal fun `vendredi prochain`() {
        testSingleCase(Krono.frCasual, "Planifions une réuinion vendredi prochain", "2015-04-18T00:00:00") {
            assertThat(it.text).isEqualTo("vendredi prochain")
            assertThat(it.index).isEqualTo(24)

            with(it.start) {
                assertThat(year()).isEqualTo(2015)
                assertThat(month()).isEqualTo(4)
                assertThat(day()).isEqualTo(24)
                assertThat(weekday()).isEqualTo(5)

                assertThat(certainDay()).isFalse()
                assertThat(certainMonth()).isFalse()
                assertThat(certainYear()).isFalse()
                assertThat(certainWeekday()).isTrue()

                assertDate("2015-04-24T12:00:00")
            }
        }
    }

    @Test
    internal fun `weekday overlap - dimanche 7 décembre 2014`() {
        testSingleCase(Krono.frCasual, "Dimanche 7 décembre 2014", "2015-04-18T00:00:00") {
            assertThat(it.text).isEqualTo("Dimanche 7 décembre 2014")
            assertThat(it.index).isEqualTo(0)

            with(it.start) {
                assertThat(year()).isEqualTo(2014)
                assertThat(month()).isEqualTo(12)
                assertThat(day()).isEqualTo(7)
                assertThat(weekday()).isEqualTo(7)

                assertThat(certainDay()).isTrue()
                assertThat(certainMonth()).isTrue()
                assertThat(certainYear()).isTrue()
                assertThat(certainWeekday()).isTrue()

                assertDate("2014-12-07T12:00:00")
            }
        }
    }

    @Test
    internal fun `weekday overlap - dimanche 7-12-2014`() {
        testSingleCase(Krono.frCasual, "Dimanche 7/12/2014", "2015-04-18T00:00:00") {
            assertThat(it.text).isEqualTo("Dimanche 7/12/2014")
            assertThat(it.index).isEqualTo(0)

            with(it.start) {
                assertThat(year()).isEqualTo(2014)
                assertThat(month()).isEqualTo(12)
                assertThat(day()).isEqualTo(7)
                assertThat(weekday()).isEqualTo(7)

                assertThat(certainDay()).isTrue()
                assertThat(certainMonth()).isTrue()
                assertThat(certainYear()).isTrue()
                assertThat(certainWeekday()).isTrue()

                assertDate("2014-12-07T12:00:00")
            }
        }
    }
}
