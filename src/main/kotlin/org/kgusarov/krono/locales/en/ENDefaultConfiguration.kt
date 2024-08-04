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
import org.kgusarov.krono.locales.en.parsers.ENRelativeDateFormatParser
import org.kgusarov.krono.locales.en.parsers.ENSlashMonthFormatParser
import org.kgusarov.krono.locales.en.parsers.ENTimeExpressionParser
import org.kgusarov.krono.locales.en.parsers.ENTimeUnitAgoFormatParser
import org.kgusarov.krono.locales.en.parsers.ENTimeUnitCasualRelativeFormatParser
import org.kgusarov.krono.locales.en.parsers.ENTimeUnitLaterFormatParser
import org.kgusarov.krono.locales.en.parsers.ENTimeUnitWithinFormatParser
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
                        ENTimeUnitWithinFormatParser(strictMode),
                        ENMonthNameLittleEndianParser(),
                        ENMonthNameMiddleEndianParser(littleEndian),
                        ENWeekdayParser(),
                        ENCasualYearMonthDayParser(),
                        ENSlashMonthFormatParser(),
                        ENTimeExpressionParser(strictMode),
                        ENTimeUnitAgoFormatParser(strictMode),
                        ENTimeUnitLaterFormatParser(strictMode),
                    ),
                    mutableListOf(
                        ENMergeDateTimeRefiner(),
                    ),
                ),
                strictMode,
            )

        result.refiners.addFirst(ENMergeRelativeFollowByDateRefiner())
        result.refiners.addFirst(ENMergeRelativeAfterDateRefiner())
        result.refiners.addFirst(OverlapRemovalRefiner())

        result.refiners.addLast(ENMergeDateTimeRefiner())
        result.refiners.addLast(ENMergeDateRangeRefiner())

        return result
    }

    fun createCasualConfiguration(littleEndian: Boolean = false): KronoConfiguration {
        val result = createConfiguration(false, littleEndian)
        result.parsers.addLast(ENCasualDateParser())
        result.parsers.addLast(ENCasualTimeParser())
        result.parsers.addLast(ENMonthNameParser())
        result.parsers.addLast(ENRelativeDateFormatParser())
        result.parsers.addLast(ENTimeUnitCasualRelativeFormatParser())
        return result
    }
}
