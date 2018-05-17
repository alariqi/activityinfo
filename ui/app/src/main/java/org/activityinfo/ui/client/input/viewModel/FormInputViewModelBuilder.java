/*
 * ActivityInfo
 * Copyright (C) 2009-2013 UNICEF
 * Copyright (C) 2014-2018 BeDataDriven Groep B.V.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.activityinfo.ui.client.input.viewModel;

import com.google.common.base.Predicate;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.activityinfo.model.database.RecordLockSet;
import org.activityinfo.model.form.FormEvalContext;
import org.activityinfo.model.form.FormInstance;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.RecordTree;
import org.activityinfo.model.formula.FormulaNode;
import org.activityinfo.model.formula.FormulaParser;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.SerialNumberType;
import org.activityinfo.model.type.primitive.TextType;
import org.activityinfo.model.type.time.PeriodValue;
import org.activityinfo.promise.Maybe;
import org.activityinfo.ui.client.input.model.FieldInput;
import org.activityinfo.ui.client.input.model.FormInputModel;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Collections.emptyMap;

/**
 * Helper class which constructs a {@link FormInputViewModel}.
 *
 * <p>This class is built from a form's tree and includes all parsed formulas needed to calculate
 * field values and relevancy, so they don't need to be re-parsed every time the {@link FormInputModel} changes.</p>
 */
public class FormInputViewModelBuilder {

    private final Logger LOGGER = Logger.getLogger(FormInputViewModelBuilder.class.getName());

    private final FormTree formTree;
    private final FormEvalContext evalContext;
    private final RecordLockSet locks;

    private Map<ResourceId, Predicate<FormInstance>> relevanceCalculators = new HashMap<>();

    private List<FieldValidator> validators = new ArrayList<>();

    public FormInputViewModelBuilder(FormTree formTree) {
        this.formTree = formTree;
        this.locks = formTree.getRootMetadata().getLocks();
        this.evalContext = new FormEvalContext(this.formTree.getRootFormClass());

        for (FormTree.Node node : this.formTree.getRootFields()) {
            if(node.getField().hasRelevanceCondition()) {
                buildRelevanceCalculator(node);
            }
            if(node.getType() instanceof TextType) {
                TextType textType = (TextType) node.getType();
                if(textType.hasInputMask()) {
                    validators.add(new FieldValidator(node.getFieldId(),
                        new InputMaskValidator(textType.getInputMask())));
                }
            }
        }
    }


    private void buildRelevanceCalculator(FormTree.Node node) {

        String formula = node.getField().getRelevanceConditionExpression();

        FormulaNode rootNode;
        try {
            rootNode = FormulaParser.parse(formula);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Invalid relevance formula: " + formula, e);
            return;
        }

        relevanceCalculators.put(node.getFieldId(), instance -> {
            evalContext.setInstance(instance);
            try {
                return rootNode.evaluateAsBoolean(evalContext);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to evaluate relevance condition", e);
                return true;
            }
        });
    }

    public FormInputViewModel build(FormInputModel inputModel) {
        return build(inputModel, Maybe.notFound());
    }


    public FormInputViewModel build(FormInputModel inputModel, Maybe<RecordTree> existingRecord) {

        // Combine the original values of the record with the newly entered data

        Map<ResourceId, FieldValue> existingValues = existingRecord
                .transform(r -> r.getRoot().getFieldValueMap())
                .or(emptyMap());

        FormInstance record = computeUpdatedRecord(existingValues, inputModel);

        // Now construct the viewModel that includes everything about
        // the current state of data entry.

        FormInputViewModel viewModel = new FormInputViewModel();
        viewModel.formTree = this.formTree;
        viewModel.inputModel = inputModel;
        viewModel.fieldValueMap = record.getFieldValueMap();
        viewModel.existingValues = existingValues;
        viewModel.relevant = computeRelevance(record);
        viewModel.missing = computeMissing(record, viewModel.relevant);
        viewModel.validationErrors = validateFieldValues(record);
        viewModel.dirty = computeDirty(existingValues, record);
        viewModel.locked = checkLocks(record);
        viewModel.valid =
                allInputValid(inputModel) &&
                viewModel.missing.isEmpty() &&
                viewModel.validationErrors.isEmpty();

        LOGGER.info("Valid = " + viewModel.valid);

        return viewModel;
    }


    /**
     * Computes the effective record, based
     * @param existingValues
     * @param inputModel
     * @return
     */
    private FormInstance computeUpdatedRecord(Map<ResourceId, FieldValue> existingValues, FormInputModel inputModel) {

        // We inherit all the existing values...

        FormInstance record = new FormInstance(ResourceId.generateId(), formTree.getRootFormId());
        record.setAll(existingValues);

        // Now apply changes...
        for (FormTree.Node node : formTree.getRootFields()) {
            FieldInput fieldInput = inputModel.get(node.getFieldId());
            switch (fieldInput.getState()) {
                case UNTOUCHED:
                    // No changes
                    break;
                case EMPTY:
                    record.set(node.getFieldId(), (FieldValue)null);
                    break;
                case VALID:
                    record.set(node.getFieldId(), fieldInput.getValue());
                    break;
                case INVALID:
                    LOGGER.info("Field with invalid input = " + node.getFieldId());
                    record.set(node.getFieldId(), (FieldValue)null);
                    break;
            }
        }
        LOGGER.info("fieldValues = " + record.getFieldValueMap());
        return record;
    }

    /**
     * Computes the set of fields that are relevant based the current state of the
     * form and the rules defined in the form's schema.
     *
     */
    private Set<ResourceId> computeRelevance(FormInstance record) {
        // All fields are relevant by default
        Set<ResourceId> relevantSet = new HashSet<>();
        for (FormTree.Node node : formTree.getRootFields()) {
            relevantSet.add(node.getFieldId());
        }

        // Now keep updating the set until it converges
        boolean changing;
        do {
            changing = false;

            for (Map.Entry<ResourceId, Predicate<FormInstance>> field : relevanceCalculators.entrySet()) {

                boolean relevant = field.getValue().apply(record);
                if(!relevant) {
                    record.set(field.getKey(), (FieldValue)null);
                }

                if(toggle(relevantSet, field.getKey(), relevant)) {
                    changing = true;
                }
            }
        } while(changing);
        return relevantSet;
    }

    /**
     * Adds or removes a field from the relevancy set.
     * @param relevantSet the relevancy set
     * @param fieldId the id of the field to add or remove
     * @param relevant true if the field is relevant
     * @return {@code true} if the set has changed.
     */
    private boolean toggle(Set<ResourceId> relevantSet, ResourceId fieldId, boolean relevant) {
        boolean wasRelevant = relevantSet.contains(fieldId);
        if(relevant) {
            relevantSet.add(fieldId);
        } else {
            relevantSet.remove(fieldId);
        }
        return wasRelevant != relevant;
    }


    /**
     * Computes the set of fields that are relevant, required, but still missing values.
     */
    private Set<ResourceId> computeMissing(FormInstance record, Set<ResourceId> relevantSet) {
        Set<ResourceId> missing = new HashSet<>();
        for (FormTree.Node node : formTree.getRootFields()) {
            if(node.getType() instanceof SerialNumberType) {
                continue;
            }
            boolean required = node.getField().isRequired();
            boolean visible = node.getField().isVisible();
            boolean relevant = relevantSet.contains(node.getFieldId());
            boolean empty = record.get(node.getFieldId()) == null;

            if (required && visible && relevant && empty) {
                missing.add(node.getFieldId());
            }
        }

        LOGGER.info("Missing fields = " + missing);

        return missing;
    }

    private Multimap<ResourceId, String> validateFieldValues(FormInstance record) {
        Multimap<ResourceId, String> validationErrors = HashMultimap.create();
        for (FieldValidator validator : validators) {
            validator.run(record, validationErrors);
        }
        return validationErrors;
    }

    private boolean checkLocks(FormInstance record) {
        FieldValue period = record.get(ResourceId.valueOf("period"));
        if(period instanceof PeriodValue) {
            boolean locked = locks.isLocked(((PeriodValue) period).asInterval());

            if(locked) {
                LOGGER.info("Locked = true");
            }
            return locked;
        }
        return false;
    }

    private boolean computeDirty(Map<ResourceId, FieldValue> existingValues, FormInstance currentValues) {

        for (FormTree.Node node : formTree.getRootFields()) {
            FieldValue originalValue = existingValues.get(node.getFieldId());
            FieldValue currentValue = currentValues.get(node.getFieldId());
            if (!Objects.equals(originalValue, currentValue)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Returns true if the user has not entered something that is not even an valid field value.
     * For example, if the user enters text in a quantity field, or an incomplete geographic coordinate.
     */
    private boolean allInputValid(FormInputModel inputModel) {
        for (FormTree.Node node : formTree.getRootFields()) {
            FieldInput fieldInput = inputModel.get(node.getFieldId());
            if(fieldInput != null && fieldInput.getState() == FieldInput.State.INVALID) {
                return false;
            }
        }
        return true;
    }

}