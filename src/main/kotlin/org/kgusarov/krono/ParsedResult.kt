package org.kgusarov.krono

/**
 * Parsed result or final output.
 * Each result object represents a date/time (or date/time-range) mentioning in the input.
 */
interface ParsedResult {
    val refDate: KronoDate
    val index: Int
    val text: String
    var start: ParsedComponents
    var end: ParsedComponents?

    /**
     * Date created from the {@code start}.
     *
     * @return Date Time object
     */
    fun instant(): KronoDate

    /**
     * Get debugging tags
     *
     * @return debugging tags of the parsed component
     */
    fun tags(): Set<String>
}
