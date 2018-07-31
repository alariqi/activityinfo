package org.activityinfo.ui.client.base.modal;

import com.google.gwt.user.client.ui.RootPanel;
import org.activityinfo.ui.vdom.client.VDomWidget;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.VTree;

/**
 * A modal dialog that blocks the full application
 */
public class AppModal {

    private VDomWidget widget;

    public AppModal(VTree body) {
        this.widget = new VDomWidget();
        this.widget.update(render(body));
    }

    public void show() {
        if(!widget.isAttached()) {
            RootPanel.get().add(widget);
        }
    }

    public void hide() {
        if(widget.isAttached()) {
            widget.removeFromParent();
        }
    }

    public static VTree render(VTree body) {
        return H.div("appmodal", body);
    }
}
