package org.kgusarov.krono.locales.en.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.common.parsers.CommonMonthNameLittleEndianParser
import org.kgusarov.krono.locales.en.EnConstants
import org.kgusarov.krono.locales.en.parseOrdinalNumberPattern
import org.kgusarov.krono.locales.en.parseYear
import org.kgusarov.krono.utils.matchAnyPattern

@SuppressFBWarnings("EI_EXPOSE_REP")
class EnMonthNameLittleEndianParser : CommonMonthNameLittleEndianParser() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun getMonthDictionary() = EnConstants.MONTH_DICTIONARY

    override fun getParseOrdinalNumberPattern() = ::parseOrdinalNumberPattern

    override fun getParseYear() = ::parseYear

    override fun getMonthNameGroup() = MONTH_NAME_GROUP

    override fun getYearGroup() = YEAR_GROUP

    override fun getDateGroup() = DATE_GROUP

    override fun getDateToGroup() = DATE_TO_GROUP

    @Suppress("RegExpRedundantEscape")
    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "(?:on\\s{0,3})?" +
                    "(${EnConstants.ORDINAL_NUMBER_PATTERN})" +
                    "(?:" +
                    "\\s{0,3}(?:to|\\-|\\–|until|through|till)?\\s{0,3}" +
                    "(${EnConstants.ORDINAL_NUMBER_PATTERN})" +
                    ")?" +
                    "(?:-|/|\\s{0,3}(?:of)?\\s{0,3})" +
                    "(${matchAnyPattern(EnConstants.MONTH_DICTIONARY)})" +
                    "(?:" +
                    "(?:-|/|,?\\s{0,3})" +
                    "(${EnConstants.YEAR_PATTERN}(?!\\w))" +
                    ")?" +
                    "(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )

        private const val DATE_GROUP = 1
        private const val DATE_TO_GROUP = 2
        private const val MONTH_NAME_GROUP = 3
        private const val YEAR_GROUP = 4
    }
}
