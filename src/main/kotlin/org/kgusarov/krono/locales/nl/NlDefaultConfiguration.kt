package org.kgusarov.krono.locales.nl

import org.kgusarov.krono.KronoConfiguration
import org.kgusarov.krono.common.parsers.SlashDateFormatParser
import org.kgusarov.krono.includeCommonConfiguration
import org.kgusarov.krono.locales.nl.parsers.NlMonthNameMiddleEndianParser
import org.kgusarov.krono.locales.nl.parsers.NlTimeUnitWithinFormatParser
import org.kgusarov.krono.locales.nl.refiners.NlMergeDateRangeRefiner
import org.kgusarov.krono.locales.nl.refiners.NlMergeDateTimeRefiner

class NlDefaultConfiguration {
    fun createConfiguration(
        strictMode: Boolean = true,
        littleEndian: Boolean = true,
    ): KronoConfiguration =
        includeCommonConfiguration(
            KronoConfiguration(
                mutableListOf(
                    SlashDateFormatParser(littleEndian),
                    NlTimeUnitWithinFormatParser(),
                    NlMonthNameMiddleEndianParser(),
                    /*
                    + new SlashDateFormatParser(littleEndian),
                    + new NLTimeUnitWithinFormatParser(),
                    + new NLMonthNameMiddleEndianParser(),
                    new NLMonthNameParser(),
                    new NLWeekdayParser(),
                    new NLCasualYearMonthDayParser(),
                    new NLSlashMonthFormatParser(),
                    new NLTimeExpressionParser(strictMode),
                    new NLTimeUnitAgoFormatParser(strictMode),
                    new NLTimeUnitLaterFormatParser(strictMode),
                     */
                ),
                mutableListOf(
                    NlMergeDateTimeRefiner(),
                    NlMergeDateRangeRefiner(),
                ),
            ),
            strictMode,
        )

    fun createCasualConfiguration(littleEndian: Boolean = true): KronoConfiguration {
        val result = createConfiguration(false, littleEndian)

        /*
        option.parsers.unshift(new NLCasualDateParser());
    option.parsers.unshift(new NLCasualTimeParser());
    option.parsers.unshift(new NLCasualDateTimeParser());
    option.parsers.unshift(new NLMonthNameParser());
    option.parsers.unshift(new NLRelativeDateFormatParser());
    option.parsers.unshift(new NLTimeUnitCasualRelativeFormatParser());
         */
        return result
    }
}
