package org.kgusarov.krono.locales.fr

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.Krono
import org.kgusarov.krono.testWithExpectedDate
import java.util.stream.Stream

internal class FrTimeUnitsAgoTest {
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

    @ParameterizedTest
    @MethodSource("singleExpressionCasualArgs")
    fun `single expression - casual`(
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
        fun singleExpressionArgs(): Stream<Arguments>
            = Stream.of(
                Arguments.of("il y a 5 jours, on a fait quelque chose", "2012-08-10T12:00:00", "2012-08-05T12:00:00"),
                Arguments.of("il y a 10 jours, on a fait quelque chose", "2012-08-10T13:30:00", "2012-07-31T13:30:00"),
                Arguments.of("il y a 15 minutes", "2012-08-10T12:14:00", "2012-08-10T11:59:00"),
                Arguments.of("   il y a    12 heures", "2012-08-10T12:14:00", "2012-08-10T00:14:00"),
                Arguments.of("il y a 12 heures il s'est passé quelque chose", "2012-08-10T12:14:00", "2012-08-10T00:14:00"),
            )

        @JvmStatic
        fun singleExpressionCasualArgs(): Stream<Arguments>
            = Stream.of(
                Arguments.of("il y a 5 mois, on a fait quelque chose", "2012-10-10T12:00:00", "2012-05-10T12:00:00"),
                Arguments.of("il y a 5 ans, on a fait quelque chose", "2012-08-10T22:22:00", "2007-08-10T22:22:00"),
                Arguments.of("il y a une semaine, on a fait quelque chose", "2012-08-03T08:34:00", "2012-07-27T08:34:00"),
            )
    }
}
