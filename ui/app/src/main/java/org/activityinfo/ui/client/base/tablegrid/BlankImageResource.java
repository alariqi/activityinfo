package org.activityinfo.ui.client.base.tablegrid;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;

public class BlankImageResource implements ImageResource {

    public static final BlankImageResource BLANK16 = new BlankImageResource(16, 16);

    private static final String DATA = "data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==";

    private final int width;
    private final int height;


    public BlankImageResource(int width, int height) {
        this.width = width;
        this.height = height;
    }


    @Override
    public int getLeft() {
        return 0;
    }

    @Override
    public int getTop() {
        return 0;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public SafeUri getSafeUri() {
        return UriUtils.fromSafeConstant(DATA);
    }

    @Override
    public String getURL() {
        return DATA;
    }


    @Override
    public boolean isAnimated() {
        return false;
    }

    @Override
    public String getName() {
        return "blank";
    }
}
