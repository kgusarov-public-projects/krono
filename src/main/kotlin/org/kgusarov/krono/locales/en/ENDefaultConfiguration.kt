package org.kgusarov.krono.locales.en

import org.kgusarov.krono.KronoConfiguration
import org.kgusarov.krono.common.parsers.SlashDateFormatParser
import org.kgusarov.krono.common.refiners.OverlapRemovalRefiner
import org.kgusarov.krono.includeCommonConfiguration
import org.kgusarov.krono.locales.en.parsers.ENCasualDateParser
import org.kgusarov.krono.locales.en.parsers.ENCasualTimeParser
import org.kgusarov.krono.locales.en.parsers.ENCasualYearMonthDayParser
import org.kgusarov.krono.locales.en.parsers.ENMonthNameLittleEndianParser
import org.kgusarov.krono.locales.en.parsers.ENMonthNameMiddleEndianParser
import org.kgusarov.krono.locales.en.parsers.ENMonthNameParser
import org.kgusarov.krono.locales.en.parsers.ENWeekdayParser
import org.kgusarov.krono.locales.en.refiners.ENMergeDateRangeRefiner
import org.kgusarov.krono.locales.en.refiners.ENMergeDateTimeRefiner
import org.kgusarov.krono.locales.en.refiners.ENMergeRelativeAfterDateRefiner
import org.kgusarov.krono.locales.en.refiners.ENMergeRelativeFollowByDateRefiner

class ENDefaultConfiguration {
    fun createConfiguration(
        strictMode: Boolean = true,
        littleEndian: Boolean = false,
    ): KronoConfiguration {
        val result =
            includeCommonConfiguration(
                KronoConfiguration(
                    mutableListOf(
                        SlashDateFormatParser(littleEndian),
                        ENMonthNameLittleEndianParser(),
                        ENMonthNameMiddleEndianParser(littleEndian),
                        ENWeekdayParser(),
                        ENCasualYearMonthDayParser(),
                    ),
                    mutableListOf(
                        ENMergeDateTimeRefiner(),
                    ),
                ),
                strictMode,
            )

        /*
        {
                parsers: [
                    new SlashDateFormatParser(littleEndian),
                    new ENTimeUnitWithinFormatParser(strictMode),
                    new ENMonthNameLittleEndianParser(),
                    new ENMonthNameMiddleEndianParser(/*shouldSkipYearLikeDate=*/ littleEndian),
                    new ENWeekdayParser(),
                    new ENCasualYearMonthDayParser,
                    new ENSlashMonthFormatParser(),
                    new ENTimeExpressionParser(strictMode),
                    new ENTimeUnitAgoFormatParser(strictMode),
                    new ENTimeUnitLaterFormatParser(strictMode),
                ]
            },
         */

        result.refiners.addFirst(ENMergeRelativeFollowByDateRefiner())
        result.refiners.addFirst(ENMergeRelativeAfterDateRefiner())
        result.refiners.addFirst(OverlapRemovalRefiner())

        result.refiners.addLast(ENMergeDateRangeRefiner())

        return result
    }

    fun createCasualConfiguration(littleEndian: Boolean = false): KronoConfiguration {
        val result = createConfiguration(false, littleEndian)
        result.parsers.addLast(ENCasualDateParser())
        result.parsers.addLast(ENCasualTimeParser())
        result.parsers.addLast(ENMonthNameParser())
//        option.parsers.push(new ENRelativeDateFormatParser());
//        option.parsers.push(new ENTimeUnitCasualRelativeFormatParser());
        return result
    }
}
