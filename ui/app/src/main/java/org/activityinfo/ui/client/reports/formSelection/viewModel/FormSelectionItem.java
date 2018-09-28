package org.activityinfo.ui.client.reports.formSelection.viewModel;

import org.activityinfo.model.resource.ResourceId;

import java.util.Objects;

public class FormSelectionItem {


    public enum Selection {
        ALL,
        NONE,
        SOME
    }

    private ResourceId id;
    private String surtitle;
    private Selection selected;
    private String label;

    public FormSelectionItem(ResourceId id, String surtitle, String label, Selection selected) {
        this.id = id;
        this.surtitle = surtitle;
        this.label = label;
        this.selected = selected;
    }

    public FormSelectionItem(ResourceId id, String label, Selection selected) {
        this(id, null, label, selected);
    }

    public ResourceId getId() {
        return id;
    }

    public Selection getSelected() {
        return selected;
    }

    public String getLabel() {
        return label;
    }


    public boolean hasSurtitle() {
        return surtitle != null;
    }

    public String getSurtitle() {
        return surtitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FormSelectionItem that = (FormSelectionItem) o;
        return Objects.equals(id, that.id) &&
                selected == that.selected &&
                Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, selected, label);
    }

    @Override
    public String toString() {
        return "FormSelectionItem{" +
                "id=" + id +
                ", selected=" + selected +
                ", label='" + label + '\'' +
                '}';
    }
}
