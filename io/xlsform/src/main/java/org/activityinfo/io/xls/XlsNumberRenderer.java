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
package org.activityinfo.io.xls;

import org.activityinfo.analysis.table.ColumnRenderer;
import org.apache.poi.ss.usermodel.Cell;

public class XlsNumberRenderer implements XlsColumnRenderer {

    private final ColumnRenderer<? extends Number> renderer;

    public XlsNumberRenderer(ColumnRenderer<? extends Number> renderer) {
        this.renderer = renderer;
    }

    @Override
    public boolean isMissing(int row) {
        return renderer.render(row) == null;
    }

    @Override
    public void setValue(Cell cell, int row) {
        cell.setCellValue(renderer.render(row).doubleValue());
    }
}
