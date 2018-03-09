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
package org.activityinfo.model.analysis;

import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import org.activityinfo.json.JsonValue;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public final class AnalysisUpdate {

    private String id;
    private String parentId;
    private String type;
    private String label;
    private JsonValue model;

    @JsOverlay
    public String getId() {
        return id;
    }

    @JsOverlay
    public void setId(String id) {
        this.id = id;
    }

    @JsOverlay
    public String getParentId() {
        return parentId;
    }

    @JsOverlay
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @JsOverlay
    public String getType() {
        return type;
    }

    @JsOverlay
    public void setType(String type) {
        this.type = type;
    }

    @JsOverlay
    public JsonValue getModel() {
        return model;
    }

    @JsOverlay
    public void setModel(JsonValue model) {
        this.model = model;
    }

    @JsOverlay
    public String getLabel() {
        return label;
    }

    @JsOverlay
    public void setLabel(String title) {
        this.label = title;
    }
}
