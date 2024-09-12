package org.kgusarov.krono.locales.es.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.common.parsers.IberoRomanMonthNameLittleEndianParser
import org.kgusarov.krono.locales.es.EsConstants
import org.kgusarov.krono.locales.es.parseYear

@SuppressFBWarnings("EI_EXPOSE_REP")
class EsMonthNameLittleEndianParser : IberoRomanMonthNameLittleEndianParser() {
    override fun getMonthDictionary() = EsConstants.MONTH_DICTIONARY

    override fun getYearPattern() = EsConstants.YEAR_PATTERN

    override fun getParseYear() = ::parseYear
}
