package org.kgusarov.krono.locales.de

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.Krono
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase

internal class DeYearTest {
    @Test
    internal fun `10 August 234 vuZ`() {
        testSingleCase(Krono.deCasual, "10. August 234 v.u.Z.", "2012-08-10T12:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("10. August 234 v.u.Z.")

            with(it.start) {
                assertThat(year()).isEqualTo(-234)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(10)

                assertDate("-0234-08-10T12:00:00")
            }
        }
    }

    @Test
    internal fun `10 August 88 nuZ`() {
        testSingleCase(Krono.deCasual, "10. August 88 nuZ", "2012-08-10T12:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("10. August 88 nuZ")

            with(it.start) {
                assertThat(year()).isEqualTo(88)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(10)

                assertDate("0088-08-10T12:00:00")
            }
        }
    }

    @Test
    internal fun `10 August 88 uZ`() {
        testSingleCase(Krono.deCasual, "10. August 88 uZ", "2012-08-10T12:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("10. August 88 uZ")

            with(it.start) {
                assertThat(year()).isEqualTo(88)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(10)

                assertDate("0088-08-10T12:00:00")
            }
        }
    }

    @Test
    internal fun `10 August 88 d g Z`() {
        testSingleCase(Krono.deCasual, "10. August 88 d.g.Z.", "2012-08-10T12:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("10. August 88 d.g.Z.")

            with(it.start) {
                assertThat(year()).isEqualTo(88)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(10)

                assertDate("0088-08-10T12:00:00")
            }
        }
    }

    @Test
    internal fun `10 august 234 vChr`() {
        testSingleCase(Krono.deCasual, "10. August 234 v.Chr.", "2012-08-10T12:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("10. August 234 v.Chr.")

            with(it.start) {
                assertThat(year()).isEqualTo(-234)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(10)

                assertDate("-0234-08-10T12:00:00")
            }
        }
    }

    @Test
    internal fun `10 august 88 nC`() {
        testSingleCase(Krono.deCasual, "10. August 88 nC", "2012-08-10T12:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("10. August 88 nC")

            with(it.start) {
                assertThat(year()).isEqualTo(88)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(10)

                assertDate("0088-08-10T12:00:00")
            }
        }
    }

    @Test
    internal fun `10 august 234 vdZ`() {
        testSingleCase(Krono.deCasual, "10. August 234 v.d.Z.", "2012-08-10T12:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("10. August 234 v.d.Z.")

            with(it.start) {
                assertThat(year()).isEqualTo(-234)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(10)

                assertDate("-0234-08-10T12:00:00")
            }
        }
    }

    @Test
    internal fun `10 august 88 ndZ`() {
        testSingleCase(Krono.deCasual, "10. August 88 ndZ", "2012-08-10T12:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("10. August 88 ndZ")

            with(it.start) {
                assertThat(year()).isEqualTo(88)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(10)

                assertDate("0088-08-10T12:00:00")
            }
        }
    }

    @Test
    internal fun `10 august 234 vd g Z`() {
        testSingleCase(Krono.deCasual, "10. August 234 v.d.g.Z.", "2012-08-10T12:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("10. August 234 v.d.g.Z.")

            with(it.start) {
                assertThat(year()).isEqualTo(-234)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(10)

                assertDate("-0234-08-10T12:00:00")
            }
        }
    }

    @Test
    internal fun `10 august 88 ndgZ`() {
        testSingleCase(Krono.deCasual, "10. August 88 ndgZ", "2012-08-10T12:00:00") {
            assertThat(it.index).isEqualTo(0)
            assertThat(it.text).isEqualTo("10. August 88 ndgZ")

            with(it.start) {
                assertThat(year()).isEqualTo(88)
                assertThat(month()).isEqualTo(8)
                assertThat(day()).isEqualTo(10)

                assertDate("0088-08-10T12:00:00")
            }
        }
    }
}