package org.kgusarov.krono

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.time.LocalDateTime

@SuppressFBWarnings("EI_EXPOSE_REP", "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
class ParsingResult(
    private val reference: ReferenceWithTimezone,
    override val index: Int,
    override val text: String,
    override val refDate: LocalDateTime,
    override var start: ParsedComponents,
    override var end: ParsedComponents?,
) : ParsedResult {
    constructor(
        reference: ReferenceWithTimezone,
        index: Int,
        text: String,
        start: ParsedComponents?,
        end: ParsedComponents?,
    ) : this(
        reference,
        index,
        text,
        reference.instant,
        start ?: ParsingComponents(reference).addTags(),
        end,
    )

    fun copy() =
        ParsingResult(
            reference,
            index,
            text,
            start.copy(),
            end?.copy(),
        )

    override fun instant() = start.instant()

    override fun tags() = start.tags() + (end?.tags() ?: setOf())
}
