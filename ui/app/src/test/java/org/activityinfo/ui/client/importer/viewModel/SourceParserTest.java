package org.activityinfo.ui.client.importer.viewModel;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.activityinfo.model.query.ColumnView;
import org.junit.Test;

import java.io.IOException;

import static com.google.common.io.Resources.getResource;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class SourceParserTest {


    @Test
    public void qisCsvColumns() throws IOException {
        String csv = Resources.toString(getResource(getClass(), "qis.csv"), Charsets.UTF_8);

        SourceParser parser = new SourceParser(csv);
        while(parser.isLoading()) {
            parser.execute();
        }

        SourceViewModel source = parser.getValue();

        assertThat(source.getColumns(), hasSize(47));

        assertThat(source.getColumn(0).getLabel(), equalTo("Partner"));
        assertThat(source.getColumn(1).getLabel(), equalTo("_CREATION_DATE"));
        assertThat(source.getColumn(2).getLabel(), equalTo("_LAST_UPDATE_URI_USER"));
        assertThat(source.getColumn(3).getLabel(), equalTo("_LAST_UPDATE_DATE"));
        assertThat(source.getColumn(9).getLabel(), equalTo("PROVERTY_STATUS"));

        ColumnView status = source.getColumn(9).getColumnView();
        assertThat(status.getString(0), equalTo("up"));
        assertThat(status.getString(1), equalTo("pp"));
        assertThat(status.getString(61), equalTo("np"));
        assertThat(status.getString(62), equalTo("up"));
    }

}