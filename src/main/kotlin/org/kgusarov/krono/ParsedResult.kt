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

    fun <T> copy(): T where T : ParsedResult
}
