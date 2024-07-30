package org.kgusarov.krono.locales.en.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.ParsedComponents
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.afternoon
import org.kgusarov.krono.common.evening
import org.kgusarov.krono.common.midnight
import org.kgusarov.krono.common.morning
import org.kgusarov.krono.common.noon
import org.kgusarov.krono.common.parsers.AbstractParserWithWordBoundaryChecking

@SuppressFBWarnings("EI_EXPOSE_REP")
class ENCasualTimeParser : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult? {
        val component: ParsedComponents? =
            when (match[1]!!.lowercase()) {
                "afternoon" -> afternoon(context.reference)
                "evening", "night" -> evening(context.reference)
                "midnight" -> midnight(context.reference)
                "morning" -> morning(context.reference)
                "noon", "midday" -> noon(context.reference)
                else -> null
            }?.addTag("parser/ENCasualTimeParser")

        return if (component == null) {
            null
        } else {
            ParserResultFactory(component)
        }
    }

    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "(?:this)?\\s{0,3}(morning|afternoon|evening|night|midnight|midday|noon)(?=\\W|\$)",
                RegexOption.IGNORE_CASE,
            )
    }
}
