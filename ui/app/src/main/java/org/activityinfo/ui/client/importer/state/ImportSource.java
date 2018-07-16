package org.activityinfo.ui.client.importer.state;

import java.util.Objects;

public class ImportSource {
    private String text;

    public ImportSource(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImportSource that = (ImportSource) o;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
