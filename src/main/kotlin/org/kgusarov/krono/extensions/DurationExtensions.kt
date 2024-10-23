package org.kgusarov.krono.extensions

import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.math.BigDecimal
import java.time.Duration

fun Duration.multipliedBy(value: Int): Duration = multipliedBy(value.toLong())

fun Duration.multipliedBy(value: BigDecimal): Duration {
    if (value == BigDecimal.ZERO) {
        return Duration.ZERO
    }

    if (value == BigDecimal.ONE) {
        return this
    }

    val bigDecimalSeconds = DurationHelpers.TO_BIG_DECIMAL_SECONDS.invokeExact(this) as BigDecimal
    return DurationHelpers.CREATE.invokeExact(bigDecimalSeconds.multiply(value)) as Duration
}

private object DurationHelpers {
    val TO_BIG_DECIMAL_SECONDS: MethodHandle
    val CREATE: MethodHandle

    init {
        val lookup = MethodHandles.lookup()
        Duration::class.java.getDeclaredMethod("toBigDecimalSeconds").let {
            it.isAccessible = true
            TO_BIG_DECIMAL_SECONDS = lookup.unreflect(it)
        }

        Duration::class.java.getDeclaredMethod("create", BigDecimal::class.java).let {
            it.isAccessible = true
            CREATE = lookup.unreflect(it)
        }
    }
}
