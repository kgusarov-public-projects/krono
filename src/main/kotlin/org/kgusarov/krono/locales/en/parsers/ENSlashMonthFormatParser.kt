package org.kgusarov.krono.locales.en.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.parsers.AbstractParserWithWordBoundaryChecking

@SuppressFBWarnings("EI_EXPOSE_REP")
class ENSlashMonthFormatParser : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult {
        val year = match.getInt(YEAR_GROUP)
        val month = match.getInt(MONTH_GROUP)

        val components =
            context.createParsingComponents()
                .imply(KronoComponents.Day, 1)
                .assign(KronoComponents.Month, month)
                .assign(KronoComponents.Year, year)

        return ParserResultFactory(components)
    }

    companion object {
        @JvmStatic
        private val PATTERN = Regex("([0-9]|0[1-9]|1[012])/([0-9]{4})", RegexOption.IGNORE_CASE)

        private const val MONTH_GROUP = 1
        private const val YEAR_GROUP = 2
    }
}
