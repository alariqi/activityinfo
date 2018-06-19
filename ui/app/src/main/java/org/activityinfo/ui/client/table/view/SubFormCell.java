package org.activityinfo.ui.client.table.view;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.ui.client.Icon;

public class SubFormCell extends AbstractCell<Integer> {

    private SafeHtml iconHtml;

    public SubFormCell() {
        super();
        iconHtml = Icon.BUBBLE_ARROWRIGHT.render(16, 16);
    }

    @Override
    public void render(Context context, Integer count, SafeHtmlBuilder html) {
        html.appendEscaped(I18N.MESSAGES.recordCount(count));
        html.appendHtmlConstant("<a href=\"#\">");
        html.append(iconHtml);
        html.appendHtmlConstant("</a>");
    }

    @Override
    public boolean handlesSelection() {
        return true;
    }
}
