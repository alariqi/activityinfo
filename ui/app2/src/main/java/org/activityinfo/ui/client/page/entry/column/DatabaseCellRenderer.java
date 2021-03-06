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
package org.activityinfo.ui.client.page.entry.column;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import org.activityinfo.legacy.shared.model.ActivityDTO;
import org.activityinfo.legacy.shared.model.SchemaDTO;
import org.activityinfo.legacy.shared.model.SiteDTO;

class DatabaseCellRenderer implements GridCellRenderer<SiteDTO> {

    private final SchemaDTO schema;

    public DatabaseCellRenderer(SchemaDTO schema) {
        this.schema = schema;
    }

    @Override
    public SafeHtml render(SiteDTO model,
                           String property,
                           ColumnData config,
                           int rowIndex,
                           int colIndex,
                           ListStore<SiteDTO> store,
                           Grid<SiteDTO> grid) {

        ActivityDTO activity = schema.getActivityById(model.getActivityId());
        return activity == null ?
                SafeHtmlUtils.EMPTY_SAFE_HTML :
                SafeHtmlUtils.fromString(activity.getDatabaseName());
    }
}
