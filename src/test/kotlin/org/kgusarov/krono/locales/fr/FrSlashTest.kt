package org.kgusarov.krono.locales.fr

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.Krono
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase

internal class FrSlashTest {
    @Test
    internal fun `8-2-2016`() {
        testSingleCase(Krono.frCasual, "8/2/2016", "2012-07-10T00:00:00") {
            it.start.assertDate("2016-02-08T12:00:00")
        }
    }

    @Test
    internal fun `le 8-2-2016`() {
        testSingleCase(Krono.frCasual, "8/2/2016", "2012-07-10T00:00:00") {
            it.start.assertDate("2016-02-08T12:00:00")
        }
    }

    @Test
    internal fun `le 8-2`() {
        testSingleCase(Krono.frCasual, "8/2", "2012-08-10T00:00:00") {
            it.start.assertDate("2013-02-08T12:00:00")
        }
    }

    @Test
    internal fun `lundi 8-2-2016`() {
        testSingleCase(Krono.frCasual, "lundi 8/2/2016", "2012-08-10T00:00:00") {
            it.start.assertDate("2016-02-08T12:00:00")
        }
    }

    @Test
    internal fun `samedi 9-2-20`() {
        testSingleCase(Krono.frCasual, "samedi 9/2/20", "2012-08-10T00:00:00") {
            it.start.assertDate("2020-02-09T12:00:00")
        }
    }
}
