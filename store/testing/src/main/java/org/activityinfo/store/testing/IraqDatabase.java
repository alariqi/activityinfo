package org.activityinfo.store.testing;

import org.activityinfo.model.database.ResourceBuilder;
import org.activityinfo.model.database.ResourceType;
import org.activityinfo.model.database.UserDatabaseMeta;
import org.activityinfo.model.resource.ResourceId;

public class IraqDatabase {

    public static final ResourceId DATABASE_ID = ResourceId.valueOf("IRAQIDP");
    public static final String LABEL = "2018 IRAQ IDP";

    private static final String[] FOLDERS = new String[] {
            "CCCM", "Education", "Emergency Livelihoods", "Food Security", "Health", "MPC",
            "Protection", "RRM", "SNFI", "WASH", "Reference"
    };

    private static final String[][] FORMS = new String[][] {
            { "CCCM", "CCCM Project" },
            { "Increase access to inclusive, protective and quality formal and non -formal education for conflict affected children, adolescents and youth",
              "Improve the quality of formal and non-formal education for conflict affected children, adolescents and youth",
              "Strengthen the capacity of the education system to plan and deliver a timely, appropriate and evidence-based education response",
              "Education Project"}
    };

    public static UserDatabaseMeta database() {

        UserDatabaseMeta.Builder builder = new UserDatabaseMeta.Builder();
        builder.setDatabaseId(DATABASE_ID);
        builder.setOwner(true);
        builder.setLabel(LABEL);
        builder.setVersion("1");


        for (int i = 0; i < FOLDERS.length; i++) {
            builder.addResource(new ResourceBuilder()
                    .setId(ResourceId.valueOf("FOLDER" + i))
                    .setLabel(FOLDERS[i])
                    .setType(ResourceType.FOLDER)
                    .setParentId(DATABASE_ID)
                    .build());
        }

        for (int i = 0; i < FORMS.length; i++) {
            for (int j = 0; j < FORMS[i].length; j++) {
                ResourceId folderId = ResourceId.valueOf("FOLDER" + i);
                builder.addResource(new ResourceBuilder()
                        .setId(ResourceId.valueOf("FORM" + i + "_" + j))
                        .setLabel(FORMS[i][j])
                        .setType(ResourceType.FORM)
                        .setParentId(folderId)
                        .build());
            }
        }

        return builder.build();

    }
}
