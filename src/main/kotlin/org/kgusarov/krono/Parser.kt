package org.kgusarov.krono

sealed interface ParserResult {
    operator fun invoke(
        context: ParsingContext,
        index: Int,
        input: TextOrEndIndexInput,
    ): ParsedResult
}

private data class RawResult(private val value: ParsedResult) : ParserResult {
    override fun invoke(
        context: ParsingContext,
        index: Int,
        input: TextOrEndIndexInput,
    ) = value
}

private data class ParsingComponentsResult(private val value: ParsedComponents) : ParserResult {
    override fun invoke(
        context: ParsingContext,
        index: Int,
        input: TextOrEndIndexInput,
    ): ParsedResult {
        val result = context.createParsingResult(index, input)
        result.start = value
        return result
    }
}

private data class ComponentsInputResult(private val value: ComponentsInput?) : ParserResult {
    override fun invoke(
        context: ParsingContext,
        index: Int,
        input: TextOrEndIndexInput,
    ): ParsedResult = context.createParsingResult(index, input, value)
}

object ParserResultFactory {
    operator fun invoke(value: ParsedResult): ParserResult = RawResult(value)

    operator fun invoke(value: ParsedComponents): ParserResult = ParsingComponentsResult(value)

    operator fun invoke(value: ComponentsInput?): ParserResult = ComponentsInputResult(value)
}

interface Parser {
    fun pattern(context: ParsingContext): Regex

    operator fun invoke(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult?
}
