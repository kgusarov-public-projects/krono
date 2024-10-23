package org.kgusarov.krono.locales.pt

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.Krono
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase
import java.util.stream.Stream

internal class PtTimeExpressionTest {
    @Test
    internal fun `single expression`() {
        testSingleCase(Krono.ptCasual, "Ficaremos às 6.13 AM", "2012-08-10T12:00:00") {
            assertThat(it.index).isEqualTo(10)
            assertThat(it.text).isEqualTo("às 6.13 AM")

            with (it.start) {
                assertThat(hour()).isEqualTo(6)
                assertThat(minute()).isEqualTo(13)

                assertDate("2012-08-10T06:13:00")
            }
        }
    }
    
    @Test
    internal fun `range expression 1`() {
        testSingleCase(Krono.ptCasual, "8:10 - 12.32", "2012-08-10T12:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("8:10 - 12.32")
            
            with (it.start) {
                assertThat(hour()).isEqualTo(8)
                assertThat(minute()).isEqualTo(10)
                
                assertThat(certainDay()).isFalse()
                assertThat(certainMonth()).isFalse()
                assertThat(certainYear()).isFalse()
                assertThat(certainHour()).isTrue()
                assertThat(certainMinute()).isTrue()
                assertThat(certainSecond()).isFalse()
                assertThat(certainMillisecond()).isFalse()
                
                assertDate("2012-08-10T08:10:00")
            }
            
            with(it.end!!) {
                assertThat(hour()).isEqualTo(12)
                assertThat(minute()).isEqualTo(32)
                
                assertThat(certainDay()).isFalse()
                assertThat(certainMonth()).isFalse()
                assertThat(certainYear()).isFalse()
                assertThat(certainHour()).isTrue()
                assertThat(certainMinute()).isTrue()
                assertThat(certainSecond()).isFalse()
                assertThat(certainMillisecond()).isFalse()

                assertDate("2012-08-10T12:32:00")
            }
        }
    }

    @Test
    internal fun `range expression 2`() {
        testSingleCase(Krono.ptCasual, " de 6:30pm a 11:00pm", "2012-08-10T12:00:00") {
            assertThat(it.index).isEqualTo(1)
            assertThat(it.text).isEqualTo("de 6:30pm a 11:00pm")

            with(it.start) {
                assertThat(hour()).isEqualTo(18)
                assertThat(minute()).isEqualTo(30)
                assertThat(meridiem()).isEqualTo(1)

                assertDate("2012-08-10T18:30:00")
            }

            with(it.end!!) {
                assertThat(hour()).isEqualTo(23)
                assertThat(minute()).isEqualTo(0)
                assertThat(meridiem()).isEqualTo(1)

                assertDate("2012-08-10T23:00:00")
            }
        }
    }

    @Test
    internal fun `date time expression`() {
        testSingleCase(
            Krono.ptCasual,
            "Algo passou em 10 de Agosto de 2012 10:12:59 pm",
            "2012-08-10T12:00:00"
        ) {
            assertThat(it.index).isEqualTo(15)
            assertThat(it.text).isEqualTo("10 de Agosto de 2012 10:12:59 pm")

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(10)
                assertThat(hour()).isEqualTo(22)
                assertThat(minute()).isEqualTo(12)
                assertThat(second()).isEqualTo(59)
                assertThat(millisecond()).isEqualTo(0)
                assertThat(certainMillisecond()).isFalse()

                assertDate("2012-08-10T22:12:59")
            }
        }
    }

    @Test
    internal fun `time expression - imply meridiem`() {
        testSingleCase(Krono.ptCasual, "de 1pm a 3", "2012-08-10T12:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("de 1pm a 3")

            with(it.start) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(10)
                assertThat(hour()).isEqualTo(13)
                assertThat(minute()).isEqualTo(0)
                assertThat(second()).isEqualTo(0)
                assertThat(millisecond()).isEqualTo(0)
                assertThat(meridiem()).isEqualTo(1)
                assertThat(certainMeridiem()).isTrue()
            }

            with(it.end!!) {
                assertThat(year()).isEqualTo(2012)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(10)
                assertThat(hour()).isEqualTo(15)
                assertThat(minute()).isEqualTo(0)
                assertThat(second()).isEqualTo(0)
                assertThat(millisecond()).isEqualTo(0)
                assertThat(certainMeridiem()).isTrue()
            }
        }
    }

    @ParameterizedTest
    @MethodSource("randomDateTimeExpressionArgs")
    internal fun `random date time expression`(
        text: String,
        expectedStart: String,
        expectedEnd: String?,
    ) {
        testSingleCase(Krono.ptCasual, text, "2012-08-10T00:00:00") {
            with(it.start) {
                assertDate(expectedStart)
            }

            if (expectedEnd != null) {
                it.end!!.assertDate(expectedEnd)
            } else {
                assertThat(it.end).isNull()
            }
        }
    }

    companion object {
        @JvmStatic
        fun randomDateTimeExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("segunda 4/29/2013 630-930am", "2013-04-29T06:30:00", "2013-04-29T09:30:00"),
            Arguments.of("terça 5/1/2013 1115am", "2013-01-05T11:15:00", null),
            Arguments.of("quarta 5/3/2013 1230pm", "2013-03-05T12:30:00", null),
            Arguments.of("domingo 5/6/2013  750am-910am", "2013-06-05T07:50:00", "2013-06-05T09:10:00"),
            Arguments.of("segunda-feira 5/13/2013 630-930am", "2013-05-13T06:30:00", "2013-05-13T09:30:00"),
            Arguments.of("quarta-feira 5/15/2013 1030am", "2013-05-15T10:30:00", null),
            Arguments.of("quinta 6/21/2013 2:30", "2013-06-21T02:30:00", null),
            Arguments.of("terça-feira 7/2/2013 1-230 pm", "2013-02-07T13:00:00", "2013-02-07T14:30:00"),
            Arguments.of("Segunda-feira, 6/24/2013, 7:00pm - 8:30pm", "2013-06-24T19:00:00", "2013-06-24T20:30:00"),
            Arguments.of("Quarta, 3 Julho de 2013 às 2pm", "2013-07-03T14:00:00", null),
            Arguments.of("6pm", "2012-08-10T18:00:00", null),
            Arguments.of("6 pm", "2012-08-10T18:00:00", null),
            Arguments.of("7-10pm", "2012-08-10T19:00:00", "2012-08-10T22:00:00"),
            Arguments.of("11.1pm", "2012-08-10T23:01:00", null),
            Arguments.of("às 12", "2012-08-10T12:00:00", null),
        )
    }
}
