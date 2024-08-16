package org.kgusarov.krono.extensions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class StringExtensionsKtTest {
    @Test
    fun `not operator`() {
        var str: String? = null

        assertThat(!str).isTrue

        str = ""
        assertThat(!str).isTrue

        str = "Hello, World!"
        assertThat(!str).isFalse
    }

    @Test
    fun substr() {
        val str: String = "123"

        assertThat(str.substr(0, 1)).isEqualTo("1")
        assertThat(str.substr(1, 1)).isEqualTo("2")
        assertThat(str.substr(2, 1)).isEqualTo("3")
        assertThat(str.substr(0, 5)).isEqualTo("123")
    }

    @Test
    fun `parse valid integer string`() {
        val str = "123"
        val result = str.safeParseInt()
        assertThat(result).isEqualTo(123)
    }

    @Test
    fun `parse valid decimal string`() {
        val str = "123.45"
        val result = str.safeParseInt()
        assertThat(result).isEqualTo(123)
    }

    @Test
    fun `parse invalid string`() {
        val str = "abc"
        val result = str.safeParseInt()
        assertThat(result).isNull()
    }

    @Test
    fun `parse empty string`() {
        val str = ""
        val result = str.safeParseInt()
        assertThat(result).isNull()
    }
}
