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
package org.activityinfo.server.database.hibernate.entity;

import com.google.common.collect.Lists;
import org.activityinfo.json.Json;
import org.activityinfo.json.JsonValue;
import org.activityinfo.model.database.GrantModel;
import org.activityinfo.model.database.Operation;
import org.activityinfo.model.legacy.CuidAdapter;
import org.activityinfo.model.resource.ResourceId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Defines a given user's access to a given database.
 * <p/>
 * Note: Owners of databases do not have UserPermission records. (Is this a good
 * idea?)
 * <p/>
 * Each <code>User</code> belongs to one and only one <code>Partner</code>, and
 * permissions are split between the data that belongs to their partner (
 * <code>View, Edit</code>) and data that belongs to other partners (
 * <code>ViewAll, EditAll</code>)
 *
 * @author Alex Bertram
 */
@Entity @NamedQueries({@NamedQuery(name = "findUserPermissionByUserIdAndDatabaseId",
        query = "select p from UserPermission p where p.database.id = :databaseId and p.user.id = :userId")})
public class UserPermission implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(UserPermission.class.getName());

    private int id;
    private Partner partner;
    private Database database;
    private User user;
    private boolean allowView;
    private boolean allowViewAll;
    private boolean allowEdit;
    private boolean allowEditAll;
    private boolean allowDesign;
    private boolean allowManageUsers;
    private boolean allowManageAllUsers;
    private long version;
    private String model;

    public UserPermission() {
    }

    public UserPermission(Database database, User user) {
        this.database = database;
        this.user = user;
    }

    public UserPermission(UserPermission sourcePermission) {
        this.partner = sourcePermission.partner;
        this.database = sourcePermission.database;
        this.user = sourcePermission.user;
        this.allowView = sourcePermission.allowView;
        this.allowViewAll = sourcePermission.allowViewAll;
        this.allowEdit = sourcePermission.allowEdit;
        this.allowEditAll = sourcePermission.allowEditAll;
        this.allowDesign = sourcePermission.allowDesign;
        this.allowManageUsers = sourcePermission.allowManageUsers;
        this.allowManageAllUsers = sourcePermission.allowManageAllUsers;
        this.version = sourcePermission.version;
        this.model = sourcePermission.model;
    }

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "UserPermissionId", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the Partner to which the <code>user</code> belongs.
     *
     * @return The <code>Partner</code> to which the <code>user</code> belongs
     */
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "PartnerId", nullable = false)
    public Partner getPartner() {
        return this.partner;
    }

    /**
     * Sets the Partner to which the <code>user</code> belongs.
     *
     * @param partner The Partner to which the <code>user</code> belongs.
     */
    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    /**
     * Gets the <code>Database</code> to which these permissions apply.
     *
     * @return The <code>Database</code> to which these permissions apply.
     */
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "DatabaseId", nullable = false, updatable = false)
    public Database getDatabase() {
        return this.database;
    }

    /**
     * Sets the <code>Database</code> to which these permissions apply
     *
     * @param database the <code>Database</code> to which these permissions
     *                 apply.
     */
    public void setDatabase(Database database) {
        this.database = database;
    }

    /**
     * Gets the <code>User</code> to whom these permissions apply
     *
     * @return The <code>User</code> to whom these permissions apply
     */
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "UserId", nullable = false, updatable = false)
    public User getUser() {
        return this.user;
    }

    /**
     * Sets the <code>User</code> to whom these permissions apply.
     *
     * @param user The <code>User</code> to whom these permissions apply.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Sets the permission to view the <code>User</code>'s own da
     *
     * @return True if the user has permission to view their own partner's data
     * in the <code>Database</code>.
     */
    @Column(name = "AllowView", nullable = false)
    public boolean isAllowView() {
        return this.allowView;
    }

    /**
     * Sets the permission to view the database
     *
     * @param allowView True if the user has permission to view their own partner's
     *                  data in the <code>Database</code>.
     */
    public void setAllowView(boolean allowView) {
        this.allowView = allowView;
    }

    /**
     * Gets the permission to view other partners in the database
     *
     * @return True if the user is allowed to view the data of other partners in
     * the database.
     */
    @Column(name = "AllowViewAll", nullable = false)
    public boolean isAllowViewAll() {
        return this.allowViewAll;
    }

    /**
     * Sets the permission to view the data of other partners in the database.
     *
     * @param allowViewAll True if the user is allowed to view the data of other partners
     *                     in the database.
     */
    public void setAllowViewAll(boolean allowViewAll) {
        this.allowViewAll = allowViewAll;
    }

    /**
     * Gets the permission to create/edit data for the User's partner.
     *
     * @return True if the user is allowed to create/edit data for their own
     * partner.
     */
    @Column(name = "AllowEdit", nullable = false)
    public boolean isAllowEdit() {
        return this.allowEdit;
    }

    /**
     * Sets the permission to create/edit data for the User's partner
     *
     * @param allowEdit True if the user is allowed to create/edit data for their own
     *                  partner.
     */
    public void setAllowEdit(boolean allowEdit) {
        this.allowEdit = allowEdit;
    }

    /**
     * Gets the permission to create/edit data for other partners.
     *
     * @return True if the user is allowed to create/edit data for other
     * partners.
     */
    @Column(name = "AllowEditAll", nullable = false)
    public boolean isAllowEditAll() {
        return this.allowEditAll;
    }

    /**
     * Sets the permission to create/edit data for other partners.
     *
     * @param allowEditAll True if the user is allowed to create/edit data for other
     *                     partners.
     */
    public void setAllowEditAll(boolean allowEditAll) {
        this.allowEditAll = allowEditAll;
    }

    /**
     * Gets the permission to design (create/change indicators, etc) the design
     * of the <code>Database</code>
     *
     * @return True if the user has permission to make changes to the design the
     * <code>Database</code>
     */
    @Column(name = "AllowDesign", nullable = false)
    public boolean isAllowDesign() {
        return this.allowDesign;
    }

    /**
     * Sets the permission to make changes to the design of the
     * <code>Database</code>
     *
     * @param allowDesign
     */
    public void setAllowDesign(boolean allowDesign) {
        this.allowDesign = allowDesign;
    }

    /**
     * Gets the permission to add/remove users and modify the View/Edit
     * permissions.
     *
     * @return true if the <code>User</code> has permission to add/remove users
     * for <code>Partner</code> and modify the View/Edit permissions.
     */
    public boolean isAllowManageUsers() {
        return allowManageUsers;
    }

    /**
     * Sets the permission to add/remove users and modify the View/Edit
     * permissions.
     *
     * @param allowManageUsers
     */
    public void setAllowManageUsers(boolean allowManageUsers) {
        this.allowManageUsers = allowManageUsers;
    }

    public boolean isAllowManageAllUsers() {
        return allowManageAllUsers;
    }

    public void setAllowManageAllUsers(boolean allowManageAllUsers) {
        this.allowManageAllUsers = allowManageAllUsers;
    }

    /**
     * Sets the timestamp on which the schema, as visble to the
     * <code>user</code> has last been updated.
     * <p/>
     * An "update" can either be a change to the structure of the database or
     * simply a change to the user's access to the database.
     *
     * @param lastSchemaUpdate The timestamp on which the change was made
     */
    @Transient
    public void setLastSchemaUpdate(Date lastSchemaUpdate) {
        this.version = lastSchemaUpdate.getTime();
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Lob
    @Column
    @Offline
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Transient
    public List<GrantModel> getGrants() {
        if(!this.allowView) {
            return Collections.emptyList();
        }

        if(model == null) {
            GrantModel.Builder databaseGrant = new GrantModel.Builder();
            databaseGrant.setResourceId(CuidAdapter.databaseId(database.getId()));
            setOperations(databaseGrant);
            return Collections.singletonList(databaseGrant.build());
        }

        JsonValue modelObject = Json.parse(model);

        if (!modelObject.hasKey("grants")) {
            LOGGER.severe(() -> "Could not parse permissions model: " + model);
            throw new UnsupportedOperationException("Unsupported model");
        }

        List<GrantModel> grants = Lists.newArrayList();
        modelObject.get("grants").values().forEach(grant -> {
            if (grant.hasKey("folderId")) {
                GrantModel.Builder folderGrant = new GrantModel.Builder();
                folderGrant.setResourceId(ResourceId.valueOf(grant.getString("folderId")));
                setFolderOperations(folderGrant, grant);
                grants.add(folderGrant.build());
            }
        });
        return grants;
    }

    private void setFolderOperations(GrantModel.Builder folderGrantModel, JsonValue folderGrant) {
        if (!folderGrant.hasKey("operations") || folderGrant.get("operations").length() == 0) {
            // Set common operations
            setOperations(folderGrantModel);
            return;
        }

        folderGrant.get("operations")
                .values()
                .forEach(operation -> folderGrantModel.addOperation(Operation.valueOf(operation.asString())));
    }

    private void setOperations(GrantModel.Builder grantModel) {
        if(isAllowViewAll()) {
            grantModel.addOperation(Operation.VIEW);
        } else if(isAllowView()) {
            grantModel.addOperation(Operation.VIEW, getPartnerFilter());
        }
        if(isAllowEditAll()) {
            grantModel.addOperation(Operation.EDIT_RECORD);
        } else if(isAllowEdit()) {
            grantModel.addOperation(Operation.EDIT_RECORD, getPartnerFilter());
        }
        if(isAllowManageAllUsers()) {
            grantModel.addOperation(Operation.MANAGE_USERS);
        } else if(isAllowManageUsers()) {
            grantModel.addOperation(Operation.MANAGE_USERS, getPartnerFilter());
        }
        if(isAllowDesign()) {
            grantModel.addOperation(Operation.CREATE_FORM);
            grantModel.addOperation(Operation.EDIT_FORM);
            grantModel.addOperation(Operation.DELETE_FORM);
        }
    }

    @Transient
    private String getPartnerFilter() {
        return CuidAdapter.partnerFormId(database.getId()).asString() + "==" +
                CuidAdapter.partnerRecordId(partner.getId()).asString();
    }
}
