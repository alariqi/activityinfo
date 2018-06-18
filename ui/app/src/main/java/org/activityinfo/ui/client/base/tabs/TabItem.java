package org.activityinfo.ui.client.base.tabs;

import org.activityinfo.ui.vdom.shared.tree.VTree;

public class TabItem {
    private String label;
    private VTree content;

    public TabItem(String label, VTree content) {
        this.label = label;
        this.content = content;
    }

    public String getLabel() {
        return label;
    }

    public VTree getContent() {
        return content;
    }
}
