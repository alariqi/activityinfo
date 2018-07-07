package org.activityinfo.ui.client;

import org.activityinfo.ui.client.header.ConnectionStatus;
import org.activityinfo.ui.client.header.Header2;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public class AppFrame {

    public static VTree render(FormStore formStore, VTree page) {
        return H.div("appframe appframe--fixed",
                Header2.render(formStore),
                ConnectionStatus.render(),
                page);
    }
}
