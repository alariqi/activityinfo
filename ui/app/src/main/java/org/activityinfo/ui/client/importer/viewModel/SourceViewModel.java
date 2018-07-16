package org.activityinfo.ui.client.importer.viewModel;

import org.activityinfo.model.query.ColumnView;

import java.util.ArrayList;
import java.util.List;

public class SourceViewModel {

    private List<SourceColumn> columns = new ArrayList<>();

    public SourceViewModel() {
    }

    public SourceViewModel(String pastedText) {
        DelimiterGuesser guesser = new DelimiterGuesser(pastedText);
        char delimiter = guesser.guess();
        if(delimiter != 0) {
            RowParser parser = new RowParser(pastedText, delimiter);
            List<ParsedRow> headerRows = parser.parseRows(1);
            if(headerRows.size() == 1) {
                ParsedRow headerRow = headerRows.get(0);
                int numColumns = headerRow.getColumnCount();
                if (numColumns > 0) {
                    ColumnView[] columns = parser.parseColumns(numColumns);
                    for (int i = 0; i < numColumns; i++) {
                        this.columns.add(new SourceColumn(headerRow.getColumnValue(i), columns[i]));
                    }
                }
            }
        }
    }

    public boolean isValid() {
        return columns.size() > 0;
    }

    public List<SourceColumn> getColumns() {
        return columns;
    }

    public int getRowCount() {
        if(columns.isEmpty()) {
            return 0;
        } else {
            return columns.get(0).getColumnView().numRows();
        }
    }
}
