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
package org.activityinfo.ui.client.base.fieldset;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.theme.base.client.field.FieldSetDefaultAppearance;
import com.sencha.gxt.themebuilder.base.client.config.ThemeDetails;
import com.sencha.gxt.widget.core.client.button.IconButton;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;

public class Css3FieldSetAppearance extends FieldSetDefaultAppearance {
  public interface Css3FieldSetResources extends FieldSetResources {
    @Source({"com/sencha/gxt/theme/base/client/field/FieldSet.gss", "Css3FieldSet.gss"})
    @Override
    Css3FieldSetStyle css();

    ThemeDetails theme();

    ImageResource collapseIcon();

    ImageResource collapseOverIcon();

    ImageResource expandIcon();

    ImageResource expandOverIcon();
  }

  public interface Css3FieldSetStyle extends FieldSetStyle {
    String collapseIcon();

    String collapseOverIcon();

    String expandIcon();

    String expandOverIcon();
  }

  private Css3FieldSetResources resources;

  public Css3FieldSetAppearance() {
    this(GWT.<Css3FieldSetResources>create(Css3FieldSetResources.class));
  }

  public Css3FieldSetAppearance(Css3FieldSetResources resources) {
    super(resources);
    this.resources = resources;
  }

  @Override
  public IconConfig collapseIcon() {
    return new IconConfig(resources.css().collapseIcon(), resources.css().collapseOverIcon());
  }

  @Override
  public IconButton.IconConfig expandIcon() {
    return new IconConfig(resources.css().expandIcon(), resources.css().expandOverIcon());
  }
}
