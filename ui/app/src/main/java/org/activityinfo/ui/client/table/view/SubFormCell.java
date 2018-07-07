package org.activityinfo.ui.client.table.view;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.ValueProvider;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.table.TablePlace;

public class SubFormCell extends AbstractCell<Integer> {

    private final ResourceId subFormId;
    private final ValueProvider<Integer, String> idProvider;
    private final ValueProvider<Integer, Integer> countProvider;
    private SafeHtml iconHtml;

    public SubFormCell(ResourceId subFormId,
                       ValueProvider<Integer, String> idProvider,
                       ValueProvider<Integer, Integer> countProvider) {
        super(BrowserEvents.CLICK);
        this.subFormId = subFormId;
        this.idProvider = idProvider;
        this.countProvider = countProvider;
        iconHtml = Icon.BUBBLE_ARROWRIGHT.render(16, 16);
    }

    @Override
    public void render(Context context, Integer rowIndex, SafeHtmlBuilder html) {

        String recordId = idProvider.getValue(rowIndex);
        Integer count = countProvider.getValue(rowIndex);

        TablePlace tablePlace = new TablePlace(subFormId, recordId);

        html.appendEscaped(I18N.MESSAGES.recordCount(count));
        html.appendHtmlConstant("<a href=\"#" + tablePlace.toString() + "\">");
        html.append(iconHtml);
        html.appendHtmlConstant("</a>");
    }

    @Override
    public boolean handlesSelection() {
        return true;
    }
}
