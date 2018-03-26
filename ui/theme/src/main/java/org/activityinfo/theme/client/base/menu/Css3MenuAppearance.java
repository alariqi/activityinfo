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
package org.activityinfo.theme.client.base.menu;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.theme.base.client.menu.MenuBaseAppearance;
import com.sencha.gxt.themebuilder.base.client.config.ThemeDetails;

public class Css3MenuAppearance extends MenuBaseAppearance {
  public interface Css3MenuResources extends MenuResources, ClientBundle {
    @Override
    @Source("Css3Menu.gss")
    Css3MenuStyle style();

    ImageResource miniTop();

    ImageResource miniBottom();

    ThemeDetails theme();
  }

  public interface Css3MenuStyle extends MenuStyle {

  }

  public Css3MenuAppearance() {
    this(GWT.<Css3MenuResources>create(Css3MenuResources.class));
  }

  public Css3MenuAppearance(Css3MenuResources resources) {
    this(resources, GWT.<BaseMenuTemplate>create(MenuBaseAppearance.BaseMenuTemplate.class));
  }

  public Css3MenuAppearance(Css3MenuResources resources, BaseMenuTemplate template) {
    super(resources, template);
  }
}
