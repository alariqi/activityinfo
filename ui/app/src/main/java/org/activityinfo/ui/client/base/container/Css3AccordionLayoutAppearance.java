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
package org.activityinfo.ui.client.base.container;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.widget.core.client.Header.HeaderAppearance;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.AccordionLayoutAppearance;
import org.activityinfo.ui.client.base.panel.Css3ContentPanelAppearance;
import org.activityinfo.ui.client.base.panel.Css3HeaderAppearance;
import org.activityinfo.ui.client.base.panel.Css3HeaderAppearance.Css3HeaderResources;
import org.activityinfo.ui.client.base.panel.Css3HeaderAppearance.Css3HeaderStyle;

public class Css3AccordionLayoutAppearance extends Css3ContentPanelAppearance implements AccordionLayoutAppearance {

  public interface Css3AccordionResources extends Css3ContentPanelResources {
    @Override
    @Source({"Css3AccordionLayout.gss"})
    Css3AccordionStyle style();
  }

  public interface Css3AccordionStyle extends Css3ContentPanelStyle {

  }

  public interface Css3AccordionHeaderStyle extends Css3HeaderStyle {

  }

  public interface Css3AccordionHeaderResources extends Css3HeaderResources {
    @Override
    @Source({"com/sencha/gxt/theme/base/client/widget/Header.gss", "Css3AccordionLayoutHeader.gss"})
    Css3AccordionHeaderStyle style();
  }

  public Css3AccordionLayoutAppearance() {
    this(GWT.<Css3AccordionResources>create(Css3AccordionResources.class));
  }

  public Css3AccordionLayoutAppearance(Css3AccordionResources resources) {
    this(resources, GWT.<Css3ContentPanelTemplate>create(Css3ContentPanelTemplate.class));
  }

  public Css3AccordionLayoutAppearance(Css3AccordionResources resources, Css3ContentPanelTemplate template) {
    super(resources, template);
  }

  @Override
  public HeaderAppearance getHeaderAppearance() {
    return new Css3HeaderAppearance(GWT.<Css3AccordionHeaderResources>create(Css3AccordionHeaderResources.class));
  }

  @Override
  public IconConfig collapseIcon() {
    return ToolButton.MINUS;
  }

  @Override
  public IconConfig expandIcon() {
    return ToolButton.PLUS;
  }
}
