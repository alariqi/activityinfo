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

    @ClassName("button--close")
    String buttonClose();

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

    String actionbar();

    @ClassName("actionbar__inner")
    String actionBarInner();

    @ClassName("actionbar__shortcuts")
    String actionBarShortcuts();


    @ClassName("actionbar__primary")
    String actionbarPrimary();

    @ClassName("actionbar__secondary")
    String actionbarSecondary();

    @ClassName("panel__header")
    String panelHeader();

    @ClassName("panel__headerinfo")
    String panelHeaderInfo();

    @ClassName("no-sidebar")
    String noSidebar();

    @ClassName("panel__container")
    String panelContainer();

    String share();

    String panel();

    @ClassName("panel--half")
    String panelHalf();

    @ClassName("panel__content")
    String panelContent();

    @ClassName("is-hidden")
    String isHidden();

    @ClassName("panel__search-count")
    String panelSearchCount();

    @ClassName("panelholder")
    String panelHolder();

    @ClassName("twoliner")
    String twoLiner();

    @ClassName("icon__container")
    String iconContainer();

    @ClassName("datasheet__head")
    String datasheetHead();

    @ClassName("datasheet__controls")
    String datasheetControls();

    @ClassName("incident-id")
    String incidentId();

    String output();

    String sidebar();

    @ClassName("detail-field")
    String detailField();

    @ClassName("datasheet__body")
    String datasheetBody();

    @ClassName("incident-detail")
    String incidentDetail();

    String datasheet();

    @ClassName("output-text")
    String outputText();

    @ClassName("detail-container")
    String detailContainer();

    @ClassName("incident-title")
    String incidentTitle();

    @ClassName("datetime-cell")
    String datetimeCell();
}
