package org.activityinfo.ui.client.base.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;

public interface MenuTemplates extends XTemplates {

    MenuTemplates TEMPLATES = GWT.create(MenuTemplates.class);

    @XTemplate(source = "MenuItem.html")
    SafeHtml menuItem();

    @XTemplate(source = "CheckMenuItem.html")
    SafeHtml checkMenuItem();
}
