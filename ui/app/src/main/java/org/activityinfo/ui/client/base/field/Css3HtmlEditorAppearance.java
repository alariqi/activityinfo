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
package org.activityinfo.ui.client.base.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.theme.base.client.field.HtmlEditorDefaultAppearance;
import com.sencha.gxt.themebuilder.base.client.config.ThemeDetails;


public class Css3HtmlEditorAppearance extends HtmlEditorDefaultAppearance {

  public interface Css3HtmlEditorResources extends HtmlEditorResources {

    @Source({"com/sencha/gxt/theme/base/client/field/HtmlEditor.gss", "Css3HtmlEditor.gss"})
    @Override
    Css3HtmlEditorStyle css();

    ThemeDetails theme();

    ImageResource bold();

    ImageResource fontColor();

    ImageResource fontDecrease();

    ImageResource fontHighlight();

    ImageResource fontIncrease();

    ImageResource italic();

    ImageResource justifyCenter();

    ImageResource justifyLeft();

    ImageResource justifyRight();

    ImageResource link();

    ImageResource ol();

    ImageResource source();

    ImageResource ul();

    ImageResource underline();
  }

  public interface Css3HtmlEditorStyle extends HtmlEditorStyle {
  }

  public Css3HtmlEditorAppearance() {
    this(GWT.<Css3HtmlEditorResources>create(Css3HtmlEditorResources.class));
  }

  public Css3HtmlEditorAppearance(Css3HtmlEditorResources resources) {
    super(resources);
  }
}
