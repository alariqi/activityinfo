package org.activityinfo.ui.client.base.cardlist;

import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public class CardGroup {
    private String header;
    private VTree content;

    public void setHeader(String header) {
        this.header = header;
    }

    public void setContent(VTree content) {
        this.content = content;
    }

    public VTree build() {
        return H.div("cardgroup",
                H.h3(header),
                content);
    }
}
