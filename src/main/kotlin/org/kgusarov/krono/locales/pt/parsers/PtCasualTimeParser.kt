package org.kgusarov.krono.locales.pt.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.KronoMeridiem
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.parsers.AbstractParserWithWordBoundaryChecking
import org.kgusarov.krono.extensions.assignTheNextDay

@SuppressFBWarnings("EI_EXPOSE_REP")
class PtCasualTimeParser : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult {
        val components = context.createParsingComponents()
        when (match[1]!!.lowercase()) {
            "tarde" -> {
                components.imply(KronoComponents.Meridiem, KronoMeridiem.PM)
                components.imply(KronoComponents.Hour, 15)
            }

            "noite" -> {
                components.imply(KronoComponents.Meridiem, KronoMeridiem.PM)
                components.imply(KronoComponents.Hour, 22)
            }

            "manha", "manhã" -> {
                components.imply(KronoComponents.Meridiem, KronoMeridiem.AM)
                components.imply(KronoComponents.Hour, 6)
            }

            "meia-noite" -> {
                context.instant.assignTheNextDay(components)
                components.imply(KronoComponents.Hour, 0)
                components.imply(KronoComponents.Minute, 0)
                components.imply(KronoComponents.Second, 0)
            }

            "meio-dia" -> {
                components.imply(KronoComponents.Meridiem, KronoMeridiem.AM)
                components.imply(KronoComponents.Hour, 12)
            }
        }

        return ParserResultFactory(components)
    }

    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "(?:esta\\s*)?(manha|manhã|tarde|meia-noite|meio-dia|noite)(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )
    }
}
