package org.kgusarov.krono.locales.en.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.TextOrEndIndexInputFactory
import org.kgusarov.krono.calculation.findYearClosestToRef
import org.kgusarov.krono.common.parsers.AbstractParserWithWordBoundaryChecking
import org.kgusarov.krono.extensions.not
import org.kgusarov.krono.extensions.plus
import org.kgusarov.krono.locales.en.EnConstants
import org.kgusarov.krono.locales.en.EnUtils
import org.kgusarov.krono.utils.matchAnyPattern

@SuppressFBWarnings("EI_EXPOSE_REP")
class ENMonthNameParser : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult? {
        val monthName = match[MONTH_NAME_GROUP]!!.lowercase()
        val matched = match[0]!!
        if (matched.length <= 3 && !EnConstants.FULL_MONTH_NAME_DICTIONARY.containsKey(monthName)) {
            return null
        }

        val result =
            context.createParsingResult(
                match.index + match[PREFIX_GROUP]!!.length,
                TextOrEndIndexInputFactory(match.index + matched.length),
            )

        result.start.imply(KronoComponents.Day, 1)
        result.start.addTag("parser/ENMonthNameParser")

        val month = EnConstants.MONTH_DICTIONARY[monthName]!!
        result.start.assign(KronoComponents.Month, month)

        if (!match[YEAR_GROUP]) {
            val year = findYearClosestToRef(context.instant, 1, month)
            result.start.imply(KronoComponents.Year, year)
        } else {
            val year = EnUtils.parseYear(match[YEAR_GROUP]!!)
            result.start.assign(KronoComponents.Year, year)
        }

        return ParserResultFactory(result)
    }

    @Suppress(
        "RegExpRedundantEscape",
        "RegExpSimplifiable",
        "RegExpSingleCharAlternation",
        "RegExpUnnecessaryNonCapturingGroup",
    )
    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "((?:in)\\s*)?" +
                    "(${matchAnyPattern(EnConstants.MONTH_DICTIONARY)})" +
                    "\\s*" +
                    "(?:" +
                    "[,-]?\\s*(${EnConstants.YEAR_PATTERN})?" +
                    ")?" +
                    "(?=[^\\s\\w]|\\s+[^0-9]|\\s+\$|$)",
                RegexOption.IGNORE_CASE,
            )

        private const val PREFIX_GROUP = 1
        private const val MONTH_NAME_GROUP = 2
        private const val YEAR_GROUP = 3
    }
}
