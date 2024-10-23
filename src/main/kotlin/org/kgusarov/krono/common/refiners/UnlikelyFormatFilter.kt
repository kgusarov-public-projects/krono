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
        if (result.text.replaceFirst(" ", "").matches(NUMBER_PATTERN)) {
            context {
                "${javaClass.simpleName} removed unlikely result $result"
            }

            return false
        }

        if (!result.start.isValidDate()) {
            context {
                "${javaClass.simpleName} removed invalid result $result"
            }

            return false
        }

        if (result.end != null && !result.end!!.isValidDate()) {
            context {
                "${javaClass.simpleName} removed invalid result $result"
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
                "${javaClass.simpleName} removed weekday only component $result"
            }

            return false
        }

        if (
            result.start.onlyTime() &&
            (!result.start.certainHour() || !result.start.certainMinute())
        ) {
            context {
                "${javaClass.simpleName} removed uncertain time component $result"
            }

            return false
        }

        return true
    }

    companion object {
        private val NUMBER_PATTERN = Regex("^\\d*(\\.\\d*)?$")
    }
}
