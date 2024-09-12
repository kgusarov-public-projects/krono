package org.kgusarov.krono.locales.pt.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.common.parsers.IberoRomanMonthNameLittleEndianParser
import org.kgusarov.krono.locales.pt.PtConstants
import org.kgusarov.krono.locales.pt.parseYear

@SuppressFBWarnings("EI_EXPOSE_REP")
class PtMonthNameLittleEndianParser : IberoRomanMonthNameLittleEndianParser() {
    override fun getMonthDictionary() = PtConstants.MONTH_DICTIONARY

    override fun getYearPattern() = PtConstants.YEAR_PATTERN

    override fun getParseYear() = ::parseYear
}
