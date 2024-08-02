package org.kgusarov.krono

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.extensions.plus
import org.kgusarov.krono.locales.en.En

@SuppressFBWarnings("SING_SINGLETON_HAS_NONPRIVATE_CONSTRUCTOR")
class Krono(configuration: KronoConfiguration) {
    private val parsers: Array<Parser> = configuration.parsers.toTypedArray()
    private val refiners: Array<Refiner> = configuration.refiners.toTypedArray()

    fun copy() = Krono(KronoConfiguration(parsers.toMutableList(), refiners.toMutableList()))

    fun parse(
        text: String,
        refDate: RefDateInput,
        option: ParsingOption? = null,
    ): List<ParsedResult> {
        val context = ParsingContext(text, refDate, option)

        var results: MutableList<ParsedResult> = mutableListOf()
        for (parser in parsers) {
            val parsedResults = executeParser(context, parser)
            results += parsedResults
        }

        results.sortBy { it.index }
        for (refiner in refiners) {
            results = refiner(context, results)
        }

        return results
    }

    fun parseDate(
        text: String,
        refDate: RefDateInput,
        option: ParsingOption? = null,
    ): KronoDate? {
        val results = parse(text, refDate, option)
        return if (results.isEmpty()) null else results[0].start.instant()
    }

    companion object {
        @JvmStatic
        val enCasual = Krono(En.casual)

        @JvmStatic
        val enStrict = Krono(En.strict)

        private fun executeParser(
            context: ParsingContext,
            parser: Parser,
        ): List<ParsedResult> {
            val results: MutableList<ParsedResult> = mutableListOf()
            val pattern = parser.pattern(context)

            val originalText = context.text
            var remainingText = context.text
            var matchResult = pattern.find(remainingText)

            while (matchResult != null) {
                val match = RegExpMatchArray(matchResult)
                val index = match.index + originalText.length - remainingText.length
                match.index = index

                val result = parser(context, match)
                if (result == null) {
                    remainingText = originalText.substring(match.index + 1)
                    matchResult = pattern.find(remainingText)
                    continue
                }

                val parsedResult = result(context, match.index!!, TextOrEndIndexInputFactory(match[0]!!))
                val parsedIndex = parsedResult.index
                val parsedText = parsedResult.text

                context {
                    "${Krono::class.java.simpleName} extracted '$parsedText' at index $parsedIndex"
                }

                results += parsedResult
                remainingText = originalText.substring(parsedIndex + parsedText.length)
                matchResult = pattern.find(remainingText)
            }

            return results
        }
    }
}
