package org.kgusarov.krono.locales.en

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.Krono
import org.kgusarov.krono.ParsingOption
import org.kgusarov.krono.testSingleCase
import org.kgusarov.krono.testUnexpectedResult

internal class EnNegativeCasesTest {
    @Test
    fun `skip random non-date patterns`() {
        testUnexpectedResult(Krono.enStrict, " 3")
        testUnexpectedResult(Krono.enStrict, "       1");
        testUnexpectedResult(Krono.enStrict, "  11 ");
        testUnexpectedResult(Krono.enStrict, " 0.5 ");
        testUnexpectedResult(Krono.enStrict, " 35.49 ");
        testUnexpectedResult(Krono.enStrict, "12.53%");
        testUnexpectedResult(Krono.enStrict, "6358fe2310> *5.0* / 5 Outstanding");
        testUnexpectedResult(Krono.enStrict, "6358fe2310> *1.5* / 5 Outstanding");
        testUnexpectedResult(Krono.enStrict, "Total: $1,194.09 [image: View Reservation");
        testUnexpectedResult(Krono.enStrict, "at 6.5 kilograms");
        testUnexpectedResult(Krono.enStrict, "ah that is unusual", ParsingOption(forwardDate = true));
    }

    @Test
    fun `url encoding`() {
        testUnexpectedResult(Krono.enStrict, "%e7%b7%8a");
        testUnexpectedResult(
            Krono.enStrict,
            "https://tenor.com/view/%e3%83%89%e3%82%ad%e3%83%89%e3%82%ad-" +
                "%e7%b7%8a%e5%bc%b5-%e5%a5%bd%e3%81%8d-%e3%83%8f%e3%83%bc%e3%83%88" +
                "-%e5%8f%af%e6%84%9b%e3%81%84-gif-15876325"
        );
    }

    @Test
    fun `skip hyphenated numbers pattern`() {
        testUnexpectedResult(Krono.enStrict, "1-2");
        testUnexpectedResult(Krono.enStrict, "1-2-3");
        testUnexpectedResult(Krono.enStrict, "4-5-6");
        testUnexpectedResult(Krono.enStrict, "20-30-12");
        testUnexpectedResult(Krono.enStrict, "2012");
        testUnexpectedResult(Krono.enStrict, "2012-14");
        testUnexpectedResult(Krono.enStrict, "2012-1400");
        testUnexpectedResult(Krono.enStrict, "2200-25");
    }

    @Test
    fun `skip impossible dates and times`() {
        testUnexpectedResult(Krono.enStrict, "February 29, 2022")
        testUnexpectedResult(Krono.enStrict, "02/29/2022")
        testUnexpectedResult(Krono.enStrict, "June 31, 2022")
        testUnexpectedResult(Krono.enStrict, "06/31/2022")
        testUnexpectedResult(Krono.enStrict, "14PM")
        testUnexpectedResult(Krono.enStrict, "25:12")
        testUnexpectedResult(Krono.enStrict, "An appointment on 13/31/2018");
    }

    @Test
    fun `skip version number pattern`() {
        testUnexpectedResult(Krono.enStrict, "Version: 1.1.3");
        testUnexpectedResult(Krono.enStrict, "Version: 1.1.30");
        testUnexpectedResult(Krono.enStrict, "Version: 1.10.30");
    }

    @Test
    fun `skip incorrect reference`() {
        testUnexpectedResult(Krono.enStrict, "for the year");
    }

    @Test
    fun `date with version number pattern`() {
        testSingleCase(Krono.enStrict, "1.5.3 - 2015-09-24") {
            assertThat(it.text).isEqualTo("2015-09-24")
        }

        testSingleCase(Krono.enStrict, "1.5.30 - 2015-09-24") {
            assertThat(it.text).isEqualTo("2015-09-24")
        }

        testSingleCase(Krono.enStrict, "1.50.30 - 2015-09-24") {
            assertThat(it.text).isEqualTo("2015-09-24")
        }
    }
}
