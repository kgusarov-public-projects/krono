package org.kgusarov.krono

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

typealias AsyncDebugBlock = () -> String

fun interface DebugHandler {
    operator fun invoke(block: AsyncDebugBlock)
}

@SuppressFBWarnings("BC_BAD_CAST_TO_ABSTRACT_COLLECTION")
class BufferedDebugHandler : DebugHandler {
    private val buffer: MutableList<AsyncDebugBlock> = mutableListOf()

    override operator fun invoke(block: AsyncDebugBlock) {
        buffer.add(block)
    }

    fun execute(): Array<String> {
        val result = buffer.map { it() }.toTypedArray()
        buffer.clear()
        return result
    }
}
