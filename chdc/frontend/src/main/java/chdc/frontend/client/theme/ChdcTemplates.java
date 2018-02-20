package chdc.frontend.client.theme;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;

public interface ChdcTemplates extends XTemplates {

    ChdcTemplates TEMPLATES = GWT.create(ChdcTemplates.class);

    @XTemplate(source = "panel.html")
    SafeHtml panel(ChdcStyles style);

    @XTemplate(source = "Banner.html")
    SafeHtml banner(ChdcStyles style);

}
