package org.kgusarov.krono.extensions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Duration

internal class DurationExtensionsKtTest {
    @Test
    internal fun `multiply duration by zero`() {
        val duration = Duration.ofSeconds(60)
        val result = duration.multipliedBy(BigDecimal.ZERO)
        assertThat(result).isEqualTo(Duration.ZERO)
    }

    @Test
    internal fun `multiply duration by one`() {
        val duration = Duration.ofSeconds(60)
        val result = duration.multipliedBy(BigDecimal.ONE)
        assertThat(result).isEqualTo(duration)
    }

    @Test
    internal fun `multiply duration by positive BigDecimal`() {
        val duration = Duration.ofSeconds(60)
        val multiplier = BigDecimal("1.5")
        val result = duration.multipliedBy(multiplier)
        assertThat(result).isEqualTo(Duration.ofSeconds(90))
    }

    @Test
    internal fun `multiply duration by negative BigDecimal`() {
        val duration = Duration.ofSeconds(60)
        val multiplier = BigDecimal("-1.5")
        val result = duration.multipliedBy(multiplier)
        assertThat(result).isEqualTo(Duration.ofSeconds(-90))
    }
}
