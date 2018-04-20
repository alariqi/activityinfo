package org.activityinfo.dev.client;

import com.google.gwt.user.client.ui.IsWidget;
import org.activityinfo.ui.client.nonideal.LoadingPage;
import org.activityinfo.ui.client.nonideal.NotFoundPage;
import org.activityinfo.ui.client.nonideal.PermissionDeniedPage;

public enum DevPage {
    INDEX {
        @Override
        public IsWidget createWidget() {
            return new IndexPage();
        }
    },
    TABLEGRID {
        @Override
        public IsWidget createWidget() {
            return new TableMockup();
        }
    },
    SEARCH {
        @Override
        public IsWidget createWidget() {
            return new SearchPanel();
        }
    },
    FORMS {
        @Override
        public IsWidget createWidget() {
            return new FormMockup();
        }
    },
    MENU {
        @Override
        public IsWidget createWidget() {
            return new MenuPage();
        }
    },
    DATABASES_PAGE {
        @Override
        public IsWidget createWidget() {
            return DatabasesMockup.smallList();
        }
    },
    EMPTY_DATABASES_PAGE {
        @Override
        public IsWidget createWidget() {
            return DatabasesMockup.emptyList();
        }
    },
    DATABASE_PAGE {
        @Override
        public IsWidget createWidget() {

            return DatabaseMockup.iraq();
        }
    },
    EMPTY_DATABASE_PAGE {
        @Override
        public IsWidget createWidget() {
            return DatabaseMockup.empty();
        }
    },
    NOT_FOUND {
        @Override
        public IsWidget createWidget() {
            return new NonIdealMockup(new NotFoundPage());
        }
    },
    PERMISSION_DENIED {
        @Override
        public IsWidget createWidget() {
            return new NonIdealMockup(new PermissionDeniedPage());
        }
    },
    LOADING {
        @Override
        public IsWidget createWidget() {
            return new NonIdealMockup(new LoadingPage());
        }
    };

    public abstract IsWidget createWidget();
}
