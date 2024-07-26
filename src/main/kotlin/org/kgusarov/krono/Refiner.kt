package org.kgusarov.krono

interface Refiner {
    operator fun invoke(
        context: ParsingContext,
        results: Array<ParsingResult>,
    ): Array<ParsingResult>
}
