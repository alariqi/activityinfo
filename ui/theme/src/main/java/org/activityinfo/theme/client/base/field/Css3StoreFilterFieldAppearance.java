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
package org.activityinfo.theme.client.base.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.form.StoreFilterField.StoreFilterFieldAppearance;

/**
 *
 */
public class Css3StoreFilterFieldAppearance extends Css3TriggerFieldAppearance implements StoreFilterFieldAppearance {
  public interface Css3StoreFilterFieldResources extends Css3TriggerFieldResources {
    @Override
    @Source({"Css3ValueBaseField.gss", "Css3TextField.gss", "Css3TriggerField.gss", "Css3StoreFilterField.gss"})
    Css3StoreFilterFieldStyle style();

    @Override
    @Source("clearTrigger.png")
    ImageResource triggerArrow();

    @Override
    @Source("clearTriggerOver.png")
    ImageResource triggerArrowOver();

    @Override
    @Source("clearTriggerClick.png")
    ImageResource triggerArrowClick();
  }

  public interface Css3StoreFilterFieldStyle extends Css3TriggerFieldStyle {

  }

  public Css3StoreFilterFieldAppearance() {
    this(GWT.<Css3StoreFilterFieldResources>create(Css3StoreFilterFieldResources.class));
  }

  public Css3StoreFilterFieldAppearance(Css3StoreFilterFieldResources resources) {
    super(resources);
  }
}
