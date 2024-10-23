package org.kgusarov.krono.common.parsers

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kgusarov.krono.ComponentsInputResult
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.MapComponentsInput
import org.kgusarov.krono.ParsedComponentsInput
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.ReferenceWithTimezone
import org.kgusarov.krono.RegExpMatchArray
import org.mockito.Mock
import org.mockito.Mockito.doAnswer
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any

@ExtendWith(MockitoExtension::class)
internal class ISOFormatParserTest {
    private val parser = ISOFormatParser()

    @Test
    internal fun `date only`(@Mock context: ParsingContext) {
        val match = RegExpMatchArray(
            0,
            arrayOf("2023-10-05", "2023", "10", "05", null, null, null, null, null, null),
        )

        mockContext(context)

        val result: ComponentsInputResult = parser.innerExtract(context, match) as ComponentsInputResult
        val components = (result.value as ParsedComponentsInput).value

        assertThat(components).isNotNull
        assertThat(components[KronoComponents.Year]).isEqualTo(2023)
        assertThat(components[KronoComponents.Month]).isEqualTo(10)
        assertThat(components[KronoComponents.Day]).isEqualTo(5)
    }

    @Test
    internal fun `date and time`(@Mock context: ParsingContext) {
        val match = RegExpMatchArray(
            0,
            arrayOf("2023-10-05T14:30", "2023", "10", "05", "14", "30", null, null, null, null),
        )

        mockContext(context)

        val result: ComponentsInputResult = parser.innerExtract(context, match) as ComponentsInputResult
        val components = (result.value as ParsedComponentsInput).value

        assertThat(components).isNotNull
        assertThat(components[KronoComponents.Year]).isEqualTo(2023)
        assertThat(components[KronoComponents.Month]).isEqualTo(10)
        assertThat(components[KronoComponents.Day]).isEqualTo(5)
        assertThat(components[KronoComponents.Hour]).isEqualTo(14)
        assertThat(components[KronoComponents.Minute]).isEqualTo(30)
    }

    @Test
    internal fun `date, time and seconds`(@Mock context: ParsingContext) {
        val match = RegExpMatchArray(
            0,
            arrayOf("2023-10-05T14:30:45", "2023", "10", "05", "14", "30", "45", null, null, null),
        )

        mockContext(context)

        val result: ComponentsInputResult = parser.innerExtract(context, match) as ComponentsInputResult
        val components = (result.value as ParsedComponentsInput).value

        assertThat(components).isNotNull
        assertThat(components[KronoComponents.Year]).isEqualTo(2023)
        assertThat(components[KronoComponents.Month]).isEqualTo(10)
        assertThat(components[KronoComponents.Day]).isEqualTo(5)
        assertThat(components[KronoComponents.Hour]).isEqualTo(14)
        assertThat(components[KronoComponents.Minute]).isEqualTo(30)
        assertThat(components[KronoComponents.Second]).isEqualTo(45)
    }

    @Test
    internal fun `date, time, seconds and milliseconds`(@Mock context: ParsingContext) {
        val match = RegExpMatchArray(
            0,
            arrayOf("2023-10-05T14:30:45.123", "2023", "10", "05", "14", "30", "45", "123", null, null),
        )

        mockContext(context)

        val result: ComponentsInputResult = parser.innerExtract(context, match) as ComponentsInputResult
        val components = (result.value as ParsedComponentsInput).value

        assertThat(components).isNotNull
        assertThat(components[KronoComponents.Year]).isEqualTo(2023)
        assertThat(components[KronoComponents.Month]).isEqualTo(10)
        assertThat(components[KronoComponents.Day]).isEqualTo(5)
        assertThat(components[KronoComponents.Hour]).isEqualTo(14)
        assertThat(components[KronoComponents.Minute]).isEqualTo(30)
        assertThat(components[KronoComponents.Second]).isEqualTo(45)
        assertThat(components[KronoComponents.Millisecond]).isEqualTo(123)
    }

    @Test
    internal fun `date, time and timezone offset`(@Mock context: ParsingContext) {
        val match = RegExpMatchArray(
            0,
            arrayOf("2023-10-05T14:30:45+02:00", "2023", "10", "05", "14", "30", "45", null, "+02:00", "02", "00"),
        )

        mockContext(context)

        val result: ComponentsInputResult = parser.innerExtract(context, match) as ComponentsInputResult
        val components = (result.value as ParsedComponentsInput).value

        assertThat(components).isNotNull
        assertThat(components[KronoComponents.Year]).isEqualTo(2023)
        assertThat(components[KronoComponents.Month]).isEqualTo(10)
        assertThat(components[KronoComponents.Day]).isEqualTo(5)
        assertThat(components[KronoComponents.Hour]).isEqualTo(14)
        assertThat(components[KronoComponents.Minute]).isEqualTo(30)
        assertThat(components[KronoComponents.Second]).isEqualTo(45)
        assertThat(components[KronoComponents.Offset]).isEqualTo(7200)
    }

    private fun mockContext(context: ParsingContext) {
        doAnswer {
            val mapComponentsInput = it.getArgument(0, MapComponentsInput::class.java)
            return@doAnswer mapComponentsInput(ReferenceWithTimezone())
        }.`when`(context).createParsingComponents(any<MapComponentsInput>())
    }
}
