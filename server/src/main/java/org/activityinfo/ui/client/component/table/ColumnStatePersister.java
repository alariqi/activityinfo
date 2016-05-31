package org.activityinfo.ui.client.component.table;

import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.google.common.collect.Sets;
import org.activityinfo.legacy.client.state.StateProvider;
import org.activityinfo.model.resource.Record;
import org.activityinfo.model.resource.Resources;

import java.util.List;
import java.util.Set;

/**
 * Created by yuriyz on 5/30/2016.
 */
public class ColumnStatePersister {

    private static final String INSTANCE_TABLE_COLUMNS_STATE = "instanceTableColumnState";

    private StateProvider stateProvider;

    public ColumnStatePersister(StateProvider stateProvider) {
        this.stateProvider = stateProvider;
    }

    public void persist(List<FieldColumn> columns) {
        final Set<String> columnNames = Sets.newHashSet();
        for (FieldColumn column : columns) {
            columnNames.add(column.getHeader());
        }

        persist(columnNames);
    }

    public void persist(Set<String> columnNames) {
        if (columnNames.isEmpty()) {
            return;
        }

        stateProvider.set(INSTANCE_TABLE_COLUMNS_STATE, Resources.toJsonObject(asRecord(columnNames)).toString());
    }

    public Set<String> getColumnNames() {
        Set<String> columns = Sets.newHashSet();
        String json = stateProvider.getString(INSTANCE_TABLE_COLUMNS_STATE);
        if (!Strings.isNullOrEmpty(json)) {
            Record record = Resources.recordFromJson(json);
            columns.addAll(record.getProperties().keySet());
        }
        return columns;
    }

    private Record asRecord(Set<String> columnNames) {
        Record record = new Record();
        for (String columnName : columnNames) {
            record.set(columnName, columnName);
        }
        return record;
    }
}
