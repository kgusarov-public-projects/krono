package org.kgusarov.krono.common.refiners

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.ParsedResult
import org.kgusarov.krono.ParsingComponents
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.ReferenceWithTimezone
import org.kgusarov.krono.TestParsedResult
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.jupiter.MockitoExtension
import java.time.ZoneOffset

@ExtendWith(MockitoExtension::class)
class ExtractTimezoneAbbrRefinerTest {
    private val refiner = ExtractTimezoneAbbrRefiner()

    @Test
    fun `pattern does not match`(@Mock context: ParsingContext) {
        val result = TestParsedResult().apply {
            index = 0
            text = "2023-10-05T14:30:45"
            start = ParsingComponents(ReferenceWithTimezone()).apply {
                assign(KronoComponents.Year, 2023)
                assign(KronoComponents.Month, 10)
                assign(KronoComponents.Day, 5)
                assign(KronoComponents.Hour, 14)
                assign(KronoComponents.Minute, 30)
                assign(KronoComponents.Second, 45)
            }
        }

        doReturn("2023-10-05T14:30:45 not_a_timezone").`when`(context).text

        val results = mutableListOf<ParsedResult>(result)
        val refinedResults = refiner.invoke(context, results)

        assertThat(refinedResults).hasSize(1)
        val refinedResult = refinedResults[0]
        assertThat(refinedResult.start.isCertain(KronoComponents.Offset)).isFalse
        assertThat(refinedResult.text).isEqualTo("2023-10-05T14:30:45")
    }

    @Test
    fun `PST timezone abbreviation`(@Mock context: ParsingContext) {
        val result = TestParsedResult().apply {
            index = 0
            text = "2023-10-05T14:30:45"
            start = ParsingComponents(ReferenceWithTimezone()).apply {
                assign(KronoComponents.Year, 2023)
                assign(KronoComponents.Month, 10)
                assign(KronoComponents.Day, 5)
                assign(KronoComponents.Hour, 14)
                assign(KronoComponents.Minute, 30)
                assign(KronoComponents.Second, 45)
            }
        }

        doReturn("2023-10-05T14:30:45 PST").`when`(context).text

        val results = mutableListOf<ParsedResult>(result)
        val refinedResults = refiner.invoke(context, results)

        assertThat(refinedResults).hasSize(1)
        val refinedResult = refinedResults[0]
        assertThat(refinedResult.start[KronoComponents.Offset]).isEqualTo(ZoneOffset.of("-08:00").totalSeconds)
        assertThat(refinedResult.text).isEqualTo("2023-10-05T14:30:45 PST")
    }

    @Test
    fun `invalid timezone abbreviation`(@Mock context: ParsingContext) {
        val result = TestParsedResult().apply {
            index = 0
            text = "2023-10-05T14:30:45"
            start = ParsingComponents(ReferenceWithTimezone()).apply {
                assign(KronoComponents.Year, 2023)
                assign(KronoComponents.Month, 10)
                assign(KronoComponents.Day, 5)
                assign(KronoComponents.Hour, 14)
                assign(KronoComponents.Minute, 30)
                assign(KronoComponents.Second, 45)
            }
        }

        doReturn("2023-10-05T14:30:45 XYZ").`when`(context).text

        val results = mutableListOf<ParsedResult>(result)
        val refinedResults = refiner.invoke(context, results)

        assertThat(refinedResults).hasSize(1)
        val refinedResult = refinedResults[0]
        assertThat(refinedResult.start.isCertain(KronoComponents.Offset)).isFalse
        assertThat(refinedResult.text).isEqualTo("2023-10-05T14:30:45")
    }

    @Test
    fun `custom timezone abbreviation`(@Mock context: ParsingContext) {
        val customAbbreviationMap = mapOf(
            "AAA" to ZoneOffset.ofHours(-6),
            "EST" to ZoneOffset.ofHours(-5)
        )
        val refiner = ExtractTimezoneAbbrRefiner(customAbbreviationMap)

        val result = TestParsedResult().apply {
            index = 0
            text = "2023-10-05T14:30:45"
            start = ParsingComponents(ReferenceWithTimezone()).apply {
                assign(KronoComponents.Year, 2023)
                assign(KronoComponents.Month, 10)
                assign(KronoComponents.Day, 5)
                assign(KronoComponents.Hour, 14)
                assign(KronoComponents.Minute, 30)
                assign(KronoComponents.Second, 45)
            }
        }

        doReturn("2023-10-05T14:30:45 AAA").`when`(context).text

        val results = mutableListOf<ParsedResult>(result)
        val refinedResults = refiner.invoke(context, results)

        assertThat(refinedResults).hasSize(1)
        val refinedResult = refinedResults[0]
        assertThat(refinedResult.start[KronoComponents.Offset]).isEqualTo(ZoneOffset.of("-06:00").totalSeconds)
        assertThat(refinedResult.text).isEqualTo("2023-10-05T14:30:45 AAA")
    }

    @Test
    fun `current offset is certain and differs`(@Mock context: ParsingContext) {
        val result = TestParsedResult().apply {
            index = 0
            text = "2023-10-05T14:30:45"
            start = ParsingComponents(ReferenceWithTimezone()).apply {
                assign(KronoComponents.Year, 2023)
                assign(KronoComponents.Month, 10)
                assign(KronoComponents.Day, 5)
                assign(KronoComponents.Hour, 14)
                assign(KronoComponents.Minute, 30)
                assign(KronoComponents.Second, 45)
                assign(KronoComponents.Offset, ZoneOffset.of("-05:00").totalSeconds)
            }
        }

        doReturn("2023-10-05T14:30:45 PST").`when`(context).text

        val results = mutableListOf<ParsedResult>(result)
        val refinedResults = refiner.invoke(context, results)

        assertThat(refinedResults).hasSize(1)
        val refinedResult = refinedResults[0]
        assertThat(refinedResult.start[KronoComponents.Offset]).isEqualTo(ZoneOffset.of("-05:00").totalSeconds)
        assertThat(refinedResult.text).isEqualTo("2023-10-05T14:30:45")
    }

    @Test
    fun `suffix does not match pattern`(@Mock context: ParsingContext) {
        val result = TestParsedResult().apply {
            index = 0
            text = "2023-10-05T14:30:45"
            start = ParsingComponents(ReferenceWithTimezone()).apply {
                assign(KronoComponents.Year, 2023)
                assign(KronoComponents.Month, 10)
                assign(KronoComponents.Day, 5)
                assign(KronoComponents.Hour, 14)
                assign(KronoComponents.Minute, 30)
                assign(KronoComponents.Second, 45)
                imply(KronoComponents.Offset, ZoneOffset.of("-05:00").totalSeconds)
            }
        }

        doReturn("2023-10-05T14:30:45 gmt").`when`(context).text

        val results = mutableListOf<ParsedResult>(result)
        val refinedResults = refiner.invoke(context, results)

        assertThat(refinedResults).hasSize(1)
        val refinedResult = refinedResults[0]
        assertThat(refinedResult.start.isCertain(KronoComponents.Offset)).isFalse
        assertThat(refinedResult.text).isEqualTo("2023-10-05T14:30:45")
    }

    @Test
    fun `start has only date`(@Mock context: ParsingContext) {
        val result = TestParsedResult().apply {
            index = 0
            text = "2023-10-05"
            start = ParsingComponents(ReferenceWithTimezone()).apply {
                assign(KronoComponents.Year, 2023)
                assign(KronoComponents.Month, 10)
                assign(KronoComponents.Day, 5)
            }
        }

        doReturn("2023-10-05 PST").`when`(context).text

        val results = mutableListOf<ParsedResult>(result)
        val refinedResults = refiner.invoke(context, results)

        assertThat(refinedResults).hasSize(1)
        val refinedResult = refinedResults[0]
        assertThat(refinedResult.start.isCertain(KronoComponents.Offset)).isTrue()
        assertThat(refinedResult.text).isEqualTo("2023-10-05 PST")
    }

    @Test
    fun `start has only date and abbreviation is incorrect`(@Mock context: ParsingContext) {
        val result = TestParsedResult().apply {
            index = 0
            text = "2023-10-05"
            start = ParsingComponents(ReferenceWithTimezone()).apply {
                assign(KronoComponents.Year, 2023)
                assign(KronoComponents.Month, 10)
                assign(KronoComponents.Day, 5)
            }
        }

        doReturn("2023-10-05 pst").`when`(context).text

        val results = mutableListOf<ParsedResult>(result)
        val refinedResults = refiner.invoke(context, results)

        assertThat(refinedResults).hasSize(1)
        val refinedResult = refinedResults[0]
        assertThat(refinedResult.start.isCertain(KronoComponents.Offset)).isFalse()
        assertThat(refinedResult.text).isEqualTo("2023-10-05")
    }

    @Test
    fun `end is present and certain`(@Mock context: ParsingContext) {
        val result = TestParsedResult().apply {
            index = 0
            text = "2023-10-05T14:30:45"
            start = ParsingComponents(ReferenceWithTimezone()).apply {
                assign(KronoComponents.Year, 2023)
                assign(KronoComponents.Month, 10)
                assign(KronoComponents.Day, 5)
                assign(KronoComponents.Hour, 14)
                assign(KronoComponents.Minute, 30)
                assign(KronoComponents.Second, 45)
                assign(KronoComponents.Offset, ZoneOffset.of("-05:00").totalSeconds)
            }
            end = ParsingComponents(ReferenceWithTimezone()).apply {
                assign(KronoComponents.Year, 2023)
                assign(KronoComponents.Month, 10)
                assign(KronoComponents.Day, 5)
                assign(KronoComponents.Hour, 15)
                assign(KronoComponents.Minute, 30)
                assign(KronoComponents.Second, 45)
                assign(KronoComponents.Offset, ZoneOffset.of("-05:00").totalSeconds)
            }
        }

        doReturn("2023-10-05T14:30:45 PST").`when`(context).text

        val results = mutableListOf<ParsedResult>(result)
        val refinedResults = refiner.invoke(context, results)

        assertThat(refinedResults).hasSize(1)
        val refinedResult = refinedResults[0]
        assertThat(refinedResult.start[KronoComponents.Offset]).isEqualTo(ZoneOffset.of("-05:00").totalSeconds)
        assertThat(refinedResult.end!![KronoComponents.Offset]).isEqualTo(ZoneOffset.of("-05:00").totalSeconds)
        assertThat(refinedResult.text).isEqualTo("2023-10-05T14:30:45")
    }

    @Test
    fun `end is present and not certain`(@Mock context: ParsingContext) {
        val result = TestParsedResult().apply {
            index = 0
            text = "2023-10-05T14:30:45"
            start = ParsingComponents(ReferenceWithTimezone()).apply {
                assign(KronoComponents.Year, 2023)
                assign(KronoComponents.Month, 10)
                assign(KronoComponents.Day, 5)
                assign(KronoComponents.Hour, 14)
                assign(KronoComponents.Minute, 30)
                assign(KronoComponents.Second, 45)
                imply(KronoComponents.Offset, ZoneOffset.of("-05:00").totalSeconds)
            }
            end = ParsingComponents(ReferenceWithTimezone()).apply {
                assign(KronoComponents.Year, 2023)
                assign(KronoComponents.Month, 10)
                assign(KronoComponents.Day, 5)
                assign(KronoComponents.Hour, 15)
                assign(KronoComponents.Minute, 30)
                assign(KronoComponents.Second, 45)
                imply(KronoComponents.Offset, ZoneOffset.of("-05:00").totalSeconds)
            }
        }

        doReturn("2023-10-05T14:30:45 PST").`when`(context).text

        val results = mutableListOf<ParsedResult>(result)
        val refinedResults = refiner.invoke(context, results)

        assertThat(refinedResults).hasSize(1)
        val refinedResult = refinedResults[0]
        assertThat(refinedResult.start[KronoComponents.Offset]).isEqualTo(ZoneOffset.of("-08:00").totalSeconds)
        assertThat(refinedResult.end!![KronoComponents.Offset]).isEqualTo(ZoneOffset.of("-08:00").totalSeconds)
        assertThat(refinedResult.text).isEqualTo("2023-10-05T14:30:45 PST")
    }

    @Test
    fun `multiple results`(@Mock context: ParsingContext) {
        val result1 = TestParsedResult().apply {
            index = 0
            text = "2023-10-05T14:30:45"
            start = ParsingComponents(ReferenceWithTimezone()).apply {
                assign(KronoComponents.Year, 2023)
                assign(KronoComponents.Month, 10)
                assign(KronoComponents.Day, 5)
                assign(KronoComponents.Hour, 14)
                assign(KronoComponents.Minute, 30)
                assign(KronoComponents.Second, 45)
            }
        }

        val result2 = TestParsedResult().apply {
            index = 24
            text = "2023-10-06T15:30:45"
            start = ParsingComponents(ReferenceWithTimezone()).apply {
                assign(KronoComponents.Year, 2023)
                assign(KronoComponents.Month, 10)
                assign(KronoComponents.Day, 6)
                assign(KronoComponents.Hour, 15)
                assign(KronoComponents.Minute, 30)
                assign(KronoComponents.Second, 45)
            }
        }

        doReturn("2023-10-05T14:30:45 PST 2023-10-06T15:30:45 EST").`when`(context).text

        val results = mutableListOf<ParsedResult>(result1, result2)
        val refinedResults = refiner.invoke(context, results)

        assertThat(refinedResults).hasSize(2)
        val refinedResult1 = refinedResults[0]
        assertThat(refinedResult1.start[KronoComponents.Offset]).isEqualTo(ZoneOffset.of("-08:00").totalSeconds)
        assertThat(refinedResult1.text).isEqualTo("2023-10-05T14:30:45 PST")

        val refinedResult2 = refinedResults[1]
        assertThat(refinedResult2.start[KronoComponents.Offset]).isEqualTo(ZoneOffset.of("-05:00").totalSeconds)
        assertThat(refinedResult2.text).isEqualTo("2023-10-06T15:30:45 EST")
    }
}