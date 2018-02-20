package chdc.frontend.client.table;

import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;

import java.util.Optional;

/**
 * Defines a common interface to incident columns
 */
public interface IncidentColumn {

    ColumnConfig<Integer, ?> getColumnConfig();

    Optional<? extends Field<?>> getEditor();
}
