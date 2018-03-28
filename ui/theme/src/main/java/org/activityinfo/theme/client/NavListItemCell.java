package org.activityinfo.ui.client.databases;


import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;

import java.util.Set;

public class NavListItemCell extends AbstractSafeHtmlCell<> {
    public NavListItemCell(SafeHtmlRenderer renderer, Set consumedEvents) {
        super(renderer, consumedEvents);
    }

    @Override
    protected void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
        throw new UnsupportedOperationException("TODO");
    }
}
