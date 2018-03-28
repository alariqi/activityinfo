package org.activityinfo.ui.client;

import com.google.gwt.place.shared.Place;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;

public class PlaceLinks {

    private PlaceLinks() {}

    public static SafeUri toUri(Place place) {
        return UriUtils.fromTrustedString("#" +  place.toString());
    }
}
