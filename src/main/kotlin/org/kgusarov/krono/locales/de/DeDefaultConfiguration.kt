package org.kgusarov.krono.locales.de

import org.kgusarov.krono.KronoConfiguration
import org.kgusarov.krono.common.parsers.ISOFormatParser
import org.kgusarov.krono.common.parsers.SlashDateFormatParser
import org.kgusarov.krono.includeCommonConfiguration
import org.kgusarov.krono.locales.de.parsers.DeCasualDateParser
import org.kgusarov.krono.locales.de.parsers.DeCasualTimeParser
import org.kgusarov.krono.locales.de.parsers.DeMonthNameLittleEndianParser
import org.kgusarov.krono.locales.de.parsers.DeSpecificTimeExpressionParser
import org.kgusarov.krono.locales.de.parsers.DeTimeExpressionParser
import org.kgusarov.krono.locales.de.parsers.DeTimeUnitRelativeFormatParser
import org.kgusarov.krono.locales.de.parsers.DeTimeUnitWithinFormatParser
import org.kgusarov.krono.locales.de.parsers.DeWeekdayParser
import org.kgusarov.krono.locales.de.refiners.DeMergeDateRangeRefiner
import org.kgusarov.krono.locales.de.refiners.DeMergeDateTimeRefiner

class DeDefaultConfiguration {
    fun createConfiguration(
        strictMode: Boolean = true,
        littleEndian: Boolean = true,
    ): KronoConfiguration =
        includeCommonConfiguration(
            KronoConfiguration(
                mutableListOf(
                    ISOFormatParser(),
                    SlashDateFormatParser(littleEndian),
                    DeTimeExpressionParser(),
                    DeSpecificTimeExpressionParser(),
                    DeMonthNameLittleEndianParser(),
                    DeWeekdayParser(),
                    DeTimeUnitWithinFormatParser(),
                ),
                mutableListOf(
                    DeMergeDateRangeRefiner(),
                    DeMergeDateTimeRefiner(),
                ),
            ),
            strictMode,
        )

    fun createCasualConfiguration(littleEndian: Boolean = true): KronoConfiguration {
        val result = createConfiguration(false, littleEndian)
        result.parsers.addFirst(DeCasualTimeParser())
        result.parsers.addFirst(DeCasualDateParser())
        result.parsers.addFirst(DeTimeUnitRelativeFormatParser())
        return result
    }
}
