package org.kgusarov.krono

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.extensions.plus
import org.kgusarov.krono.extensions.safeSubstring
import org.kgusarov.krono.locales.en.En
import org.kgusarov.krono.locales.es.Es
import org.kgusarov.krono.locales.fr.Fr
import org.kgusarov.krono.locales.ja.Ja
import org.kgusarov.krono.locales.pt.Pt
import java.time.Duration

@SuppressFBWarnings("SING_SINGLETON_HAS_NONPRIVATE_CONSTRUCTOR", "EI_EXPOSE_REP")
class Krono(configuration: KronoConfiguration) {
    val parsers: Array<Parser> = configuration.parsers.toTypedArray()
    val refiners: Array<Refiner> = configuration.refiners.toTypedArray()

    init {
        verifyOpenedPackage()
    }

    fun copy() = Krono(KronoConfiguration(parsers.toMutableList(), refiners.toMutableList()))

    fun parse(
        text: String,
        option: ParsingOption? = null,
    ): List<ParsedResult> = parse(text, RefDateInputFactory(KronoDate.now()), option)

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

        @JvmStatic
        val enGb = Krono(En.gb)

        @JvmStatic
        val jaCasual = Krono(Ja.casual)

        @JvmStatic
        val jaStrict = Krono(Ja.strict)

        @JvmStatic
        val ptCasual = Krono(Pt.casual)

        @JvmStatic
        val ptStrict = Krono(Pt.strict)

        @JvmStatic
        val esCasual = Krono(Es.casual)

        @JvmStatic
        val esStrict = Krono(Es.strict)

        @JvmStatic
        val frStrict = Krono(Fr.strict)

        @JvmStatic
        val frCasual = Krono(Fr.casual)

        @JvmStatic
        private fun verifyOpenedPackage() {
            try {
                with(Duration::class.java.getDeclaredMethod("toBigDecimalSeconds")) {
                    isAccessible = true
                }
            } catch (ignored: Exception) {
                throw IllegalStateException("Krono requires java.base/java.time to be opened")
            }
        }

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
                    remainingText = originalText.safeSubstring(match.index + 1)
                    matchResult = pattern.find(remainingText)
                    continue
                }

                val parsedResult = result(context, match.index!!, TextOrEndIndexInputFactory(match[0]!!))
                val parsedIndex = parsedResult.index
                val parsedText = parsedResult.text

                context {
                    "${parser::class.java.simpleName} extracted '$parsedText' at index $parsedIndex -> $parsedResult"
                }

                results += parsedResult
                remainingText = originalText.substring(parsedIndex + parsedText.length)
                matchResult = pattern.find(remainingText)
            }

            return results
        }
    }
}
