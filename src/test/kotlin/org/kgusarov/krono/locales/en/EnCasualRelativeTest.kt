package org.kgusarov.krono.locales.en

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.Krono
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.locales.en.parsers.EnTimeUnitCasualRelativeFormatParser
import org.kgusarov.krono.testSingleCase
import org.kgusarov.krono.testUnexpectedResult

private const val REF_DATE = "2016-10-01T12:00:00"
private const val REF_DATE2 = "2012-06-10T12:14:00"

internal class EnCasualRelativeTest {
    @Test
    internal fun `next 2 weeks`() {
        testSingleCase(Krono.enCasual, "next 2 weeks", REF_DATE) {
            assertThat(it.text).isEqualTo("next 2 weeks")
            assertThat(it.tags()).contains("result/relativeDate")

            with (it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(10)
                assertThat(day()).isEqualTo(15)
                assertThat(tags()).contains("result/relativeDate")
            }
        }
    }

    @Test
    internal fun `next 2 days`() {
        testSingleCase(Krono.enCasual, "next 2 days", REF_DATE) {
            assertThat(it.text).isEqualTo("next 2 days")
            with (it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(10)
                assertThat(day()).isEqualTo(3)
                assertThat(hour()).isEqualTo(12)
            }
        }
    }

    @Test
    internal fun `next two years`() {
        testSingleCase(Krono.enCasual, "next two years", REF_DATE) {
            assertThat(it.text).isEqualTo("next two years")
            with (it.start) {
                assertThat(year()).isEqualTo(2018)
                assertThat(month()).isEqualTo(10)
                assertThat(day()).isEqualTo(1)
                assertThat(hour()).isEqualTo(12)
            }
        }
    }

    @Test
    internal fun `next 2 weeks 3 days`() {
        testSingleCase(Krono.enCasual, "next 2 weeks 3 days", REF_DATE) {
            assertThat(it.text).isEqualTo("next 2 weeks 3 days")
            with (it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(10)
                assertThat(day()).isEqualTo(18)
                assertThat(hour()).isEqualTo(12)
            }
        }
    }

    @Test
    internal fun `after a year`() {
        testSingleCase(Krono.enCasual, "after a year", REF_DATE) {
            assertThat(it.text).isEqualTo("after a year")
            with (it.start) {
                assertThat(year()).isEqualTo(2017)
                assertThat(month()).isEqualTo(10)
                assertThat(day()).isEqualTo(1)
                assertThat(hour()).isEqualTo(12)
            }
        }
    }

    @Test
    internal fun `after an hour`() {
        testSingleCase(Krono.enCasual, "after an hour", REF_DATE) {
            assertThat(it.text).isEqualTo("after an hour")
            assertThat(it.tags()).contains("result/relativeDate", "result/relativeDateAndTime")

            with (it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(10)
                assertThat(day()).isEqualTo(1)
                assertThat(hour()).isEqualTo(13)
                assertThat(it.tags()).contains("result/relativeDate", "result/relativeDateAndTime")
            }
        }
    }

    @Test
    internal fun `last 2 weeks`() {
        testSingleCase(Krono.enCasual, "last 2 weeks", REF_DATE) {
            assertThat(it.text).isEqualTo("last 2 weeks")
            with (it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(9)
                assertThat(day()).isEqualTo(17)
                assertThat(hour()).isEqualTo(12)
            }
        }
    }

    @Test
    internal fun `last two weeks`() {
        testSingleCase(Krono.enCasual, "last two weeks", REF_DATE) {
            assertThat(it.text).isEqualTo("last two weeks")
            with (it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(9)
                assertThat(day()).isEqualTo(17)
                assertThat(hour()).isEqualTo(12)
            }
        }
    }

    @Test
    internal fun `past 2 days`() {
        testSingleCase(Krono.enCasual, "past 2 days", REF_DATE) {
            assertThat(it.text).isEqualTo("past 2 days")
            with (it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(9)
                assertThat(day()).isEqualTo(29)
                assertThat(hour()).isEqualTo(12)
            }
        }
    }

    @Test
    internal fun `+2 months, 5 days`() {
        testSingleCase(Krono.enCasual, "+2 months, 5 days", REF_DATE) {
            assertThat(it.text).isEqualTo("+2 months, 5 days")
            with (it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(12)
                assertThat(day()).isEqualTo(6)
                assertThat(hour()).isEqualTo(12)
            }
        }
    }

    @Test
    internal fun `+15 minutes`() {
        testSingleCase(Krono.enCasual, "+15 minutes", REF_DATE2) {
            assertThat(it.text).isEqualTo("+15 minutes")
            with (it.start) {
                assertThat(hour()).isEqualTo(12)
                assertThat(minute()).isEqualTo(29)
                assertDate("2012-06-10T12:29:00")
            }
        }
    }

    @Test
    internal fun `+15min`() {
        testSingleCase(Krono.enCasual, "+15min", REF_DATE2) {
            assertThat(it.text).isEqualTo("+15min")
            with (it.start) {
                assertThat(hour()).isEqualTo(12)
                assertThat(minute()).isEqualTo(29)
                assertDate("2012-06-10T12:29:00")
            }
        }
    }

    @Test
    internal fun `+1 day 2 hour`() {
        testSingleCase(Krono.enCasual, "+1 day 2 hour", REF_DATE2) {
            assertThat(it.text).isEqualTo("+1 day 2 hour")
            with (it.start) {
                assertThat(day()).isEqualTo(11)
                assertThat(hour()).isEqualTo(14)
                assertThat(minute()).isEqualTo(14)
                assertDate("2012-06-11T14:14:00")
            }
        }
    }

    @Test
    internal fun `+1m`() {
        testSingleCase(Krono.enCasual, "+1m", REF_DATE2) {
            assertThat(it.text).isEqualTo("+1m")
            with (it.start) {
                assertThat(hour()).isEqualTo(12)
                assertThat(minute()).isEqualTo(15)
                assertDate("2012-06-10T12:15:00")
            }
        }
    }

    @Test
    internal fun `-3y`() {
        testSingleCase(Krono.enCasual, "-3y", "2015-07-10T12:14:00") {
            assertThat(it.text).isEqualTo("-3y")
            with (it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(7)
                assertThat(day()).isEqualTo(10)
                assertThat(hour()).isEqualTo(12)
                assertThat(minute()).isEqualTo(14)
                assertDate("2012-07-10T12:14:00")
            }
        }
    }

    @Test
    internal fun `-2hr5min`() {
        testSingleCase(Krono.enCasual, "-2hr5min", REF_DATE) {
            assertThat(it.text).isEqualTo("-2hr5min")
            with (it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(10)
                assertThat(day()).isEqualTo(1)
                assertThat(hour()).isEqualTo(9)
                assertThat(minute()).isEqualTo(55)
            }
        }
    }

    @Test
    internal fun `with custom parser without abbreviations`() {
        val custom = Krono(En.strict.copy().apply {
            parsers.add(EnTimeUnitCasualRelativeFormatParser(false))
        })

        testUnexpectedResult(custom, "-3y")
        testUnexpectedResult(custom, "last 2m")

        testSingleCase(custom, "-2 hours 5 minutes", REF_DATE) {
            assertThat(it.text).isEqualTo("-2 hours 5 minutes")
            with (it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(10)
                assertThat(day()).isEqualTo(1)
                assertThat(hour()).isEqualTo(9)
                assertThat(minute()).isEqualTo(55)
            }
        }
    }

    @Test
    internal fun `negative cases`() {
        testUnexpectedResult(Krono.enCasual, "3y")
        testUnexpectedResult(Krono.enCasual, "1 m")
        testUnexpectedResult(Krono.enCasual, "the day")
        testUnexpectedResult(Krono.enCasual, "a day")
        testUnexpectedResult(Krono.enCasual, "+am");
        testUnexpectedResult(Krono.enCasual, "+them");
    }
}
