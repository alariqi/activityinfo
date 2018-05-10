package org.activityinfo.ui.client.database;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;

public interface DatabaseTemplates extends XTemplates {

    DatabaseTemplates TEMPLATES = GWT.create(DatabaseTemplates.class);

    @XTemplate("<h2>{heading}</h2>")
    SafeHtml header(String heading);

}
