package org.kgusarov.krono.common.refiners

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.ParsedResult
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.Refiner
import org.kgusarov.krono.TimezoneAbbreviations
import org.kgusarov.krono.extensions.get
import java.time.ZoneId

@SuppressFBWarnings("EI_EXPOSE_REP", "EI_EXPOSE_REP2")
class ExtractTimezoneAbbrRefiner(
    private val abbreviationMap: Map<String, ZoneId> = TimezoneAbbreviations.TIMEZONE_ABBR_MAP,
) : Refiner {
    override fun invoke(
        context: ParsingContext,
        results: MutableList<ParsedResult>,
    ): MutableList<ParsedResult> {
        results.forEach {
            val suffix = context.text.substring(it.index + it.text.length)
            val match = TIMEZONE_NAME_PATTERN.find(suffix) ?: return@forEach
            val timezoneAbbr = match[1].uppercase()
            val refDate = it.start.instant()
            val extractedTimezoneOffset =
                (
                    try {
                        ZoneId.of(timezoneAbbr)
                    } catch (_: Exception) {
                        abbreviationMap[timezoneAbbr] ?: return@forEach
                    }
                ).rules.getOffset(refDate)!!

            context {
                "${javaClass.simpleName} extracted timezone '$timezoneAbbr' ($extractedTimezoneOffset)"
            }

            val currentTimezoneOffset = it.start[KronoComponents.Offset]
            val extractedOffsetSeconds = extractedTimezoneOffset.totalSeconds
            if (currentTimezoneOffset != null && extractedOffsetSeconds != currentTimezoneOffset) {
                if (it.start.isCertain(KronoComponents.Offset)) {
                    return@forEach
                }

                if (timezoneAbbr != match[1]) {
                    return@forEach
                }
            }

            if (it.start.onlyDate()) {
                if (timezoneAbbr != match[1]) {
                    return@forEach
                }
            }

            it.text += match.value
            if (!it.start.isCertain(KronoComponents.Offset)) {
                it.start.assign(KronoComponents.Offset, extractedOffsetSeconds)
            }

            if (it.end != null && !it.end!!.isCertain(KronoComponents.Offset)) {
                it.end!!.assign(KronoComponents.Offset, extractedOffsetSeconds)
            }
        }

        return results
    }

    companion object {
        @JvmStatic
        private val TIMEZONE_NAME_PATTERN =
            Regex(
                "^\\s*,?\\s*\\(?([A-Z]{2,4})\\)?(?=\\W|$)",
                RegexOption.IGNORE_CASE,
            )
    }
}
