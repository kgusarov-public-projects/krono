package org.kgusarov.krono

sealed interface ComponentsInput {
    operator fun invoke(reference: ReferenceWithTimezone): ParsedComponents
}

private data class MapComponentsInput(private val value: Map<KronoComponent, Int>?) : ComponentsInput {
    override fun invoke(reference: ReferenceWithTimezone) = ParsingComponents(reference, value)
}

private data class ParsedComponentsInput(private val value: ParsedComponents) : ComponentsInput {
    override fun invoke(reference: ReferenceWithTimezone) = value
}

sealed interface RefDateInput {
    operator fun invoke(): ReferenceWithTimezone
}

private data class KronoRefDateInput(private val value: KronoDate) : RefDateInput {
    override fun invoke() = ReferenceWithTimezone(value)
}

private data class ParsingReferenceDateInput(private val value: ParsingReference) : RefDateInput {
    override fun invoke() = ReferenceWithTimezone(value)
}

sealed interface TextOrEndIndexInput {
    operator fun invoke(
        index: Int,
        from: String,
    ): String
}

private data class TextInput(private val value: String) : TextOrEndIndexInput {
    override fun invoke(
        index: Int,
        from: String,
    ) = value
}

private data class EndIndexInput(private val value: Int) : TextOrEndIndexInput {
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

object ComponentsInputFactory {
    operator fun invoke(value: ParsedComponents): ComponentsInput = ParsedComponentsInput(value)

    operator fun invoke(value: Map<KronoComponent, Int>?): ComponentsInput = MapComponentsInput(value)
}

object TextOrEndIndexInputFactory {
    operator fun invoke(value: Int): TextOrEndIndexInput = EndIndexInput(value)

    operator fun invoke(value: String): TextOrEndIndexInput = TextInput(value)
}

data class ParsingContext(
    val text: String,
    private val option: ParsingOption,
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
