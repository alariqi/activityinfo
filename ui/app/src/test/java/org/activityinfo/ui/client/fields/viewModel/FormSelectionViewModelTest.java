package org.activityinfo.ui.client.fields.viewModel;

import org.activityinfo.observable.Connection;
import org.activityinfo.store.testing.IraqDatabase;
import org.activityinfo.ui.client.reports.formSelection.state.FormPath;
import org.activityinfo.ui.client.reports.formSelection.viewModel.*;
import org.activityinfo.ui.client.store.TestSetup;
import org.junit.Test;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class FormSelectionViewModelTest {

    private TestSetup setup = new TestSetup();

    @Test
    public void start() {
        FormPath model = new FormPath();
        FormSelectionViewModel viewModel = FormSelectionBuilder.compute(setup.getFormStore(), model);

        assertThat(viewModel.getColumns(), hasSize(1));
    }

    @Test
    public void selectDatabase() {

        setup.describeDatabase(IraqDatabase.database());

        FormPath model = new FormPath()
                .navigateTo(0, FormPath.DATABASE_ROOT_ID);

        FormSelectionViewModel viewModel = FormSelectionBuilder.compute(setup.getFormStore(), model);

        assertThat(viewModel.getColumns(), hasSize(2));

        Connection<SelectionColumn> databaseColumn = setup.connect(viewModel.getColumns().get(1));
        assertThat(databaseColumn.assertLoaded().getItems(), contains(
                new SelectionNode(IraqDatabase.DATABASE_ID, NodeType.DATABASE,
                        IraqDatabase.LABEL, SelectionStatus.NONE)));
    }


}