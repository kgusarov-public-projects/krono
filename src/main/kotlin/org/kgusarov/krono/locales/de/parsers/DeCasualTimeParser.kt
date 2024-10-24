package org.kgusarov.krono.locales.de.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.KronoMeridiem
import org.kgusarov.krono.KronoUnit
import org.kgusarov.krono.ParsedComponents
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.parsers.AbstractParserWithWordBoundaryChecking
import org.kgusarov.krono.extensions.compareTo
import org.kgusarov.krono.extensions.implySimilarTime
import org.kgusarov.krono.utils.addImpliedTimeUnits

@SuppressFBWarnings("EI_EXPOSE_REP")
class DeCasualTimeParser : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult {
        val targetDate = context.instant
        val timeKeywordPattern = match[2]!!.lowercase()
        val component = context.createParsingComponents()
        targetDate.implySimilarTime(component)
        return ParserResultFactory(extractTimeComponents(component, timeKeywordPattern))
    }

    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "(diesen)?\\s*(morgen|vormittag|mittags?|nachmittag|abend|nacht|mitternacht)(?=\\W|\$)",
                RegexOption.IGNORE_CASE,
            )

        @JvmStatic
        fun extractTimeComponents(
            components: ParsedComponents,
            timeKeywordPattern: String,
        ): ParsedComponents {
            var result = components
            when (timeKeywordPattern) {
                "morgen" -> {
                    result.imply(KronoComponents.Hour, 6)
                    result.imply(KronoComponents.Minute, 0)
                    result.imply(KronoComponents.Second, 0)
                    result.imply(KronoComponents.Meridiem, KronoMeridiem.AM)
                }

                "vormittag" -> {
                    result.imply(KronoComponents.Hour, 9)
                    result.imply(KronoComponents.Minute, 0)
                    result.imply(KronoComponents.Second, 0)
                    result.imply(KronoComponents.Meridiem, KronoMeridiem.AM)
                }

                "mittag", "mittags" -> {
                    result.imply(KronoComponents.Hour, 12)
                    result.imply(KronoComponents.Minute, 0)
                    result.imply(KronoComponents.Second, 0)
                    result.imply(KronoComponents.Meridiem, KronoMeridiem.AM)
                }

                "nachmittag" -> {
                    result.imply(KronoComponents.Hour, 15)
                    result.imply(KronoComponents.Minute, 0)
                    result.imply(KronoComponents.Second, 0)
                    result.imply(KronoComponents.Meridiem, KronoMeridiem.PM)
                }

                "abend" -> {
                    result.imply(KronoComponents.Hour, 18)
                    result.imply(KronoComponents.Minute, 0)
                    result.imply(KronoComponents.Second, 0)
                    result.imply(KronoComponents.Meridiem, KronoMeridiem.PM)
                }

                "nacht" -> {
                    result.imply(KronoComponents.Hour, 22)
                    result.imply(KronoComponents.Minute, 0)
                    result.imply(KronoComponents.Second, 0)
                    result.imply(KronoComponents.Meridiem, KronoMeridiem.PM)
                }

                "mitternacht" -> {
                    if (result[KronoComponents.Hour] > 1) {
                        result = addImpliedTimeUnits(result, mutableMapOf(KronoUnit.Day to 1))
                    }
                    result.imply(KronoComponents.Hour, 0)
                    result.imply(KronoComponents.Minute, 0)
                    result.imply(KronoComponents.Second, 0)
                    result.imply(KronoComponents.Meridiem, KronoMeridiem.AM)
                }
            }

            return result
        }
    }
}
