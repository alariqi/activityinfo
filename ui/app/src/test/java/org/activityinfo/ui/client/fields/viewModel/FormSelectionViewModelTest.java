package org.activityinfo.ui.client.fields.viewModel;

import org.activityinfo.observable.Connection;
import org.activityinfo.store.testing.IraqDatabase;
import org.activityinfo.ui.client.fields.model.FormSelectionModel;
import org.activityinfo.ui.client.store.TestSetup;
import org.junit.Test;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class FormSelectionViewModelTest {

    private TestSetup setup = new TestSetup();

    @Test
    public void start() {
        FormSelectionModel model = new FormSelectionModel();
        FormSelectionViewModel viewModel = FormSelectionBuilder.compute(setup.getFormStore(), model);

        assertThat(viewModel.getColumns(), hasSize(1));
    }

    @Test
    public void selectDatabase() {

        setup.describeDatabase(IraqDatabase.database());

        FormSelectionModel model = new FormSelectionModel()
                .navigateTo(0, FormSelectionModel.DATABASE_ROOT_ID);

        FormSelectionViewModel viewModel = FormSelectionBuilder.compute(setup.getFormStore(), model);

        assertThat(viewModel.getColumns(), hasSize(2));

        Connection<FormSelectionColumn> databaseColumn = setup.connect(viewModel.getColumns().get(1));
        assertThat(databaseColumn.assertLoaded().getItems(), contains(
                new FormSelectionItem(IraqDatabase.DATABASE_ID, IraqDatabase.LABEL, FormSelectionItem.Selection.NONE)));
    }


}