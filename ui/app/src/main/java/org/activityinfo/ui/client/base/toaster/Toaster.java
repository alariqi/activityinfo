package org.activityinfo.ui.client.base.toaster;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;
import org.activityinfo.ui.vdom.client.VDomWidget;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Toaster {

    private static Toaster INSTANCE;

    public static void show(Toast toast) {
        if(INSTANCE == null) {
            INSTANCE = new Toaster();
        }
        INSTANCE.showToast(toast);
    }

    private final List<Toast> toasts = new ArrayList<>();
    private final VDomWidget vdom;

    private Toaster() {
        vdom = new VDomWidget();
        RootPanel.get().add(vdom);
    }

    private void showToast(Toast toast) {
        toasts.add(toast);
        if(toast.isAutoHide()) {
            Scheduler.get().scheduleFixedDelay(() -> {
                hideToast(toast);
                return false;
            }, toast.getAutoHideDelay());
        }
        vdom.update(renderToasts(toasts));
    }

    private void hideToast(Toast toast) {
        toasts.remove(toast);
        vdom.update(renderToasts(toasts));
    }

    private VTree renderToasts(List<Toast> toasts) {
        return H.div("toaster", toasts.stream().map(this::renderToast));
    }

    private VTree renderToast(Toast toast) {
        return H.div(toastStyles(toast),
                H.div("toast__header", new VText(toast.getTitle())),
                H.div("toast__message", renderToastMessage(toast)));
    }

    private Stream<VTree> renderToastMessage(Toast toast) {
        List<VTree> children = new ArrayList<>();
        children.add(new VText(toast.getMessage()));
        if(toast.hasAction()) {
            children.add(renderToastAction(toast));
        }
        return children.stream();
    }

    private VTree renderToastAction(Toast toast) {
        PropMap buttonProps = Props.create();
        buttonProps.set("onclick", new EventHandler() {
                    @Override
                    public void onEvent(Event event) {
                        try {
                            toast.getAction().run();
                        } catch (Exception e) {
                        }
                        hideToast(toast);
                    }
                });
        buttonProps.set("type", "button");

        return new VNode(HtmlTag.BUTTON, buttonProps, new VText(toast.getActionLabel()));
    }

    private String toastStyles(Toast toast) {
        switch (toast.getType()) {
            case SUCCESS:
                return "toast toast--success";
            case WARNING:
                return "toast toast--warning";
            case ERROR:
                return "toast toast--error";
        }
        return "toast";
    }
}
