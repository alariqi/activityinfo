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
package org.activityinfo.server.digest.geo;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.activityinfo.legacy.shared.command.DimensionType;
import org.activityinfo.legacy.shared.command.Filter;
import org.activityinfo.legacy.shared.command.GenerateElement;
import org.activityinfo.legacy.shared.command.GetSchema;
import org.activityinfo.legacy.shared.reports.content.MapContent;
import org.activityinfo.legacy.shared.reports.model.MapReportElement;
import org.activityinfo.legacy.shared.reports.model.clustering.AutomaticClustering;
import org.activityinfo.legacy.shared.reports.model.labeling.ArabicNumberSequence;
import org.activityinfo.legacy.shared.reports.model.layers.BubbleMapLayer;
import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.entity.Database;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.digest.DigestModelBuilder;
import org.activityinfo.server.digest.UserDigest;
import org.activityinfo.server.digest.geo.GeoDigestModel.DatabaseModel;
import org.activityinfo.server.generated.GeneratedResource;
import org.activityinfo.server.generated.StorageProvider;
import org.activityinfo.server.report.renderer.image.ImageMapRenderer;
import org.activityinfo.server.util.date.DateFormatter;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class GeoDigestModelBuilder implements DigestModelBuilder {
    private static final String BUBBLE_COLOR = "67a639";
    private static final int BUBBLE_SIZE = 20;

    private static final Logger LOGGER = Logger.getLogger(GeoDigestModelBuilder.class.getName());

    private final Provider<EntityManager> entityManager;
    private final DispatcherSync dispatcher;
    private final ImageMapRenderer imageMapRenderer;
    private final StorageProvider storageProvider;

    @Inject
    public GeoDigestModelBuilder(Provider<EntityManager> entityManager,
                                 DispatcherSync dispatcher,
                                 ImageMapRenderer imageMapRenderer,
                                 StorageProvider storageProvider) {
        this.entityManager = entityManager;
        this.dispatcher = dispatcher;
        this.imageMapRenderer = imageMapRenderer;
        this.storageProvider = storageProvider;
    }

    @Override
    public GeoDigestModel createModel(UserDigest userDigest) throws IOException {

        GeoDigestModel model = new GeoDigestModel(userDigest);

        List<Database> databases = findDatabases(userDigest.getUser());
        LOGGER.finest("found " + databases.size() + " database(s) for user " + userDigest.getUser().getId());

        if (!databases.isEmpty()) {
            model.setSchemaDTO(dispatcher.execute(new GetSchema()));

            for (Database database : databases) {
                createDatabaseModel(model, database);
            }
        }

        return model;
    }

    private void createDatabaseModel(GeoDigestModel model, Database database) throws IOException {

        DatabaseModel databaseModel = new DatabaseModel(model, database);

        List<Integer> siteIds = findSiteIds(database, model.getUserDigest().getFrom());

        LOGGER.finest("rendering geo digest for user " + model.getUserDigest().getUser().getId() + " and database " + database.getId() +
                      " - found " + siteIds.size() + " site(s) that were edited since " +
                      DateFormatter.formatDateTime(model.getUserDigest().getFrom()));

        if (!siteIds.isEmpty()) {
            MapReportElement reportModel = new MapReportElement();
            reportModel.setMaximumZoomLevel(9);

            BubbleMapLayer layer = createLayer(siteIds);
            reportModel.setLayers(layer);

            MapContent content = dispatcher.execute(new GenerateElement<MapContent>(reportModel));
            databaseModel.setContent(content);

            if (!content.getMarkers().isEmpty()) {
                reportModel.setContent(content);

                GeneratedResource storage = storageProvider.create("image/png", "map.png");
                try(OutputStream outputStream = storage.openOutputStream()) {
                    imageMapRenderer.render(reportModel, outputStream);
                }
                databaseModel.setUrl(storage.getDownloadUri());
            }
        }
    }

    private BubbleMapLayer createLayer(List<Integer> siteIds) {
        Filter filter = new Filter();
        filter.addRestriction(DimensionType.Site, siteIds);

        BubbleMapLayer layer = new BubbleMapLayer(filter);
        layer.setLabelSequence(new ArabicNumberSequence());
        layer.setClustering(new AutomaticClustering());
        layer.setMinRadius(BUBBLE_SIZE);
        layer.setMaxRadius(BUBBLE_SIZE);
        layer.setBubbleColor(BUBBLE_COLOR);
        return layer;
    }

    /**
     * @return all UserDatabases for the contextual user where the user is the database owner, or where the database has
     * a UserPermission for the specified user with allowView set to true. If the user happens to have his
     * emailnotification preference set to false, an empty list is returned.
     */
    @VisibleForTesting @SuppressWarnings("unchecked") List<Database> findDatabases(User user) {
        // sanity check
        if (!user.isEmailNotification()) {
            return new ArrayList<>();
        }

        Query query = entityManager.get()
                                   .createQuery("select distinct d from Database d left join d.userPermissions p " +
                                                "where (d.owner = :user or (p.user = :user and p.allowView = true)) " +
                                                "and d.dateDeleted is null " +
                                                "order by d.name");
        query.setParameter("user", user);

        return query.getResultList();
    }

    /**
     * @param database the database the sites should be linked to (via an activity)
     * @param from     the timestamp (millis) to start searching from for edited sites
     * @return the siteIds linked to the specified database that were edited since the specified timestamp
     */
    @VisibleForTesting @SuppressWarnings("unchecked") List<Integer> findSiteIds(Database database, long from) {

        Query query = entityManager.get().createQuery("select distinct s.id from Site s " +
                                                      "join s.siteHistories h " +
                                                      "where s.activity.database = :database " +
                                                      "and h.timeCreated >= :from");
        query.setParameter("database", database);
        query.setParameter("from", from);

        return query.getResultList();
    }
}
