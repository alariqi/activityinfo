package org.activityinfo.store.testing;

import com.google.common.base.Supplier;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.form.FormInstance;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.primitive.TextType;

import java.util.ArrayList;
import java.util.List;

/**
 * List of "acts" from the CHDC database. Has a single text key, but additional
 * text fields are used for lookup. Not all lookup keys have values.
 */
public class ActForm implements TestForm {

    private FormClass formClass;
    private final Ids ids;

    private final FormField nameField;
    private final FormField categoryField;
    private final FormField subCategoryField;
    private final FormField rangeField;

    private List<FormInstance> records = null;

    public ActForm() {
        this(new UnitTestingIds());
    }

    public ActForm(Ids ids) {
        this.ids = ids;
        formClass = new FormClass(ids.formId("ACT"));
        formClass.setLabel("Acts");
        formClass.setDatabaseId(ids.databaseId());

        for (FormField field : ids.builtinFields()) {
            formClass.addElement(field);
        }

        nameField = formClass.addField(ids.fieldId("F1"))
                .setCode("NAME")
                .setLabel("Name")
                .setType(TextType.SIMPLE)
                .setRequired(true)
                .setKey(true)
                .setVisible(true);

        categoryField = formClass.addField(ids.fieldId("F2"))
                .setCode("CATEGORY")
                .setLabel("Category")
                .setType(TextType.SIMPLE)
                .setRequired(true)
                .setVisible(true);

        subCategoryField = formClass.addField(ids.fieldId("F3"))
                .setCode("SUBCATEGORY")
                .setLabel("Sub-category")
                .setType(TextType.SIMPLE)
                .setRequired(false)
                .setVisible(true);


        rangeField = formClass.addField(ids.fieldId("F4"))
                .setCode("RANGE")
                .setLabel("Range")
                .setType(TextType.SIMPLE)
                .setRequired(true)
                .setVisible(true);


    }


    @Override
    public ResourceId getFormId() {
        return formClass.getId();
    }

    @Override
    public FormClass getFormClass() {
        return formClass;
    }

    @Override
    public List<FormInstance> getRecords() {
        if(records == null) {
            records = new ArrayList<>();
            records.add(record("Attack", "Attack on Person/Organisation", "Direct Physical Contact", "beating/stabbing"));
            records.add(record("Attack", "Attack on Person/Organisation", "Direct Physical Contact", "suicide attack"));
            records.add(record("Attack", "Attack on Person/Organisation", "Direct Physical Contact", "immolation"));
            records.add(record("Attack", "Attack on Person/Organisation", "Direct Physical Contact", "sexual harassment"));
            records.add(record("Attack", "Attack on Person/Organisation", "Direct Physical Contact", "sexual violence / rape"));
            records.add(record("Attack", "Attack on Person/Organisation", "Direct Physical Contact", "brawl (two-sided)"));
            records.add(record("Attack", "Attack on Person/Organisation", "Direct Physical Contact", "deliberate injuring (torture / mutilation / acid attack / execution of corporeal punishment)"));
            records.add(record("Attack", "Attack on Person/Organisation", "Direct Physical Contact", "other (direct physical contact)"));
            records.add(record("Attack", "Attack on Person/Organisation", "Short Range", "massacre"));
            records.add(record("Attack", "Attack on Person/Organisation", "Short Range", "sniper attack"));
            records.add(record("Attack", "Attack on Person/Organisation", "Short Range", "assassination (short range)"));
            records.add(record("Attack", "Attack on Person/Organisation", "Direct Physical Contact", "beheading/throat-cutting"));
            records.add(record("Attack", "Attack on Person/Organisation", "Short Range", "shooting"));
            records.add(record("Attack", "Attack on Person/Organisation", "Short Range", "ambush"));
            records.add(record("Attack", "Attack on Person/Organisation", "Short Range", "crowd control / EOF"));
            records.add(record("Attack", "Attack on Person/Organisation", "Short Range", "search operation ('capture or kill') / raid / incursion"));
            records.add(record("Attack", "Attack on Person/Organisation", "Short Range", "violent demonstration / riot"));
            records.add(record("Attack", "Attack on Person/Organisation", "Short Range", "accidental discharge"));
            records.add(record("Attack", "Attack on Person/Organisation", "Short Range", "skirmish/crossfire/shootout (two-sided)"));
            records.add(record("Attack", "Attack on Person/Organisation", "Short Range", "clash (two-sided)"));
            records.add(record("Attack", "Attack on Person/Organisation", "Short Range", "other (short range)"));
            records.add(record("Attack", "Attack on Person/Organisation", "Ranged", "one-sided military operation (e.g. cordon and search)"));
            records.add(record("Attack", "Attack on Person/Organisation", "Direct Physical Contact", "hanging/lynching"));
            records.add(record("Attack", "Attack on Person/Organisation", "Ranged", "military engagement (two-sided)"));
            records.add(record("Attack", "Attack on Person/Organisation", "Ranged", "sea-to-sea attack"));
            records.add(record("Attack", "Attack on Person/Organisation", "Ranged", "sea-to-ground attack"));
            records.add(record("Attack", "Attack on Person/Organisation", "Ranged", "sea-to-air attack"));
            records.add(record("Attack", "Attack on Person/Organisation", "Ranged", "ground-to-air attack"));
            records.add(record("Attack", "Attack on Person/Organisation", "Ranged", "ground-to-sea attack"));
            records.add(record("Attack", "Attack on Person/Organisation", "Ranged", "ground-to-ground attack"));
            records.add(record("Attack", "Attack on Person/Organisation", "Ranged", "air-to-air attack"));
            records.add(record("Attack", "Attack on Person/Organisation", "Ranged", "air-to-sea attack"));
            records.add(record("Attack", "Attack on Person/Organisation", "Ranged", "air-to-ground attack"));
            records.add(record("Attack", "Attack on Person/Organisation", "Direct Physical Contact", "asphyxiation/strangulation/drowning"));
            records.add(record("Attack", "Attack on Person/Organisation", "Remote", "non-suicide IED attack"));
            records.add(record("Attack", "Attack on Person/Organisation", "Remote", "premature detonation"));
            records.add(record("Attack", "Attack on Person/Organisation", "Remote", "drone attack"));
            records.add(record("Attack", "Attack on Person/Organisation", "Remote", "other (remote)"));
            records.add(record("Seize/Steal", "", "", "theft/confiscation/fraud/embezzlement (taking of legal or illergal property without the use of violence; regardless if the actor is a state, non-state or criminal)"));
            records.add(record("Seize/Steal", "", "", "robbery/seizure (taking of legal or illegal property involving the use or at least threat of violence, regardless whether the actor is a state, non-state or criminal)"));
            records.add(record("Recover", "", "", "recovery (retrieval of stolen goods by state actors)"));
            records.add(record("Seize/Steal", "", "", "confiscation/freezing of NGO funds (by state actors)"));
            records.add(record("Seize/Steal", "", "", "other (taking of property)"));
            records.add(record("Attack", "Attack on Building/Asset", "", "arson"));
            records.add(record("Attack", "Attack on Person/Organisation", "Direct Physical Contact", "burning"));
            records.add(record("Attack", "Attack on Building/Asset", "", "demolition"));
            records.add(record("Attack", "Attack on Building/Asset", "", "looting / ransacking"));
            records.add(record("Confine", "", "", "deportation"));
            records.add(record("Confine", "", "", "forced recruitment"));
            records.add(record("Confine", "", "", "hijacking (including forced transportation)"));
            records.add(record("Confine", "Abduction", "", "Kidnap"));
            records.add(record("Confine", "Abduction", "", "Detention"));
            records.add(record("Confine", "Arrest", "", "Imprison"));
            records.add(record("Confine", "Arrest", "", "deport"));
            records.add(record("Confine", "Restriction of Movement", "", "travel bans on NGO"));
            records.add(record("Attack", "Attack on Person/Organisation", "Direct Physical Contact", "poisoning"));
            records.add(record("Confine", "Restriction of Movement", "", "access denial towards NGO"));
            records.add(record("Confine", "Restriction of Movement", "", "checkpoint/roadblocks (including VCP)"));
            records.add(record("Confine", "Restriction of Movement", "", "other (restriction of movement)"));
            records.add(record("Confine", "Restriction of Movement", "", "partial restriction of NGO activity"));
            records.add(record("Confine", "Restriction of Movement", "", "complete restriction of NGO activity / ban of NGO"));
            records.add(record("Threaten", "Verbal", "", "spoken threats (e.g. face to face, via telephone or other media)"));
            records.add(record("Threaten", "Written", "", "written threats (e.g. threat letter, e-mail, text message, press article, post in new social media)"));
            records.add(record("Threaten", "Physical", "", "warning shot / weapons test / manoeuvre / demonstration of force (DOF) (further specified by 'means')"));
            records.add(record("Threaten", "Physical", "", "invasive questioning / invasive security checks"));
            records.add(record("Threaten", "Physical", "", "house search"));
            records.add(record("Attack", "Attack on Person/Organisation", "Direct Physical Contact", "assassination (direct physical contact)"));
            records.add(record("Threaten", "Physical", "", "physical intimidation"));
            records.add(record("Threaten", "Physical", "", "protest / civil disobedience (non-violent)"));
            records.add(record("Threaten", "Physical", "", "other (non-verbal threats)"));
            records.add(record("Rule", "NGO Ban", "", "NGO Ban"));
            records.add(record("Rule", "NGO Restriction", "", "NGO Restriction"));
            records.add(record("Rule", "Border", "", "the change of boundaries and the fortification of borders"));
            records.add(record("Rule", "Peace Treaty", "", "peace treaty"));
            records.add(record("Rule", "Ceasefire", "", "ceasefire"));
            records.add(record("Rule", "Alliance", "", "alliance"));
            records.add(record("Rule", "Split", "", "split"));
            records.add(record("Attack", "Attack on Person/Organisation", "Direct Physical Contact", "electrocution"));
            records.add(record("Attack", "Attack on Person/Organisation", "Direct Physical Contact", "stoning/dropping"));
        }
        return records;
    }

    private FormInstance record(String category, String subCategory, String range, String name) {
        FormInstance record = new FormInstance(ids.recordId(getFormId(), records.size()), getFormId());
        record.set(categoryField.getId(), category);
        if(!subCategory.isEmpty()) {
            record.set(subCategoryField.getId(), subCategory);
        }
        if(!range.isEmpty()) {
            record.set(rangeField.getId(), range);
        }
        record.set(nameField.getId(), name);
        return record;
    }

    @Override
    public Supplier<FormInstance> getGenerator() {
        throw new UnsupportedOperationException();
    }
}
