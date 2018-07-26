package org.activityinfo.store.hrd.columns;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.query.StringArrayColumnView;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.primitive.TextValue;
import org.activityinfo.store.hrd.entity.FormEntity;

import javax.annotation.Nullable;
import java.util.BitSet;
import java.util.Iterator;

public class RecordIdBlock implements BlockManager {

    public static final String BLOCK_NAME = "$ID";

    public static final ResourceId FIELD_ID = ResourceId.valueOf(BLOCK_NAME);

    public static final int BLOCK_SIZE = 1024 * 4;

    @Override
    public int getBlockSize() {
        return BLOCK_SIZE;
    }

    @Override
    public int getMaxFieldSize() {
        return 1;
    }

    @Override
    public String getBlockType() {
        return "id";
    }

    @Override
    public Entity update(Entity blockEntity, int recordOffset, @Nullable FieldValue fieldValue) {

        Blob strings = (Blob) blockEntity.getProperty("ids");
        int length = StringPools.size(strings);

        // since we only ever update when adding a new record, we should be able to simply append to the string pool
        if(recordOffset != length) {
            throw new IllegalStateException("length = " + length + ", recordOffset = " + recordOffset);
        }

        blockEntity.setProperty("ids", StringPools.appendString(strings, ((TextValue) fieldValue).asString()));

        return blockEntity;
    }

    @Override
    public ColumnView buildView(FormEntity header, TombstoneIndex tombstones, Iterator<Entity> blockIterator, String component) {

        String[] ids = new String[header.getRecordCount()];

        while (blockIterator.hasNext()) {
            Entity block = blockIterator.next();
            int blockIndex = (int)(block.getKey().getId() - 1);
            int blockStart = blockIndex * getBlockSize();

            int targetIndex = blockStart - tombstones.countDeletedBefore(blockStart);
            BitSet deleted = tombstones.getDeletedBitSet(blockStart, getBlockSize());

            String[] values = StringPools.toArray((Blob) block.getProperty("ids"));
            for (int i = 0; i < values.length; i++) {
                if(!deleted.get(i)) {
                    ids[targetIndex++] = values[i];
                }
            }
        }

        return new StringArrayColumnView(ids);
    }
}
