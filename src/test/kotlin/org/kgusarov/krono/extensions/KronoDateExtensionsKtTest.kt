package org.kgusarov.krono.extensions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.KronoDate

internal class KronoDateExtensionsKtTest {
    @Test
    internal fun addInvalidUnit() {
        val date = KronoDate.parse("2010-12-13T00:00:00")
        assertThat(date.add("unknown", 100)).isEqualTo(date)
    }
}
