package org.kgusarov.krono

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.time.ZoneId

@SuppressFBWarnings("EI_EXPOSE_REP", "EI_EXPOSE_REP2")
data class ParsingOption(
    val forwardDate: Boolean = false,
    val abbreviationMap: Map<String, ZoneId> = TimezoneAbbreviations.TIMEZONE_ABBR_MAP,
    val debug: DebugHandler =
        DebugHandler {
            // Nothing to do by default
        },
)
