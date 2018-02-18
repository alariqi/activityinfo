package chdc.frontend.client.theme;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class Banner extends Widget {

    public Banner() {
        Element header = Document.get().createElement("header");
        header.setAttribute("role", "banner");
        header.addClassName(ChdcTheme.INSTANCE.style().banner());
        header.setInnerSafeHtml(ChdcTemplates.TEMPLATES.banner(ChdcTheme.INSTANCE.style()));
        setElement(header);
    }
}
