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
package org.activityinfo.theme.client.base.grid;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.resources.client.CssResource.Import;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.grid.GridView.GridStateStyles;
import com.sencha.gxt.widget.core.client.grid.GroupSummaryView.GroupSummaryViewAppearance;
import com.sencha.gxt.widget.core.client.grid.GroupSummaryView.GroupSummaryViewStyle;

public class Css3GroupSummaryViewAppearance extends Css3GroupingViewAppearance implements GroupSummaryViewAppearance {

  public interface Css3GroupSummaryResources extends Css3GroupingViewResources {
    @Override
    @Import(GridStateStyles.class)
    @Source({"Css3GroupingView.gss", "Css3GroupSummaryView.gss"})
    Css3GroupSummaryStyles style();
  }

  public interface Css3GroupSummaryStyles extends Css3GroupingViewStyle, GroupSummaryViewStyle {
    String hideSummaries();
  }

  public Css3GroupSummaryViewAppearance() {
    this(GWT.<Css3GroupSummaryResources>create(Css3GroupSummaryResources.class));
  }

  public Css3GroupSummaryViewAppearance(Css3GroupSummaryResources resources) {
    super(resources);
  }

  @Override
  public void toggleSummaries(XElement parent, boolean visible) {
    parent.setClassName(style().hideSummaries(), !visible);
  }

  @Override
  public NodeList<Element> getSummaries(XElement table) {
    return table.select("." + style().summaryRow());
  }

  @Override
  public int getGroupIndex(XElement group) {
    return group.getParentElement().<XElement>cast().indexOf(group) / 3;
  }

  @Override
  public Css3GroupSummaryStyles style() {
    return (Css3GroupSummaryStyles) super.style();
  }
}
