package chdc.frontend.client.table;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.query.ColumnModel;
import org.activityinfo.ui.client.table.view.ColumnSetProxy;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;

/**
 * Time:
 * Time will be one cell which includes both a textfield with a datepicker dropdown and a
 * second smaller textfield where the time can be entered in 24 hour format. The time
 * textfield will round to the nearest hour and validate the datetime automatically.
 */
public class TimeColumn implements IncidentColumn {

    private static final DateTimeFormat FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm");

    private final ColumnConfig<Integer, Date> config;
    private final DateField field;

    public TimeColumn(ColumnSetProxy proxy) {
        config = new ColumnConfig<>(proxy.getDateProvider("date"));
        config.setHeader(I18N.CONSTANTS.time());
        config.setWidth(200);
        config.setCell(new DateCell(FORMAT));

        field = new DateField(new DateTimePropertyEditor(FORMAT));
    }

    @Override
    public List<ColumnModel> getColumnsToQuery() {
        return singletonList(new ColumnModel("date"));
    }

    @Override
    public ColumnConfig<Integer, ?> getColumnConfig() {
        return config;
    }

    @Override
    public Optional<? extends IsField<?>> getEditor() {
        return Optional.of(field);
    }
}
