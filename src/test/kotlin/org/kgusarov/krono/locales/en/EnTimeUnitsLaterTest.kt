package org.kgusarov.krono.locales.en

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.Krono
import org.kgusarov.krono.KronoMeridiem
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase
import org.kgusarov.krono.testUnexpectedResult
import org.kgusarov.krono.testWithExpectedDate

internal class EnTimeUnitsLaterTest {
    @Test
    fun `2 days later`() {
        testSingleCase(Krono.enCasual, "2 days later", "2012-08-10T12:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("2 days later")

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(12)

                assertThat(certainDay()).isTrue
                assertThat(certainMonth()).isTrue

                assertDate("2012-08-12T12:00:00")
            }
        }
    }

    @Test
    fun `5 minutes later`() {
        testSingleCase(Krono.enCasual, "5 minutes later", "2012-08-10T10:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("5 minutes later")

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(10)
                assertThat(hour()).isEqualTo(10)
                assertThat(minute()).isEqualTo(5)

                assertThat(certainHour()).isTrue
                assertThat(certainMinute()).isTrue

                assertDate("2012-08-10T10:05:00")
            }
        }
    }

    @Test
    fun `3 week later`() {
        testSingleCase(Krono.enCasual, "3 week later", "2012-07-10T10:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("3 week later")

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(7)
                assertThat(day()).isEqualTo(31)

                assertDate("2012-07-31T10:00:00")
            }
        }
    }

    @Test
    fun `3w later`() {
        testSingleCase(Krono.enCasual, "3w later", "2012-07-10T10:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("3w later")

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(7)
                assertThat(day()).isEqualTo(31)

                assertDate("2012-07-31T10:00:00")
            }
        }
    }

    @Test
    fun `3mo later`() {
        testSingleCase(Krono.enCasual, "3mo later", "2012-07-10T10:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("3mo later")

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(10)
                assertThat(day()).isEqualTo(10)

                assertDate("2012-10-10T10:00:00")
            }
        }
    }

    @Test
    fun `5 days from now, we did something`() {
        testSingleCase(Krono.enCasual, "5 days from now, we did something", "2012-08-10T12:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("5 days from now")

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(15)

                assertDate("2012-08-15T12:00:00")
            }
        }
    }

    @Test
    fun `10 days from now, we did something`() {
        testSingleCase(Krono.enCasual, "10 days from now, we did something", "2012-08-10T12:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("10 days from now")

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(20)

                assertDate("2012-08-20T12:00:00")
            }
        }
    }

    @Test
    fun `15 minute from now`() {
        testSingleCase(Krono.enCasual, "15 minute from now", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(10)
                assertThat(hour()).isEqualTo(12)
                assertThat(minute()).isEqualTo(29)
                assertThat(meridiem()).isEqualTo(KronoMeridiem.PM)

                assertDate("2012-08-10T12:29:00")
            }
        }
    }

    @Test
    fun `15 minutes earlier`() {
        testSingleCase(Krono.enCasual, "15 minutes earlier", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(10)
                assertThat(hour()).isEqualTo(11)
                assertThat(minute()).isEqualTo(59)
                assertThat(meridiem()).isEqualTo(KronoMeridiem.AM)

                assertDate("2012-08-10T11:59:00")
            }
        }
    }

    @Test
    fun `15 minute out`() {
        testSingleCase(Krono.enCasual, "15 minute out", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(10)
                assertThat(hour()).isEqualTo(12)
                assertThat(minute()).isEqualTo(29)
                assertThat(meridiem()).isEqualTo(KronoMeridiem.PM)

                assertDate("2012-08-10T12:29:00")
            }
        }
    }

    @Test
    fun `12 hours from now`() {
        testSingleCase(Krono.enCasual, "   12 hours from now", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(3)
            assertThat(it.text).isEqualTo("12 hours from now")

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(11)
                assertThat(hour()).isEqualTo(0)
                assertThat(minute()).isEqualTo(14)

                assertDate("2012-08-11T00:14:00")
            }
        }
    }

    @Test
    fun `12 hrs from now`() {
        testSingleCase(Krono.enCasual, "   12 hrs from now", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(3)
            assertThat(it.text).isEqualTo("12 hrs from now")

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(11)
                assertThat(hour()).isEqualTo(0)
                assertThat(minute()).isEqualTo(14)
                assertThat(meridiem()).isEqualTo(KronoMeridiem.AM)

                assertDate("2012-08-11T00:14:00")
            }
        }
    }

    @Test
    fun `half an hour from now`() {
        testSingleCase(Krono.enCasual, "   half an hour from now", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(3)
            assertThat(it.text).isEqualTo("half an hour from now")

            with(it.start) {
                assertThat(hour()).isEqualTo(12)
                assertThat(minute()).isEqualTo(44)
                assertThat(meridiem()).isEqualTo(KronoMeridiem.PM)

                assertDate("2012-08-10T12:44:00")
            }
        }
    }

    @Test
    fun `12 hours from now I did something`() {
        testSingleCase(Krono.enCasual, "12 hours from now I did something", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("12 hours from now")

            with(it.start) {
                assertThat(day()).isEqualTo(11)
                assertThat(hour()).isEqualTo(0)
                assertThat(minute()).isEqualTo(14)

                assertDate("2012-08-11T00:14:00")
            }
        }
    }

    @Test
    fun `12 seconds from now I did something`() {
        testSingleCase(Krono.enCasual, "12 seconds from now I did something", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("12 seconds from now")

            with(it.start) {
                assertThat(hour()).isEqualTo(12)
                assertThat(minute()).isEqualTo(14)
                assertThat(second()).isEqualTo(12)

                assertDate("2012-08-10T12:14:12")
            }
        }
    }

    @Test
    fun `three seconds from now I did something`() {
        testSingleCase(Krono.enCasual, "three seconds from now I did something", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("three seconds from now")

            with(it.start) {
                assertThat(hour()).isEqualTo(12)
                assertThat(minute()).isEqualTo(14)
                assertThat(second()).isEqualTo(3)

                assertDate("2012-08-10T12:14:03")
            }
        }
    }

    @Test
    fun `5 Days from now, we did something`() {
        testSingleCase(Krono.enCasual, "5 Days from now, we did something", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("5 Days from now")

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(15)

                assertDate("2012-08-15T12:14:00")
            }
        }
    }

    @Test
    fun `half An hour from now`() {
        testSingleCase(Krono.enCasual, "   half An hour from now", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(3)

            with(it.start) {
                assertThat(hour()).isEqualTo(12)
                assertThat(minute()).isEqualTo(44)

                assertDate("2012-08-10T12:44:00")
            }
        }
    }

    @Test
    fun `A days from now, we did something`() {
        testSingleCase(Krono.enCasual, "A days from now, we did something", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("A days from now")

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(11)

                assertDate("2012-08-11T12:14:00")
            }
        }
    }

    @Test
    fun `a min out`() {
        testSingleCase(Krono.enCasual, "a min out", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("a min out")

            with(it.start) {
                assertThat(hour()).isEqualTo(12)
                assertThat(minute()).isEqualTo(15)

                assertDate("2012-08-10T12:15:00")
            }
        }
    }

    @Test
    fun `in 1 hour`() {
        testSingleCase(Krono.enCasual, "in 1 hour", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("in 1 hour")

            with(it.start) {
                assertThat(hour()).isEqualTo(13)
                assertThat(minute()).isEqualTo(14)

                assertDate("2012-08-10T13:14:00")
            }
        }
    }

    @Test
    fun `in 1 mon`() {
        testSingleCase(Krono.enCasual, "in 1 mon", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("in 1 mon")

            with(it.start) {
                assertThat(month()).isEqualTo(9)

                assertDate("2012-09-10T12:14:00")
            }
        }
    }

    @Test
    fun `in 1 point 5 hours`() {
        testSingleCase(Krono.enCasual, "in 1.5 hours", "2012-08-10T12:40:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("in 1.5 hours")

            with(it.start) {
                assertThat(hour()).isEqualTo(14)
                assertThat(minute()).isEqualTo(10)

                assertDate("2012-08-10T14:10:00")
            }
        }
    }

    @Test
    fun `in 1d 2hr 5min`() {
        testSingleCase(Krono.enCasual, "in 1d 2hr 5min", "2012-08-10T12:40:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("in 1d 2hr 5min")

            with(it.start) {
                assertThat(day()).isEqualTo(11)
                assertThat(hour()).isEqualTo(14)
                assertThat(minute()).isEqualTo(45)

                assertDate("2012-08-11T14:45:00")
            }
        }

        testSingleCase(Krono.enCasual, "in 1d, 2hr, and 5min", "2012-08-10T12:40:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("in 1d, 2hr, and 5min")

            with(it.start) {
                assertThat(day()).isEqualTo(11)
                assertThat(hour()).isEqualTo(14)
                assertThat(minute()).isEqualTo(45)

                assertDate("2012-08-11T14:45:00")
            }
        }
    }

    @Test
    fun `the min after`() {
        testSingleCase(Krono.enCasual, "the min after", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("the min after")

            with(it.start) {
                assertThat(hour()).isEqualTo(12)
                assertThat(minute()).isEqualTo(15)
                assertThat(meridiem()).isEqualTo(KronoMeridiem.PM)

                assertDate("2012-08-10T12:15:00")
            }
        }
    }

    @Test
    fun `15 minutes from now`() {
        testSingleCase(Krono.enStrict, "15 minutes from now", "2012-08-10T12:14:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("15 minutes from now")

            with(it.start) {
                assertThat(hour()).isEqualTo(12)
                assertThat(minute()).isEqualTo(29)

                assertDate("2012-08-10T12:29:00")
            }
        }
    }

    @Test
    fun `25 minutes later`() {
        testSingleCase(Krono.enStrict, "25 minutes later", "2012-08-10T12:40:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("25 minutes later")

            with(it.start) {
                assertThat(hour()).isEqualTo(13)
                assertThat(minute()).isEqualTo(5)

                assertDate("2012-08-10T13:05:00")
            }
        }
    }

    @Test
    fun `strict unexpected`() {
        testUnexpectedResult(Krono.enStrict, "15m from now")
        testUnexpectedResult(Krono.enStrict, "15s later")
    }

    @Test
    fun `after with reference`() {
        testWithExpectedDate(
            Krono.enCasual, "2 day after today", "2012-08-10T12:00:00", "2012-08-12T12:00:00"
        )

        testWithExpectedDate(
            Krono.enCasual, "the day after tomorrow", "2012-08-10T12:00:00", "2012-08-12T12:00:00"
        )

        testWithExpectedDate(
            Krono.enCasual, "2 day after tomorrow", "2012-08-10T12:00:00", "2012-08-13T12:00:00"
        )

        testWithExpectedDate(
            Krono.enCasual, "a week after tomorrow", "2012-08-10T12:00:00", "2012-08-18T12:00:00"
        )
    }

    @Test
    fun `plus after reference`() {
        testWithExpectedDate(
            Krono.enCasual, "next tuesday +10 days", "2023-12-29T12:00:00", "2024-01-12T12:00:00"
        )

        testWithExpectedDate(
            Krono.enCasual, "2023-12-29 -10days", "2023-12-29T12:00:00", "2023-12-19T12:00:00"
        )

        testWithExpectedDate(
            Krono.enCasual, "now + 40minutes", "2023-12-29T08:30:00", "2023-12-29T09:10:00"
        )
    }
}
