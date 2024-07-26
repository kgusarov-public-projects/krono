package org.kgusarov.krono

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class ParsingResultTest {
    @Test
    fun `create and manipulate parsing results`() {
        val reference = ReferenceWithTimezone()
        val text = "1 - 2 hour later"

        val startComponents = ParsingComponents
            .createRelativeFromReference(reference, "hour" to 1)
            .addTag("custom/testing_start_component_tag")

        val endComponents = ParsingComponents
            .createRelativeFromReference(reference, "hour" to 2)
            .addTag("custom/testing_end_component_tag")

        val result = ParsingResult(reference, 0, text, startComponents, endComponents)

        with(result) {
            Assertions.assertThat(instant()).isEqualTo(startComponents.instant())

            Assertions.assertThat(tags()).containsExactlyInAnyOrder(
                "custom/testing_start_component_tag",
                "custom/testing_end_component_tag"
            )
        }
    }
}
