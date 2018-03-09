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
package org.activityinfo.server.report.generator;

import org.activityinfo.legacy.shared.command.Filter;
import org.activityinfo.legacy.shared.command.PivotSites;
import org.activityinfo.legacy.shared.command.PivotSites.PivotResult;
import org.activityinfo.legacy.shared.impl.pivot.PivotTableDataBuilder;
import org.activityinfo.legacy.shared.reports.content.PivotTableData;
import org.activityinfo.legacy.shared.reports.model.Dimension;
import org.activityinfo.legacy.shared.reports.model.PivotReportElement;
import org.activityinfo.server.command.DispatcherSync;

import java.util.List;
import java.util.Locale;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public abstract class PivotGenerator<T extends PivotReportElement> extends BaseGenerator<T> {

    public PivotGenerator(DispatcherSync dispatcher) {
        super(dispatcher);
    }

    protected PivotTableData generateData(int userId,
                                          Locale locale,
                                          T element,
                                          Filter filter,
                                          List<Dimension> rowDims,
                                          List<Dimension> colDims) {

        PivotSites command = new PivotSites(element.allDimensions(), filter);
        PivotResult result = command.isTooBroad() ? new PivotResult() : getDispatcher().execute(command);

        PivotTableDataBuilder builder = new PivotTableDataBuilder();
        return builder.build(rowDims, colDims, result.getBuckets());
    }
}
