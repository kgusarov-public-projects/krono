package org.kgusarov.krono.common.parsers

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kgusarov.krono.ComponentsInputResult
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.MapComponentsInput
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ISOFormatParserTest {
    private val parser = ISOFormatParser()

    @Test
    fun `date only`(@Mock context: ParsingContext) {
        val match = RegExpMatchArray(
            0,
            arrayOf("2023-10-05", "2023", "10", "05", null, null, null, null, null, null),
        )
        val result: ComponentsInputResult = parser.innerExtract(context, match) as ComponentsInputResult
        val components = (result.value as MapComponentsInput).value

        assertThat(components).isNotNull
        assertThat(components?.get(KronoComponents.Year)).isEqualTo(2023)
        assertThat(components?.get(KronoComponents.Month)).isEqualTo(10)
        assertThat(components?.get(KronoComponents.Day)).isEqualTo(5)
    }

    @Test
    fun `date and time`(@Mock context: ParsingContext) {
        val match = RegExpMatchArray(
            0,
            arrayOf("2023-10-05T14:30", "2023", "10", "05", "14", "30", null, null, null, null),
        )
        val result: ComponentsInputResult = parser.innerExtract(context, match) as ComponentsInputResult
        val components = (result.value as MapComponentsInput).value

        assertThat(components).isNotNull
        assertThat(components?.get(KronoComponents.Year)).isEqualTo(2023)
        assertThat(components?.get(KronoComponents.Month)).isEqualTo(10)
        assertThat(components?.get(KronoComponents.Day)).isEqualTo(5)
        assertThat(components?.get(KronoComponents.Hour)).isEqualTo(14)
        assertThat(components?.get(KronoComponents.Minute)).isEqualTo(30)
    }

    @Test
    fun `date, time and seconds`(@Mock context: ParsingContext) {
        val match = RegExpMatchArray(
            0,
            arrayOf("2023-10-05T14:30:45", "2023", "10", "05", "14", "30", "45", null, null, null),
        )
        val result: ComponentsInputResult = parser.innerExtract(context, match) as ComponentsInputResult
        val components = (result.value as MapComponentsInput).value

        assertThat(components).isNotNull
        assertThat(components?.get(KronoComponents.Year)).isEqualTo(2023)
        assertThat(components?.get(KronoComponents.Month)).isEqualTo(10)
        assertThat(components?.get(KronoComponents.Day)).isEqualTo(5)
        assertThat(components?.get(KronoComponents.Hour)).isEqualTo(14)
        assertThat(components?.get(KronoComponents.Minute)).isEqualTo(30)
        assertThat(components?.get(KronoComponents.Second)).isEqualTo(45)
    }

    @Test
    fun `date, time, seconds and milliseconds`(@Mock context: ParsingContext) {
        val match = RegExpMatchArray(
            0,
            arrayOf("2023-10-05T14:30:45.123", "2023", "10", "05", "14", "30", "45", "123", null, null),
        )
        val result: ComponentsInputResult = parser.innerExtract(context, match) as ComponentsInputResult
        val components = (result.value as MapComponentsInput).value

        assertThat(components).isNotNull
        assertThat(components?.get(KronoComponents.Year)).isEqualTo(2023)
        assertThat(components?.get(KronoComponents.Month)).isEqualTo(10)
        assertThat(components?.get(KronoComponents.Day)).isEqualTo(5)
        assertThat(components?.get(KronoComponents.Hour)).isEqualTo(14)
        assertThat(components?.get(KronoComponents.Minute)).isEqualTo(30)
        assertThat(components?.get(KronoComponents.Second)).isEqualTo(45)
        assertThat(components?.get(KronoComponents.Millisecond)).isEqualTo(123)
    }

    @Test
    fun `date, time and timezone offset`(@Mock context: ParsingContext) {
        val match = RegExpMatchArray(
            0,
            arrayOf("2023-10-05T14:30:45+02:00", "2023", "10", "05", "14", "30", "45", null, "02", "00"),
        )
        val result: ComponentsInputResult = parser.innerExtract(context, match) as ComponentsInputResult
        val components = (result.value as MapComponentsInput).value

        assertThat(components).isNotNull
        assertThat(components?.get(KronoComponents.Year)).isEqualTo(2023)
        assertThat(components?.get(KronoComponents.Month)).isEqualTo(10)
        assertThat(components?.get(KronoComponents.Day)).isEqualTo(5)
        assertThat(components?.get(KronoComponents.Hour)).isEqualTo(14)
        assertThat(components?.get(KronoComponents.Minute)).isEqualTo(30)
        assertThat(components?.get(KronoComponents.Second)).isEqualTo(45)
        assertThat(components?.get(KronoComponents.Offset)).isEqualTo(7200)
    }
}
