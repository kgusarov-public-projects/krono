package org.kgusarov.krono.locales.en.refiners

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.common.refiners.AbstractMergeDateRangeRefiner

@SuppressFBWarnings("EI_EXPOSE_REP")
class EnMergeDateRangeRefiner : AbstractMergeDateRangeRefiner() {
    override fun patternBetween() = PATTERN

    companion object {
        @JvmStatic
        private val PATTERN = Regex("^\\s*(to|-|–|until|through|till)\\s*\$", RegexOption.IGNORE_CASE)
    }
}
