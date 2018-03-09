package org.activityinfo.server.command.handler.binding.dim;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.common.base.Strings;
import org.activityinfo.legacy.shared.command.DimensionType;
import org.activityinfo.legacy.shared.model.ProjectDTO;
import org.activityinfo.legacy.shared.reports.content.DimensionCategory;
import org.activityinfo.legacy.shared.reports.model.Dimension;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formula.CompoundExpr;
import org.activityinfo.model.formula.SymbolNode;
import org.activityinfo.model.legacy.CuidAdapter;
import org.activityinfo.model.query.ColumnModel;
import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.store.mysql.metadata.Activity;

import java.util.Arrays;
import java.util.List;

public class ProjectDimBinding extends DimBinding {


    public static final String PROJECT_ID_COLUMN = "ProjectId";
    public static final String PROJECT_LABEL_COLUMN = "ProjectLabel";

    private static final String PROJECT_FIELD = "project";

    private final Dimension model = new Dimension(DimensionType.Project);

    @Override
    public BaseModelData[] extractFieldData(BaseModelData[] dataArray, ColumnSet columnSet) {
        ColumnView id = columnSet.getColumnView(PROJECT_ID_COLUMN);
        ColumnView label = columnSet.getColumnView(PROJECT_LABEL_COLUMN);

        for (int i=0; i<columnSet.getNumRows(); i++) {
            String projectId = id.getString(i);
            String projectLabel = Strings.nullToEmpty(label.getString(i));

            if (projectId != null && !Strings.isNullOrEmpty(projectLabel)) {
                ProjectDTO project = new ProjectDTO(CuidAdapter.getLegacyIdFromCuid(projectId), projectLabel);
                dataArray[i].set(PROJECT_FIELD, project);
            }
        }

        return dataArray;
    }

    @Override
    public List<ColumnModel> getColumnQuery(FormTree formTree) {
        return getTargetColumnQuery(formTree.getRootFormId());
    }

    @Override
    public List<ColumnModel> getTargetColumnQuery(ResourceId targetFormId) {
        SymbolNode projectField = new SymbolNode(CuidAdapter.field(targetFormId, CuidAdapter.PROJECT_FIELD));

        ColumnModel projectId = new ColumnModel();
        projectId.setFormula(projectField);
        projectId.setId(PROJECT_ID_COLUMN);

        ColumnModel projectLabel = new ColumnModel();
        projectLabel.setFormula(new CompoundExpr(projectField, new SymbolNode("label")));
        projectLabel.setId(PROJECT_LABEL_COLUMN);

        return Arrays.asList(projectId, projectLabel);
    }

    @Override
    public Dimension getModel() {
        return model;
    }

    @Override
    public DimensionCategory[] extractCategories(Activity activity, ColumnSet columnSet) {
        return extractEntityCategories(columnSet, PROJECT_ID_COLUMN, PROJECT_LABEL_COLUMN);
    }

    @Override
    public DimensionCategory extractTargetCategory(Activity activity, ColumnSet columnSet, int rowIndex) {
        return extractEntityCategory(
                columnSet.getColumnView(PROJECT_ID_COLUMN),
                columnSet.getColumnView(PROJECT_LABEL_COLUMN), rowIndex);
    }
}
