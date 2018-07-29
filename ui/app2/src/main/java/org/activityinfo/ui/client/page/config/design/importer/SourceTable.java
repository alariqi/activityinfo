package org.activityinfo.ui.client.page.config.design.importer;

import org.activityinfo.io.csv.DelimiterGuesser;
import org.activityinfo.io.csv.RowParser;

import java.util.ArrayList;
import java.util.List;

public class SourceTable {

    private List<SourceColumn> columns = new ArrayList<>();
    private List<SourceRow> rows = new ArrayList<>();

    public SourceTable(String text) {
        DelimiterGuesser guesser = new DelimiterGuesser(text);
        char delimiter = guesser.guess();
        RowParser parser = new RowParser(text, delimiter);

        // Parse the first line as the header
        parser.parse(new RowParser.EventListener() {
            @Override
            public void field(String string) {
                columns.add(new SourceColumn(string, columns.size()));
            }

            @Override
            public boolean endOfRecord() {
                // Stop parsing, we only want the first row of headers
                return false;
            }

            @Override
            public void endOfFile() {
            }
        });

        // Parse remaining rows
        parser.parse(new RowParser.EventListener() {

            private SourceRow currentRow = new SourceRow(0);

            @Override
            public void field(String string) {
                currentRow.columnValues.add(string);
            }

            @Override
            public boolean endOfRecord() {
                rows.add(currentRow);
                currentRow = new SourceRow(currentRow.rowIndex + 1);
                return true;
            }

            @Override
            public void endOfFile() {
            }
        });
    }

    public List<SourceColumn> getColumns() {
        return columns;
    }

    public String getColumnHeader(int col) {
        return getColumns().get(col).getHeader();
    }

    public List<SourceRow> getRows() {
        return rows;
    }
}
