package org.kgusarov.krono.locales.de.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.calculation.createParsingComponentsAtWeekday
import org.kgusarov.krono.common.parsers.AbstractParserWithWordBoundaryChecking
import org.kgusarov.krono.locales.de.DeConstants
import org.kgusarov.krono.utils.matchAnyPattern

private val LAST = Regex("letzte")
private val NEXT = Regex("chste")
private val THIS = Regex("diese")

@SuppressFBWarnings("EI_EXPOSE_REP")
class DeWeekdayParser : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult {
        val dayOfWeek = match[WEEKDAY_GROUP]!!.lowercase()
        val offset = DeConstants.WEEKDAY_DICTIONARY[dayOfWeek]!!
        val prefix = match[PREFIX_GROUP]
        val postfix = match[SUFFIX_GROUP]

        val norm =
            (
                when {
                    !prefix.isNullOrEmpty() -> prefix
                    !postfix.isNullOrEmpty() -> postfix
                    else -> ""
                }
            ).lowercase()

        val modifier =
            when {
                norm.contains(LAST) -> "last"
                norm.contains(NEXT) -> "next"
                norm.contains(THIS) -> "this"
                else -> null
            }

        return ParserResultFactory(createParsingComponentsAtWeekday(context.reference, offset, modifier))
    }

    @Suppress("RegExpRedundantEscape", "RegExpSingleCharAlternation")
    companion object {
        const val PREFIX_GROUP = 1
        const val SUFFIX_GROUP = 3
        const val WEEKDAY_GROUP = 2

        @JvmStatic
        private val PATTERN =
            Regex(
                "(?:(?:\\,|\\(|\\（)\\s*)?" +
                    "(?:a[mn]\\s*?)?" +
                    "(?:(diese[mn]|letzte[mn]|n(?:ä|ae)chste[mn])\\s*)?" +
                    "(${matchAnyPattern(DeConstants.WEEKDAY_DICTIONARY)})" +
                    "(?:\\s*(?:\\,|\\)|\\）))?" +
                    "(?:\\s*(diese|letzte|n(?:ä|ae)chste)\\s*woche)?" +
                    "(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )
    }
}
