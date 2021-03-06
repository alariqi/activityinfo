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
package org.activityinfo.legacy.shared.reports.util;

import org.activityinfo.legacy.shared.reports.content.DimensionCategory;
import org.activityinfo.legacy.shared.reports.content.MonthCategory;
import org.activityinfo.legacy.shared.reports.content.YearCategory;
import org.activityinfo.legacy.shared.reports.model.DateRange;
import org.activityinfo.model.date.DateUnit;
import org.activityinfo.model.type.time.Month;

import java.util.Date;

/**
 * Abstract class providing functions for manipulating dates. There are concrete
 * implementations based on the <code>Calendar</code> class, and another for
 * client-side usage.
 *
 * @author Alex Bertram
 * @see org.activityinfo.ui.client.dispatch.type.DateUtilGWTImpl
 * @see org.activityinfo.server.report.util.DateUtilCalendarImpl
 */
public abstract class DateUtil {

    public abstract DateRange yearRange(int year);

    public abstract DateRange monthRange(int year, int month);

    public DateRange monthRange(Month month) {
        return monthRange(month.getYear(), month.getMonth());
    }

    public DateRange rangeFromCategory(DimensionCategory category) {

        if (category instanceof YearCategory) {
            return yearRange(((YearCategory) category).getYear());
        } else if (category instanceof MonthCategory) {
            MonthCategory monthCategory = (MonthCategory) category;
            return monthRange(monthCategory.getYear(), monthCategory.getMonth());
        } else {
            return new DateRange();
        }
    }

    public abstract int getYear(Date date);

    public abstract int getMonth(Date date);

    public abstract int getDay(Date date);

    public abstract Date floorMonth(Date date);

    public abstract Date ceilMonth(Date date);

    public abstract Date add(Date date, DateUnit dateUnit, int count);

    public abstract boolean isLastDayOfMonth(Date date);

    public Date startDateOfLastCompleteMonth(Date today) {
        // set this to the beginning of the last complete month
        Date start = floorMonth(today);
        if (!isLastDayOfMonth(today)) {
            start = add(start, DateUnit.MONTH, -1);

        }
        return start;
    }

    public Date endDateOfLastCompleteMonth(Date today) {
        // set this to the beginning of the last complete month
        Date end = ceilMonth(today);
        if (!isLastDayOfMonth(today)) {
            end = add(end, DateUnit.MONTH, -1);
        }
        return end;
    }

    public DateRange lastCompleteMonthRange(Date today) {
        return new DateRange(startDateOfLastCompleteMonth(today), endDateOfLastCompleteMonth(today));
    }
}
