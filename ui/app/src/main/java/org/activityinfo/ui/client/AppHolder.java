package org.activityinfo.ui.client;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.ui.client.database.DatabaseListPage;
import org.activityinfo.ui.client.database.DatabaseListPlace;
import org.activityinfo.ui.client.database.DatabasePage;
import org.activityinfo.ui.client.database.DatabasePlace;
import org.activityinfo.ui.client.importer.ImportPage;
import org.activityinfo.ui.client.importer.ImportPlace;
import org.activityinfo.ui.client.reports.ReportListPage;
import org.activityinfo.ui.client.reports.ReportListPlace;
import org.activityinfo.ui.client.reports.pivot.PivotPage;
import org.activityinfo.ui.client.reports.pivot.PivotPlace;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.client.table.TablePage;
import org.activityinfo.ui.client.table.TablePlace;
import org.activityinfo.ui.vdom.client.VDomWidget;

import java.util.Objects;

public class AppHolder implements IsWidget {

    private final FormStore formStore;
    private final VDomWidget widget;

    private Place currentPlace;
    private Page currentPage;

    public AppHolder(FormStore formStore) {
        this.formStore = formStore;
        widget = new VDomWidget();
        Window.addWindowClosingHandler(closingEvent -> {
            beforeNavigateAway(closingEvent);
        });
    }

    private void beforeNavigateAway(Window.ClosingEvent closingEvent) {
        if(currentPage != null) {
            String message = currentPage.mayStop();
            if(message != null) {
                closingEvent.setMessage(message);
            }
        }
    }

    public void navigateTo(Place place) {

        if(Objects.equals(currentPlace, place)) {
            return;
        }

        if(currentPage != null) {

            String warning = currentPage.mayStop();
            if(warning != null) {
                if(!Window.confirm(warning)) {
                    History.replaceItem(currentPlace.toString());
                    return;
                }
            }

            if(currentPage.tryNavigate(place)) {
                return;
            }

            currentPage.stop();
        }

        currentPlace = place;
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

        } else if(p instanceof ImportPlace) {
            return new ImportPage(formStore, (ImportPlace) p);

        } else if(p instanceof ReportListPlace) {
            return new ReportListPage(formStore);

        } else if(p instanceof PivotPlace) {
            return new PivotPage(formStore, ((PivotPlace) p));

        } else {
            return new NotFoundPage(formStore);
        }
    }

    @Override
    public Widget asWidget() {
        return widget;
    }
}
