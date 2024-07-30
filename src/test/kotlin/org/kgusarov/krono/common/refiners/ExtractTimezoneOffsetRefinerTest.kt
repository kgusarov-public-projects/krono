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

@ExtendWith(MockitoExtension::class)
class ExtractTimezoneOffsetRefinerTest {

    private val refiner = ExtractTimezoneOffsetRefiner()

    @Test
    fun `extract timezone offset`(@Mock context: ParsingContext) {
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

        doReturn("2023-10-05T14:30:45+02:00").`when`(context).text

        val results = mutableListOf<ParsedResult>(result)
        val refinedResults = refiner.invoke(context, results)

        assertThat(refinedResults).hasSize(1)
        val refinedResult = refinedResults[0]
        assertThat(refinedResult.start[KronoComponents.Offset]).isEqualTo(7200)
        assertThat(refinedResult.text).isEqualTo("2023-10-05T14:30:45+02:00")
    }

    @Test
    fun `do not extract invalid timezone offset`(@Mock context: ParsingContext) {
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

        doReturn("2023-10-05T14:30:45+15:00").`when`(context).text

        val results = mutableListOf<ParsedResult>(result)
        val refinedResults = refiner.invoke(context, results)

        assertThat(refinedResults).hasSize(1)
        val refinedResult = refinedResults[0]
        assertThat(refinedResult.start.isCertain(KronoComponents.Offset)).isFalse
        assertThat(refinedResult.text).isEqualTo("2023-10-05T14:30:45")
    }

    @Test
    fun `extract negative timezone offset`(@Mock context: ParsingContext) {
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

        doReturn("2023-10-05T14:30:45-02:00").`when`(context).text

        val results = mutableListOf<ParsedResult>(result)
        val refinedResults = refiner.invoke(context, results)

        assertThat(refinedResults).hasSize(1)
        val refinedResult = refinedResults[0]
        assertThat(refinedResult.start[KronoComponents.Offset]).isEqualTo(-7200)
        assertThat(refinedResult.text).isEqualTo("2023-10-05T14:30:45-02:00")
    }

    @Test
    fun `do not extract timezone offset when already certain`(@Mock context: ParsingContext) {
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
                assign(KronoComponents.Offset, 7200)
            }
        }

        val results = mutableListOf<ParsedResult>(result)
        val refinedResults = refiner.invoke(context, results)

        assertThat(refinedResults).hasSize(1)
        val refinedResult = refinedResults[0]
        assertThat(refinedResult.start[KronoComponents.Offset]).isEqualTo(7200)
        assertThat(refinedResult.text).isEqualTo("2023-10-05T14:30:45")
    }

    @Test
    fun `extract timezone offset with different format`(@Mock context: ParsingContext) {
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
            end = ParsingComponents(ReferenceWithTimezone())
        }

        doReturn("2023-10-05T14:30:45 GMT+02:00").`when`(context).text

        val results = mutableListOf<ParsedResult>(result)
        val refinedResults = refiner.invoke(context, results)

        assertThat(refinedResults).hasSize(1)
        val refinedResult = refinedResults[0]
        assertThat(refinedResult.start[KronoComponents.Offset]).isEqualTo(7200)
        assertThat(refinedResult.end!![KronoComponents.Offset]).isEqualTo(7200)
        assertThat(refinedResult.text).isEqualTo("2023-10-05T14:30:45 GMT+02:00")
    }

    @Test
    fun `extract timezone offset at limit`(@Mock context: ParsingContext) {
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

        doReturn("2023-10-05T14:30:45+14:00").`when`(context).text

        val results = mutableListOf<ParsedResult>(result)
        val refinedResults = refiner.invoke(context, results)

        assertThat(refinedResults).hasSize(1)
        val refinedResult = refinedResults[0]
        assertThat(refinedResult.start[KronoComponents.Offset]).isEqualTo(50400)
        assertThat(refinedResult.text).isEqualTo("2023-10-05T14:30:45+14:00")
    }
}