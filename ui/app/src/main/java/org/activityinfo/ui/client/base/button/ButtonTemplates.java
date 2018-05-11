package org.activityinfo.ui.client.base.button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;

public interface ButtonTemplates extends XTemplates {

    ButtonTemplates TEMPLATES = GWT.create(ButtonTemplates.class);

    @XTemplate(source = "IconButton.html")
    SafeHtml iconButton(String iconHref, String label, String styleNames);


    @XTemplate(source = "IconLinkButton.html")
    SafeHtml iconLinkButton(String iconHref, String label);

    @XTemplate(source = "MenuButton.html")
    SafeHtml menuButton(String label);

    @XTemplate(source = "ButtonGroup.html")
    SafeHtml buttonGroup();
}
