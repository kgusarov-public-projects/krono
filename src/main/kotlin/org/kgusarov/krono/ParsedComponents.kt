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

    fun certainComponents(): Array<KronoComponent>

    fun year() = get(KronoComponents.Year)

    fun certainYear() = isCertain(KronoComponents.Year)

    fun month() = get(KronoComponents.Month)

    fun certainMonth() = isCertain(KronoComponents.Month)

    fun day() = get(KronoComponents.Day)

    fun certainDay() = isCertain(KronoComponents.Day)

    fun hour() = get(KronoComponents.Hour)

    fun certainHour() = isCertain(KronoComponents.Hour)

    fun minute() = get(KronoComponents.Minute)

    fun certainMinute() = isCertain(KronoComponents.Minute)

    fun second() = get(KronoComponents.Second)

    fun certainSecond() = isCertain(KronoComponents.Second)

    fun millisecond() = get(KronoComponents.Millisecond)

    fun certainMillisecond() = isCertain(KronoComponents.Millisecond)

    fun offset() = get(KronoComponents.Offset)

    fun certainOffset() = isCertain(KronoComponents.Offset)

    fun weekday() = get(KronoComponents.Weekday)

    fun certainWeekday() = isCertain(KronoComponents.Weekday)

    fun meridiem() = get(KronoComponents.Meridiem)

    fun certainMeridiem() = isCertain(KronoComponents.Meridiem)

    fun onlyDate() = !certainHour() && !certainMinute() && !certainSecond()

    fun onlyTime() = !certainWeekday() && !certainDay() && !certainMonth()

    fun onlyWeekday() = certainWeekday() && !certainDay() && !certainMonth()

    fun dateWithUnknownYear() = certainMonth() && !certainYear()
}
