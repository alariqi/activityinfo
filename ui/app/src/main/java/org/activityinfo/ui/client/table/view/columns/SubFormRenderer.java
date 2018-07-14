package org.activityinfo.ui.client.table.view.columns;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.ui.client.base.Svg;
import org.activityinfo.ui.client.table.TablePlace;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

public class SubFormRenderer implements ColumnRenderer {

    private final ResourceId subFormId;
    private final String idColumnId;
    private final String countColumnId;
    private final VTree icon;
    private final PropMap cellProps;

    private ColumnView idColumn;
    private ColumnView countColumn;

    private boolean loaded;

    public SubFormRenderer(ResourceId subFormId, String idColumnId, String countColumnId) {
        this.subFormId = subFormId;
        this.idColumnId = idColumnId;
        this.countColumnId = countColumnId;
        this.icon = Svg.svg("icon", "#bubble_arrowright", "0 0 16 16");
        this.cellProps = Props.withClass("subform");
    }

    @Override
    public void init(ColumnSet columnSet) {
        idColumn = columnSet.getColumnView(idColumnId);
        countColumn = columnSet.getColumnView(countColumnId);
        loaded = (idColumn != null && countColumn != null);
    }

    @Override
    public VTree renderCell(int row) {

        if(!loaded) {
            return EMPTY_CELL;
        }

        String recordId = idColumn.getString(row);
        int count = (int) countColumn.getDouble(row);

        TablePlace tablePlace = new TablePlace(subFormId, recordId);
        String href = "#" + tablePlace.toString();

        String label = I18N.MESSAGES.recordCount(count);

        return new VNode(HtmlTag.TD, cellProps, new VNode(HtmlTag.A, Props.create().set("href", href),
                new VText(label)));
    }
}
