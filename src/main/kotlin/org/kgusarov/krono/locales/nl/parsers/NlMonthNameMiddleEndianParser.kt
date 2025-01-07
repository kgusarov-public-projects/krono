package org.kgusarov.krono.locales.nl.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.ComponentsInputFactory
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.parsers.AbstractMonthNameMiddleEndianParser
import org.kgusarov.krono.extensions.plus
import org.kgusarov.krono.locales.nl.NlConstants
import org.kgusarov.krono.locales.nl.parseOrdinalNumberPattern
import org.kgusarov.krono.locales.nl.parseYear
import org.kgusarov.krono.utils.matchAnyPattern

@SuppressFBWarnings("EI_EXPOSE_REP")
class NlMonthNameMiddleEndianParser : AbstractMonthNameMiddleEndianParser() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult? {
        val month = NlConstants.MONTH_DICTIONARY[match[MONTH_NAME_GROUP]!!.lowercase()]
        val day = parseOrdinalNumberPattern(match[DATE_GROUP]!!)
        if (day > 31) {
            match.index += match[DATE_GROUP]!!.length
            return null
        }

        val components =
            context.createParsingComponents(
                ComponentsInputFactory(
                    KronoComponents.Day to day,
                    KronoComponents.Month to month,
                ),
            )

        return parse(
            context,
            match,
            components,
            YEAR_GROUP,
            DATE_TO_GROUP,
            day,
            month,
            ::parseOrdinalNumberPattern,
            ::parseYear,
        )
    }

    companion object {
        @JvmStatic
        @Suppress("RegExpRedundantEscape")
        private val PATTERN =
            Regex(
                "(?:on\\s*?)?" +
                    "(${NlConstants.ORDINAL_NUMBER_PATTERN})" +
                    "(?:\\s*" +
                    "(?:tot|\\-|\\–|until|through|till|\\s)\\s*" +
                    "(${NlConstants.ORDINAL_NUMBER_PATTERN})" +
                    ")?" +
                    "(?:-|/|\\s*(?:of)?\\s*)" +
                    "(" +
                    matchAnyPattern(NlConstants.MONTH_DICTIONARY) +
                    ")" +
                    "(?:" +
                    "(?:-|/|,?\\s*)" +
                    "(${NlConstants.YEAR_PATTERN}(?![^\\s]\\d))" +
                    ")?" +
                    "(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )

        private const val MONTH_NAME_GROUP = 3
        private const val DATE_GROUP = 1
        private const val DATE_TO_GROUP = 2
        private const val YEAR_GROUP = 4
    }
}
