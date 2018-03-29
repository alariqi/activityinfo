package org.activityinfo.theme.client.search;

public class SearchResult {
    private String id;
    private String label;

    public SearchResult(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
