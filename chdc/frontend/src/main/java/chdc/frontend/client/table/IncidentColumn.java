package chdc.frontend.client.table;

import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import org.activityinfo.model.query.ColumnModel;

import java.util.List;
import java.util.Optional;

/**
 * Defines a common interface to incident columns
 */
public interface IncidentColumn {

    /**
     *
     * @return a list of columns that are needed from the server to
     * display this column.
     */
    List<ColumnModel> getColumnsToQuery();

    /**
     * @return the GXT {@link ColumnConfig} that defines how the column is displayed
     * in the GridView.
     */
    ColumnConfig<Integer, ?> getColumnConfig();

    /**
     * @return the {@link Field} that provides the field to edit the value.
     */
    Optional<? extends IsField<?>> getEditor();


}
