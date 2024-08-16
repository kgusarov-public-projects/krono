package org.kgusarov.krono.locales.ja.parsers

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.kgusarov.krono.KronoComponents
import org.kgusarov.krono.KronoMeridiem
import org.kgusarov.krono.Parser
import org.kgusarov.krono.ParserResult
import org.kgusarov.krono.ParserResultFactory
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.RegExpMatchArray
import org.kgusarov.krono.common.today
import org.kgusarov.krono.common.tomorrow
import org.kgusarov.krono.common.yesterday

@SuppressFBWarnings("EI_EXPOSE_REP")
class JaCasualDateParser : Parser {
    override fun pattern(context: ParsingContext) = PATTERN

    override fun invoke(
        context: ParsingContext,
        match: RegExpMatchArray,
    ): ParserResult {
        val text = normalizeTextToKanji(match[0]!!)
        val date = context.instant
        val components = context.createParsingComponents()

        when (text) {
            "昨日" -> return ParserResultFactory(yesterday(context.reference))
            "明日" -> return ParserResultFactory(tomorrow(context.reference))
            "今日", "当日" -> return ParserResultFactory(today(context.reference))
        }

        if (text == "今夜" || text == "今夕" || text == "今晩") {
            components.imply(KronoComponents.Hour, 22)
            components.assign(KronoComponents.Meridiem, KronoMeridiem.PM)
        } else if (text == "今朝") {
            components.imply(KronoComponents.Hour, 6)
            components.assign(KronoComponents.Meridiem, KronoMeridiem.AM)
        }

        components.assign(KronoComponents.Day, date.dayOfMonth)
        components.assign(KronoComponents.Month, date.monthValue)
        components.assign(KronoComponents.Year, date.year)
        return ParserResultFactory(components)
    }

    companion object {
        @JvmStatic
        private val PATTERN =
            Regex(
                "今日|きょう|当日|とうじつ|昨日|きのう|明日|あした|今夜|こんや|今夕|こんゆう|今晩|こんばん|今朝|けさ",
                RegexOption.IGNORE_CASE,
            )

        @JvmStatic
        fun normalizeTextToKanji(text: String) =
            when (text) {
                "きょう" -> "今日"
                "とうじつ" -> "当日"
                "きのう" -> "昨日"
                "あした" -> "明日"
                "こんや" -> "今夜"
                "こんゆう" -> "今夕"
                "こんばん" -> "今晩"
                "けさ" -> "今朝"
                else -> text
            }
    }
}
