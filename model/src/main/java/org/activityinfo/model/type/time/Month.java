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
package org.activityinfo.model.type.time;

import org.activityinfo.json.Json;
import org.activityinfo.json.JsonValue;
import org.activityinfo.model.type.FieldTypeClass;

import java.io.Serializable;
import java.util.Date;

/**
 * Encapsulates a Gregorian month
 *
 * @author Alex Bertram
 */
public class Month implements Serializable, Comparable<Month>, PeriodValue {

    private static final int MONTHS_PER_YEAR = 12;



    private int year;
    private int month;

    public static final String PROPERTY_PREFIX = "M";

    /**
     * Constructs an uninitialized <code>Month</code>
     */
    public Month() {
    }

    /**
     * Constructs a <code>Month</code> at the given month and year. If month is
     * outside of the range 1..12, the values are normalized. For example,
     * <code>Month(2001, 13)</code> initalizes this <code>Month</code> to
     * January, 2002.
     *
     * @param year  Gregorian year (for example, 1999, 2009)
     * @param month Gregorian month (january=1, feb=2, ... dec=12)
     */
    public Month(int year, int month) {
        this.year = year;
        this.month = month;

        normalize();
    }

    private void normalize() {
        while (month > MONTHS_PER_YEAR) {
            year++;
            month -= MONTHS_PER_YEAR;
        }
        while (month < 1) {
            year--;
            month += MONTHS_PER_YEAR;
        }
    }

    /**
     * Gets this <code>Month</code>'s year
     *
     * @return the month's year.
     */
    public int getYear() {
        return year;
    }

    /**
     * Gets this month
     *
     * @return The Gregorian month (1=january, 12=december)
     */
    public int getMonth() {
        return month;
    }


    public int getMonthOfYear() {
        return month;
    }

    public LocalDate getFirstDayOfMonth() {
        return new LocalDate(year, month, 1);
    }


    /**
     * Compares this <code>Month</code> to another month
     *
     * @param m The <code>Month</code> with which to compare this
     *          <code>Month</code>
     * @return 0 if this month is the same as <code>m</code>, -1 if this month
     * is earlier than <code>m</code>, or +1 if this month follows
     * <code>m</code>
     */
    @Override
    public int compareTo(Month m) {
        if (year < m.year) {
            return -1;
        }
        if (year > m.year) {
            return 1;
        }
        if (month < m.month) {
            return -1;
        }
        if (month > m.month) {
            return 1;
        }
        return 0;
    }

    /**
     * Returns a string representation of this Month in the format 2009-1
     *
     * @return a string representation of this Month in the format 2009-1,
     * 2009-12
     */
    @Override
    public String toString() {
        return toString(year, month);
    }

    public static String toString(int year, int month) {
        return year + (month < 10 ? "-0" : "-") + month;
    }

    /**
     * @return a string representation of this Month in the format M2009-1, M2009-12
     */
    public String getPropertyName() {
        return PROPERTY_PREFIX + toString();
    }

    /**
     * Parses a string in the format 2009-1, 2009-03, 2009-12 and returns the
     * value as a <code>Month</code>
     *
     * @param s The <code>String</code> to parse
     * @return The value of the string as a <code>Month</code>
     */
    @SuppressWarnings("NonJREEmulationClassesInClientCode")
    public static Month parseMonth(String s) {
        String[] tokens = s.split("-");
        if (tokens.length != 2) {
            throw new NumberFormatException();
        }

        return new Month(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Month month1 = (Month) o;

        if (month != month1.month) {
            return false;
        }
        if (year != month1.year) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        return result;
    }

    public Month plus(int count) {
        return new Month(year, month + count);
    }

    public Month next() {
        return plus(+1);
    }

    public Month previous() {
        return plus(-1);
    }

    @SuppressWarnings("deprecation")
    public static Month of(Date date) {
        return new Month(date.getYear()+1900, date.getMonth()+1);
    }

    @Override
    public FieldTypeClass getTypeClass() {
        return MonthType.TYPE_CLASS;
    }

    @Override
    public JsonValue toJson() {
        return Json.create(toString());
    }

    @Override
    public LocalDateInterval asInterval() {
        return new LocalDateInterval(
                new LocalDate(year, month, 1),
                new LocalDate(year, month, LocalDate.getLastDayOfMonth(year, month)));
    }
}
