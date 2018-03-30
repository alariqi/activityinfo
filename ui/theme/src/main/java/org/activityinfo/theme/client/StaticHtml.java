package org.activityinfo.theme.client;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Widget;

public class StaticHtml extends Widget {

    public StaticHtml(SafeHtml html) {
        DivElement div = Document.get().createDivElement();
        div.setInnerSafeHtml(html);
        assert div.getChildCount() == 1 : "supplied html should have single root <element>";

        setElement(div.getFirstChildElement());
    }
}
