package org.kgusarov.krono.locales.pt.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.common.parsers.AbstractTimeExpressionParser

@SuppressFBWarnings("EI_EXPOSE_REP")
class PtTimeExpressionParser : AbstractTimeExpressionParser() {
    override fun primaryPrefix() = PRIMARY_PREFIX

    override fun followingPhase() = FOLLOWING_PHASE

    companion object {
        @JvmStatic
        private val PRIMARY_PREFIX = "(?:(?:ao?|às?|das|da|de|do)\\s*)?"

        @JvmStatic
        private val FOLLOWING_PHASE = "\\s*(?:\\-|\\–|\\~|\\〜|a(?:o)?|\\?)\\s*"
    }
}
