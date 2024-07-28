package org.kgusarov.krono.locales.en

import org.kgusarov.krono.KronoConfiguration
import org.kgusarov.krono.includeCommonConfiguration
import org.kgusarov.krono.locales.en.parsers.ENCasualTimeParser
import org.kgusarov.krono.locales.en.parsers.ENWeekdayParser
import org.kgusarov.krono.locales.en.refiners.ENMergeDateRangeRefiner
import org.kgusarov.krono.locales.en.refiners.ENMergeDateTimeRefiner

class ENDefaultConfiguration {
    fun createConfiguration(
        strictMode: Boolean = true,
        littleEndian: Boolean = false,
    ): KronoConfiguration {
        val result =
            includeCommonConfiguration(
                KronoConfiguration(
                    mutableListOf(
                        ENWeekdayParser(),
                    ),
                    mutableListOf(
                        ENMergeDateTimeRefiner(),
                    ),
                ),
                strictMode,
            )

        // Keep the date range refiner at the end (after all other refinements).
        result.refiners += ENMergeDateRangeRefiner()

        return result
    }

    fun createCasualConfiguration(littleEndian: Boolean = false): KronoConfiguration {
        val result = createConfiguration(false, littleEndian)
//        option.parsers.push(new ENCasualDateParser());
        result.parsers += ENCasualTimeParser()
//        option.parsers.push(new ENMonthNameParser());
//        option.parsers.push(new ENRelativeDateFormatParser());
//        option.parsers.push(new ENTimeUnitCasualRelativeFormatParser());
        return result
    }

    /*
    /**
     * Create a default {@Link Configuration} for English chrono
     *
     * @param strictMode If the timeunit mentioning should be strict, not casual
     * @param littleEndian If format should be date-first/littleEndian (e.g. en_UK), not month-first/middleEndian (e.g. en_US)
     */
    createConfiguration(strictMode = true, littleEndian = false): Configuration {
        const options = includeCommonConfiguration(
            {
                parsers: [
                    new SlashDateFormatParser(littleEndian),
                    new ENTimeUnitWithinFormatParser(strictMode),
                    new ENMonthNameLittleEndianParser(),
                    new ENMonthNameMiddleEndianParser(/*shouldSkipYearLikeDate=*/ littleEndian),

                    new ENCasualYearMonthDayParser(),
                    new ENSlashMonthFormatParser(),
                    new ENTimeExpressionParser(strictMode),
                    new ENTimeUnitAgoFormatParser(strictMode),
                    new ENTimeUnitLaterFormatParser(strictMode),
                ],
                refiners: [],
            },
            strictMode
        );
        // These relative-dates consideration should be done before other common refiners.
        options.refiners.unshift(new ENMergeRelativeFollowByDateRefiner());
        options.refiners.unshift(new ENMergeRelativeAfterDateRefiner());
        options.refiners.unshift(new OverlapRemovalRefiner());

        // Re-apply the date time refiner again after the timezone refinement and exclusion in common refiners.
        options.refiners.push(new ENMergeDateTimeRefiner());

        return options;
    }
     */
}
