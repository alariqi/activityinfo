package org.activityinfo.store.hrd.columns;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.primitive.TextType;
import org.activityinfo.model.type.primitive.TextValue;
import org.activityinfo.store.hrd.entity.FormEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static java.util.Collections.emptyIterator;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class StringBlockTest {

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig()
                    .setDefaultHighRepJobPolicyUnappliedJobPercentage(100));

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }


    @Test
    public void strings() {
        FormField stringField = new FormField(ResourceId.valueOf("F")).setType(TextType.SIMPLE);
        StringBlock block = (StringBlock) BlockFactory.get(stringField);

        Entity blockEntity = new Entity("Block", 1);

        block.update(blockEntity, 0, TextValue.valueOf("Hello World"));
        block.update(blockEntity, 2, TextValue.valueOf(null));
        block.update(blockEntity, 1, TextValue.valueOf("Sue"));
        block.update(blockEntity, 8, TextValue.valueOf("Bob"));
        block.update(blockEntity, 9, TextValue.valueOf("Dan"));

        FormEntity header = new FormEntity();
        header.setNumberedRecordCount(10);

        TombstoneIndex tombstoneIndex = new TombstoneIndex(header, emptyIterator());

        ColumnView view = block.buildView(header, tombstoneIndex, Arrays.asList(blockEntity).iterator());
        assertThat(view.getString(0), equalTo("Hello World"));
        assertThat(view.getString(1), equalTo("Sue"));
        assertThat(view.getString(2), nullValue());
        assertThat(view.getString(8), equalTo("Bob"));
        assertThat(view.getString(9), equalTo("Dan"));
    }

    @Test
    public void deleted() {
        FormField stringField = new FormField(ResourceId.valueOf("F")).setType(TextType.SIMPLE);
        StringBlock block = (StringBlock) BlockFactory.get(stringField);

        Entity blockEntity = new Entity("Block", 1);
        block.update(blockEntity, 0, TextValue.valueOf("Hello World"));
        block.update(blockEntity, 1, TextValue.valueOf("Goodbye World"));
        block.update(blockEntity, 2, TextValue.valueOf("Hello Again"));


        Entity tombstone = new Entity("Tombstone", 1);
        TombstoneBlock.markDeleted(tombstone, 1);

        FormEntity header = new FormEntity();
        header.setNumberedRecordCount(3);
        header.setDeletedCount(1);

        TombstoneIndex tombstoneIndex = new TombstoneIndex(header, Arrays.asList(tombstone).iterator());

        ColumnView view = block.buildView(header, tombstoneIndex, Arrays.asList(blockEntity).iterator());
        assertThat(view.numRows(), equalTo(2));
        assertThat(view.getString(0), equalTo("Hello World"));
        assertThat(view.getString(1), equalTo("Hello Again"));
    }


    @Test
    public void multiBlockDeleted() {
        FormField stringField = new FormField(ResourceId.valueOf("F")).setType(TextType.SIMPLE);
        StringBlock block = (StringBlock) BlockFactory.get(stringField);

        Entity blockEntity1 = new Entity("Block", 1);
        block.update(blockEntity1, 0, TextValue.valueOf("Hello World"));
        block.update(blockEntity1, 1, TextValue.valueOf("Goodbye World"));

        Entity blockEntity2 = new Entity("Block", 2);
        block.update(blockEntity2, 0, TextValue.valueOf("Hello Again"));

        FormEntity header = new FormEntity();
        header.setNumberedRecordCount(block.getBlockSize() * 2);
        header.setDeletedCount(1);

        Entity tombstone = new Entity("Tombstone", 1);
        TombstoneBlock.markDeleted(tombstone, 0);

        TombstoneIndex tombstoneIndex = new TombstoneIndex(header, Arrays.asList(tombstone).iterator());

        ColumnView view = block.buildView(header, tombstoneIndex, Arrays.asList(blockEntity1, blockEntity2).iterator());
        assertThat(view.numRows(), equalTo(block.getBlockSize() * 2 - 1));
        assertThat(view.getString(0), equalTo("Goodbye World"));
        assertThat(view.getString(1), nullValue());
        assertThat(view.getString(block.getBlockSize() - 1), equalTo("Hello Again"));
    }


    @Test
    public void update() {
        FormField stringField = new FormField(ResourceId.valueOf("F")).setType(TextType.SIMPLE);
        StringBlock block = (StringBlock) BlockFactory.get(stringField);
        Entity blockEntity = new Entity("Block", 1);

        block.update(blockEntity, 0, TextValue.valueOf("Hello World"));
        block.update(blockEntity, 1, TextValue.valueOf("Goodbye World"));
        block.update(blockEntity, 1, null);

        FormEntity header = new FormEntity();
        header.setNumberedRecordCount(2);

        TombstoneIndex tombstoneIndex = new TombstoneIndex(header, Collections.<Entity>emptyList().iterator());

        ColumnView view = block.buildView(header, tombstoneIndex, Arrays.asList(blockEntity).iterator());
        assertThat(view.numRows(), equalTo(2));
        assertThat(view.getString(0), equalTo("Hello World"));
        assertThat(view.getString(1), nullValue());
    }

}