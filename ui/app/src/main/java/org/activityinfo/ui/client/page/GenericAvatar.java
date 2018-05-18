package org.activityinfo.ui.client.page;

import com.google.gwt.safehtml.shared.SafeHtml;

public enum GenericAvatar implements Avatar {

    DATABASE,
    FORM;

    @Override
    public SafeHtml render() {
        return PageTemplates.TEMPLATES.genericAvatar("#type_" + name().toLowerCase());
    }
}