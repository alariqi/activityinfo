package org.activityinfo.theme.dev.client;

import com.google.gwt.place.shared.Place;

import java.util.Objects;

public class DevPlace extends Place {
    private DevPage page;

    public DevPlace(DevPage page) {
        this.page = page;
    }

    public DevPage getPage() {
        return page;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DevPlace devPlace = (DevPlace) o;
        return page == devPlace.page;
    }

    @Override
    public int hashCode() {
        return Objects.hash(page);
    }
}
