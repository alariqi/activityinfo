package org.activityinfo.ui.client.header;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;

interface LogoTemplate extends XTemplates {

    @XTemplate(source = "Logo.html")
    SafeHtml logo();
}
