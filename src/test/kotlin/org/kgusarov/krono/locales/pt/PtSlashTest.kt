package org.kgusarov.krono.locales.pt

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.Krono
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase

internal class PtSlashTest {
    @Test
    internal fun `segunda 8-2-2016`() {
        testSingleCase(Krono.ptCasual, "segunda 8/2/2016", "2012-07-10T00:00:00") {
            assertThat(it.text).isEqualTo("segunda 8/2/2016")
            assertThat(it.index).isEqualTo(0)
            it.start.assertDate("2016-02-08T12:00:00")
        }
    }

    @Test
    internal fun `terça-feira 9-2-2016`() {
        testSingleCase(Krono.ptCasual, "Terça-feira 9/2/2016", "2012-07-10T00:00:00") {
            assertThat(it.text).isEqualTo("Terça-feira 9/2/2016")
            assertThat(it.index).isEqualTo(0)
            it.start.assertDate("2016-02-09T12:00:00")
        }
    }
}
