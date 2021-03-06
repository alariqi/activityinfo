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
package org.activityinfo.legacy.shared.command;

import com.bedatadriven.rebar.time.calendar.LocalDate;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.collect.Lists;
import org.activityinfo.fixtures.InjectionSupport;
import org.activityinfo.legacy.shared.command.PivotSites.ValueType;
import org.activityinfo.legacy.shared.command.result.Bucket;
import org.activityinfo.legacy.shared.exception.CommandException;
import org.activityinfo.legacy.shared.impl.pivot.PivotTableDataBuilder;
import org.activityinfo.legacy.shared.reports.content.*;
import org.activityinfo.legacy.shared.reports.model.*;
import org.activityinfo.model.date.DateUnit;
import org.activityinfo.model.type.time.Month;
import org.activityinfo.server.command.CommandTestCase2;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.report.util.DateUtilCalendarImpl;
import org.junit.*;
import org.junit.runner.RunWith;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class PivotSitesHandlerTest extends CommandTestCase2 {


    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalMemcacheServiceTestConfig());


    private Set<Dimension> dimensions;
    private Dimension indicatorDim = new Dimension(DimensionType.Indicator);
    private AdminDimension provinceDim = new AdminDimension(OWNER_USER_ID);
    private AdminDimension territoireDim = new AdminDimension(2);
    private final Dimension projectDim = new Dimension(DimensionType.Project);
    private final Dimension targetDim = new Dimension(DimensionType.Target);

    private Filter filter;
    private List<Bucket> buckets;
    private Dimension partnerDim;
    private ValueType valueType = ValueType.INDICATOR;

    private static final int OWNER_USER_ID = 1;
    private static final int NB_BENEFICIARIES_ID = 1;
    private DateDimension yearDim = new DateDimension(DateUnit.YEAR);
    private DateDimension monthDim = new DateDimension(DateUnit.MONTH);
    private final Dimension activityCategoryDim = new Dimension(DimensionType.ActivityCategory);

    @BeforeClass
    public static void setup() {
        Logger.getLogger("org.activityinfo").setLevel(Level.ALL);
        Logger.getLogger("com.bedatadriven.rebar").setLevel(Level.ALL);
    }

    @Before
    public void setUp() throws Exception {
        dimensions = new HashSet<>();
        filter = new Filter();
        helper.setUp();
    }
    
    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void testNoIndicator() {
        withIndicatorAsDimension();
        filteringOnDatabases(1, 2);

        execute();

        assertThat().forIndicator(1).thereIsOneBucketWithValue(15100);
    }

    @Test
    public void testBasic() {
        withIndicatorAsDimension();
        filter.addRestriction(DimensionType.Indicator, 1);

        execute();

        assertThat().forIndicator(1).thereIsOneBucketWithValue(15100);
    }

    @Test
    public void filterBySite() {
        withIndicatorAsDimension();
        filter.addRestriction(DimensionType.Indicator, 1);
        filter.addRestriction(DimensionType.Site, 1);

        execute();

        assertThat().forIndicator(1).thereIsOneBucketWithValue(1500);
    }
    
    @Test
    public void testBasicWithCalculatedIndicators() {
        withIndicatorAsDimension();
        filter.addRestriction(DimensionType.Indicator, 12451);

        execute();
        assertThat().forIndicator(12451).thereIsOneBucketWithValue(17300);
    }

    @Test
    public void calculatedIndicatorsAndActivityCategory() {
        withIndicatorAsDimension();
        dimensions.add(activityCategoryDim);
        filter.addRestriction(DimensionType.Indicator, 12451);

        execute();
        assertThat().forIndicator(12451).thereIsOneBucketWithValue(17300);
        assertThat().forIndicator(12451).with(activityCategoryDim).label("NFI Cluster");
    }

    @Test
    @OnDataSet("/dbunit/sites-simple-target.db.xml")
    public void testTargetsWithCalculatedIndicators() {
        withIndicatorAsDimension();
        dimensions.add(new Dimension(DimensionType.Target));
        filter.addRestriction(DimensionType.Indicator, Arrays.asList(1, 111));

        execute();
        assertThat().forIndicatorTarget(1).thereIsOneBucketWithValue(20000);
        assertThat().forRealizedIndicator(1).thereIsOneBucketWithValue(15100);

        assertThat().forIndicatorTarget(111).thereIsOneBucketWithValue(2000000);
        assertThat().forRealizedIndicator(111).thereIsOneBucketWithValue(1510000);

    }

    @Test
    @OnDataSet("/dbunit/sites-simple-target.db.xml")
    public void testTargetsWithCalculatedIndicatorsByYear() {
        withIndicatorAsDimension();
        dimensions.add(new Dimension(DimensionType.Target));
        dimensions.add(new DateDimension(DateUnit.YEAR));
        filter.addRestriction(DimensionType.Indicator, Arrays.asList(1, 111));

        execute();

        assertThat().forIndicatorTarget(111).forYear(2008).thereIsOneBucketWithValue(2000000);
    }


    @Test
    public void testTotalSiteCount() {
        forTotalSiteCounts();
        filteringOnDatabases(1, 2, 3, 4, 5);

        execute();

        assertThat().thereIsOneBucketWithValue(8);
    }


    @Test
    public void testYears() {
        forTotalSiteCounts();
        filteringOnDatabases(1, 2);
        dimensions.add(new DateDimension(DateUnit.YEAR));

        execute();

        assertThat().forYear(2008).thereIsOneBucketWithValue(3);
        assertThat().forYear(2009).thereIsOneBucketWithValue(4);
    }

    @Test
    public void pivotOnSites() {
        filter.addRestriction(DimensionType.Indicator, 1);
        dimensions.add(new Dimension(DimensionType.Site));

        execute();

        assertThat().thereAre(3).buckets();

        assertThat().forSite(1).thereIsOneBucketWithValue(1500);
        assertThat().forSite(2).thereIsOneBucketWithValue(3600);
        assertThat().forSite(3).thereIsOneBucketWithValue(10000);
    }

    @Test
    public void pivotByDate() {
        filter.addRestriction(DimensionType.Indicator, 5);
        dimensions.add(new DateDimension(DateUnit.DAY));

        execute();
    }

    @Test
    public void testSiteCountOnQuarters() {
                
        /*
        Database #1
        Activity #1 (once)
        Site #1: [2009-01-01, 2009-01-02]
        Site #2: [2009-01-15, 2009-01-16]
        Site #3: [2008-10-05, 2008-10-06]
        Activity #2 (once)
        Site #4: [2008-10-06, 2008-11-06]
        Site #5: [2008-10-05, 2008-10-05]
        
        Database #2
        Activity #3 (monthly)
        Site #9: [2009-01, 2009-02, 2009-03]
            
        Activity #4 (monthly)
        Site #6: [2009-01, 2009-02]
        
        Site #7
        Activity #5 (monthly)
        */
        
        forTotalSiteCounts();
        filteringOnDatabases(1, 2);
        dimensions.add(new DateDimension(DateUnit.QUARTER));

        execute();

        assertThat().forQuarter(2008, 4).thereIsOneBucketWithValue(3);
        assertThat().forQuarter(2009, 1).thereIsOneBucketWithValue(4);
    }

    @Test
    public void testMonths() {
        forTotalSiteCounts();
        filteringOnDatabases(1, 2);
        dimensions.add(new DateDimension(DateUnit.MONTH));
        filter.setEndDateRange(new DateUtilCalendarImpl().yearRange(2009));

        execute();

        assertThat().thereAre(3).buckets();
    }

    @Test
    public void testIndicatorFilter() {
        withIndicatorAsDimension();
        filter.addRestriction(DimensionType.Database, 1);
        filter.addRestriction(DimensionType.Activity, 1);
        filter.addRestriction(DimensionType.Indicator, 1);
        filter.addRestriction(DimensionType.Partner, 2);

        execute();

        assertThat().thereIsOneBucketWithValue(10000);
    }

    @Test
    public void testAdminFilter() {
        withIndicatorAsDimension();
        filter.addRestriction(DimensionType.AdminLevel, 11);
        filter.addRestriction(DimensionType.Indicator, 1);

        execute();

        assertThat().thereIsOneBucketWithValue(3600);
    }


    @Test
    public void testAdminJoin() {
        withAdminDimension(new AdminDimension(1));
        filter.addRestriction(DimensionType.Activity, asList(1, 2));

        execute();

    }



    @Test
    public void testPartnerPivot() {

        withIndicatorAsDimension();
        withPartnerAsDimension();
        filter.addRestriction(DimensionType.Indicator, 1);

        execute();

        assertThat().thereAre(2).buckets();
        assertThat().forPartner(1).thereIsOneBucketWithValue(5100)
                .andItsPartnerLabelIs("NRC");
        assertThat().forPartner(2).thereIsOneBucketWithValue(10000)
                .andItsPartnerLabelIs("Solidarites");
    }

    @Test
    @OnDataSet("/dbunit/sites-simple-target.db.xml")
    public void testTargetPivot() {

        withIndicatorAsDimension();
        dimensions.add(new DateDimension(DateUnit.YEAR));
        dimensions.add(new Dimension(DimensionType.Target));
        filter.addRestriction(DimensionType.Indicator, 1);
        filter.setEndDateRange(new DateRange(new LocalDate(2008, 1, 1), new LocalDate(2008, 12, 31)));
        execute();

        assertThat().thereAre(2).buckets();
        assertThat().forRealizedIndicator(1).thereIsOneBucketWithValue(10000);
        assertThat().forIndicatorTarget(1).thereIsOneBucketWithValue(20000);
    }


    @Test
    @OnDataSet("/dbunit/sites-simple1.db.xml")
    public void testNoTargetPivot() {
        withIndicatorAsDimension();
        dimensions.add(new DateDimension(DateUnit.YEAR));
        dimensions.add(new Dimension(DimensionType.Target));
        filter.addRestriction(DimensionType.Indicator, 1);
        filter.setEndDateRange(new DateRange(new LocalDate(2008, 1, 1), new LocalDate(2008, 12, 31)));
        execute();

        assertThat().thereAre(1).buckets();
    }

    @Test
    public void testFractions() {
        filter.addRestriction(DimensionType.Indicator, 5);

        execute();

        assertThat().thereIsOneBucketWithValue(0.26666666);
    }

    @Test
    public void testAttributePivot() {
        withIndicatorAsDimension();
        withAttributeGroupDim(1);

        filter.addRestriction(DimensionType.Indicator, 1);

        execute();

        assertThat().thereAre(3).buckets();

        assertThat().forAttributeGroupLabeled(1, "Deplacement")
                .thereIsOneBucketWithValue(3600);

        assertThat().forAttributeGroupLabeled(1, "Catastrophe Naturelle")
                .thereIsOneBucketWithValue(10000);
    }

    @Test
    public void testAdminPivot() {
        withIndicatorAsDimension();
        withAdminDimension(provinceDim);
        withAdminDimension(territoireDim);
        filter.addRestriction(DimensionType.Indicator, 1);

        execute();

        assertThat().thereAre(3).buckets();
        assertThat().forProvince(2).thereAre(2).buckets();
        assertThat().forProvince(2).forTerritoire(11)
                .thereIsOneBucketWithValue(3600).with(provinceDim)
                .label("Sud Kivu").with(territoireDim).label("Walungu");
        assertThat().forProvince(4).thereIsOneBucketWithValue(10000);
    }

    @Test
    public void testSiteCount() {

        withIndicatorAsDimension();
        filter.addRestriction(DimensionType.Indicator, 103);

        execute();

        int expectedCount = 1;
        assertBucketCount(expectedCount);
        assertEquals(3, (int) buckets.get(0).doubleValue());
    }

    @Test
    public void testSiteCountOnMonthly() {
        forTotalSiteCounts();
        withAdminDimension(new AdminDimension(1));
        filter.addRestriction(DimensionType.Activity, 3);

        execute();

        assertThat().thereIsOneBucketWithValue(1);
    }


    @Test
    public void testPartnerDimOnMonthlySiteCounts() {
        forTotalSiteCounts();
        withPartnerAsDimension();
        filter.addRestriction(DimensionType.Activity, 3);

        execute();

        assertThat().thereIsOneBucketWithValue(1).andItsPartnerLabelIs("NRC");
    }


    @Test
    public void testPartnerDimOnMonthly() {
        withPartnerAsDimension();
        filter.addRestriction(DimensionType.Activity, 3);

        execute();

        assertThat().thereIsOneBucketWithValue(0.26666).andItsPartnerLabelIs("NRC");
    }


    @Test
    public void projects() {

        withIndicatorAsDimension();
        withProjectAsDimension();
        filter.addRestriction(DimensionType.Database, 1);
        filter.addRestriction(DimensionType.Indicator, 1);

        execute();

        assertBucketCount(2);
        assertThat().forProject(3).thereIsOneBucketWithValue(5100);
        assertThat().forProject(2).thereIsOneBucketWithValue(10000);

    }

    @Test
    public void projectFilters() {

        withIndicatorAsDimension();
        withProjectAsDimension();

        filter.addRestriction(DimensionType.Database, 1);
        filter.addRestriction(DimensionType.Project, 3);
        filter.addRestriction(DimensionType.Indicator, asList(1, 2, 103));

        execute();

        assertBucketCount(3);

        assertThat().forIndicator(1).thereIsOneBucketWithValue(5100);
        assertThat().forIndicator(2).thereIsOneBucketWithValue(1700);
        assertThat().forIndicator(103).thereIsOneBucketWithValue(2);
    }

    private void assertBucketCount(int expectedCount) {
        assertEquals(expectedCount, buckets.size());
    }

    @Test
    public void siteCountWithIndicatorFilter() {

        // PivotSites [dimensions=[Partner], filter=Indicator={ 275 274 278 277
        // 276 129 4736 119 118 125 124 123 122 121 },
        // valueType=TOTAL_SITES]
        withPartnerAsDimension();
        forTotalSiteCounts();
        filter.addRestriction(DimensionType.Indicator,
                Lists.newArrayList(1, 2, 3));

        execute();
    }

    @Test
    public void targetFilter() {
        // Pivoting: PivotSites [dimensions=[Date, Partner, Date, Target,
        // Activity, Indicator],
        // filter=AdminLevel={ 141801 }, Partner={ 130 },
        // Indicator={ 747 746 745 744 749 748 739 738 743 740 119 118 3661 125
        // 124 123 122 121 }, valueType=INDICATOR]

        withPartnerAsDimension();
        dimensions.add(new DateDimension(DateUnit.YEAR));
        dimensions.add(new Dimension(DimensionType.Target));
        dimensions.add(new Dimension(DimensionType.Activity));
        dimensions.add(new Dimension(DimensionType.Indicator));

        filter.addRestriction(DimensionType.AdminLevel, 141801);
        filter.addRestriction(DimensionType.Partner, 130);
        filter.addRestriction(DimensionType.Indicator, 1);

        execute();

    }
    
    @Test
    public void testMixedSumAndAvgIndicators() {
        // The rule is, whenever sum-indicators and avg-indicators are combineed,
        // sum all values together (ignoring the average value)
        filter.addRestriction(DimensionType.Indicator, 5);
        filter.addRestriction(DimensionType.Indicator, 1);
        execute();
        
        assertThat().thereIsOneBucketWithValue(15100.8);
    }

    @Test
    @OnDataSet("/dbunit/sum-avg-mix.db.xml")
    public void testMixedSumAndAvgIndicators2() {
        // The rule is, whenever sum-indicators and avg-indicators are combineed,
        // sum all values together (ignoring the average value)
        filter.addRestriction(DimensionType.Indicator, 5);
        filter.addRestriction(DimensionType.Indicator, 1000);
        execute();

        assertThat().thereIsOneBucketWithValue(15100.8);
    }


    @Test
    public void testIndicatorOrder() {

        withIndicatorAsDimension();

        filter.addRestriction(DimensionType.Indicator, 1);
        filter.addRestriction(DimensionType.Indicator, 2);

        execute();

        assertEquals(2, buckets.size());

        Bucket indicator1 = findBucketsByCategory(buckets, indicatorDim,
                new EntityCategory(1)).get(0);
        Bucket indicator2 = findBucketsByCategory(buckets, indicatorDim,
                new EntityCategory(2)).get(0);

        EntityCategory cat1 = (EntityCategory) indicator1
                .getCategory(indicatorDim);
        EntityCategory cat2 = (EntityCategory) indicator2
                .getCategory(indicatorDim);

        assertEquals(2, cat1.getSortOrder().intValue());
        assertEquals(OWNER_USER_ID, cat2.getSortOrder().intValue());

    }

    @Test
    @OnDataSet("/dbunit/sites-deleted.db.xml")
    public void testDeletedNotIncluded() {

        withIndicatorAsDimension();
        filter.addRestriction(DimensionType.Indicator, 1);

        execute();

        assertEquals(1, buckets.size());
        assertEquals(13600, (int) buckets.get(0).doubleValue());
    }

    @Test
    @OnDataSet("/dbunit/sites-deleted.db.xml")
    public void testDeletedNotLinked() {

        /*
        Database #1
        Activity #1 (once)
        Site #1: I1=1500 (site deleted)
        Site #2: I1=3600
        Site #3: I1=10000
        
        Database #2 (deleted)
        Activity #3 (monthly)
        I400: (no data)
        
        Database #400
        Activity #400 (monthly)
        I401
        
        Links
        I1 -> I400
        I1 -> I401
        */


        withIndicatorAsDimension();
        filter.addRestriction(DimensionType.Indicator, asList(400, 401));

        execute();

        // There should only be one bucket, because indicator 401 belongs to activity 3 -> database 2, 
        // which is deleted
        assertBucketCount(1);
        assertThat().forIndicator(401).thereIsOneBucketWithValue(13600);
    }

    @Test
    @OnDataSet("/dbunit/sites-zeros.db.xml")
    public void zerosIncluded() {

        withIndicatorAsDimension();
        filter.addRestriction(DimensionType.Indicator, asList(5, 6));

        execute();

        assertThat().forIndicator(5).thereIsOneBucketWithValue(0); // average indicator
        assertThat().forIndicator(6).thereIsOneBucketWithValue(0);  // sum indicator
    }

    @Test
    @OnDataSet("/dbunit/sites-null.db.xml")
    public void nullValuesExcluded() {

        withIndicatorAsDimension();
        filter.addRestriction(DimensionType.Indicator, asList(5, 6));

        execute();

        assertBucketCount(0);
    }


    @Test
    @OnDataSet("/dbunit/sites-weeks.db.xml")
    public void testWeeks() {

        final Dimension weekDim = new DateDimension(DateUnit.WEEK_MON);
        dimensions.add(weekDim);

        filter.addRestriction(DimensionType.Indicator, 1);

        execute();

        assertEquals(3, buckets.size());
        assertEquals(3600, (int) findBucketByWeek(buckets, 2011, 52)
                .doubleValue());
        assertEquals(1500, (int) findBucketByWeek(buckets, 2012, 1)
                .doubleValue());
        assertEquals(4142, (int) findBucketByWeek(buckets, 2012, 13)
                .doubleValue());

    }

    @Test
    @OnDataSet("/dbunit/sites-quarters.db.xml")
    public void testQuarters() {

        final Dimension quarterDim = new DateDimension(DateUnit.QUARTER);
        dimensions.add(quarterDim);

        filter.addRestriction(DimensionType.Indicator, 1);

        execute();

        assertEquals(3, buckets.size());
        assertThat().forQuarter(2009, 1).thereIsOneBucketWithValue(1500);
        assertThat().forQuarter(2009, 2).thereIsOneBucketWithValue(3600);
        assertThat().forQuarter(2008, 4).thereIsOneBucketWithValue(10000);
    }

    @Test
    @OnDataSet("/dbunit/sites-linked.db.xml")
    public void testLinked() {
        withIndicatorAsDimension();
        filter.addRestriction(DimensionType.Indicator, 1);
        execute();
        assertThat().forIndicator(1).thereIsOneBucketWithValue(1900);
    }

    @Test
    @OnDataSet("/dbunit/sites-linked2.db.xml")
    public void testLinked2() {
        withIndicatorAsDimension();
        filter.addRestriction(DimensionType.Indicator, 1);
        execute();
        assertThat().forIndicator(1).thereIsOneBucketWithValue(1500 + 400 + 35);
    }


    @Test
    @OnDataSet("/dbunit/sites-linked-monthly.db.xml")
    public void testLinkedMonthlyToOnce() {
        withIndicatorAsDimension();
        filter.addRestriction(DimensionType.Indicator, 1);
        execute();
        assertThat().forIndicator(1).thereIsOneBucketWithValue(1500 + 400 + 35);
    }



    @Test
    @OnDataSet("/dbunit/sites-linked.db.xml")
    public void testLinkedValuesAreDuplicated() {
        withIndicatorAsDimension();
        filter.addRestriction(DimensionType.Indicator, asList(1, 3));
        execute();
        assertThat().forIndicator(1).thereIsOneBucketWithValue(1900);
        assertThat().forIndicator(3).thereIsOneBucketWithValue(1500);
    }

    @Test
    @OnDataSet("/dbunit/sites-linked.db.xml")
    public void testLinkedPartnerSiteCount() {
        withPartnerAsDimension();
        forTotalSiteCounts();
        filteringOnDatabases(1, 2);
        execute();
        assertThat().thereAre(2).buckets();
        assertThat().forPartner(1).thereIsOneBucketWithValue(2).andItsPartnerLabelIs("NRC");
        assertThat().forPartner(2).thereIsOneBucketWithValue(1).andItsPartnerLabelIs("NRC2");
    }
    
    @Test
    @OnDataSet("/dbunit/sites-linked.db.xml")
    public void testLinkedPartnerFilterData() {
        
        /*
        Data:
        ----
        database #1
        activity #1
        site #2: partner=NRC2, I1=400
    
        database #2
        activity #2
        site #1: partner=NRC, I3=1500
    
        links:
        I3 -> I1
        I3 -> I2
    
        ----------
        Query result:
        site1: partner=NRC, I3=1500
        site1: (linked), partner=NRC, I3->I1: 1500, I3->I2: 1500
        site2: partner=NRC2, I1=400
        */

        withPartnerAsDimension();
        filter.addRestriction(DimensionType.Database, asList(1, 2));
        forTotalSiteCounts();
        execute();

        assertThat().thereAre(2).buckets();
        assertThat().forPartner(1).thereIsOneBucketWithValue(2).andItsPartnerLabelIs("NRC");
        assertThat().forPartner(2).thereIsOneBucketWithValue(1).andItsPartnerLabelIs("NRC2");
    }

    @Test
    @OnDataSet("/dbunit/attrib-merge.db.xml")
    public void testAttributesAreMergedAcrossDbByName() {
        filteringOnDatabases(1, 2);
        withIndicatorAsDimension();
        withAttributeGroupDim(1);
        execute();
        assertThat().forAttributeGroupLabeled(1, "Planned").forIndicator(1).thereIsOneBucketWithValue(1500);
        assertThat().forAttributeGroupLabeled(1, "Planned").forIndicator(2).thereIsOneBucketWithValue(3600);
        assertThat().forAttributeGroupLabeled(1, "Completed").forIndicator(2).thereIsOneBucketWithValue(9200);
    }

    @Test
    @OnDataSet("/dbunit/attrib-merge.db.xml")
    public void testAttributeFilter() {
        filteringOnDatabases(1, 2);
        filter.addRestriction(DimensionType.Attribute, 1); // = Planned
        withAttributeGroupDim(1);
        withIndicatorAsDimension();
        dimensions.add(new Dimension(DimensionType.Site));
        execute();
        assertThat().forSite(1).thereIsOneBucketWithValue(1500);
        assertThat().forSite(2).thereIsOneBucketWithValue(3600);
    }

    @Test
    @OnDataSet("/dbunit/attrib-merge.db.xml")
    public void testAttributeFilterMultiple() {
        filteringOnDatabases(1, 2);
        filter.addRestriction(DimensionType.Attribute, 1); // = Planned
        filter.addRestriction(DimensionType.Attribute, 3); // = Completed
        withAttributeGroupDim(1);
        withIndicatorAsDimension();
        execute();
        assertThat().thereAre(0);
    }
    
    @Test
    @OnDataSet("/dbunit/monthly-calc-indicators.db.xml")
    public void testMonthlyCalculatedIndicators() {

        /*
        Database #1
        
        Activity #901 (monthly)
        I7001 = A: quantity
        I7002 = B: quantity
        I7003 = C: A+B
        
        Site #1 @ Location #401 -> Province #2
        ReportingPeriod #11 [2009-01-01, 2009-01-31]
        I7001 = 200
        I7002 = 300
        I7003 = 500 (calculated)
        
        Reportingperiod #12 [2009-02-01, 2009-02-28]
        I7001 = 150
        I7002 = 330
        I7003 = 480 (calculated)
        
        Site #2 @ Location #402 -> Province #3
        ReportingPeriod #21 [2009-01-01, 2009-01-31]
        I7001 = 11
        I7002 = 26
        I7003 = 37 (calculated)
        
        ReportingPeriod #22 [2009-02-01, 2009-02-28]
        I7001 =  99
        I7002 =  55
        I7003 = 154 (calculated)
        */

        withIndicatorAsDimension();
        filteringOnDatabases(1);
        dimensions.add(new DateDimension(DateUnit.MONTH));

        execute();
        assertThat().thereAre(6).buckets();
        assertThat().forMonth(2009, 1).forIndicator(7003).thereIsOneBucketWithValue(537);
        assertThat().forMonth(2009, 2).forIndicator(7003).thereIsOneBucketWithValue(634);
    }

    @Test
    @OnDataSet("/dbunit/monthly-calc-indicators.db.xml")
    public void testMonthlyCalculatedIndicatorsWithPartnerAndYear() {

        filteringOnDatabases(1);
        withIndicatorAsDimension();
        withPartnerAsDimension();
        dimensions.add(yearDim);
        dimensions.add(monthDim);

        execute();
        assertThat().thereAre(6).buckets();
        assertThat().forMonth(2009, 1).forIndicator(7003).thereIsOneBucketWithValue(537);
        assertThat().forMonth(2009, 2).forIndicator(7003).thereIsOneBucketWithValue(634);

        PivotTableReportElement report = new PivotTableReportElement();
        report.setColumnDimensions(Arrays.asList(indicatorDim, partnerDim));
        report.setRowDimensions(Arrays.<Dimension>asList(yearDim, monthDim));

        PivotTableDataBuilder tableDataBuilder = new PivotTableDataBuilder();
        PivotTableData table = tableDataBuilder.build(report.getRowDimensions(), report.getColumnDimensions(),
            buckets);
    }

    @Test
    @OnDataSet("/dbunit/monthly-calc-indicators.db.xml")
    public void testMonthlyCalculatedIndicatorsByProvince() {
        withIndicatorAsDimension();
        filteringOnDatabases(1);
        withAdminDimension(new AdminDimension(1));
        dimensions.add(new DateDimension(DateUnit.MONTH));

        execute();
        assertThat().forMonth(2009, 1).forIndicator(7003).forProvince(2).thereIsOneBucketWithValue(500);
        assertThat().forMonth(2009, 1).forIndicator(7003).forProvince(3).thereIsOneBucketWithValue(37);

        assertThat().forMonth(2009, 2).forIndicator(7003).forProvince(2).thereIsOneBucketWithValue(480);
        assertThat().forMonth(2009, 2).forIndicator(7003).forProvince(3).thereIsOneBucketWithValue(154);
    }


    @Test
    @OnDataSet("/dbunit/monthly-calc-indicators.db.xml")
    public void testMonthlyCalculatedIndicatorsByAttribute() {
        withIndicatorAsDimension();
        filteringOnDatabases(1);
        withAttributeGroupDim(1);
        dimensions.add(new DateDimension(DateUnit.MONTH));

        execute();
        assertThat().forMonth(2009, 1).forIndicator(7003).forAttributeGroupLabeled(1, "B").thereIsOneBucketWithValue(500);
        assertThat().forMonth(2009, 1).forIndicator(7003).forAttributeGroupLabeled(1, "A").thereIsOneBucketWithValue(37);

        assertThat().forMonth(2009, 2).forIndicator(7003).forAttributeGroupLabeled(1, "B").thereIsOneBucketWithValue(480);
        assertThat().forMonth(2009, 2).forIndicator(7003).forAttributeGroupLabeled(1, "A").thereIsOneBucketWithValue(154);

        PivotTableReportElement report = new PivotTableReportElement();
        report.setColumnDimensions(Arrays.asList(indicatorDim, new AttributeGroupDimension(1)));
        report.setRowDimensions(Arrays.<Dimension>asList(yearDim, monthDim));

        PivotTableDataBuilder tableDataBuilder = new PivotTableDataBuilder();
        PivotTableData table = tableDataBuilder.build(report.getRowDimensions(), report.getColumnDimensions(),
            buckets);
    }
    
    @Test
    public void noVisibility() {
        
        // Christian's access has been revoked
        int christian = 5;
        
        filteringOnDatabases(1);
        dimensions.add(new Dimension(DimensionType.Database));
        withIndicatorAsDimension();
        
        execute(christian);
        
        assertBucketCount(0);
    }
    
    @Test
    public void partnerLimitedVisibility() {
        // Bavon can only view NRC
        int bavon = 2;
        int nrc = 1;
        int solidarite = 2;
        
        filteringOnDatabases(1);
        withIndicatorAsDimension();
        withPartnerAsDimension();
        
        execute(bavon);
        
        assertThat().forPartner(solidarite).thereAre(0);
        assertThat().forPartner(nrc).thereAre(4);
        assertBucketCount(5);
    }
    
    @Test
    @OnDataSet("/dbunit/attrib-multi.db.xml")
    public void filterOnMultiAttribute() {
        int soap = 5;
        int numBeneficiares = 1;

        filter.addRestriction(DimensionType.Indicator, numBeneficiares);
        filter.addRestriction(DimensionType.Attribute, soap);
        
        execute();
        
        assertThat().thereIsOneBucketWithValue(1500);
        
    }

    @Test
    @Ignore
    @OnDataSet("/dbunit/attrib-multi.db.xml")
    public void pivotOnMultiAttribute() {
        int contenuDuKit = 2;
        int casserole = 4;
        int soap = 5;
        int numBeneficiares = 1;

        filter.addRestriction(DimensionType.Indicator, numBeneficiares);
        withAttributeGroupDim(contenuDuKit);
        
        execute();
        
        assertThat().forAttributeGroupLabeled(contenuDuKit, "Casserole").thereIsOneBucketWithValue(1500);
        
    }
    
    @Test
    public void filterMonthlyReportsOnSiteId() {
        int utilizationRate = 5;
        int csNgeshwe = 9;
        
        filter.addRestriction(DimensionType.Indicator, utilizationRate);
        filter.addRestriction(DimensionType.Site, csNgeshwe);
        
        dimensions.add(new Dimension(DimensionType.Site));
        dimensions.add(new DateDimension(DateUnit.MONTH));

        execute();
        
        assertThat().forSite(csNgeshwe).thereAre(3).buckets();
    }
    
    
    private void filteringOnDatabases(Integer... databaseIds) {
        filter.addRestriction(DimensionType.Database, asList(databaseIds));
    }

    private List<Bucket> findBucketsByCategory(List<Bucket> buckets,
                                               Dimension dim, DimensionCategory cat) {
        List<Bucket> matching = new ArrayList<Bucket>();
        for (Bucket bucket : buckets) {
            if (bucket.getCategory(dim).equals(cat)) {
                matching.add(bucket);
            }
        }
        return matching;
    }

    private Bucket findBucketByQuarter(List<Bucket> buckets, int year,
                                       int quarter) {
        for (Bucket bucket : buckets) {
            QuarterCategory category = (QuarterCategory) bucket
                    .getCategory(new DateDimension(DateUnit.QUARTER));
            if (category.getYear() == year && category.getQuarter() == quarter) {
                return bucket;
            }
        }
        throw new AssertionError("No bucket for " + year + "q" + quarter);
    }

    private Bucket findBucketByWeek(List<Bucket> buckets, int year, int week) {
        for (Bucket bucket : buckets) {
            WeekCategory category = (WeekCategory) bucket
                    .getCategory(new DateDimension(DateUnit.WEEK_MON));
            if (category != null && category.getYear() == year
                    && category.getWeek() == week) {
                return bucket;
            }
        }
        throw new AssertionError("No bucket for " + year + " W " + week);
    }

    private void forTotalSiteCounts() {
        valueType = valueType.TOTAL_SITES;
    }

    private void withIndicatorAsDimension() {
        dimensions.add(indicatorDim);
    }

    private void withProjectAsDimension() {
        dimensions.add(projectDim);
    }

    private void withAdminDimension(AdminDimension adminDimension) {
        dimensions.add(adminDimension);
    }

    private void withPartnerAsDimension() {
        partnerDim = new Dimension(DimensionType.Partner);
        dimensions.add(partnerDim);
    }

    private void withPoints() {
    }

    private void withAttributeGroupDim() {
        dimensions.add(new Dimension(DimensionType.AttributeGroup));
    }

    private void withAttributeGroupDim(int groupId) {
        dimensions.add(new AttributeGroupDimension(groupId));
    }

    private void execute() {
        execute(OWNER_USER_ID);
    }
    private void execute(int userId) {
        setUser(userId);
        try {
            PivotSites pivot = new PivotSites(dimensions, filter);
            pivot.setValueType(valueType);
            buckets = execute(pivot).getBuckets();
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Buckets = [");
        for (Bucket bucket : buckets) {
            System.out.print("  { Value: " + bucket.doubleValue());
            for (Dimension dim : bucket.dimensions()) {
                DimensionCategory cat = bucket.getCategory(dim);
                System.out.print("\n    " + dim.toString() + ": ");
                System.out.print("" + cat);
            }
            System.out.println("\n  }");
        }
        System.out.print("]\n");
    }

    public AssertionBuilder assertThat() {
        return new AssertionBuilder();
    }

    private class AssertionBuilder {
        List<Bucket> matchingBuckets = new ArrayList<Bucket>(buckets);
        StringBuilder criteria = new StringBuilder();

        Object predicate;

        public AssertionBuilder forIndicator(int indicatorId) {
            criteria.append(" with indicator ").append(indicatorId);
            filter(indicatorDim, indicatorId);
            return this;
        }


        public AssertionBuilder forIndicatorTarget(int indicatorId) {
            criteria.append(" with target for indicator ").append(indicatorId);
            filter(indicatorDim, indicatorId);
            filter(targetDim, TargetCategory.TARGETED);
            return this;
        }


        public AssertionBuilder forRealizedIndicator(int indicatorId) {
            criteria.append(" with target for indicator ").append(indicatorId);
            filter(indicatorDim, indicatorId);
            filter(targetDim, TargetCategory.REALIZED);
            return this;
        }


        public AssertionBuilder forYear(int year) {
            criteria.append(" in year ").append(year);
            filter(new DateDimension(DateUnit.YEAR), Integer.toString(year));
            return this;
        }


        public AssertionBuilder forMonth(int year, int monthOfYear) {
            criteria.append(" in month ").append(new Month(year, monthOfYear));
            filter(new DateDimension(DateUnit.MONTH), new MonthCategory(year, monthOfYear));
            return this;
        }

        public AssertionBuilder forSite(int siteId) {
            criteria.append(" for site id ").append(siteId);

            filter(new Dimension(DimensionType.Site), siteId);
            return this;
        }

        public AssertionBuilder forQuarter(int year, int quarter) {
            criteria.append(" in quarter ").append(year)
                    .append("Q").append(quarter).append(" ");
            filter(new DateDimension(DateUnit.QUARTER), year + "Q" + quarter);
            return this;
        }

        public AssertionBuilder forProject(int projectId) {
            criteria.append(" with project ").append(projectId);
            filter(projectDim, projectId);
            return this;
        }

        public AssertionBuilder forPartner(int partnerId) {
            criteria.append(" with partner ").append(partnerId);
            filter(partnerDim, partnerId);
            return this;
        }

        public AssertionBuilder forProvince(int provinceId) {
            criteria.append(" with province ").append(provinceId);
            filter(provinceDim, provinceId);
            return this;
        }

        public AssertionBuilder forTerritoire(int territoireId) {
            criteria.append(" with territoire ").append(territoireId);
            filter(territoireDim, territoireId);
            return this;
        }

        public AssertionBuilder forLocation(int locationId) {
            criteria.append(" with location id=").append(locationId);
            filter(new Dimension(DimensionType.Location), locationId);
            return this;
        }

        public AssertionBuilder forAttributeGroupLabeled(int groupId,
                                                         String label) {
            criteria.append(" with a dimension labeled '").append(label)
                    .append("'");
            filter(new AttributeGroupDimension(groupId), label);
            return this;
        }

        private void filter(Dimension dim, String label) {
            ListIterator<Bucket> it = matchingBuckets.listIterator();
            while (it.hasNext()) {
                Bucket bucket = it.next();
                DimensionCategory category = bucket.getCategory(dim);
                if (category == null || !category.getLabel().equals(label)) {
                    it.remove();
                }
            }
        }

        private void filter(Dimension dim, int id) {
            ListIterator<Bucket> it = matchingBuckets.listIterator();
            while (it.hasNext()) {
                Bucket bucket = it.next();
                DimensionCategory category = bucket.getCategory(dim);
                if (!(category instanceof EntityCategory) ||
                        ((EntityCategory) category).getId() != id) {

                    it.remove();
                }
            }
        }

        private void filter(Dimension dim, DimensionCategory expectedCategory) {
            ListIterator<Bucket> it = matchingBuckets.listIterator();
            while (it.hasNext()) {
                Bucket bucket = it.next();
                if (!Objects.equals(bucket.getCategory(dim), expectedCategory)) {
                    it.remove();
                }
            }
        }

        private String description(String assertion) {
            String s = assertion + " " + criteria.toString();
            return s.trim();
        }

        public AssertionBuilder thereAre(int predicate) {
            this.predicate = predicate;
            return this;
        }

        public AssertionBuilder with(Dimension predicate) {
            this.predicate = predicate;
            return this;
        }

        public AssertionBuilder buckets() {
            bucketCountIs((Integer) predicate);
            return this;
        }

        public AssertionBuilder label(String label) {
            Dimension dim = (Dimension) predicate;
            assertEquals(description(dim.toString() + " label of only bucket"),
                    label,
                    matchingBuckets.get(0).getCategory(dim).getLabel());
            return this;
        }

        public AssertionBuilder bucketCountIs(int expectedCount) {
            assertEquals(description("count of buckets"), expectedCount,
                    matchingBuckets.size());
            return this;
        }

        public AssertionBuilder thereIsOneBucketWithValue(double expectedValue) {
            bucketCountIs(OWNER_USER_ID);
            assertEquals(description("value of only bucket"), expectedValue,
                    matchingBuckets.get(0).doubleValue(), 0.001);
            return this;
        }

        public AssertionBuilder andItsPartnerLabelIs(String label) {
            bucketCountIs(1);
            with(partnerDim).label(label);
            return this;
        }
    }

}
