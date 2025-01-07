package org.kgusarov.krono.locales.nl.refiners

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.common.refiners.AbstractMergeDateTimeRefiner

@Suppress("RegExpUnnecessaryNonCapturingGroup", "RegExpSingleCharAlternation")
@SuppressFBWarnings("EI_EXPOSE_REP")
class NlMergeDateTimeRefiner : AbstractMergeDateTimeRefiner() {
    override fun patternBetween() = PATTERN

    companion object {
        @JvmStatic
        private val PATTERN = Regex("^\\s*(om|na|voor|in de|,|-)?\\s*$", RegexOption.IGNORE_CASE)
    }
}
