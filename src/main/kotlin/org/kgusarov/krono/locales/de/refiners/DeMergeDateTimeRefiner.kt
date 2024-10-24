package org.kgusarov.krono.locales.de.refiners

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.common.refiners.AbstractMergeDateTimeRefiner

@SuppressFBWarnings("EI_EXPOSE_REP")
class DeMergeDateTimeRefiner : AbstractMergeDateTimeRefiner() {
    override fun patternBetween() = PATTERN

    companion object {
        @JvmStatic
        private val PATTERN = Regex("^\\s*(T|um|am|,|-)?\\s*$", RegexOption.IGNORE_CASE)
    }
}
