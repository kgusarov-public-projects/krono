package org.kgusarov.krono

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

@SuppressFBWarnings("EI_EXPOSE_REP", "EI_EXPOSE_REP2")
data class ParsingOption(
    val forwardDate: Boolean = false,
    val debug: DebugHandler =
        DebugHandler {
            // Nothing to do by default
        },
)
