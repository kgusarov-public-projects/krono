package org.kgusarov.krono.common.refiners

import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.KronoDate
import org.kgusarov.krono.ParsedComponents
import org.kgusarov.krono.ParsedResult
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.Refiner
import org.kgusarov.krono.extensions.implySimilarDate

class ForwardDateRefiner : Refiner {
    override fun invoke(
        context: ParsingContext,
        results: MutableList<ParsedResult>,
    ): MutableList<ParsedResult> {
        if (!context.option.forwardDate) {
            return results
        }

        results.forEach {
            var refMoment = it.refDate
            if (it.start.onlyTime() && refMoment.isAfter(it.start.instant())) {
                refMoment = refMoment.plusDays(1L)
                refMoment.implySimilarDate(it.start)

                if (it.end != null && it.end!!.onlyTime()) {
                    refMoment = refMoment.plusDays(1L)
                    refMoment.implySimilarDate(it.end!!)
                }
            }

            if (it.start.onlyWeekday() && refMoment.isAfter(it.start.instant())) {
                refMoment = adjustDayOfWeek(it.start, refMoment)
                refMoment.implySimilarDate(it.start)

                context {
                    "Forward weekly adjusted for $it (${it.start})"
                }

                if (it.end != null && it.end!!.onlyWeekday()) {
                    refMoment = adjustDayOfWeek(it.end!!, refMoment)
                    refMoment.implySimilarDate(it.end!!)

                    context {
                        "Forward weekly adjusted for $it (${it.end})"
                    }
                }
            }

            if (it.start.dateWithUnknownYear() && refMoment.isAfter(it.start.instant())) {
                for (i in 0 until 3) {
                    if (refMoment.isAfter(it.start.instant())) {
                        it.start.imply(KronoComponents.Year, it.start[KronoComponents.Year]!! + 1)
                        context {
                            "Forward yearly adjusted for $it (${it.start})"
                        }
                    }

                    if (it.end != null && it.end!!.dateWithUnknownYear() && refMoment.isAfter(it.end!!.instant())) {
                        it.end!!.imply(KronoComponents.Year, it.end!![KronoComponents.Year]!! + 1)
                        context {
                            "Forward yearly adjusted for $it (${it.end})"
                        }
                    }
                }
            }
        }

        return results
    }

    private fun adjustDayOfWeek(
        it: ParsedComponents,
        refMoment: KronoDate,
    ): KronoDate {
        if (it.weekday() == refMoment.dayOfWeek.value) {
            return refMoment.plusWeeks(1L)
        }

        var updatedRefMoment = refMoment
        while (it.weekday() != updatedRefMoment.dayOfWeek.value) {
            updatedRefMoment = updatedRefMoment.plusDays(1L)
        }

        return updatedRefMoment
    }
}
