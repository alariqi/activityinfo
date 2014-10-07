package org.activityinfo.model.table;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.activityinfo.model.json.ObjectMapperFactory;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.resource.Resources;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class TableModelTest {


    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = ObjectMapperFactory.get();
    }

    @Test
    public void serialization() throws IOException {

        ResourceId classId = ResourceId.valueOf("f023423");
        TableModel model = new TableModel(classId);
        model.selectField(Resources.generateId()).as("c1");

        String json = mapper.writeValueAsString(TableModelClass.INSTANCE.toRecord(model));
        System.out.println(json);

        TableModel remodel = mapper.readValue(json, TableModel.class);
        assertThat(remodel.getRowSources(), contains(new RowSource(classId)));
        assertThat(remodel.getColumns(), hasSize(1));

    }
}