package org.activityinfo.ui.client.page;

import com.google.gwt.safehtml.shared.SafeHtml;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public enum GenericAvatar implements Avatar {

    DATABASE,
    FORM;

    @Override
    public SafeHtml render() {
        return PageTemplates.TEMPLATES.genericAvatar("#type_" + name().toLowerCase());
    }

    @Override
    public VTree renderTree() {
        throw new UnsupportedOperationException("TODO");
    }
}
