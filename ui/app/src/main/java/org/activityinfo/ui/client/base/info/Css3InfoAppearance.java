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
package org.activityinfo.ui.client.base.info;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.themebuilder.base.client.config.ThemeDetails;
import com.sencha.gxt.widget.core.client.info.Info.InfoAppearance;

public class Css3InfoAppearance implements InfoAppearance {
  interface Template extends XTemplates {
    @XTemplate("<div class='{infoWrap}'><div class='{info}'></div></div>")
    SafeHtml render(Styles styles);
  }
  interface Styles extends CssResource {
    String infoWrap();
    String info();
  }
  interface Resources extends ClientBundle {

    @Source("info.gss")
    Styles info();
    ThemeDetails theme();
  }

  private final Template template;
  private final Styles styles;

  public Css3InfoAppearance() {
    this(GWT.<Resources>create(Resources.class));

  }

  public Css3InfoAppearance(Resources resources) {
    this(resources, GWT.<Template>create(Template.class));
  }

  public Css3InfoAppearance(Resources resources, Template template) {
    this.template = template;
    this.styles = resources.info();
    StyleInjectorHelper.ensureInjected(styles, true);
  }

  @Override
  public XElement getContentElement(XElement parent) {
    return parent.getFirstChildElement().getFirstChildElement().cast();
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(styles));
  }
}
