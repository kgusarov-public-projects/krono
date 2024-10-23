package org.kgusarov.krono.locales.es.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.now
import org.kgusarov.krono.common.parsers.AbstractParserWithWordBoundaryChecking
import org.kgusarov.krono.common.today
import org.kgusarov.krono.common.tomorrow
import org.kgusarov.krono.common.yesterday

@SuppressFBWarnings("EI_EXPOSE_REP")
class EsCasualDateParser : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult {
        val lowerText = match[0]!!.lowercase()
        val component = context.createParsingComponents()

        when (lowerText) {
            "ahora" -> return ParserResultFactory(now(context.reference))
            "hoy" -> return ParserResultFactory(today(context.reference))
            "mañana" -> return ParserResultFactory(tomorrow(context.reference))
            "ayer" -> return ParserResultFactory(yesterday(context.reference))
        }

        return ParserResultFactory(component)
    }

    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "(ahora|hoy|mañana|ayer)(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )
    }
}
