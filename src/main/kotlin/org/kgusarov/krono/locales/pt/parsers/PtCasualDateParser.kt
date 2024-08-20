package org.kgusarov.krono.locales.pt.parsers

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
class PtCasualDateParser : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult {
        val lowerText = match[0]!!.lowercase()

        return when (lowerText) {
            "agora" -> ParserResultFactory(now(context.reference))
            "hoje" -> ParserResultFactory(today(context.reference))
            "amanha", "amanhã" -> ParserResultFactory(tomorrow(context.reference))
            "ontem" -> ParserResultFactory(yesterday(context.reference))
            else -> ParserResultFactory(context.createParsingComponents())
        }
    }

    companion object {
        @JvmStatic
        private val PATTERN = Regex("(agora|hoje|amanha|amanhã|ontem)(?=\\W|$)", RegexOption.IGNORE_CASE)
    }
}
