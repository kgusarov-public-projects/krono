package org.kgusarov.krono.locales.de

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.Krono
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase
import java.util.stream.Stream

internal class DeDotTest {
    @ParameterizedTest
    @MethodSource("args")
    internal fun `extract`(text: String, expectedDate: String) {
        testSingleCase(Krono.deCasual, text) {
            it.start.assertDate(expectedDate)
        }
    }

    companion object {
        @JvmStatic
        fun args(): Stream<Arguments> = Stream.of(
            Arguments.of("30.12.16", "2016-12-30T12:00:00.000"),
            Arguments.of("Freitag 30.12.16", "2016-12-30T12:00:00.000")
        )
    }
}