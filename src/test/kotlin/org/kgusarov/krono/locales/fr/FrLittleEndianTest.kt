package org.kgusarov.krono.locales.fr

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.Krono
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase
import org.kgusarov.krono.testUnexpectedResult
import java.util.stream.Stream

internal class FrLittleEndianTest {
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
    @MethodSource("combinedExpressionArgs")
    internal fun `combined expression`(text: String, refDate: String, expectedDate: String) {
        testSingleCase(Krono.frCasual, text, refDate) {
            it.start.assertDate(expectedDate)
        }
    }

    @ParameterizedTest
    @MethodSource("accentedExpressionArgs")
    internal fun `accented expression`(text: String, refDate: String, expectedDate: String) {
        testSingleCase(Krono.frCasual, text, refDate) {
            it.start.assertDate(expectedDate)
        }
    }

    @Test
    internal fun `impossible dates`() {
        testUnexpectedResult(Krono.frCasual, "32 Août 2014")
        testUnexpectedResult(Krono.frCasual, "29 Février 2014")
        testUnexpectedResult(Krono.frCasual, "32 Aout")
        testUnexpectedResult(Krono.frCasual, "29 Fevrier", "2013-08-10T12:00:00")
    }

    companion object {
        @JvmStatic
        fun singleExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("10 Août 2012", "2012-08-10T12:00:00", "2012-08-10T12:00:00"),
            Arguments.of("8 Février", "2012-08-10T12:00:00", "2013-02-08T12:00:00"),
            Arguments.of("1er Août 2012", "2012-08-01T12:00:00", "2012-08-01T12:00:00"),
            Arguments.of("10 Août 234 AC", "2012-08-10T12:00:00", "-0234-08-10T12:00:00"),
            Arguments.of("10 Août 88 p. Chr. n.", "2012-08-10T12:00:00", "0088-08-10T12:00:00"),
            Arguments.of("Dim 15 Sept", "2013-09-15T12:00:00", "2013-09-15T12:00:00"),
            Arguments.of("DIM 15SEPT", "2013-09-15T12:00:00", "2013-09-15T12:00:00"),
            Arguments.of("La date limite est le 10 Août", "2012-08-10T12:00:00", "2012-08-10T12:00:00"),
            Arguments.of("La date limite est le Mardi 10 janvier", "2012-08-10T12:00:00", "2013-01-10T12:00:00"),
            Arguments.of("La date limite est Mar 10 Jan", "2012-08-10T12:00:00", "2013-01-10T12:00:00"),
            Arguments.of("31 mars 2016", "2012-08-10T12:00:00", "2016-03-31T12:00:00"),
        )

        @JvmStatic
        fun rangeExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("10 - 22 Août 2012", "2012-08-10T12:00:00", "2012-08-10T12:00:00", "2012-08-22T12:00:00"),
            Arguments.of("10 au 22 Août 2012", "2012-08-10T12:00:00", "2012-08-10T12:00:00", "2012-08-22T12:00:00"),
            Arguments.of("10 Août - 12 Septembre", "2012-08-10T12:00:00", "2012-08-10T12:00:00", "2012-09-12T12:00:00"),
            Arguments.of("10 Août - 12 Septembre 2013", "2012-08-10T12:00:00", "2013-08-10T12:00:00", "2013-09-12T12:00:00"),
        )

        @JvmStatic
        fun combinedExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("12 juillet à 19:00", "2012-08-10T12:00:00", "2012-07-12T19:00:00"),
            Arguments.of("5 mai 12:00", "2012-08-10T12:00:00", "2012-05-05T12:00:00"),
            Arguments.of("7 Mai 11:00", "2012-08-10T12:00:00", "2012-05-07T11:00:00"),
        )

        @JvmStatic
        fun accentedExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("10 Août 2012", "2012-08-10T12:00:00", "2012-08-10T12:00:00"),
            Arguments.of("10 Février 2012", "2012-08-10T12:00:00", "2012-02-10T12:00:00"),
            Arguments.of("10 Décembre 2012", "2012-08-10T12:00:00", "2012-12-10T12:00:00"),
            Arguments.of("10 Aout 2012", "2012-08-10T12:00:00", "2012-08-10T12:00:00"),
            Arguments.of("10 Fevrier 2012", "2012-08-10T12:00:00", "2012-02-10T12:00:00"),
            Arguments.of("10 Decembre 2012", "2012-08-10T12:00:00", "2012-12-10T12:00:00"),
        )
    }
}