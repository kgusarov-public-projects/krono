package org.kgusarov.krono.locales.en

import com.google.common.base.Stopwatch
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.Krono
import org.kgusarov.krono.TestLogger

internal class EnPerformanceTest {
    @Test
    fun `benchmarking against whitespace backtracking`() {
        val stopwatch = Stopwatch.createStarted()
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

        val results = Krono.enCasual.parse(str)
        stopwatch.stop()

        assertThat(results).isEmpty()

        TestLogger.LOGGER.info("Elapsed time: {}", stopwatch)
    }
}
