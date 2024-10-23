package org.kgusarov.krono

interface ParsedResult {
    val refDate: KronoDate
    var index: Int
    var text: String
    var start: ParsedComponents
    var end: ParsedComponents?
    val reference: ReferenceWithTimezone

    fun instant(): KronoDate

    fun tags(): Set<String>

    fun <T> addTag(tag: String): T where T : ParsedResult = addTags(setOf(tag))

    @Suppress("UNCHECKED_CAST")
    fun <T> addTags(tags: Set<String>): T where T : ParsedResult {
        start.addTags(tags)
        if (end != null) {
            end!!.addTags(tags)
        }
        return this as T
    }

    fun <T> addTags(vararg tags: String): T where T : ParsedResult = addTags(tags.toSet())

    fun <T> copy(): T where T : ParsedResult
}
