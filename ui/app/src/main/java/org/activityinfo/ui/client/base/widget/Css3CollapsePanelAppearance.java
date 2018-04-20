/**
 * Sencha GXT 4.0.0 - Sencha for GWT
 * Copyright (c) 2006-2015, Sencha Inc.
 *
 * licensing@sencha.com
 * http://www.sencha.com/products/gxt/license/
 *
 * ================================================================================
 * Open Source License
 * ================================================================================
 * This version of Sencha GXT is licensed under the terms of the Open Source GPL v3
 * license. You may use this license only if you are prepared to distribute and
 * share the source code of your application under the GPL v3 license:
 * http://www.gnu.org/licenses/gpl.html
 *
 * If you are NOT prepared to distribute and share the source code of your
 * application under the GPL v3 license, other commercial and oem licenses
 * are available for an alternate download of Sencha GXT.
 *
 * Please see the Sencha GXT Licensing page at:
 * http://www.sencha.com/products/gxt/license/
 *
 * For clarification or additional options, please contact:
 * licensing@sencha.com
 * ================================================================================
 *
 *
 * ================================================================================
 * Disclaimer
 * ================================================================================
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 * ================================================================================
 */
package org.activityinfo.ui.client.base.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.Style.LayoutRegion;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.themebuilder.base.client.config.ThemeDetails;
import com.sencha.gxt.widget.core.client.CollapsePanel.CollapsePanelAppearance;

/**
 *
 */
public class Css3CollapsePanelAppearance implements CollapsePanelAppearance {

  public interface Css3CollapsePanelResources extends ClientBundle {

    @Source("Css3CollapsePanel.gss")
    Css3CollapsePanelStyle style();

    ThemeDetails theme();
  }

  public interface Css3CollapsePanelStyle extends CssResource {
    String panel();

    String noHeader();

    String iconWrap();

    String textWrap();

    String west();

    String east();

    String north();

    String south();
  }

  private final Css3CollapsePanelResources resources;
  private final Css3CollapsePanelStyle style;

  public Css3CollapsePanelAppearance() {
    this(GWT.<Css3CollapsePanelResources>create(Css3CollapsePanelResources.class));
  }

  public Css3CollapsePanelAppearance(Css3CollapsePanelResources resources) {
    this.resources = resources;
    this.style = this.resources.style();

    StyleInjectorHelper.ensureInjected(style, true);
  }


  @Override
  public void render(SafeHtmlBuilder sb, LayoutRegion region, boolean header) {
    String cls = style.panel();

    switch (region) {
      case WEST:
        cls += " " + style.west();
        break;
      case EAST:
        cls += " " + style.east();
        break;
      case NORTH:
        cls += " " + style.north();
        break;
      case SOUTH:
        cls += " " + style.south();
        break;
    }

    if (!header) {
      cls += " " + style.noHeader();
    }

    sb.appendHtmlConstant("<div class='" + cls + "'>");
    sb.appendHtmlConstant("<div class='" + style.iconWrap() + "'></div>");
    sb.appendHtmlConstant("<div class='" + style.textWrap() + "'></div>");
    sb.appendHtmlConstant("</div>");
  }

  @Override
  public XElement iconWrap(XElement parent) {
    return parent.selectNode("." + style.iconWrap()).cast();
  }

  @Override
  public XElement textWrap(XElement parent) {
    return parent.selectNode("." + style.textWrap()).cast();
  }

}
