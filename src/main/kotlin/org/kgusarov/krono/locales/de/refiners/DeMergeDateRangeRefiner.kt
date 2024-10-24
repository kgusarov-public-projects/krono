package org.kgusarov.krono.locales.de.refiners

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.common.refiners.AbstractMergeDateRangeRefiner

@SuppressFBWarnings("EI_EXPOSE_REP")
class DeMergeDateRangeRefiner : AbstractMergeDateRangeRefiner() {
    override fun patternBetween() = PATTERN

    companion object {
        @JvmStatic
        private val PATTERN = Regex("^\\s*(bis(?:\\s*(?:am|zum))?|-)\\s*$", RegexOption.IGNORE_CASE)
    }
}
