package org.activityinfo.ui.client.input.view.field;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;

public interface AttachmentTemplates extends XTemplates {

    @XTemplate()
    SafeHtml upload(String name, String ext);
}
