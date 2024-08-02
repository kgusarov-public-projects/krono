package org.kgusarov.krono

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.common.parsers.ISOFormatParser
import org.kgusarov.krono.common.refiners.ExtractTimezoneAbbrRefiner
import org.kgusarov.krono.common.refiners.ExtractTimezoneOffsetRefiner
import org.kgusarov.krono.common.refiners.ForwardDateRefiner
import org.kgusarov.krono.common.refiners.MergeWeekdayComponentRefiner
import org.kgusarov.krono.common.refiners.OverlapRemovalRefiner
import org.kgusarov.krono.common.refiners.UnlikelyFormatFilter

@SuppressFBWarnings("EI_EXPOSE_REP", "EI_EXPOSE_REP2")
data class KronoConfiguration(
    val parsers: MutableList<Parser>,
    val refiners: MutableList<Refiner>,
)

fun includeCommonConfiguration(
    configuration: KronoConfiguration,
    strictMode: Boolean = false,
): KronoConfiguration {
    configuration.parsers.addFirst(ISOFormatParser())

    configuration.refiners.addFirst(MergeWeekdayComponentRefiner())
    configuration.refiners.addFirst(ExtractTimezoneOffsetRefiner())
    configuration.refiners.addFirst(OverlapRemovalRefiner())

    configuration.refiners.addLast(ExtractTimezoneAbbrRefiner())
    configuration.refiners.addLast(OverlapRemovalRefiner())
    configuration.refiners.addLast(ForwardDateRefiner())
    configuration.refiners.addLast(UnlikelyFormatFilter(strictMode))

    return configuration
}
