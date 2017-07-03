package org.activityinfo.ui.client.table.viewModel;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import net.lightoze.gwt.i18n.server.LocaleProxy;
import org.activityinfo.analysis.table.EffectiveTableColumn;
import org.activityinfo.analysis.table.EffectiveTableModel;
import org.activityinfo.analysis.table.TableViewModel;
import org.activityinfo.model.analysis.ImmutableTableModel;
import org.activityinfo.model.analysis.TableModel;
import org.activityinfo.model.form.FormInstance;
import org.activityinfo.model.form.FormRecord;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.RecordTree;
import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.observable.Connection;
import org.activityinfo.observable.Observable;
import org.activityinfo.promise.Maybe;
import org.activityinfo.promise.Promise;
import org.activityinfo.store.testing.IncidentForm;
import org.activityinfo.store.testing.ReferralSubForm;
import org.activityinfo.store.testing.Survey;
import org.activityinfo.ui.client.store.TestSetup;
import org.activityinfo.ui.client.table.view.DeleteRecordAction;
import org.junit.Before;
import org.junit.Test;

import static org.activityinfo.observable.ObservableTesting.connect;
import static org.hamcrest.CoreMatchers.equalTo;
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

        TableViewModel model = new TableViewModel(setup.getFormStore(), tableModel);

        Connection<EffectiveTableModel> view = setup.connect(model.getEffectiveTable());

        EffectiveTableModel effectiveTableModel = view.assertLoaded();

        EffectiveTableColumn nameColumn = effectiveTableModel.getColumns().get(0);
        assertThat(nameColumn.getLabel(), equalTo("Respondent Name"));
    }

    @Test
    public void testDeletedSelection() {

        TableModel tableModel = ImmutableTableModel.builder()
                .formId(setup.getSurveyForm().getFormId())
                .build();

        TableViewModel viewModel = new TableViewModel(setup.getFormStore(), tableModel);

        Connection<Optional<FormRecord>> selection = connect(viewModel.getSelectedRecord());

        // Initially, we don't expect a selection
        assertThat(selection.assertLoaded().isPresent(), equalTo(false));


        // Ensure that when the selection is changed, the observable changes...

        selection.resetChangeCounter();

        RecordRef selectedRef = setup.getSurveyForm().getRecordRef(101);

        viewModel.select(selectedRef);
        selection.assertChanged();

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
    public void testSubFormPane() {

        IncidentForm incidentForm = setup.getCatalog().getIncidentForm();


        TableModel tableModel = ImmutableTableModel.builder().formId(incidentForm.getFormId()).build();
        TableViewModel viewModel = new TableViewModel(setup.getFormStore(), tableModel);

        Connection<EffectiveTableModel> subTableView = setup.connect(viewModel.getEffectiveSubTable(ReferralSubForm.FORM_ID));
        Connection<ColumnSet> subTable = setup.connect(subTableView.assertLoaded().getColumnSet());


        // The sub table should not include parent forms
        for (EffectiveTableColumn subColumn : subTableView.assertLoaded().getColumns()) {
            if(subColumn.getLabel().equals(incidentForm.getUrgencyField().getLabel())) {
                throw new AssertionError("Sub table should not include parent fields");
            }
        }

        // Initially there should be no rows because there is no selection
        assertThat(subTable.assertLoaded().getNumRows(), equalTo(0));


        // Once we make a selection, then the column set should update to show the sub records of the selected
        // parent record

        subTable.resetChangeCounter();

        viewModel.select(incidentForm.getRecordRef(0));
        setup.runScheduled();

        subTable.assertChanged();

        assertThat(subTable.assertLoaded().getNumRows(), equalTo(4));
    }



}