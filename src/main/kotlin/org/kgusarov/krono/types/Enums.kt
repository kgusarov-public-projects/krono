package org.kgusarov.krono.types

fun interface IntEnum {
    operator fun invoke(): Int
}

enum class Meridiem(private val `val`: Int) : IntEnum {
    AM(0),
    PM(1),
    ;

    override operator fun invoke(): Int = `val`
}
