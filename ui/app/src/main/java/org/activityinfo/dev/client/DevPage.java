package org.activityinfo.dev.client;

import com.google.gwt.user.client.ui.IsWidget;
import org.activityinfo.ui.client.nonideal.LoadingPage;

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
    LOADING {
        @Override
        public IsWidget createWidget() {
            return new NonIdealMockup(new LoadingPage());
        }
    },
    NOTIFICATIONS {
        @Override
        public IsWidget createWidget() {
            return new NotificationsMockup();
        }
    };

    public abstract IsWidget createWidget();
}
