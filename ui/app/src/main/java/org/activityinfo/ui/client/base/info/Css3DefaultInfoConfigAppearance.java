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

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.themebuilder.base.client.config.ThemeDetails;
import com.sencha.gxt.widget.core.client.info.DefaultInfoConfig.DefaultInfoConfigAppearance;

public class Css3DefaultInfoConfigAppearance implements DefaultInfoConfigAppearance {
  interface Style extends CssResource {
    String header();

    String message();
  }

  interface Resources extends ClientBundle {
    @Source("infoConfig.gss")
    Style infoConfig();

    ThemeDetails theme();
  }

  interface Template extends XTemplates {
    @XTemplate("<div class='{style.header}'>{title}</div><div class='{style.message}'>{message}</div>")
    SafeHtml render(Style style, SafeHtml title, SafeHtml message);
  }

  private Template template;
  private Style style;

  public Css3DefaultInfoConfigAppearance() {
    this(GWT.<Resources>create(Resources.class));
  }

  public Css3DefaultInfoConfigAppearance(Resources resources) {
    this(resources, GWT.<Template>create(Template.class));
  }

  public Css3DefaultInfoConfigAppearance(Resources resources, Template template) {
    this.template = template;
    this.style = resources.infoConfig();
    StyleInjectorHelper.ensureInjected(style, true);
  }

  @Override
  public void render(SafeHtmlBuilder sb, SafeHtml title, SafeHtml message) {
    sb.append(template.render(style, title, message));
  }
}
