package org.kgusarov.krono.common.parsers

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RawResult
import org.kgusarov.krono.RegExpMatchArray

internal class SlashDateFormatParserTest {
    @Test
    internal fun `little-endian date format`() {
        val parser = SlashDateFormatParser(true)
        val context = ParsingContext("12/31/2023")
        val match = RegExpMatchArray(0, arrayOf("12/31/2023", "", "12", "31", "2023", ""))
        val result = parser.invoke(context, match)

        assertThat(result).isNotNull
        val parsedResult = (result as RawResult).value

        assertThat(parsedResult.start[KronoComponents.Day]).isEqualTo(31)
        assertThat(parsedResult.start[KronoComponents.Month]).isEqualTo(12)
        assertThat(parsedResult.start[KronoComponents.Year]).isEqualTo(2023)
    }

    @Test
    internal fun `big-endian date format`() {
        val parser = SlashDateFormatParser(false)
        val context = ParsingContext("31/12/2023")
        val match = RegExpMatchArray(0, arrayOf("31/12/2023", "", "31", "12", "2023", ""))
        val result = parser.invoke(context, match)

        assertThat(result).isNotNull

        val parsedResult = (result as RawResult).value
        assertThat(parsedResult.start[KronoComponents.Day]).isEqualTo(31)
        assertThat(parsedResult.start[KronoComponents.Month]).isEqualTo(12)
        assertThat(parsedResult.start[KronoComponents.Year]).isEqualTo(2023)
    }

    @Test
    internal fun `big-endian date format with little-endian date`() {
        val parser = SlashDateFormatParser(false)
        val context = ParsingContext("12/31/2023")
        val match = RegExpMatchArray(0, arrayOf("12/31/2023", "", "12", "31", "2023", ""))
        val result = parser.invoke(context, match)

        assertThat(result).isNotNull

        val parsedResult = (result as RawResult).value
        assertThat(parsedResult.start[KronoComponents.Day]).isEqualTo(31)
        assertThat(parsedResult.start[KronoComponents.Month]).isEqualTo(12)
        assertThat(parsedResult.start[KronoComponents.Year]).isEqualTo(2023)
    }

    @Test
    internal fun `invalid date format`() {
        val parser = SlashDateFormatParser(true)
        val context = ParsingContext("2023/12/31")
        val match = RegExpMatchArray(0, arrayOf("2023/12/31", "", "2023", "12", "31", ""))
        val result = parser.invoke(context, match)

        assertThat(result).isNull()
    }

    @Test
    internal fun `version format 1`() {
        val parser = SlashDateFormatParser(true)
        val context = ParsingContext("1.0")
        val match = RegExpMatchArray(0, arrayOf("1.0", "", "1", "0", "", ""))
        val result = parser.invoke(context, match)

        assertThat(result).isNull()
    }

    @Test
    internal fun `version format 2`() {
        val parser = SlashDateFormatParser(true)
        val context = ParsingContext("1.12.14")
        val match = RegExpMatchArray(0, arrayOf("1.12.14", "", "1", "12", "14", ""))
        val result = parser.invoke(context, match)

        assertThat(result).isNull()
    }

    @Test
    internal fun `day too big`() {
        val parser = SlashDateFormatParser(true)
        val context = ParsingContext("31/12/2023")
        val match = RegExpMatchArray(0, arrayOf("31/12/2023", "", "32", "12", "2023", ""))
        val result = parser.invoke(context, match)

        assertThat(result).isNull()
    }

    @Test
    internal fun `day too small`() {
        val parser = SlashDateFormatParser(true)
        val context = ParsingContext("00/12/2023")
        val match = RegExpMatchArray(0, arrayOf("00/12/2023", "", "00", "12", "2023", ""))
        val result = parser.invoke(context, match)

        assertThat(result).isNull()
    }

    @Test
    internal fun `missing year`() {
        val parser = SlashDateFormatParser(true)
        val context = ParsingContext("12/31")
        val match = RegExpMatchArray(0, arrayOf("12/31", "", "12", "31", "", ""))
        val result = parser.invoke(context, match)

        assertThat(result).isNotNull

        val parsedResult = (result as RawResult).value
        assertThat(parsedResult.start[KronoComponents.Day]).isEqualTo(31)
        assertThat(parsedResult.start[KronoComponents.Month]).isEqualTo(12)
        assertThat(parsedResult.start[KronoComponents.Year]).isEqualTo(context.instant.year)
    }
}
