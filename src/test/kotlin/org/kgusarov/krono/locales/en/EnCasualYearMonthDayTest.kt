package org.kgusarov.krono.locales.en

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.Krono
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase
import org.kgusarov.krono.testUnexpectedResult

private const val REF_DATE_2012_07_10 = "2012-07-10T00:00:00"

internal class EnCasualYearMonthDayTest {
    @Test
    fun `single expression start with year`() {
        testSingleCase(Krono.enCasual, "2012/8/10", REF_DATE_2012_07_10) {
            assertThat(it.text).isEqualTo("2012/8/10")
            assertThat(it.index).isEqualTo(0)

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(10)

                assertDate("2012-08-10T12:00:00")
            }
        }

        testSingleCase(Krono.enCasual, "The Deadline is 2012/8/10", REF_DATE_2012_07_10) {
            assertThat(it.text).isEqualTo("2012/8/10")
            assertThat(it.index).isEqualTo(16)

            with(it.start) {
                assertDate("2012-08-10T12:00:00")
            }
        }

        testSingleCase(Krono.enStrict, "2014/2/28") {
            assertThat(it.text).isEqualTo("2014/2/28")
            assertThat(it.index).isEqualTo(0)

            with(it.start) {
                assertDate("2014-02-28T12:00:00")
            }
        }

        testSingleCase(Krono.enStrict, "2014/12/28") {
            assertThat(it.text).isEqualTo("2014/12/28")
            assertThat(it.index).isEqualTo(0)

            with(it.start) {
                assertDate("2014-12-28T12:00:00")
            }
        }

        testSingleCase(Krono.enStrict, "2014.12.28") {
            assertThat(it.text).isEqualTo("2014.12.28")
            assertThat(it.index).isEqualTo(0)

            with(it.start) {
                assertDate("2014-12-28T12:00:00")
            }
        }

        testSingleCase(Krono.enStrict, "2014 12 28") {
            assertThat(it.text).isEqualTo("2014 12 28")
            assertThat(it.index).isEqualTo(0)

            with(it.start) {
                assertDate("2014-12-28T12:00:00")
            }
        }
    }

    @Test
    fun `single expression start with year and month name`() {
        testSingleCase(Krono.enCasual, "2012/Aug/10", REF_DATE_2012_07_10) {
            assertThat(it.text).isEqualTo("2012/Aug/10")
            assertThat(it.index).isEqualTo(0)

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(10)

                assertDate("2012-08-10T12:00:00")
            }
        }

        testSingleCase(Krono.enCasual, "The Deadline is 2012/aug/10", REF_DATE_2012_07_10) {
            assertThat(it.text).isEqualTo("2012/aug/10")
            assertThat(it.index).isEqualTo(16)

            with(it.start) {
                assertDate("2012-08-10T12:00:00")
            }
        }

        testSingleCase(Krono.enCasual, "The Deadline is 2018 March 18", REF_DATE_2012_07_10) {
            assertThat(it.text).isEqualTo("2018 March 18")
            assertThat(it.index).isEqualTo(16)

            with(it.start) {
                assertThat(year()).isEqualTo(2018)
                assertThat(month()).isEqualTo(3)
                assertThat(day()).isEqualTo(18)

                assertDate("2018-03-18T12:00:00")
            }
        }
    }

    @Test
    fun `negative year-month-day like pattern`() {
        testUnexpectedResult(Krono.enCasual, "2012-80-10", REF_DATE_2012_07_10)
        testUnexpectedResult(Krono.enCasual, "2012 80 10", REF_DATE_2012_07_10)
    }
}
