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

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.HeaderGroupConfig;
import org.activityinfo.analysis.table.*;
import org.activityinfo.i18n.shared.I18N;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Constructs a GXT Grid column model from our EffectiveTableModel.
 */
public class ColumnModelBuilder {

    private final ColumnSetProxy proxy;
    private final List<ColumnConfig<Integer, ?>> columnConfigs = new ArrayList<>();
    private final List<HeaderGroupConfig> headerGroupConfigs = new ArrayList<>();
    private final List<ColumnView> filters = new ArrayList<>();

    public ColumnModelBuilder(ColumnSetProxy proxy) {
        this.proxy = proxy;
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
            });

        }
    }

    private void addTextColumn(EffectiveTableColumn tableColumn, TextFormat textFormat) {
        ValueProvider<Integer, String> valueProvider = proxy.getValueProvider(textFormat);

        ColumnConfig<Integer, String> config = new ColumnConfig<>(valueProvider, tableColumn.getWidth());
        config.setHeader(tableColumn.getLabel());
        columnConfigs.add(config);
    }


    private void addNumberColumn(EffectiveTableColumn tableColumn, NumberFormat numberFormat) {
        ValueProvider<Integer, Double> valueProvider = proxy.getValueProvider(numberFormat);

        ColumnConfig<Integer, Double> config = new ColumnConfig<>(valueProvider, tableColumn.getWidth());
        config.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        config.setHeader(tableColumn.getLabel());
        columnConfigs.add(config);
    }

    private void addEnumType(EffectiveTableColumn tableColumn, SingleEnumFormat format) {

        ValueProvider<Integer, String> valueProvider = proxy.getValueProvider(format);

        ColumnConfig<Integer, String> config = new ColumnConfig<>(valueProvider, tableColumn.getWidth());
        config.setHeader(tableColumn.getLabel());
        columnConfigs.add(config);

    }

    private void addDateColumn(EffectiveTableColumn tableColumn, DateFormat dateFormat) {
        ValueProvider<Integer, Date> valueProvider = proxy.getValueProvider(dateFormat);

        ColumnConfig<Integer, Date> config = new ColumnConfig<>(valueProvider, tableColumn.getWidth());
        config.setHeader(tableColumn.getLabel());
        config.setCell(new DateCell(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT)));
        config.setMenuDisabled(true);
        columnConfigs.add(config);
    }


    private void addMultiEnumColumn(EffectiveTableColumn tableColumn, MultiEnumFormat multiEnumFormat) {
        // Add a single, comma-delimited list for now
        ValueProvider<Integer, String> valueProvider = proxy.getValueProvider(tableColumn.getId(), multiEnumFormat.createRenderer());

        ColumnConfig<Integer, String> config = new ColumnConfig<>(valueProvider, tableColumn.getWidth());
        config.setHeader(tableColumn.getLabel());
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
        columnConfigs.add(latitudeConfig);

        ColumnConfig<Integer, Double> longitudeConfig = new ColumnConfig<>(lngProvider);
        longitudeConfig.setHeader(I18N.CONSTANTS.longitude());
        longitudeConfig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        columnConfigs.add(longitudeConfig);

        HeaderGroupConfig groupConfig = new HeaderGroupConfig(SafeHtmlUtils.fromString(columnModel.getLabel()), 1, 2);
        groupConfig.setRow(0);
        groupConfig.setColumn(latitudeColumnIndex);

        headerGroupConfigs.add(groupConfig);
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
