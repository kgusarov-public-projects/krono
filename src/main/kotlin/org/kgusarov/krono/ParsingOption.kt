package org.kgusarov.krono

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

@SuppressFBWarnings("EI_EXPOSE_REP", "EI_EXPOSE_REP2")
data class ParsingOption(
    /**
     * To parse only forward dates (the results should be after the reference date).
     * This effects date/time implication (e.g. weekday or time mentioning)
     */
    val forwardDate: Boolean = true,
    /**
     * Internal debug event handler.
     */
    val debug: DebugHandler =
        DebugHandler {
            // Nothing to do by default
        },
)
