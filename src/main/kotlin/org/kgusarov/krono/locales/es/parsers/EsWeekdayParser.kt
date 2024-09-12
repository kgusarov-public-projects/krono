package org.kgusarov.krono.locales.es.parsers

import org.kgusarov.krono.common.parsers.IberoRomanWeekdayParser
import org.kgusarov.krono.locales.es.EsConstants

class EsWeekdayParser : IberoRomanWeekdayParser() {
    override fun getThisModifier() = "pasado"

    override fun getWeekdayDictionary() = EsConstants.WEEKDAY_DICTIONARY
}
