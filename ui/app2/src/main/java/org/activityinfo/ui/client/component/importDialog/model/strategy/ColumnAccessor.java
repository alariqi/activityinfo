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
package org.activityinfo.ui.client.component.importDialog.model.strategy;

import org.activityinfo.ui.client.component.importDialog.model.source.SourceRow;

/**
 * Interface to a column's value, abstracting away differences between
 * columns in imported tables, values provided by users during import and
 * (ultimately) calculated values
 */
public interface ColumnAccessor {

    String getHeading();

    String getValue(SourceRow row);

    boolean isMissing(SourceRow row);



}