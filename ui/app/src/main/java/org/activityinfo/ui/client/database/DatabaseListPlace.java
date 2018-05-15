package org.activityinfo.ui.client.database;

import com.google.gwt.place.shared.Place;

public class DatabaseListPlace extends Place {

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
