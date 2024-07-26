package org.kgusarov.krono.common.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.Parser
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.extensions.plus

@SuppressFBWarnings("EI_EXPOSE_REP")
abstract class AbstractParserWithWordBoundaryChecking : Parser {
    private var cachedInnerPattern: Regex? = null
    private var cachedPattern: Regex? = null

    val patternLeftBoundary = "(\\W|^)"

    fun innerPatternHasChange(
        context: ParsingContext,
        currentInnerPattern: Regex,
    ): Boolean = innerPattern(context) != currentInnerPattern

    override fun pattern(context: ParsingContext): Regex {
        val cachedInner = cachedInnerPattern
        if (cachedInner != null) {
            if (!innerPatternHasChange(context, cachedInner)) {
                return cachedPattern!!
            }
        }

        cachedInnerPattern = innerPattern(context)
        cachedPattern =
            Regex(
                "$patternLeftBoundary${cachedInnerPattern!!.pattern}",
                cachedInnerPattern!!.options,
            )

        return cachedPattern!!
    }

    override fun invoke(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult? {
        val header = match[1] ?: ""
        match.index += header.length
        match[0] = match[0]!!.substring(header.length)

        for (i in 2..<match.length) {
            match[i - 1] = match[i]
        }

        return innerExtract(context, match)
    }

    abstract fun innerPattern(context: ParsingContext): Regex

    abstract fun innerExtract(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult?
}
