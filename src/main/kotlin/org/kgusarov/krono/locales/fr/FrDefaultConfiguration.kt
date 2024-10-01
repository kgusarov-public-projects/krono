package org.kgusarov.krono.locales.fr

import org.kgusarov.krono.KronoConfiguration
import org.kgusarov.krono.common.parsers.SlashDateFormatParser
import org.kgusarov.krono.includeCommonConfiguration
import org.kgusarov.krono.locales.fr.parsers.FrCasualDateParser
import org.kgusarov.krono.locales.fr.parsers.FrCasualTimeParser
import org.kgusarov.krono.locales.fr.parsers.FrMonthNameLittleEndianParser
import org.kgusarov.krono.locales.fr.parsers.FrSpecificTimeExpressionParser
import org.kgusarov.krono.locales.fr.parsers.FrTimeExpressionParser
import org.kgusarov.krono.locales.fr.parsers.FrTimeUnitAgoFormatParser
import org.kgusarov.krono.locales.fr.parsers.FrTimeUnitRelativeFormatParser
import org.kgusarov.krono.locales.fr.parsers.FrTimeUnitWithinFormatParser
import org.kgusarov.krono.locales.fr.parsers.FrWeekdayParser
import org.kgusarov.krono.locales.fr.refiners.FrMergeDateRangeRefiner
import org.kgusarov.krono.locales.fr.refiners.FrMergeDateTimeRefiner

class FrDefaultConfiguration {
    fun createConfiguration(
        strictMode: Boolean = true,
        littleEndian: Boolean = true,
    ): KronoConfiguration =
        includeCommonConfiguration(
            KronoConfiguration(
                mutableListOf(
                    SlashDateFormatParser(littleEndian),
                    FrMonthNameLittleEndianParser(),
                    FrTimeExpressionParser(),
                    FrSpecificTimeExpressionParser(),
                    FrTimeUnitAgoFormatParser(),
                    FrTimeUnitWithinFormatParser(),
                    FrWeekdayParser(),
                ),
                mutableListOf(
                    FrMergeDateTimeRefiner(),
                    FrMergeDateRangeRefiner(),
                ),
            ),
            strictMode,
        )

    fun createCasualConfiguration(littleEndian: Boolean = true): KronoConfiguration {
        val result = createConfiguration(false, littleEndian)
        result.parsers.addLast(FrCasualDateParser())
        result.parsers.addLast(FrCasualTimeParser())
        result.parsers.addLast(FrTimeUnitRelativeFormatParser())
        return result
    }
}
