package org.kgusarov.krono.locales.fr

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.Krono
import org.kgusarov.krono.testUnexpectedResult
import org.kgusarov.krono.testWithExpectedDate
import java.util.stream.Stream

internal class FrTimeUnitsCasualRelativeTest {
    @ParameterizedTest
    @MethodSource("singleExpressionArgs")
    internal fun `single expression`(
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

    @Test
    internal fun `negative cases`() {
        testUnexpectedResult(Krono.frCasual, "le mois d'avril")
        testUnexpectedResult(Krono.frCasual, "le mois d'avril prochain")
    }

    companion object {
        @JvmStatic
        fun singleExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("la semaine prochaine", "2017-05-12T12:00:00", "2017-05-19T12:00:00"),
            Arguments.of("les 2 prochaines semaines", "2017-05-12T18:11:00", "2017-05-26T18:11:00"),
            Arguments.of("les trois prochaines semaines", "2017-05-12T12:00:00", "2017-06-02T12:00:00"),
            Arguments.of("le mois dernier", "2017-05-12T12:00:00", "2017-04-12T12:00:00"),
            Arguments.of("les 30 jours précédents", "2017-05-12T12:00:00", "2017-04-12T12:00:00"),
            Arguments.of("les 24 heures passées", "2017-05-12T11:27:00", "2017-05-11T11:27:00"),
            Arguments.of("les 90 secondes suivantes", "2017-05-12T11:27:03", "2017-05-12T11:28:33"),
            Arguments.of("les huit dernieres minutes", "2017-05-12T11:27:00", "2017-05-12T11:19:00"),
            Arguments.of("le dernier trimestre", "2017-05-12T11:27:00", "2017-02-12T11:27:00"),
            Arguments.of("l'année prochaine", "2017-05-12T11:27:00", "2018-05-12T11:27:00"),
        )
    }
}
