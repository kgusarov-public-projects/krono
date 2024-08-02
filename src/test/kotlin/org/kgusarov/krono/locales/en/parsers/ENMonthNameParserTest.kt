package org.kgusarov.krono.locales.en.parsers

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kgusarov.krono.Krono
import org.kgusarov.krono.KronoDate
import org.kgusarov.krono.ParsingOption
import org.kgusarov.krono.RefDateInputFactory
import org.kgusarov.krono.assertDate
import org.kgusarov.krono.testSingleCase

private const val REF_DATE_2023_04_09 = "2023-04-09T00:00:00"

private const val REF_DATE_2020_11_22 = "2020-11-22T00:00:00"

internal class ENMonthNameParserTest {
    @Test
    fun `month-year - September 2012`() {
        testSingleCase(Krono.enCasual, "September 2012", RefDateInputFactory(KronoDate.now())) {
            assertThat(it.text).isEqualTo("September 2012")

            with (it.start) {
                assertThat(year).isEqualTo(2012)
                assertThat(month).isEqualTo(9)
                assertThat(day).isEqualTo(1)

                assertThat(certainYear).isTrue
                assertThat(certainMonth).isTrue
                assertThat(certainDay).isFalse

                assertDate("2012-09-01T12:00:00")
            }
        }
    }

    @Test
    fun `month-year - Sept 2012`() {
        testSingleCase(Krono.enCasual, "Sept 2012", RefDateInputFactory(KronoDate.now())) {
            assertThat(it.text).isEqualTo("Sept 2012")

            with (it.start) {
                assertThat(year).isEqualTo(2012)
                assertThat(month).isEqualTo(9)
                assertThat(day).isEqualTo(1)

                assertThat(certainYear).isTrue
                assertThat(certainMonth).isTrue
                assertThat(certainDay).isFalse

                assertDate("2012-09-01T12:00:00")
            }
        }
    }

    @Test
    fun `month-year - Sep 2012`() {
        testSingleCase(Krono.enCasual, "Sep 2012", RefDateInputFactory(KronoDate.now())) {
            assertThat(it.text).isEqualTo("Sep 2012")

            with (it.start) {
                assertThat(year).isEqualTo(2012)
                assertThat(month).isEqualTo(9)
                assertThat(day).isEqualTo(1)

                assertThat(certainYear).isTrue
                assertThat(certainMonth).isTrue
                assertThat(certainDay).isFalse

                assertDate("2012-09-01T12:00:00")
            }
        }
    }

    @Test
    fun `month-year - Sep-dot-2012`() {
        testSingleCase(Krono.enCasual, "Sep. 2012", RefDateInputFactory(KronoDate.now())) {
            assertThat(it.text).isEqualTo("Sep. 2012")

            with (it.start) {
                assertThat(year).isEqualTo(2012)
                assertThat(month).isEqualTo(9)
                assertThat(day).isEqualTo(1)

                assertThat(certainYear).isTrue
                assertThat(certainMonth).isTrue
                assertThat(certainDay).isFalse

                assertDate("2012-09-01T12:00:00")
            }
        }
    }

    @Test
    fun `month-year - Sep-2012`() {
        testSingleCase(Krono.enCasual, "Sep-2012", RefDateInputFactory(KronoDate.now())) {
            assertThat(it.text).isEqualTo("Sep-2012")

            with (it.start) {
                assertThat(year).isEqualTo(2012)
                assertThat(month).isEqualTo(9)
                assertThat(day).isEqualTo(1)

                assertThat(certainYear).isTrue
                assertThat(certainMonth).isTrue
                assertThat(certainDay).isFalse

                assertDate("2012-09-01T12:00:00")
            }
        }
    }

    @Test
    fun `month-year - complex phrase`() {
        testSingleCase(
            Krono.enCasual,
            "Statement of comprehensive income for the year ended Dec. 2021",
            RefDateInputFactory(KronoDate.now()),
        ) {
            assertThat(it.text).isEqualTo("Dec. 2021")

            with (it.start) {
                assertThat(year).isEqualTo(2021)
                assertThat(month).isEqualTo(12)
                assertThat(day).isEqualTo(1)

                assertThat(certainYear).isTrue
                assertThat(certainMonth).isTrue
                assertThat(certainDay).isFalse

                assertDate("2021-12-01T12:00:00")
            }
        }
    }

    @Test
    fun `month only - in January`() {
        testSingleCase(Krono.enCasual, "In January", REF_DATE_2020_11_22) {
            assertThat(it.text).isEqualTo("January")

            with (it.start) {
                assertThat(year).isEqualTo(2021)
                assertThat(month).isEqualTo(1)
                assertThat(day).isEqualTo(1)

                assertThat(certainYear).isFalse
                assertThat(certainMonth).isTrue
                assertThat(certainDay).isFalse

                assertDate("2021-01-01T12:00:00")
            }
        }
    }

    @Test
    fun `month only - in Jan`() {
        testSingleCase(Krono.enCasual, "in Jan", REF_DATE_2020_11_22) {
            assertThat(it.text).isEqualTo("Jan")

            with (it.start) {
                assertThat(year).isEqualTo(2021)
                assertThat(month).isEqualTo(1)
                assertThat(day).isEqualTo(1)

                assertThat(certainYear).isFalse
                assertThat(certainMonth).isTrue
                assertThat(certainDay).isFalse

                assertDate("2021-01-01T12:00:00")
            }
        }
    }

    @Test
    fun `month only - May`() {
        testSingleCase(Krono.enCasual, "May", REF_DATE_2020_11_22) {
            assertThat(it.text).isEqualTo("May")

            with (it.start) {
                assertThat(year).isEqualTo(2021)
                assertThat(month).isEqualTo(5)
                assertThat(day).isEqualTo(1)

                assertThat(certainYear).isFalse
                assertThat(certainMonth).isTrue
                assertThat(certainDay).isFalse

                assertDate("2021-05-01T12:00:00")
            }
        }
    }

    @Test
    fun `from May to December`() {
        testSingleCase(Krono.enCasual, "From May to December", REF_DATE_2023_04_09) {
            with (it.start) {
                assertThat(year).isEqualTo(2023)
                assertThat(month).isEqualTo(5)
            }

            with (it.end!!) {
                assertThat(year).isEqualTo(2023)
                assertThat(month).isEqualTo(12)
            }
        }
    }

    @Test
    fun `from December to May`() {
        testSingleCase(Krono.enCasual, "From December to May", REF_DATE_2023_04_09) {
            with (it.start) {
                assertThat(year).isEqualTo(2022)
                assertThat(month).isEqualTo(12)
            }

            with (it.end!!) {
                assertThat(year).isEqualTo(2023)
                assertThat(month).isEqualTo(5)
            }
        }
    }

    @Test
    fun `from May to December 2022`() {
        testSingleCase(Krono.enCasual, "From May to December, 2022", REF_DATE_2023_04_09) {
            with (it.start) {
                assertThat(year).isEqualTo(2022)
                assertThat(month).isEqualTo(5)
            }

            with (it.end!!) {
                assertThat(year).isEqualTo(2022)
                assertThat(month).isEqualTo(12)
            }
        }
    }

    @Test
    fun `from December to May 2022`() {
        testSingleCase(Krono.enCasual, "From December to May 2022", REF_DATE_2023_04_09) {
            with (it.start) {
                assertThat(year).isEqualTo(2021)
                assertThat(month).isEqualTo(12)
            }

            with (it.end!!) {
                assertThat(year).isEqualTo(2022)
                assertThat(month).isEqualTo(5)
            }
        }
    }

    @Test
    fun `forward - in December`() {
        testSingleCase(Krono.enCasual, "in December", REF_DATE_2023_04_09, ParsingOption(forwardDate = true)) {
            with (it.start) {
                assertThat(year).isEqualTo(2023)
                assertThat(month).isEqualTo(12)
            }
        }
    }

    @Test
    fun `forward - in May`() {
        testSingleCase(Krono.enCasual, "in May", REF_DATE_2023_04_09, ParsingOption(forwardDate = true)) {
            with (it.start) {
                assertThat(year).isEqualTo(2023)
                assertThat(month).isEqualTo(5)
            }
        }
    }

    @Test
    fun `forward - From May to December`() {
        testSingleCase(Krono.enCasual, "From May to December", REF_DATE_2023_04_09, ParsingOption(forwardDate = true)) {
            with (it.start) {
                assertThat(year).isEqualTo(2023)
                assertThat(month).isEqualTo(5)
            }

            with (it.end!!) {
                assertThat(year).isEqualTo(2023)
                assertThat(month).isEqualTo(12)
            }
        }
    }

    @Test
    fun `forward - From December to May`() {
        testSingleCase(Krono.enCasual, "From December to May", REF_DATE_2023_04_09, ParsingOption(forwardDate = true)) {
            with (it.start) {
                assertThat(year).isEqualTo(2023)
                assertThat(month).isEqualTo(12)
            }

            with (it.end!!) {
                assertThat(year).isEqualTo(2024)
                assertThat(month).isEqualTo(5)
            }
        }
    }

    @Test
    fun `complex context Sep 2012`() {
        testSingleCase(Krono.enCasual, "The date is Sep 2012 is the date", KronoDate.now()) {
            assertThat(it.index).isEqualTo(12)
            assertThat(it.text).isEqualTo("Sep 2012")

            with (it.start) {
                assertThat(year).isEqualTo(2012)
                assertThat(month).isEqualTo(9)

                assertDate("2012-09-01T12:00:00")
            }
        }
    }

    @Test
    fun `complex context November 2019`() {
        testSingleCase(Krono.enCasual, "By Angie Mar November 2019", KronoDate.now()) {
            assertThat(it.text).isEqualTo("November 2019")

            with (it.start) {
                assertThat(year).isEqualTo(2019)
                assertThat(month).isEqualTo(11)

                assertDate("2019-11-01T12:00:00")
            }
        }
    }
    /*

    test("Test - Month slash expression", function () {
        testSingleCase(chrono, "9/2012", new Date(2012, 7, 10), (result) => {
            expect(result.start).not.toBeNull();
            expect(result.start.get("year")).toBe(2012);
            expect(result.start.get("month")).toBe(9);

            expect(result.index).toBe(0);
            expect(result.text).toBe("9/2012");

            expect(result.start).toBeDate(new Date(2012, 9 - 1, 1, 12));
        });

        testSingleCase(chrono, "09/2012", new Date(2012, 7, 10), (result) => {
            expect(result.start.get("year")).toBe(2012);
            expect(result.start.get("month")).toBe(9);

            expect(result.index).toBe(0);
            expect(result.text).toBe("09/2012");

            expect(result.start).toBeDate(new Date(2012, 9 - 1, 1, 12));
        });
    });

    test("Test - year 90's parsing", () => {
        testSingleCase(chrono, "Aug 96", new Date(2012, 7, 10), (result) => {
            expect(result.text).toBe("Aug 96");

            expect(result.start.get("year")).toBe(1996);
            expect(result.start.get("month")).toBe(8);
        });

        testSingleCase(chrono, "96 Aug 96", new Date(2012, 7, 10), (result) => {
            expect(result.text).toBe("Aug 96");

            expect(result.start.get("year")).toBe(1996);
            expect(result.start.get("month")).toBe(8);
        });
    });

    test("Test - Month should not have timezone", () => {
        testSingleCase(
            chrono,
            "People visiting Buñol towards the end of August get a good chance to participate in La Tomatina (under normal circumstances)",
            new Date(2012, 7, 10),
            (result) => {
                expect(result.text).toBe("August");
                expect(result.start.get("year")).toBe(2012);
                expect(result.start.get("month")).toBe(8);
            }
        );
    });




    test("Test - Month-Only Range expression", () => {

        testSingleCase(chrono, "From December to May 2020", new Date(2023, 4 - 1, 9), (result) => {
            expect(result.start.get("year")).toBe(2019);
            expect(result.start.get("month")).toBe(12);
            expect(result.end.get("year")).toBe(2020);
            expect(result.end.get("month")).toBe(5);
        });

        testSingleCase(chrono, "From December to May 2025", new Date(2023, 4 - 1, 9), (result) => {
            expect(result.start.get("year")).toBe(2024);
            expect(result.start.get("month")).toBe(12);
            expect(result.end.get("year")).toBe(2025);
            expect(result.end.get("month")).toBe(5);
        });
    });

         */
}