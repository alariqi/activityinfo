package org.activityinfo.ui.client.table.view;

import com.google.common.base.Optional;
import com.sencha.gxt.core.client.dom.XElement;
import org.activityinfo.model.formTree.RecordTree;

public class DetailUpdater {
    private final DetailsRenderer renderer;
    private final Optional<RecordTree> recordTree;

    public DetailUpdater(DetailsRenderer renderer, Optional<RecordTree> recordTree) {
        this.renderer = renderer;
        this.recordTree = recordTree;
    }

    public void update(XElement parent) {
        if(recordTree.isPresent()) {
            renderer.update(parent, recordTree.get());
        }
    }
}
