package org.kgusarov.krono.locales.ja

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.Krono
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase
import java.util.stream.Stream

internal class JaCasualTest {
    @ParameterizedTest
    @MethodSource("singleExpressionArgs")
    internal fun `single expression`(text: String, refDate: String, expectedText: String, expectedDate: String) {
        testSingleCase(Krono.jaCasual, text, refDate) {
            assertThat(it.text).isEqualTo(expectedText)
            it.start.assertDate(expectedDate)
        }
    }

    companion object {
        @JvmStatic
        fun singleExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of("今日感じたことを忘れずに", "2012-08-10T12:00:00", "今日", "2012-08-10T12:00:00"),
            Arguments.of("きょう感じたことを忘れずに", "2012-08-10T12:00:00", "きょう", "2012-08-10T12:00:00"),
            Arguments.of("昨日の全国観測値ランキング", "2012-08-10T12:00:00", "昨日", "2012-08-09T12:00:00"),
            Arguments.of("きのうの全国観測値ランキング", "2012-08-10T12:00:00", "きのう", "2012-08-09T12:00:00"),
            Arguments.of("明日の天気は晴れです", "2012-08-10T12:00:00", "明日", "2012-08-11T12:00:00"),
            Arguments.of("あしたの天気は晴れです", "2012-08-10T12:00:00", "あした", "2012-08-11T12:00:00"),
            Arguments.of("今夜には雨が降るでしょう", "2012-08-10T12:00:00", "今夜", "2012-08-10T22:00:00"),
            Arguments.of("こんやには雨が降るでしょう", "2012-08-10T12:00:00", "こんや", "2012-08-10T22:00:00"),
            Arguments.of("今夕には雨が降るでしょう", "2012-08-10T12:00:00", "今夕", "2012-08-10T22:00:00"),
            Arguments.of("こんゆうには雨が降るでしょう", "2012-08-10T12:00:00", "こんゆう", "2012-08-10T22:00:00"),
            Arguments.of("今晩には雨が降るでしょう", "2012-08-10T12:00:00", "今晩", "2012-08-10T22:00:00"),
            Arguments.of("こんばんには雨が降るでしょう", "2012-08-10T12:00:00", "こんばん", "2012-08-10T22:00:00"),
            Arguments.of("今朝食べたパンは美味しかった", "2012-08-10T12:00:00", "今朝", "2012-08-10T06:00:00"),
            Arguments.of("けさ食べたパンは美味しかった", "2012-08-10T12:00:00", "けさ", "2012-08-10T06:00:00"),
        )
    }
}
