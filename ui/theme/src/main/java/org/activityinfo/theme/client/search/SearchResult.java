package org.activityinfo.theme.client.search;

import java.util.Arrays;
import java.util.List;

public class SearchResult {
    private String id;
    private String label;
    private final List<String> parents;

    public SearchResult(String id, String label, String... parents) {
        this.id = id;
        this.label = label;
        this.parents = Arrays.asList(parents);
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public List<String> getParents() {
        return parents;
    }

    public String getDatabase() {
        if(parents.isEmpty()) {
            return label;
        } else {
            return parents.get(0);
        }
    }

    public String getType() {
        if(id.endsWith("d")) {
            return "database";
        } else if(id.startsWith("f")) {
            return "folder";
        } else {
            return "form";
        }
    }

    public String getTypeName() {
        return getType().substring(0, 1).toUpperCase() + getType().substring(1);
    }

    @Override
    public String toString() {
        return label;
    }
}
