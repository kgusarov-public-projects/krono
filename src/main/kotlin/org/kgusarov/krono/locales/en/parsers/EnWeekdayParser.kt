package org.kgusarov.krono.locales.en.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.calculation.createParsingComponentsAtWeekday
import org.kgusarov.krono.common.parsers.AbstractParserWithWordBoundaryChecking
import org.kgusarov.krono.locales.en.EnConstants
import org.kgusarov.krono.utils.matchAnyPattern
import java.time.DayOfWeek

@SuppressFBWarnings("EI_EXPOSE_REP")
class EnWeekdayParser : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult {
        val prefix = match[PREFIX_GROUP]
        val postfix = match[POSTFIX_GROUP]
        val modifierWord =
            (
                if (!prefix.isNullOrEmpty()) {
                    prefix
                } else if (!postfix.isNullOrEmpty()) {
                    postfix
                } else {
                    ""
                }
            ).lowercase()

        val modifier: String? =
            when (modifierWord) {
                "last", "past" -> "last"
                "next" -> "next"
                "this" -> "this"
                else -> null
            }

        val weekdayWord = match[WEEKDAY_GROUP]?.lowercase()
        val weekday =
            when {
                EnConstants.WEEKDAY_DICTIONARY.containsKey(weekdayWord) -> EnConstants.WEEKDAY_DICTIONARY[weekdayWord]!!
                weekdayWord == "weekend" -> {
                    if (modifier == "last") {
                        DayOfWeek.SUNDAY
                    } else {
                        DayOfWeek.SATURDAY
                    }
                }
                weekdayWord == "weekday" -> {
                    val refWeekday = context.reference.withAdjustedTimezone().dayOfWeek
                    if (refWeekday == DayOfWeek.SUNDAY || refWeekday == DayOfWeek.SATURDAY) {
                        if (modifier == "last") {
                            DayOfWeek.FRIDAY
                        } else {
                            DayOfWeek.MONDAY
                        }
                    } else {
                        val weekdayIndex = refWeekday.value - 1
                        val weekdayIndexModifier = if (modifier == "last") -1 else 1
                        val weekdayIndexNormalized = (weekdayIndex + weekdayIndexModifier) % 5
                        DayOfWeek.of(weekdayIndexNormalized + 1)
                    }
                }
                else -> return ParserResultFactory(null)
            }

        return ParserResultFactory(createParsingComponentsAtWeekday(context.reference, weekday, modifier))
    }

    @Suppress("RegExpRedundantEscape", "RegExpSimplifiable", "RegExpSingleCharAlternation")
    companion object {
        const val PREFIX_GROUP = 1
        const val WEEKDAY_GROUP = 2
        const val POSTFIX_GROUP = 3

        @JvmStatic
        private val PATTERN =
            Regex(
                "(?:(?:\\,|\\(|\\（)\\s*)?" +
                    "(?:on\\s*?)?" +
                    "(?:(this|last|past|next)\\s*)?" +
                    "(${matchAnyPattern(EnConstants.WEEKDAY_DICTIONARY)}|weekend|weekday)" +
                    "(?:\\s*(?:\\,|\\)|\\）))?" +
                    "(?:\\s*(this|last|past|next)\\s*week)?" +
                    "(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )
    }
}
