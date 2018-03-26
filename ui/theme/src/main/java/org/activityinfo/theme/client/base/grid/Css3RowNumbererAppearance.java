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

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.Import;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.themebuilder.base.client.config.ThemeDetails;
import com.sencha.gxt.widget.core.client.grid.GridView.GridStateStyles;
import com.sencha.gxt.widget.core.client.grid.RowNumberer.RowNumbererAppearance;

public class Css3RowNumbererAppearance implements RowNumbererAppearance {

  public interface RowNumbererResources extends ClientBundle {
    @Import(GridStateStyles.class)
    @Source("Css3RowNumberer.gss")
    RowNumbererStyles styles();

    //to be placed in sliced impl after sliced job is created
//    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
//    ImageResource specialColumn();
//
//    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
//    ImageResource specialColumnSelected();

    ThemeDetails theme();
  }
  public interface RowNumbererStyles extends CssResource{
    String numberer();
    String cell();
  }

  private final RowNumbererResources resources;

  public Css3RowNumbererAppearance() {
    this(GWT.<RowNumbererResources>create(RowNumbererResources.class));
  }

  public Css3RowNumbererAppearance(RowNumbererResources resources) {
    this.resources = resources;
    StyleInjectorHelper.ensureInjected(resources.styles(), true);
  }

  @Override
  public String getCellClassName() {
    return resources.styles().cell();
  }

  @Override
  public void renderCell(int rowNumber, SafeHtmlBuilder sb) {
    sb.appendHtmlConstant("<div class='"+resources.styles().numberer()+"'>").append(rowNumber).appendHtmlConstant("</div>");
  }

  @Override
  public SafeHtml renderHeader() {
    return SafeHtmlUtils.EMPTY_SAFE_HTML;
  }
}