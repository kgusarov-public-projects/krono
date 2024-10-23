package org.kgusarov.krono.locales.es.refiners

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.common.refiners.AbstractMergeDateTimeRefiner

@Suppress("RegExpUnnecessaryNonCapturingGroup", "RegExpSingleCharAlternation")
@SuppressFBWarnings("EI_EXPOSE_REP")
class EsMergeDateTimeRefiner : AbstractMergeDateTimeRefiner() {
    override fun patternBetween() = PATTERN

    companion object {
        @JvmStatic
        private val PATTERN = Regex("^\\s*(?:,|de|aslas|a)?\\s*$", RegexOption.IGNORE_CASE)
    }
}
