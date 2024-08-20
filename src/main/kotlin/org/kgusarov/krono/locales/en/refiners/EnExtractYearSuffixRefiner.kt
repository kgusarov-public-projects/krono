package org.kgusarov.krono.locales.en.refiners

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.ParsedResult
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.Refiner
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.locales.en.EnConstants
import org.kgusarov.krono.locales.en.parseYear

@SuppressFBWarnings("EI_EXPOSE_REP")
class EnExtractYearSuffixRefiner : Refiner {
    override fun invoke(
        context: ParsingContext,
        results: MutableList<ParsedResult>,
    ): MutableList<ParsedResult> {
        results.forEach {
            if (!it.start.dateWithUnknownYear()) {
                return@forEach
            }

            val suffix = context.text.substring(it.index + it.text.length)
            val rawMatch = YEAR_SUFFIX_PATTERN.find(suffix) ?: return@forEach
            val match = RegExpMatchArray(rawMatch)

            context {
                "${javaClass.simpleName} extracted year '${match[0]}'"
            }

            val year = parseYear(match[YEAR_GROUP]!!)
            if (it.end != null) {
                it.end!!.assign(KronoComponents.Year, year)
            }

            it.start.assign(KronoComponents.Year, year)
            it.text += match[0]!!
        }

        return results
    }

    companion object {
        @JvmStatic
        private val YEAR_SUFFIX_PATTERN = Regex("^\\s*(${EnConstants.YEAR_PATTERN})", RegexOption.IGNORE_CASE)

        private const val YEAR_GROUP = 1
    }
}
