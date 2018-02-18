package chdc.frontend.client.theme;


import com.google.gwt.core.client.GWT;

public class ChdcTheme {

    public static final ChdcResources INSTANCE = GWT.create(ChdcResources.class);

    public static final ChdcStyles STYLES = INSTANCE.style();
}
