package org.activityinfo.dev.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.model.database.Resource;
import org.activityinfo.model.database.ResourceBuilder;
import org.activityinfo.model.database.ResourceType;
import org.activityinfo.model.database.UserDatabaseMeta;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.ui.client.base.container.CssLayoutContainer;
import org.activityinfo.ui.client.database.DatabasePage;
import org.activityinfo.ui.client.database.DatabaseViewModel;
import org.activityinfo.ui.client.header.ConnectionStatus;
import org.activityinfo.ui.client.header.Header;

public class DatabaseMockup implements IsWidget {

    private static final ResourceId DATABASE_ID = ResourceId.valueOf("d0");

    private CssLayoutContainer container;

    private DatabaseMockup(DatabaseViewModel databaseViewModel) {
        Header header = new Header(SearchResults.getResourceList());
        DatabasePage page = new DatabasePage();
        page.updateView(databaseViewModel);

        header.setSettingsActive(true);

        this.container = new CssLayoutContainer();
        this.container.add(header);
        this.container.add(new ConnectionStatus());
        this.container.add(page);
    }

    public static DatabaseMockup iraq() {

        UserDatabaseMeta database = new UserDatabaseMeta.Builder()
            .setOwner(true)
            .setDatabaseId(DATABASE_ID)
            .setLabel("2018: IRAQ IDP")
            .addResource(form("a2105583057", "Referring emergency cases to higher-level facilities"))
            .addResource(form("a2105583056", "Providing front-line trauma care at trauma stabilization points to people who are wounded and referring emergency cases onwards to field hospitals and facilities with specialized units"))
            .addResource(form("a2105583071", "Procuring, pre-positioning and dispatching essential medicines and supplies to priority locations"))
            .addResource(form("a2105583070", "Promoting health awareness"))
            .addResource(form("f0000006869", "CCCM 1: Help to ensure dignified, safe, liveable conditions for displaced families in settlements"))
            .addResource(form("f0000006909", "HEALTH : Full Cluster Response"))
            .addResource(form("a2105583069", "Accelerating routine vaccinations and strengthening cold chain systems"))
            .build();

        DatabaseViewModel databaseViewModel = new DatabaseViewModel(database);

        return new DatabaseMockup(databaseViewModel);
    }

    public static DatabaseMockup empty() {

        UserDatabaseMeta database = new UserDatabaseMeta.Builder()
                .setOwner(true)
                .setDatabaseId(DATABASE_ID)
                .setLabel("2018: IRAQ IDP")
                .build();

        return new DatabaseMockup(new DatabaseViewModel(database));

    }

    private static Resource form(String id, String label) {
        return new ResourceBuilder()
                .setId(ResourceId.valueOf(id))
                .setLabel(label)
                .setParentId(DATABASE_ID    )
                .setType(ResourceType.FORM)
                .build();
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
