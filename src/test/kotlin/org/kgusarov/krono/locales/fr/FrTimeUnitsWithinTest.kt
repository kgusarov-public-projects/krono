package org.kgusarov.krono.locales.fr

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.Krono
import org.kgusarov.krono.ParsingOption
import org.kgusarov.krono.TimezoneAbbreviations
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.assertOffsetDate
import org.kgusarov.krono.testSingleCase
import org.kgusarov.krono.testWithExpectedDate
import java.util.stream.Stream

internal class FrTimeUnitsWithinTest {
    @ParameterizedTest
    @MethodSource("singleExpressionArgs")
    fun `single expression`(
        text: String,
        refDate: String,
        expectedDate: String
    ) {
        testWithExpectedDate(
            Krono.frCasual,
            text,
            refDate,
            expectedDate,
        )
    }

    companion object {
        @JvmStatic
        fun singleExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("On doit faire quelque chose dans 5 jours.", "2012-08-10T12:00:00", "2012-08-15T12:00:00"),
            Arguments.of("On doit faire quelque chose dans cinq jours.", "2012-08-10T11:12:00", "2012-08-15T11:12:00"),
            Arguments.of("dans 5 minutes", "2012-08-10T12:14:00", "2012-08-10T12:19:00"),
            Arguments.of("pour 5 minutes", "2012-08-10T12:14:00", "2012-08-10T12:19:00"),
            Arguments.of("en 1 heure", "2012-08-10T12:14:00", "2012-08-10T13:14:00"),
            Arguments.of("régler une minuterie de 5 minutes", "2012-08-10T12:14:00", "2012-08-10T12:19:00"),
            Arguments.of("Dans 5 minutes je vais rentrer chez moi", "2012-08-10T12:14:00", "2012-08-10T12:19:00"),
            Arguments.of("Dans 5 secondes une voiture va bouger", "2012-08-10T12:14:00", "2012-08-10T12:14:05"),
            Arguments.of("dans deux semaines", "2012-08-10T12:14:00", "2012-08-24T12:14:00"),
            Arguments.of("dans un mois", "2012-08-10T07:14:00", "2012-09-10T07:14:00"),
            Arguments.of("dans quelques mois", "2012-07-10T22:14:00", "2012-10-10T22:14:00"),
            Arguments.of("en une année", "2012-08-10T12:14:00", "2013-08-10T12:14:00"),
            Arguments.of("dans une Année", "2012-08-10T12:14:00", "2013-08-10T12:14:00"),
            Arguments.of("Dans 5 Minutes une voiture doit être bougée", "2012-08-10T12:14:00", "2012-08-10T12:19:00"),
            Arguments.of("Dans 5 mins une voiture doit être bougée", "2012-08-10T12:14:00", "2012-08-10T12:19:00"),
        )
    }
}
