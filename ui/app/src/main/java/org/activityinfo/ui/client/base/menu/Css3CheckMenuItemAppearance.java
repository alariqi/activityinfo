
package org.activityinfo.ui.client.base.menu;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.menu.CheckMenuItem.CheckMenuItemAppearance;
import org.activityinfo.ui.client.base.tablegrid.BlankImageResource;

public class Css3CheckMenuItemAppearance extends Css3MenuItemAppearance implements CheckMenuItemAppearance {

  private static final ImageResource DUMMY_CHECKED_IMAGE = new BlankImageResource(0,0 );

  private static final ImageResource UNCHECKED_IMAGE = new BlankImageResource(0, 0);

  private static final ImageResource RADIO_IMAGE = new BlankImageResource(0, 0);


  @Override
  public void render(SafeHtmlBuilder result) {
    result.append(MenuTemplates.TEMPLATES.checkMenuItem());
  }

  @Override
  public void applyChecked(XElement parent, boolean state) {
    parent.setClassName("menu__item--checked", state);
    parent.setClassName("menu__item--unchecked", !state);
  }

  @Override
  public XElement getCheckIcon(XElement parent) {
    return parent.selectNode("svg");
  }

  @Override
  public ImageResource checked() {
    return DUMMY_CHECKED_IMAGE;
  }

  @Override
  public ImageResource unchecked() {
    return UNCHECKED_IMAGE;
  }

  @Override
  public ImageResource radio() {
    return RADIO_IMAGE;
  }
}
