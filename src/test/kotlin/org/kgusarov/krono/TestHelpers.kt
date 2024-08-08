package org.kgusarov.krono

import org.assertj.core.api.Assertions.assertThat
import org.slf4j.LoggerFactory
import java.time.OffsetDateTime
import java.time.ZoneId
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
) {
    testSingleCase(
        krono,
        text,
        RefDateInputFactory(refDate),
        option,
        checkResult
    )
}

internal fun testSingleCase(
    krono: Krono,
    text: String,
    refDate: KronoDate,
    option: ParsingOption? = null,
    checkResult: CheckResult,
) {
    testSingleCase(
        krono,
        text,
        RefDateInputFactory(refDate),
        option,
        checkResult
    )
}

internal fun testSingleCase(
    krono: Krono,
    text: String,
    option: ParsingOption? = null,
    checkResult: CheckResult,
) {
    testSingleCase(
        krono,
        text,
        RefDateInputFactory(KronoDate.now()),
        option,
        checkResult
    )
}

internal fun testSingleCase(
    krono: Krono,
    text: String,
    ref: ReferenceWithTimezone,
    option: ParsingOption? = null,
    checkResult: CheckResult,
) {
    testSingleCase(
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
}

internal fun testSingleCase(
    krono: Krono,
    text: String,
    refDate: RefDateInput,
    option: ParsingOption? = null,
    checkResult: CheckResult,
) {
    val debugHandler = BufferedDebugHandler()
    val testOption = option?.copy(debug = debugHandler) ?: ParsingOption(debug = debugHandler)
    var result: ParsedResult? = null

    try {
        val results = krono.parse(text, refDate, testOption)
        results.assertSingleOnText(text)
        result = results[0]
        checkResult(result)
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
) {
    testSingleCase(krono, text) {
        with(it.start) {
            assertDate(expectedDate)
        }
    }
}

internal fun testWithExpectedOffsetDate(
    krono: Krono,
    text: String,
    expectedDate: String,
) {
    testSingleCase(krono, text) {
        with(it.start) {
            assertOffsetDate(expectedDate)
        }
    }
}


/*

export function testWithExpectedDate(chrono: ChronoLike, text: string, expectedDate: Date) {
    testSingleCase(chrono, text, (result) => {
        expect(result.start).toBeDate(expectedDate);
    });
}

toBeDate(resultOrComponent, date) {
        if (typeof resultOrComponent.date !== "function") {
            return {
                message: () => `${resultOrComponent} is not a ParsedResult or ParsedComponent`,
                pass: false,
            };
        }

        const actualDate = resultOrComponent.date();
        const actualTime = actualDate.getTime();
        const expectedTime = date.getTime();
        return {
            message: () => `Expected date to be: ${date} Received: ${actualDate} (${resultOrComponent})`,
            pass: actualTime === expectedTime,
        };
    },

try {
        const results = chrono.parse(text, refDateOrCheckResult as Date, optionOrCheckResult);
        expect(results).toBeSingleOnText(text);
        if (checkResult) {
            checkResult(results[0], text);
        }
    } catch (e) {
        debugHandler.executeBufferedBlocks();
        e.stack = e.stack.replace(/[^\n]*at .*test_util.*\n/g, "");
        throw e;
    }

export function testSingleCase(chrono: ChronoLike, text: string, checkResult?: CheckResult);
export function testSingleCase(
    chrono: ChronoLike,
    text: string,
    refDateOrCheckResult?: ParsingReference | Date | CheckResult,
    checkResult?: CheckResult
);
export function testSingleCase(
    chrono: ChronoLike,
    text: string,
    refDateOrCheckResult?: ParsingReference | Date | CheckResult,
    optionOrCheckResult?: ParsingOption | CheckResult,
    checkResult?: CheckResult
);
export function testSingleCase(
    chrono: ChronoLike,
    text: string,
    refDateOrCheckResult?: ParsingReference | Date | CheckResult,
    optionOrCheckResult?: ParsingOption | CheckResult,
    checkResult?: CheckResult
) {
    if (checkResult === undefined && typeof optionOrCheckResult === "function") {
        checkResult = optionOrCheckResult;
        optionOrCheckResult = undefined;
    }

    if (optionOrCheckResult === undefined && typeof refDateOrCheckResult === "function") {
        checkResult = refDateOrCheckResult;
        refDateOrCheckResult = undefined;
    }

    const debugHandler = new BufferedDebugHandler();
    optionOrCheckResult = (optionOrCheckResult as ParsingOption) || {};
    optionOrCheckResult.debug = debugHandler;


}
 */