package org.activityinfo.ui.client.base.button;


import com.google.gwt.dom.client.Element;
import com.sencha.gxt.cell.core.client.ButtonCell;
import com.sencha.gxt.core.client.dom.XElement;

public class MenuButtonCell extends ButtonCell<String> {

    private XElement menuParent;

    public MenuButtonCell() {
        super(new MenuButtonAppearance());
    }

    @Override
    public void showMenu(Element target) {
        super.showMenu(target);
        menuParent = target.cast();
        getAppearance().getButtonElement(menuParent).addClassName("button--active");
    }

    @Override
    public void hideMenu() {
        super.hideMenu();
        getAppearance().getButtonElement(menuParent).removeClassName("button--active");
    }
}
