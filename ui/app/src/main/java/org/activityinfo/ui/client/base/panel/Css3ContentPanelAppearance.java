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
package org.activityinfo.ui.client.base.panel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.core.client.util.Size;
import com.sencha.gxt.themebuilder.base.client.config.ThemeDetails;
import com.sencha.gxt.widget.core.client.ContentPanel.ContentPanelAppearance;
import com.sencha.gxt.widget.core.client.Header;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.ToolButton;

/**
 */
public class Css3ContentPanelAppearance implements ContentPanelAppearance {

  public interface Css3ContentPanelResources extends ClientBundle {
    @Source("Css3ContentPanel.gss")
    Css3ContentPanelStyle style();

    ThemeDetails theme();
  }

  public interface ThemeDetailsBundle extends ClientBundle {

    ThemeDetails theme();
  }

  public interface Css3ContentPanelStyle extends CssResource {
    String body();

    String bodyWrap();

    String footer();

    String header();

    String panel();
  }

  public interface Css3ContentPanelTemplate extends XTemplates {
    @XTemplate(source = "Css3ContentPanel.html")
    SafeHtml render(Css3ContentPanelStyle style);
  }

  protected Css3ContentPanelTemplate template;
  protected final Css3ContentPanelStyle style;
  protected final Css3ContentPanelResources resources;
  protected final ThemeDetails theme;

  public Css3ContentPanelAppearance() {
    this(GWT.<Css3ContentPanelResources> create(Css3ContentPanelResources.class));
  }

  public Css3ContentPanelAppearance(Css3ContentPanelResources resources) {
    this(resources, GWT.<Css3ContentPanelTemplate> create(Css3ContentPanelTemplate.class));
  }

  public Css3ContentPanelAppearance(Css3ContentPanelResources resources, Css3ContentPanelTemplate template) {
    this.resources = resources;
    this.style = this.resources.style();

    StyleInjectorHelper.ensureInjected(this.style, true);
    this.template = template;
    
    ThemeDetailsBundle bundle = GWT.create(ThemeDetailsBundle.class);
    this.theme = bundle.theme();
  }

  @Override
  public Header.HeaderAppearance getHeaderAppearance() {
    return new Css3HeaderAppearance();
  }

  @Override
  public void onHideHeader(XElement parent, boolean hide) {
    parent.selectNode("." + style.header()).setVisible(!hide);
  }

  @Override
  public void onBodyBorder(XElement parent, boolean border) {
    getContentElem(parent).applyStyles(!border ? "border: 0px" : "border: 1px solid " + ThemeStyles.get().borderColor() + ";");
  }

  @Override
  public XElement getBodyWrap(XElement parent) {
    return parent.selectNode("." + style.bodyWrap());
  }

  @Override
  public XElement getContentElem(XElement parent) {
    return parent.selectNode("." + style.body());
  }

  @Override
  public XElement getFooterElem(XElement parent) {
    return parent.selectNode("." + style.footer());
  }

  @Override
  public int getFrameHeight(XElement parent) {
    return theme.panel().border().top() + theme.panel().border().bottom();
  }

  @Override
  public int getFrameWidth(XElement parent) {
    int adj = parent.getBorders(Style.Side.LEFT, Style.Side.RIGHT);
    return adj + theme.panel().border().left() + theme.panel().border().right();
  }

  @Override
  public XElement getHeaderElem(XElement parent) {
    return parent.selectNode("." + style.header());
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(style));
  }

  @Override
  public IconConfig collapseIcon() {
    return ToolButton.UP;
  }

  @Override
  public IconConfig expandIcon() {
    return ToolButton.DOWN;
  }

  @Override
  public Size getHeaderSize(XElement parent) {
    Element head = parent.getFirstChildElement();
    return new Size(head.getOffsetWidth(), head.getOffsetHeight());
  }
}
