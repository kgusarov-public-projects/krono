package org.kgusarov.krono.extensions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class NumberExtensionsKtTest {
    @Test
    fun plus() {
        val num: Int? = null
        assertThat(num + 12).isEqualTo(12)
    }

    @Test
    fun compareTo() {
        val num: Int? = null
        assertThat(num < 12).isTrue()
        assertThat(num > 12).isFalse()
        assertThat(num.compareTo(0)).isEqualTo(0)
    }
}
