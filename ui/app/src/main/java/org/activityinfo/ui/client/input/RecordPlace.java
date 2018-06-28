package org.activityinfo.ui.client.input;

import org.activityinfo.model.type.RecordRef;
import org.activityinfo.ui.client.Place2;

import java.util.Objects;

public class RecordPlace extends Place2 {
    private RecordRef ref;

    public RecordPlace(RecordRef ref) {
        this.ref = ref;
    }

    public RecordRef getRef() {
        return ref;
    }

    @Override
    public String toString() {
        return "edit/" + ref.getFormId().asString() + "/edit/" + ref.getRecordId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordPlace place = (RecordPlace) o;
        return Objects.equals(ref, place.ref);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ref);
    }
}
