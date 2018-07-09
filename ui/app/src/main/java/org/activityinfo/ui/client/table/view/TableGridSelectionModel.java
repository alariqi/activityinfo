package org.activityinfo.ui.client.table.view;

import com.sencha.gxt.data.shared.event.StoreClearEvent;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;

public class TableGridSelectionModel extends GridSelectionModel<Integer> {

    TableGridSelectionModel() {
      super();
    }

    @Override
    protected void onClear(StoreClearEvent<Integer> event) {
        // DO NOTHING
        // This gets triggered by the LiveGridView refresh and kills our selection unnecessarily.
    }
}
