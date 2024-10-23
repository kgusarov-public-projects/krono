package org.kgusarov.krono.utils

internal fun extractTerms(dictionary: Map<String, *>): Array<String> = dictionary.keys.toTypedArray()

internal fun matchAnyPattern(dictionary: Map<String, *>): String {
    val joinedTerms =
        extractTerms(dictionary)
            .sortedByDescending { it.length }
            .joinToString("|")
            .replace(".", "\\.")

    return "(?:$joinedTerms)"
}

internal fun repeatedTimeUnitPattern(
    prefix: String,
    singleTimeunitPattern: String,
    connectorPattern: String = "\\s{0,5},?\\s{0,5}",
): String {
    val singleTimeunitPatternNoCapture = singleTimeunitPattern.replace(Regex("\\((?!\\?)"), "(?:")
    return "$prefix$singleTimeunitPatternNoCapture(?:$connectorPattern$singleTimeunitPatternNoCapture){0,10}"
}
