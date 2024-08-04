package org.kgusarov.krono

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

@SuppressFBWarnings("EI_EXPOSE_REP2")
class RegExpMatchArray(
    var index: Int?,
    private val matches: Array<String?>,
) {
    val length = matches.size

    constructor(matchResult: MatchResult) : this(
        matchResult.range.first,
        matchResult.groupValues.toTypedArray(),
    )

    operator fun get(index: Int): String? {
        return matches.getOrNull(index)
    }

    fun getInt(index: Int): Int {
        val s = this[index]
        return if (s.isNullOrEmpty()) 0 else s.toInt()
    }

    fun getIntOrNull(index: Int): Int? {
        val s = this[index]
        return if (s.isNullOrEmpty()) null else s.toInt()
    }

    operator fun set(
        index: Int,
        value: String?,
    ) {
        matches[index] = value
    }
}
