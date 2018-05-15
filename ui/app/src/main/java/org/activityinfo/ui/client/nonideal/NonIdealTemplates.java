package org.activityinfo.ui.client.nonideal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;
import org.activityinfo.i18n.shared.UiConstants;

public interface NonIdealTemplates extends XTemplates {

    NonIdealTemplates TEMPLATES = GWT.create(NonIdealTemplates.class);

    @XTemplate(source = "NotFound.html")
    SafeHtml notFound(UiConstants i18n);

    @XTemplate(source = "PermissionDenied.html")
    SafeHtml permissionDenied();

    @XTemplate(source = "Loading.html")
    SafeHtml loading();

}
