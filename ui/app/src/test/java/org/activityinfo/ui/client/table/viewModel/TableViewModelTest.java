/*
 * ActivityInfo
 * Copyright (C) 2009-2013 UNICEF
 * Copyright (C) 2014-2018 BeDataDriven Groep B.V.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.activityinfo.ui.client.table.viewModel;

import net.lightoze.gwt.i18n.server.LocaleProxy;
import org.activityinfo.analysis.table.EffectiveTableColumn;
import org.activityinfo.analysis.table.EffectiveTableModel;
import org.activityinfo.json.JsonValue;
import org.activityinfo.model.analysis.ImmutableTableAnalysisModel;
import org.activityinfo.model.analysis.ImmutableTableColumn;
import org.activityinfo.model.analysis.TableAnalysisModel;
import org.activityinfo.model.analysis.TableColumn;
import org.activityinfo.model.formTree.RecordTree;
import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.observable.Connection;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.StatefulValue;
import org.activityinfo.promise.Maybe;
import org.activityinfo.promise.Promise;
import org.activityinfo.store.testing.IncidentForm;
import org.activityinfo.ui.client.store.TestSetup;
import org.activityinfo.ui.client.table.TablePlace;
import org.activityinfo.ui.client.table.state.SliderState;
import org.activityinfo.ui.client.table.state.TableState;
import org.activityinfo.ui.client.table.view.DeleteRecordAction;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Optional;

import static org.activityinfo.observable.ObservableTesting.connect;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

public class TableViewModelTest {

    private TestSetup setup = new TestSetup();

    @Before
    public void setup() {
        LocaleProxy.initialize();
    }


    @Test
    public void test() {
        StatefulValue<TableState> tableModel =
                new StatefulValue<>(new TableState(setup.getSurveyForm().getFormId()));

        TableViewModel viewModel = viewModel(setup.getSurveyForm().getFormId(), tableModel);

        Connection<EffectiveTableModel> view = setup.connect(viewModel.getEffectiveTable());

        EffectiveTableModel effectiveTableModel = view.assertLoaded();

        EffectiveTableColumn nameColumn = effectiveTableModel.getColumns().get(0);
        assertThat(nameColumn.getLabel(), equalTo("Respondent Name"));

        // Now verify that we can update the column label

        view.resetChangeCounter();

        TableColumn updatedColumn = ImmutableTableColumn.builder()
            .from(nameColumn.getModel())
            .label("MY column")
            .build();

        tableModel.updateValue(
                tableModel.get().withAnalysisModel(
                ImmutableTableAnalysisModel.builder()
                    .from(tableModel.get().getAnalysisModel())
                    .columns(Collections.singletonList(updatedColumn))
                    .build()));

        setup.runScheduled();


        // Should receive a change event...
        view.assertChanged();
        EffectiveTableModel updatedModel = view.assertLoaded();
        assertThat(updatedModel.getColumns().get(0).getLabel(), equalTo("MY column"));
    }

    @Test
    public void serializeModel() {

        ImmutableTableAnalysisModel model = ImmutableTableAnalysisModel.builder()
                .formId(ResourceId.valueOf("MY_FORM"))
                .addColumns(ImmutableTableColumn.builder().id("c1").label("Foo Squared").formula("foo*foo").build())
                .addColumns(ImmutableTableColumn.builder().id("c2").formula("foo").build())
                .build();

        JsonValue object = model.toJson();

        TableAnalysisModel remodel = TableAnalysisModel.fromJson(object);

        assertThat(remodel.getFormId(), equalTo(model.getFormId()));
        assertThat(remodel.getColumns(), hasSize(2));

        assertThat(remodel.getColumns().get(0), equalTo(model.getColumns().get(0)));
        assertThat(remodel.getColumns().get(1), equalTo(model.getColumns().get(1)));
    }


    @Test
    public void testDeletedSelection() {

        ResourceId formId = setup.getSurveyForm().getFormId();
        StatefulValue<TableState> tableModel = new StatefulValue<>(new TableState(formId));

        TableViewModel viewModel = viewModel(formId, tableModel);

        Connection<Optional<RecordRef>> selection = connect(viewModel.getSelectedRecordRef());
        Connection<Optional<RecordTree>> selectionTree = connect(viewModel.getSelectedRecordTree());



        setup.runScheduled();

        // Initially, we don't expect a selection
        assertThat(selection.assertLoaded().isPresent(), equalTo(false));
        assertThat(selectionTree.assertLoaded().isPresent(), equalTo(false));


        // Ensure that when the selection is changed, the observable changes...

        selection.resetChangeCounter();

        RecordRef selectedRef = setup.getSurveyForm().getRecordRef(101);

        tableModel.updateIfNotSame(tableModel.get().withSelection(selectedRef));
        selection.assertChanged();
        setup.runScheduled();
        assertThat(selection.assertLoaded().isPresent(), equalTo(true));
        assertThat(selectionTree.assertLoaded().isPresent(), equalTo(true));
//        assertThat(selection.assertLoaded().get().isEditAllowed(), equalTo(true));
//        assertThat(selection.assertLoaded().get().isDeleteAllowed(), equalTo(true));


        // Now delete the selected record...

        selection.resetChangeCounter();

        DeleteRecordAction action = new DeleteRecordAction(setup.getFormStore(), "", selectedRef);
        Promise<Void> deleted = action.execute();

        setup.runScheduled();

        assertThat(deleted.getState(), equalTo(Promise.State.FULFILLED));

        // And verify that the selection is changed to empty

        selection.assertChanged();

        assertThat(selection.assertLoaded().isPresent(), equalTo(false));


    }

    private TableViewModel viewModel(ResourceId formId, StatefulValue<TableState> tableModel) {
        return new TableViewModel(setup.getFormStore(),
                new SliderTree(setup.getFormTree(formId)),
                formId,
                tableModel,
                Observable.just(new TablePlace(formId)));
    }

    @Test
    public void testDeletedForm() {

        StatefulValue<SliderState> tableModel = new StatefulValue<>(new SliderState(new TablePlace(setup.getSurveyForm().getFormId())));

        setup.deleteForm(setup.getSurveyForm().getFormId());

        Observable<Maybe<TableSliderViewModel>> viewModel = TableSliderViewModel.compute(setup.getFormStore(), tableModel);

        Connection<Maybe<TableSliderViewModel>> view = setup.connect(viewModel);

        assertThat(view.assertLoaded().getState(), equalTo(Maybe.State.DELETED));

    }

    @Test
    public void testSubFormColumn() {

        IncidentForm incidentForm = setup.getCatalog().getIncidentForm();

        StatefulValue<TableState> tableModel = new StatefulValue<>(
                new TableState(incidentForm.getFormId())
                .withAnalysisModel(ImmutableTableAnalysisModel.builder()
                        .formId(incidentForm.getFormId())
                        .addColumns(ImmutableTableColumn.builder()
                                .id("pcode")
                                .label("My PCODE")
                                .formula(IncidentForm.PROTECTION_CODE_FIELD_ID.asString())
                                .build())
                        .addColumns(ImmutableTableColumn.builder()
                                .id("subform")
                                .label("Referrals")
                                .formula(IncidentForm.REFERRAL_FIELD_ID.asString())
                                .build())
                        .build()));

        TableViewModel viewModel = viewModel(incidentForm.getFormId(), tableModel);

        Connection<ColumnSet> columnsViewModel = setup.connect(viewModel.getColumnSet());

        ColumnSet columnSet = columnsViewModel.assertLoaded();

        assertThat(columnSet.getColumnView("pcode").getString(0), equalTo("c899"));
        assertThat(columnSet.getColumnView("subform").getDouble(0), equalTo(4.0));

    }

}