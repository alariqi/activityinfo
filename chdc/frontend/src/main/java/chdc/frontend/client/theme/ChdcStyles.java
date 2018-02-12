package chdc.frontend.client.theme;

import com.google.gwt.resources.client.CssResource;

/**
 * Defines CSS style names
 */
public interface ChdcStyles extends CssResource {

    String banner();

    String topnav();

    String button();

    @ClassName("button--small")
    String buttonSmall();

    @ClassName("button--primary")
    String buttonPrimary();

    @ClassName("button--naked")
    String buttonNaked();

    @ClassName("button--icon")
    String buttonIcon();

    @ClassName("button--dimmed")
    String buttonDimmed();

    String icon();

    @ClassName("icon--dimmed")
    String iconDimmed();

    @ClassName("icon--xs")
    String iconExtraSmall();

    @ClassName("icon--small")
    String iconSmall();

    @ClassName("icon-boxes")
    String iconBoxes();


    @ClassName("actionbar__inner")
    String actionBarInner();

    @ClassName("actionbar__shortcuts")
    String actionBarShortcuts();


    @ClassName("actionbar__primary")
    String actionbarPrimary();

    @ClassName("actionbar__secondary")
    String actionbarSecondary();
}
