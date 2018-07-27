package org.activityinfo.ui.client.lookup.viewModel;

import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.query.StringArrayColumnView;
import org.activityinfo.model.resource.ResourceId;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class LookupIndexTest {

    @Test
    public void test() {

        ResourceId formId = ResourceId.valueOf("FORM");
        ColumnView id = new StringArrayColumnView(new String[]{"r1", "r2", "r3", "r4"});
        ColumnView province = new StringArrayColumnView(new String[]{"Nord Kivu", "Sud Kivu", "Nord Kivu", "Sud Kivu"});
        ColumnView territory = new StringArrayColumnView(new String[]{"Beni", "Penekusu", "Rutshuru", "Bukavu"});

        LookupIndex index = new LookupIndex(formId, id, Arrays.asList(province, territory));

        // Exact lookup

        assertThat(index.lookup("Nord Kivu", "Beni"), equalTo("r1"));
        assertThat(index.lookup("Sud Kivu", "Beni"), nullValue());
        assertThat(index.lookup("Sud Kivu", "Penekusu"), equalTo("r2"));

        // Fuzzy lookup
        assertThat(index.lookup("NORD Kivu", "Beni"), equalTo("r1"));
        assertThat(index.lookup("NORD KIVI", "BeENI"), equalTo("r1"));

    }
}
