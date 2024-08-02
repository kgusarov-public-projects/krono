package org.kgusarov.krono

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.kgusarov.krono.locales.en.En
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.test.fail

typealias CheckResult = (p: ParsedResult) -> Unit

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
    refDate: RefDateInput,
    option: ParsingOption? = null,
    checkResult: CheckResult,
) {
    val debugHandler = BufferedDebugHandler()
    val testOption = option?.copy(debug = debugHandler) ?: ParsingOption(debug = debugHandler)

    try {
        val results = krono.parse(text, refDate, testOption)
        results.assertSingleOnText(text)
        checkResult(results[0])
    } finally {
        TestLogger.LOGGER.info("Tested single case with text: '$text'")
        debugHandler.execute().forEach {
            TestLogger.LOGGER.info("\t$it")
        }
    }
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

class TestParsedResult(
    override val refDate: KronoDate = KronoDate.now(),
    override var index: Int = 0,
    override var text: String = "",
    override var start: ParsedComponents = ParsingComponents(ReferenceWithTimezone()),
    override var end: ParsedComponents? = null,
) : ParsedResult {
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


/*

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