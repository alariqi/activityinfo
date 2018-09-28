package org.activityinfo.ui.client.table;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.analysis.ImmutableTableAnalysisModel;
import org.activityinfo.model.analysis.ImmutableTableColumn;
import org.activityinfo.model.analysis.TableColumn;
import org.activityinfo.model.job.ExportFormJob;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.StatefulValue;
import org.activityinfo.promise.Maybe;
import org.activityinfo.ui.client.AppFrame;
import org.activityinfo.ui.client.Page;
import org.activityinfo.ui.client.Place;
import org.activityinfo.ui.client.fields.state.DropTarget;
import org.activityinfo.ui.client.fields.state.FieldChoiceState;
import org.activityinfo.ui.client.fields.state.FieldChoiceUpdater;
import org.activityinfo.ui.client.input.model.FieldInput;
import org.activityinfo.ui.client.input.model.FormInputModel;
import org.activityinfo.ui.client.input.view.InputHandler;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.client.table.state.SliderState;
import org.activityinfo.ui.client.table.state.SliderUpdater;
import org.activityinfo.ui.client.table.state.TableState;
import org.activityinfo.ui.client.table.state.TableUpdater;
import org.activityinfo.ui.client.table.view.TableView;
import org.activityinfo.ui.client.table.viewModel.TableSliderViewModel;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TablePage extends Page implements SliderUpdater {

    private final FormStore formStore;

    private StatefulValue<SliderState> state;
    private Observable<Maybe<TableSliderViewModel>> viewModel;

    public TablePage(FormStore formStore, TablePlace tablePlace) {
        this.formStore = formStore;
        this.state = new StatefulValue<>(new SliderState(tablePlace));
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
    public String mayStop() {
        if(state.get().getInput().isPresent()) {
            FormInputModel inputModel = state.get().getInput().get();
            if(!inputModel.isEmpty()) {
                return I18N.CONSTANTS.unsavedChangesWarning();
            }
        }
        return null;
    }

    @Override
    public VTree render() {
        return AppFrame.render(formStore, TableView.render(formStore, viewModel, this));
    }

    @Override
    public void update(Function<SliderState, SliderState> function) {
        state.update(function);
    }

    @Override
    public TableUpdater getTableUpdater(ResourceId formId) {
        return new TableUpdater() {

            @Override
            public void update(Function<TableState, TableState> function) {
                state.update(s -> s.updateTable(formId, function));
            }

            @Override
            public void newRecord() {

                ResourceId newRecordId = ResourceId.generateSubmissionId(formId);
                Optional<String> parentId = state.get().getPlace().getParentId();

                FormInputModel inputModel = new FormInputModel(new RecordRef(formId, newRecordId), parentId);

                state.update(s -> s.withInput(inputModel));

            }

            @Override
            public void editSelection() {
                TableState tableState = state.get().getTable(formId);
                tableState.getSelected().ifPresent(selectedRef -> {
                    FormInputModel inputModel = new FormInputModel(selectedRef);

                    state.updateIfNotSame(state.get().withInput(inputModel));
                });
            }

            @Override
            public FieldChoiceUpdater fieldChoiceUpdater() {
                return new FieldChoiceUpdater() {
                    @Override
                    public void update(Function<FieldChoiceState, FieldChoiceState> function) {
                        state.update(slider -> slider.updateTable(formId, t -> t.updateColumnOptions(function)));
                    }

                    @Override
                    public void drop(DropTarget dropTarget) {
                        dropColumn(dropTarget);
                    }
                };
            }

            @Override
            public void startExport() {
                formStore.startJob(new ExportFormJob(state.get().getActiveTable().getAnalysisModel()));
            }

            private void dropColumn(DropTarget dropTarget) {
                FieldChoiceState state = TablePage.this.state.get().getTable(formId).getColumnOptions();
                viewModel.get().getIfVisible().toJavaUtil().ifPresent(sliderViewModel -> {
                    sliderViewModel.getTableViewModel(formId).ifPresent(tableViewModel -> {
                        tableViewModel.getEffectiveTable().ifLoaded(table -> {
                            List<TableColumn> existingColumns = table.getColumns()
                                    .stream()
                                    .map(c -> c.getModel())
                                    .collect(Collectors.toList());

                            if(state.getDraggingFieldId().isPresent()) {
                                existingColumns.add(dropTarget.getIndex(), ImmutableTableColumn.builder().formula(state.getDraggingFieldId().get()).build());
                            }

                            ImmutableTableAnalysisModel updatedModel = ImmutableTableAnalysisModel.builder()
                                    .from(table.getModel())
                                    .columns(existingColumns)
                                    .build();

                            update(s -> s.withAnalysisModel(updatedModel));
                        });
                    });
                });
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
