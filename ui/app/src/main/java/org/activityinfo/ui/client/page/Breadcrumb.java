package org.activityinfo.ui.client.page;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;

public class Breadcrumb {
    private String label;
    private SafeUri uri;

    public Breadcrumb(String label, SafeUri uri) {
        this.label = label;
        this.uri = uri;
    }

    public String getLabel() {
        return label;
    }

    public SafeUri getUri() {
        return uri;
    }

    public void renderTo(SafeHtmlBuilder html) {
        html.append(PageTemplates.TEMPLATES.breadcrumb(label, uri));
    }
}
