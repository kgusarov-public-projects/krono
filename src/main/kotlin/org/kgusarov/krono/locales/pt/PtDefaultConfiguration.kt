package org.kgusarov.krono.locales.pt

import org.kgusarov.krono.KronoConfiguration
import org.kgusarov.krono.common.parsers.SlashDateFormatParser
import org.kgusarov.krono.includeCommonConfiguration
import org.kgusarov.krono.locales.pt.parsers.PtCasualDateParser
import org.kgusarov.krono.locales.pt.parsers.PtCasualTimeParser
import org.kgusarov.krono.locales.pt.parsers.PtMonthNameLittleEndianParser
import org.kgusarov.krono.locales.pt.parsers.PtTimeExpressionParser
import org.kgusarov.krono.locales.pt.parsers.PtWeekdayParser
import org.kgusarov.krono.locales.pt.refiners.PtMergeDateRangeRefiner
import org.kgusarov.krono.locales.pt.refiners.PtMergeDateTimeRefiner

class PtDefaultConfiguration {
    fun createConfiguration(
        strictMode: Boolean = true,
        littleEndian: Boolean = true,
    ): KronoConfiguration =
        includeCommonConfiguration(
            KronoConfiguration(
                mutableListOf(
                    SlashDateFormatParser(littleEndian),
                    PtWeekdayParser(),
                    PtTimeExpressionParser(),
                    PtMonthNameLittleEndianParser(),
                ),
                mutableListOf(
                    PtMergeDateTimeRefiner(),
                    PtMergeDateRangeRefiner(),
                ),
            ),
            strictMode,
        )

    fun createCasualConfiguration(littleEndian: Boolean = true): KronoConfiguration {
        val result = createConfiguration(false, littleEndian)
        result.parsers.addLast(PtCasualDateParser())
        result.parsers.addLast(PtCasualTimeParser())
        return result
    }
}
