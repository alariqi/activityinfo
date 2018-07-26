/*
 * ActivityInfo
 * Copyright (C) 2009-2013 UNICEF
 * Copyright (C) 2014-2018 BeDataDriven Groep B.V.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.activityinfo.io.match.date;

import org.activityinfo.model.type.time.LocalDate;
import org.junit.Ignore;
import org.junit.Test;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;


public class LatinDateParserTest {


    @Test
    public void simpleTests() {

        LatinDateParser parser = new LatinDateParser();

        assertThat(parser.parse("2011-01-15"), equalTo(ymd(2011,1,15)));
        assertThat(parser.parse("15 May 2049"), equalTo(ymd(2049,5,15)));
        assertThat(parser.parse("4/30/07"), equalTo(ymd(2007,4,30)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidInput() {
        LatinDateParser parser = new LatinDateParser();
        assertThat(parser.parse("foo"), nullValue());
    }

    @Test
    public void turkishDates() {
        LatinDateParser parser = new LatinDateParser();

        assertThat(parser.parse("3.12.2018"), equalTo(new LocalDate(2018, 12, 3)));
        assertThat(parser.parse("3.ocak.2018"), equalTo(new LocalDate(2018, 1, 3)));
        assertThat(parser.parse("15.mayıs.2018"), equalTo(new LocalDate(2018, 5, 15)));
    }

    @Test
    public void monthNameDayYear() {
        assertThat(convertString("Oct 31st, 1940"), equalTo(ymd(1940,10,31)));
    }

    @Test
    public void longDateTest() {
        assertThat(convertString("Wed, 14th of May, 1999"), equalTo(ymd(1999,5,14)));
    }


    private LocalDate convertString(String string) {
        return new LatinDateParser().parse(string);
    }

    private LocalDate ymd(int year, int month, int day) {
        return new LocalDate(year, month, day);
    }

    @Test
    @Ignore
    public void exhaustiveTest() {

        int styles[] = new int[] { DateFormat.SHORT, DateFormat.MEDIUM, DateFormat.LONG };

        String languages[] = new String[] { "en", "fr", "es", "it", "pt", "nl" };

        for(int style : styles) {
            for(String language : languages) {
                testFormat(language, DateFormat.getDateInstance(style, Locale.forLanguageTag(language)));
            }
        }
    }


    private void testFormat(String language, DateFormat format) {
        LatinDateParser converter = new LatinDateParser();

        for(int year=1950;year<2050;++year) {
            for(int month=1;month<=12;++month) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month - 1);
                int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                for(int day=1;day<=daysInMonth;++day) {

                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    Date date = calendar.getTime();
                    LocalDate localDate = new LocalDate(date);

                    String string = format.format(date);
                    try {
                        LocalDate converted = converter.parse(string);

                        if(!converted.equals(localDate)) {
                            System.out.println("[" + language +"] " + string + " => " + converted +
                            " [expected: " + localDate + "]");
                        }
                    } catch(Exception e) {
                        System.out.println("[" + language +"] " + string + " => " + "ERROR: " + e.getMessage());
                    }
                }
            }
        }
    }
}
