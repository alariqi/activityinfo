package org.activityinfo.ui.client.importer.viewModel;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Resources;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.store.testing.ColumnSetMatchers;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static com.google.common.io.Resources.getResource;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class RowParserTest {



    @Test
    public void simpleCsv() {

        List<ParsedRow> rows = new RowParser("hello,brave,new,world\n1,2,3,4\n5,6,7,8", ',').parseRows();

        assertThat(rows.size(), equalTo(3));
        assertThat(rows.get(0).getColumnValue(0), equalTo("hello"));
        assertThat(rows.get(0).getColumnValue(1), equalTo("brave"));
        assertThat(rows.get(0).getColumnValue(3), equalTo("world"));
        assertThat(rows.get(1).getColumnValue(0), equalTo("1"));
        assertThat(rows.get(1).getColumnValue(1), equalTo("2"));
        assertThat(rows.get(1).getColumnValue(3), equalTo("4"));
        assertThat(rows.get(2).getColumnValue(3), equalTo("8"));
    }

    @Test
    public void quotedFields() {
        List<ParsedRow> rows = new RowParser("\"hello, fred\",bob,\"hello, there\"\na,b,c", ',').parseRows();

        assertThat(rows.size(), equalTo(2));
        assertThat(rows.get(0).getColumnValue(0), equalTo("hello, fred"));
        assertThat(rows.get(0).getColumnValue(1), equalTo("bob"));
        assertThat(rows.get(0).getColumnValue(2), equalTo("hello, there"));
        assertThat(rows.get(1).getColumnValue(0), equalTo("a"));
    }

    @Test
    public void quotedFieldsWithNewlines() {
        List<ParsedRow> rows = new RowParser(
                "1,Jane Goodall,\"304 E42nd street\nNew York, NY\nUSA\"\n" +
                        "2,Richard Feynman,\"401 1st Street\nCaltech\nUSA\"", ',').parseRows();

        System.out.println(Joiner.on('\n').join(rows));

        assertThat(rows.size(), equalTo(2));
        assertThat(rows.get(0).getColumnValue(2), equalTo("304 E42nd street\n" +
                "New York, NY\n" +
                "USA"));

        assertThat(rows.get(1).getColumnValue(0), equalTo("2"));
    }

    @Test
    public void quotedFieldsWithCrNewlines() {
        List<ParsedRow> rows = new RowParser(
                "1,Jane Goodall,\"304 E42nd street\r\nNew York, NY\r\nUSA\"\r\n" +
                        "2,Richard Feynman,\"401 1st Street\r\nCaltech\r\nUSA\"", ',').parseRows();

        assertThat(rows.size(), equalTo(2));
        assertThat(rows.get(0).getColumnValue(2), equalTo("304 E42nd street\r\n" +
                "New York, NY\r\n" +
                "USA"));

        assertThat(rows.get(1).getColumnValue(0), equalTo("2"));
    }

    @Test
    public void qisCsvColumns() throws IOException {
        String csv = Resources.toString(getResource(getClass(), "qis.csv"), Charsets.UTF_8);
        RowParser parser = new RowParser(csv, ',');
        List<ParsedRow> headers = parser.parseRows(1);

        assertThat(headers.size(), equalTo(1));
        ParsedRow header = headers.get(0);
        assertThat(header.getColumnValue(0), equalTo("Partner"));
        assertThat(header.getColumnValue(1), equalTo("_CREATION_DATE"));
        assertThat(header.getColumnValue(2), equalTo("_LAST_UPDATE_URI_USER"));
        assertThat(header.getColumnValue(3), equalTo("_LAST_UPDATE_DATE"));
        assertThat(header.getColumnValue(9), equalTo("PROVERTY_STATUS"));

        ColumnView[] columns = parser.parseColumns(header.getColumnCount());
        for (int i = 0; i < header.getColumnCount(); i++) {
            assertThat(header.getColumnValue(i), columns[i].numRows(), equalTo(63));
        }

        ColumnView status = columns[9];
        assertThat(status.getString(0), equalTo("up"));
        assertThat(status.getString(1), equalTo("pp"));
        assertThat(status.getString(61), equalTo("np"));
        assertThat(status.getString(62), equalTo("up"));
    }

    @Test
    public void trailingNewLine() throws IOException {
        URL resource = Resources.getResource(ImportViewModel.class, "qis-villages.csv");
        String text = Resources.toString(resource, Charsets.UTF_8);

        RowParser parser = new RowParser(text, ',');
        ColumnView[] columnViews = parser.parseColumns(2);

        assertThat(columnViews[0], ColumnSetMatchers.hasValues("Name", "Village 1"));
        assertThat(columnViews[1], ColumnSetMatchers.hasValues("District", "Chittagong"));
    }
}