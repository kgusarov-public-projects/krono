package org.kgusarov.krono.common.refiners

import org.kgusarov.krono.ParsedResult
import org.kgusarov.krono.ParsingContext
import org.kgusarov.krono.common.Filter

class UnlikelyFormatFilter(
    private val strictMode: Boolean,
) : Filter() {
    override fun isValid(
        context: ParsingContext,
        result: ParsedResult,
    ): Boolean {
        if (result.text.replace(" ", "").matches(NUMBER_PATTERN)) {
            context {
                "Removing unlikely result '${result.text}'"
            }

            return false
        }

        if (!result.start.isValidDate()) {
            context {
                "Removing invalid result: $result (${result.start})"
            }

            return false
        }

        if (result.end != null && !result.end!!.isValidDate()) {
            context {
                "Removing invalid result: $result (${result.end})"
            }

            return false
        }

        if (strictMode) {
            return isStrictModeValid(context, result)
        }

        return true
    }

    private fun isStrictModeValid(
        context: ParsingContext,
        result: ParsedResult,
    ): Boolean {
        if (result.start.onlyWeekday()) {
            context {
                "(Strict) Removing weekday only component: $result (${result.end})"
            }

            return false
        }

        if (
            result.start.onlyTime() &&
            (!result.start.certainHour() || !result.start.certainMinute())
        ) {
            context {
                "(Strict) Removing uncertain time component: $result (${result.end})"
            }

            return false
        }

        return true
    }

    companion object {
        private val NUMBER_PATTERN = Regex("^\\d*(\\.\\d*)?\$")
    }
}
