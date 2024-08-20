package org.kgusarov.krono.locales.en.refiners

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.common.refiners.AbstractMergeDateTimeRefiner

@SuppressFBWarnings("EI_EXPOSE_REP")
class EnMergeDateTimeRefiner : AbstractMergeDateTimeRefiner() {
    override fun patternBetween() = PATTERN

    companion object {
        @JvmStatic
        private val PATTERN = Regex("^\\s*(T|at|after|before|on|of|,|-|\\.|∙|:)?\\s*$")
    }
}
