package org.kgusarov.krono

/**
 * A collection of parsed date/time components (e.g. day, hour, minute, ..., etc).
 *
 * Each parsed component has three different levels of certainty.
 * - *Certain* (or *Known*): The component is directly mentioned and parsed.
 * - *Implied*: The component is not directly mentioned, but implied by other parsed information.
 * - *Unknown*: Completely no mention of the component.
 */
interface ParsedComponents {
    /**
     * Check the component certainly if the component is *Certain* (or *Known*)
     *
     * @param component - component to check
     * @return {@code true} if component is certain
     */
    fun isCertain(component: KronoComponent): Boolean

    /**
     * Get the component value for either *Certain* or *Implied* value.
     *
     * @param component - component to get
     * @return requested component or {@code null}
     */
    fun get(component: KronoComponent): Int?

    /**
     * Get referenced instant
     *
     * @return Date time represented by this result
     */
    fun instant(): KronoDate

    /**
     * Get debugging tags
     *
     * @return debugging tags of the parsed component
     */
    fun tags(): Set<String>
}
