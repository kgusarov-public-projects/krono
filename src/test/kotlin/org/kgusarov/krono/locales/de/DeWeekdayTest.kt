package org.kgusarov.krono.locales.de

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.Krono
import org.kgusarov.krono.ParsingOption
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase

internal class DeWeekdayTest {
    @Test
    internal fun `simple expression montag`() {
        testSingleCase(Krono.deCasual, "Montag", "2012-08-09T12:00:00") {
            with (it.start) {
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
    internal fun `simple expression am donnerstag`() {
        testSingleCase(Krono.deCasual, "am Donnerstag", "2012-08-09T12:00:00") {
            assertThat(it.text).isEqualTo("am Donnerstag")
            with (it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(9)
                assertThat(weekday()).isEqualTo(4)

                assertDate("2012-08-09T12:00:00")
            }
        }
    }

    @Test
    internal fun `simple expression sonntag`() {
        testSingleCase(Krono.deCasual, "Sonntag", "2012-08-09T12:00:00") {
            assertThat(it.text).isEqualTo("Sonntag")
            with (it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(12)
                assertThat(weekday()).isEqualTo(7)

                assertDate("2012-08-12T12:00:00")
            }
        }
    }

    @Test
    internal fun `simple expression letzten freitag`() {
        testSingleCase(Krono.deCasual, "Die Deadline war letzten Freitag...", "2012-08-09T12:00:00") {
            assertThat(it.index).isEqualTo(17)
            assertThat(it.text).isEqualTo("letzten Freitag")
            with (it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(3)
                assertThat(weekday()).isEqualTo(5)

                assertDate("2012-08-03T12:00:00")
            }
        }
    }

    @Test
    internal fun `simple expression treffen wir uns am freitag naechste woche`() {
        testSingleCase(Krono.deCasual, "Treffen wir uns am Freitag nächste Woche", "2015-04-18T12:00:00") {
            assertThat(it.index).isEqualTo(16)
            assertThat(it.text).isEqualTo("am Freitag nächste Woche")
            with (it.start) {
                assertThat(year()).isEqualTo(2015)
                assertThat(month()).isEqualTo(4)
                assertThat(day()).isEqualTo(24)
                assertThat(weekday()).isEqualTo(5)

                assertDate("2015-04-24T12:00:00")
            }
        }
    }

    @Test
    internal fun `simple expression ich habe vor am dienstag naechste woche freizunehmen`() {
        testSingleCase(Krono.deCasual, "Ich habe vor, am Dienstag nächste Woche freizunehmen", "2015-04-18T12:00:00") {
            assertThat(it.index).isEqualTo(14)
            assertThat(it.text).isEqualTo("am Dienstag nächste Woche")
            with (it.start) {
                assertThat(year()).isEqualTo(2015)
                assertThat(month()).isEqualTo(4)
                assertThat(day()).isEqualTo(21)
                assertThat(weekday()).isEqualTo(2)

                assertDate("2015-04-21T12:00:00")
            }
        }
    }

    @Test
    internal fun `forward date diesen freitag bis diesen montag`() {
        testSingleCase(
            Krono.deCasual,
            "diesen Freitag bis diesen Montag",
            "2016-08-04T12:00:00",
            ParsingOption(forwardDate = true)
        ) {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("diesen Freitag bis diesen Montag")
            with (it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(5)
                assertThat(weekday()).isEqualTo(5)

                assertThat(certainDay()).isFalse()
                assertThat(certainMonth()).isFalse()
                assertThat(certainYear()).isFalse()
                assertThat(certainWeekday()).isTrue()

                assertDate("2016-08-05T12:00:00")
            }
            with (it.end!!) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(8)
                assertThat(weekday()).isEqualTo(1)

                assertThat(certainDay()).isFalse()
                assertThat(certainMonth()).isFalse()
                assertThat(certainYear()).isFalse()
                assertThat(certainWeekday()).isTrue()

                assertDate("2016-08-08T12:00:00")
            }
        }
    }

    @Test
    internal fun `weekday overlap - sonntag, den 7 dezember 2014`() {
        testSingleCase(Krono.deCasual, "Sonntag, den 7. Dezember 2014", "2012-08-09T12:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("Sonntag, den 7. Dezember 2014")
            with (it.start) {
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
    internal fun `weekday overlap - sonntag 7 12 2014`() {
        testSingleCase(Krono.deCasual, "Sonntag 7.12.2014", "2012-08-09T12:00:00") {
            assertThat(it.text).isEqualTo("Sonntag 7.12.2014")
            with (it.start) {
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
