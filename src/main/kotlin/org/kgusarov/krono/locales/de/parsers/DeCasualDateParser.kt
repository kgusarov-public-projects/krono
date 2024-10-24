package org.kgusarov.krono.locales.de.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.KronoUnit
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.now
import org.kgusarov.krono.common.parsers.AbstractParserWithWordBoundaryChecking
import org.kgusarov.krono.common.today
import org.kgusarov.krono.extensions.add
import org.kgusarov.krono.extensions.assignSimilarDate
import org.kgusarov.krono.extensions.assignTheNextDay
import org.kgusarov.krono.extensions.implySimilarTime

private val LAST_NIGHT = Regex("letzte\\s*nacht", RegexOption.IGNORE_CASE)

@SuppressFBWarnings("EI_EXPOSE_REP")
class DeCasualDateParser : AbstractParserWithWordBoundaryChecking() {
    override fun innerPattern(context: ParsingContext) = PATTERN

    override fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult {
        val targetDate = context.instant
        val dateKeyword =
            (
                if (match[DATE_GROUP].isNullOrEmpty()) {
                    ""
                } else {
                    match[DATE_GROUP]!!
                }
            ).lowercase()

        val timeKeyword =
            (
                if (match[TIME_GROUP].isNullOrEmpty()) {
                    ""
                } else {
                    match[TIME_GROUP]!!
                }
            ).lowercase()

        var component = context.createParsingComponents()
        when (dateKeyword) {
            "jetzt" -> component = now(context.reference)
            "heute" -> component = today(context.reference)
            "morgen" -> targetDate.assignTheNextDay(component)
            "übermorgen", "uebermorgen" -> {
                targetDate.add(KronoUnit.Day, 1).assignTheNextDay(component)
            }

            "gestern" -> {
                targetDate.add(KronoUnit.Day, -1).apply {
                    assignSimilarDate(component)
                    implySimilarTime(component)
                }
            }

            "vorgestern" -> {
                targetDate.add(KronoUnit.Day, -2).apply {
                    assignSimilarDate(component)
                    implySimilarTime(component)
                }
            }

            else -> {
                if (dateKeyword.contains(LAST_NIGHT)) {
                    val newTargetDate =
                        if (targetDate.hour > 6) {
                            targetDate.add(KronoUnit.Day, -1)
                        } else {
                            targetDate
                        }

                    newTargetDate.assignSimilarDate(component)
                    component.imply(KronoComponents.Hour, 0)
                }
            }
        }

        if (timeKeyword.isNotEmpty()) {
            component = DeCasualTimeParser.extractTimeComponents(component, timeKeyword)
        }

        return ParserResultFactory(component)
    }

    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "(jetzt|heute|morgen|übermorgen|uebermorgen|gestern|vorgestern|letzte\\s*nacht)" +
                    "(?:\\s*(morgen|vormittag|mittags?|nachmittag|abend|nacht|mitternacht))?" +
                    "(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )

        private const val DATE_GROUP = 1
        private const val TIME_GROUP = 2
    }
}
