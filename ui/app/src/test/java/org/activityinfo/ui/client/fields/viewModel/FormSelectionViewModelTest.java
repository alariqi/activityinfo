package org.activityinfo.ui.client.fields.viewModel;

import org.activityinfo.observable.Connection;
import org.activityinfo.store.testing.IraqDatabase;
import org.activityinfo.ui.client.reports.formSelection.state.FormSelectionState;
import org.activityinfo.ui.client.reports.formSelection.viewModel.FormSelectionBuilder;
import org.activityinfo.ui.client.reports.formSelection.viewModel.FormSelectionColumn;
import org.activityinfo.ui.client.reports.formSelection.viewModel.FormSelectionItem;
import org.activityinfo.ui.client.reports.formSelection.viewModel.FormSelectionViewModel;
import org.activityinfo.ui.client.store.TestSetup;
import org.junit.Test;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class FormSelectionViewModelTest {

    private TestSetup setup = new TestSetup();

    @Test
    public void start() {
        FormSelectionState model = new FormSelectionState();
        FormSelectionViewModel viewModel = FormSelectionBuilder.compute(setup.getFormStore(), model);

        assertThat(viewModel.getColumns(), hasSize(1));
    }

    @Test
    public void selectDatabase() {

        setup.describeDatabase(IraqDatabase.database());

        FormSelectionState model = new FormSelectionState()
                .navigateTo(0, FormSelectionState.DATABASE_ROOT_ID);

        FormSelectionViewModel viewModel = FormSelectionBuilder.compute(setup.getFormStore(), model);

        assertThat(viewModel.getColumns(), hasSize(2));

        Connection<FormSelectionColumn> databaseColumn = setup.connect(viewModel.getColumns().get(1));
        assertThat(databaseColumn.assertLoaded().getItems(), contains(
                new FormSelectionItem(IraqDatabase.DATABASE_ID, "Database", IraqDatabase.LABEL, FormSelectionItem.Selection.NONE)));
    }


}