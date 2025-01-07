package org.kgusarov.krono.locales.de

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.Krono
import org.kgusarov.krono.KronoMeridiem
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase
import java.util.stream.Stream

internal class DeTimeExpressionTest {
    @Test
    internal fun `single expression 18-10`() {
        testSingleCase(Krono.deCasual, "18:10", "2012-08-10T12:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("18:10")

            with(it.start) {
                assertThat(hour()).isEqualTo(18)
                assertThat(minute()).isEqualTo(10)

                assertThat(certainDay()).isFalse()
                assertThat(certainMonth()).isFalse()
                assertThat(certainYear()).isFalse()
                assertThat(certainHour()).isTrue()
                assertThat(certainMinute()).isTrue()
                assertThat(certainSecond()).isFalse()
                assertThat(certainMillisecond()).isFalse()

                assertDate("2012-08-10T18:10:00")
            }
        }
    }

    @Test
    internal fun `single expression um 14 Uhr`() {
        testSingleCase(Krono.deCasual, "um 14 Uhr") {
            with(it.start) {
                assertThat(hour()).isEqualTo(14)
                assertThat(minute()).isEqualTo(0)
            }
        }
    }

    @Test
    internal fun `single expression um 16h`() {
        testSingleCase(Krono.deCasual, "um 16h") {
            with(it.start) {
                assertThat(hour()).isEqualTo(16)
                assertThat(minute()).isEqualTo(0)
            }
        }
    }

    @Test
    internal fun `single expression um 7 morgens`() {
        testSingleCase(Krono.deCasual, "um 7 morgens") {
            with(it.start) {
                assertThat(hour()).isEqualTo(7)
                assertThat(minute()).isEqualTo(0)
                assertThat(meridiem()).isEqualTo(KronoMeridiem.AM)
                assertThat(certainMeridiem()).isTrue()
            }
        }
    }

    @Test
    internal fun `single expression 11-00 Uhr vormittags`() {
        testSingleCase(Krono.deCasual, "11:00 Uhr vormittags") {
            with(it.start) {
                assertThat(hour()).isEqualTo(11)
                assertThat(minute()).isEqualTo(0)
                assertThat(meridiem()).isEqualTo(KronoMeridiem.AM)
                assertThat(certainMeridiem()).isTrue()
            }
        }
    }

    @Test
    internal fun `single expression um 8 Uhr nachmittags`() {
        testSingleCase(Krono.deCasual, "um 8 Uhr nachmittags") {
            with(it.start) {
                assertThat(hour()).isEqualTo(20)
                assertThat(minute()).isEqualTo(0)
                assertThat(meridiem()).isEqualTo(KronoMeridiem.PM)
                assertThat(certainMeridiem()).isTrue()
            }
        }
    }

    @Test
    internal fun `single expression um 8 Uhr in der Nacht`() {
        testSingleCase(Krono.deCasual, "um 8 Uhr in der Nacht") {
            with(it.start) {
                assertThat(hour()).isEqualTo(20)
                assertThat(minute()).isEqualTo(0)
                assertThat(meridiem()).isEqualTo(KronoMeridiem.PM)
                assertThat(certainMeridiem()).isTrue()
            }
        }
    }

    @Test
    internal fun `single expression um 5 Uhr in der Nacht`() {
        testSingleCase(Krono.deCasual, "um 5 Uhr in der Nacht") {
            with(it.start) {
                assertThat(hour()).isEqualTo(5)
                assertThat(minute()).isEqualTo(0)
                assertThat(meridiem()).isEqualTo(KronoMeridiem.AM)
                assertThat(certainMeridiem()).isTrue()
            }
        }
    }
    
    @Test
    internal fun `range expression 18-10 - 22-32`() {
        testSingleCase(Krono.deCasual, "18:10 - 22:32", "2012-08-10T12:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("18:10 - 22:32")

            with(it.start) {
                assertThat(hour()).isEqualTo(18)
                assertThat(minute()).isEqualTo(10)

                assertThat(certainDay()).isFalse()
                assertThat(certainMonth()).isFalse()
                assertThat(certainYear()).isFalse()
                assertThat(certainHour()).isTrue()
                assertThat(certainMinute()).isTrue()
                assertThat(certainSecond()).isFalse()
                assertThat(certainMillisecond()).isFalse()

                assertDate("2012-08-10T18:10:00")
            }

            with(it.end!!) {
                assertThat(hour()).isEqualTo(22)
                assertThat(minute()).isEqualTo(32)

                assertThat(certainDay()).isFalse()
                assertThat(certainMonth()).isFalse()
                assertThat(certainYear()).isFalse()
                assertThat(certainHour()).isTrue()
                assertThat(certainMinute()).isTrue()
                assertThat(certainSecond()).isFalse()
                assertThat(certainMillisecond()).isFalse()

                assertDate("2012-08-10T22:32:00")
            }
        }
    }

    @Test
    internal fun `range expression von 6-30 bis 23-00`() {
        testSingleCase(Krono.deCasual, "von 6:30 bis 23:00", "2012-08-10T12:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("von 6:30 bis 23:00")

            with(it.start) {
                assertThat(hour()).isEqualTo(6)
                assertThat(minute()).isEqualTo(30)
                assertThat(meridiem()).isEqualTo(KronoMeridiem.AM)

                assertDate("2012-08-10T06:30:00")
            }

            with(it.end!!) {
                assertThat(hour()).isEqualTo(23)
                assertThat(minute()).isEqualTo(0)
                assertThat(meridiem()).isEqualTo(KronoMeridiem.PM)

                assertDate("2012-08-10T23:00:00")
            }
        }
    }

    @Test
    internal fun `range expression von 6h30 bis 23h00`() {
        testSingleCase(Krono.deCasual, "von 6h30 bis 23h00", "2012-08-10T12:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("von 6h30 bis 23h00")

            with(it.start) {
                assertThat(hour()).isEqualTo(6)
                assertThat(minute()).isEqualTo(30)
                assertThat(meridiem()).isEqualTo(KronoMeridiem.AM)

                assertDate("2012-08-10T06:30:00")
            }

            with(it.end!!) {
                assertThat(hour()).isEqualTo(23)
                assertThat(minute()).isEqualTo(0)
                assertThat(meridiem()).isEqualTo(KronoMeridiem.PM)

                assertDate("2012-08-10T23:00:00")
            }
        }
    }

    @Test
    internal fun `range expression von 6h30 morgens bis 11 am Abend`() {
        testSingleCase(Krono.deCasual, "von 6h30 morgens bis 11 am Abend", "2012-08-10T12:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("von 6h30 morgens bis 11 am Abend")

            with(it.start) {
                assertThat(hour()).isEqualTo(6)
                assertThat(minute()).isEqualTo(30)
                assertThat(meridiem()).isEqualTo(KronoMeridiem.AM)

                assertDate("2012-08-10T06:30:00")
            }

            with(it.end!!) {
                assertThat(hour()).isEqualTo(23)
                assertThat(minute()).isEqualTo(0)
                assertThat(meridiem()).isEqualTo(KronoMeridiem.PM)

                assertDate("2012-08-10T23:00:00")
            }
        }
    }

    @ParameterizedTest
    @MethodSource("timezoneExtractionArgs")
    internal fun `timezone extraction`(text: String, refDate: String, isCertain: Boolean, offset: Int?) {
        testSingleCase(Krono.deCasual, text, refDate) {
            with(it.start) {
                assertThat(certainOffset()).isEqualTo(isCertain)
                if (isCertain) {
                    assertThat(offsetMinutes()).isEqualTo(offset)
                }
            }
        }
    }

    @Test
    internal fun `random date + time expression`() {
        testSingleCase(Krono.deCasual, "um 12") {
            assertThat(it.text).isEqualTo("um 12")
        }

        testSingleCase(Krono.deCasual, "am Mittag") {
            assertThat(it.text).isEqualTo("Mittag")
            assertThat(it.start.hour()).isEqualTo(12)
        }
    }

    companion object {
        @JvmStatic
        fun timezoneExtractionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("um 14 Uhr", "2016-02-28T00:00:00", false, null),
            Arguments.of("um 14 Uhr CET", "2016-02-28T00:00:00", true, 60),
            Arguments.of("14 Uhr cet", "2016-05-28T00:00:00", true, 120),
            Arguments.of("am Freitag um 14 Uhr cetteln wir etwas an", "2016-02-28T00:00:00", false, null),
            Arguments.of("Freitag um 14 Uhr CET", "2016-05-28T00:00:00", true, 120),
        )
    }
}
