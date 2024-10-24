package org.kgusarov.krono.locales.de.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.ParsedComponents
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.parsers.AbstractTimeExpressionParser

@SuppressFBWarnings("EI_EXPOSE_REP")
class DeTimeExpressionParser : AbstractTimeExpressionParser() {
    override fun primaryPrefix() = PRIMARY_PREFIX

    override fun followingPhase() = FOLLOWING_PHASE

    override fun extractPrimaryTimeComponents(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParsedComponents? {
        if (YEAR_LIKE.matches(match[0]!!)) {
            return null
        }

        return super.extractPrimaryTimeComponents(context, match)
    }

    companion object {
        @JvmStatic
        private val PRIMARY_PREFIX = "(?:(?:um|von)\\s*)?"

        @JvmStatic
        private val FOLLOWING_PHASE = "\\s*(?:\\-|\\–|\\~|\\〜|bis)\\s*"

        @JvmStatic
        private val YEAR_LIKE = Regex("^\\s*\\d{4}\\s*$")
    }
}
