package org.activityinfo.ui.client.base;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import org.activityinfo.ui.client.Icon;

public class SvgIcon implements ImageResource {

    private final Icon icon;
    private final int size;

    public SvgIcon(Icon icon, int size) {
        this.icon = icon;
        this.size = size;
    }

    public Icon getIcon() {
        return icon;
    }

    @Override
    public int getHeight() {
        return size;
    }

    @Override
    public int getLeft() {
        return 0;
    }

    @Override
    public SafeUri getSafeUri() {
        return UriUtils.fromTrustedString("#");
    }

    @Override
    public int getTop() {
        return 0;
    }

    @Override
    public String getURL() {
        return "#";
    }

    @Override
    public int getWidth() {
        return size;
    }

    @Override
    public boolean isAnimated() {
        return false;
    }

    @Override
    public String getName() {
        return "href";
    }
}
