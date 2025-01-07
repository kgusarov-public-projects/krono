package org.kgusarov.krono.locales.en

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.Krono
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.assertOffsetDate
import org.kgusarov.krono.testSingleCase

private const val REF_DATE = "2022-08-08T00:00:00"

internal class EnInterStdTest {
    @Test
    internal fun `before this 2013-2-7`() {
        testSingleCase(Krono.enStrict, "Let's finish this before this 2013-2-7.", REF_DATE) {
            with (it.start) {
                assertThat(year()).isEqualTo(2013)
                assertThat(month()).isEqualTo(2)
                assertThat(day()).isEqualTo(7)

                assertDate("2013-02-07T12:00:00")
            }
        }
    }

    @Test
    internal fun `ISO with timezone offset`() {
        testSingleCase(Krono.enStrict, "1994-11-05T08:15:30-05:30", REF_DATE) {
            assertThat(it.text).isEqualTo("1994-11-05T08:15:30-05:30")

            with (it.start) {
                assertThat(year()).isEqualTo(1994)
                assertThat(month()).isEqualTo(11)
                assertThat(day()).isEqualTo(5)
                assertThat(hour()).isEqualTo(8)
                assertThat(minute()).isEqualTo(15)
                assertThat(second()).isEqualTo(30)
                assertThat(offsetMinutes()).isEqualTo(-330)

                assertOffsetDate("1994-11-05T08:15:30-05:30")
            }
        }
    }

    @Test
    internal fun `Zoned ISO`() {
        testSingleCase(Krono.enStrict, "1994-11-05T13:15:30Z", REF_DATE) {
            assertThat(it.text).isEqualTo("1994-11-05T13:15:30Z")

            with (it.start) {
                assertThat(year()).isEqualTo(1994)
                assertThat(month()).isEqualTo(11)
                assertThat(day()).isEqualTo(5)
                assertThat(hour()).isEqualTo(13)
                assertThat(minute()).isEqualTo(15)
                assertThat(second()).isEqualTo(30)
                assertThat(offsetMinutes()).isEqualTo(0)

                assertOffsetDate("1994-11-05T13:15:30Z")
            }
        }
    }

    @Test
    internal fun `ISO with millis`() {
        testSingleCase(Krono.enStrict, "2016-05-07T23:45:00.487+01:00", REF_DATE) {
            assertThat(it.text).isEqualTo("2016-05-07T23:45:00.487+01:00")

            with (it.start) {
                assertThat(year()).isEqualTo(2016)
                assertThat(month()).isEqualTo(5)
                assertThat(day()).isEqualTo(7)
                assertThat(hour()).isEqualTo(23)
                assertThat(minute()).isEqualTo(45)
                assertThat(second()).isEqualTo(0)
                assertThat(millisecond()).isEqualTo(487)
                assertThat(offsetMinutes()).isEqualTo(60)

                assertOffsetDate("2016-05-07T23:45:00.487+01:00")
            }
        }
    }

    @Test
    internal fun `no timezone offset 1`() {
        testSingleCase(Krono.enStrict, "1994-11-05T13:15:30", REF_DATE) {
            assertThat(it.text).isEqualTo("1994-11-05T13:15:30")

            with (it.start) {
                assertThat(year()).isEqualTo(1994)
                assertThat(month()).isEqualTo(11)
                assertThat(day()).isEqualTo(5)
                assertThat(hour()).isEqualTo(13)
                assertThat(minute()).isEqualTo(15)
                assertThat(second()).isEqualTo(30)
                assertThat(millisecond()).isEqualTo(0)
                assertThat(certainOffset()).isFalse()

                assertDate("1994-11-05T13:15:30")
            }
        }
    }

    @Test
    internal fun `no timezone offset 2`() {
        testSingleCase(Krono.enStrict, "2015-07-31T12:00:00", REF_DATE) {
            assertThat(it.text).isEqualTo("2015-07-31T12:00:00")

            with (it.start) {
                assertThat(year()).isEqualTo(2015)
                assertThat(month()).isEqualTo(7)
                assertThat(day()).isEqualTo(31)
                assertThat(hour()).isEqualTo(12)
                assertThat(minute()).isEqualTo(0)
                assertThat(second()).isEqualTo(0)
                assertThat(millisecond()).isEqualTo(0)
                assertThat(certainOffset()).isFalse()

                assertDate("2015-07-31T12:00:00")
            }
        }
    }
}
