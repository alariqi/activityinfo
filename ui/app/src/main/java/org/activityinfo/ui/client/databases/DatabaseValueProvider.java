package org.activityinfo.ui.client.databases;

import com.sencha.gxt.core.client.ValueProvider;
import org.activityinfo.model.database.UserDatabaseMeta;
import org.activityinfo.theme.client.NavListItem;

public class DatabaseValueProvider implements ValueProvider<UserDatabaseMeta, NavListItem> {
    @Override
    public NavListItem getValue(UserDatabaseMeta object) {
        return new NavListItem(object.getDatabaseId().asString(), object.getLabel());
    }

    @Override
    public void setValue(UserDatabaseMeta object, NavListItem value) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public String getPath() {
        return "";
    }
}
