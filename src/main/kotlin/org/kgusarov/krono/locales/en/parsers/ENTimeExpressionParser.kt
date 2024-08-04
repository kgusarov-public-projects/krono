package org.kgusarov.krono.locales.en.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.KronoMeridiem
import org.kgusarov.krono.ParsedComponents
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.parsers.AbstractTimeExpressionParser
import org.kgusarov.krono.extensions.compareTo
import org.kgusarov.krono.extensions.plus

@SuppressFBWarnings("EI_EXPOSE_REP")
class ENTimeExpressionParser(strictMode: Boolean) : AbstractTimeExpressionParser(strictMode) {
    override fun primaryPrefix() = PRIMARY_PREFIX_PATTERN

    override fun followingPhase() = FOLLOWING_PHASE_PATTERN

    override fun primarySuffix() = PRIMARY_SUFFIX_PATTERN

    override fun extractPrimaryTimeComponents(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParsedComponents? {
        val components = super.extractPrimaryTimeComponents(context, match) ?: return null

        if (match[0]!!.endsWith("night")) {
            val hour = components.hour()
            if (hour >= 6 && hour < 12) {
                components.assign(KronoComponents.Hour, components.hour() + 12)
                components.assign(KronoComponents.Meridiem, KronoMeridiem.PM)
            } else if (hour < 6) {
                components.assign(KronoComponents.Meridiem, KronoMeridiem.AM)
            }
        }

        if (match[0]!!.endsWith("afternoon")) {
            components.assign(KronoComponents.Meridiem, KronoMeridiem.PM)
            val hour = components.hour()
            if (hour >= 0 && hour <= 6) {
                components.assign(KronoComponents.Hour, components.hour() + 12)
            }
        }

        if (match[0]!!.endsWith("morning")) {
            components.assign(KronoComponents.Meridiem, KronoMeridiem.AM)
            val hour = components.hour()
            if (hour < 12) {
                components.assign(KronoComponents.Hour, components.hour())
            }
        }

        return components.addTag("parser/ENTimeExpressionParser")
    }

    companion object {
        @JvmStatic
        private val FOLLOWING_PHASE_PATTERN = "\\s*(?:\\-|\\–|\\~|\\〜|to|until|through|till|\\?)\\s*"

        @JvmStatic
        private val PRIMARY_PREFIX_PATTERN = "(?:(?:at|from)\\s*)??"

        @JvmStatic
        private val PRIMARY_SUFFIX_PATTERN =
            "(?:\\s*(?:o\\W*clock|at\\s*night|in\\s*the\\s*(?:morning|afternoon)))?(?!/)(?=\\W|$)"
    }
}
