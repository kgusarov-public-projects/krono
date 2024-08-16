package org.kgusarov.krono.locales.ja.refiners

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.common.refiners.AbstractMergeDateRangeRefiner

@SuppressFBWarnings("EI_EXPOSE_REP")
class JaMergeDateRangeRefiner : AbstractMergeDateRangeRefiner() {
    override fun patternBetween(): Regex = PATTERN

    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "\\s*(から|ー|-)\\s*$",
                RegexOption.IGNORE_CASE,
            )
    }
}
