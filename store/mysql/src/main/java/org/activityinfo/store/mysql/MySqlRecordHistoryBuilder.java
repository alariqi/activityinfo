package org.activityinfo.store.mysql;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import org.activityinfo.json.JsonValue;
import org.activityinfo.model.form.*;
import org.activityinfo.model.legacy.CuidAdapter;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.model.type.ReferenceType;
import org.activityinfo.model.type.ReferenceValue;
import org.activityinfo.model.type.enumerated.EnumItem;
import org.activityinfo.model.type.enumerated.EnumType;
import org.activityinfo.model.type.enumerated.EnumValue;
import org.activityinfo.model.type.number.Quantity;
import org.activityinfo.model.type.number.QuantityType;
import org.activityinfo.model.type.primitive.HasStringValue;
import org.activityinfo.model.type.subform.SubFormReferenceType;
import org.activityinfo.model.type.time.LocalDate;
import org.activityinfo.model.type.time.LocalDateType;
import org.activityinfo.store.mysql.metadata.Activity;
import org.activityinfo.store.mysql.metadata.ActivityField;
import org.activityinfo.store.spi.FormNotFoundException;
import org.activityinfo.store.spi.FormStorage;
import org.activityinfo.store.spi.RecordHistoryProvider;
import org.activityinfo.store.spi.RecordVersion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Assembles a history of a record and it's sub records.
 */
public class MySqlRecordHistoryBuilder implements RecordHistoryProvider {

    private MySqlStorageProvider catalog;

    private static class FieldDelta {
        private FormField field;
        private FieldValue oldValue;
        private FieldValue newValue;
        private SubFormKind subFormKind;
        private String subFormKey;
    }


    private static class RecordDelta {
        private RecordVersion version;
        private FormField subFormField;
        private final List<FieldDelta> changes = new ArrayList<>();
    }

    private static class User {
        private String name;
        private String email;
    }

    public MySqlRecordHistoryBuilder(MySqlStorageProvider catalog) {
        this.catalog = catalog;
    }


    @Override
    public RecordHistory build(RecordRef recordRef) throws SQLException {
        Optional<FormStorage> form = catalog.getForm(recordRef.getFormId());
        if(!form.isPresent()) {
            throw new FormNotFoundException(recordRef.getFormId());
        }

        FormClass formClass = form.get().getFormClass();
        Map<ResourceId, String> monthlyFieldLabels = getMonthlyFieldLabels(formClass);

        List<RecordDelta> deltas = computeDeltas(formClass, null,
                form.get().getVersions(recordRef.getRecordId()), monthlyFieldLabels);

        // Now add deltas from sub forms...
        for (FormField field : formClass.getFields()) {
            if(field.getType() instanceof SubFormReferenceType) {
                deltas.addAll(computeSubFormDeltas(recordRef.getRecordId(), field, monthlyFieldLabels));
            }
        }

        sort(deltas);

        // Query users involved in the changes
        Map<Long, User> userMap = queryUsers(deltas);

        // Now render the complete object for the user
        List<RecordHistoryEntry> entries = new ArrayList<>();
        for (RecordDelta delta : deltas) {

            User user = userMap.get(delta.version.getUserId());
            if(user == null) {
                user = new User();
                user.email = delta.version.getUserId() + "@activityinfo.org";
                user.name = "User " + delta.version.getUserId();
            }

            RecordHistoryEntry.Builder entry = new RecordHistoryEntry.Builder();
            entry.setFormId(recordRef.getFormId().asString());
            entry.setRecordId(recordRef.getRecordId().asString());

            if(delta.subFormField != null) {
                entry.setSubFieldId(delta.subFormField.getId().asString());
                entry.setSubFieldLabel(delta.subFormField.getLabel());
            }

            entry.setTime((int)(delta.version.getTime() / 1000));
            entry.setChangeType(delta.version.getType().name().toLowerCase());
            entry.setUserName(user.name);
            entry.setUserEmail(user.email);

            for (FieldDelta change : delta.changes) {
                entry.addValue(renderChange(change));
            }
            entries.add(entry.build());
        }
        return RecordHistory.create(entries);
    }

    private Map<ResourceId, String> getMonthlyFieldLabels(FormClass formClass) throws SQLException {
        Map<ResourceId,String> labels = new HashMap<>();
        if(formClass.getId().getDomain() == CuidAdapter.ACTIVITY_DOMAIN) {
            Activity activity = catalog.getActivityLoader().load(CuidAdapter.getLegacyIdFromCuid(formClass.getId()));
            if (activity.isMonthly()) {
                labels = getMonthlyFieldLabels(activity);
            }
        }
        return labels;
    }

    private void sort(List<RecordDelta> deltas) {
        Collections.sort(deltas, Collections.reverseOrder(new Comparator<RecordDelta>() {
            @Override
            public int compare(RecordDelta o1, RecordDelta o2) {
                int compare = Integer.compare((int) (o1.version.getTime() / 1000), (int) (o2.version.getTime() / 1000));
                if (compare != 0) {
                    return compare;
                } else {
                    if (o1.version.getSubformKind() != null && o2.version.getSubformKind() == null) {
                        return 1;
                    }
                    if (o2.version.getSubformKind() != null && o1.version.getSubformKind() == null) {
                        return -1;
                    }
                    return 0;
                }
            }
        }));
    }

    private Collection<RecordDelta> computeSubFormDeltas(ResourceId parentRecordId, FormField subFormField, Map<ResourceId,String> monthlyFieldLabels) {
        SubFormReferenceType subFormType = (SubFormReferenceType) subFormField.getType();
        Optional<FormStorage> subForm = catalog.getForm(subFormType.getClassId());
        FormClass subFormClass = subForm.get().getFormClass();

        List<RecordVersion> versions = subForm.get().getVersionsForParent(parentRecordId);

        return computeDeltas(subFormClass, subFormField, versions, monthlyFieldLabels);
    }


    private List<RecordDelta> computeDeltas(FormClass formClass,
                                            FormField subFormField,
                                            List<RecordVersion> versions,
                                            Map<ResourceId,String> monthlyFieldLabels) {

        List<RecordDelta> deltas = new ArrayList<>();

        Map<ResourceId, Map<ResourceId, FieldValue>> currentStateMap = new HashMap<>();

        for (RecordVersion version : versions) {
            RecordDelta delta = new RecordDelta();
            delta.version = version;
            delta.subFormField = subFormField;

            Map<ResourceId, FieldValue> currentState = currentStateMap.get(version.getRecordId());

            if(currentState == null) {

                // Initialize our state map for this record
                currentStateMap.put(version.getRecordId(),
                        new HashMap<>(delta.version.getValues()));

            } else {

                // Identify changes to the record values compared to the previous 
                // version.
                for (FormField field : formClass.getFields()) {
                    if (version.getValues().containsKey(field.getId())) {

                        FieldValue oldValue = currentState.get(field.getId());
                        FieldValue newValue = version.getValues().get(field.getId());

                        if (!Objects.equals(oldValue, newValue)) {
                            FieldDelta fieldDelta = new FieldDelta();
                            fieldDelta.field = field;
                            fieldDelta.oldValue = oldValue;
                            fieldDelta.newValue = newValue;

                            if (!Strings.isNullOrEmpty(version.getSubformKey())) { // subforms
                                fieldDelta.subFormKey = version.getSubformKey();
                                fieldDelta.subFormKind = version.getSubformKind();
                            }

                            delta.changes.add(fieldDelta);
                        }
                        currentState.put(field.getId(),  newValue);
                    }
                }

                // special handling for old legacy history
                for (Map.Entry<ResourceId, FieldValue> entry : version.getValues().entrySet()) {
                    ResourceId fieldId = entry.getKey();
                    String fieldIdAsString = fieldId.asString();

                    if (fieldIdAsString.startsWith("I") && fieldIdAsString.contains("M")) { // e.g. I309566527M2016-8

                        int idInt = CuidAdapter.getLegacyIdFromCuid(fieldIdAsString.substring(fieldIdAsString.indexOf("I"),
                                                                                              fieldIdAsString.indexOf("M")));
                        ResourceId id = CuidAdapter.indicatorField(idInt);

                        if(monthlyFieldLabels.containsKey(id)) { // check to ensure field still exists
                            FieldValue oldValue = currentState.get(fieldId);
                            FieldValue newValue = version.getValues().get(fieldId);

                            if (!Objects.equals(oldValue, newValue)) {

                                String month = fieldIdAsString.substring(fieldIdAsString.indexOf("M") + 1);

                                FieldDelta fieldDelta = new FieldDelta();
                                fieldDelta.field = new FormField(fieldId);
                                fieldDelta.field.setLabel(Strings.nullToEmpty(monthlyFieldLabels.get(id)) + " (" + month + ")");
                                fieldDelta.field.setType(new QuantityType());
                                fieldDelta.oldValue = oldValue;
                                fieldDelta.newValue = newValue;
                                delta.changes.add(fieldDelta);
                            }
                            currentState.put(fieldId, newValue);
                        }
                    }
                }

            }
            deltas.add(delta);
        }
        return deltas;
    }

    private Map<ResourceId,String> getMonthlyFieldLabels(Activity activity) {
        Map<ResourceId, String> monthlyFields = new HashMap<>();
        for (ActivityField field : activity.getFields()) {
            monthlyFields.put(field.getFormField().getId(),
                    Strings.nullToEmpty(field.getFormField().getLabel()));
        }
        return monthlyFields;
    }

    private Map<Long, User> queryUsers(List<RecordDelta> deltas) throws SQLException {
        Set<Long> userSet = new HashSet<>();
        for (RecordDelta delta : deltas) {
            userSet.add(delta.version.getUserId());
        }

        Map<Long, User> userMap = new HashMap<>();

        if(userSet.isEmpty()) {
            return userMap;
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT userId, name, email FROM userlogin WHERE userId IN (");
        boolean needsComma = false;
        for (Long userId : userSet) {
            if(needsComma) {
                sql.append(", ");
            }
            sql.append(userId);
            needsComma = true;
        }
        sql.append(")");

        try(ResultSet rs = catalog.getExecutor().query(sql.toString())) {
            while(rs.next()) {

                int userId = rs.getInt(1);

                User user = new User();
                user.name = rs.getString(2);
                user.email = rs.getString(3);

                userMap.put((long) userId, user);
            }
        }
        return userMap;
    }

    private FieldValueChange renderChange(FieldDelta delta) {
        FieldValueChange.Builder builder = new FieldValueChange.Builder();
        builder.setFieldId(delta.field.getId().asString());
        builder.setFieldLabel(delta.field.getLabel());
        if(delta.subFormKey != null) {
            builder.setSubFormKey(delta.subFormKey);
            builder.setSubFormKind(delta.subFormKind != null ? delta.subFormKind.name() : null);
        }
        builder.setOldValueLabel(renderValue(delta.field, delta.oldValue));
        builder.setNewValueLabel(renderValue(delta.field, delta.newValue));
        return builder.build();
    }

    private String renderValue(FormField field, FieldValue value) {
        if(value == null) {
            return "";
        }
        if(value instanceof HasStringValue) {
            return ((HasStringValue) value).asString();
        }
        if(field.getType() instanceof EnumType) {
            return render((EnumType)field.getType(), (EnumValue)value);
        }
        if(field.getType() instanceof QuantityType) {
            Quantity quantity = (Quantity) value;
            return Double.toString(quantity.getValue());
        }
        if(field.getType() instanceof LocalDateType) {
            return ((LocalDate) value).toString();
        }
        if(field.getType() instanceof ReferenceType) {
            return renderRef((ReferenceType)field.getType(), (ReferenceValue)value);
        }
        return "";
    }

    private String renderRef(ReferenceType type, ReferenceValue value) {
       return Joiner.on(", ").join(queryLabels(type, value));
    }

    private List<String> queryLabels(ReferenceType type, ReferenceValue value) {
        Map<ResourceId, String> labelMap = new HashMap<>();
        for (ResourceId formId : type.getRange()) {
            Optional<FormStorage> form = catalog.getForm(formId);
            if(form.isPresent()) {
                Optional<ResourceId> labelFieldId = findLabelField(form.get().getFormClass());

                for (RecordRef ref : value.getReferences()) {
                    Optional<FormRecord> record = form.get().get(ref.getRecordId());
                    if (record.isPresent()) {
                        JsonValue labelValue = null;

                        if (labelFieldId.isPresent()) {
                            labelValue = record.get().getFields().get(labelFieldId.get().asString());
                        }

                        if (labelValue.isJsonNull()) {
                            labelValue = record.get().getFields().get(CuidAdapter.field(formId, CuidAdapter.NAME_FIELD).asString());
                        }

                        if (labelValue.isJsonPrimitive()) {
                            labelMap.put(ref.getRecordId(), labelValue.asString());
                        }
                    }
                }
            }
        }

        List<String> list = new ArrayList<>();
        for (RecordRef ref : value.getReferences()) {
            String label = labelMap.get(ref.getRecordId());
            if(label == null) {
                list.add(ref.toQualifiedString());
            } else {
                list.add(label);
            }
        }
        return list;
    }

    private Optional<ResourceId> findLabelField(FormClass formClass) {
        for (FormField field : formClass.getFields()) {
            if(field.getSuperProperties().contains(ResourceId.valueOf("label"))) {
                return Optional.of(field.getId());
            }
        }
        return Optional.absent();
    }

    private String render(EnumType type, EnumValue value) {
        StringBuilder sb = new StringBuilder();
        boolean needsComma = false;
        for (EnumItem enumItem : value.getValuesAsItems(type)) {
            if(needsComma) {
                sb.append(", ");
            }
            sb.append(enumItem.getLabel());
            needsComma = true;
        }
        return sb.toString();
    }
}
