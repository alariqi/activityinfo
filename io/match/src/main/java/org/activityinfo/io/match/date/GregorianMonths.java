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

/**
 * Parsing functions for Gregorian months
 */
public class GregorianMonths {
    
    public static final int NOT_FOUND = -1;
    
    public static final int JAN = 1;
    public static final int FEB = 2;
    public static final int MARCH = 3;
    public static final int APRIL = 4;
    public static final int MAY = 5;
    public static final int JUNE = 6;
    public static final int JULY = 7;
    public static final int AUG = 8;
    public static final int SEPT = 9;
    public static final int OCT = 10;
    public static final int NOV = 11;
    public static final int DEC = 12;
    
    public static final int MIN_MONTH_NAME_LENGTH = 2;
    
    private static final int[] MAX_DAYS = new int[] { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };


    /**
     * 
     * @param month the one-based index of the month
     * @return the maximum number of days in the given calendar month for any given year. 
     */
    public static int getMaxDaysInMonth(int month) {
        assert month >=1 && month <= 12;
        
        return MAX_DAYS[month-1];
    }


    /**
     * Tries to parse a string as a month name in any language using
     * a series of hand-tuned heuristics.
     *
     * @param string the string, with a length of at least {@code MIN_MONTH_NAME_LENGTH}
     * @return a month index, 1-12, or {@code NOT_FOUND} if there is no match
     */
    public static int tryParseName(String string) {
        String lowered = string.toLowerCase();

        // Some of the month names in turkish are not derived from
        // latin, so require special handling
        // https://en.wikipedia.org/wiki/Turkish_months
        switch (lowered) {
            case "ocak":
                return JAN;
            case "subat":
            case "şubat":
                return FEB;
            case "nisan":
                return APRIL;
            case "haziran":
                return JUNE;
            case "temmuz":
                return JULY;
            case "eylül":
            case "eylul":
                return SEPT;
            case "ekim":
                return SEPT;
            case "kasım":
            case "kasim":
                return NOV;
            case "aralık":
            case "aralik":
                return DEC;
        }

        switch(lowered.charAt(0)) {
            case 'e':
                return JAN; // enero
            case 'f':
                return FEB; // februrary, febrero
            case 'm':
                if(hasAny(lowered, 'y', 'i', 'g')) {
                    return MAY; // may, maio, mei, mayo, mag (it)
                } else {
                    return MARCH; // march, marzo, marco, marz
                }
            case 'a':
                if(hasAny(lowered, 'g', 'o')) {
                    return AUG;
                } else if(hasAny(lowered, 'b', 'p', 'v')) {
                    return APRIL;
                }
                break;
            case 'i':
            case 'j':
                if(hasChar(lowered, 'a')) {
                    return JAN;  // january, januar, januari,

                } else if(hasChar(lowered, 'n')) {
                    return JUNE; // june, juni, junio

                } else if(hasAny(lowered, 'i', 'y', 'l')) {
                    return JULY; // july, julio, juli
                }
                break;

            case 'l':
                if(lowered.charAt(1) == 'u') {
                    return JULY; // luglio (it)
                }

            case 's':
                return SEPT; // september, septiembre, etc

            case 'o':
                return OCT; // oktober, october, octubre

            case 'n':
                if(lowered.indexOf('v') != -1) {
                    return NOV; // november, noviembre,
                }
                break;

            case 'd':
                return DEC;

            case 'g':
                // italian
                if(lowered.indexOf('e') != -1) {
                    return JAN; // genn
                } else if(lowered.indexOf('u') != -1) {
                    return JUNE; // giugno
                }
        }

        return NOT_FOUND;
    }

    private static boolean hasAny(String lowered, char i, char b, char c) {
        return lowered.indexOf(i) != -1 || lowered.indexOf(b) != -1 || lowered.indexOf(c) != -1;
    }

    private static boolean hasChar(String lowered, char a) {
        return lowered.indexOf(a) != -1;
    }

    private static boolean hasAny(String lowered, char a, char b) {
        return lowered.indexOf(a) != -1 || lowered.indexOf(b) != -1;
    }
}
