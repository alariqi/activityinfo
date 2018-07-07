package org.activityinfo.ui.client;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;

public abstract class Place {

    public SafeUri toUri() {
        return UriUtils.fromTrustedString("#" + toString());
    }
}
