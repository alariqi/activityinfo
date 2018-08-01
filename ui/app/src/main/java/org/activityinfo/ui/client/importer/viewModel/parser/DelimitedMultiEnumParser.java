package org.activityinfo.ui.client.importer.viewModel.parser;

import com.google.common.collect.Ordering;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.enumerated.EnumItem;
import org.activityinfo.model.type.enumerated.EnumType;
import org.activityinfo.model.type.enumerated.EnumValue;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

import javax.annotation.Nonnull;
import java.util.*;

public class DelimitedMultiEnumParser implements FieldParser {

    private char delimiter = ',';
    private String[] labels;
    private ResourceId[] itemIds;
    private BitSet[] singletons;

    private Map<String, Integer> labelMap = new HashMap<>();

    public DelimitedMultiEnumParser(EnumType type) {

        // Sort the labels in descending order of length so we don't
        // risking a substring that could match a longer label

        List<EnumItem> items = new ArrayList<>(type.getValues());
        items.sort(Ordering.natural().onResultOf(item -> -item.getLabel().length()));

        labels = new String[items.size()];
        itemIds = new ResourceId[items.size()];
        singletons = new BitSet[items.size()];
        for (int i = 0; i < items.size(); i++) {
            EnumItem item = items.get(i);
            labels[i] = EnumParser.normalize(item.getLabel());
            itemIds[i] = item.getId();
            singletons[i] = new BitSet();
            singletons[i].set(i);
        }

        for (int i = 0; i < items.size(); i++) {
            labelMap.put(labels[i], i);
        }
    }

    @Override
    public double scoreContent(SourceColumn column) {
        // Quickly check to see what proportion of sampled rows contains at
        // least one label

        String[] sample = column.getSample();
        if(sample.length == 0) {
            return 0;
        }

        int countMatching = 0;
        for (int i = 0; i < sample.length; i++) {
            if(match(sample[i]) != null) {
                countMatching++;
            }
        }

        return ((double)countMatching) / ((double)sample.length);
    }

    @Override
    public boolean validate(@Nonnull String value) {
        return match(value) != null;
    }

    @Override
    public FieldValue parse(@Nonnull String input) {
        BitSet bitSet = match(input);
        if(bitSet == null) {
            return null;
        }

        List<ResourceId> items = new ArrayList<>();
        for (int i = 0; i < itemIds.length; i++) {
            if(bitSet.get(i)) {
                items.add(itemIds[i]);
            }
        }
        return new EnumValue(items);
    }

    private BitSet match(@Nonnull String input) {

        // Normalize input
        input = EnumParser.normalize(input);

        // Quick match of a single item?
        Integer singletonIndex = labelMap.get(input);
        if(singletonIndex != null) {
            return singletons[singletonIndex];
        }

        // Otherwise, loop through the input looking for matching labels

        BitSet bitSet = new BitSet();

        int currentPos = 0;
        int inputLength = input.length();

        while(currentPos < inputLength) {

            // Find a label that starts at this position
            int labelIndex = labelAtIndex(input, currentPos);
            if(labelIndex == -1) {
                return null;
            }

            // Mark this label as present
            bitSet.set(labelIndex);

            // Advance to next item
            currentPos += labels[labelIndex].length() + 1;

            // Skip any whitespace following the delimiter
            while(currentPos < inputLength && input.charAt(currentPos) == ' ') {
                currentPos++;
            }
        }

        return bitSet;
    }

    private int labelAtIndex(String input, int currentPos) {

        int remainingLength = input.length() - currentPos;

        for (int i = 0; i < labels.length; i++) {
            String label = labels[i];
            int labelLength = label.length();

            if (labelLength <= remainingLength &&
                    input.substring(currentPos, currentPos + labelLength).equals(label)) {

                // To be a match, the label either needs to be followed by the delimiter, or
                // end the input
                if(remainingLength == labelLength ||
                        input.charAt(currentPos + labelLength) == delimiter) {
                    return i;
                }
            }
        }
        return -1;
    }
}
