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
package org.activityinfo.ui.client.base.progressbar;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.cell.core.client.ProgressBarCell.ProgressBarAppearance;
import com.sencha.gxt.cell.core.client.ProgressBarCell.ProgressBarAppearanceOptions;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.themebuilder.base.client.config.ThemeDetails;

public class Css3ProgressBarAppearance implements ProgressBarAppearance {
  public interface Css3ProgressBarResources extends ClientBundle {

    @Source("Css3ProgressBar.gss")
    Css3ProgressBarStyles styles();

    ThemeDetails theme();
  }

  public interface Css3ProgressBarStyles extends CssResource {
    String wrap();

    String bar();

    String text();

    String textBack();
  }

  public interface Css3ProgressBarTemplate extends XTemplates {
    @XTemplate(source = "Css3ProgressBar.html")
    SafeHtml render(SafeHtml html, Css3ProgressBarStyles style, SafeStyles wrapStyles, SafeStyles progressBarStyles, SafeStyles progressTextStyles, SafeStyles widthStyles);
  }

  private final Css3ProgressBarStyles styles;
  private final Css3ProgressBarTemplate template;

  public Css3ProgressBarAppearance() {
    this(GWT.<Css3ProgressBarResources>create(Css3ProgressBarResources.class));
  }

  public Css3ProgressBarAppearance(Css3ProgressBarResources resources) {
    this(resources, GWT.<Css3ProgressBarTemplate>create(Css3ProgressBarTemplate.class));
  }

  public Css3ProgressBarAppearance(Css3ProgressBarResources resources, Css3ProgressBarTemplate template) {
    styles = resources.styles();
    StyleInjectorHelper.ensureInjected(styles, false);
    this.template = template;
  }

  @Override
  public void render(SafeHtmlBuilder sb, Double value, ProgressBarAppearanceOptions options) {
    value = value == null ? 0 : value;

    String text = options.getProgressText();

    if (text != null) {
      int v = (int) Math.round(value * 100);
      text = Format.substitute(text, v);
    }

    SafeHtml txt = Util.isEmptyString(text) ? Util.NBSP_SAFE_HTML : SafeHtmlUtils.fromString(text);

    SafeStyles widthStyles = SafeStylesUtils.fromTrustedNameAndValue("width", options.getWidth() + "px");

    final SafeStyles progressBarStyles;
    if (value <= 0) {
      progressBarStyles = SafeStylesUtils.fromTrustedNameAndValue("visibility", "hidden");
    } else {
      progressBarStyles = SafeStylesUtils.fromTrustedNameAndValue("width", value * 100 + "%");
    }


    sb.append(template.render(txt, styles, null, progressBarStyles, null, widthStyles));

  }
}
