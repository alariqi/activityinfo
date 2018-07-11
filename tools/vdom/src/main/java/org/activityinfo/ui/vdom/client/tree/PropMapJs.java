package org.activityinfo.ui.vdom.client.tree;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.safehtml.shared.SafeUri;
import org.activityinfo.ui.vdom.shared.tree.EventHandler;
import org.activityinfo.ui.vdom.shared.tree.PropMap;
import org.activityinfo.ui.vdom.shared.tree.Style;

import java.util.Set;

/**
 * Node Property Map.
 *
 * <p>This is a specialized version that translates to a single Javascript object.</p>
 */
public final class PropMapJs extends JavaScriptObject implements PropMap {

    protected PropMapJs() {
    }

    public static native PropMapJs create() /*-{
        return {};
    }-*/;

    public native PropMapJs set(String propertyName, Object value) /*-{
        this[propertyName] = value;
        return this;
    }-*/;

    public native Object get(String propertyName) /*-{
        return this[propertyName];
    }-*/;

    private native void remove(String propertyName) /*-{
        delete this[propertyName];
    }-*/;

    @Override
    @GwtIncompatible
    public Set<String> keys() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public native boolean isEmpty() /*-{
        for(var prop in this) {
            if(this.hasOwnProperty(prop))
                return false;
        }
        return true;
    }-*/;

    /*================= Below this point the Java and Javascript versions are the same ===================*/


    /**
     * Creates a new {@code PropMapJs} with the given style object.
     */
    public static PropMapJs withStyle(Style style) {
        PropMapJs propMap = create();
        propMap.setStyle(style);
        return propMap;
    }

    /**
     * Creates a new {@code PropMapJs} with the given value for the {@code className} property
     */
    public static PropMapJs withClasses(String classes) {
        return create().setClass(classes);
    }


    public void addClassName(String newClass) {
        String classNameValue = (String) get("className");
        if(classNameValue == null) {
            set("className", newClass);
        } else {
            set("className", classNameValue + " " + newClass);
        }
    }

    public PropMapJs addClassName(String className, boolean add) {
        if(add) {
            addClassName(className);
        }
        return this;
    }


    /**
     * Sets the data-{dataPropertyName} property to the given value.
     */
    public PropMapJs data(String dataPropertyName, String value) {
        return set("data-" + dataPropertyName, value);
    }

    public PropMapJs href(SafeUri uri) {
        return set("href", uri.asString());
    }

    /**
     * Sets the "id" property
     */
    public PropMapJs setId(String id) {
        return set("id", id);
    }


    public PropMapJs setClass(String classNames) {
        return set("className", classNames);
    }

    public PropMapJs setClass(String className, boolean add) {
        if(add) {
            setClass(className);
        }
        return this;
    }

    public static boolean isObject(Object object) {
        return object instanceof PropMapJs;
    }

    public PropMapJs setStyle(Style style) {
        set("style", style.asPropMap());
        return this;
    }

    public PropMapJs disabled(boolean disabled) {
        if(disabled) {
            set("disabled", "true");
        } else {
            remove("disabled");
        }
        return this;
    }

    public PropMapJs onclick(EventHandler handler) {
        return set("onclick", handler);
    }

    public PropMapJs oninput(EventHandler handler) {
        return set("oninput", handler);
    }

    @Override
    public PropMap ondragstart(EventHandler handler) {
        return set("ondragstart", handler);
    }

    @Override
    public PropMap ondragend(EventHandler handler) {
        return set("ondragend", handler);
    }


    public EventHandler getEventHandler(String eventName) {
        return null;
    }

    public PropMapJs draggable(boolean draggable) {
        return set("draggable", draggable ? "true" : "false");
    }

    public PropMapJs placeholder(String text) {
        return set("placeholder", text);
    }
}
