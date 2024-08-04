package org.kgusarov.krono.common.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.ComponentsInputFactory
import org.kgusarov.krono.KronoComponent
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import java.util.concurrent.TimeUnit

@SuppressFBWarnings("EI_EXPOSE_REP")
class ISOFormatParser : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult {
        val components = mutableMapOf<KronoComponent, Int>()
        components[KronoComponents.Year] = match.getInt(YEAR_NUMBER_GROUP)
        components[KronoComponents.Month] = match.getInt(MONTH_NUMBER_GROUP)
        components[KronoComponents.Day] = match.getInt(DATE_NUMBER_GROUP)

        if (!match[HOUR_NUMBER_GROUP].isNullOrEmpty()) {
            components[KronoComponents.Hour] = match.getInt(HOUR_NUMBER_GROUP)
            components[KronoComponents.Minute] = match.getInt(MINUTE_NUMBER_GROUP)

            if (!match[SECOND_NUMBER_GROUP].isNullOrEmpty()) {
                components[KronoComponents.Second] = match.getInt(SECOND_NUMBER_GROUP)
            }

            if (!match[MILLISECOND_NUMBER_GROUP].isNullOrEmpty()) {
                components[KronoComponents.Millisecond] = match.getInt(MILLISECOND_NUMBER_GROUP)
            }

            if (!match[TZD_HOUR_OFFSET_GROUP].isNullOrEmpty()) {
                val hourOffset = match.getInt(TZD_HOUR_OFFSET_GROUP)
                val minuteOffset = match.getInt(TZD_MINUTE_OFFSET_GROUP)

                val offset =
                    TimeUnit.HOURS.toSeconds(hourOffset.toLong()) +
                        TimeUnit.MINUTES.toSeconds(minuteOffset.toLong())
                components[KronoComponents.Offset] = offset.toInt()
            } else {
                components[KronoComponents.Offset] = 0
            }
        }

        return ParserResultFactory(ComponentsInputFactory(components))
    }

    @Suppress("RegExpRedundantEscape", "RegExpSimplifiable")
    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "([0-9]{4})\\-([0-9]{1,2})\\-([0-9]{1,2})" +
                    "(?:T" +
                    "([0-9]{1,2}):([0-9]{1,2})" +
                    "(?:" +
                    ":([0-9]{1,2})(?:\\.(\\d{1,4}))?" +
                    ")?" +
                    "(?:" +
                    "Z|([+-]\\d{2}):?(\\d{2})?" +
                    ")?" +
                    ")?" +
                    "(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )

        const val YEAR_NUMBER_GROUP = 1
        const val MONTH_NUMBER_GROUP = 2
        const val DATE_NUMBER_GROUP = 3
        const val HOUR_NUMBER_GROUP = 4
        const val MINUTE_NUMBER_GROUP = 5
        const val SECOND_NUMBER_GROUP = 6
        const val MILLISECOND_NUMBER_GROUP = 7
        const val TZD_HOUR_OFFSET_GROUP = 8
        const val TZD_MINUTE_OFFSET_GROUP = 9
    }
}
