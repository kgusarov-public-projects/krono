package org.kgusarov.krono

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
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
    refDate: RefDateInput,
    option: ParsingOption? = null,
    checkResult: CheckResult,
) {
    val results = krono.parse(text, refDate, option)
    results.assertSingleOnText(text)
    checkResult(results[0])
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