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
package org.activityinfo.ui.client.base.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.TabPanel.TabPanelBottomAppearance;

public class Css3TabPanelBottomAppearance extends Css3TabPanelAppearance implements TabPanelBottomAppearance {

  public interface Css3TabPanelBottomTemplate extends XTemplates {
    @XTemplate(source = "Css3TabPanelBottom.html")
    SafeHtml render(Css3TabPanelBottomStyle style);
  }

  public interface Css3TabPanelBottomResources extends Css3TabPanelResources {
    @Source("Css3TabPanelBottom.gss")
    @Override
    Css3TabPanelBottomStyle style();
  }

  public interface Css3TabPanelBottomStyle extends Css3TabPanelStyle {
  }

  private final Css3TabPanelBottomTemplate template;

  public Css3TabPanelBottomAppearance() {
    this(GWT.<Css3TabPanelBottomResources>create(Css3TabPanelBottomResources.class));
  }

  public Css3TabPanelBottomAppearance(Css3TabPanelBottomResources resources) {
    this(resources, GWT.<Css3TabPanelBottomTemplate>create(Css3TabPanelBottomTemplate.class));
  }

  public Css3TabPanelBottomAppearance(Css3TabPanelBottomResources resources, Css3TabPanelBottomTemplate template) {
    super(resources);
    this.template = template;
  }

  @Override
  public void render(SafeHtmlBuilder builder) {
    builder.append(template.render((Css3TabPanelBottomStyle) style));
  }

  @Override
  public XElement getBar(XElement parent) {
    return parent.getLastChild().cast();
  }

  public XElement getStrip(XElement parent) {
    return getBar(parent).selectNode("." + style.tabStrip());
  }
}
