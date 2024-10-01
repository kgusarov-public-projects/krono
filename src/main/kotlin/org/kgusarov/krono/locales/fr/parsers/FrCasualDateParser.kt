package org.kgusarov.krono.locales.fr.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.KronoMeridiem
import org.kgusarov.krono.KronoUnit
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.now
import org.kgusarov.krono.common.parsers.AbstractParserWithWordBoundaryChecking
import org.kgusarov.krono.common.today
import org.kgusarov.krono.common.tomorrow
import org.kgusarov.krono.common.yesterday
import org.kgusarov.krono.extensions.add
import org.kgusarov.krono.extensions.assignSimilarDate

private val TONIGHT = Regex("cette\\s*nuit")
private val YESTERDAY = Regex("la\\s*veille")

@SuppressFBWarnings("EI_EXPOSE_REP")
class FrCasualDateParser : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult {
        var targetDate = context.instant
        val lowerText = match[0]!!.lowercase()
        val component = context.createParsingComponents()

        when (lowerText) {
            "maintenant" -> return ParserResultFactory(now(context.reference))
            "aujourd'hui" -> return ParserResultFactory(today(context.reference))
            "hier" -> return ParserResultFactory(yesterday(context.reference))
            "demain" -> return ParserResultFactory(tomorrow(context.reference))
            else -> {
                if (lowerText.contains(TONIGHT)) {
                    targetDate.assignSimilarDate(component)
                    component.imply(KronoComponents.Hour, 22)
                    component.imply(KronoComponents.Meridiem, KronoMeridiem.PM)
                } else if (lowerText.contains(YESTERDAY)) {
                    targetDate = targetDate.add(KronoUnit.Day, -1)
                    targetDate.assignSimilarDate(component)
                    component.imply(KronoComponents.Hour, 0)
                }
            }
        }

        return ParserResultFactory(component)
    }

    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "(maintenant|aujourd'hui|demain|hier|cette\\s*nuit|la\\s*veille)(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )
    }
}
