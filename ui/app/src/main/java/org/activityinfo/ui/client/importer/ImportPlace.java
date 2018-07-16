package org.activityinfo.ui.client.importer;

import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.ui.client.Place;

import java.util.Objects;

public class ImportPlace extends Place {
    private ResourceId formId;

    public ImportPlace(ResourceId formId) {
        this.formId = formId;
    }

    public ResourceId getFormId() {
        return formId;
    }

    @Override
    public String toString() {
        return "import/" + formId.asString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImportPlace that = (ImportPlace) o;
        return Objects.equals(formId, that.formId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(formId);
    }
}
