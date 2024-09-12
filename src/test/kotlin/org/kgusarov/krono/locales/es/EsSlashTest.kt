package org.kgusarov.krono.locales.es

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.Krono
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase

internal class EsSlashTest {
    @Test
    fun `lunes 8-2-2016`() {
        testSingleCase(Krono.esCasual, "lunes 8/2/2016", "2012-07-10T00:00:00") {
            assertThat(it.text).isEqualTo("lunes 8/2/2016")
            assertThat(it.index).isEqualTo(0)
            it.start.assertDate("2016-02-08T12:00:00")
        }
    }

    @Test
    fun `maretes 9-2-2016`() {
        testSingleCase(Krono.esCasual, "Martes 9/2/2016", "2012-07-10T00:00:00") {
            assertThat(it.text).isEqualTo("Martes 9/2/2016")
            assertThat(it.index).isEqualTo(0)
            it.start.assertDate("2016-02-09T12:00:00")
        }
    }
}
