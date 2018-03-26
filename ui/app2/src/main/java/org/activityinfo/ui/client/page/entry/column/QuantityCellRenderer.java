package org.activityinfo.ui.client.page.entry.column;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import org.activityinfo.ui.client.dispatch.type.IndicatorNumberFormat;

class QuantityCellRenderer implements GridCellRenderer {
    @Override
    public SafeHtml render(ModelData model,
                           String property,
                           ColumnData config,
                           int rowIndex,
                           int colIndex,
                           ListStore listStore,
                           Grid grid) {
        Object value = model.get(property);
        if (value instanceof Double && (Double) value != 0) {
            return SafeHtmlUtils.fromTrustedString(
                    IndicatorNumberFormat.INSTANCE.format((Double) value));
        } else {
            return SafeHtmlUtils.EMPTY_SAFE_HTML;
        }
    }
}