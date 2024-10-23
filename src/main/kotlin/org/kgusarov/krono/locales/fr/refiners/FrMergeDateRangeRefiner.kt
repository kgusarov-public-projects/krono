package org.kgusarov.krono.locales.fr.refiners

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.common.refiners.AbstractMergeDateRangeRefiner

@Suppress("RegExpUnnecessaryNonCapturingGroup")
@SuppressFBWarnings("EI_EXPOSE_REP")
class FrMergeDateRangeRefiner : AbstractMergeDateRangeRefiner() {
    override fun patternBetween() = PATTERN

    companion object {
        @JvmStatic
        private val PATTERN = Regex("^\\s*(à|a|au|-)\\s*$", RegexOption.IGNORE_CASE)
    }
}
