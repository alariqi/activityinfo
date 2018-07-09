package org.activityinfo.ui.client.table.model;

import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.ui.client.input.model.FormInputModel;
import org.activityinfo.ui.client.table.TablePlace;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TableSliderModel {

    private TablePlace place;

    private Map<ResourceId, TableModel> tableMap;

    private Optional<FormInputModel> inputModel = Optional.empty();

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

    public TableModel getTable(ResourceId formId) {
        TableModel tableModel = tableMap.get(formId);
        if(tableModel == null) {
            return new TableModel(formId);
        }
        return tableModel;
    }

    public TableModel getActiveTable() {
        return tableMap.get(place.getFormId());
    }

    public TableSliderModel withNewPlace(TablePlace tablePlace) {

        if(this.place.equals(tablePlace)) {
            return this;
        }

        TableSliderModel model = new TableSliderModel();
        model.place = tablePlace;
        model.tableMap = this.tableMap;
        model.inputModel = Optional.empty();
        return model;
    }

    public TableSliderModel withTableModel(TableModel tableModel) {
        if(tableMap.get(tableModel.getFormId()) == tableModel) {
            return this;
        }
        Map<ResourceId, TableModel> updatedTables = new HashMap<>(this.tableMap);
        updatedTables.put(tableModel.getFormId(), tableModel);

        TableSliderModel model = new TableSliderModel();
        model.place = this.place;
        model.tableMap = updatedTables;
        return model;
    }

    public TableSliderModel withInput(Optional<FormInputModel> inputModel) {
        TableSliderModel model = new TableSliderModel();
        model.place = this.place;
        model.tableMap = this.tableMap;
        model.inputModel = inputModel;
        return model;
    }

    public TableSliderModel withInput(FormInputModel inputModel) {
        if(this.inputModel.isPresent() && this.inputModel.get() == inputModel) {
            return this;
        }
        return withInput(Optional.of(inputModel));
    }

    public Optional<FormInputModel> getInput() {
        return inputModel;
    }
}
