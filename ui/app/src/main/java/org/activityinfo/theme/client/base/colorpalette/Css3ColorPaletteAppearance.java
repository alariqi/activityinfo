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
package org.activityinfo.theme.client.base.colorpalette;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.sencha.gxt.theme.base.client.colorpalette.ColorPaletteBaseAppearance;
import com.sencha.gxt.themebuilder.base.client.config.ThemeDetails;

public class Css3ColorPaletteAppearance extends ColorPaletteBaseAppearance {
  public interface Css3ColorPaletteResources extends ColorPaletteResources, ClientBundle {
    @Override
    @Source({"com/sencha/gxt/theme/base/client/colorpalette/ColorPalette.gss", "Css3ColorPalette.gss"})
    Css3ColorPaletteStyle style();

    ThemeDetails theme();
  }

  public interface Css3ColorPaletteStyle extends ColorPaletteStyle {

  }

  public Css3ColorPaletteAppearance() {
    this(GWT.<Css3ColorPaletteResources>create(Css3ColorPaletteResources.class));
  }

  public Css3ColorPaletteAppearance(Css3ColorPaletteResources resources) {
    this(resources, GWT.<BaseColorPaletteTemplate>create(BaseColorPaletteTemplate.class));
  }

  public Css3ColorPaletteAppearance(ColorPaletteResources resources, BaseColorPaletteTemplate template) {
    super(resources, template);
  }
}