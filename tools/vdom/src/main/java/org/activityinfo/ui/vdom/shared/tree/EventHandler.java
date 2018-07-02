package org.activityinfo.ui.vdom.shared.tree;

import com.google.gwt.user.client.Event;
import jsinterop.annotations.JsFunction;

@JsFunction
public interface EventHandler {
    void onEvent(Event event);
}
