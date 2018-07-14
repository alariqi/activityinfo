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

import org.activityinfo.analysis.table.*;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.base.datatable.DataTableColumn;
import org.activityinfo.ui.client.base.datatable.RowRange;
import org.activityinfo.ui.client.base.datatable.TableSlice;
import org.activityinfo.ui.client.table.view.columns.*;
import org.activityinfo.ui.client.table.view.columns.ColumnRenderer;
import org.activityinfo.ui.client.table.view.columns.DoubleRenderer;
import org.activityinfo.ui.client.table.viewModel.ColumnHeaderViewModel;
import org.activityinfo.ui.client.table.viewModel.TableViewModel;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.PropMap;
import org.activityinfo.ui.vdom.shared.tree.Props;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Constructs a DataTable from an {@link EffectiveTableModel}
 */
public class DataTableRenderer {


    private final List<DataTableColumn> columns = new ArrayList<>();
    private final List<ColumnRenderer> cells = new ArrayList<>();
    private final TableViewModel tableViewModel;


    public DataTableRenderer(TableViewModel tableViewModel, EffectiveTableModel tableModel) {
        this.tableViewModel = tableViewModel;
        for (EffectiveTableColumn tableColumn : tableModel.getColumns()) {

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

        Observable<ColumnHeaderViewModel> viewModel = tableViewModel.getColumnHeader(textFormat.getFormula());
        DataTableColumn column = ColumnBuilder.build(viewModel)
                .heading(tableColumn.getLabel())
                .build();

        columns.add(column);
        cells.add(new StringColumnRenderer(textFormat.getId()));
    }


    private void addNumberColumn(EffectiveTableColumn tableColumn, NumberFormat numberFormat) {

        Observable<ColumnHeaderViewModel> viewModel = tableViewModel.getColumnHeader(numberFormat.getFormula());
        DataTableColumn column = ColumnBuilder.build(viewModel)
                .heading(tableColumn.getLabel())
                .build();

        columns.add(column);
        cells.add(new DoubleRenderer(numberFormat.getId()));
    }

    private void addEnumType(EffectiveTableColumn tableColumn, SingleEnumFormat format) {

        Observable<ColumnHeaderViewModel> viewModel = tableViewModel.getColumnHeader(format.getFormula());
        DataTableColumn column = ColumnBuilder.build(viewModel)
                .heading(tableColumn.getLabel())
                .build();

        columns.add(column);
        cells.add(new StringColumnRenderer(format.getId()));
    }

    private void addDateColumn(EffectiveTableColumn tableColumn, DateFormat dateFormat) {

        Observable<ColumnHeaderViewModel> viewModel = tableViewModel.getColumnHeader(dateFormat.getFormula());
        DataTableColumn column = ColumnBuilder.build(viewModel)
                .heading(tableColumn.getLabel())
                .build();

        columns.add(column);
        cells.add(new StringColumnRenderer(dateFormat.getId()));
    }


    private void addMultiEnumColumn(EffectiveTableColumn tableColumn, MultiEnumFormat multiEnumFormat) {
//        // Add a single, comma-delimited list for now
//        ValueProvider<Integer, String> valueProvider = proxy.getValueProvider(tableColumn.getId(), multiEnumFormat.createRenderer());
//
//        ColumnConfig<Integer, String> config = new ColumnConfig<>(valueProvider, tableColumn.getWidth());
//        config.setHeader(tableColumn.getLabel());
//        config.setVerticalHeaderAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
//        columnConfigs.add(config);
    }

    private void addGeoPointColumn(EffectiveTableColumn columnModel, GeoPointFormat format) {
//        // Add a single, comma-delimited list for now
//        ValueProvider<Integer, Double> latProvider =
//                proxy.getValueProvider(format.getLatitudeId(), format.createLatitudeRenderer());
//        ValueProvider<Integer, Double> lngProvider =
//                proxy.getValueProvider(format.getLongitudeId(), format.createLongitudeRenderer());
//
//        int latitudeColumnIndex = columnConfigs.size();
//
//        ColumnConfig<Integer, Double> latitudeConfig = new ColumnConfig<>(latProvider);
//        latitudeConfig.setHeader(I18N.CONSTANTS.latitude());
//        latitudeConfig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//        latitudeConfig.setVerticalHeaderAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
//
//        columnConfigs.add(latitudeConfig);
//
//        ColumnConfig<Integer, Double> longitudeConfig = new ColumnConfig<>(lngProvider);
//        longitudeConfig.setHeader(I18N.CONSTANTS.longitude());
//        longitudeConfig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//        longitudeConfig.setVerticalHeaderAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
//        columnConfigs.add(longitudeConfig);
//
//        HeaderGroupConfig groupConfig = new HeaderGroupConfig(SafeHtmlUtils.fromString(columnModel.getLabel()), 1, 2);
//        groupConfig.setRow(0);
//        groupConfig.setColumn(latitudeColumnIndex);
//
//        headerGroupConfigs.add(groupConfig);
    }


    private void addSubFormColumn(EffectiveTableColumn columnModel, SubFormFormat format) {

        Observable<ColumnHeaderViewModel> viewModel = tableViewModel.getColumnHeader(format.getFormula());
        DataTableColumn column = ColumnBuilder.build(viewModel)
                .heading(I18N.CONSTANTS.subForm(), columnModel.getLabel())
                .build();

        columns.add(column);
        cells.add(new SubFormRenderer(format.getSubFormId(), TableViewModel.ID_COLUMN_ID, format.getId()));
    }

    private void addErrorColumn(EffectiveTableColumn tableColumn, ErrorFormat errorFormat) {
//        ValueProvider<Integer, String> valueProvider = new ValueProvider<Integer, String>() {
//            @Override
//            public String getValue(Integer object) {
//                return null;
//            }
//
//            @Override
//            public void setValue(Integer object, String value) {
//            }
//
//            @Override
//            public String getPath() {
//                return tableColumn.getId();
//            }
//        };
//
//        ColumnConfig<Integer, String> config = new ColumnConfig<>(valueProvider, tableColumn.getWidth());
//        config.setHeader(tableColumn.getLabel());
//        config.setCell(new ErrorCell());
//        config.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LOCALE_END);
//        config.setVerticalHeaderAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
//        columnConfigs.add(config);
    }


    public List<DataTableColumn> getColumns() {
        return columns;
    }

    public Observable<TableSlice> renderRows(Observable<ColumnSet> columnSet,
                                             Observable<RowRange> range,
                                             Observable<Optional<RecordRef>> selectedRef) {
        return Observable.transform(columnSet, range, (columns, r) -> {
            int numRows = columns.getNumRows();
            int numCols = this.cells.size();

            for (ColumnRenderer cell : this.cells) {
                cell.init(columns);
            }

            ColumnView idColumn = columns.getColumnView(TableViewModel.ID_COLUMN_ID);


            int startIndex = r.getStartIndex();
            int endIndex = Math.min(numRows, startIndex + r.getRowCount());
            int rowCount = (endIndex - startIndex);
            VTree[] rows = new VTree[rowCount];

            for (int i = 0; i < rowCount; i++) {
                int rowIndex = startIndex + i;
                String rowId = idColumn.getString(rowIndex);

                PropMap rowProps = Props.create();
                rowProps.setData("row", rowId);

                VTree[] cells = new VTree[numCols];
                for (int j = 0; j < numCols; j++) {
                    cells[j] = this.cells.get(j).renderCell(rowIndex);
                }
                rows[i] = new VNode(HtmlTag.TR, rowProps, cells, rowId);
            }

            return new TableSlice(startIndex, numRows,
                    new VNode(HtmlTag.TBODY, rows));

        }).join(slice -> {

           // Apply selection style if present
           return selectedRef.transform(optional -> {
                return slice.applySelectionClass(optional.map(ref -> ref.getRecordId().asString()));
           });
        });
    }


}
