package org.activityinfo.ui.client.importer.state;

import java.util.function.Function;

public interface ImportUpdater {

    void update(Function<ImportState, ImportState> function);
}
