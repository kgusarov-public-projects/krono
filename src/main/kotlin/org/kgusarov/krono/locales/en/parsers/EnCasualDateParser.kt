package org.kgusarov.krono.locales.en.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.KronoUnit
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.now
import org.kgusarov.krono.common.parsers.AbstractParserWithWordBoundaryChecking
import org.kgusarov.krono.common.today
import org.kgusarov.krono.common.tomorrow
import org.kgusarov.krono.common.tonight
import org.kgusarov.krono.common.yesterday
import org.kgusarov.krono.extensions.add
import org.kgusarov.krono.extensions.assignSimilarDate

@SuppressFBWarnings("EI_EXPOSE_REP")
class EnCasualDateParser : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult {
        var targetDate = context.instant
        val lowerText = match[0]!!.lowercase()
        var component = context.createParsingComponents()

        when (lowerText) {
            "now" -> component = now(context.reference)
            "today" -> component = today(context.reference)
            "yesterday" -> component = yesterday(context.reference)
            "tomorrow", "tmr", "tmrw" -> component = tomorrow(context.reference)
            "tonight" -> component = tonight(context.reference)
            else -> {
                if (lowerText.matches(LAST_NIGHT)) {
                    if (targetDate.hour > 6) {
                        targetDate = targetDate.add(KronoUnit.Day, -1)
                    }

                    targetDate.assignSimilarDate(component)
                    component.imply(KronoComponents.Hour, 0)
                }
            }
        }

        component.addTag("parser/ENCasualDateParser")
        return ParserResultFactory(component)
    }

    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "(now|today|tonight|tomorrow|tmr|tmrw|yesterday|last\\s*night)(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )

        @JvmStatic
        private val LAST_NIGHT = Regex("last\\s*night")
    }
}
