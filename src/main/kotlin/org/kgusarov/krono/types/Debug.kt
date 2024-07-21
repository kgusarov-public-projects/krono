package org.kgusarov.krono.types

typealias AsyncDebugBlock = () -> Any?

fun interface DebugHandler {
    fun debug(block: AsyncDebugBlock)
}

class BufferedDebugHandler : DebugHandler {
    private val buffer: MutableList<AsyncDebugBlock> = mutableListOf()

    override fun debug(block: AsyncDebugBlock) {
        buffer.add(block)
    }

    fun execute(): Array<Any?> {
        val result = buffer.map { it() }.toTypedArray()
        buffer.clear()
        return result
    }
}
