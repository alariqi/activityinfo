package org.activityinfo.ui.client;

import org.activityinfo.ui.client.base.NonIdeal;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public class NotFoundPage extends Page {

    private final FormStore formStore;

    public NotFoundPage(FormStore formStore) {
        this.formStore = formStore;
    }

    @Override
    public VTree render() {
        return AppFrame.render(formStore, NonIdeal.notFound());
    }
}
