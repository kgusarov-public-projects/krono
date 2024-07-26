package org.kgusarov.krono

data class KronoConfiguration(
    val parsers: Sequence<Parser>,
    val refiners: Sequence<Refiner>,
)
