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
package org.activityinfo.json.impl;

import org.activityinfo.json.Json;
import org.activityinfo.json.JsonFactory;
import org.activityinfo.json.JsonType;
import org.activityinfo.json.JsonValue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Server-side implementation of JsonArray.
 */
class JreJsonArray extends JreJsonValue {

    private static final long serialVersionUID = 1L;

    private transient ArrayList<JsonValue> arrayValues = new ArrayList<JsonValue>();

    private transient JsonFactory factory;

    public JreJsonArray(JsonFactory factory) {
        this.factory = factory;
    }

    @Override
    public boolean asBoolean() {
        return true;
    }

    @Override
    public double asNumber() {
        switch (length()) {
            case 0:
                return 0;
            case 1:
                return get(0).asNumber();
            default:
                return Double.NaN;
        }
    }

    @Override
    public String asString() {
        StringBuilder toReturn = new StringBuilder();
        for (int i = 0; i < length(); i++) {
            if (i > 0) {
                toReturn.append(",");
            }
            toReturn.append(get(i).asString());
        }
        return toReturn.toString();
    }

    @Override
    public Iterable<JsonValue> values() {
        return new JsonArrayIterable(this);
    }


    public boolean getBoolean(int index) {
        return get(index).asBoolean();
    }

    public double getNumber(int index) {
        return get(index).asNumber();
    }

    public JsonValue get(int index) {
        return arrayValues.get(index);
    }

    public Object getObject() {
        List<Object> objs = new ArrayList<Object>();
        for (JsonValue val : arrayValues) {
            objs.add(((JreJsonValue) val).getObject());
        }
        return objs;
    }

    public String getString(int index) {
        return get(index).asString();
    }

    public JsonType getType() {
        return JsonType.ARRAY;
    }

    @Override
    public boolean jsEquals(JsonValue value) {
        return getObject().equals(((JreJsonValue) value).getObject());
    }

    public int length() {
        return arrayValues.size();
    }

    public void set(int index, JsonValue value) {
        if (value == null) {
            value = factory.createNull();
        }
        if (index == arrayValues.size()) {
            arrayValues.add(index, value);
        } else {
            arrayValues.set(index, value);
        }
    }

    public void set(int index, String string) {
        set(index, factory.create(string));
    }

    public void set(int index, double number) {
        set(index, factory.create(number));
    }

    public void set(int index, boolean bool) {
        set(index, factory.create(bool));
    }

    @Override
    public void add(JsonValue value) {
        arrayValues.add(value);
    }

    public String toJson() {
        return JsonUtil.stringify(this);
    }

    @Override
    public void traverse(JsonVisitor visitor,
                         JsonContext ctx) {
        if (visitor.visitArray(this, ctx)) {
            JsonArrayContext arrayCtx = new JsonArrayContext(this);
            for (int i = 0; i < length(); i++) {
                arrayCtx.setCurrentIndex(i);
                if (visitor.visitIndex(arrayCtx.getCurrentIndex(), arrayCtx)) {
                    visitor.accept(get(i), arrayCtx);
                    arrayCtx.setFirst(false);
                }
            }
        }
        visitor.endArrayVisit(this, ctx);
    }

    @Override
    public boolean isJsonArray() {
        return true;
    }

    @com.google.gwt.core.shared.GwtIncompatible
    private void readObject(ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        JreJsonArray instance = parseJson(stream);
        this.factory = Json.instance();
        this.arrayValues = instance.arrayValues;
    }

    @com.google.gwt.core.shared.GwtIncompatible
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(toJson());
    }
}
