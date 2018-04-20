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
package org.activityinfo.ui.client.base.tips;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.themebuilder.base.client.config.ThemeDetails;
import com.sencha.gxt.widget.core.client.tips.Tip.TipAppearance;

public class Css3TipAppearance implements TipAppearance {
  public interface Css3TipResources extends ClientBundle {
    @Source("Css3Tip.gss")
    Css3TipStyle style();

    ImageResource anchorBottom();

    ImageResource anchorLeft();

    ImageResource anchorRight();

    ImageResource anchorTop();

    ThemeDetails theme();
  }
  public interface Css3TipStyle extends CssResource {
    String tip();

    String tipWrap();

    String anchorTop();

    String anchorRight();

    String anchorBottom();

    String anchorLeft();

    String anchor();

    String heading();

    String headingWrap();

    String text();

    String textWrap();

    String tools();
  }

  public interface Css3TipTemplate extends XTemplates {
    @XTemplate("<div class='{style.tipWrap}'><div class='{style.tip}'>" +
        "<div class='{style.tools}'></div>" +
        "<div class='{style.headingWrap}'><span class='{style.heading}'></span></div>" +
        "<div class='{style.textWrap}'><span class='{style.text}'></span></div>" +
        "</div></div>")
    SafeHtml render(Css3TipStyle style);
  }

  private final Css3TipResources resources;
  private final Css3TipStyle style;
  private final Css3TipTemplate template;

  public Css3TipAppearance() {
    this(GWT.<Css3TipResources>create(Css3TipResources.class));
  }

  public Css3TipAppearance(Css3TipResources resources) {
    this(resources, GWT.<Css3TipTemplate>create(Css3TipTemplate.class));
  }

  public Css3TipAppearance(Css3TipResources resources, Css3TipTemplate template) {
    this.template = template;
    this.resources = resources;
    style = resources.style();
    StyleInjectorHelper.ensureInjected(style, true);
  }

  @Override
  public void applyAnchorDirectionStyle(XElement anchorEl, Side anchor) {
    anchorEl.setClassName(style.anchorTop(), anchor == Side.TOP);
    anchorEl.setClassName(style.anchorBottom(), anchor == Side.BOTTOM);
    anchorEl.setClassName(style.anchorRight(), anchor == Side.RIGHT);
    anchorEl.setClassName(style.anchorLeft(), anchor == Side.LEFT);
  }

  @Override
  public void applyAnchorStyle(XElement anchorEl) {
    anchorEl.addClassName(style.anchor());
  }

  public XElement getHeaderElement(XElement parent) {
    return parent.selectNode("." + style.heading());
  }

  @Override
  public XElement getBodyElement(XElement parent) {
    return parent.selectNode("." + style.text());
  }

  @Override
  public XElement getToolsElement(XElement parent) {
    return parent.selectNode("." + style.tools());
  }

  @Override
  public void removeAnchorStyle(XElement anchorEl) {
    anchorEl.removeClassName(style.anchor());
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(style));
  }

  @Override
  public void updateContent(XElement parent, SafeHtml title, SafeHtml body) {
    XElement header = getHeaderElement(parent);
    if (title == SafeHtmlUtils.EMPTY_SAFE_HTML) {
      header.getParentElement().getStyle().setDisplay(Display.NONE);
    } else {
      header.setInnerSafeHtml(title);
      header.getParentElement().getStyle().clearDisplay();
    }

    getBodyElement(parent).setInnerSafeHtml(body);
  }
}
