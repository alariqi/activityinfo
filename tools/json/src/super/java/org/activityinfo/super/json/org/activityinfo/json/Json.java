/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.activityinfo.json;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GwtScriptOnly;
import org.activityinfo.json.impl.JsJsonFactory;
import org.activityinfo.json.JsonSerializable;

/**
 * Vends out implementation of JsonFactory.
 */
@GwtScriptOnly
public class Json {

  private Json() {
  }

  public static JsonValue createFromNullable(String string) {
    return instance().createFromNullable(string);
  }

  public static JsonValue create(String string) {
    return instance().create(string);
  }

  public static JsonValue create(boolean bool) {
    return instance().create(bool);
  }

  public static JsonValue createArray() {
    return instance().createArray();
  }

  public static JsonValue createNull() {
    return instance().createNull();
  }

  public static JsonValue create(double number) {
    return instance().create(number);
  }

  public static JsonValue createObject() {
    return instance().createObject();
  }

  public static JsonFactory instance() {
      assert GWT.isScript() : "Super-sourced Json class ran in DevMode";
      return new JsJsonFactory();
  }

  public static JsonValue parse(String jsonString) {
    return instance().parse(jsonString);
  }

  public static native String stringify(Object value) /*-{
    return $wnd.JSON.stringify(value);
  }-*/;


  public static native String stringify(JsonValue jsonValue) /*-{
    return $wnd.JSON.stringify(jsonValue);
  }-*/;

  public static native String stringify(JsonValue jsonValue, int indent) /*-{
    return $wnd.JSON.stringify(jsonValue, indent);
  }-*/;

  public static JsonValue toJson(Object value) {
    if(value instanceof JsonSerializable) {
      return ((JsonSerializable) value).toJson();

    } else if(value instanceof Collection) {
      JsonValue array = createArray();
      Collection<?> collection = (Collection)value;
      for(Object element : collection) {
        array.add(toJson(element));
      }
      return array;

    } else {
      return toJsonMagic(value);
    }
  }


  public static JsonValue toJsonArray(Iterable<? extends JsonSerializable> objects) {
    JsonValue array = Json.createArray();
    for (JsonSerializable object : objects) {
      array.add(object.toJson());
    }
    return array;
  }

  private static native JsonValue toJsonMagic(Object value) /*-{
    // value *must* be either JsonValue or a type annotated with @JsType or
    // the result will be gibberish.
    return value;
  }-*/;

  public static native <T> T fromJson(Class<T> clazz, JsonValue object) throws JsonMappingException /*-{
    // value *must* be either JsonValue or a type annotated with @JsType or
    // the result will be gibberish.
    return object;
  }-*/;

  public static <T> List<T> fromJsonArray(Class<T> clazz, JsonValue array) throws JsonMappingException {
    return Arrays.<T>asList(fromJsonArray(array));
  }

  private static native <T> T[] fromJsonArray(JsonValue array) throws JsonMappingException /*-{
      // value *must* be either JsonValue or a type annotated with @JsType or
      // the result will be gibberish.
      return array;
  }-*/;
}
