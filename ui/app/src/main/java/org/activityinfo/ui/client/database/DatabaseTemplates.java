package org.activityinfo.ui.client.database;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;
import org.activityinfo.i18n.shared.UiConstants;

public interface DatabaseTemplates extends XTemplates {

    DatabaseTemplates TEMPLATES = GWT.create(DatabaseTemplates.class);

    @XTemplate(source = "DatabasePage.html")
    SafeHtml page(UiConstants i18n);

    @XTemplate(source = "SettingsLink.html")
    SafeHtml settingsLink(UiConstants i18n);

}
