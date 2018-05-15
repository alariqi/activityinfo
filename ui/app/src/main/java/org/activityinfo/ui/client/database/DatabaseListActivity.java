package org.activityinfo.ui.client.database;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.SubscriptionSet;
import org.activityinfo.ui.client.store.FormStore;

import java.util.List;
import java.util.stream.Collectors;

public class DatabaseListActivity implements Activity {

    private final Observable<List<ListItem>> databaseList;
    private final SubscriptionSet subscriptions = new SubscriptionSet();

    public DatabaseListActivity(FormStore formStore) {
        this.databaseList = formStore.getCatalogChildren(ResourceId.valueOf("databases"))
                .transform(entries ->
                    entries.stream().map(entry ->
                        new ListItem(entry.getId(), entry.getLabel(),
                                UriUtils.fromSafeConstant("#database/" + entry.getId()), "#type_database", false))
                        .collect(Collectors.toList())
                );
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        DatabaseListPage page = new DatabaseListPage();
        panel.setWidget(page);

        subscriptions.add(databaseList.subscribe(page::updateView));
    }

    @Override
    public String mayStop() {
        return null;
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onStop() {
        subscriptions.unsubscribeAll();
    }

}
