package org.kgusarov.krono

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

sealed interface ComponentsInput {
    operator fun invoke(reference: ReferenceWithTimezone): ParsedComponents
}

@SuppressFBWarnings("EI_EXPOSE_REP", "EI_EXPOSE_REP2")
internal data class MapComponentsInput(
    val value: Map<KronoComponent, Int>?,
) : ComponentsInput {
    override fun invoke(reference: ReferenceWithTimezone) = ParsingComponents(reference, value)
}

internal data class ParsedComponentsInput(private val value: ParsedComponents) : ComponentsInput {
    override fun invoke(reference: ReferenceWithTimezone) = value
}

sealed interface RefDateInput {
    operator fun invoke(): ReferenceWithTimezone
}

internal data class KronoRefDateInput(private val value: KronoDate) : RefDateInput {
    override fun invoke() = ReferenceWithTimezone(value)
}

internal data class ParsingReferenceDateInput(private val value: ParsingReference) : RefDateInput {
    override fun invoke() = ReferenceWithTimezone(value)
}

sealed interface TextOrEndIndexInput {
    operator fun invoke(
        index: Int,
        from: String,
    ): String
}

internal data class TextInput(private val value: String) : TextOrEndIndexInput {
    override fun invoke(
        index: Int,
        from: String,
    ) = value
}

internal data class EndIndexInput(private val value: Int) : TextOrEndIndexInput {
    override fun invoke(
        index: Int,
        from: String,
    ) = from.substring(index, value)
}

object RefDateInputFactory {
    operator fun invoke(value: KronoDate): RefDateInput = KronoRefDateInput(value)

    operator fun invoke(value: ParsingReference): RefDateInput = ParsingReferenceDateInput(value)

    operator fun invoke(value: String): RefDateInput {
        val date = KronoDate.parse(value)
        return KronoRefDateInput(date)
    }
}

@SuppressFBWarnings("BC_BAD_CAST_TO_ABSTRACT_COLLECTION")
object ComponentsInputFactory {
    operator fun invoke(value: ParsedComponents): ComponentsInput = ParsedComponentsInput(value)

    operator fun invoke(value: Map<KronoComponent, Int>?): ComponentsInput = MapComponentsInput(value)

    operator fun invoke(vararg value: Pair<KronoComponent, Int?>): ComponentsInput =
        MapComponentsInput(
            mapOf(
                *value.mapNotNull { (component, value) ->
                    value?.let { component to it }
                }.toTypedArray(),
            ),
        )
}

object TextOrEndIndexInputFactory {
    operator fun invoke(value: Int): TextOrEndIndexInput = EndIndexInput(value)

    operator fun invoke(value: String): TextOrEndIndexInput = TextInput(value)
}

data class ParsingContext(
    val text: String,
    val option: ParsingOption,
    val reference: ReferenceWithTimezone,
) : DebugHandler {
    val instant = reference.instant

    constructor(
        text: String,
        refDate: RefDateInput,
        option: ParsingOption?,
    ) : this(
        text,
        option ?: ParsingOption(),
        refDate(),
    )

    constructor(
        text: String,
        option: ParsingOption? = null,
    ) : this(
        text,
        RefDateInputFactory(KronoDate.now()),
        option,
    )

    fun createParsingComponents(): ParsedComponents = createParsingComponents(ComponentsInputFactory(mapOf()))

    fun createParsingComponents(components: ComponentsInput): ParsedComponents = components(reference)

    fun createParsingResult(
        index: Int,
        textOrEndIndex: TextOrEndIndexInput,
        startComponents: ComponentsInput? = null,
        endComponents: ComponentsInput? = null,
    ): ParsedResult {
        val txt = textOrEndIndex(index, text)
        val start = if (startComponents != null) createParsingComponents(startComponents) else null
        val end = if (endComponents != null) createParsingComponents(endComponents) else null

        return ParsingResult(reference, index, txt, start, end)
    }

    override fun invoke(block: AsyncDebugBlock) {
        option.debug(block)
    }
}
