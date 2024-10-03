package org.kgusarov.krono.common.refiners

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.ParsingComponents
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.ReferenceWithTimezone
import org.kgusarov.krono.TestParsedResult
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class MergeWeekdayComponentRefinerTest {
    private val refiner = MergeWeekdayComponentRefiner()

    @Test
    internal fun `should merge results when weekday then normal date`(@Mock context: ParsingContext) {
        val currentResult = TestParsedResult().apply {
            start.assign(KronoComponents.Weekday, 3)
        }

        val nextResult = TestParsedResult().apply {
            start.assign(KronoComponents.Day, 5)
        }

        val textBetween = ", "
        val shouldMerge = refiner.shouldMergeResults(textBetween, currentResult, nextResult, context)

        assertThat(shouldMerge).isTrue
    }

    @Test
    internal fun `should not merge results when not weekday then normal date`(@Mock context: ParsingContext) {
        val currentResult = TestParsedResult().apply {
            start.assign(KronoComponents.Day, 3)
        }

        val nextResult = TestParsedResult().apply {
            start.assign(KronoComponents.Day, 5)
        }

        val textBetween = ", "
        val shouldMerge = refiner.shouldMergeResults(textBetween, currentResult, nextResult, context)

        assertThat(shouldMerge).isFalse
    }

    @Test
    internal fun `merge results correctly`(@Mock context: ParsingContext) {
        val currentResult = TestParsedResult().apply {
            index = 5
            text = "Wednesday"
            start.assign(KronoComponents.Weekday, 3)
        }

        val nextResult = TestParsedResult(
            end = ParsingComponents(ReferenceWithTimezone())
        ).apply {
            index = 10
            text = "October 5"
            start.assign(KronoComponents.Day, 5)
        }

        val textBetween = ", "
        val mergedResult = refiner.mergeResults(textBetween, currentResult, nextResult, context)

        assertThat(mergedResult.index).isEqualTo(5)
        assertThat(mergedResult.text).isEqualTo("Wednesday, October 5")
        assertThat(mergedResult.start[KronoComponents.Weekday]).isEqualTo(3)
        assertThat(mergedResult.start[KronoComponents.Day]).isEqualTo(5)

        assertThat(mergedResult.end!![KronoComponents.Weekday]).isEqualTo(3)
    }
}
