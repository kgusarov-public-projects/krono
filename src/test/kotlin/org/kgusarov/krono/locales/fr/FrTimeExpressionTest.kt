package org.kgusarov.krono.locales.fr

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.Krono
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase
import org.kgusarov.krono.testUnexpectedResult
import java.util.stream.Stream

internal class FrTimeExpressionTest {
    @ParameterizedTest
    @MethodSource("singleExpressionArgs")
    internal fun `single expression`(text: String, refDate: String, expectedDate: String) {
        testSingleCase(Krono.frCasual, text, refDate) {
            it.start.assertDate(expectedDate)
        }
    }

    @ParameterizedTest
    @MethodSource("rangeExpressionArgs")
    internal fun `range expression`(
        text: String,
        refDate: String,
        expectedStartDate: String,
        expectedEndDate: String,
    ) {
        testSingleCase(Krono.frCasual, text, refDate) {
            it.start.assertDate(expectedStartDate)
            it.end!!.assertDate(expectedEndDate)
        }
    }

    @ParameterizedTest
    @MethodSource("combinedSingleExpressionArgs")
    internal fun `combined single expression`(text: String, refDate: String, expectedDate: String) {
        testSingleCase(Krono.frCasual, text, refDate) {
            it.start.assertDate(expectedDate)
        }
    }

    @ParameterizedTest
    @MethodSource("combinedRangeExpressionArgs")
    internal fun `combined range expression`(
        text: String,
        refDate: String,
        expectedStartDate: String,
        expectedEndDate: String,
    ) {
        testSingleCase(Krono.frCasual, text, refDate) {
            it.start.assertDate(expectedStartDate)
            it.end!!.assertDate(expectedEndDate)
        }
    }

    @Test
    internal fun `impossible cases`() {
        testUnexpectedResult(Krono.frCasual, "8:62", "2012-08-10T12:00:00")
        testUnexpectedResult(Krono.frCasual, "25:12", "2012-08-10T12:00:00")
        testUnexpectedResult(Krono.frCasual, "12h12:99s", "2012-08-10T12:00:00")
        testUnexpectedResult(Krono.frCasual, "13.12 PM", "2012-08-10T12:00:00")
        testUnexpectedResult(Krono.frCasual, "that I need to know or am I covered?");
    }

    @ParameterizedTest
    @MethodSource("timezoneExpressionArgs")
    internal fun `timezone expression`(text: String, refDate: String, expectedOffset: Int?) {
        testSingleCase(Krono.frCasual, text, refDate) {
            assertThat(it.start.offsetMinutes()).isEqualTo(expectedOffset)
        }
    }

    @ParameterizedTest
    @MethodSource("randomTextArgs")
    internal fun `random text`(text: String) {
        testSingleCase(Krono.frCasual, text) {
            assertThat(it.text).isEqualTo(text)
        }
    }

    companion object {
        @JvmStatic
        fun singleExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("8h10", "2012-08-10T12:00:00", "2012-08-10T08:10:00"),
            Arguments.of("8h10m", "2012-08-10T12:00:00", "2012-08-10T08:10:00"),
            Arguments.of("8h10m00", "2012-08-10T12:00:00", "2012-08-10T08:10:00"),
            Arguments.of("8h10m00s", "2012-08-10T12:00:00", "2012-08-10T08:10:00"),
            Arguments.of("8:10 PM", "2012-08-10T12:00:00", "2012-08-10T20:10:00"),
            Arguments.of("8h10 PM", "2012-08-10T12:00:00", "2012-08-10T20:10:00"),
            Arguments.of("1230pm", "2012-08-10T12:00:00", "2012-08-10T12:30:00"),
            Arguments.of("5:16p", "2012-08-10T12:00:00", "2012-08-10T17:16:00"),
            Arguments.of("5h16p", "2012-08-10T12:00:00", "2012-08-10T17:16:00"),
            Arguments.of("5h16mp", "2012-08-10T12:00:00", "2012-08-10T17:16:00"),
            Arguments.of("5:16 p.m.", "2012-08-10T12:00:00", "2012-08-10T17:16:00"),
            Arguments.of("5h16 p.m.", "2012-08-10T12:00:00", "2012-08-10T17:16:00"),
            Arguments.of("RDV à 6.13 AM", "2012-08-10T12:00:00", "2012-08-10T06:13:00"),
            Arguments.of(" 2012 à 10:12:59", "2012-08-10T12:00:00", "2012-08-10T10:12:59"),
        )

        @JvmStatic
        fun rangeExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("8:10 - 12.32", "2012-08-10T12:00:00", "2012-08-10T08:10:00", "2012-08-10T12:32:00"),
            Arguments.of("8:10 - 12h32", "2012-08-10T12:00:00", "2012-08-10T08:10:00", "2012-08-10T12:32:00"),
            Arguments.of(" de 6:30pm à 11:00pm ", "2012-08-10T12:00:00", "2012-08-10T18:30:00", "2012-08-10T23:00:00"),
            Arguments.of("13h-15h", "2012-08-10T12:00:00", "2012-08-10T13:00:00", "2012-08-10T15:00:00"),
            Arguments.of("13-15h", "2012-08-10T12:00:00", "2012-08-10T13:00:00", "2012-08-10T15:00:00"),
            Arguments.of("1-3pm", "2012-08-10T12:00:00", "2012-08-10T13:00:00", "2012-08-10T15:00:00"),
            Arguments.of("11pm-2", "2012-08-10T12:00:00", "2012-08-10T23:00:00", "2012-08-11T02:00:00"),
        )

        @JvmStatic
        fun combinedSingleExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("Quelque chose se passe le 2014-04-18 à 3h00", "2012-08-10T12:00:00", "2014-04-18T03:00:00"),
            Arguments.of("Quelque chose se passe le 10 Août 2012 à 10:12:59", "2012-08-10T12:00:00", "2012-08-10T10:12:59"),
            Arguments.of("Quelque chose se passe le 15juin 2016 20h", "2012-08-10T12:00:00", "2016-06-15T20:00:00"),
        )

        @JvmStatic
        fun combinedRangeExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "Quelque chose se passe le 2014-04-18 7:00 - 8h00 ...",
                "2012-08-10T12:00:00", "2014-04-18T07:00:00", "2014-04-18T08:00:00"
            ),
            Arguments.of(
                "Quelque chose se passe le 2014-04-18 de 7:00 à 20:00 ...",
                "2012-08-10T12:00:00", "2014-04-18T07:00:00", "2014-04-18T20:00:00"
            ),
        )

        @JvmStatic
        fun timezoneExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("Vendredi à 2 pm", "2016-04-28T12:00:00", null),
            Arguments.of("vendredi 2 pm EST", "2016-04-28T12:00:00", -300),
            Arguments.of("vendredi 15h CET", "2016-02-28T12:00:00", 60),
            Arguments.of("vendredi 15h cest", "2016-02-28T12:00:00", 120),
            Arguments.of("Vendredi à 2 pm est", "2016-04-28T12:00:00", -300),
            Arguments.of("Vendredi à 2 pm j'ai rdv...", "2016-04-28T12:00:00", null),
            Arguments.of("Vendredi à 2 pm je vais faire quelque chose", "2016-04-28T12:00:00", null),
        )

        @JvmStatic
        fun randomTextArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("lundi 29/4/2013 630-930am"),
            Arguments.of("mercredi 1/5/2013 1115am"),
            Arguments.of("vendredi 3/5/2013 1230pm"),
            Arguments.of("dimanche 6/5/2013  750am-910am"),
            Arguments.of("lundi 13/5/2013 630-930am"),
            Arguments.of("Vendredi 21/6/2013 2:30"),
            Arguments.of("mardi 7/2/2013 1-230 pm"),
            Arguments.of("mardi 7/2/2013 1-23h0"),
            Arguments.of("mardi 7/2/2013 1h-23h0m"),
            Arguments.of("Lundi, 24/6/2013, 7:00pm - 8:30pm"),
            Arguments.of("Jeudi6/5/2013 de 7h à 10h"),
            Arguments.of("18h"),
            Arguments.of("18-22h"),
            Arguments.of("11h-13"),
            Arguments.of("à 12h"),
            Arguments.of("Mercredi, 3 juil 2013 14h"),
        )
    }
}
