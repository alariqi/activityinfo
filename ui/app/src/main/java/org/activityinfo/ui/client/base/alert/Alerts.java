package org.activityinfo.ui.client.base.alert;

import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.VNode;

public class Alerts {

    public static VNode error(String message) {
        return H.div("alert alert--error", H.t(message));
    }
}
