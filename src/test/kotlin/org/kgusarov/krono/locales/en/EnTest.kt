package org.kgusarov.krono.locales.en

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.Krono
import org.kgusarov.krono.KronoConfiguration
import org.kgusarov.krono.KronoMeridiem
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.locales.en.parsers.EnTimeExpressionParser
import org.kgusarov.krono.testMultipleResults
import org.kgusarov.krono.testSingleCase
import org.kgusarov.krono.testUnexpectedResult

private const val REF_DATE_2020_07_06 = "2020-07-06T00:00:00"

private const val REF_DATE_2017_07_07 = "2017-07-07T00:00:00"

private const val REF_DATE_2012_07_10 = "2017-07-10T00:00:00"

internal class EnTest {
    @Test
    internal fun `date and time expression`() {
        testSingleCase(Krono.enStrict, "Something happen on 2014-04-18 13:00 - 16:00 as") {
            assertThat(it.text).isEqualTo("2014-04-18 13:00 - 16:00")

            with(it.start) {
                assertDate("2014-04-18T13:00:00")
            }

            with(it.end!!) {
                assertDate("2014-04-18T16:00:00")
            }
        }
    }

    @Test
    internal fun `time expression between`() {
        testSingleCase(Krono.enStrict, "between 3:30-4:30pm", REF_DATE_2020_07_06) {
            assertThat(it.text).isEqualTo("3:30-4:30pm")

            with(it.start) {
                assertDate("2020-07-06T15:30:00")
            }

            with(it.end!!) {
                assertDate("2020-07-06T16:30:00")
            }
        }
    }

    @Test
    internal fun `time expression PST`() {
        testSingleCase(Krono.enStrict, "9:00 PST", REF_DATE_2020_07_06) {
            assertThat(it.text).isEqualTo("9:00 PST")

            with(it.start) {
                assertThat(hour()).isEqualTo(9)
                assertThat(minute()).isEqualTo(0)
                assertThat(offsetMinutes()).isEqualTo(-480)
            }
        }
    }

    @Test
    internal fun `quoted expression 1`() {
        testSingleCase(Krono.enStrict, "Want to meet for dinner (5pm EST)?", REF_DATE_2020_07_06) {
            assertThat(it.text).contains("5pm EST")
        }
    }

    @Test
    internal fun `quoted expression 2`() {
        testSingleCase(Krono.enStrict, "between '3:30-4:30pm'", REF_DATE_2020_07_06) {
            assertThat(it.text).isEqualTo("3:30-4:30pm")

            with(it.start) {
                assertDate("2020-07-06T15:30:00")
            }

            with(it.end!!) {
                assertDate("2020-07-06T16:30:00")
            }
        }
    }

    @Test
    internal fun `quoted expression 3`() {
        testSingleCase(Krono.enStrict, "The date is '2014-04-18'") {
            assertThat(it.text).isEqualTo("2014-04-18")

            with(it.start) {
                assertDate("2014-04-18T12:00:00")
            }
        }
    }

    @Test
    internal fun `strict mode - only weekday`() {
        testUnexpectedResult(Krono.enStrict, "Tuesday")
    }

    @Test
    internal fun `built-in English variants`() {
        testSingleCase(Krono.enStrict, "6/10/2018") {
            with(it.start) {
                assertDate("2018-06-10T12:00:00")
            }
        }

        testSingleCase(Krono.enGb, "6/10/2018") {
            with(it.start) {
                assertDate("2018-10-06T12:00:00")
            }
        }
    }

    @Test
    internal fun `random text`() {
        testSingleCase(Krono.enCasual, "Adam <Adam@supercalendar.com> написал(а):\nThe date is 02.07.2013") {
            assertThat(it.text).isEqualTo("02.07.2013")
        }

        testSingleCase(Krono.enCasual, "174 November 1,2001- March 31,2002") {
            assertThat(it.text).isEqualTo("November 1,2001- March 31,2002")
        }

        testSingleCase(Krono.enCasual, "...Thursday, December 15, 2011 Best Available Rate ") {
            assertThat(it.text).isEqualTo("Thursday, December 15, 2011")
            with(it.start) {
                assertDate("2011-12-15T12:00:00")
            }
        }

        testSingleCase(Krono.enCasual, "SUN 15SEP 11:05 AM - 12:50 PM") {
            assertThat(it.text).isEqualTo("SUN 15SEP 11:05 AM - 12:50 PM")

            with(it.end!!) {
                assertThat(hour()).isEqualTo(12)
                assertThat(minute()).isEqualTo(50)
            }
        }

        testSingleCase(Krono.enCasual, "FRI 13SEP 1:29 PM - FRI 13SEP 3:29 PM") {
            assertThat(it.text).isEqualTo("FRI 13SEP 1:29 PM - FRI 13SEP 3:29 PM")

            with(it.start) {
                assertThat(day()).isEqualTo(13)
                assertThat(hour()).isEqualTo(13)
                assertThat(minute()).isEqualTo(29)
            }

            with(it.end!!) {
                assertThat(day()).isEqualTo(13)
                assertThat(hour()).isEqualTo(15)
                assertThat(minute()).isEqualTo(29)
            }
        }

        testSingleCase(Krono.enCasual, "9:00 AM to 5:00 PM, Tuesday, 20 May 2013") {
            assertThat(it.text).isEqualTo("9:00 AM to 5:00 PM, Tuesday, 20 May 2013")

            with(it.start) {
                assertDate("2013-05-20T09:00:00")
            }

            with(it.end!!) {
                assertDate("2013-05-20T17:00:00")
            }
        }

        testSingleCase(Krono.enCasual, "Monday afternoon to last night", REF_DATE_2017_07_07) {
            assertThat(it.text).isEqualTo("Monday afternoon to last night")

            with(it.start) {
                assertThat(day()).isEqualTo(3)
                assertThat(month()).isEqualTo(7)
            }

            with(it.end!!) {
                assertThat(day()).isEqualTo(7)
                assertThat(month()).isEqualTo(7)
            }
        }

        testSingleCase(Krono.enCasual, "07-27-2022, 02:00 AM", REF_DATE_2017_07_07) {
            assertThat(it.text).isEqualTo("07-27-2022, 02:00 AM")

            with(it.start) {
                assertThat(day()).isEqualTo(27)
                assertThat(month()).isEqualTo(7)
                assertThat(year()).isEqualTo(2022)
                assertThat(hour()).isEqualTo(2)
                assertThat(meridiem()).isEqualTo(KronoMeridiem.AM)
            }
        }
    }

    @Test
    internal fun `wikipedia text`() {
        val testText =
            "October 7, 2011, of which details were not revealed out of respect to Jobs's family.[239] " +
                "Apple announced on the same day that they had no plans for a public service, but were encouraging " +
                "\"well-wishers\" to send their remembrance messages to an email address created to receive such messages.[240] " +
                "Sunday, October 16, 2011"

        testMultipleResults(Krono.enCasual, testText, REF_DATE_2012_07_10) {
            assertThat(it).hasSize(2)

            with(it[0]) {
                assertThat(start.year()).isEqualTo(2011)
                assertThat(start.month()).isEqualTo(10)
                assertThat(start.day()).isEqualTo(7)
                assertThat(index).isEqualTo(0)
                assertThat(text).isEqualTo("October 7, 2011")
            }

            with(it[1]) {
                assertThat(start.year()).isEqualTo(2011)
                assertThat(start.month()).isEqualTo(10)
                assertThat(start.day()).isEqualTo(16)
                assertThat(index).isEqualTo(297)
                assertThat(text).isEqualTo("Sunday, October 16, 2011")
            }
        }
    }

    @Test
    internal fun `parse multiple date results`() {
        val testText = "I will see you at 2:30. If not I will see you somewhere between 3:30-4:30pm"

        testMultipleResults(Krono.enCasual, testText, REF_DATE_2020_07_06) {
            assertThat(it).hasSize(2)

            with(it[0]) {
                assertThat(text).isEqualTo("at 2:30")
                assertThat(start.year()).isEqualTo(2020)
                assertThat(start.month()).isEqualTo(7)
                assertThat(start.day()).isEqualTo(6)
                assertThat(start.hour()).isEqualTo(2)
                assertThat(end).isNull()
            }

            with(it[1]) {
                assertThat(text).isEqualTo("3:30-4:30pm")
                assertThat(start.hour()).isEqualTo(15)
                assertThat(start.minute()).isEqualTo(30)
                assertThat(end!!.hour()).isEqualTo(16)
                assertThat(end!!.minute()).isEqualTo(30)
            }
        }
    }

    @Test
    internal fun `customize by removing time extraction`() {
        val custom = Krono(
            KronoConfiguration(
                En.casual.parsers.filter {
                    p -> p !is EnTimeExpressionParser
                }.toMutableList(),
                En.casual.refiners.toMutableList(),
            )
        )

        testSingleCase(custom, "Thursday 9AM", "2020-11-29T00:00:00") {
            assertThat(it.text).isEqualTo("Thursday")
            assertThat(it.start.year()).isEqualTo(2020)
            assertThat(it.start.month()).isEqualTo(11)
            assertThat(it.start.day()).isEqualTo(26)
        }
    }
}
