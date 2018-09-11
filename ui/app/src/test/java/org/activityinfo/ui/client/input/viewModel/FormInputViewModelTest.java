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

import org.activityinfo.model.resource.RecordTransaction;
import org.activityinfo.model.database.RecordLock;
import org.activityinfo.model.database.Resource;
import org.activityinfo.model.database.ResourceType;
import org.activityinfo.model.database.UserDatabaseMeta;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormMetadata;
import org.activityinfo.model.form.FormPermissions;
import org.activityinfo.model.formTree.FormClassProviders;
import org.activityinfo.model.formTree.FormMetadataProvider;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.FormTreeBuilder;
import org.activityinfo.model.resource.RecordUpdate;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.model.type.SerialNumber;
import org.activityinfo.model.type.enumerated.EnumValue;
import org.activityinfo.model.type.number.Quantity;
import org.activityinfo.model.type.primitive.TextValue;
import org.activityinfo.model.type.time.LocalDate;
import org.activityinfo.promise.Promise;
import org.activityinfo.store.testing.IntakeForm;
import org.activityinfo.store.testing.Survey;
import org.activityinfo.store.testing.TestForm;
import org.activityinfo.ui.client.input.model.FieldInput;
import org.activityinfo.ui.client.input.model.FormInputModel;
import org.activityinfo.ui.client.store.TestSetup;
import org.activityinfo.ui.client.store.TestingFormStore;
import org.junit.Before;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class FormInputViewModelTest {

    private TestSetup setup = new TestSetup();

    @Test
    public void testSurveyRelevance() {

        Survey survey = setup.getCatalog().getSurvey();

        FormInputViewModelBuilder builder = builderFor(survey);

        // Start with no input
        FormInputModel inputModel = new FormInputModel(new RecordRef(survey.getFormId(), ResourceId.generateId()));



        // Is this valid?
        FormInputViewModel viewModel = builder.build(inputModel);

        // Fields with invalid relevance are considered relevant
        assertTrue("field with bad relevance is relevant", viewModel.isRelevant(survey.getAgeFieldId()));


        assertThat("pregnant is not yet relevant", viewModel.isRelevant(survey.getPregnantFieldId()), equalTo(false));
        assertThat("prenatale care is not relevant", viewModel.isRelevant(survey.getPrenataleCareFieldId()), equalTo(false));

        assertThat("form is valid", viewModel.isValid(), equalTo(false));

        // Answer the gender
        inputModel = inputModel.update(survey.getGenderFieldId(), new FieldInput(new EnumValue(survey.getFemaleId())));
        viewModel = builder.build(inputModel);

        assertThat("pregnant is now relevant", viewModel.isRelevant(survey.getPregnantFieldId()), equalTo(true));
        assertThat("prenatale care is still not relevant", viewModel.isRelevant(survey.getPrenataleCareFieldId()), equalTo(false));

        // Answer pregnant = yes
        inputModel = inputModel.update(survey.getPregnantFieldId(), new FieldInput(new EnumValue(survey.getPregnantId())));
        viewModel = builder.build(inputModel);

        assertThat("pregnant is still relevant", viewModel.isRelevant(survey.getPregnantFieldId()), equalTo(true));
        assertThat("prenatale is now relevant", viewModel.isRelevant(survey.getPrenataleCareFieldId()), equalTo(true));

        // Change gender = Male
        inputModel = inputModel.update(survey.getGenderFieldId(), new FieldInput(new EnumValue(survey.getMaleId())));
        viewModel = builder.build(inputModel);

        assertThat("pregnant is not relevant", viewModel.isRelevant(survey.getPregnantFieldId()), equalTo(false));
        assertThat("prenatale is not relevant", viewModel.isRelevant(survey.getPrenataleCareFieldId()), equalTo(false));

    }
    @Test
    public void testSurveyEdit() {

        TestingFormStore store = new TestingFormStore();
        Survey survey = store.getCatalog().getSurvey();

        RecordRef recordRef = survey.getRecordRef(5);

        FormStructure stucture = fetchStructure(recordRef);
        FormInputViewModelBuilder builder = new FormInputViewModelBuilder(stucture.getFormTree());

        FormInputModel inputModel = new FormInputModel(new RecordRef(survey.getFormId(), ResourceId.generateId()));

        FormInputViewModel viewModel = builder.build(inputModel, stucture.getExistingRecord());

        assertTrue(viewModel.isValid());
    }

    @Test
    public void testNewlyIrrelvantFieldSetToEmpty() {

        TestingFormStore store = new TestingFormStore();
        Survey survey = store.getCatalog().getSurvey();

        RecordRef recordRef = survey.getRecordRef(8);

        FormStructure structure = fetchStructure(recordRef);
        FormInputViewModelBuilder builder = new FormInputViewModelBuilder(structure.getFormTree());

        FormInputModel inputModel = new FormInputModel(new RecordRef(survey.getFormId(), ResourceId.generateId()));

        // The record was saved as GENDER=Female, and PREGNANT=No

        FormInputViewModel viewModel = builder.build(inputModel, structure.getExistingRecord());
        assertThat(viewModel.getField(survey.getGenderFieldId()), equalTo(new EnumValue(survey.getFemaleId())));
        assertThat(viewModel.isRelevant(survey.getPregnantFieldId()), equalTo(true));
        assertThat(viewModel.getField(survey.getPregnantFieldId()), equalTo(new EnumValue(survey.getPregnantNo())));

        // When we change the Gender to Male, then PREGNANT should be set to empty
        inputModel = inputModel.update(survey.getGenderFieldId(), new EnumValue(survey.getMaleId()));
        viewModel = builder.build(inputModel, structure.getExistingRecord());

        assertThat(viewModel.isRelevant(survey.getPregnantFieldId()), equalTo(false));

        RecordTransaction tx = viewModel.buildTransaction();
        assertThat(tx.getChangeArray(), arrayWithSize(1));

        RecordUpdate update = tx.getChanges().iterator().next();
        assertTrue(update.getFields().get(survey.getPregnantFieldId().asString()).isJsonNull());
    }

    @Test
    public void testPersistence() {

        Survey survey = setup.getCatalog().getSurvey();

        FormInputViewModelBuilder builder = builderFor(survey);

        // Start with no input
        FormInputModel inputModel = new FormInputModel(new RecordRef(survey.getFormId(), ResourceId.generateId()))
                .update(survey.getGenderFieldId(), new FieldInput(new EnumValue(survey.getMaleId())))
                .update(survey.getNameFieldId(), new FieldInput(TextValue.valueOf("BOB")))
                .update(survey.getDobFieldId(), new FieldInput(new LocalDate(1982,1,16)))
                .update(survey.getAgeFieldId(), new FieldInput(new Quantity(35)));

        // Verify that it's valid
        FormInputViewModel viewModel = builder.build(inputModel);
        assertThat(viewModel.isValid(), equalTo(true));

        // Now build the update transaction and save!
        Promise<Void> completed = setup.getFormStore().updateRecords(viewModel.buildTransaction());
        assertThat(completed.getState(), equalTo(Promise.State.FULFILLED));
    }

    @Test
    public void testSerialNumberEdit() {
        IntakeForm intakeForm = setup.getCatalog().getIntakeForm();

        RecordRef ref = intakeForm.getRecords().get(0).getRef();
        FormStructure structure = fetchStructure(ref);

        FormInputViewModelBuilder builder = builderFor(structure);
        FormInputModel model = new FormInputModel(ref);

        FormInputViewModel viewModel = builder.build(model, structure.getExistingRecord());

        assertThat(viewModel.getField(intakeForm.getProtectionCodeFieldId()), equalTo(new SerialNumber(1)));
    }


    @Test
    public void testMultipleSelectPersistence() {

        IntakeForm intakeForm = setup.getCatalog().getIntakeForm();

        FormInputViewModelBuilder builder = builderFor(intakeForm);

        FormInputModel inputModel = new FormInputModel(new RecordRef(intakeForm.getFormId(), ResourceId.generateId()))
                .update(intakeForm.getOpenDateFieldId(), new LocalDate(2017,1,1))
                .update(intakeForm.getNationalityFieldId(), new EnumValue(intakeForm.getPalestinianId(), intakeForm.getJordanianId()))
                .update(intakeForm.getQuantityField().getId(), new Quantity(12));

        FormInputViewModel viewModel = builder.build(inputModel);
        assertThat(viewModel.isValid(), equalTo(true));

        Promise<Void> completed = setup.getFormStore().updateRecords(viewModel.buildTransaction());
        assertThat(completed.getState(), equalTo(Promise.State.FULFILLED));
    }

    @Test
    public void inputMask() {
        IntakeForm intakeForm = setup.getCatalog().getIntakeForm();
        FormInputViewModelBuilder builder = builderFor(intakeForm);

        FormInputModel inputModel = new FormInputModel(new RecordRef(intakeForm.getFormId(), ResourceId.generateId()));

        // Fill in required fields
        inputModel = inputModel
            .update(intakeForm.getOpenDateFieldId(), new LocalDate(2017, 1, 1));

        // Does not match input mask "000"
        inputModel = inputModel.update(intakeForm.getRegNumberFieldId(), new FieldInput(TextValue.valueOf("FOOOOO")));

        FormInputViewModel viewModel = builder.build(inputModel);

        assertThat(viewModel.isValid(), equalTo(false));
        assertThat(viewModel.getValidationErrors(intakeForm.getRegNumberFieldId()), not(empty()));
    }

    private FormInputViewModelBuilder builderFor(TestForm survey) {
        return new FormInputViewModelBuilder(fetchStructure(survey.getFormId()).getFormTree());
    }

    private FormInputViewModelBuilder builderFor(FormStructure structure) {
        return new FormInputViewModelBuilder(structure.getFormTree());
    }

    private FormStructure fetchStructure(ResourceId formId) {
        ResourceId newRecordId = ResourceId.generateSubmissionId(formId);
        RecordRef newRecordRef = new RecordRef(formId, newRecordId);

        return fetchStructure(newRecordRef);
    }

    private FormStructure fetchStructure(RecordRef ref) {
        return setup.connect(FormStructure.fetch(setup.getFormStore(), ref)).assertLoaded();
    }



}