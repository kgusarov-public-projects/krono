package org.kgusarov.krono

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.extensions.toSecondsInt
import java.time.temporal.ChronoField
import java.util.concurrent.TimeUnit
import java.util.stream.Stream

internal class ParsingComponentsTest {
    @Test
    internal fun `create and manipulate parsing components`() {
        val reference = ReferenceWithTimezone()
        val components = ParsingComponents(
            reference,
            ChronoField.YEAR to 2014,
            ChronoField.MONTH_OF_YEAR to 11,
            ChronoField.DAY_OF_MONTH to 24,
        )

        with(components) {
            assertThat(get(ChronoField.YEAR)).isEqualTo(2014)
            assertThat(get(ChronoField.MONTH_OF_YEAR)).isEqualTo(11)
            assertThat(get(ChronoField.DAY_OF_MONTH)).isEqualTo(24)
            assertThat(tags()).isEmpty()

            assertDoesNotThrow {
                instant()
            }

            assertThat(get(ChronoField.DAY_OF_WEEK)).isNull()
            assertThat(isCertain(ChronoField.DAY_OF_WEEK)).isFalse()

            imply(ChronoField.DAY_OF_WEEK, 1)
            assertThat(get(ChronoField.DAY_OF_WEEK)).isEqualTo(1)
            assertThat(isCertain(ChronoField.DAY_OF_WEEK)).isFalse()

            assign(ChronoField.DAY_OF_WEEK, 2)
            assertThat(get(ChronoField.DAY_OF_WEEK)).isEqualTo(2)
            assertThat(isCertain(ChronoField.DAY_OF_WEEK)).isTrue()

            imply(ChronoField.YEAR, 2013)
            assertThat(get(ChronoField.YEAR)).isEqualTo(2014)

            assign(ChronoField.YEAR, 2013)
            assertThat(get(ChronoField.YEAR)).isEqualTo(2013)

            addTag("custom/testing_component_tag")
            assertThat(tags()).hasSize(1)
            assertThat(tags()).contains("custom/testing_component_tag")
        }
    }

    @Test
    internal fun `calendar checking with implied components`() {
        val reference = ReferenceWithTimezone()
        val components = ParsingComponents(
            reference,
            ChronoField.DAY_OF_MONTH to 13,
            ChronoField.MONTH_OF_YEAR to 3,
            ChronoField.YEAR to 2021,
            ChronoField.HOUR_OF_DAY to 14,
            ChronoField.MINUTE_OF_HOUR to 2,
            ChronoField.SECOND_OF_MINUTE to 14,
            ChronoField.MILLI_OF_SECOND to 0,
        )

        components.imply(ChronoField.OFFSET_SECONDS, TimeUnit.MINUTES.toSecondsInt(-300))
        assertThat(components.isValidDate()).isTrue()
    }

    @ParameterizedTest
    @MethodSource("checkCalendarArgs")
    internal fun `check calendar`(expected: Boolean, vararg parts: Pair<KronoComponent, Int>) {
        val reference = ReferenceWithTimezone(KronoDate.now())
        val components = ParsingComponents(reference, *parts)
        assertThat(components.isValidDate()).isEqualTo(expected)
    }

    @ParameterizedTest
    @MethodSource("checkDstSkipArgs")
    internal fun `check non-existing date during DST skip`(expected: Boolean, vararg parts: Pair<KronoComponent, Int>) {
        val reference = ReferenceWithTimezone(ParsingReference(
            KronoDate.now(),
            "CET",
        ))
        val components = ParsingComponents(reference, *parts)
        assertThat(components.isValidDate()).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun checkCalendarArgs(): Stream<Arguments> =
            Stream.of(
                Arguments.of(
                    true,
                    arrayOf(
                        ChronoField.YEAR to 2014,
                        ChronoField.MONTH_OF_YEAR to 11,
                        ChronoField.DAY_OF_MONTH to 24,
                    )
                ),
                Arguments.of(
                    true,
                    arrayOf(
                        ChronoField.YEAR to 2014,
                        ChronoField.MONTH_OF_YEAR to 11,
                        ChronoField.DAY_OF_MONTH to 24,
                        ChronoField.HOUR_OF_DAY to 12,
                    )
                ),
                Arguments.of(
                    true,
                    arrayOf(
                        ChronoField.YEAR to 2014,
                        ChronoField.MONTH_OF_YEAR to 11,
                        ChronoField.DAY_OF_MONTH to 24,
                        ChronoField.HOUR_OF_DAY to 12,
                        ChronoField.MINUTE_OF_HOUR to 30,
                    )
                ),
                Arguments.of(
                    true,
                    arrayOf(
                        ChronoField.YEAR to 2014,
                        ChronoField.MONTH_OF_YEAR to 11,
                        ChronoField.DAY_OF_MONTH to 24,
                        ChronoField.HOUR_OF_DAY to 12,
                        ChronoField.MINUTE_OF_HOUR to 30,
                        ChronoField.SECOND_OF_MINUTE to 30,
                    )
                ),
                Arguments.of(
                    false,
                    arrayOf(
                        ChronoField.YEAR to 2014,
                        ChronoField.MONTH_OF_YEAR to 13,
                        ChronoField.DAY_OF_MONTH to 24,
                    )
                ),
                Arguments.of(
                    false,
                    arrayOf(
                        ChronoField.YEAR to 2014,
                        ChronoField.MONTH_OF_YEAR to 11,
                        ChronoField.DAY_OF_MONTH to 32,
                    )
                ),
                Arguments.of(
                    false,
                    arrayOf(
                        ChronoField.YEAR to 2014,
                        ChronoField.MONTH_OF_YEAR to 11,
                        ChronoField.DAY_OF_MONTH to 24,
                        ChronoField.HOUR_OF_DAY to 24,
                    )
                ),
                Arguments.of(
                    false,
                    arrayOf(
                        ChronoField.YEAR to 2014,
                        ChronoField.MONTH_OF_YEAR to 11,
                        ChronoField.DAY_OF_MONTH to 24,
                        ChronoField.HOUR_OF_DAY to 12,
                        ChronoField.MINUTE_OF_HOUR to 60,
                    )
                ),
                Arguments.of(
                    false,
                    arrayOf(
                        ChronoField.YEAR to 2014,
                        ChronoField.MONTH_OF_YEAR to 11,
                        ChronoField.DAY_OF_MONTH to 24,
                        ChronoField.HOUR_OF_DAY to 12,
                        ChronoField.MINUTE_OF_HOUR to 60,
                        ChronoField.SECOND_OF_MINUTE to 60,
                    )
                ),
            )

        @JvmStatic
        fun checkDstSkipArgs(): Stream<Arguments> =
            Stream.of(
                Arguments.of(
                    false,
                    arrayOf(
                        ChronoField.YEAR to 2022,
                        ChronoField.MONTH_OF_YEAR to 3,
                        ChronoField.DAY_OF_MONTH to 27,
                        ChronoField.HOUR_OF_DAY to 2,
                        ChronoField.MINUTE_OF_HOUR to 0,
                    )
                ),
                Arguments.of(
                    false,
                    arrayOf(
                        ChronoField.YEAR to 2022,
                        ChronoField.MONTH_OF_YEAR to 3,
                        ChronoField.DAY_OF_MONTH to 27,
                        ChronoField.HOUR_OF_DAY to 2,
                        ChronoField.MINUTE_OF_HOUR to 1,
                    )
                ),
                Arguments.of(
                    false,
                    arrayOf(
                        ChronoField.YEAR to 2022,
                        ChronoField.MONTH_OF_YEAR to 3,
                        ChronoField.DAY_OF_MONTH to 27,
                        ChronoField.HOUR_OF_DAY to 2,
                        ChronoField.MINUTE_OF_HOUR to 59,
                    )
                ),
                Arguments.of(
                    true,
                    arrayOf(
                        ChronoField.YEAR to 2022,
                        ChronoField.MONTH_OF_YEAR to 3,
                        ChronoField.DAY_OF_MONTH to 27,
                        ChronoField.HOUR_OF_DAY to 1,
                        ChronoField.MINUTE_OF_HOUR to 59,
                    )
                ),
                Arguments.of(
                    true,
                    arrayOf(
                        ChronoField.YEAR to 2022,
                        ChronoField.MONTH_OF_YEAR to 3,
                        ChronoField.DAY_OF_MONTH to 27,
                        ChronoField.HOUR_OF_DAY to 3,
                        ChronoField.MINUTE_OF_HOUR to 0,
                    )
                ),
            )
    }
}
