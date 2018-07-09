package org.activityinfo.ui.client.table;

import com.google.common.base.Optional;
import org.activityinfo.model.formula.FormulaNode;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.StatefulValue;
import org.activityinfo.promise.Maybe;
import org.activityinfo.ui.client.AppFrame;
import org.activityinfo.ui.client.Page;
import org.activityinfo.ui.client.Place;
import org.activityinfo.ui.client.input.model.FieldInput;
import org.activityinfo.ui.client.input.model.FormInputModel;
import org.activityinfo.ui.client.input.view.InputHandler;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.client.table.model.SliderUpdater;
import org.activityinfo.ui.client.table.model.TableModel;
import org.activityinfo.ui.client.table.model.TableSliderModel;
import org.activityinfo.ui.client.table.model.TableUpdater;
import org.activityinfo.ui.client.table.view.TableView;
import org.activityinfo.ui.client.table.viewModel.TableSliderViewModel;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.function.Function;

public class TablePage extends Page implements SliderUpdater {

    private final FormStore formStore;

    private StatefulValue<TableSliderModel> state;
    private Observable<Maybe<TableSliderViewModel>> viewModel;

    public TablePage(FormStore formStore, TablePlace tablePlace) {
        this.formStore = formStore;
        this.state = new StatefulValue<>(new TableSliderModel(tablePlace));
        this.viewModel = TableSliderViewModel.compute(formStore, state);
    }

    @Override
    public boolean tryNavigate(Place place) {
        if(place instanceof TablePlace) {
            TablePlace tablePlace = (TablePlace) place;
            state.updateValue(state.get().withNewPlace(tablePlace));

            return true;

        } else {
            return false;
        }
    }

    @Override
    public VTree render() {
        return AppFrame.render(formStore, TableView.render(formStore, viewModel, this));
    }

    @Override
    public TableUpdater getTableUpdater(ResourceId formId) {
        return new TableUpdater() {

            private void updateTable(TableModel tableModel) {
                TableSliderModel model = TablePage.this.state.get();
                TableSliderModel updatedModel = model.withTableModel(tableModel);
                TablePage.this.state.updateIfNotSame(updatedModel);
            }

            @Override
            public void updateFilter(Optional<FormulaNode> filterFormula) {
                throw new UnsupportedOperationException("TODO");
            }

            @Override
            public void updateColumnWidth(String columnId, int width) {
            }

            @Override
            public void newRecord() {
                ResourceId newRecordId = ResourceId.generateSubmissionId(formId);
                FormInputModel inputModel = new FormInputModel(new RecordRef(formId, newRecordId));

                state.updateIfNotSame(state.get().withInput(inputModel));
            }

            @Override
            public void editSelection() {

                TableModel tableModel = state.get().getTable(formId);
                tableModel.getSelected().ifPresent(selectedRef -> {
                    FormInputModel inputModel = new FormInputModel(selectedRef);

                    state.updateIfNotSame(state.get().withInput(inputModel));
                });
            }

            @Override
            public void stopEditing() {
                state.updateIfNotSame(state.get().withInput(java.util.Optional.empty()));
            }

            @Override
            public void selectRecord(RecordRef selectedRef) {
                updateTable(state.get().getTable(formId).withSelection(selectedRef));
            }

            @Override
            public void expandRecordPanel(boolean expanded) {
                updateTable(state.get().getTable(formId).withRecordPanelExpanded(expanded));
            }
        };
    }

    @Override
    public InputHandler getInputHandler() {

        return new InputHandler() {

            private void update(Function<FormInputModel, FormInputModel> mutator) {
                state.get().getInput().ifPresent(inputModel -> {
                    FormInputModel updatedInput = mutator.apply(inputModel);
                    if(updatedInput != inputModel) {
                        state.updateIfNotSame(state.get().withInput(updatedInput));
                    }
                });
            }

            @Override
            public void updateField(RecordRef record, ResourceId fieldId, FieldInput value) {
                update(input -> input.update(fieldId, value));
            }

            @Override
            public void touchField(RecordRef recordRef, ResourceId fieldId) {
                update(input -> input.touch(fieldId));
            }

            @Override
            public void requestValidation() {
                update(input -> input.validationRequested());
            }

            @Override
            public void cancelEditing() {
                state.updateIfNotSame(state.get().withInput(java.util.Optional.empty()));
            }

            @Override
            public void savedRecord(RecordRef recordRef) {
                state.updateIfNotSame(state.get().withInput(java.util.Optional.empty()));
            }
        };
    }
}
