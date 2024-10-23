package org.kgusarov.krono.locales.fr.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.common.parsers.CommonMonthNameLittleEndianParser
import org.kgusarov.krono.locales.fr.FrConstants
import org.kgusarov.krono.locales.fr.parseOrdinalNumberPattern
import org.kgusarov.krono.locales.fr.parseYear
import org.kgusarov.krono.utils.matchAnyPattern

@SuppressFBWarnings("EI_EXPOSE_REP")
class FrMonthNameLittleEndianParser : CommonMonthNameLittleEndianParser() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun getMonthDictionary() = FrConstants.MONTH_DICTIONARY

    override fun getParseOrdinalNumberPattern() = ::parseOrdinalNumberPattern

    override fun getParseYear() = ::parseYear

    override fun getMonthNameGroup() = MONTH_NAME_GROUP

    override fun getYearGroup() = YEAR_GROUP

    override fun getDateGroup() = DATE_GROUP

    override fun getDateToGroup() = DATE_TO_GROUP

    @Suppress(
        "RegExpRedundantEscape",
        "RegExpUnnecessaryNonCapturingGroup",
        "RegExpSimplifiable",
    )
    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "(?:on\\s*?)?" +
                    "(${FrConstants.ORDINAL_NUMBER_PATTERN})" +
                    "(?:\\s*(?:au|\\-|\\–|jusqu'au?|\\s)\\s*(${FrConstants.ORDINAL_NUMBER_PATTERN}))?" +
                    "(?:-|/|\\s*(?:de)?\\s*)" +
                    "(${matchAnyPattern(FrConstants.MONTH_DICTIONARY)})" +
                    "(?:(?:-|/|,?\\s*)(${FrConstants.YEAR_PATTERN}(?![^\\s]\\d)))?" +
                    "(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )

        private const val DATE_GROUP = 1
        private const val DATE_TO_GROUP = 2
        private const val MONTH_NAME_GROUP = 3
        private const val YEAR_GROUP = 4
    }
}
