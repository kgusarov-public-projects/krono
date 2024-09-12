package org.kgusarov.krono.locales.es.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.common.parsers.AbstractTimeExpressionParser

@SuppressFBWarnings("EI_EXPOSE_REP")
class EsTimeExpressionParser : AbstractTimeExpressionParser() {
    override fun primaryPrefix() = PRIMARY_PREFIX

    override fun followingPhase() = FOLLOWING_PHASE

    companion object {
        @JvmStatic
        private val PRIMARY_PREFIX = "(?:(?:aslas|deslas|las?|al?|de|del)\\s*)?"

        @JvmStatic
        private val FOLLOWING_PHASE = "\\s*(?:\\-|\\–|\\~|\\〜|a(?:l)?|\\?)\\s*"
    }
}
