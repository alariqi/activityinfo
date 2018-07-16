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
package org.activityinfo.ui.client;

import com.google.common.base.Function;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.ui.client.analysis.AnalysisPlace;
import org.activityinfo.ui.client.database.DatabaseListPlace;
import org.activityinfo.ui.client.database.DatabasePlace;
import org.activityinfo.ui.client.importer.ImportPlace;
import org.activityinfo.ui.client.input.RecordPlace;
import org.activityinfo.ui.client.table.TablePlace;

import javax.annotation.Nullable;

public class AppPlaceHistoryMapper implements Function<String, Place> {


    @Override
    public Place apply(@Nullable String token) {

        if(token != null) {

            String[] parts = token.split("/");
            if (parts[0].equals("table")) {
                return TablePlace.parse(parts);

            } else if (parts[0].equals("import") && parts.length == 2) {
                return new ImportPlace(ResourceId.valueOf(parts[1]));

            } else if(parts[0].equals("edit")) {
                return new RecordPlace(new RecordRef(ResourceId.valueOf(parts[1]), ResourceId.valueOf(parts[2])));

            } else if (parts[0].equals("analysis")) {
                if (parts.length > 1) {
                    return new AnalysisPlace(parts[1]);
                } else {
                    return new AnalysisPlace(ResourceId.generateCuid());
                }

            } else if (parts[0].equals("databases")) {
                return DatabaseListPlace.INSTANCE;

            } else if (parts[0].equals("database")) {
                if(parts.length == 2) {
                    return new DatabasePlace(ResourceId.valueOf(parts[1]));
                } else if(parts.length == 3) {
                    return new DatabasePlace(ResourceId.valueOf(parts[1]), ResourceId.valueOf(parts[2]));
                }
            }
        }
        return DatabaseListPlace.INSTANCE;
    }


}
