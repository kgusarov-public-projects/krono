package org.kgusarov.krono.locales.en

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.Krono
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase
import org.kgusarov.krono.testUnexpectedResult

private const val REF_DATE_2012_07_10 = "2012-07-10T00:00:00"

internal class EnYearMonthDayTest {
    @Test
    internal fun `single expression start with year`() {
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
    internal fun `single expression start with year and month name`() {
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
    internal fun `allow date month swap in casual mode`() {
        testUnexpectedResult(Krono.enStrict, "2024/13/1")
        testUnexpectedResult(Krono.enStrict, "2024-13-01")

        testSingleCase(Krono.enCasual, "2024/13/1", REF_DATE_2012_07_10) {
            it.start.assertDate("2024-01-13T12:00:00")
        }

        testSingleCase(Krono.enCasual, "2024-13-01", REF_DATE_2012_07_10) {
            it.start.assertDate("2024-01-13T12:00:00")
        }
    }

    @Test
    internal fun `unlikely xxxx-xx-xx pattern`() {
        testUnexpectedResult(Krono.enCasual, "2012/80/10")
        testUnexpectedResult(Krono.enCasual, "2012 80 10")
    }

    @Test
    internal fun `impossible dates`() {
        testUnexpectedResult(Krono.enCasual, "2014-08-32")
        testUnexpectedResult(Krono.enCasual, "2014-02-30")
    }
}
