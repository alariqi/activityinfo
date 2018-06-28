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
package org.activityinfo.model.job;

import org.activityinfo.json.JsonValue;
import org.activityinfo.model.analysis.ImmutableTableAnalysisModel;
import org.activityinfo.model.analysis.TableAnalysisModel;
import org.activityinfo.model.resource.ResourceId;

import static org.activityinfo.json.Json.createObject;

/**
 * Exports a single form to a CSV table
 */
public class ExportFormJob implements JobDescriptor<ExportResult> {

    public static final String TYPE = "exportForm";

    private TableAnalysisModel tableModel;

    public ExportFormJob(TableAnalysisModel tableModel) {
        this.tableModel = tableModel;
    }

    public ResourceId getFormId() {
        return tableModel.getFormId();
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public ExportResult parseResult(JsonValue resultObject) {
        return ExportResult.fromJson(resultObject);
    }

    public TableAnalysisModel getTableModel() {
        return tableModel;
    }

    @Override
    public JsonValue toJson() {
        JsonValue object = createObject();
        object.put("model", tableModel.toJson());
        return object;
    }

    public static ExportFormJob fromJson(JsonValue object) {
        TableAnalysisModel tableModel;
        if(object.hasKey("formId")) {
            tableModel = ImmutableTableAnalysisModel.builder()
                    .formId(ResourceId.valueOf(object.get("formId").asString()))
                    .build();
        } else {
            tableModel = TableAnalysisModel.fromJson(object.get("model"));
        }
        return new ExportFormJob(tableModel);
    }
}
