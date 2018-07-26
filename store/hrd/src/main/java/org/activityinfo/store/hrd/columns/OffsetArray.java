package org.activityinfo.store.hrd.columns;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.PropertyContainer;

/**
 * An array of 16-bit offsets encoded as one-based little-endian byte array.
 *
 * <p>Can be updated without deserialization and
 * re-serialization. Missing offsets are encoded as zero.
 *
 * <p>Unallocated elements at the end of the array are considered to be missing (zero)</p>
 */
public class OffsetArray {


    public static final int BYTES = 2;

    static final String OFFSETS_PROPERTY = "values";

    public static Blob update(Blob values, int index, char offset) {
        if(offset == 0) {
            return updateToZero(values, index);
        } else {
            byte[] bytes = ValueArrays.ensureCapacity(values, index, BYTES);
            ValueArrays.setChar(bytes, index, offset);

            return new Blob(bytes);
        }
    }

    /**
     * Zeros out the value at the given index. Unallocated values are assumed to be zero,
     * so if the array is only updated if it is already long enough to include the given index.
     *
     * @param values the value blob
     * @param index the index within the blob
     */
    public static Blob updateToZero(Blob values, int index) {
        if(values == null) {
            return null;
        }
        byte[] bytes = values.getBytes();

        int pos = index * BYTES;

        if(pos < bytes.length) {
            bytes[pos] = 0;
            bytes[pos + 1] = 0;
        }

        return new Blob(bytes);
    }

    public static int length(byte[] bytes) {
        return bytes.length / BYTES;
    }

    public static int length(Blob values) {
        if(values == null) {
            return 0;
        } else {
            return length(values.getBytes());
        }
    }


    /**
     * Retrieves the one-based offset from the entity. Zero if the offset is missing.
     */
    public static int get(PropertyContainer entity, String propertyName, int index) {
        return get(((Blob) entity.getProperty(propertyName)), index);
    }

    /**
     * Retrieves the one-based offset from the blob. Zero if the offset is missing.
     */
    public static int get(Blob blob, int index) {
        if(blob == null) {
            return 0;
        } else {
            return get(blob.getBytes(), index);
        }
    }

    /**
     * Retrieves the one-based offset from the byte array. Zero if the offset is missing.
     */
    public static int get(byte[] bytes, int index) {
        int pos = index * BYTES;

        if(pos >= bytes.length) {
            return 0;
        }

        return (char) ((bytes[pos+1] << 8) | (bytes[pos] & 0xFF));
    }

    /**
     * Updates the offset array property of a block.
     *
     * @param blockEntity
     * @param recordOffset
     * @param value
     *
     * @return true if the offset array was updated, or false if there was no change.
     */
    static boolean updateOffset(PropertyContainer blockEntity, int recordOffset, char value) {
        return updateOffset(blockEntity, OFFSETS_PROPERTY, recordOffset, value);
    }

    /**
     * Updates the offset array property of a block.
     *
     * @param blockEntity
     * @param propertyName
     * @param recordOffset
     * @param value
     *
     * @return true if the offset array was updated, or false if there was no change.
     */
    static boolean updateOffset(PropertyContainer blockEntity, String propertyName, int recordOffset, char value) {

        Blob values = (Blob) blockEntity.getProperty(propertyName);
        int existingLength = length(values);

        if(value == 0 && recordOffset >= existingLength) {
            /* No updated needed */
            return false;

        } else {
            values = update(values, recordOffset, value);
            blockEntity.setProperty(propertyName, values);

            return true;
        }
    }
}
