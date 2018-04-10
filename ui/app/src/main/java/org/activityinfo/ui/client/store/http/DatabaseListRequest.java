package org.activityinfo.ui.client.store.http;

import org.activityinfo.api.client.ActivityInfoClientAsync;
import org.activityinfo.model.database.DatabaseHeader;
import org.activityinfo.promise.Promise;

import java.util.List;

public class DatabaseListRequest implements HttpRequest<List<DatabaseHeader>> {
    @Override
    public Promise<List<DatabaseHeader>> execute(ActivityInfoClientAsync client) {
        return client.getDatabases();
    }

    @Override
    public int refreshInterval(List<DatabaseHeader> result) {
        return -1;
    }
}
