package org.kgusarov.krono.locales.fr.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.ParsedComponents
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.parsers.AbstractTimeExpressionParser

@SuppressFBWarnings("EI_EXPOSE_REP")
class FrTimeExpressionParser : AbstractTimeExpressionParser() {
    override fun primaryPrefix() = PRIMARY_PREFIX_PATTERN

    override fun followingPhase() = FOLLOWING_PHASE_PATTERN

    override fun extractPrimaryTimeComponents(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParsedComponents? {
        if (match[0]!!.matches(YEAR_LIKE)) {
            return null
        }

        return super.extractPrimaryTimeComponents(context, match)
    }

    companion object {
        @JvmStatic
        private val FOLLOWING_PHASE_PATTERN = "\\s*(?:\\-|\\–|\\~|\\〜|[àa]|\\?)\\s*"

        @JvmStatic
        private val PRIMARY_PREFIX_PATTERN = "(?:(?:[àa])\\s*)?"

        @JvmStatic
        private val YEAR_LIKE = Regex("^\\s*\\d{4}\\s*$")
    }
}
