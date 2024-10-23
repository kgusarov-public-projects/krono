package org.kgusarov.krono.locales.ja

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.kgusarov.krono.locales.ja.parsers.JaCasualDateParser

internal class JaCasualDateParserTest {
    @ParameterizedTest
    @CsvSource(
        "きょう, 今日",
        "とうじつ, 当日",
        "きのう, 昨日",
        "あした, 明日",
        "こんや, 今夜",
        "こんゆう, 今夕",
        "こんばん, 今晩",
        "けさ, 今朝",
        "unknown, unknown"
    )
    internal fun `normalize text to Kanji`(input: String, expected: String) {
        val result = JaCasualDateParser.normalizeTextToKanji(input)
        assertThat(result).isEqualTo(expected)
    }
}
