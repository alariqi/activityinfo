package chdc.frontend.client.cheatsheet;

import chdc.frontend.client.theme.ChdcTheme;
import chdc.frontend.client.theme.CloseButton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

/**
 * Slide out panel
 */
public class SlideoutPanel {


    interface SlideoutPanelUiBinder extends UiBinder<HTMLPanel, SlideoutPanel> {
    }

    private static SlideoutPanelUiBinder ourUiBinder = GWT.create(SlideoutPanelUiBinder.class);

    private final HTMLPanel panel;


    @UiField
    HeadingElement titleHeading;

    @UiField
    FlowLayoutContainer panelContent;

    @UiField
    CloseButton closeButton;

    public SlideoutPanel() {
        panel = ourUiBinder.createAndBindUi(this);
    }

    public void setTitleHeading(SafeHtml title) {
        titleHeading.setInnerSafeHtml(title);
    }

    public void add(IsWidget widget) {
        panelContent.add(widget);
    }

    public void show() {
        if(!panel.isAttached()) {
            RootPanel.get().add(panel);
        }
        panel.removeStyleName(ChdcTheme.STYLES.isHidden());
    }


    public void attach() {
        if(!panel.isAttached()) {
            RootPanel.get().add(panel);
        }
    }

    public void detach() {
        if(panel.isAttached()) {
            panel.removeFromParent();
        }
    }

    @UiHandler("closeButton")
    public void onClose(SelectEvent event) {
        hide();
    }

    public void hide() {
        panel.addStyleName(ChdcTheme.STYLES.isHidden());
    }
}