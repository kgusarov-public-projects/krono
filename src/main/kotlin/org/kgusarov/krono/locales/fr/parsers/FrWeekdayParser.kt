package org.kgusarov.krono.locales.fr.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.calculation.createParsingComponentsAtWeekday
import org.kgusarov.krono.common.parsers.AbstractParserWithWordBoundaryChecking
import org.kgusarov.krono.locales.fr.FrConstants
import org.kgusarov.krono.utils.matchAnyPattern

@SuppressFBWarnings("EI_EXPOSE_REP")
class FrWeekdayParser : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult? {
        val dayOfWeek = match[WEEKDAY_GROUP]?.lowercase()
        val weekday = FrConstants.WEEKDAY_DICTIONARY[dayOfWeek] ?: return null

        var suffix = match[POSTFIX_GROUP]
        suffix = suffix ?: ""
        suffix = suffix.lowercase()

        val modifier: String? =
            when (suffix) {
                "dernier" -> "last"
                "prochain" -> "next"
                else -> null
            }

        return ParserResultFactory(createParsingComponentsAtWeekday(context.reference, weekday, modifier))
    }

    @Suppress(
        "RegExpRedundantEscape",
        "RegExpSimplifiable",
        "RegExpSingleCharAlternation",
        "RegExpUnnecessaryNonCapturingGroup",
    )
    companion object {
        const val WEEKDAY_GROUP = 1
        const val POSTFIX_GROUP = 2

        @JvmStatic
        private val PATTERN =
            Regex(
                "(?:(?:\\,|\\(|\\（)\\s*)?" +
                    "(?:(?:ce)\\s*)?" +
                    "(${matchAnyPattern(FrConstants.WEEKDAY_DICTIONARY)})" +
                    "(?:\\s*(?:\\,|\\)|\\）))?" +
                    "(?:\\s*(dernier|prochain)\\s*)?" +
                    "(?=\\W|\\d|$)",
                RegexOption.IGNORE_CASE,
            )
    }
}
