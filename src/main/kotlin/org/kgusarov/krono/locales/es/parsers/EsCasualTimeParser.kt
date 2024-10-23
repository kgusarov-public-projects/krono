package org.kgusarov.krono.locales.es.parsers

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
class EsCasualTimeParser : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult {
        val targetDate = context.instant
        val component = context.createParsingComponents()

        when (match[1]!!.lowercase()) {
            "tarde" -> {
                component.imply(KronoComponents.Meridiem, KronoMeridiem.PM)
                component.imply(KronoComponents.Hour, 15)
            }

            "noche" -> {
                component.imply(KronoComponents.Meridiem, KronoMeridiem.PM)
                component.imply(KronoComponents.Hour, 22)
            }

            "mañana" -> {
                component.imply(KronoComponents.Meridiem, KronoMeridiem.AM)
                component.imply(KronoComponents.Hour, 6)
            }

            "medianoche" -> {
                targetDate.assignTheNextDay(component)
                component.imply(KronoComponents.Hour, 0)
                component.imply(KronoComponents.Minute, 0)
                component.imply(KronoComponents.Second, 0)
            }

            "mediodia", "mediodía" -> {
                component.imply(KronoComponents.Meridiem, KronoMeridiem.AM)
                component.imply(KronoComponents.Hour, 12)
            }
        }

        return ParserResultFactory(component)
    }

    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "(?:esta\\s*)?(mañana|tarde|medianoche|mediodia|mediodía|noche)(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )
    }
}
