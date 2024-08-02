package org.kgusarov.krono.extensions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Suppress("KotlinConstantConditions")
internal class NumberExtensionsKtTest {
    @Test
    fun plus() {
        var num: Int? = null
        assertThat(num + 12).isEqualTo(12)

        num = 0
        assertThat(num + 12).isEqualTo(12)
    }

    @Test
    fun minus() {
        var num: Int? = null
        assertThat(num - 12).isEqualTo(-12)

        num = 0
        assertThat(num - 12).isEqualTo(-12)
    }

    @Test
    fun unaryMinus() {
        var num: Int? = null
        assertThat(-num).isEqualTo(-0)

        num = 0
        assertThat(-num).isEqualTo(-0)
    }

    @Test
    fun compareTo() {
        var num: Int? = null
        assertThat(num < 12).isTrue()
        assertThat(num > 12).isFalse()
        assertThat(num.compareTo(0)).isEqualTo(0)

        num = 0
        assertThat(num < 12).isTrue()
        assertThat(num > 12).isFalse()
        assertThat(num.compareTo(0)).isEqualTo(0)
    }
}
