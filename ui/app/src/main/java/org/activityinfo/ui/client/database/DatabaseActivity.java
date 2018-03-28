package org.activityinfo.ui.client.database;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import org.activityinfo.observable.SubscriptionSet;
import org.activityinfo.ui.client.store.FormStore;

public class DatabaseActivity implements Activity {

    private final SubscriptionSet subscriptionSet = new SubscriptionSet();
    private final FormStore formStore;
    private final DatabasePlace place;

    public DatabaseActivity(FormStore formStore, DatabasePlace place) {
        this.formStore = formStore;
        this.place = place;
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        DatabasePage page = new DatabasePage();
        panel.setWidget(page);

        subscriptionSet.add(formStore
                .getDatabase(place.getDatabaseId())
                .transform(DatabaseViewModel::new)
                .subscribe(page::updateView));
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
        subscriptionSet.unsubscribeAll();
    }

}
