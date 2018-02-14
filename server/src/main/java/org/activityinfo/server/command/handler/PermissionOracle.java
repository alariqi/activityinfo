package org.activityinfo.server.command.handler;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.util.Providers;
import org.activityinfo.legacy.shared.AuthenticatedUser;
import org.activityinfo.legacy.shared.exception.IllegalAccessCommandException;
import org.activityinfo.legacy.shared.model.Published;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.legacy.CuidAdapter;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.server.database.hibernate.entity.*;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.activityinfo.model.legacy.CuidAdapter.DATABASE_DOMAIN;

public class PermissionOracle {

    private final Provider<EntityManager> em;

    private static final Logger LOGGER = Logger.getLogger(PermissionOracle.class.getName());

    @Inject
    public PermissionOracle(Provider<EntityManager> em) {
        this.em = em;
    }

    public PermissionOracle(EntityManager em) {
        this(Providers.of(em));
    }

    /**
     * Returns true if the given user is allowed to modify the structure of the
     * database.
     */
    public boolean isDesignAllowed(UserDatabase database, User user) {
        return getPermissionByUser(database, user).isAllowDesign();
    }

    public boolean isViewAllowed(UserDatabase database, User user) {
        UserPermission permission = getPermissionByUser(database, user);
        return permission.isAllowView() || permission.isAllowViewAll();
    }

    public boolean isManagePartnersAllowed(UserDatabase db, User user) {
        UserPermission perm = getPermissionByUser(db, user);
        return perm.isAllowDesign() || perm.isAllowManageAllUsers();
    }

    public void assertDesignPrivileges(UserDatabase database, User user) {
        if (!isDesignAllowed(database, user)) {
            LOGGER.severe(String.format(
                    "User %d does not have design privileges on database %d",
                    user.getId(),
                    database.getId()));
            throw new IllegalAccessCommandException();
        }
    }
    
    public void assertDesignPrivileges(FormClass formClass, User user) {
        assertDesignPrivileges(lookupDatabase(formClass), user);
    }

    private UserDatabase lookupDatabase(FormClass formClass) {
        ResourceId databaseId = formClass.getDatabaseId();
        
        if(databaseId.getDomain() == DATABASE_DOMAIN) {
            return em.get().getReference(UserDatabase.class, CuidAdapter.getLegacyIdFromCuid(databaseId));
        }
        LOGGER.severe(String.format("FormClass %s [%s] with owner %s cannot be matched to " +
                "a database", formClass.getLabel(), formClass.getId(), formClass.getDatabaseId()));
        throw new IllegalArgumentException();
    }

    public void assertManagePartnerAllowed(UserDatabase database, User user) {
        if (!isManagePartnersAllowed(database, user)) {
            LOGGER.severe(String.format(
                    "User %d does not have design or manageAllUsers privileges on database %d",
                    user.getId(),
                    database.getId()));
            throw new IllegalAccessCommandException();
        }
    }

    /**
     * Returns true if the given user is allowed to edit the values of the
     * given site.
     */
    public boolean isEditAllowed(Site site, User user) {
        UserPermission permission = getPermissionByUser(site.getActivity().getDatabase(), user);

        if (permission.isAllowEditAll()) {
            return true;
        }

        if (permission.isAllowEdit()) {
            // without AllowEditAll, edit permission is contingent on the site's partner
            return site.getPartner().getId() == permission.getPartner().getId();
        }

        return false;
    }


    public void assertEditAllowed(Site site, User user) {
        if(!isEditAllowed(site, user)) {
            LOGGER.severe(String.format("User %d does not have permission to edit" +
                    " site %d", user.getId(), site.getId()));
            throw new IllegalAccessCommandException();
        }
    }



    public boolean isEditSiteAllowed(User user, Activity activity, Partner partner) {
        UserPermission permission = getPermissionByUser(activity.getDatabase(), user);
        if(permission.isAllowEditAll()) {
            return true;
        } else if(permission.isAllowEdit()) {
            return partner.getId() == permission.getPartner().getId();
        }
        return false;
    }

    public void assertEditSiteAllowed(User user, Activity activity, Partner partner) {
        if(!isEditSiteAllowed(user, activity, partner)) {
            LOGGER.severe(String.format("User %d does not have permission to edit" +
                    " sites in activity %d and partner %d", user.getId(), activity.getId(), partner.getId()));
            throw new IllegalAccessCommandException();
        }
    }

    /**
     * Returns true if the given user is allowed to edit the values of the
     * given site.
     */
    public boolean isViewAllowed(Site site, User user) {

        if(site.getActivity().getPublished() == Published.ALL_ARE_PUBLISHED.getIndex()) {
            return true;
        }

        UserPermission permission = getPermissionByUser(site.getActivity().getDatabase(), user);

        if (permission.isAllowViewAll()) {
            return true;
        }

        if (permission.isAllowView()) {
            // without AllowViewAll, edit permission is contingent on the site's partner
            return site.getPartner().getId() == permission.getPartner().getId();
        }

        return false;
    }


    public boolean isEditAllowed(Site site, AuthenticatedUser user) {
        return isEditAllowed(site, em.get().getReference(User.class, user.getId()));
    }

    @Nonnull
    public UserPermission getPermissionByUser(UserDatabase database, User user) {

        if (database.getOwner().getId() == user.getId()) {
            // owner has all rights
            UserPermission ownersPermission = new UserPermission();
            ownersPermission.setAllowView(true);
            ownersPermission.setAllowViewAll(true);
            ownersPermission.setAllowDesign(true);
            ownersPermission.setAllowEdit(true);
            ownersPermission.setAllowEditAll(true);
            ownersPermission.setAllowManageAllUsers(true);
            ownersPermission.setAllowManageUsers(true);
            ownersPermission.setUser(user);
            return ownersPermission;
        }

        List<UserPermission> permissions = em.get()
                                             .createQuery(
                                                     "select u from UserPermission u where u.user = :user and u" +
                                                     ".database = :db",
                                                     UserPermission.class)
                                             .setParameter("user", user)
                                             .setParameter("db", database)
                                             .getResultList();

        if (permissions.isEmpty()) {
            // return a permission with nothing enabled
            return new UserPermission();

        } else {
            return permissions.get(0);
        }
    }

    public void assertDeletionAuthorized(Object entity, User user) {
        if(entity instanceof UserDatabase) {
            assertDatabaseDeletionAuthorized(((UserDatabase) entity), user);

        } else if(entity instanceof Site) {
            assertEditAllowed(((Site) entity), user);

        } else if(entity instanceof Activity) {
            assertDesignPrivileges(((Activity) entity).getDatabase(), user);

        } else if(entity instanceof Indicator) {
            assertDesignPrivileges(((Indicator) entity).getActivity().getDatabase(), user);

        } else if(entity instanceof AttributeGroup) {
            assertEditAllowed(((AttributeGroup) entity), user);

        } else if(entity instanceof Attribute) {
            assertEditAllowed(((Attribute) entity).getGroup(), user);

        } else if(entity instanceof Project) {
            assertDesignPrivileges(((Project) entity).getUserDatabase(), user);

        } else if(entity instanceof LockedPeriod) {
            assertDesignPrivileges(((LockedPeriod) entity).getParentDatabase(), user);

        } else if(entity instanceof Target) {
            assertDesignPrivileges(((Target) entity).getUserDatabase(), user);

        } else if(entity instanceof TargetValue) {
            assertDesignPrivileges(((TargetValue) entity).getTarget().getUserDatabase(), user);

        } else if(entity instanceof LocationType) {
            assertDesignPrivileges(((LocationType) entity).getDatabase(), user);

        } else if(entity instanceof Folder) {
            assertDesignPrivileges(((Folder) entity).getDatabase(), user);

        } else {
            LOGGER.log(Level.SEVERE, "Unable to determine permissions for deleting entity of type " + 
                    entity.getClass().getName());
            
            throw new UnsupportedOperationException();
        }
    }

    public void assertDatabaseDeletionAuthorized(UserDatabase entity, User user) {
        if(entity.getOwner().getId() != user.getId()) {
            LOGGER.severe(String.format("User %d is not authorized to delete " +
                    "database %d: it is owned by user %d", user.getId(), entity.getId(), entity.getOwner().getId()));
            throw new IllegalAccessCommandException();
        }
    }
    
    public boolean isEditAllowed(AttributeGroup entity, User user) {
        if(entity.getActivities().isEmpty()) {
            LOGGER.severe("Unable to check authorization to delete attribute group " +
                    entity.getName() + ": there are no associated activities.");
            return false;
        }
        
        for(Activity activity : entity.getActivities()) {
            if(!isDesignAllowed(activity.getDatabase(), user)) {
                return false;
            }
        }
        
        return true;
    }
    
    public void assertEditAllowed(AttributeGroup group, User user) {
        if(!isEditAllowed(group, user)) {
            throw new IllegalAccessCommandException();
        }
    }


    public static PermissionOracle using(EntityManager em) {
        return new PermissionOracle(em);
    }


    public void assertDesignPrivileges(FormClass formClass, AuthenticatedUser user) {
        assertDesignPrivileges(formClass, em.get().getReference(User.class, user.getId()));
    }

    public void assertDesignPrivileges(int databaseId, AuthenticatedUser authenticatedUser) {
        UserDatabase userDatabase = em.get().find(UserDatabase.class, databaseId);
        User user = em.get().find(User.class, authenticatedUser.getId());

        assertDesignPrivileges(userDatabase, user);
    }

    public boolean isViewAllowed(ResourceId databaseId, AuthenticatedUser authenticatedUser) {
        if(databaseId.getDomain() != DATABASE_DOMAIN) {
            return false;
        }

        UserDatabase database = em.get().find(UserDatabase.class, CuidAdapter.getLegacyIdFromCuid(databaseId));
        User user = em.get().find(User.class, authenticatedUser.getId());

        return isViewAllowed(database, user);
    }
}
