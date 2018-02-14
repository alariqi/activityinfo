package org.activityinfo.server.command.handler;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import org.activityinfo.legacy.shared.command.UpdateMonthlyReports;
import org.activityinfo.legacy.shared.command.result.CommandResult;
import org.activityinfo.legacy.shared.command.result.VoidResult;
import org.activityinfo.legacy.shared.exception.CommandException;
import org.activityinfo.legacy.shared.exception.IllegalAccessCommandException;
import org.activityinfo.legacy.shared.exception.LockAcquisitionException;
import org.activityinfo.model.legacy.KeyGenerator;
import org.activityinfo.model.type.time.Month;
import org.activityinfo.server.database.hibernate.entity.*;
import org.activityinfo.server.event.sitehistory.ChangeType;
import org.activityinfo.server.event.sitehistory.SiteHistoryProcessor;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

import static org.activityinfo.legacy.shared.model.IndicatorDTO.getPropertyName;

/**
 * @author Alex Bertram
 * @see org.activityinfo.legacy.shared.command.UpdateMonthlyReports
 */
public class UpdateMonthlyReportsHandler implements CommandHandler<UpdateMonthlyReports> {

    private final String lockKeyRoot = "site";
    private final double lockTimeout = 7;

    private final EntityManager em;
    private final KeyGenerator keyGenerator;
    private final SiteHistoryProcessor siteHistoryProcessor;
    private final PermissionOracle permissionOracle;

    @Inject
    public UpdateMonthlyReportsHandler(EntityManager em,
                                       KeyGenerator keyGenerator,
                                       SiteHistoryProcessor siteHistoryProcessor) {
        this.em = em;
        this.keyGenerator = keyGenerator;
        this.siteHistoryProcessor = siteHistoryProcessor;
        this.permissionOracle = new PermissionOracle(em);
    }

    @Override
    public CommandResult execute(UpdateMonthlyReports cmd, User user) throws CommandException {

        // Phantom Row issue occurs when attempting to update Monthly ReportingPeriods concurrently.
        // To prevent this, we introduce a locking mechanism to prevent simultaneous insertions into table which result
        // in duplicate reporting periods on the given site.
        // Once we have acquired a lock, we can then safely execute the command


        if (!acquireLock(cmd.getSiteId())) {
            throw new LockAcquisitionException("Cannot acquire lock for site " + cmd.getSiteId());
        }

        try {

            Site site = em.find(Site.class, cmd.getSiteId());
            if (site == null) {
                throw new CommandException(cmd, "site " + cmd.getSiteId() + " not found for user " + user.getEmail());
            }

            if (!permissionOracle.isEditAllowed(site, user)) {
                throw new IllegalAccessCommandException("Not authorized to modify sites");
            }

            Map<Month, ReportingPeriod> periods = Maps.newHashMap();
            Map<String, Object> siteHistoryChangeMap = createChangeMap();


            for (ReportingPeriod period : site.getReportingPeriods()) {
                periods.put(HandlerUtil.monthFromRange(period.getDate1(), period.getDate2()), period);
            }

            // update tables in consistent order to avoid deadlocks

            // First create any new reporting periods needed

            for (UpdateMonthlyReports.Change change : cmd.getChanges()) {
                if (!periods.containsKey(change.getMonth())) {

                    ReportingPeriod period = new ReportingPeriod(site);
                    period.setId(keyGenerator.generateInt());

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, change.getMonth().getYear());
                    calendar.set(Calendar.MONTH, change.getMonth().getMonth() - 1);
                    calendar.set(Calendar.DATE, 1);
                    period.setDate1(calendar.getTime());

                    calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
                    period.setDate2(calendar.getTime());

                    em.persist(period);

                    periods.put(change.getMonth(), period);
                }
            }

            // Now update indicator values

            for (UpdateMonthlyReports.Change change : cmd.getChanges()) {

                updateIndicatorValue(em,
                        periods.get(change.getMonth()),
                        change.getIndicatorId(),
                        change.getValue(), false);

                siteHistoryChangeMap.put(getPropertyName(change.getIndicatorId(), change.getMonth()), change.getValue());
            }

            // update the timestamp on the site entity so changes get picked up
            // by the synchro mechanism
            site.setVersion(site.getActivity().incrementSiteVersion());

            siteHistoryProcessor.persistHistory(site, user, ChangeType.UPDATE, siteHistoryChangeMap);

        } finally {
            releaseLock(cmd.getSiteId());
        }

        return new VoidResult();
    }

    /**
     * <p>Attempts to acquire a lock on the database for the update of site {@code siteId}
     * with the lock key "siteId.<{@code siteId}>", and a timeout of {@code lockTimeout} seconds.
     *
     * @param siteId
     * @return TRUE if lock is acquired. FALSE otherwise.
     *
     * @see <a href="https://dev.mysql.com/doc/refman/5.7/en/miscellaneous-functions.html#function_get-lock">GET_LOCK() documentation</a>
     */
    private boolean acquireLock(int siteId) {
        String lockKey = lockKeyRoot + "." + siteId;

        BigInteger lockResult = (BigInteger) em
                .createNativeQuery("SELECT GET_LOCK(?1, ?2)")
                .setParameter(1, lockKey)
                .setParameter(2, lockTimeout)
                .getSingleResult();

        return Objects.equals(lockResult,BigInteger.valueOf(1));
    }

    private void releaseLock(int siteId) {
        String lockKey = lockKeyRoot + "." + siteId;

        em.createNativeQuery("SELECT RELEASE_LOCK(?1)")
                .setParameter(1, lockKey);
    }

    public void updateIndicatorValue(EntityManager em,
                                     ReportingPeriod period,
                                     int indicatorId,
                                     Double value,
                                     boolean creating) {

        if (value == null && !creating) {
            int rowsAffected = em.createQuery(
                    "delete from IndicatorValue v where v.indicator.id = ?1 and v.reportingPeriod.id = ?2")
                                 .setParameter(1, indicatorId)
                                 .setParameter(2, period.getId())
                                 .executeUpdate();

            assert rowsAffected <= 1 : "whoops, deleted too many";

        } else if (value != null) {
            int rowsAffected = 0;

            if (!creating) {
                rowsAffected = em.createQuery("update IndicatorValue v set v.value = ?1 where " +
                                              "v.indicator.id = ?2 and " +
                                              "v.reportingPeriod.id = ?3")
                                 .setParameter(1, value)
                                 .setParameter(2, indicatorId)
                                 .setParameter(3, period.getId())
                                 .executeUpdate();
            }

            if (rowsAffected == 0) {
                IndicatorValue iValue = new IndicatorValue(period,
                        em.getReference(Indicator.class, indicatorId),
                        value);
                em.persist(iValue);
            }
        }
    }

    private Map<String, Object> createChangeMap() {
        return Maps.newTreeMap(new Comparator<String>() {
            @Override
            // comparing eg. I345M2009-7, first part as string, part after the dash as number
            public int compare(String o1, String o2) {
                int result = o1.substring(0, o1.indexOf('-')).compareToIgnoreCase(o2.substring(0, o2.indexOf('-')));
                if (result == 0) {
                    String m1 = o1.substring(o1.indexOf('-') + 1);
                    String m2 = o2.substring(o2.indexOf('-') + 1);
                    result = Integer.valueOf(m1).compareTo(Integer.valueOf(m2));
                }
                return result;
            }
        });
    }
}
