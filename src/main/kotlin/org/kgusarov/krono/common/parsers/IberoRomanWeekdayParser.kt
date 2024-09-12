package org.kgusarov.krono.common.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.calculation.createParsingComponentsAtWeekday
import org.kgusarov.krono.utils.matchAnyPattern
import java.time.DayOfWeek

@SuppressFBWarnings("EI_EXPOSE_REP")
abstract class IberoRomanWeekdayParser : AbstractParserWithWordBoundaryChecking() {
    @Suppress("RegExpRedundantEscape", "RegExpSingleCharAlternation")
    override fun innerPattern(context: ParsingContext) =
        Regex(
            "(?:(?:\\,|\\(|\\（)\\s*)?" +
                "(?:(este|esta|${getThisModifier()}|pr[oó]ximo)\\s*)?" +
                "(${matchAnyPattern(getWeekdayDictionary())})" +
                "(?:\\s*(?:\\,|\\)|\\）))?" +
                "(?:\\s*(este|esta|${getThisModifier()}|pr[óo]ximo)\\s*semana)?" +
                "(?=\\W|\\d|$)",
            RegexOption.IGNORE_CASE,
        )

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult? {
        val dayOfWeek = match[WEEKDAY_GROUP]!!.lowercase()
        val weekday = getWeekdayDictionary()[dayOfWeek] ?: return null
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
                getThisModifier() -> "last"
                "próximo", "proximo" -> "next"
                "este" -> "this"
                else -> null
            }

        return ParserResultFactory(createParsingComponentsAtWeekday(context.reference, weekday, modifier))
    }

    abstract fun getThisModifier(): String

    abstract fun getWeekdayDictionary(): Map<String, DayOfWeek>

    companion object {
        private const val PREFIX_GROUP = 1
        private const val WEEKDAY_GROUP = 2
        private const val POSTFIX_GROUP = 3
    }
}
