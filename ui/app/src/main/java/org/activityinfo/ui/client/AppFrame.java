package org.activityinfo.ui.client;

import org.activityinfo.ui.client.header.Header;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public class AppFrame {

    public static VTree render(FormStore formStore, VTree page) {
        return H.div("appframe appframe--fixed",
                Header.render(formStore),
                connectionStatus(),
                page);
    }

    public static VTree connectionStatus() {
        return H.div("connectionstatus",
                H.div("connectionstatus__inner", new VNode(HtmlTag.DIV)));
    }
}
