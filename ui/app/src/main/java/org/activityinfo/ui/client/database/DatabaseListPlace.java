package org.activityinfo.ui.client.database;

import org.activityinfo.ui.client.Place;

public class DatabaseListPlace extends Place {

    public static final DatabaseListPlace INSTANCE = new DatabaseListPlace();

    public DatabaseListPlace() {

    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DatabaseListPlace;
    }

    @Override
    public String toString() {
        return "databases";
    }
}
