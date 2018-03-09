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
package org.activityinfo.ui.client.page.entry;

import com.extjs.gxt.ui.client.data.BaseModelData;
import org.activityinfo.legacy.shared.reports.content.YearCategory;
import org.activityinfo.legacy.shared.reports.model.DateRange;
import org.activityinfo.ui.client.dispatch.type.DateUtilGWTImpl;

final class YearModel extends BaseModelData {

    private YearCategory category;

    public YearModel(YearCategory category) {
        this.category = category;
        set("name", category.getLabel());
    }

    /**
     * Further restricts an existing filter to the year range defined by this
     * model.
     */
    public DateRange getDateRange() {
        return DateUtilGWTImpl.INSTANCE.yearRange(category.getYear());
    }

    public String getKey() {
        return "Y" + category.getYear();
    }

}
