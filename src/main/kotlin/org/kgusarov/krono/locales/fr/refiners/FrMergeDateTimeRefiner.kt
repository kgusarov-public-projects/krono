package org.kgusarov.krono.locales.fr.refiners

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.common.refiners.AbstractMergeDateTimeRefiner

@Suppress("RegExpUnnecessaryNonCapturingGroup", "RegExpSingleCharAlternation")
@SuppressFBWarnings("EI_EXPOSE_REP")
class FrMergeDateTimeRefiner : AbstractMergeDateTimeRefiner() {
    override fun patternBetween() = PATTERN

    companion object {
        @JvmStatic
        private val PATTERN = Regex("^\\s*(T|à|a|au|vers|de|,|-)?\\s*$", RegexOption.IGNORE_CASE)
    }
}
