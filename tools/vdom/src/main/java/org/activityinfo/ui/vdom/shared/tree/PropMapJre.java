package org.activityinfo.ui.vdom.shared.tree;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.safehtml.shared.SafeUri;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Node Property Map.
 *
 * <p>Important: an alternate implementation is provided in the translatable/ source root
 * that will allow the GWT compiler to map this to a simple Javascript object. If you add accessor/setter methods here,
 * they must also be copied to the translatable version.
 * </p>
 */
final class PropMapJre implements PropMap {


    private final Map<String, Object> propMap = new HashMap<>();

    PropMapJre() {
        if(GWT.isScript()) {
            throw new IllegalStateException("super sources are not being used.");
        }
    }

    @Override
    public PropMap set(String propertyName, Object value) {
        propMap.put(propertyName, value);
        return this;
    }

    @Override
    public Object get(String propertyName) {
        return propMap.get(propertyName);
    }

    private void remove(String propertyName) {
        propMap.remove(propertyName);
    }


    @GwtIncompatible
    public Set<String> keys() {
        return propMap.keySet();
    }

    @Override
    public boolean isEmpty() {
        return propMap.isEmpty();
    }

    /*================= Below this point the Java and Javascript versions are the same ===================*/


    public static final PropMap EMPTY = Props.create();


    @Override
    public void addClassName(String newClass) {
        String classNameValue = (String) get("className");
        if(classNameValue == null) {
            set("className", newClass);
        } else {
            set("className", classNameValue + " " + newClass);
        }
    }

    @Override
    public PropMap addClassName(String className, boolean add) {
        if(add) {
            addClassName(className);
        }
        return this;
    }

    /**
     * Sets the data-{dataPropertyName} property to the given value.
     */
    @Override
    public PropMap data(String dataPropertyName, String value) {
        return set("data-" + dataPropertyName, value);
    }

    @Override
    public PropMap href(SafeUri uri) {
        return set("href", uri.asString());
    }

    /**
     * Sets the "id" property
     */
    @Override
    public PropMap setId(String id) {
        return set("id", id);
    }


    @Override
    public PropMap setClass(String classNames) {
        return set("className", classNames);
    }

    @Override
    public PropMap setClass(String className, boolean add) {
        if(add) {
            setClass(className);
        }
        return this;
    }

    public static boolean isObject(Object object) {
        return object instanceof PropMapJre;
    }

    @Override
    public PropMap setStyle(Style style) {
        set("style", style.asPropMap());
        return this;
    }

    @Override
    public PropMap disabled(boolean disabled) {
        if(disabled) {
            set("disabled", "true");
        } else {
            remove("disabled");
        }
        return this;
    }

    @Override
    public PropMap onclick(EventHandler handler) {
        return set("onclick", handler);
    }

    @Override
    public PropMap oninput(EventHandler handler) {
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

    @Override
    public PropMap ondragover(EventHandler handler) {
        return set("ondragover", handler);
    }

    @Override
    public PropMap ondragleave(EventHandler handler) {
        return set("ondragleave", handler);
    }

    @Override
    public PropMap ondrop(EventHandler handler) {
        return set("ondrop", handler);
    }

    @Override
    public EventHandler getEventHandler(String eventName) {
        return null;
    }

    @Override
    public PropMap draggable(boolean draggable) {
        return set("draggable", draggable ? "true" : "false");
    }

    @Override
    public PropMap placeholder(String text) {
        return set("placeholder", text);
    }

    @Override
    public PropMap setData(String name, String value) {
        return set("data-" + name, value);
    }
}
