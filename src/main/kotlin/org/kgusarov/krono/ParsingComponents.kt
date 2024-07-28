package org.kgusarov.krono

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.extensions.add
import org.kgusarov.krono.extensions.assignSimilarDate
import org.kgusarov.krono.extensions.assignSimilarTime
import org.kgusarov.krono.extensions.contains
import org.kgusarov.krono.extensions.implySimilarTime
import org.kgusarov.krono.extensions.toNanosInt
import java.util.concurrent.TimeUnit

@SuppressFBWarnings("EI_EXPOSE_REP")
class ParsingComponents(
    private val reference: ReferenceWithTimezone,
    knownComponents: Map<KronoComponent, Int>? = null,
) : ParsedComponents {
    private val knownValues: MutableMap<KronoComponent, Int?> = mutableMapOf()
    private val impliedValues: MutableMap<KronoComponent, Int?> = mutableMapOf()
    private val tags: MutableSet<String> = mutableSetOf()

    init {
        if (knownComponents != null) {
            knownValues.putAll(knownComponents)
        }

        imply(KronoComponents.Day, reference.instant.dayOfMonth)
        imply(KronoComponents.Month, reference.instant.monthValue)
        imply(KronoComponents.Year, reference.instant.year)
        imply(KronoComponents.Hour, 12)
        imply(KronoComponents.Minute, 0)
        imply(KronoComponents.Second, 0)
        imply(KronoComponents.Millisecond, 0)
    }

    constructor(
        reference: ReferenceWithTimezone,
        vararg entries: Pair<KronoComponent, Int>,
    ) : this(reference, mapOf(*entries))

    override fun imply(
        component: KronoComponent,
        value: Int?,
    ): ParsedComponents {
        if (component in knownValues) {
            return this
        }
        impliedValues[component] = value
        return this
    }

    override operator fun get(component: KronoComponent) =
        when (component) {
            in knownValues -> knownValues[component]
            in impliedValues -> impliedValues[component]
            else -> null
        }

    override fun isCertain(component: KronoComponent) = component in knownValues

    override fun tags() = tags.toSet()

    override fun instant(): KronoDate {
        val result = dateWithoutTimezoneAdjustment()
        return reference.withAdjustedTimezone(result, get(KronoComponents.Offset))
    }

    override fun assign(
        component: KronoComponent,
        value: Int?,
    ): ParsedComponents {
        this.knownValues[component] = value
        this.impliedValues.remove(component)
        return this
    }

    override fun addTag(tag: String): ParsedComponents {
        tags.add(tag)
        return this
    }

    override fun addTags(vararg values: String): ParsedComponents {
        tags.addAll(values)
        return this
    }

    override fun addTags(values: Set<String>): ParsedComponents {
        tags.addAll(values)
        return this
    }

    override val certainComponents: Array<KronoComponent> = knownValues.keys.toTypedArray()

    fun delete(component: KronoComponent) {
        knownValues.remove(component)
        impliedValues.remove(component)
    }

    override fun copy(): ParsedComponents {
        val result = ParsingComponents(reference.copy())
        result.knownValues.putAll(knownValues)
        result.impliedValues.putAll(impliedValues)
        return result
    }

    fun isValidDate(): Boolean {
        val date =
            try {
                instant()
            } catch (_: Exception) {
                return false
            }

        if (date.year != get(KronoComponents.Year)) {
            return false
        }

        if (date.monthValue != get(KronoComponents.Month)) {
            return false
        }

        if (date.dayOfMonth != get(KronoComponents.Day)) {
            return false
        }

        if ((get(KronoComponents.Hour) != null) && (date.hour != get(KronoComponents.Hour))) {
            return false
        }

        if ((get(KronoComponents.Minute) != null) && (date.minute != get(KronoComponents.Minute))) {
            return false
        }

        return true
    }

    private fun dateWithoutTimezoneAdjustment() =
        KronoDate.of(
            get(KronoComponents.Year) ?: 0,
            get(KronoComponents.Month) ?: 0,
            get(KronoComponents.Day) ?: 0,
            get(KronoComponents.Hour) ?: 0,
            get(KronoComponents.Minute) ?: 0,
            get(KronoComponents.Second) ?: 0,
            nanos(),
        )

    private fun nanos(): Int {
        val value = get(KronoComponents.Millisecond) ?: 0
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
                    components.assign(KronoComponents.Offset, seconds)
                }
            } else {
                date.implySimilarTime(components)
                if (reference.timezone != null) {
                    val seconds = reference.timezone.rules.getOffset(KronoDate.now()).totalSeconds
                    components.assign(KronoComponents.Offset, seconds)
                }

                if (fragments.contains(KronoUnit.Day)) {
                    date.assignSimilarDate(components)
                } else {
                    if (fragments.contains(KronoUnit.Week)) {
                        components.imply(KronoComponents.Weekday, date.dayOfWeek.value)
                    }

                    components.imply(KronoComponents.Day, date.dayOfMonth)
                    if (fragments.contains(KronoUnit.Month)) {
                        components.assign(KronoComponents.Month, date.monthValue)
                        components.assign(KronoComponents.Year, date.year)
                    } else {
                        components.imply(KronoComponents.Month, date.monthValue)
                        if (fragments.contains(KronoUnit.Year)) {
                            components.assign(KronoComponents.Year, date.year)
                        } else {
                            components.imply(KronoComponents.Year, date.year)
                        }
                    }
                }
            }

            return components
        }
    }
}
