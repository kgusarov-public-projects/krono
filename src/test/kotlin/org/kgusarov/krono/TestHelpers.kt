package org.kgusarov.krono

import com.google.common.base.Stopwatch
import org.assertj.core.api.Assertions.assertThat
import org.slf4j.LoggerFactory
import java.time.OffsetDateTime
import java.time.ZoneOffset
import kotlin.test.fail

typealias CheckResult = (p: ParsedResult) -> Unit

typealias CheckResults = (p: List<ParsedResult>) -> Unit

internal fun testSingleCase(
    krono: Krono,
    text: String,
    refDate: String,
    option: ParsingOption? = null,
    checkResult: CheckResult,
): ParsedResult = testSingleCase(
    krono,
    text,
    RefDateInputFactory(refDate),
    option,
    checkResult
)

internal fun testSingleCase(
    krono: Krono,
    text: String,
    refDate: KronoDate,
    option: ParsingOption? = null,
    checkResult: CheckResult,
): ParsedResult = testSingleCase(
    krono,
    text,
    RefDateInputFactory(refDate),
    option,
    checkResult
)

internal fun testSingleCase(
    krono: Krono,
    text: String,
    option: ParsingOption? = null,
    checkResult: CheckResult,
): ParsedResult = testSingleCase(
    krono,
    text,
    RefDateInputFactory(KronoDate.now()),
    option,
    checkResult
)

internal fun testSingleCase(
    krono: Krono,
    text: String,
    ref: ReferenceWithTimezone,
    option: ParsingOption? = null,
    checkResult: CheckResult,
): ParsedResult = testSingleCase(
    krono,
    text,
    if (ref.timezone != null) {
        RefDateInputFactory(ParsingReference(ref.instant, ref.timezone))
    } else {
        RefDateInputFactory(ref.instant)
    },
    option,
    checkResult
)

internal fun testSingleCase(
    krono: Krono,
    text: String,
    refDate: RefDateInput,
    option: ParsingOption? = null,
    checkResult: CheckResult,
): ParsedResult {
    val debugHandler = BufferedDebugHandler()
    val testOption = option?.copy(debug = debugHandler) ?: ParsingOption(debug = debugHandler)
    var result: ParsedResult? = null

    try {
        val results = krono.parse(text, refDate, testOption)
        results.assertSingleOnText(text)
        result = results[0]
        checkResult(result)

        return result
    } finally {
        TestLogger.LOGGER.info("Tested single case with text: '$text' -> $result")
        debugHandler.execute().forEachIndexed { index, it ->
            TestLogger.LOGGER.info("\t${index + 1}. $it")
        }
    }
}

internal fun testUnexpectedResult(
    krono: Krono,
    text: String,
    option: ParsingOption? = null,
) {
    testUnexpectedResult(
        krono,
        text,
        RefDateInputFactory(KronoDate.now()),
        option
    )
}

internal fun testUnexpectedResult(
    krono: Krono,
    text: String,
    refDate: String,
    option: ParsingOption? = null,
) {
    testUnexpectedResult(
        krono,
        text,
        RefDateInputFactory(refDate),
        option
    )
}

internal fun testUnexpectedResult(
    krono: Krono,
    text: String,
    refDate: RefDateInput,
    option: ParsingOption? = null,
) {
    val debugHandler = BufferedDebugHandler()
    val testOption = option?.copy(debug = debugHandler) ?: ParsingOption(debug = debugHandler)

    try {
        val results = krono.parse(text, refDate, testOption)
        assertThat(results).isEmpty()
    } finally {
        TestLogger.LOGGER.info("Tested unexpected result with text: '$text'")
        debugHandler.execute().forEachIndexed { index, it ->
            TestLogger.LOGGER.info("\t${index + 1}. $it")
        }
    }
}

internal fun testMultipleResults(
    krono: Krono,
    text: String,
    refDate: RefDateInput,
    option: ParsingOption? = null,
    checkResults: CheckResults,
) {
    val debugHandler = BufferedDebugHandler()
    val testOption = option?.copy(debug = debugHandler) ?: ParsingOption(debug = debugHandler)
    var results: List<ParsedResult>? = null

    try {
        results = krono.parse(text, refDate, testOption)
        checkResults(results)
    } finally {
        TestLogger.LOGGER.info("Tested case with text: '$text' -> $results")
        debugHandler.execute().forEachIndexed { index, it ->
            TestLogger.LOGGER.info("\t${index + 1}. $it")
        }
    }
}

internal fun testMultipleResults(
    krono: Krono,
    text: String,
    refDate: String,
    option: ParsingOption? = null,
    checkResults: CheckResults,
) {
    testMultipleResults(
        krono,
        text,
        RefDateInputFactory(refDate),
        option,
        checkResults,
    )
}

fun List<ParsedResult>.assertSingleOnText(text: String) {
    if (size != 1) {
        fail("Got $size results from '$text': ${System.lineSeparator()}${joinToString(System.lineSeparator())}")
    }
}

fun ParsedResult.assertDate(expected: String) {
    val actual = instant()
    val expectedDate = KronoDate.parse(expected)
    assertThat(actual).isEqualTo(expectedDate)
}

fun ParsedComponents.assertDate(expected: String) {
    val actual = instant()
    val expectedDate = KronoDate.parse(expected)
    assertThat(actual).isEqualTo(expectedDate)
}

fun ParsedComponents.assertOffsetDate(expected: String) {
    val actual = instant()
    val actualOffset = offset() ?: 0
    val actualAtOffset = actual.atOffset(ZoneOffset.ofTotalSeconds(actualOffset))
    val expectedDate = OffsetDateTime.parse(expected)
    assertThat(actualAtOffset).isEqualTo(expectedDate)
}

class TestParsedResult(
    override val refDate: KronoDate = KronoDate.now(),
    override var index: Int = 0,
    override var text: String = "",
    override var start: ParsedComponents = ParsingComponents(ReferenceWithTimezone()),
    override var end: ParsedComponents? = null,
) : ParsedResult {
    override val reference = ReferenceWithTimezone(refDate)

    override fun instant(): KronoDate = start.instant()

    override fun tags(): Set<String> = emptySet()

    @Suppress("UNCHECKED_CAST")
    override fun <T> copy(): T where T : ParsedResult = TestParsedResult(
        refDate,
        index,
        text,
        start.copy(),
        end?.copy(),
    ) as T

    override fun toString() = "TestParsedResult(index=$index, text='$text', refDate=$refDate)"
}

internal object TestLogger {
    internal val LOGGER = LoggerFactory.getLogger("TestLogger")
}

internal fun testWithExpectedDate(
    krono: Krono,
    text: String,
    expectedDate: String,
    option: ParsingOption? = null,
): ParsedResult = testSingleCase(krono, text, option) {
    with(it.start) {
        assertDate(expectedDate)
    }
}

internal fun testWithExpectedDate(
    krono: Krono,
    text: String,
    refDate: String,
    expectedDate: String,
    option: ParsingOption? = null,
): ParsedResult = testSingleCase(krono, text, refDate, option) {
    with(it.start) {
        assertDate(expectedDate)
    }
}

internal fun testWithExpectedRange(
    krono: Krono,
    text: String,
    refDate: String,
    expectedStart: String,
    expectedEnd: String,
    option: ParsingOption? = null,
): ParsedResult = testSingleCase(krono, text, refDate, option) {
    with(it.start) {
        assertDate(expectedStart)
    }

    with(it.end!!) {
        assertDate(expectedEnd)
    }
}

internal fun testWithExpectedOffsetDate(
    krono: Krono,
    text: String,
    expectedDate: String,
): ParsedResult = testSingleCase(krono, text) {
    with(it.start) {
        assertOffsetDate(expectedDate)
    }
}

internal fun measureTime(block: () -> Unit): Stopwatch {
    val stopwatch = Stopwatch.createStarted()
    block()
    stopwatch.stop()

    TestLogger.LOGGER.info("Elapsed time: {}", stopwatch)

    return stopwatch
}
