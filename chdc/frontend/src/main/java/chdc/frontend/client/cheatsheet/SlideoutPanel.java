package chdc.frontend.client.cheatsheet;

import chdc.frontend.client.theme.ChdcTheme;
import chdc.frontend.client.theme.CloseButton;
import chdc.frontend.client.theme.CssLayoutContainer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

/**
 * Slide out panel.
 */
public class SlideoutPanel implements HasCloseHandlers<SlideoutPanel> {


    interface SlideoutPanelUiBinder extends UiBinder<HTMLPanel, SlideoutPanel> {
    }

    private static SlideoutPanelUiBinder ourUiBinder = GWT.create(SlideoutPanelUiBinder.class);

    private final HTMLPanel panel;


    @UiField
    HeadingElement titleHeading;

    @UiField
    CssLayoutContainer panelContent;

    @UiField
    CloseButton closeButton;

    private boolean visible = false;

    private SimpleEventBus eventBus = new SimpleEventBus();

    public SlideoutPanel() {
        panel = ourUiBinder.createAndBindUi(this);

        panel.addDomHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if(event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
                    hide();
                }
            }
        }, KeyUpEvent.getType());
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
        visible = true;
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
        visible = false;
        CloseEvent.fire(this, this);
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public HandlerRegistration addCloseHandler(CloseHandler<SlideoutPanel> handler) {
        return eventBus.addHandler(CloseEvent.getType(), handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        eventBus.fireEvent(event);
    }
}