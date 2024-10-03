package org.kgusarov.krono.locales.en

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.Krono
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase

private const val REF_DATE = "2022-02-02T00:00:00"

internal class EnMergingRelativeDatesTest {
    @Test
    internal fun `2 weeks after yesterday`() {
        testSingleCase(Krono.enCasual, "2 weeks after yesterday", REF_DATE) {
            assertThat(it.text).isEqualTo("2 weeks after yesterday")

            with (it.start) {
                assertThat(year()).isEqualTo(2022)
                assertThat(month()).isEqualTo(2)
                assertThat(day()).isEqualTo(15)
                assertThat(weekday()).isEqualTo(2)

                assertThat(certainDay()).isTrue
                assertThat(certainMonth()).isTrue
                assertThat(certainYear()).isTrue

                assertDate("2022-02-15T00:00:00")
            }
        }
    }

    @Test
    internal fun `2 months before 2nd February`() {
        testSingleCase(Krono.enCasual, "2 months before 02/02", REF_DATE) {
            assertThat(it.text).isEqualTo("2 months before 02/02")

            with (it.start) {
                assertThat(year()).isEqualTo(2021)
                assertThat(month()).isEqualTo(12)
                assertThat(day()).isEqualTo(2)

                assertThat(certainDay()).isFalse
                assertThat(certainMonth()).isTrue
                assertThat(certainYear()).isTrue

                assertDate("2021-12-02T12:00:00")
            }
        }
    }

    @Test
    internal fun `2 days after next friday`() {
        testSingleCase(Krono.enCasual, "2 days after next Friday", REF_DATE) {
            assertThat(it.text).isEqualTo("2 days after next Friday")

            with (it.start) {
                assertThat(year()).isEqualTo(2022)
                assertThat(month()).isEqualTo(2)
                assertThat(day()).isEqualTo(13)

                assertThat(certainDay()).isTrue
                assertThat(certainMonth()).isTrue
                assertThat(certainYear()).isTrue

                assertDate("2022-02-13T12:00:00")
            }
        }
    }
}
