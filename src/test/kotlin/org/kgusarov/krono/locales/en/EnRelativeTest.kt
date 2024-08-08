package org.kgusarov.krono.locales.en

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.Krono
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.ReferenceWithTimezone
import org.kgusarov.krono.TimezoneAbbreviations
import org.kgusarov.krono.assertOffsetDate
import org.kgusarov.krono.testSingleCase
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

private const val REF_DATE_2017_11_19 = "2017-11-19T12:00:00"
private const val REF_DATE_2017_11_01 = "2017-11-01T12:00:00"
private const val REF_DATE_2016_10_01 = "2016-10-01T12:00:00"
private val JST_REF =
    run {
        val zdt = ZonedDateTime.of(LocalDateTime.parse("2020-11-29T13:24:13"), ZoneId.of("Japan"))
        ReferenceWithTimezone(zdt.toLocalDateTime(), zdt.zone)
    }


internal class EnRelativeTest {
    @Test
    fun `this expressions`() {
        testSingleCase(Krono.enCasual, "this week", REF_DATE_2017_11_19) {
            assertThat(it.text).isEqualTo("this week")
            with (it.start) {
                assertThat(year()).isEqualTo(2017)
                assertThat(month()).isEqualTo(11)
                assertThat(day()).isEqualTo(19)
                assertThat(hour()).isEqualTo(12)
            }
        }

        testSingleCase(Krono.enCasual, "this month", REF_DATE_2017_11_19) {
            assertThat(it.text).isEqualTo("this month")
            with (it.start) {
                assertThat(year()).isEqualTo(2017)
                assertThat(month()).isEqualTo(11)
                assertThat(day()).isEqualTo(1)
                assertThat(hour()).isEqualTo(12)
            }
        }

        testSingleCase(Krono.enCasual, "this month", REF_DATE_2017_11_01) {
            assertThat(it.text).isEqualTo("this month")
            with (it.start) {
                assertThat(year()).isEqualTo(2017)
                assertThat(month()).isEqualTo(11)
                assertThat(day()).isEqualTo(1)
                assertThat(hour()).isEqualTo(12)
            }
        }

        testSingleCase(Krono.enCasual, "this year", REF_DATE_2017_11_19) {
            assertThat(it.text).isEqualTo("this year")
            with (it.start) {
                assertThat(year()).isEqualTo(2017)
                assertThat(month()).isEqualTo(1)
                assertThat(day()).isEqualTo(1)
                assertThat(hour()).isEqualTo(12)
            }
        }
    }

    @Test
    fun `past relative expressions`() {
        testSingleCase(Krono.enCasual, "last week", REF_DATE_2016_10_01) {
            assertThat(it.text).isEqualTo("last week")
            with (it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(9)
                assertThat(day()).isEqualTo(24)
                assertThat(hour()).isEqualTo(12)
            }
        }

        testSingleCase(Krono.enCasual, "lastmonth", REF_DATE_2016_10_01) {
            assertThat(it.text).isEqualTo("lastmonth")
            with (it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(9)
                assertThat(day()).isEqualTo(1)
                assertThat(hour()).isEqualTo(12)
            }
        }

        testSingleCase(Krono.enCasual, "last day", REF_DATE_2016_10_01) {
            assertThat(it.text).isEqualTo("last day")
            with (it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(9)
                assertThat(day()).isEqualTo(30)
                assertThat(hour()).isEqualTo(12)
            }
        }

        testSingleCase(Krono.enCasual, "last month", REF_DATE_2016_10_01) {
            assertThat(it.text).isEqualTo("last month")
            with (it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(9)
                assertThat(day()).isEqualTo(1)
                assertThat(hour()).isEqualTo(12)
            }
        }

        testSingleCase(Krono.enCasual, "past week", REF_DATE_2016_10_01) {
            assertThat(it.text).isEqualTo("past week")
            with (it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(9)
                assertThat(day()).isEqualTo(24)
                assertThat(hour()).isEqualTo(12)
            }
        }
    }

    @Test
    fun `next hour`() {
        testSingleCase(Krono.enCasual, "next hour", REF_DATE_2016_10_01) {
            assertThat(it.text).isEqualTo("next hour")
            with (it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(10)
                assertThat(day()).isEqualTo(1)
                assertThat(hour()).isEqualTo(13)
            }
        }
    }

    @Test
    fun `next week`() {
        testSingleCase(Krono.enCasual, "next week", REF_DATE_2016_10_01) {
            assertThat(it.text).isEqualTo("next week")
            with (it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(10)
                assertThat(day()).isEqualTo(8)
                assertThat(hour()).isEqualTo(12)
            }
        }
    }

    @Test
    fun `next day`() {
        testSingleCase(Krono.enCasual, "next day", REF_DATE_2016_10_01) {
            assertThat(it.text).isEqualTo("next day")
            with (it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(10)
                assertThat(day()).isEqualTo(2)
                assertThat(hour()).isEqualTo(12)
            }
        }
    }

    @Test
    fun `next month`() {
        testSingleCase(Krono.enCasual, "next month", REF_DATE_2016_10_01) {
            assertThat(it.text).isEqualTo("next month")
            with (it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(11)
                assertThat(day()).isEqualTo(1)
                assertThat(hour()).isEqualTo(12)

                assertThat(certainYear()).isTrue()
                assertThat(certainMonth()).isTrue()
                assertThat(certainDay()).isFalse()
                assertThat(certainHour()).isFalse()
            }
        }
    }

    @Test
    fun `next year`() {
        testSingleCase(Krono.enCasual, "next year", "2020-11-22T12:11:32.006") {
            assertThat(it.text).isEqualTo("next year")
            with (it.start) {
                assertThat(year()).isEqualTo(2021)
                assertThat(month()).isEqualTo(11)
                assertThat(day()).isEqualTo(22)
                assertThat(hour()).isEqualTo(12)
                assertThat(minute()).isEqualTo(11)
                assertThat(second()).isEqualTo(32)
                assertThat(millisecond()).isEqualTo(6)

                assertThat(certainYear()).isTrue()
                assertThat(certainMonth()).isFalse()
                assertThat(certainDay()).isFalse()
                assertThat(certainHour()).isFalse()
                assertThat(certainMinute()).isFalse()
                assertThat(certainSecond()).isFalse()
                assertThat(certainMillisecond()).isFalse()
                assertThat(certainOffset()).isFalse()
            }
        }
    }

    @Test
    fun `next quarter`() {
        testSingleCase(Krono.enCasual, "next quarter", "2021-01-22T12:00:00") {
            assertThat(it.text).isEqualTo("next quarter")
            with (it.start) {
                assertThat(year()).isEqualTo(2021)
                assertThat(month()).isEqualTo(4)
                assertThat(day()).isEqualTo(22)
                assertThat(hour()).isEqualTo(12)

                assertThat(certainYear()).isFalse()
                assertThat(certainMonth()).isFalse()
                assertThat(certainDay()).isFalse()
                assertThat(certainHour()).isFalse()
            }
        }
    }

    @Test
    fun `next qtr`() {
        testSingleCase(Krono.enCasual, "next qtr", "2021-10-22T12:00:00") {
            assertThat(it.text).isEqualTo("next qtr")
            with (it.start) {
                assertThat(year()).isEqualTo(2022)
                assertThat(month()).isEqualTo(1)
                assertThat(day()).isEqualTo(22)
                assertThat(hour()).isEqualTo(12)

                assertThat(certainYear()).isFalse()
                assertThat(certainMonth()).isFalse()
                assertThat(certainDay()).isFalse()
                assertThat(certainHour()).isFalse()
            }
        }
    }

    @Test
    fun `next two quarter`() {
        testSingleCase(Krono.enCasual, "next two quarter", "2021-01-22T12:00:00") {
            assertThat(it.text).isEqualTo("next two quarter")
            with (it.start) {
                assertThat(year()).isEqualTo(2021)
                assertThat(month()).isEqualTo(7)
                assertThat(day()).isEqualTo(22)
                assertThat(hour()).isEqualTo(12)

                assertThat(certainYear()).isFalse()
                assertThat(certainMonth()).isFalse()
                assertThat(certainDay()).isFalse()
                assertThat(certainHour()).isFalse()
            }
        }
    }

    @Test
    fun `after this year`() {
        testSingleCase(Krono.enCasual, "after this year", "2020-11-22T12:11:32.006") {
            assertThat(it.text).isEqualTo("after this year")
            with (it.start) {
                assertThat(year()).isEqualTo(2021)
                assertThat(month()).isEqualTo(11)
                assertThat(day()).isEqualTo(22)
                assertThat(hour()).isEqualTo(12)
                assertThat(minute()).isEqualTo(11)
                assertThat(second()).isEqualTo(32)
                assertThat(millisecond()).isEqualTo(6)

                assertThat(certainYear()).isTrue()
                assertThat(certainMonth()).isFalse()
                assertThat(certainDay()).isFalse()
                assertThat(certainHour()).isFalse()
                assertThat(certainMinute()).isFalse()
                assertThat(certainSecond()).isFalse()
                assertThat(certainMillisecond()).isFalse()
                assertThat(certainOffset()).isFalse()
            }
        }
    }

    @Test
    fun `connect back after this year`() {
        testSingleCase(Krono.enCasual, "connect back after this year", "2022-04-16T12:00:00") {
            with (it.start) {
                assertThat(year()).isEqualTo(2023)
                assertThat(month()).isEqualTo(4)
                assertThat(day()).isEqualTo(16)
            }
        }
    }

    @Test
    fun `next certainty`() {
        val refDate = ZonedDateTime.of(LocalDateTime.parse("2016-10-07T12:00:00"), ZoneId.of("Europe/Riga"))
        val reference = ReferenceWithTimezone(refDate.toLocalDateTime(), refDate.zone)
        val offset = refDate.zone.rules.getOffset(refDate.toLocalDateTime()).totalSeconds

        testSingleCase(Krono.enCasual, "next hour", reference) {
            assertThat(it.text).isEqualTo("next hour")

            with (it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(10)
                assertThat(day()).isEqualTo(7)
                assertThat(hour()).isEqualTo(13)
                assertThat(offset()).isEqualTo(offset)

                assertThat(certainYear()).isTrue()
                assertThat(certainMonth()).isTrue()
                assertThat(certainDay()).isTrue()
                assertThat(certainHour()).isTrue()
                assertThat(certainOffset()).isTrue()
            }
        }

        testSingleCase(Krono.enCasual, "next month", reference) {
            assertThat(it.text).isEqualTo("next month")

            with (it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(11)
                assertThat(day()).isEqualTo(7)
                assertThat(hour()).isEqualTo(12)
                assertThat(offset()).isEqualTo(offset)

                assertThat(certainYear()).isTrue()
                assertThat(certainMonth()).isTrue()
                assertThat(certainDay()).isFalse()
                assertThat(certainHour()).isFalse()
                assertThat(certainOffset()).isFalse()
            }
        }
    }

    @Test
    fun `JST ref - now`() {
        testSingleCase(Krono.enCasual, "now", JST_REF) {
            assertThat(it.text).isEqualTo("now")

            with (it.start) {
                imply(KronoComponents.Offset, 3600)

                assertOffsetDate("2020-11-29T13:24:13+09:00")
                assertOffsetDate("2020-11-29T05:24:13+01:00")
            }
        }
    }

    @Test
    fun `JST ref - tomorrow at 5pm`() {
        testSingleCase(Krono.enCasual, "tomorrow at 5pm", JST_REF) {
            assertThat(it.text).isEqualTo("tomorrow at 5pm")

            with (it.start) {
                imply(KronoComponents.Offset, 3600)

                assertOffsetDate("2020-12-01T01:00:00+09:00")
                assertOffsetDate("2020-11-30T17:00:00+01:00")
            }
        }
    }

    @Test
    fun `JST ref - in 10 minutes`() {
        testSingleCase(Krono.enCasual, "in 10 minutes", JST_REF) {
            assertThat(it.text).isEqualTo("in 10 minutes")

            with (it.start) {
                imply(KronoComponents.Offset, 3600)

                assertOffsetDate("2020-11-29T13:34:13+09:00")
                assertOffsetDate("2020-11-29T05:34:13+01:00")
            }
        }
    }

    @Test
    fun `BST ref - in 10 minutes`() {
        val bst = TimezoneAbbreviations.TIMEZONE_ABBR_MAP["BST"]
        val ref = ReferenceWithTimezone(
            JST_REF.instant.atZone(JST_REF.timezone).withZoneSameInstant(bst).toLocalDateTime(),
            bst,
        )

        testSingleCase(Krono.enCasual, "in 10 minutes", ref) {
            assertThat(it.text).isEqualTo("in 10 minutes")

            with (it.start) {
                imply(KronoComponents.Offset, 3600)

                assertOffsetDate("2020-11-29T13:34:13+09:00")
                assertOffsetDate("2020-11-29T05:34:13+01:00")
            }
        }
    }

    @Test
    fun `JST ref with no zone`() {
        testSingleCase(Krono.enCasual, "in 20 minutes", ReferenceWithTimezone(JST_REF.instant)) {
            assertThat(it.text).isEqualTo("in 20 minutes")

            with (it.start) {
                assertThat(certainOffset()).isFalse()
            }
        }
    }
}

/*
test("Test - Relative date components' certainty and imply timezone", () => {
    const refDate = new Date("Sun Nov 29 2020 13:24:13 GMT+0900 (Japan Standard Time)");

    {
        const text = "in 10 minutes";
        const result = chrono.parse(text, { instant: refDate, timezone: "BST" })[0] as ParsingResult;

        expect(result.text).toBe(text);
        result.start.imply("timezoneOffset", 60);

        expect(result).toBeDate(new Date("Sun Nov 29 2020 13:34:13 GMT+0900 (Japan Standard Time)"));
        expect(result).toBeDate(new Date("Sun Nov 29 2020 5:34:13 GMT+0100"));
    }

    {
        const text = "in 10 minutes";
        const result = chrono.parse(text, { instant: refDate, timezone: "JST" })[0] as ParsingResult;

        expect(result.text).toBe(text);
        result.start.imply("timezoneOffset", 60);

        expect(result).toBeDate(new Date("Sun Nov 29 2020 13:34:13 GMT+0900 (Japan Standard Time)"));
        expect(result).toBeDate(new Date("Sun Nov 29 2020 5:34:13 GMT+0100"));
    }

    {
        const text = "in 20 minutes";
        const result = chrono.parse(text, { instant: refDate, timezone: null })[0] as ParsingResult;

        expect(result.text).toBe(text);
        expect(result.start.isCertain("timezoneOffset")).toBe(false);
    }
});

 */