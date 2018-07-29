package org.activityinfo.ui.client.importer.viewModel;

import com.google.gwt.core.client.GWT;
import org.activityinfo.io.csv.ColumnBuilder;
import org.activityinfo.io.csv.DelimiterGuesser;
import org.activityinfo.io.csv.RowParser;
import org.activityinfo.observable.IncrementalTask;

import java.util.ArrayList;
import java.util.List;

public class SourceParser implements IncrementalTask<SourceViewModel> {

    /**
     * The number of rows to parse for each iteration of the event loop
     */
    private static final int BATCH_SIZE = 200;

    private final List<String> headings = new ArrayList<>();
    private final RowParser parser;
    private final ColumnBuilder[] columns;

    /**
     * True if all the rows have been parsed
     */
    private boolean done = false;

    private SourceViewModel result;

    public SourceParser(String text) {
        parser = new RowParser(text, DelimiterGuesser.guess(text));

        // Parse ONLY the first row to get a column count and the
        // list of headers.

        parser.parse(new RowParser.EventListener() {
            @Override
            public void field(String string) {
                headings.add(string);
            }

            @Override
            public boolean endOfRecord() {
                return false;
            }
        });

        columns = new ColumnBuilder[headings.size()];
        for (int i = 0; i < headings.size(); i++) {
            columns[i] = createBuilder();
        }


    }


    @Override
    public boolean execute() {

        final int columnCount = columns.length;


        // Parse up to BATCH_SIZE rows before yielding to the rest
        // of the event loop.

        parser.parse(new RowParser.EventListener() {
            int columnIndex = 0;
            int rowsParsed = 0;

            @Override
            public void field(String string) {
                if(columnIndex < columnCount) {
                    columns[columnIndex++].add(string);
                }
            }

            @Override
            public boolean endOfRecord() {
                while(columnIndex < columnCount) {
                    columns[columnIndex++].add(null);
                }
                columnIndex = 0;
                rowsParsed++;
                return rowsParsed < BATCH_SIZE;
            }

            @Override
            public void endOfFile() {
                done = true;
            }
        });

        // If we are done, we can build the final result
        List<SourceColumn> sourceColumns = new ArrayList<>();
        List<String> columnIds = composeColumnIds();
        for (int i = 0; i < columns.length; i++) {
            sourceColumns.add(new SourceColumn(columnIds.get(i), headings.get(i), columns[i].build()));
        }
        this.result = new SourceViewModel(sourceColumns);

        return !done;
    }

    private List<String> composeColumnIds() {
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < headings.size(); i++) {
            ids.add("col" + i);
        }
        return ids;
    }

    @Override
    public boolean isLoading() {
        return !done;
    }

    @Override
    public SourceViewModel getValue() {
        return result;
    }

    private ColumnBuilder createBuilder() {
        if(GWT.isScript()) {
            return JsColumnBuilder.create();
        } else {
            return new JvmColumnBuilder();
        }
    }
}
