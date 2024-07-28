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
    operator fun get(component: KronoComponent): Int?

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

    /**
     * Create copy
     *
     * @return Copy of these Parsed components
     */
    fun copy(): ParsedComponents

    /**
     * Imply non-certain component
     *
     * @param component Component to imply
     * @param value Value to imply
     * @return Parsed Components
     */
    fun imply(
        component: KronoComponent,
        value: Int?,
    ): ParsedComponents

    /**
     * Assing certain component
     *
     * @param component Component to assign
     * @param value Value to assign
     * @return Parsed Components
     */
    fun assign(
        component: KronoComponent,
        value: Int?,
    ): ParsedComponents

    /**
     * Add new tag to these components
     *
     * @param tag Tag to add
     * @return Parsed Components
     */
    fun addTag(tag: String): ParsedComponents

    /**
     * Add new tags to these components
     *
     * @param values Tags to add
     * @return Parsed Components
     */
    fun addTags(vararg values: String): ParsedComponents

    /**
     * Add new tags to these components
     *
     * @param values Tags to add
     * @return Parsed Components
     */
    fun addTags(values: Set<String>): ParsedComponents

    val certainComponents: Array<KronoComponent>

    val year: Int?
        get() = get(KronoComponents.Year)

    val certainYear: Boolean
        get() = isCertain(KronoComponents.Year)

    val month: Int?
        get() = get(KronoComponents.Month)

    val certainMonth: Boolean
        get() = isCertain(KronoComponents.Month)

    val day: Int?
        get() = get(KronoComponents.Day)

    val certainDay: Boolean
        get() = isCertain(KronoComponents.Day)

    val hour: Int?
        get() = get(KronoComponents.Hour)

    val certainHour: Boolean
        get() = isCertain(KronoComponents.Hour)

    val minute: Int?
        get() = get(KronoComponents.Minute)

    val certainMinute: Boolean
        get() = isCertain(KronoComponents.Minute)

    val second: Int?
        get() = get(KronoComponents.Second)

    val certainSecond: Boolean
        get() = isCertain(KronoComponents.Second)

    val millisecond: Int?
        get() = get(KronoComponents.Millisecond)

    val certainMillisecond: Boolean
        get() = isCertain(KronoComponents.Millisecond)

    val offset: Int?
        get() = get(KronoComponents.Offset)

    val certainOffset: Boolean
        get() = isCertain(KronoComponents.Offset)

    val weekday: Int?
        get() = get(KronoComponents.Weekday)

    val certainWeekday: Boolean
        get() = isCertain(KronoComponents.Weekday)

    val meridiem: Int?
        get() = get(KronoComponents.Meridiem)

    val certainMeridiem: Boolean
        get() = isCertain(KronoComponents.Meridiem)

    val onlyDate: Boolean
        get() =
            !isCertain(KronoComponents.Hour) &&
                !isCertain(KronoComponents.Minute) &&
                !isCertain(KronoComponents.Second)

    val onlyTime: Boolean
        get() =
            !isCertain(KronoComponents.Weekday) &&
                !isCertain(KronoComponents.Day) &&
                !isCertain(KronoComponents.Month)

    val onlyWeekday: Boolean
        get() =
            isCertain(KronoComponents.Weekday) &&
                !isCertain(KronoComponents.Day) &&
                !isCertain(KronoComponents.Month)

    val dateWithUnknownYear: Boolean
        get() =
            isCertain(KronoComponents.Month) &&
                !isCertain(KronoComponents.Year)
}
