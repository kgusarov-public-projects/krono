package org.kgusarov.krono

interface Refiner {
    operator fun invoke(
        context: ParsingContext,
        results: MutableList<ParsedResult>,
    ): MutableList<ParsedResult>
}
