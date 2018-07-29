package org.activityinfo.ui.client.page.config.design.importer;

public class SourceColumn {
    private final String header;
    private final int index;

    public SourceColumn(String header, int index) {
        this.header = header;
        this.index = index;
    }

    public String getHeader() {
        return header;
    }

    public int getIndex() {
        return index;
    }
}
