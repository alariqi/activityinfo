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
package org.activityinfo.server.endpoint.rest;

import com.google.common.collect.Lists;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.api.view.Viewable;
import org.activityinfo.legacy.shared.AuthenticatedUser;
import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.activityinfo.server.database.hibernate.entity.AdminLevel;
import org.activityinfo.server.database.hibernate.entity.Country;
import org.activityinfo.server.database.hibernate.entity.LocationType;
import org.activityinfo.server.endpoint.rest.model.NewAdminEntity;
import org.activityinfo.server.endpoint.rest.model.NewAdminLevel;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

@Path("/country") 
@Produces(MediaType.APPLICATION_JSON)
public class CountryResource {

    private Country country;

    public CountryResource(Country country) {
        this.country = country;
    }

    @GET 
    @Produces(MediaType.TEXT_HTML)
    public Viewable getPage() {
        return new Viewable("/resource/Country.ftl", country);
    }

    @GET 
    @Produces(MediaType.APPLICATION_JSON)
    public Country getJson() {
        return country;
    }

    @GET 
    @Path("locationTypes") 
    @Produces(MediaType.APPLICATION_JSON)
    public List<LocationType> getLocationTypes() {
        List<LocationType> types = Lists.newArrayList();
        for (LocationType type : country.getLocationTypes()) {
            if (type.getBoundAdminLevel() == null) {
                types.add(type);
            }
        }
        return types;
    }


    @GET 
    @Path("adminLevels") 
    @Produces(MediaType.APPLICATION_JSON)
    public Set<AdminLevel> getAdminLevels() {
        return country.getAdminLevels();
    }

    @POST 
    @Path("adminLevels") 
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postNewLevel(@InjectParam AuthenticatedUser user,
                                 @InjectParam EntityManager em,
                                 NewAdminLevel newLevel) {

        // assertAuthorized(user);

        em.getTransaction().begin();
        em.setFlushMode(FlushModeType.COMMIT);

        AdminLevel level = new AdminLevel();
        level.setCountry(country);
        level.setName(newLevel.getName());
        level.setVersion(1);
        em.persist(level);

        for (NewAdminEntity newEntity : newLevel.getEntities()) {
            AdminEntity entity = new AdminEntity();
            entity.setName(newEntity.getName());
            entity.setLevel(level);
            entity.setCode(newEntity.getCode());
            entity.setBounds(newEntity.getBounds());
            entity.setGeometry(newEntity.getGeometry());
            level.getEntities().add(entity);
            em.persist(entity);
        }

        // create bound location type
        LocationType boundType = new LocationType();
        boundType.setBoundAdminLevel(level);
        boundType.setCountry(level.getCountry());
        boundType.setName(level.getName());
        em.persist(boundType);

        em.getTransaction().commit();

        return Response.ok().build();
    }

}
