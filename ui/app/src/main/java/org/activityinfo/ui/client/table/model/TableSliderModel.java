package org.activityinfo.ui.client.table.model;

import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.ui.client.table.TablePlace;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TableSliderModel {

    private TablePlace place;

    private Map<ResourceId, TableModel> tableMap;

    private TableSliderModel() {
    }

    public TableSliderModel(TablePlace place) {
        this.place = place;
        this.tableMap = new HashMap<>();
        this.tableMap.put(place.getFormId(), new TableModel(place.getFormId()));
    }

    public TablePlace getPlace() {
        return place;
    }

    public Collection<TableModel> getTables() {
        return tableMap.values();
    }

    public TableModel getTable(ResourceId formId) {
        return tableMap.get(formId);
    }

    public TableSliderModel withNewPlace(TablePlace tablePlace) {

        if(this.place.equals(tablePlace)) {
            return this;
        }

        Map<ResourceId, TableModel> updatedTables = new HashMap<>();

        // If we are navigating to a new table within this tree,
        // then add it to the model

        if(!tableMap.containsKey(tablePlace.getFormId())) {
            updatedTables.put(tablePlace.getFormId(), new TableModel(tablePlace.getFormId()));
        }

        // Update the other tables to close any open data entry
        // forms

        for (TableModel tableModel : tableMap.values()) {
            updatedTables.put(tableModel.getFormId(), tableModel.withoutEditing());
        }

        TableSliderModel model = new TableSliderModel();
        model.place = tablePlace;
        model.tableMap = updatedTables;

        return model;
    }

    public TableSliderModel withTableModel(TableModel tableModel) {
        if(tableMap.get(tableModel.getFormId()) == tableModel) {
            return this;
        }
        Map<ResourceId, TableModel> updatedTables = new HashMap<>();
        updatedTables.put(tableModel.getFormId(), tableModel);

        TableSliderModel model = new TableSliderModel();
        model.place = this.place;
        model.tableMap = updatedTables;
        return model;
    }
}
