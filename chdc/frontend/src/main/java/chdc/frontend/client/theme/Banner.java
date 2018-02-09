package chdc.frontend.client.theme;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class Banner extends Widget {

    public Banner() {
        Element header = Document.get().createElement("header");
        header.setAttribute("role", "banner");
        header.addClassName(ChdcResources.INSTANCE.styles().banner());
        header.setInnerSafeHtml(ChdcTemplates.TEMPLATES.banner(ChdcResources.INSTANCE.styles()));
        setElement(header);
    }
}
