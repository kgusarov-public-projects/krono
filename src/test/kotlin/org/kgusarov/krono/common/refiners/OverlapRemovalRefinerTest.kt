package org.kgusarov.krono.common.refiners

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kgusarov.krono.ParsedResult
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.TestParsedResult
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class OverlapRemovalRefinerTest {

    private val refiner = OverlapRemovalRefiner()

    @Test
    internal fun `should remove overlapping results out of order`(@Mock context: ParsingContext) {
        val result1 = TestParsedResult().apply {
            index = 0
            text = "2023-10-05"
        }
        val result2 = TestParsedResult().apply {
            index = 5
            text = "10-05"
        }
        val result3 = TestParsedResult().apply {
            index = 8
            text = "05"
        }

        val results = mutableListOf<ParsedResult>(result2, result1, result3)
        val refinedResults = refiner.invoke(context, results)

        assertThat(refinedResults).hasSize(1)
        assertThat(refinedResults[0]).isEqualTo(result1)
    }

    @Test
    internal fun `should remove overlapping results in order`(@Mock context: ParsingContext) {
        val result1 = TestParsedResult().apply {
            index = 0
            text = "2023-10-05"
        }
        val result2 = TestParsedResult().apply {
            index = 5
            text = "10-05"
        }
        val result3 = TestParsedResult().apply {
            index = 8
            text = "05"
        }

        val results = mutableListOf<ParsedResult>(result1, result2, result3)
        val refinedResults = refiner.invoke(context, results)

        assertThat(refinedResults).hasSize(1)
        assertThat(refinedResults[0]).isEqualTo(result1)
    }

    @Test
    internal fun `should keep non-overlapping results`(@Mock context: ParsingContext) {
        val result1 = TestParsedResult().apply {
            index = 0
            text = "2023-10-05"
        }
        val result2 = TestParsedResult().apply {
            index = 11
            text = "2023-10-06"
        }

        val results = mutableListOf<ParsedResult>(result1, result2)
        val refinedResults = refiner.invoke(context, results)

        assertThat(refinedResults).hasSize(2)
        assertThat(refinedResults).containsExactly(result1, result2)
    }

    @Test
    internal fun `should handle single result`(@Mock context: ParsingContext) {
        val result = TestParsedResult().apply {
            index = 0
            text = "2023-10-05"
        }

        val results = mutableListOf<ParsedResult>(result)
        val refinedResults = refiner.invoke(context, results)

        assertThat(refinedResults).hasSize(1)
        assertThat(refinedResults[0]).isEqualTo(result)
    }

    @Test
    internal fun `should handle empty results`(@Mock context: ParsingContext) {
        val results = mutableListOf<ParsedResult>()
        val refinedResults = refiner.invoke(context, results)

        assertThat(refinedResults).isEmpty()
    }
}
