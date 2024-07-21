package org.kgusarov.krono

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

/**
 * Contract for a timezone that can be precise or ambiguous
 */
sealed interface KronoTimezone

/**
 * Timezone with precise offset
 */
data class KronoSimpleTimezone(val offset: Int) : KronoTimezone

/**
 * Timezone with offset depending on the time of year — daylight savings time (DST), or non-DST
 */
data class KronoDstTimezone(
    val dstOffset: Int,
    val nonDstOffset: Int,
    val dstStart: (year: Int) -> LocalDateTime,
    val dstEnd: (year: Int) -> LocalDateTime,
) : KronoTimezone

/**
 * Get the date which is the nth occurence of a given weekday in a given month and year.
 *
 * @param year The year for which to find the date
 * @param month The month in which the date occurs
 * @param weekday The weekday on which the date occurs
 * @param n The nth occurence of the given weekday on the month to return
 * @param hour The hour of day which should be set on the returned date
 *
 * @return The date which is the nth occurence of a given weekday in a given month and year, at the given hour of day
 */
internal fun getNthWeekdayOfMonth(
    year: Int,
    month: Month,
    weekday: DayOfWeek,
    n: Int,
    hour: Int,
): LocalDateTime {
    var dayOfMonth = 0
    var i = 0
    while (i < n) {
        dayOfMonth++
        val date = LocalDate.of(year, month.value, dayOfMonth)
        if (date.dayOfWeek == weekday) {
            i++
        }
    }

    return LocalDateTime.of(year, month.value, dayOfMonth, hour, 0)
}

/**
 * Get the date which is the last occurence of a given weekday in a given month and year.
 *
 * @param year The year for which to find the date
 * @param month The month in which the date occurs
 * @param weekday The weekday on which the date occurs
 * @param hour The hour of day which should be set on the returned date
 * @return The date which is the last occurence of a given weekday in a given month and year, at the given hour of day
 */
internal fun getLastWeekdayOfMonth(
    year: Int,
    month: Month,
    weekday: DayOfWeek,
    hour: Int,
): LocalDateTime {
    val date = LocalDateTime.of(year, month + 1, 1, 12, 0)
    val firstWeekdayNextMonth = date.dayOfWeek
    val dayDiff: Long =
        when {
            firstWeekdayNextMonth == weekday -> 7L
            firstWeekdayNextMonth.value < weekday.value -> 7L + firstWeekdayNextMonth.value - weekday.value
            else -> (firstWeekdayNextMonth.value - weekday.value).toLong()
        }

    return date.minusDays(dayDiff).withHour(hour)
}

typealias TimezoneMap = Map<String, KronoTimezone>

@SuppressFBWarnings("EI_EXPOSE_REP")
object Timezones {
    val MAP: TimezoneMap =
        mapOf(
            "ACDT" to KronoSimpleTimezone(630),
            "ACST" to KronoSimpleTimezone(570),
            "ADT" to KronoSimpleTimezone(-180),
            "AEDT" to KronoSimpleTimezone(660),
            "AEST" to KronoSimpleTimezone(600),
            "AFT" to KronoSimpleTimezone(270),
            "AKDT" to KronoSimpleTimezone(-480),
            "AKST" to KronoSimpleTimezone(-540),
            "ALMT" to KronoSimpleTimezone(360),
            "AMST" to KronoSimpleTimezone(-180),
            "AMT" to KronoSimpleTimezone(-240),
            "ANAST" to KronoSimpleTimezone(720),
            "ANAT" to KronoSimpleTimezone(720),
            "AQTT" to KronoSimpleTimezone(300),
            "ART" to KronoSimpleTimezone(-180),
            "AST" to KronoSimpleTimezone(-240),
            "AWDT" to KronoSimpleTimezone(540),
            "AWST" to KronoSimpleTimezone(480),
            "AZOST" to KronoSimpleTimezone(0),
            "AZOT" to KronoSimpleTimezone(-60),
            "AZST" to KronoSimpleTimezone(300),
            "AZT" to KronoSimpleTimezone(240),
            "BNT" to KronoSimpleTimezone(480),
            "BOT" to KronoSimpleTimezone(-240),
            "BRST" to KronoSimpleTimezone(-120),
            "BRT" to KronoSimpleTimezone(-180),
            "BST" to KronoSimpleTimezone(60),
            "BTT" to KronoSimpleTimezone(360),
            "CAST" to KronoSimpleTimezone(480),
            "CAT" to KronoSimpleTimezone(120),
            "CCT" to KronoSimpleTimezone(390),
            "CDT" to KronoSimpleTimezone(-300),
            "CEST" to KronoSimpleTimezone(120),
            "CHADT" to KronoSimpleTimezone(825),
            "CHAST" to KronoSimpleTimezone(765),
            "CKT" to KronoSimpleTimezone(-600),
            "CLST" to KronoSimpleTimezone(-180),
            "CLT" to KronoSimpleTimezone(-240),
            "COT" to KronoSimpleTimezone(-300),
            "CST" to KronoSimpleTimezone(-360),
            "CVT" to KronoSimpleTimezone(-60),
            "CXT" to KronoSimpleTimezone(420),
            "ChST" to KronoSimpleTimezone(600),
            "DAVT" to KronoSimpleTimezone(420),
            "EASST" to KronoSimpleTimezone(-300),
            "EAST" to KronoSimpleTimezone(-360),
            "EAT" to KronoSimpleTimezone(180),
            "ECT" to KronoSimpleTimezone(-300),
            "EDT" to KronoSimpleTimezone(-240),
            "EEST" to KronoSimpleTimezone(180),
            "EET" to KronoSimpleTimezone(120),
            "EGST" to KronoSimpleTimezone(0),
            "EGT" to KronoSimpleTimezone(-60),
            "EST" to KronoSimpleTimezone(-300),
            "FJST" to KronoSimpleTimezone(780),
            "FJT" to KronoSimpleTimezone(720),
            "FKST" to KronoSimpleTimezone(-180),
            "FKT" to KronoSimpleTimezone(-240),
            "FNT" to KronoSimpleTimezone(-120),
            "GALT" to KronoSimpleTimezone(-360),
            "GAMT" to KronoSimpleTimezone(-540),
            "GET" to KronoSimpleTimezone(240),
            "GFT" to KronoSimpleTimezone(-180),
            "GILT" to KronoSimpleTimezone(720),
            "GMT" to KronoSimpleTimezone(0),
            "GST" to KronoSimpleTimezone(240),
            "GYT" to KronoSimpleTimezone(-240),
            "HAA" to KronoSimpleTimezone(-180),
            "HAC" to KronoSimpleTimezone(-300),
            "HADT" to KronoSimpleTimezone(-540),
            "HAE" to KronoSimpleTimezone(-240),
            "HAP" to KronoSimpleTimezone(-420),
            "HAR" to KronoSimpleTimezone(-360),
            "HAST" to KronoSimpleTimezone(-600),
            "HAT" to KronoSimpleTimezone(-90),
            "HAY" to KronoSimpleTimezone(-480),
            "HKT" to KronoSimpleTimezone(480),
            "HLV" to KronoSimpleTimezone(-210),
            "HNA" to KronoSimpleTimezone(-240),
            "HNC" to KronoSimpleTimezone(-360),
            "HNE" to KronoSimpleTimezone(-300),
            "HNP" to KronoSimpleTimezone(-480),
            "HNR" to KronoSimpleTimezone(-420),
            "HNT" to KronoSimpleTimezone(-150),
            "HNY" to KronoSimpleTimezone(-540),
            "HOVT" to KronoSimpleTimezone(420),
            "ICT" to KronoSimpleTimezone(420),
            "IDT" to KronoSimpleTimezone(180),
            "IOT" to KronoSimpleTimezone(360),
            "IRDT" to KronoSimpleTimezone(270),
            "IRKST" to KronoSimpleTimezone(540),
            "IRKT" to KronoSimpleTimezone(540),
            "IRST" to KronoSimpleTimezone(210),
            "IST" to KronoSimpleTimezone(330),
            "JST" to KronoSimpleTimezone(540),
            "KGT" to KronoSimpleTimezone(360),
            "KRAST" to KronoSimpleTimezone(480),
            "KRAT" to KronoSimpleTimezone(480),
            "KST" to KronoSimpleTimezone(540),
            "KUYT" to KronoSimpleTimezone(240),
            "LHDT" to KronoSimpleTimezone(660),
            "LHST" to KronoSimpleTimezone(630),
            "LINT" to KronoSimpleTimezone(840),
            "MAGST" to KronoSimpleTimezone(720),
            "MAGT" to KronoSimpleTimezone(720),
            "MART" to KronoSimpleTimezone(-510),
            "MAWT" to KronoSimpleTimezone(300),
            "MDT" to KronoSimpleTimezone(-360),
            "MESZ" to KronoSimpleTimezone(120),
            "MEZ" to KronoSimpleTimezone(60),
            "MHT" to KronoSimpleTimezone(720),
            "MMT" to KronoSimpleTimezone(390),
            "MSD" to KronoSimpleTimezone(240),
            "MSK" to KronoSimpleTimezone(180),
            "MST" to KronoSimpleTimezone(-420),
            "MUT" to KronoSimpleTimezone(240),
            "MVT" to KronoSimpleTimezone(300),
            "MYT" to KronoSimpleTimezone(480),
            "NCT" to KronoSimpleTimezone(660),
            "NDT" to KronoSimpleTimezone(-90),
            "NFT" to KronoSimpleTimezone(690),
            "NOVST" to KronoSimpleTimezone(420),
            "NOVT" to KronoSimpleTimezone(360),
            "NPT" to KronoSimpleTimezone(345),
            "NST" to KronoSimpleTimezone(-150),
            "NUT" to KronoSimpleTimezone(-660),
            "NZDT" to KronoSimpleTimezone(780),
            "NZST" to KronoSimpleTimezone(720),
            "OMSST" to KronoSimpleTimezone(420),
            "OMST" to KronoSimpleTimezone(420),
            "PDT" to KronoSimpleTimezone(-420),
            "PET" to KronoSimpleTimezone(-300),
            "PETST" to KronoSimpleTimezone(720),
            "PETT" to KronoSimpleTimezone(720),
            "PGT" to KronoSimpleTimezone(600),
            "PHOT" to KronoSimpleTimezone(780),
            "PHT" to KronoSimpleTimezone(480),
            "PKT" to KronoSimpleTimezone(300),
            "PMDT" to KronoSimpleTimezone(-120),
            "PMST" to KronoSimpleTimezone(-180),
            "PONT" to KronoSimpleTimezone(660),
            "PST" to KronoSimpleTimezone(-480),
            "PWT" to KronoSimpleTimezone(540),
            "PYST" to KronoSimpleTimezone(-180),
            "PYT" to KronoSimpleTimezone(-240),
            "RET" to KronoSimpleTimezone(240),
            "SAMT" to KronoSimpleTimezone(240),
            "SAST" to KronoSimpleTimezone(120),
            "SBT" to KronoSimpleTimezone(660),
            "SCT" to KronoSimpleTimezone(240),
            "SGT" to KronoSimpleTimezone(480),
            "SRT" to KronoSimpleTimezone(-180),
            "SST" to KronoSimpleTimezone(-660),
            "TAHT" to KronoSimpleTimezone(-600),
            "TFT" to KronoSimpleTimezone(300),
            "TJT" to KronoSimpleTimezone(300),
            "TKT" to KronoSimpleTimezone(780),
            "TLT" to KronoSimpleTimezone(540),
            "TMT" to KronoSimpleTimezone(300),
            "TVT" to KronoSimpleTimezone(720),
            "ULAT" to KronoSimpleTimezone(480),
            "UTC" to KronoSimpleTimezone(0),
            "UYST" to KronoSimpleTimezone(-120),
            "UYT" to KronoSimpleTimezone(-180),
            "UZT" to KronoSimpleTimezone(300),
            "VET" to KronoSimpleTimezone(-210),
            "VLAST" to KronoSimpleTimezone(660),
            "VLAT" to KronoSimpleTimezone(660),
            "VUT" to KronoSimpleTimezone(660),
            "WAST" to KronoSimpleTimezone(120),
            "WAT" to KronoSimpleTimezone(60),
            "WEST" to KronoSimpleTimezone(60),
            "WESZ" to KronoSimpleTimezone(60),
            "WET" to KronoSimpleTimezone(0),
            "WEZ" to KronoSimpleTimezone(0),
            "WFT" to KronoSimpleTimezone(720),
            "WGST" to KronoSimpleTimezone(-120),
            "WGT" to KronoSimpleTimezone(-180),
            "WIB" to KronoSimpleTimezone(420),
            "WIT" to KronoSimpleTimezone(540),
            "WITA" to KronoSimpleTimezone(480),
            "WST" to KronoSimpleTimezone(780),
            "WT" to KronoSimpleTimezone(0),
            "YAKST" to KronoSimpleTimezone(600),
            "YAKT" to KronoSimpleTimezone(600),
            "YAPT" to KronoSimpleTimezone(600),
            "YEKST" to KronoSimpleTimezone(360),
            "YEKT" to KronoSimpleTimezone(360),
            "CET" to
                KronoDstTimezone(
                    120,
                    60,
                    { getLastWeekdayOfMonth(it, Month.MARCH, DayOfWeek.SUNDAY, 2) },
                    { getLastWeekdayOfMonth(it, Month.OCTOBER, DayOfWeek.SUNDAY, 3) },
                ),
            "CT" to
                KronoDstTimezone(
                    -300,
                    -360,
                    { getNthWeekdayOfMonth(it, Month.MARCH, DayOfWeek.SUNDAY, 2, 2) },
                    { getNthWeekdayOfMonth(it, Month.NOVEMBER, DayOfWeek.SUNDAY, 1, 2) },
                ),
            "ET" to
                KronoDstTimezone(
                    -240,
                    -300,
                    { getNthWeekdayOfMonth(it, Month.MARCH, DayOfWeek.SUNDAY, 2, 2) },
                    { getNthWeekdayOfMonth(it, Month.NOVEMBER, DayOfWeek.SUNDAY, 1, 2) },
                ),
            "MT" to
                KronoDstTimezone(
                    -360,
                    -420,
                    { getNthWeekdayOfMonth(it, Month.MARCH, DayOfWeek.SUNDAY, 2, 2) },
                    { getNthWeekdayOfMonth(it, Month.NOVEMBER, DayOfWeek.SUNDAY, 1, 2) },
                ),
            "PT" to
                KronoDstTimezone(
                    -420,
                    -480,
                    { getNthWeekdayOfMonth(it, Month.MARCH, DayOfWeek.SUNDAY, 2, 2) },
                    { getNthWeekdayOfMonth(it, Month.NOVEMBER, DayOfWeek.SUNDAY, 1, 2) },
                ),
        )
}
