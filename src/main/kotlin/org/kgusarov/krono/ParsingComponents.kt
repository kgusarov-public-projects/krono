package org.kgusarov.krono

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.extensions.add
import org.kgusarov.krono.extensions.assignSimilarDate
import org.kgusarov.krono.extensions.assignSimilarTime
import org.kgusarov.krono.extensions.contains
import org.kgusarov.krono.extensions.implySimilarTime
import org.kgusarov.krono.extensions.toNanosInt
import java.time.temporal.ChronoField
import java.util.concurrent.TimeUnit

class ParsingComponents(
    private val reference: ReferenceWithTimezone,
    knownComponents: Map<KronoComponent, Int>? = null,
) : ParsedComponents {
    private val knownValues: MutableMap<KronoComponent, Int> = mutableMapOf()
    private val impliedValues: MutableMap<KronoComponent, Int> = mutableMapOf()
    private val tags: MutableSet<String> = mutableSetOf()

    init {
        if (knownComponents != null) {
            knownValues.putAll(knownComponents)
        }

        imply(ChronoField.DAY_OF_MONTH, reference.instant.dayOfMonth)
        imply(ChronoField.MONTH_OF_YEAR, reference.instant.monthValue)
        imply(ChronoField.YEAR, reference.instant.year)
        imply(ChronoField.HOUR_OF_DAY, 12)
        imply(ChronoField.MINUTE_OF_HOUR, 0)
        imply(ChronoField.SECOND_OF_MINUTE, 0)
        imply(ChronoField.MILLI_OF_SECOND, 0)
    }

    constructor(
        reference: ReferenceWithTimezone,
        vararg entries: Pair<KronoComponent, Int>,
    ) : this(reference, mapOf(*entries))

    fun imply(
        component: KronoComponent,
        value: Int,
    ): ParsingComponents {
        if (component in knownValues) {
            return this
        }
        impliedValues[component] = value
        return this
    }

    override fun get(component: KronoComponent) =
        when (component) {
            in knownValues -> knownValues[component]
            in impliedValues -> impliedValues[component]
            else -> null
        }

    override fun isCertain(component: KronoComponent) = component in knownValues

    override fun tags() = tags.toSet()

    override fun instant(): KronoDate {
        val result = dateWithoutTimezoneAdjustment()
        return reference.withAdjustedTimezone(result, get(ChronoField.OFFSET_SECONDS))
    }

    fun assign(
        component: KronoComponent,
        value: Int,
    ): ParsingComponents {
        this.knownValues[component] = value
        this.impliedValues.remove(component)
        return this
    }

    fun addTag(tag: String): ParsingComponents {
        tags.add(tag)
        return this
    }

    fun addTags(vararg values: String): ParsingComponents {
        tags.addAll(values)
        return this
    }

    fun addTags(values: Set<String>): ParsingComponents {
        tags.addAll(values)
        return this
    }

    fun getCertainComponents(): Array<KronoComponent> = knownValues.keys.toTypedArray()

    fun delete(component: KronoComponent) {
        knownValues.remove(component)
        impliedValues.remove(component)
    }

    fun copy(): ParsingComponents {
        val result = ParsingComponents(reference)
        result.knownValues.putAll(knownValues)
        result.impliedValues.putAll(impliedValues)
        return result
    }

    val onlyDate =
        !isCertain(ChronoField.HOUR_OF_DAY) &&
            !isCertain(ChronoField.MINUTE_OF_HOUR) &&
            !isCertain(ChronoField.SECOND_OF_MINUTE)

    val onlyTime =
        !isCertain(ChronoField.DAY_OF_WEEK) &&
            !isCertain(ChronoField.DAY_OF_MONTH) &&
            !isCertain(ChronoField.MONTH_OF_YEAR)

    val onlyWeekday =
        isCertain(ChronoField.DAY_OF_WEEK) &&
            !isCertain(ChronoField.DAY_OF_MONTH) &&
            !isCertain(ChronoField.MONTH_OF_YEAR)

    val dateWithUnknownYear =
        isCertain(ChronoField.MONTH_OF_YEAR) &&
            !isCertain(ChronoField.YEAR)

    fun isValidDate(): Boolean {
        val date =
            try {
                instant()
            } catch (_: Exception) {
                return false
            }

        if (date.year != get(ChronoField.YEAR)) {
            return false
        }

        if (date.monthValue != get(ChronoField.MONTH_OF_YEAR)) {
            return false
        }

        if (date.dayOfMonth != get(ChronoField.DAY_OF_MONTH)) {
            return false
        }

        if ((get(ChronoField.HOUR_OF_DAY) != null) && (date.hour != get(ChronoField.HOUR_OF_DAY))) {
            return false
        }

        if ((get(ChronoField.MINUTE_OF_HOUR) != null) && (date.minute != get(ChronoField.MINUTE_OF_HOUR))) {
            return false
        }

        return true
    }

    private fun dateWithoutTimezoneAdjustment() =
        KronoDate.of(
            get(ChronoField.YEAR) ?: 0,
            get(ChronoField.MONTH_OF_YEAR) ?: 0,
            get(ChronoField.DAY_OF_MONTH) ?: 0,
            get(ChronoField.HOUR_OF_DAY) ?: 0,
            get(ChronoField.MINUTE_OF_HOUR) ?: 0,
            get(ChronoField.SECOND_OF_MINUTE) ?: 0,
            nanos(),
        )

    private fun nanos(): Int {
        val value = get(ChronoField.MILLI_OF_SECOND) ?: 0
        return TimeUnit.MILLISECONDS.toNanosInt(value)
    }

    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    companion object {
        @JvmStatic
        fun createRelativeFromReference(
            reference: ReferenceWithTimezone,
            vararg fragments: Pair<String, Int>,
        ): ParsingComponents = createRelativeFromReference(reference, mapOf(*fragments))

        @JvmStatic
        fun createRelativeFromReference(
            reference: ReferenceWithTimezone,
            fragments: Map<String, Int> = mapOf(),
        ): ParsingComponents {
            var date = reference.instant
            for (entry in fragments.entries) {
                date = date.add(entry.key, entry.value)
            }

            val components = ParsingComponents(reference)
            if (
                fragments.contains(KronoUnit.Hour) ||
                fragments.contains(KronoUnit.Minute) ||
                fragments.contains(KronoUnit.Second)
            ) {
                date.assignSimilarTime(components)
                date.assignSimilarDate(components)
                if (reference.timezone != null) {
                    val seconds = reference.timezone.rules.getOffset(KronoDate.now()).totalSeconds
                    components.assign(ChronoField.OFFSET_SECONDS, seconds)
                }
            } else {
                date.implySimilarTime(components)
                if (reference.timezone != null) {
                    val seconds = reference.timezone.rules.getOffset(KronoDate.now()).totalSeconds
                    components.assign(ChronoField.OFFSET_SECONDS, seconds)
                }

                if (fragments.contains(KronoUnit.Day)) {
                    date.assignSimilarDate(components)
                } else {
                    if (fragments.contains(KronoUnit.Week)) {
                        components.imply(ChronoField.DAY_OF_WEEK, date.dayOfWeek.value)
                    }

                    components.imply(ChronoField.DAY_OF_MONTH, date.dayOfMonth)
                    if (fragments.contains(KronoUnit.Month)) {
                        components.assign(ChronoField.MONTH_OF_YEAR, date.monthValue)
                        components.assign(ChronoField.YEAR, date.year)
                    } else {
                        components.imply(ChronoField.MONTH_OF_YEAR, date.monthValue)
                        if (fragments.contains(KronoUnit.Year)) {
                            components.assign(ChronoField.YEAR, date.year)
                        } else {
                            components.imply(ChronoField.YEAR, date.year)
                        }
                    }
                }
            }

            return components
        }
    }
}
