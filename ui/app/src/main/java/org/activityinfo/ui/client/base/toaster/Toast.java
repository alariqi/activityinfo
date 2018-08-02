package org.activityinfo.ui.client.base.toaster;

import org.activityinfo.api.client.ApiException;
import org.activityinfo.i18n.shared.I18N;

public class Toast {

    static final int DEFAULT_AUTO_HIDE_DELAY_MS = 2500;

    private ToastType type;

    private String title;
    private String message;

    private String actionLabel;
    private Runnable action;

    /**
     * True if the toast should be hidden after a suitable delay
     */
    private int autoHide;

    private Toast() {
    }

    public ToastType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public boolean isAutoHide() {
        return autoHide > 0;
    }

    public boolean hasAction() {
        return action != null;
    }

    public String getActionLabel() {
        return actionLabel;
    }

    public Runnable getAction() {
        return action;
    }

    public int getAutoHideDelay() {
        return autoHide;
    }

    public static class Builder {
        private Toast toast = new Toast();

        public Builder success(String message) {
            toast.title = I18N.CONSTANTS.success();
            toast.message = message;
            toast.type = ToastType.SUCCESS;
            return this;
        }

        public Builder error(String message) {
            toast.title = I18N.CONSTANTS.error();
            toast.message = message;
            toast.type = ToastType.ERROR;
            return this;
        }

        public Builder error(Throwable throwable) {
            return error(messageFromException(throwable));
        }

        private static String messageFromException(Throwable caught) {
            if(caught instanceof ApiException && caught.getMessage().equals("0")) {
                return I18N.CONSTANTS.saveFailedConnectionProblem();
            }
            return I18N.CONSTANTS.errorOnServer();
        }

        public Builder type(ToastType type) {
            toast.type = type;
            return this;
        }

        public Builder title(String title) {
            toast.title = title;
            return this;
        }

        public Builder message(String message) {
            toast.message = message;
            return this;
        }

        public Builder autoHide() {
            toast.autoHide = DEFAULT_AUTO_HIDE_DELAY_MS;
            return this;
        }

        public Builder autoHideSeconds(int delay) {
            toast.autoHide = delay * 1000;
            return this;
        }

        public Builder autoHide(int milliseconds) {
            toast.autoHide = milliseconds;
            return this;
        }

        public Builder action(String actionText, Runnable handler) {
            toast.actionLabel = actionText;
            toast.action = handler;
            return this;
        }

        public Toast build() {
            return toast;
        }

    }
}
