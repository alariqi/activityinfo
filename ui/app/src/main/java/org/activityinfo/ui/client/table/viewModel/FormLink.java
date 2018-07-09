package org.activityinfo.ui.client.table.viewModel;

import com.google.gwt.safehtml.shared.SafeUri;
import org.activityinfo.ui.client.table.TablePlace;

public class FormLink {
    private TablePlace place;
    private String formLabel;

    public FormLink(TablePlace place, String formLabel) {
        this.place = place;
        this.formLabel = formLabel;
    }

    public String getFormLabel() {
        return formLabel;
    }

    public SafeUri toUri() {
        return place.toUri();
    }
}
