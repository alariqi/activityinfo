package org.activityinfo.ui.vdom.shared.tree;

import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.safehtml.shared.SafeUri;

import java.util.Set;

public interface PropMap {

    PropMap EMPTY = Props.create();

    PropMap set(String propertyName, Object value);

    Object get(String propertyName);

    void addClassName(String newClass);

    PropMap addClassName(String className, boolean add);

    PropMap data(String dataPropertyName, String value);

    PropMap href(SafeUri uri);

    PropMap setId(String id);

    PropMap setClass(String classNames);

    PropMap setClass(String className, boolean add);

    PropMap setStyle(Style style);

    PropMap setStyle(PropMap style);

    PropMap setTitle(String title);

    PropMap disabled(boolean disabled);

    PropMap onclick(EventHandler handler);

    PropMap oninput(EventHandler handler);

    PropMap ondragstart(EventHandler handler);

    PropMap ondragend(EventHandler handler);

    PropMap ondragover(EventHandler handler);

    PropMap ondragleave(EventHandler handler);

    PropMap ondrop(EventHandler handler);

    EventHandler getEventHandler(String eventName);

    PropMap draggable(boolean draggable);

    PropMap placeholder(String text);

    PropMap setData(String name, String value);

    @GwtIncompatible
    Set<String> keys();

    boolean isEmpty();

}