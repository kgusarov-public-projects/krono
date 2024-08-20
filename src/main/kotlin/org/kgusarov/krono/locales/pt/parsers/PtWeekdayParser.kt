package org.kgusarov.krono.locales.pt.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.calculation.createParsingComponentsAtWeekday
import org.kgusarov.krono.common.parsers.AbstractParserWithWordBoundaryChecking
import org.kgusarov.krono.locales.pt.PtConstants
import org.kgusarov.krono.utils.matchAnyPattern

@SuppressFBWarnings("EI_EXPOSE_REP")
class PtWeekdayParser : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult? {
        val dayOfWeek = match[WEEKDAY_GROUP]!!.lowercase()
        val weekday = PtConstants.WEEKDAY_DICTIONARY[dayOfWeek] ?: return null
        val prefix = match[PREFIX_GROUP]
        val postfix = match[POSTFIX_GROUP]
        val norm =
            when {
                !prefix.isNullOrEmpty() -> prefix
                !postfix.isNullOrEmpty() -> postfix
                else -> ""
            }

        val modifier: String? =
            when (norm) {
                "passado" -> "last"
                "próximo", "proximo" -> "next"
                "este" -> "this"
                else -> null
            }

        return ParserResultFactory(createParsingComponentsAtWeekday(context.reference, weekday, modifier))
    }

    @Suppress("RegExpRedundantEscape", "RegExpSingleCharAlternation")
    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "(?:(?:\\,|\\(|\\（)\\s*)?" +
                    "(?:(este|esta|passado|pr[oó]ximo)\\s*)?" +
                    "(${matchAnyPattern(PtConstants.WEEKDAY_DICTIONARY)})" +
                    "(?:\\s*(?:\\,|\\)|\\）))?" +
                    "(?:\\s*(este|esta|passado|pr[óo]ximo)\\s*semana)?" +
                    "(?=\\W|\\d|$)",
                RegexOption.IGNORE_CASE,
            )

        private const val PREFIX_GROUP = 1
        private const val WEEKDAY_GROUP = 2
        private const val POSTFIX_GROUP = 3
    }
}
