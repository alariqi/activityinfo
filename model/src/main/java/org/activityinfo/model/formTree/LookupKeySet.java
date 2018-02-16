package org.activityinfo.model.formTree;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import org.activityinfo.model.expr.CompoundExpr;
import org.activityinfo.model.expr.ExprNode;
import org.activityinfo.model.expr.SymbolExpr;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.form.FormInstance;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.model.type.ReferenceType;
import org.activityinfo.model.type.SerialNumberType;
import org.activityinfo.model.type.enumerated.EnumType;
import org.activityinfo.model.type.primitive.TextType;
import org.activityinfo.promise.Maybe;

import java.util.*;

/**
 * Set of keys that can be used to lookup a value for a reference field.
 *
 * <p>Fields of type {@link ReferenceType} refer to Records in other Forms. In order to be select
 * a value, users need a way to "look up" the ID of a Record. We could of course ask users to the enter
 * the auto-generated RecordId, but that would be difficult.</p>
 *
 * <p>One intuitive way to look up Records is to use the fields that form designers have identified as "keys".</p>
 *
 * <p>In the simplest case, a Form will have a single text key field. Such a reference field will have a single
 * {@link LookupKey}. The user can select one value from the {@code LookupKey} and that will be sufficient to
 * uniquely identify the record.</p>
 *
 * <p>In other cases, a Form might have two text keys, such as "Last Name" and "First Name". Neither key is
 * sufficient to identify a record uniquely, but together they can be used to look up a record.</p>
 */
public class LookupKeySet {

    private List<LookupKey> lookupKeys = new ArrayList<>();

    /**
     * Map from a referenced form to it's leaf lookup key.
     *
     * For example, if you have a reference field that includes [PROVINCE, TERRITORY, SECTOR]
     * in its reference range, that means that _either_ a PROVINCE can be selected, _or_ a TERRITORY _or_ a SECTOR
     * can be selected.
     *
     * If those three forms have the following keys:
     *
     * <pre>
     *     PROVINCE:  k0[PROVINCE.name]
     *     TERRITORY: k0[PROVINCE.name], k1[TERRITORY.name]
     *     SECTOR:    k0[PROVINCE.name], k1[TERRITORY.name], k2[SECTOR.name]
     * </pre>
     *
     * Then the "leaf key" of the PROVINCE form is k0, while the leaf key of TERRITORY is k1, and
     * the leaf key of SECTOR is k2.
     *
     */
    private Map<ResourceId, LookupKey> leafKeyMap = HashBiMap.create();

    /**
     * Maps each visited form to its leaf key.
     *
     * <p>For reference fields with multiple forms included in the range, the keys may overlap. We want
     * to
     *
     */
    private Map<ResourceId, LookupKey> formMap = new HashMap<>();

    private Multimap<ResourceId, ResourceId> parentMap = HashMultimap.create();


    private FormTree formTree;
    private String referenceFieldLabel;

    private Collection<ResourceId> range;

    public LookupKeySet(FormTree formTree, FormField referenceField) {
        this.formTree = formTree;
        this.referenceFieldLabel = referenceField.getLabel();
        this.range = ((ReferenceType) referenceField.getType()).getRange();

        ReferenceType referenceType = (ReferenceType) referenceField.getType();
        for (ResourceId referenceFormId : referenceType.getRange()) {
            leafKeyMap.put(referenceFormId, addLevels(formTree.getFormClass(referenceFormId)));
        }

        // Now that we know how many keys there are and from which forms,
        // we can compose labels
        Set<ResourceId> multiKeyForms = findFormsWithMultipleKeyFields();

        for (LookupKey lookupKey : lookupKeys) {
            composeLabel(multiKeyForms, lookupKey);
        }
    }

    public Collection<ResourceId> getRange() {
        return range;
    }

    private LookupKey addLevels(FormClass formClass) {

        ResourceId formId = formClass.getId();

        if(formMap.containsKey(formId)) {
            return formMap.get(formId);
        }

        // if serial number is present, we use that exclusively.
        Optional<FormField> serialNumberField = findSerialNumberField(formClass);
        if(serialNumberField.isPresent()) {
            LookupKey lookupKey = serialNumberLevel(formClass, serialNumberField.get());
            lookupKeys.add(lookupKey);
            return lookupKey;
        }

        LookupKey parentLevel = null;
        String parentFieldId = null;

        // If there is a reference key, then we climb the reference tree recursively.
        Optional<FormField> referenceKey = findReferenceKey(formClass);
        if(referenceKey.isPresent()) {
            ReferenceType referenceType = (ReferenceType) referenceKey.get().getType();
            ResourceId referencedFormId = Iterables.getOnlyElement(referenceType.getRange());
            FormClass referencedFormClass = formTree.getFormClass(referencedFormId);
            parentMap.put(formId, referencedFormId);
            parentLevel = addLevels(referencedFormClass);
            parentFieldId = referenceKey.get().getId().asString();
        }

        // Now check for text key fields
        for (FormField formField : formClass.getFields()) {
            if(isTextLikeKey(formField)) {
                LookupKey lookupKey = textKeyLevel(formClass, parentLevel, parentFieldId, formField);
                lookupKeys.add(lookupKey);
                parentLevel = lookupKey;
                parentFieldId = null;
            }
        }

        // If there is really no other key fields, then use the autogenerated id as a key
        if (parentLevel == null) {
            parentLevel = idLevel(formClass);
            lookupKeys.add(parentLevel);
        }

        formMap.put(formId, parentLevel);

        return parentLevel;
    }

    private boolean isTextLikeKey(FormField formField) {
        return formField.isKey() &&
                (formField.getType() instanceof TextType ||
                 formField.getType() instanceof EnumType);
    }


    private int nextKeyIndex() {
        return lookupKeys.size() + 1;
    }


    private LookupKey serialNumberLevel(FormClass form, FormField field) {
        return new LookupKey(nextKeyIndex(), form, field);
    }

    private LookupKey textKeyLevel(FormClass form,
                                   LookupKey parentLevel,
                                   String parentFieldId,
                                   FormField formField) {

        return new LookupKey(nextKeyIndex(), parentFieldId, parentLevel, form, formField);
    }

    private LookupKey idLevel(FormClass formSchema) {
        return new LookupKey(nextKeyIndex(), formSchema);
    }

    private Optional<FormField> findSerialNumberField(FormClass formClass) {
        for (FormField formField : formClass.getFields()) {
            if(formField.getType() instanceof SerialNumberType) {
                return Optional.of(formField);
            }
        }
        return Optional.absent();
    }

    private Optional<FormField> findReferenceKey(FormClass formClass) {
        for (FormField formField : formClass.getFields()) {
            if(formField.isKey() && formField.getType() instanceof ReferenceType) {
                return Optional.of(formField);
            }
        }
        return Optional.absent();
    }


    private void composeLabel(Set<ResourceId> multiKeyForms, LookupKey lookupKey) {


        StringBuilder label = new StringBuilder();

        if (formMap.size() > 1) {
            // If there are keys that live on multiple forms,
            // we need to distinguish them by their form name
            label.append(lookupKey.getFormLabel());
        } else {
            // If there is only one form referenced,
            // we always use the referencing field's label
            label.append(Strings.nullToEmpty(referenceFieldLabel));
        }

        // Only qualify the label with the field name of the key if this
        // form has multiple (non-reference) key fields.
        if(multiKeyForms.contains(lookupKey.getFormId())) {
            if(label.length() > 0) {
                label.append(" ");
            }
            if(lookupKey.getFieldLabel() == null) {
                label.append("ID");
            } else {
                label.append(lookupKey.getFieldLabel());
            }
        }

        lookupKey.keyLabel = label.toString();
    }

    private Set<ResourceId> findFormsWithMultipleKeyFields() {
        Set<ResourceId> once = new HashSet<>();
        Set<ResourceId> multiple = new HashSet<>();

        for (LookupKey lookupKey : lookupKeys) {
            ResourceId formId = lookupKey.getFormId();
            if(!once.add(formId)) {
                multiple.add(formId);
            }
        }

        return multiple;
    }

    public List<LookupKey> getLookupKeys() {
        return lookupKeys;
    }

    /**
     * @return a List of LookupKeys with no children.
     */
    public Collection<LookupKey> getLeafKeys() {
        return leafKeyMap.values();
    }


    /**
     * Composes a human-readable label for a record reference.
     */
    public Maybe<String> label(RecordTree tree, RecordRef ref) {
        Optional<FormInstance> potentialRecord = tree.getRecord(ref).getIfVisible();
        if (potentialRecord.isPresent()) {
            FormInstance record = potentialRecord.get();
            for (LookupKey lookupKey : lookupKeys) {
                if (record.getFormId().equals(lookupKey.getFormId())) {
                    return Maybe.of(lookupKey.label(record));
                }
            }
        }
        return Maybe.notFound();
    }

    public LookupKey getKey(int i) {
        return lookupKeys.get(i);
    }

    public LookupKey getLeafKey(ResourceId referencedFormId) {
        LookupKey lookupKey = leafKeyMap.get(referencedFormId);
        assert lookupKey != null : "No leaf key for " + referencedFormId;
        return lookupKey;
    }

    public Set<ResourceId> getAncestorForms(ResourceId formId) {
        Set<ResourceId> set = new HashSet<>();
        collectAncestors(Collections.singletonList(formId), set);
        return set;
    }

    private void collectAncestors(Collection<ResourceId> formIds, Set<ResourceId> set) {
        for (ResourceId formId : formIds) {
            Collection<ResourceId> parents = parentMap.get(formId);
            set.addAll(parents);
            collectAncestors(parents, set);
        }
    }

    public Map<LookupKey, ExprNode> getKeyFormulas(ExprNode baseField) {

        if(leafKeyMap.size() == 1) {
            return Iterables.getOnlyElement(getLeafKeys()).getKeyFormulas(baseField);

        } else {
            // For the case where a field can refer to one of several different forms,
            // query the keys using the syntax [formId].[keyFieldId]

            Map<LookupKey, ExprNode> keyMap = new HashMap<>();
            for (LookupKey lookupKey : lookupKeys) {
                keyMap.put(lookupKey, new CompoundExpr(new SymbolExpr(lookupKey.getFormId()), lookupKey.getKeyField()));
            }

            return keyMap;
        }
    }

    public Map<LookupKey, ExprNode> getKeyFormulas(ResourceId fieldId) {
        return getKeyFormulas(new SymbolExpr(fieldId));
    }
}
