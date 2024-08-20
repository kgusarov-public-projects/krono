package org.kgusarov.krono.locales.en

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.Krono
import org.kgusarov.krono.ParsingOption
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase
import org.kgusarov.krono.testUnexpectedResult
import org.kgusarov.krono.testWithExpectedDate
import org.kgusarov.krono.testWithExpectedOffsetDate

private const val REF_DATE = "2012-08-10T00:00:00"

internal class EnSlashTest {
    @Test
    fun `offset expression`() {
        testSingleCase(Krono.enCasual, "    04/2016   ", REF_DATE) {
            assertThat(it.index).isEqualTo(4)
            assertThat(it.text).isEqualTo("04/2016")
        }
    }

    @Test
    fun `The event is going ahead (04-2016)`() {
        testSingleCase(Krono.enCasual, "The event is going ahead (04/2016)", REF_DATE) {
            assertThat(it.index).isEqualTo(26)
            assertThat(it.text).isEqualTo("04/2016")

            with(it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(4)
                assertThat(day()).isEqualTo(1)

                assertDate("2016-04-01T12:00:00")
            }
        }
    }

    @Test
    fun `published 06-2004`() {
        testSingleCase(Krono.enCasual, "Published: 06/2004", REF_DATE) {
            assertThat(it.index).isEqualTo(11)
            assertThat(it.text).isEqualTo("06/2004")

            with(it.start) {
                assertThat(year()).isEqualTo(2004)
                assertThat(month()).isEqualTo(6)
                assertThat(day()).isEqualTo(1)

                assertDate("2004-06-01T12:00:00")
            }
        }
    }

    @Test
    fun `8-10-2012`() {
        testSingleCase(Krono.enCasual, "8/10/2012", REF_DATE) {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("8/10/2012")

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(10)

                assertThat(certainDay()).isTrue()
                assertThat(certainMonth()).isTrue()
                assertThat(certainYear()).isTrue()

                assertDate("2012-08-10T12:00:00")
            }
        }
    }

    @Test
    fun `colon 8-1-2012`() {
        testSingleCase(Krono.enCasual, ": 8/1/2012", REF_DATE) {
            assertThat(it.index).isEqualTo(2)
            assertThat(it.text).isEqualTo("8/1/2012")

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(1)

                assertDate("2012-08-01T12:00:00")
            }
        }
    }

    @Test
    fun `8-10`() {
        testSingleCase(Krono.enCasual, "8/10", REF_DATE) {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("8/10")

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(10)

                assertThat(certainDay()).isTrue()
                assertThat(certainMonth()).isTrue()
                assertThat(certainYear()).isFalse()

                assertDate("2012-08-10T12:00:00")
            }
        }
    }

    @Test
    fun `the deadline is 8-10-2012`() {
        testSingleCase(Krono.enCasual, "The deadline is 8/10/2012", REF_DATE) {
            assertThat(it.index).isEqualTo(16)
            assertThat(it.text).isEqualTo("8/10/2012")

            with(it.start) {
                assertDate("2012-08-10T12:00:00")
            }
        }
    }

    @Test
    fun `the deadline is Tuesday 11-3-2015`() {
        testSingleCase(Krono.enCasual, "The Deadline is Tuesday 11/3/2015", REF_DATE) {
            assertThat(it.index).isEqualTo(16)
            assertThat(it.text).isEqualTo("Tuesday 11/3/2015")

            with(it.start) {
                assertDate("2015-11-03T12:00:00")
            }
        }
    }

    @Test
    fun `strict`() {
        testSingleCase(Krono.enStrict, "2/28/2014") {
            assertThat(it.text).isEqualTo("2/28/2014")
        }

        testWithExpectedDate(Krono.enStrict, "12-30-16", "2016-12-30T12:00:00")

        testSingleCase(Krono.enStrict, "Friday 12-30-16") {
            assertThat(it.text).isEqualTo("Friday 12-30-16")
            it.start.assertDate("2016-12-30T12:00:00")
        }
    }

    @Test
    fun `little endian`() {
        testWithExpectedDate(Krono.enGb, "8/10/2012", "2012-10-08T12:00:00")
        testWithExpectedDate(Krono.enStrict, "30-12-16", "2016-12-30T12:00:00")
        testWithExpectedDate(Krono.enStrict, "Friday 30-12-16", "2016-12-30T12:00:00")
    }

    @Test
    fun `little endian with month name`() {
        testWithExpectedDate(Krono.enGb, "8/Oct/2012", "2012-10-08T12:00:00")
        testWithExpectedDate(Krono.enStrict, "06/Nov/2023", "2023-11-06T12:00:00")
        testWithExpectedDate(Krono.enStrict, "06/Nov/2023:06:36:02", "2023-11-06T06:36:02")
        testWithExpectedOffsetDate(Krono.enStrict, "06/Nov/2023:06:36:02 +0200", "2023-11-06T06:36:02+02:00")
    }

    @Test
    fun `range expression`() {
        testSingleCase(Krono.enCasual, "8/10/2012 - 8/15/2012", REF_DATE) {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("8/10/2012 - 8/15/2012")

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(10)

                assertDate("2012-08-10T12:00:00")
            }

            with(it.end!!) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(15)

                assertDate("2012-08-15T12:00:00")
            }
        }
    }

    @Test
    fun `range expression with time`() {
        testSingleCase(Krono.enCasual, "from 01/21/2021 10:00 to 01/01/2023 07:00", REF_DATE) {
            assertThat(it.index).isEqualTo(5)
            assertThat(it.text).isEqualTo("01/21/2021 10:00 to 01/01/2023 07:00")

            with(it.start) {
                assertThat(year()).isEqualTo(2021)
                assertThat(month()).isEqualTo(1)
                assertThat(day()).isEqualTo(21)
                assertThat(hour()).isEqualTo(10)
                assertThat(minute()).isEqualTo(0)

                assertDate("2021-01-21T10:00:00")
            }

            with(it.end!!) {
                assertThat(year()).isEqualTo(2023)
                assertThat(month()).isEqualTo(1)
                assertThat(day()).isEqualTo(1)
                assertThat(hour()).isEqualTo(7)
                assertThat(minute()).isEqualTo(0)

                assertDate("2023-01-01T07:00:00")
            }
        }

        testSingleCase(Krono.enCasual, "08/08/2023, 09:15 AM to 08/29/2023, 09:15 AM", REF_DATE) {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("08/08/2023, 09:15 AM to 08/29/2023, 09:15 AM")

            with(it.start) {
                assertThat(year()).isEqualTo(2023)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(8)
                assertThat(hour()).isEqualTo(9)
                assertThat(minute()).isEqualTo(15)

                assertDate("2023-08-08T09:15:00")
            }

            with(it.end!!) {
                assertThat(year()).isEqualTo(2023)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(29)
                assertThat(hour()).isEqualTo(9)
                assertThat(minute()).isEqualTo(15)

                assertDate("2023-08-29T09:15:00")
            }
        }
    }

    @Test
    fun `splitter variances patterns`() {
        testWithExpectedDate(Krono.enCasual, "2015-05-25", "2015-05-25T12:00:00")
        testWithExpectedDate(Krono.enCasual, "2015/05/25", "2015-05-25T12:00:00")
        testWithExpectedDate(Krono.enCasual, "2015.05.25", "2015-05-25T12:00:00")
        testWithExpectedDate(Krono.enCasual, "05-25-2015", "2015-05-25T12:00:00")
        testWithExpectedDate(Krono.enCasual, "05/25/2015", "2015-05-25T12:00:00")
        testWithExpectedDate(Krono.enCasual, "05.25.2015", "2015-05-25T12:00:00")
        testWithExpectedDate(Krono.enCasual, "/05/25/2015", "2015-05-25T12:00:00")
        testWithExpectedDate(Krono.enCasual, "25/05/2015", "2015-05-25T12:00:00")
    }

    @Test
    fun `impossible dates and unexpected results`() {
        testUnexpectedResult(Krono.enCasual, "8/32/2014")
        testUnexpectedResult(Krono.enCasual, "8/32")
        testUnexpectedResult(Krono.enCasual, "2/29/2014")
        testUnexpectedResult(Krono.enCasual, "2014/22/29")
        testUnexpectedResult(Krono.enCasual, "2014/13/22")
        testUnexpectedResult(Krono.enCasual, "80-32-89-89")
        testUnexpectedResult(Krono.enCasual, "02/29/2022")
        testUnexpectedResult(Krono.enCasual, "06/31/2022")
        testUnexpectedResult(Krono.enCasual, "06/-31/2022")
        testUnexpectedResult(Krono.enCasual, "18/13/2022")
        testUnexpectedResult(Krono.enCasual, "15/28/2022")
        testUnexpectedResult(Krono.enCasual, "4/13/1");
    }

    @Test
    fun `forward dates only`() {
        testSingleCase(Krono.enCasual, "5/31", "1999-06-01T00:00:00", ParsingOption(forwardDate = true)) {
            assertThat(it.text).isEqualTo("5/31")
            assertThat(it.index).isEqualTo(0)

            with(it.start) {
                assertThat(year()).isEqualTo(2000)
                assertThat(month()).isEqualTo(5)
                assertThat(day()).isEqualTo(31)

                assertThat(certainDay()).isTrue()
                assertThat(certainMonth()).isTrue()
                assertThat(certainYear()).isFalse()

                assertDate("2000-05-31T12:00:00")
            }
        }

        testSingleCase(Krono.enCasual, "1/8 at 12pm", "2021-09-25T12:00:00", ParsingOption(forwardDate = true)) {
            assertThat(it.text).isEqualTo("1/8 at 12pm")
            assertThat(it.index).isEqualTo(0)

            with(it.start) {
                assertThat(year()).isEqualTo(2022)
                assertThat(month()).isEqualTo(1)
                assertThat(day()).isEqualTo(8)

                assertDate("2022-01-08T12:00:00")
            }
        }
    }
}
