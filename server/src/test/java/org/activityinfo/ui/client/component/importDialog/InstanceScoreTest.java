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
package org.activityinfo.ui.client.component.importDialog;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import org.activityinfo.fixtures.InjectionSupport;
import org.activityinfo.model.formTree.FieldPath;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.FormTreePrettyPrinter;
import org.activityinfo.model.legacy.CuidAdapter;
import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.query.QueryModel;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.promise.Promise;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.ui.client.component.importDialog.model.ImportModel;
import org.activityinfo.ui.client.component.importDialog.model.match.JvmConverterFactory;
import org.activityinfo.ui.client.component.importDialog.model.source.PastedTable;
import org.activityinfo.ui.client.component.importDialog.model.source.SourceRow;
import org.activityinfo.ui.client.component.importDialog.model.strategy.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.google.common.io.Resources.getResource;
import static org.activityinfo.model.legacy.CuidAdapter.field;
import static org.activityinfo.model.legacy.CuidAdapter.locationFormClass;
import static org.activityinfo.promise.PromiseMatchers.assertResolves;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author yuriyz on 5/20/14.
 */
@RunWith(InjectionSupport.class)
public class InstanceScoreTest extends AbstractImporterTest {

    private static final ResourceId ADMINISTRATIVE_UNIT_FIELD = field(locationFormClass(2), CuidAdapter.ADMIN_FIELD);

    @OnDataSet("/dbunit/nfi-import.db.xml")
    @Test
    public void adminEntityScoring() throws IOException {
        setUser(3);

        FormTree formTree = assertResolves(locator.getFormTree(ImportWithMultiClassRangeTest.SCHOOL_FORM_CLASS));
        FormTreePrettyPrinter.print(formTree);

        importModel = new ImportModel(formTree);
        importer = new Importer(locator, formTree, FieldImportStrategies.get(JvmConverterFactory.get()));


        // Step 1: User pastes in data to import
        PastedTable source = new PastedTable(
                Resources.toString(getResource(getClass(), "school-import.csv"), Charsets.UTF_8));
        importModel.setSource(source);

        dumpList("COLUMNS", source.getColumns());

        importModel.setColumnAction(columnIndex("School"), target("Name"));

        // Province is at the root of both hierarchies
        importModel.setColumnAction(columnIndex("Province"), target("Province Name"));

        // Admin hierarchy
        importModel.setColumnAction(columnIndex("District"), target("District Name"));
        importModel.setColumnAction(columnIndex("Territoire"), target("Territoire Name"));
        importModel.setColumnAction(columnIndex("Secteur"), target("Secteur Name"));
        importModel.setColumnAction(columnIndex("Groupement"), target("Secteur Name"));

        // health ministry hierarchy
        importModel.setColumnAction(columnIndex("Zone de Santé"), target("Zone de Santé Name"));

        FormTree.Node rootField = formTree.getRootField(ADMINISTRATIVE_UNIT_FIELD);
        TargetCollector targetCollector = new TargetCollector(rootField);

        Map<TargetSiteId, ColumnAccessor> mappedColumns = importModel.getMappedColumns(rootField.getFieldId());
        List<ColumnAccessor> sourceColumns = Lists.newArrayList();
        Map<FieldPath, Integer> referenceFields = targetCollector.getPathMap(mappedColumns, sourceColumns);

        // Province level
        ColumnSet columnSet = assertResolves(query(referenceFields, ImportWithMultiClassRangeTest.PROVINCE_LEVEL));
        InstanceScoreSource scoreSource = new InstanceScoreSourceBuilder(
                CuidAdapter.adminLevelFormClass(ImportWithMultiClassRangeTest.PROVINCE_LEVEL),
                referenceFields, sourceColumns).build(columnSet);
        InstanceScorer.Score score = score(source.getRows().get(0), scoreSource);
        assertScore(score, "Katanga");

        // District level
        columnSet = assertResolves(query(referenceFields, ImportWithMultiClassRangeTest.DISTRICT_LEVEL));
        scoreSource = new InstanceScoreSourceBuilder(
                CuidAdapter.adminLevelFormClass(ImportWithMultiClassRangeTest.DISTRICT_LEVEL),
                referenceFields, sourceColumns).build(columnSet);
        score = score(source.getRows().get(1), scoreSource);
        assertScore(score, "Katanga");
        assertScore(score, "Tanganika");


        // Territoire level
        columnSet = assertResolves(query(referenceFields, ImportWithMultiClassRangeTest.TERRITOIRE_LEVEL));
        scoreSource = new InstanceScoreSourceBuilder(
                CuidAdapter.adminLevelFormClass(ImportWithMultiClassRangeTest.TERRITOIRE_LEVEL),
                referenceFields, sourceColumns).build(columnSet);
        score = score(source.getRows().get(2), scoreSource);
        assertScore(score, "Katanga");
        assertScore(score, "Tanganika");
        assertScore(score, "Kalemie");
        assertThat(scoreSource.getReferenceInstanceIds().get(score.getBestMatchIndex()), equalTo(ImportWithMultiClassRangeTest.TERRITOIRE_KALEMIE));
    }

    private Promise<ColumnSet> query(Map<FieldPath, Integer> referenceFields, int adminLevel) {
        ResourceId formId = CuidAdapter.adminLevelFormClass(adminLevel);
        QueryModel queryModel = new QueryModel(formId);
        queryModel.selectRecordId().as("_id");
        for (FieldPath fieldPath : referenceFields.keySet()) {
            queryModel.selectField(fieldPath).as(fieldPath.toString());
        }
        return locator.queryTable(queryModel);
    }

    private InstanceScorer.Score score(SourceRow row, InstanceScoreSource scoreSource) {
        return new InstanceScorer(scoreSource).score(row);
    }

    private static void assertScore(InstanceScorer.Score score, String name) {
        for (int i = 0; i < score.getImported().length; i++) {
            String imported = score.getImported()[i];
            if (name.equals(imported) && score.getBestScores()[i] >= 1.0) {
                return;
            }
        }
        throw new RuntimeException("Failed to score : " + name);
    }
}
