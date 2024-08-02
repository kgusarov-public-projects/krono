package org.kgusarov.krono

interface ParsedComponents {
    fun isCertain(component: KronoComponent): Boolean

    operator fun get(component: KronoComponent): Int?

    fun instant(): KronoDate

    fun tags(): Set<String>

    fun copy(): ParsedComponents

    fun imply(
        component: KronoComponent,
        value: Int?,
    ): ParsedComponents

    fun assign(
        component: KronoComponent,
        value: Int?,
    ): ParsedComponents

    fun addTag(tag: String): ParsedComponents

    fun addTags(vararg values: String): ParsedComponents

    fun addTags(values: Set<String>): ParsedComponents

    fun isValidDate(): Boolean

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
