package org.kgusarov.krono.locales.pt.refiners

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.common.refiners.AbstractMergeDateTimeRefiner

@Suppress("RegExpUnnecessaryNonCapturingGroup", "RegExpSingleCharAlternation")
@SuppressFBWarnings("EI_EXPOSE_REP")
class PtMergeDateTimeRefiner : AbstractMergeDateTimeRefiner() {
    override fun patternBetween() = PATTERN

    companion object {
        @JvmStatic
        private val PATTERN = Regex("^\\s*(?:,|à)?\\s*$", RegexOption.IGNORE_CASE)
    }
}
