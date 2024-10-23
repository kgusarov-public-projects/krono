package org.kgusarov.krono.locales.fr.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.KronoMeridiem
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.parsers.AbstractParserWithWordBoundaryChecking

@SuppressFBWarnings("EI_EXPOSE_REP")
class FrCasualTimeParser : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult {
        val component = context.createParsingComponents()

        when (match[2]!!.lowercase()) {
            "après-midi", "aprem" -> {
                component.imply(KronoComponents.Hour, 14)
                component.imply(KronoComponents.Minute, 0)
                component.imply(KronoComponents.Meridiem, KronoMeridiem.PM)
            }

            "soir" -> {
                component.imply(KronoComponents.Hour, 18)
                component.imply(KronoComponents.Minute, 0)
                component.imply(KronoComponents.Meridiem, KronoMeridiem.PM)
            }

            "matin" -> {
                component.imply(KronoComponents.Hour, 8)
                component.imply(KronoComponents.Minute, 0)
                component.imply(KronoComponents.Meridiem, KronoMeridiem.AM)
            }

            "a midi" -> {
                component.imply(KronoComponents.Hour, 12)
                component.imply(KronoComponents.Minute, 0)
                component.imply(KronoComponents.Meridiem, KronoMeridiem.AM)
            }

            "à minuit" -> {
                component.imply(KronoComponents.Hour, 0)
                component.imply(KronoComponents.Meridiem, KronoMeridiem.AM)
            }
        }

        return ParserResultFactory(component)
    }

    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "(cet?)?\\s*(matin|soir|après-midi|aprem|a midi|à minuit)(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )
    }
}
