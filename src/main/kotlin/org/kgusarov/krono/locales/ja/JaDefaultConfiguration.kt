package org.kgusarov.krono.locales.ja

import org.kgusarov.krono.KronoConfiguration
import org.kgusarov.krono.locales.ja.parsers.JaCasualDateParser
import org.kgusarov.krono.locales.ja.parsers.JaStandardParser
import org.kgusarov.krono.locales.ja.refiners.JaMergeDateRangeRefiner

class JaDefaultConfiguration {
    fun createConfiguration(): KronoConfiguration =
        KronoConfiguration(
            mutableListOf(
                JaStandardParser(),
            ),
            mutableListOf(
                JaMergeDateRangeRefiner(),
            ),
        )

    fun createCasualConfiguration(): KronoConfiguration {
        val result = createConfiguration()
        result.parsers.addFirst(JaCasualDateParser())
        return result
    }
}
