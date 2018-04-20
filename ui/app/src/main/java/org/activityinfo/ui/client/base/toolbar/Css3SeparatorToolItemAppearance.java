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
package org.activityinfo.ui.client.base.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.themebuilder.base.client.config.ThemeDetails;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem.SeparatorToolItemAppearance;

/**
 *
 */
public class Css3SeparatorToolItemAppearance implements SeparatorToolItemAppearance {

  public interface Css3SeparatorToolItemResources extends ClientBundle {
    @Source("Css3SeparatorToolItem.gss")
    Css3SeparatorToolItemStyle style();

    ThemeDetails theme();
  }

  public interface Css3SeparatorToolItemStyle extends CssResource {
    String separator();
  }

  public interface Css3SeparatorToolItemTemplate extends XTemplates {
    @XTemplate("<div class=\"{style.separator}\"></div>")
    SafeHtml render(Css3SeparatorToolItemStyle style);
  }


  private final Css3SeparatorToolItemStyle style;
  private final Css3SeparatorToolItemTemplate template;

  public Css3SeparatorToolItemAppearance() {
    this(GWT.<Css3SeparatorToolItemResources>create(Css3SeparatorToolItemResources.class));
  }

  public Css3SeparatorToolItemAppearance(Css3SeparatorToolItemResources resources) {
    this(resources, GWT.<Css3SeparatorToolItemTemplate>create(Css3SeparatorToolItemTemplate.class));
  }

  public Css3SeparatorToolItemAppearance(Css3SeparatorToolItemResources resources, Css3SeparatorToolItemTemplate template) {
    this.style = resources.style();
    this.template = template;

    StyleInjectorHelper.ensureInjected(style, true);
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(style));
  }
}
