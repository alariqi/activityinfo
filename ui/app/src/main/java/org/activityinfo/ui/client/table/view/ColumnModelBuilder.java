/*
 * ActivityInfo
 * Copyright (C) 2009-2013 UNICEF
 * Copyright (C) 2014-2018 BeDataDriven Groep B.V.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.activityinfo.ui.client.table.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.HeaderGroupConfig;
import org.activityinfo.analysis.table.*;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.ui.client.table.viewModel.TableViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Constructs a GXT Grid column model from our EffectiveTableModel.
 */
public class ColumnModelBuilder {

    interface Templates extends XTemplates {

        @XTemplate("<h4>{subform}</h4>{name}")
        SafeHtml subFormHeader(String subform, String name);
    }

    private final Templates TEMPLATES = GWT.create(Templates.class);

    private final ColumnSetProxy proxy;
    private final List<ColumnConfig<Integer, ?>> columnConfigs = new ArrayList<>();
    private final List<HeaderGroupConfig> headerGroupConfigs = new ArrayList<>();


    public ColumnModelBuilder(ColumnSetProxy proxy) {
        this.proxy = proxy;
    }

    /**
     * Add loading place holder columns
     */
    public void addLoadingColumns() {
        ValueProvider<Integer, String> valueProvider = new ValueProvider<Integer, String>() {
            @Override
            public String getValue(Integer object) {
                return null;
            }

            @Override
            public void setValue(Integer object, String value) {
            }

            @Override
            public String getPath() {
                return "loading";
            }
        };

        for (int i = 0; i < 5; i++) {
            ColumnConfig<Integer, String> config = new ColumnConfig<>(valueProvider, 100);
            config.setHeader("Loading...");
            columnConfigs.add(config);
        }
    }

    public void addAll(List<EffectiveTableColumn> columns) {

        for (EffectiveTableColumn tableColumn : columns) {

            tableColumn.accept(new TableColumnVisitor<Void>() {
                @Override
                public Void visitTextColumn(EffectiveTableColumn columnModel, TextFormat textFormat) {
                    addTextColumn(tableColumn, textFormat);
                    return null;
                }

                @Override
                public Void visitNumberColumn(EffectiveTableColumn columnModel, NumberFormat numberFormat) {
                    addNumberColumn(tableColumn, numberFormat);
                    return null;
                }

                @Override
                public Void visitErrorColumn(EffectiveTableColumn columnModel, ErrorFormat errorFormat) {
                    addErrorColumn(tableColumn, errorFormat);
                    return null;
                }

                @Override
                public Void visitGeoPointColumn(EffectiveTableColumn columnModel, GeoPointFormat geoPointFormat) {
                    addGeoPointColumn(columnModel, geoPointFormat);
                    return null;
                }

                @Override
                public Void visitMultiEnumColumn(EffectiveTableColumn columnModel, MultiEnumFormat multiEnumFormat) {
                    addMultiEnumColumn(columnModel, multiEnumFormat);
                    return null;
                }

                @Override
                public Void visitBooleanColumn(EffectiveTableColumn columnModel, BooleanFormat booleanFormat) {
                    return null;
                }

                @Override
                public Void visitDateColumn(EffectiveTableColumn columnModel, DateFormat dateFormat) {
                    addDateColumn(columnModel, dateFormat);
                    return null;
                }

                @Override
                public Void visitSingleEnumColumn(EffectiveTableColumn columnModel, SingleEnumFormat singleEnumFormat) {
                    addEnumType(columnModel, singleEnumFormat);
                    return null;
                }

                @Override
                public Void visitSubFormColumn(EffectiveTableColumn columnModel, SubFormFormat subFormFormat) {
                    addSubFormColumn(columnModel, subFormFormat);
                    return null;
                }

            });

        }
    }

    private void addTextColumn(EffectiveTableColumn tableColumn, TextFormat textFormat) {
        ValueProvider<Integer, String> valueProvider = proxy.getValueProvider(textFormat);

        ColumnConfig<Integer, String> config = new ColumnConfig<>(valueProvider, tableColumn.getWidth());
        config.setHeader(tableColumn.getLabel());
        config.setVerticalHeaderAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
        columnConfigs.add(config);
    }


    private void addNumberColumn(EffectiveTableColumn tableColumn, NumberFormat numberFormat) {
        ValueProvider<Integer, Double> valueProvider = proxy.getValueProvider(numberFormat);

        ColumnConfig<Integer, Double> config = new ColumnConfig<>(valueProvider, tableColumn.getWidth());
        config.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        config.setHeader(tableColumn.getLabel());
        config.setVerticalHeaderAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
        columnConfigs.add(config);
    }

    private void addEnumType(EffectiveTableColumn tableColumn, SingleEnumFormat format) {

        ValueProvider<Integer, String> valueProvider = proxy.getValueProvider(format);

        ColumnConfig<Integer, String> config = new ColumnConfig<>(valueProvider, tableColumn.getWidth());
        config.setHeader(tableColumn.getLabel());
        config.setVerticalHeaderAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
        columnConfigs.add(config);

    }

    private void addDateColumn(EffectiveTableColumn tableColumn, DateFormat dateFormat) {
        ValueProvider<Integer, String> valueProvider = proxy.getValueProvider(dateFormat.asTextFormat());
        ColumnConfig<Integer, String> config = new ColumnConfig<>(valueProvider, tableColumn.getWidth());
        config.setVerticalHeaderAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
        config.setHeader(tableColumn.getLabel());
        columnConfigs.add(config);
    }


    private void addMultiEnumColumn(EffectiveTableColumn tableColumn, MultiEnumFormat multiEnumFormat) {
        // Add a single, comma-delimited list for now
        ValueProvider<Integer, String> valueProvider = proxy.getValueProvider(tableColumn.getId(), multiEnumFormat.createRenderer());

        ColumnConfig<Integer, String> config = new ColumnConfig<>(valueProvider, tableColumn.getWidth());
        config.setHeader(tableColumn.getLabel());
        config.setVerticalHeaderAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
        columnConfigs.add(config);
    }

    private void addGeoPointColumn(EffectiveTableColumn columnModel, GeoPointFormat format) {
        // Add a single, comma-delimited list for now
        ValueProvider<Integer, Double> latProvider =
                proxy.getValueProvider(format.getLatitudeId(), format.createLatitudeRenderer());
        ValueProvider<Integer, Double> lngProvider =
                proxy.getValueProvider(format.getLongitudeId(), format.createLongitudeRenderer());

        int latitudeColumnIndex = columnConfigs.size();

        ColumnConfig<Integer, Double> latitudeConfig = new ColumnConfig<>(latProvider);
        latitudeConfig.setHeader(I18N.CONSTANTS.latitude());
        latitudeConfig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        latitudeConfig.setVerticalHeaderAlignment(HasVerticalAlignment.ALIGN_BOTTOM);

        columnConfigs.add(latitudeConfig);

        ColumnConfig<Integer, Double> longitudeConfig = new ColumnConfig<>(lngProvider);
        longitudeConfig.setHeader(I18N.CONSTANTS.longitude());
        longitudeConfig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        longitudeConfig.setVerticalHeaderAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
        columnConfigs.add(longitudeConfig);

        HeaderGroupConfig groupConfig = new HeaderGroupConfig(SafeHtmlUtils.fromString(columnModel.getLabel()), 1, 2);
        groupConfig.setRow(0);
        groupConfig.setColumn(latitudeColumnIndex);

        headerGroupConfigs.add(groupConfig);
    }


    private void addSubFormColumn(EffectiveTableColumn columnModel, SubFormFormat format) {
        ValueProvider<Integer, Integer> countProvider =
                proxy.getValueProvider(format.getId(), format.createRenderer());

        ValueProvider<Integer, String> idProvider =
                proxy.getValueProvider(TableViewModel.ID_COLUMN_ID, new StringRenderer(TableViewModel.ID_COLUMN_ID));

        ColumnConfig<Integer, Integer> columnConfig = new ColumnConfig<>(new IdentityValueProvider<>());
        columnConfig.setColumnHeaderClassName("columnheader__header--subform");
        columnConfig.setHeader(TEMPLATES.subFormHeader(I18N.CONSTANTS.subForm(), columnModel.getLabel()));
        columnConfig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        columnConfig.setCellClassName("tablegrid__cell--subform");
        columnConfig.setCell(new SubFormCell(format.getSubFormId(), idProvider, countProvider));
        columnConfig.setVerticalHeaderAlignment(HasVerticalAlignment.ALIGN_BOTTOM);

        columnConfigs.add(columnConfig);

    }

    private void addErrorColumn(EffectiveTableColumn tableColumn, ErrorFormat errorFormat) {
        ValueProvider<Integer, String> valueProvider = new ValueProvider<Integer, String>() {
            @Override
            public String getValue(Integer object) {
                return null;
            }

            @Override
            public void setValue(Integer object, String value) {
            }

            @Override
            public String getPath() {
                return tableColumn.getId();
            }
        };

        ColumnConfig<Integer, String> config = new ColumnConfig<>(valueProvider, tableColumn.getWidth());
        config.setHeader(tableColumn.getLabel());
        config.setCell(new ErrorCell());
        config.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LOCALE_END);
        config.setVerticalHeaderAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
        columnConfigs.add(config);
    }


    public ColumnModel<Integer> buildColumnModel() {
        ColumnModel<Integer> cm = new ColumnModel<>(columnConfigs);

        for (HeaderGroupConfig headerGroupConfig : headerGroupConfigs) {
            cm.addHeaderGroup(headerGroupConfig.getRow(), headerGroupConfig.getColumn(), headerGroupConfig);
        }

        return cm;
    }

}
