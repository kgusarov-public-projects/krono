package org.kgusarov.krono.locales.es

import org.kgusarov.krono.KronoConfiguration
import org.kgusarov.krono.common.parsers.SlashDateFormatParser
import org.kgusarov.krono.includeCommonConfiguration
import org.kgusarov.krono.locales.es.parsers.EsCasualDateParser
import org.kgusarov.krono.locales.es.parsers.EsCasualTimeParser
import org.kgusarov.krono.locales.es.parsers.EsMonthNameLittleEndianParser
import org.kgusarov.krono.locales.es.parsers.EsTimeExpressionParser
import org.kgusarov.krono.locales.es.parsers.EsTimeUnitWithinFormatParser
import org.kgusarov.krono.locales.es.parsers.EsWeekdayParser
import org.kgusarov.krono.locales.es.refiners.EsMergeDateRangeRefiner
import org.kgusarov.krono.locales.es.refiners.EsMergeDateTimeRefiner

class EsDefaultConfiguration {
    fun createConfiguration(
        strictMode: Boolean = true,
        littleEndian: Boolean = true,
    ): KronoConfiguration =
        includeCommonConfiguration(
            KronoConfiguration(
                mutableListOf(
                    SlashDateFormatParser(littleEndian),
                    EsWeekdayParser(),
                    EsTimeExpressionParser(),
                    EsMonthNameLittleEndianParser(),
                    EsTimeUnitWithinFormatParser(),
                ),
                mutableListOf(
                    EsMergeDateTimeRefiner(),
                    EsMergeDateRangeRefiner(),
                ),
            ),
            strictMode,
        )

    fun createCasualConfiguration(littleEndian: Boolean = true): KronoConfiguration {
        val result = createConfiguration(false, littleEndian)
        result.parsers.addLast(EsCasualDateParser())
        result.parsers.addLast(EsCasualTimeParser())
        return result
    }
}
