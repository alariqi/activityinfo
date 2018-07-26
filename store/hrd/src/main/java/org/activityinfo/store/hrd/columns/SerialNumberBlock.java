package org.activityinfo.store.hrd.columns;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.query.StringArrayColumnView;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.SerialNumber;
import org.activityinfo.model.type.SerialNumberType;
import org.activityinfo.store.hrd.entity.FormEntity;

import javax.annotation.Nullable;
import java.util.BitSet;
import java.util.Iterator;

public class SerialNumberBlock implements BlockManager {

    private static final String PREFIX_POOL_PROPERTY = "prefixPool";
    private static final String PREFIX_OFFSET_PROPERTY = "prefix";
    private static final String NUMBER_PROPERTY = "number";

    private final SerialNumberType type;

    public SerialNumberBlock(SerialNumberType type) {
        this.type = type;
    }

    @Override
    public int getBlockSize() {
        return 1024 * 6;
    }

    @Override
    public int getMaxFieldSize() {
        return 1;
    }

    @Override
    public String getBlockType() {
        return "serial";
    }

    /**
     * Update a block with a new field value
     * @param blockEntity the data store entity for the block
     * @param recordOffset the zero-based index of the record, relative to the start of the block
     * @param fieldValue the new field value
     */
    public Entity update(Entity blockEntity, int recordOffset, @Nullable FieldValue fieldValue) {

        // Serial numbers cannot be changed
        if(fieldValue == null) {
            // no change
            return null;
        }

        SerialNumber serialNumber = (SerialNumber) fieldValue;

        char prefixOffset = StringPools.findOrInsertStringInPool(blockEntity, PREFIX_POOL_PROPERTY, serialNumber.getPrefix());

        OffsetArray.updateOffset(blockEntity, PREFIX_OFFSET_PROPERTY, recordOffset, prefixOffset);
        IntValueArray.update(blockEntity, NUMBER_PROPERTY, recordOffset, serialNumber.getNumber());

        return blockEntity;
    }

    @Override
    public ColumnView buildView(FormEntity header, TombstoneIndex tombstones, Iterator<Entity> blockIterator, String component) {
        String[] values = new String[header.getRecordCount()];
        while (blockIterator.hasNext()) {
            Entity block = blockIterator.next();
            int blockIndex = (int)(block.getKey().getId() - 1);
            int blockStart = blockIndex * getBlockSize();

            int targetIndex = blockStart - tombstones.countDeletedBefore(blockStart);
            BitSet deleted = tombstones.getDeletedBitSet(blockStart, getBlockSize());

            String[] prefixPool = null;
            byte[] prefixOffsets = null;
            if(block.hasProperty(PREFIX_POOL_PROPERTY) && block.hasProperty(PREFIX_OFFSET_PROPERTY)) {
                prefixPool = StringPools.toArray((Blob) block.getProperty(PREFIX_POOL_PROPERTY));
                prefixOffsets = ((Blob)block.getProperty(PREFIX_OFFSET_PROPERTY)).getBytes();
            }

            byte[] numbers = ((Blob)block.getProperty(NUMBER_PROPERTY)).getBytes();
            int numberLength = IntValueArray.length(numbers);

            for (int i = 0; i < numberLength; i++) {
                if(!deleted.get(i)) {
                    int prefixOffset = 0;
                    if (prefixOffsets != null) {
                        prefixOffset = OffsetArray.get(prefixOffsets, i);
                    }
                    String prefix;
                    if (prefixOffset == 0) {
                        prefix = null;
                    } else {
                        prefix = prefixPool[prefixOffset - 1];
                    }

                    int number = IntValueArray.get(numbers, i);
                    if (number != IntValueArray.MISSING) {
                        values[targetIndex] = type.format(prefix, number);
                    }
                    targetIndex++;
                }
            }
        }
        return new StringArrayColumnView(values);
    }



}
