package org.kgusarov.krono.locales.de

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.Krono
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase
import org.kgusarov.krono.testUnexpectedResult
import java.util.stream.Stream

internal class DeCasualTest {
    @Test
    internal fun `random negative text`() {
        testUnexpectedResult(Krono.deCasual, "nicheute")
        testUnexpectedResult(Krono.deCasual, "heutenicht")
        testUnexpectedResult(Krono.deCasual, "angestern")
        testUnexpectedResult(Krono.deCasual, "jetztig")
        testUnexpectedResult(Krono.deCasual, "ljetztlich")
    }

    @ParameterizedTest
    @MethodSource("singleExpressionArgs")
    internal fun `single expression`(text: String, refDate: String, expectedDate: String) {
        testSingleCase(Krono.deCasual, text, refDate) {
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
        testSingleCase(Krono.deCasual, text, refDate) {
            it.start.assertDate(expectedStartDate)
            it.end!!.assertDate(expectedEndDate)
        }
    }

    companion object {
        @JvmStatic
        fun singleExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("Die Deadline ist jetzt", "2012-08-10T08:09:10.011", "2012-08-10T08:09:10.011"),
            Arguments.of("Die Deadline ist heute", "2012-08-10T12:00:00", "2012-08-10T12:00:00"),
            Arguments.of("Die Deadline ist morgen", "2012-08-10T12:00:00", "2012-08-11T12:00:00"),
            Arguments.of("Die Deadline ist morgen", "2012-08-10T01:00:00", "2012-08-11T01:00:00"),
            Arguments.of("Die Deadline war gestern", "2012-08-10T12:00:00", "2012-08-09T12:00:00"),
            Arguments.of("Die Deadline war letzte Nacht", "2012-08-10T12:00:00", "2012-08-09T00:00:00"),
            Arguments.of("Die Deadline war gestern Nacht", "2012-08-10T12:00:00", "2012-08-09T22:00:00"),
            Arguments.of("Die Deadline war heute Morgen", "2012-08-10T12:00:00", "2012-08-10T06:00:00"),
            Arguments.of("Die Deadline war heute Nachmittag", "2012-08-10T12:00:00", "2012-08-10T15:00:00"),
            Arguments.of("Die Deadline war heute Abend", "2012-08-10T12:00:00", "2012-08-10T18:00:00"),
            Arguments.of("Die Deadline ist mittags", "2012-08-10T08:09:10.011", "2012-08-10T12:00:00.011"),
            Arguments.of("um Mitternacht", "2012-08-10T12:00:00", "2012-08-11T00:00:00"),
            Arguments.of("um Mitternacht", "2012-08-10T01:00:00", "2012-08-10T00:00:00"),
            Arguments.of("Die Deadline ist heute 17 Uhr", "2012-08-10T12:00:00", "2012-08-10T17:00:00"),
            Arguments.of("Die Deadline ist heute um 17 Uhr", "2012-08-10T12:00:00", "2012-08-10T17:00:00"),
            Arguments.of("heute Nacht", "2012-08-10T12:00:00", "2012-08-10T22:00:00"),
            Arguments.of("heute Nacht um 20 Uhr", "2012-08-10T12:00:00", "2012-08-10T20:00:00"),
            Arguments.of("heute Abend um 8", "2012-08-10T12:00:00", "2012-08-10T20:00:00"),
            Arguments.of("8 Uhr abends", "2012-08-10T12:00:00", "2012-08-10T20:00:00"),
            Arguments.of("Do", "2012-08-10T12:00:00", "2012-08-09T12:00:00"),
            Arguments.of("Donnerstag", "2012-08-10T12:00:00", "2012-08-09T12:00:00"),
            Arguments.of("gestern Nachmittag", "2012-08-10T12:00:00", "2012-08-09T15:00:00"),
            Arguments.of("morgen Morgen", "2012-08-10T08:00:00", "2012-08-11T06:00:00"),
            Arguments.of("uebermorgen Abend", "2012-08-10T08:00:00", "2012-08-12T18:00:00"),
            Arguments.of("vorgestern Vormittag", "2012-08-10T12:00:00", "2012-08-08T09:00:00"),
        )

        @JvmStatic
        fun rangeExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "Der Event ist heute - nächsten Freitag", "2012-08-04T12:00:00", "2012-08-04T12:00:00", "2012-08-10T12:00:00"
            ),
            Arguments.of(
                "Der Event ist heute - nächsten Freitag", "2012-08-10T12:00:00", "2012-08-10T12:00:00", "2012-08-17T12:00:00"
            ),
        )
    }
}
