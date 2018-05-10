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

import com.google.common.base.Optional;
import net.lightoze.gwt.i18n.server.LocaleProxy;
import org.activityinfo.analysis.table.*;
import org.activityinfo.json.Json;
import org.activityinfo.json.JsonValue;
import org.activityinfo.model.analysis.ImmutableTableColumn;
import org.activityinfo.model.analysis.ImmutableTableModel;
import org.activityinfo.model.analysis.TableColumn;
import org.activityinfo.model.analysis.TableModel;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formula.CompoundExpr;
import org.activityinfo.model.formula.SymbolNode;
import org.activityinfo.model.query.ColumnModel;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.observable.Connection;
import org.activityinfo.observable.Observable;
import org.activityinfo.promise.Promise;
import org.activityinfo.store.testing.IncidentForm;
import org.activityinfo.store.testing.LocaliteForm;
import org.activityinfo.store.testing.ReferralSubForm;
import org.activityinfo.ui.client.store.TestSetup;
import org.activityinfo.ui.client.table.view.DeleteRecordAction;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.activityinfo.observable.ObservableTesting.connect;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class TableViewModelTest {

    private TestSetup setup = new TestSetup();

    @Before
    public void setup() {
        LocaleProxy.initialize();
    }


    @Test
    public void test() {
        TableModel tableModel = ImmutableTableModel.builder()
                .formId(setup.getSurveyForm().getFormId())
                .build();

        TableViewModel viewModel = new TableViewModel(setup.getFormStore(), tableModel);

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

        viewModel.update(ImmutableTableModel.builder()
            .from(tableModel)
            .columns(Collections.singletonList(updatedColumn))
            .build());

        setup.runScheduled();


        // Should receive a change event...
        view.assertChanged();
        EffectiveTableModel updatedModel = view.assertLoaded();
        assertThat(updatedModel.getColumns().get(0).getLabel(), equalTo("MY column"));
    }

    @Test
    public void serializeModel() {

        ImmutableTableModel model = ImmutableTableModel.builder()
                .formId(ResourceId.valueOf("MY_FORM"))
                .addColumns(ImmutableTableColumn.builder().id("c1").label("Foo Squared").formula("foo*foo").build())
                .addColumns(ImmutableTableColumn.builder().id("c2").formula("foo").build())
                .build();

        JsonValue object = model.toJson();

        TableModel remodel = TableModel.fromJson(object);

        assertThat(remodel.getFormId(), equalTo(model.getFormId()));
        assertThat(remodel.getColumns(), hasSize(2));

        assertThat(remodel.getColumns().get(0), equalTo(model.getColumns().get(0)));
        assertThat(remodel.getColumns().get(1), equalTo(model.getColumns().get(1)));
    }


    @Test
    public void testDeletedSelection() {

        TableModel tableModel = ImmutableTableModel.builder()
                .formId(setup.getSurveyForm().getFormId())
                .build();

        TableViewModel viewModel = new TableViewModel(setup.getFormStore(), tableModel);

        Connection<Optional<SelectionViewModel>> selection = connect(viewModel.getSelectionViewModel());

        // Initially, we don't expect a selection
        assertThat(selection.assertLoaded().isPresent(), equalTo(false));


        // Ensure that when the selection is changed, the observable changes...

        selection.resetChangeCounter();

        RecordRef selectedRef = setup.getSurveyForm().getRecordRef(101);

        viewModel.select(selectedRef);
        selection.assertChanged();
        setup.runScheduled();
        assertThat(selection.assertLoaded().isPresent(), equalTo(true));
        assertThat(selection.assertLoaded().get().isEditAllowed(), equalTo(true));
        assertThat(selection.assertLoaded().get().isDeleteAllowed(), equalTo(true));


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

    @Test
    public void testDeletedForm() {

        TableModel tableModel = ImmutableTableModel.builder()
                .formId(setup.getSurveyForm().getFormId())
                .build();

        setup.deleteForm(setup.getSurveyForm().getFormId());

        TableViewModel model = new TableViewModel(setup.getFormStore(), tableModel);

        Connection<EffectiveTableModel> view = setup.connect(model.getEffectiveTable());

        EffectiveTableModel effectiveTableModel = view.assertLoaded();

        assertThat(effectiveTableModel.getFormTree().getRootState(), equalTo(FormTree.State.DELETED));

    }

    @Test
    public void testSubFormExport() {

        IncidentForm incidentForm = setup.getCatalog().getIncidentForm();

        TableModel tableModel = ImmutableTableModel.builder().formId(incidentForm.getFormId())
                .addColumns(ImmutableTableColumn.builder()
                        .label("My PCODE")
                        .formula(IncidentForm.PROTECTION_CODE_FIELD_ID.asString())
                        .build())
                .build();
        TableViewModel viewModel = new TableViewModel(setup.getFormStore(), tableModel);

        Observable<ExportViewModel> exportModel = viewModel.computeExportModel(
                Observable.just(ReferralSubForm.FORM_ID),
                Observable.just(ExportScope.SELECTED));
        Connection<TableModel> exportModelConnection = setup.connect(exportModel.transform(ExportViewModel::getTableModel));
        Connection<Boolean> columnLimitConnection = setup.connect(exportModel.transform(ExportViewModel::isColumnLimitExceeded));

        System.out.println(Json.stringify(exportModelConnection.assertLoaded().toJson(), 2));

        assertThat(exportModelConnection.assertLoaded().getFormId(), equalTo(ReferralSubForm.FORM_ID));
        assertThat(exportModelConnection.assertLoaded().getColumns(), hasSize(3));
        assertThat(columnLimitConnection.assertLoaded(), equalTo(false));

        TableColumn firstColumn = exportModelConnection.assertLoaded().getColumns().get(0);
        assertThat(firstColumn.getLabel(), equalTo(Optional.of("My PCODE")));
        assertThat(firstColumn.getFormula(), equalTo(
                new CompoundExpr(new SymbolNode(ColumnModel.PARENT_SYMBOL),
                        IncidentForm.PROTECTION_CODE_FIELD_ID).asExpression()));
    }

    @Test
    public void testSubFormExportWithFilter() {

        IncidentForm incidentForm = setup.getCatalog().getIncidentForm();

        TableModel tableModel = ImmutableTableModel.builder().formId(incidentForm.getFormId())
                .addColumns(ImmutableTableColumn.builder()
                        .label("My PCODE")
                        .formula(IncidentForm.PROTECTION_CODE_FIELD_ID.asString())
                        .build())
                .filter(constructFilter())
                .build();
        TableViewModel viewModel = new TableViewModel(setup.getFormStore(), tableModel);

        Connection<TableModel> exportModel = setup.connect(
                viewModel.computeExportModel(
                        Observable.just(ReferralSubForm.FORM_ID),
                        Observable.just(ExportScope.SELECTED),
                        Observable.just(ExportScope.SELECTED))
                        .transform(ExportViewModel::getTableModel));

        System.out.println(Json.stringify(exportModel.assertLoaded().toJson(), 2));

        assertThat(exportModel.assertLoaded().getFormId(), equalTo(ReferralSubForm.FORM_ID));
        assertThat(exportModel.assertLoaded().getColumns(), hasSize(3));
        assertThat(exportModel.assertLoaded().getFilter(), equalTo(Optional.of(constructFilter())));
    }

    private String constructFilter() {
        return new CompoundExpr(IncidentForm.URGENCY, IncidentForm.HIGH.asString()).asExpression();
    }

    @Test
    public void testClassicAdminHierarchy() {

        LocaliteForm localiteForm = setup.getCatalog().getLocaliteForm();


        TableModel tableModel = ImmutableTableModel.builder().formId(localiteForm.getFormId()).build();
        TableViewModel viewModel = new TableViewModel(setup.getFormStore(), tableModel);

        EffectiveTableModel effectiveTable = setup.connect(viewModel.getEffectiveTable()).assertLoaded();

        for (EffectiveTableColumn tableColumn : effectiveTable.getColumns()) {
            System.out.println(tableColumn.getLabel() + " => " + tableColumn.getFormula().getFormula());
        }

    }
}