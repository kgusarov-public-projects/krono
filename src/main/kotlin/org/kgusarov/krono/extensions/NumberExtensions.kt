package org.kgusarov.krono.extensions

operator fun Int?.compareTo(other: Int): Int = (this ?: 0).compareTo(other)

operator fun Int?.plus(other: Int): Int = (this ?: 0) + other

operator fun Int?.minus(other: Int): Int = (this ?: 0) - other

operator fun Int?.unaryMinus(): Int = -(this ?: 0)
