package org.activityinfo.ui.client.importer.state;

import org.activityinfo.ui.client.importer.viewModel.ImportedTable;

import java.util.function.Function;

public interface ImportUpdater {

    void update(Function<ImportState, ImportState> function);

    void startImport(ImportedTable table);

    void cancel();
}
