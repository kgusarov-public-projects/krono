package org.kgusarov.krono.locales.pt.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.common.parsers.IberoRomanWeekdayParser
import org.kgusarov.krono.locales.pt.PtConstants

@SuppressFBWarnings("EI_EXPOSE_REP")
class PtWeekdayParser : IberoRomanWeekdayParser() {
    override fun getThisModifier() = "passado"

    override fun getWeekdayDictionary() = PtConstants.WEEKDAY_DICTIONARY
}
