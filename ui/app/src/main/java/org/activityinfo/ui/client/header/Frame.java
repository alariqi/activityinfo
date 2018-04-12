package org.activityinfo.ui.client.header;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import org.activityinfo.model.database.Resource;
import org.activityinfo.model.database.UserDatabaseMeta;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.search.SearchResult;
import org.activityinfo.ui.client.store.FormStore;

import java.util.ArrayList;
import java.util.List;

public class Frame implements AcceptsOneWidget, IsWidget {

    private final VerticalLayoutContainer container = new VerticalLayoutContainer();
    private Widget currentPageWidget;

    public Frame(FormStore formStore) {
        container.add(new Header(resourceList(formStore)),
                new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        container.add(new ConnectionStatus());
    }

    private Observable<List<SearchResult>> resourceList(FormStore formStore) {
        return formStore.getDatabases().transform(databases -> {
            List<SearchResult> results = new ArrayList<>();
            for (UserDatabaseMeta database : databases) {
                results.add(new SearchResult(database));
                for (Resource resource : database.getResources()) {
                    results.add(new SearchResult(database, resource));
                }
            }
            return results;
        });
    }

    @Override
    public void setWidget(IsWidget w) {

        Widget newWidget;
        if(w == null) {
            newWidget = null;
        } else {
            newWidget = w.asWidget();
        }

        if(newWidget != currentPageWidget) {
            if(currentPageWidget != null) {
                container.remove(currentPageWidget);
            }
            if(newWidget != null) {
                container.add(newWidget, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
            }
            currentPageWidget = newWidget;

            Scheduler.get().scheduleFinally(container::forceLayout);
        }
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
