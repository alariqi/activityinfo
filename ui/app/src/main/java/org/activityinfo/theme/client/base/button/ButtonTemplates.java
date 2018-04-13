package org.activityinfo.theme.client.base.button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;

public interface ButtonTemplates extends XTemplates {

    ButtonTemplates TEMPLATES = GWT.create(ButtonTemplates.class);

    @XTemplate(source = "IconButton.html")
    SafeHtml iconButton(String iconHref, String label, String styleNames);

    @XTemplate(source = "MenuButton.html")
    SafeHtml menuButton(String label);

}
