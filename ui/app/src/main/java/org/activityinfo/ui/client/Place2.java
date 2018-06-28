package org.activityinfo.ui.client;

import com.google.gwt.place.shared.Place;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;

public abstract class Place2 extends Place {

    public SafeUri toUri() {
        return UriUtils.fromTrustedString("#" + toString());
    }
}
