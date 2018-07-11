package org.activityinfo.ui.client.table.state;

import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.ui.client.input.model.FormInputModel;
import org.activityinfo.ui.client.table.TablePlace;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class SliderState {

    private TablePlace place;

    private Map<ResourceId, TableState> tableMap;

    private Optional<FormInputModel> inputModel = Optional.empty();

    private SliderState() {
    }

    public SliderState(TablePlace place) {
        this.place = place;
        this.tableMap = new HashMap<>();
        this.tableMap.put(place.getFormId(), new TableState(place.getFormId()));
    }

    public TablePlace getPlace() {
        return place;
    }

    public TableState getTable(ResourceId formId) {
        TableState tableState = tableMap.get(formId);
        if(tableState == null) {
            return new TableState(formId);
        }
        return tableState;
    }

    public TableState getActiveTable() {
        return tableMap.get(place.getFormId());
    }

    public SliderState withNewPlace(TablePlace tablePlace) {

        if(this.place.equals(tablePlace)) {
            return this;
        }

        SliderState updated = new SliderState();
        updated.place = tablePlace;
        updated.tableMap = this.tableMap;
        updated.inputModel = Optional.empty();
        return updated;
    }


    public SliderState withTable(TableState tableState) {
        if(tableMap.get(tableState.getFormId()) == tableState) {
            return this;
        }
        Map<ResourceId, TableState> updatedTables = new HashMap<>(this.tableMap);
        updatedTables.put(tableState.getFormId(), tableState);

        SliderState updatedState = new SliderState();
        updatedState.place = this.place;
        updatedState.tableMap = updatedTables;
        return updatedState;
    }

    public SliderState updateTable(ResourceId formId, Function<TableState, TableState> function) {
        TableState oldState = getTable(formId);
        TableState updatedState = function.apply(oldState);
        return withTable(updatedState);
    }

    public SliderState withInput(Optional<FormInputModel> inputModel) {
        SliderState updatedState = new SliderState();
        updatedState.place = this.place;
        updatedState.tableMap = this.tableMap;
        updatedState.inputModel = inputModel;
        return updatedState;
    }

    public SliderState withInput(FormInputModel inputModel) {
        if(this.inputModel.isPresent() && this.inputModel.get() == inputModel) {
            return this;
        }
        return withInput(Optional.of(inputModel));
    }

    public Optional<FormInputModel> getInput() {
        return inputModel;
    }
}
