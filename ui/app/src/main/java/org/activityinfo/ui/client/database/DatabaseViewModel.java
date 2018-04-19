package org.activityinfo.ui.client.database;

import org.activityinfo.model.database.Resource;
import org.activityinfo.model.database.ResourceType;
import org.activityinfo.model.database.UserDatabaseMeta;
import org.activityinfo.ui.client.PlaceLinks;
import org.activityinfo.ui.client.databases.ListItem;
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

    public List<ListItem> getFormLinks() {
        List<ListItem> items = new ArrayList<>();
        for (Resource resource : database.getResources()) {
            if(resource.getType() == ResourceType.FORM &&
                    resource.getParentId().equals(database.getDatabaseId())) {
                items.add(new ListItem(
                        resource.getId().asString(),
                        resource.getLabel(),
                        PlaceLinks.toUri(new TablePlace(resource.getId())),
                        "#type_form", false));
            }
        }
        return items;
    }

    public String getAvatarHref() {
        return "#type_database";
    }
}
