package org.activityinfo.ui.vdom.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.ui.vdom.shared.tree.VComponent;


public interface RenderContext {

    void attachWidget(Widget widget, Element container);

    void detachWidget(Widget widget);

    void fireUpdate(VComponent thunk);

}
