package org.activityinfo.ui.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.ui.client.database.DatabaseListPage;
import org.activityinfo.ui.client.database.DatabaseListPlace;
import org.activityinfo.ui.client.database.DatabasePage;
import org.activityinfo.ui.client.database.DatabasePlace;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.client.table.TablePage;
import org.activityinfo.ui.client.table.TablePlace;
import org.activityinfo.ui.vdom.client.VDomWidget;

public class AppHolder implements IsWidget {

    private final FormStore formStore;
    private final VDomWidget widget;

    private Page currentPage;

    public AppHolder(FormStore formStore) {
        this.formStore = formStore;
        widget = new VDomWidget();
    }

    public void navigateTo(Place place) {

        if(currentPage != null) {
            if(currentPage.tryNavigate(place)) {
                return;
            }
        }

        currentPage = createPage(place);
        widget.update(currentPage.render());
    }

    private Page createPage(Place p) {
        if(p instanceof DatabaseListPlace) {
            return new DatabaseListPage(formStore);
        } else if(p instanceof DatabasePlace) {
            return new DatabasePage(formStore, (DatabasePlace) p);

        } else if(p instanceof TablePlace) {
            return new TablePage(formStore, (TablePlace) p);

        } else {
            return new NotFoundPage(formStore);
        }
    }

    @Override
    public Widget asWidget() {
        return widget;
    }
}
