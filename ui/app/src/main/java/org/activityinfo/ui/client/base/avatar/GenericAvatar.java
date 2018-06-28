package org.activityinfo.ui.client.base.avatar;

import com.google.gwt.safehtml.shared.SafeHtml;
import org.activityinfo.ui.client.base.NonIdeal;
import org.activityinfo.ui.client.page.PageTemplates;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public enum GenericAvatar implements Avatar {

    DATABASE,
    FORM,
    FOLDER;

    @Override
    public SafeHtml render() {
        return PageTemplates.TEMPLATES.genericAvatar("#type_" + name().toLowerCase());
    }

    @Override
    public VTree renderTree() {
        return NonIdeal.svg("avatar", "#type_" + name().toLowerCase(), "0 0 18 27");
    }

    public String href() {
        return "#type_" + name().toLowerCase();
    }
}
