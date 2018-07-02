package org.activityinfo.ui.vdom.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.ui.vdom.shared.tree.VComponent;

public class SimpleRenderContext implements RenderContext {

    @Override
    public void attachWidget(Widget widget, Element container) {

    }

    @Override
    public void detachWidget(Widget widget) {
    }

    @Override
    public void fireUpdate(VComponent thunk) {
    }
}
