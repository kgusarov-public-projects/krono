package org.kgusarov.krono.locales.en

import org.kgusarov.krono.KronoConfiguration
import org.kgusarov.krono.common.parsers.SlashDateFormatParser
import org.kgusarov.krono.common.refiners.OverlapRemovalRefiner
import org.kgusarov.krono.includeCommonConfiguration
import org.kgusarov.krono.locales.en.parsers.ENYearMonthDayParser
import org.kgusarov.krono.locales.en.parsers.EnCasualDateParser
import org.kgusarov.krono.locales.en.parsers.EnCasualTimeParser
import org.kgusarov.krono.locales.en.parsers.EnMonthNameLittleEndianParser
import org.kgusarov.krono.locales.en.parsers.EnMonthNameMiddleEndianParser
import org.kgusarov.krono.locales.en.parsers.EnMonthNameParser
import org.kgusarov.krono.locales.en.parsers.EnRelativeDateFormatParser
import org.kgusarov.krono.locales.en.parsers.EnSlashMonthFormatParser
import org.kgusarov.krono.locales.en.parsers.EnTimeExpressionParser
import org.kgusarov.krono.locales.en.parsers.EnTimeUnitAgoFormatParser
import org.kgusarov.krono.locales.en.parsers.EnTimeUnitCasualRelativeFormatParser
import org.kgusarov.krono.locales.en.parsers.EnTimeUnitLaterFormatParser
import org.kgusarov.krono.locales.en.parsers.EnTimeUnitWithinFormatParser
import org.kgusarov.krono.locales.en.parsers.EnWeekdayParser
import org.kgusarov.krono.locales.en.refiners.EnExtractYearSuffixRefiner
import org.kgusarov.krono.locales.en.refiners.EnMergeDateRangeRefiner
import org.kgusarov.krono.locales.en.refiners.EnMergeDateTimeRefiner
import org.kgusarov.krono.locales.en.refiners.EnMergeRelativeAfterDateRefiner
import org.kgusarov.krono.locales.en.refiners.EnMergeRelativeFollowByDateRefiner

class EnDefaultConfiguration {
    fun createConfiguration(
        strictMode: Boolean = true,
        littleEndian: Boolean = false,
    ): KronoConfiguration {
        val result =
            includeCommonConfiguration(
                KronoConfiguration(
                    mutableListOf(
                        SlashDateFormatParser(littleEndian),
                        EnTimeUnitWithinFormatParser(strictMode),
                        EnMonthNameLittleEndianParser(),
                        EnMonthNameMiddleEndianParser(littleEndian),
                        EnWeekdayParser(),
                        EnSlashMonthFormatParser(),
                        EnTimeExpressionParser(strictMode),
                        EnTimeUnitAgoFormatParser(strictMode),
                        EnTimeUnitLaterFormatParser(strictMode),
                    ),
                    mutableListOf(
                        EnMergeDateTimeRefiner(),
                    ),
                ),
                strictMode,
            )

        result.parsers.addFirst(ENYearMonthDayParser(strictMode))

        result.refiners.addFirst(EnMergeRelativeFollowByDateRefiner())
        result.refiners.addFirst(EnMergeRelativeAfterDateRefiner())
        result.refiners.addFirst(OverlapRemovalRefiner())

        result.refiners.addLast(EnMergeDateTimeRefiner())
        result.refiners.addLast(EnExtractYearSuffixRefiner())
        result.refiners.addLast(EnMergeDateRangeRefiner())

        return result
    }

    fun createCasualConfiguration(littleEndian: Boolean = false): KronoConfiguration {
        val result = createConfiguration(false, littleEndian)
        result.parsers.addLast(EnCasualDateParser())
        result.parsers.addLast(EnCasualTimeParser())
        result.parsers.addLast(EnMonthNameParser())
        result.parsers.addLast(EnRelativeDateFormatParser())
        result.parsers.addLast(EnTimeUnitCasualRelativeFormatParser())
        return result
    }
}
