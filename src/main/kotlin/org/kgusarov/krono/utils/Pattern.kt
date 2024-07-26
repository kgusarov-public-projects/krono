package org.kgusarov.krono.utils

internal fun extractTerms(dictionary: Map<String, *>): Array<String> = dictionary.keys.toTypedArray()

internal fun matchAnyPattern(dictionary: Map<String, *>): String {
    val joinedTerms =
        extractTerms(dictionary)
            .toSortedSet { a, b -> b.length - a.length }
            .joinToString("|")
            .replace("\\.".toRegex(), "\\.")

    return "(?:$joinedTerms)"
}
