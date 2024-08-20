package org.kgusarov.krono.locales.pt.refiners

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.common.refiners.AbstractMergeDateRangeRefiner

@Suppress("RegExpUnnecessaryNonCapturingGroup")
@SuppressFBWarnings("EI_EXPOSE_REP")
class PtMergeDateRangeRefiner : AbstractMergeDateRangeRefiner() {
    override fun patternBetween() = PATTERN

    companion object {
        @JvmStatic
        private val PATTERN = Regex("^\\s*(?:-)\\s*$", RegexOption.IGNORE_CASE)
    }
}
