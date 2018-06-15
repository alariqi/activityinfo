package org.activityinfo.ui.client.base.menu;

import com.sencha.gxt.theme.base.client.menu.MenuBaseAppearance;

public class Css3MenuAppearance extends MenuBaseAppearance {

    private static class Style implements MenuBaseAppearance.MenuStyle {

        private MenuArrow arrow;

        public Style(MenuArrow arrow) {
            this.arrow = arrow;
        }

        @Override
        public String dateMenu() {
            return "menu--date";
        }

        @Override
        public String menu() {
            if(arrow == MenuArrow.TOP) {
                return "menu menu--arrow-top";
            } else {
                return "menu";
            }
        }

        @Override
        public String menuList() {
            return "menu__list";
        }

        @Override
        public String menuListItemIndent() {
            return "menu__list__item--indent";
        }

        @Override
        public String menuRadioGroup() {
            return "menu__radio-group";
        }

        @Override
        public String menuScroller() {
            return "menu__scroller";
        }

        @Override
        public String menuScrollerActive() {
            return "menu__scroller--active";
        }

        @Override
        public String menuScrollerBottom() {
            return "menu__scroller__bottom";
        }

        @Override
        public String menuScrollerTop() {
            return "menu__scroller__top";
        }

        @Override
        public String noSeparator() {
            return "menu--no-separator";
        }

        @Override
        public String plain() {
            return "menu--plain";
        }

        @Override
        public boolean ensureInjected() {
            return false;
        }

        @Override
        public String getText() {
            return "";
        }

        @Override
        public String getName() {
            return "menu";
        }
    }


    private static class Resources implements MenuBaseAppearance.MenuResources {

        private final MenuBaseAppearance.MenuStyle style;

        public Resources(MenuArrow arrow) {
            style = new Style(arrow);
        }

        @Override
        public MenuBaseAppearance.MenuStyle style() {
            return style;
        }
    }

    public Css3MenuAppearance() {
        this(MenuArrow.TOP);
    }

    public Css3MenuAppearance(MenuArrow arrow) {
        super(new Resources(arrow), com.google.gwt.core.client.GWT.create(MenuBaseAppearance.BaseMenuTemplate.class));
    }
}