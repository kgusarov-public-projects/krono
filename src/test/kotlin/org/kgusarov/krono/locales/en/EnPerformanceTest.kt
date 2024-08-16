package org.kgusarov.krono.locales.en

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.Krono
import org.kgusarov.krono.measureTime

internal class EnPerformanceTest {
    @Test
    fun `benchmarking against whitespace backtracking`() {
        val str = "BGR3                                                                                         " +
            "                                                                                        186          " +
            "                                      days                                                           " +
            "                                                                                                     " +
            "                                                                                                     " +
            "           18                                                hours                                   " +
            "                                                                                                     " +
            "                                                                                                     " +
            "                                   37                                                minutes         " +
            "                                                                                                     " +
            "                                                                                                     " +
            "                                                             01                                      " +
            "          seconds"

        measureTime {
            val results = Krono.enCasual.parse(str)
            assertThat(results).isEmpty()
        }
    }
}
