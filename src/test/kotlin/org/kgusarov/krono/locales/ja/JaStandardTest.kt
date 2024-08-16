package org.kgusarov.krono.locales.ja

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kgusarov.krono.Krono
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase
import java.util.stream.Stream

internal class JaStandardTest {
    @ParameterizedTest
    @MethodSource("singleExpressionArgs")
    fun `single expression`(text: String, refDate: String, index: Int, expectedText: String, expectedDate: String) {
        testSingleCase(Krono.jaStrict, text, refDate) {
            assertThat(it.text).isEqualTo(expectedText)
            assertThat(it.index).isEqualTo(index)
            it.start.assertDate(expectedDate)
        }
    }

    @ParameterizedTest
    @MethodSource("rangeExpressionArgs")
    fun `range expression`(
        text: String,
        refDate: String,
        index: Int,
        expectedText: String,
        expectedStartDate: String,
        expectedEndDate: String
    ) {
        testSingleCase(Krono.jaStrict, text, refDate) {
            assertThat(it.text).isEqualTo(expectedText)
            assertThat(it.index).isEqualTo(index)
            it.start.assertDate(expectedStartDate)
            it.end!!.assertDate(expectedEndDate)
        }
    }

    companion object {
        @JvmStatic
        fun singleExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "主な株主（2012年3月31日現在）", "2012-08-10T12:00:00", 5, "2012年3月31日", "2012-03-31T12:00:00"
            ),
            Arguments.of(
                "主な株主（2012年９月3日現在）", "2012-08-10T12:00:00", 5, "2012年９月3日", "2012-09-03T12:00:00"
            ),
            Arguments.of(
                "主な株主（2020年2月29日現在）", "2019-08-10T12:00:00", 5, "2020年2月29日", "2020-02-29T12:00:00"
            ),
            Arguments.of(
                "主な株主（９月3日現在）", "2012-08-10T12:00:00", 5, "９月3日", "2012-09-03T12:00:00"
            ),
            Arguments.of(
                "主な株主（平成26年12月29日）", "2012-08-10T12:00:00", 5, "平成26年12月29日", "2014-12-29T12:00:00"
            ),
            Arguments.of(
                "主な株主（昭和６４年１月７日）", "2012-08-10T12:00:00", 5, "昭和６４年１月７日", "1989-01-07T12:00:00"
            ),
            Arguments.of(
                "主な株主（令和元年5月1日）", "2012-08-10T12:00:00", 5, "令和元年5月1日", "2019-05-01T12:00:00"
            ),
            Arguments.of(
                "主な株主（令和2年5月1日）", "2012-08-10T12:00:00", 5, "令和2年5月1日", "2020-05-01T12:00:00"
            ),
            Arguments.of(
                "主な株主（同年7月27日）", "2012-08-10T12:00:00", 5, "同年7月27日", "2012-07-27T12:00:00"
            ),
            Arguments.of(
                "主な株主（本年7月27日）", "2012-08-10T12:00:00", 5, "本年7月27日", "2012-07-27T12:00:00"
            ),
            Arguments.of(
                "主な株主（今年7月27日）", "2012-08-10T12:00:00", 5, "今年7月27日", "2012-07-27T12:00:00"
            ),
            Arguments.of(
                "主な株主（今年11月27日）", "2012-01-10T12:00:00", 5, "今年11月27日", "2012-11-27T12:00:00"
            ),
            Arguments.of(
                "7月27日", "2012-08-10T12:00:00", 0, "7月27日", "2012-07-27T12:00:00"
            ),
            Arguments.of(
                "11月27日", "2012-01-10T12:00:00", 0, "11月27日", "2011-11-27T12:00:00"
            ),
        )

        @JvmStatic
        fun rangeExpressionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "2013年12月26日-2014年1月7日",
                "2012-08-10T12:00:00",
                0,
                "2013年12月26日-2014年1月7日",
                "2013-12-26T12:00:00",
                "2014-01-07T12:00:00",
            ),
            Arguments.of(
                "２０１３年１２月２６日ー2014年1月7日",
                "2012-08-10T12:00:00",
                0,
                "２０１３年１２月２６日ー2014年1月7日",
                "2013-12-26T12:00:00",
                "2014-01-07T12:00:00",
            ),
        )
    }
}
