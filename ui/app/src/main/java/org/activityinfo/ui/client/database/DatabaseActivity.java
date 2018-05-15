package org.activityinfo.ui.client.database;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import org.activityinfo.observable.SubscriptionSet;
import org.activityinfo.ui.client.nonideal.MaybeContainer;
import org.activityinfo.ui.client.store.FormStore;

/**
 * Displays a list of forms within a database or folder
 */
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
        MaybeContainer<DatabaseViewModel> maybeContainer = new MaybeContainer<>(page);

        panel.setWidget(maybeContainer);

        subscriptionSet.add(formStore
                .getDatabase(place.getDatabaseId())
                .transform(m -> m.transform(DatabaseViewModel::new))
                .subscribe(vm -> maybeContainer.updateView(vm)));
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
