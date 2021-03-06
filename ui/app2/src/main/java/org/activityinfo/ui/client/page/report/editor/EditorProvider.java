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
package org.activityinfo.ui.client.page.report.editor;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.activityinfo.legacy.shared.reports.model.*;
import org.activityinfo.ui.client.component.report.editor.chart.ChartEditor;
import org.activityinfo.ui.client.component.report.editor.map.MapEditor;
import org.activityinfo.ui.client.component.report.editor.pivotTable.PivotTableEditor;
import org.activityinfo.ui.client.component.report.editor.text.TextElementEditor;

public class EditorProvider {

    private final Provider<ChartEditor> chartEditor;
    private final Provider<MapEditor> mapEditor;
    private final Provider<PivotTableEditor> pivotEditor;
    private final Provider<CompositeEditor2> compositeEditor;
    private final Provider<TextElementEditor> textEditor;

    @Inject
    public EditorProvider(Provider<ChartEditor> chartEditor,
                          Provider<MapEditor> mapEditor,
                          Provider<PivotTableEditor> pivotEditor,
                          Provider<CompositeEditor2> compositeEditor,
                          Provider<TextElementEditor> textEditor) {
        this.chartEditor = chartEditor;
        this.mapEditor = mapEditor;
        this.pivotEditor = pivotEditor;
        this.compositeEditor = compositeEditor;
        this.textEditor = textEditor;
    }

    public ReportElementEditor create(ReportElement model) {
        if (model instanceof PivotChartReportElement) {
            return chartEditor.get();
        } else if (model instanceof PivotTableReportElement) {
            return pivotEditor.get();
        } else if (model instanceof MapReportElement) {
            return mapEditor.get();
        } else if (model instanceof TextReportElement) {
            return textEditor.get();
        } else if (model instanceof Report) {
            return compositeEditor.get();
        } else {
            throw new IllegalArgumentException(model.getClass().getName());
        }
    }
}
