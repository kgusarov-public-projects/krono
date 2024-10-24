package org.kgusarov.krono.locales.de.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.common.parsers.CommonMonthNameLittleEndianParser
import org.kgusarov.krono.locales.de.DeConstants
import org.kgusarov.krono.locales.de.parseNumberPattern
import org.kgusarov.krono.locales.de.parseYear
import org.kgusarov.krono.utils.matchAnyPattern

@SuppressFBWarnings("EI_EXPOSE_REP")
class DeMonthNameLittleEndianParser : CommonMonthNameLittleEndianParser() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun getMonthDictionary() = DeConstants.MONTH_DICTIONARY

    override fun getParseOrdinalNumberPattern() =
        { it: String ->
            parseNumberPattern(it).toInt()
        }

    override fun getParseYear() = ::parseYear

    override fun getMonthNameGroup() = MONTH_NAME_GROUP

    override fun getYearGroup() = YEAR_GROUP

    override fun getDateGroup() = DATE_GROUP

    override fun getDateToGroup() = DATE_TO_GROUP

    @Suppress("RegExpRedundantEscape", "RegExpSimplifiable", "RegExpUnnecessaryNonCapturingGroup")
    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "(?:am\\s*?)?" +
                    "(?:den\\s*?)?" +
                    "([0-9]{1,2})\\." +
                    "(?:\\s*(?:bis(?:\\s*(?:am|zum))?|\\-|\\–|\\s)\\s*([0-9]{1,2})\\.?)?\\s*" +
                    "(${matchAnyPattern(DeConstants.MONTH_DICTIONARY)})" +
                    "(?:(?:-|/|,?\\s*)(${DeConstants.YEAR_PATTERN}(?![^\\s]\\d)))?" +
                    "(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )

        private const val DATE_GROUP = 1
        private const val DATE_TO_GROUP = 2
        private const val MONTH_NAME_GROUP = 3
        private const val YEAR_GROUP = 4
    }
}
