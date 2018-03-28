package org.activityinfo.ui.client.database;

import org.activityinfo.model.database.Resource;
import org.activityinfo.model.database.ResourceType;
import org.activityinfo.model.database.UserDatabaseMeta;
import org.activityinfo.theme.client.NavListItem;
import org.activityinfo.ui.client.PlaceLinks;
import org.activityinfo.ui.client.table.TablePlace;

import java.util.ArrayList;
import java.util.List;

public class DatabaseViewModel {

    private UserDatabaseMeta database;

    public DatabaseViewModel(UserDatabaseMeta database) {
        this.database = database;
    }

    public String getLabel() {
        return this.database.getLabel();
    }

    public UserDatabaseMeta getDatabase() {
        return database;
    }

    public List<NavListItem> getFormLinks() {
        List<NavListItem> items = new ArrayList<>();
        for (Resource resource : database.getResources()) {
            if(resource.getType() == ResourceType.FORM &&
                    resource.getParentId().equals(database.getDatabaseId())) {
                items.add(new NavListItem(
                        resource.getId().asString(),
                        resource.getLabel(),
                        PlaceLinks.toUri(new TablePlace(resource.getId()))));
            }
        }
        return items;
    }
}
