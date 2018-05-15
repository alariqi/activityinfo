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
package org.activityinfo.i18n.shared;

import com.google.gwt.i18n.client.Constants;

/**
 *
 */
public interface UiConstants extends Constants {

    @DefaultStringValue("Folder Assignment Error")
    String noFolderAssignmentTitle();

    @DefaultStringValue("User must be assigned to a Folder or Folders")
    String noFolderAssignmentMessage();

    @DefaultStringValue("Permission Assignment Error")
    String permissionAssignmentErrorTitle();

    @DefaultStringValue("Cannot give greater permissions than you currently have")
    String permissionAssignmentErrorMessage();

    @DefaultStringValue("Permission/Folder editing locked as user has greater permissions than you have")
    String permissionEditingLockedWarning();

    @DefaultStringValue("Looking for Categories?")
    String categoryInfo();

    @DefaultStringValue("Copy addresses to clipboard")
    String CopyAddressToClipBoard();

    @DefaultStringValue("Forms")
    String activities();

    @DefaultStringValue("Form")
    String activity();

    @DefaultStringValue("Form")
    String form();

    @DefaultStringValue("Forms")
    String forms();

    @DefaultStringValue("Add")
    String add();

    @DefaultStringValue("Add Chart")
    String addChart();

    @DefaultStringValue("Add")
    String addItem();

    @DefaultStringValue("Add Layer")
    String addLayer();

    @DefaultStringValue("Add new site")
    String addLocation();

    @DefaultStringValue("If you can't find the site listed here, you can add a new site.")
    String addLocationDescription();

    @DefaultStringValue("Add Map")
    String addMap();

    @DefaultStringValue("Add Partner to Program")
    String addPartner();

    @DefaultStringValue("Add a project")
    String addProject();

    @DefaultStringValue("Add Pivot Table")
    String addTable();

    @DefaultStringValue("Add Text")
    String addText();

    @DefaultStringValue("Add a timelock")
    String addTimeLock();

    @DefaultStringValue("Add User")
    String addUser();

    @DefaultStringValue("Administrative Unit")
    String adminEntities();

    @DefaultStringValue("Aggregation Method")
    String aggregationMethod();

    @DefaultStringValue("All")
    String all();

    @DefaultStringValue("All are published")
    String allArePublished();

    @DefaultStringValue("All dates")
    String allDates();

    @DefaultStringValue("Design")
    String allowDesign();

    @DefaultStringValue("Allow user to modify the structure of forms")
    String allowDesignLong();

    @DefaultStringValue("Edit")
    String allowEdit();

    @DefaultStringValue("Edit All")
    String allowEditAll();

    @DefaultStringValue("Allow user to edit form submissions of all partners")
    String allowEditAllLong();

    @DefaultStringValue("Allow user to create and edit form submissions")
    String allowEditLong();

    @DefaultStringValue("Manage all users")
    String allowManageAllUsers();

    @DefaultStringValue("Manage Users")
    String allowManageUsers();

    @DefaultStringValue("View")
    String allowView();

    @DefaultStringValue("View All")
    String allowViewAll();

    @DefaultStringValue("Allow user to view form submissions of all partners")
    String allowViewAllLong();

    @DefaultStringValue("Allow user to view form submissions list")
    String allowViewLong();

    @DefaultStringValue("Downloading application files")
    String appCacheProgress();

    @DefaultStringValue("Application version")
    String appVersion();

    @DefaultStringValue("Apply")
    String apply();

    @DefaultStringValue("Attach File")
    String attachFile();

    @DefaultStringValue("Attachments")
    String attachment();

    @DefaultStringValue("Attributes")
    String attributes();

    @DefaultStringValue("Automatic")
    String automatic();

    @DefaultStringValue("Average")
    String average();

    @DefaultStringValue("Axe")
    String axe();

    @DefaultStringValue("« Back")
    String backButton();

    @DefaultStringValue("Bars")
    String bars();

    @DefaultStringValue("Basemap")
    String basemap();

    @DefaultStringValue("Boundaries")
    String boundaries();

    @DefaultStringValue("OK")
    String ok();

    @DefaultStringValue("Cancel")
    String cancel();

    @DefaultStringValue("Category")
    String category();

    @DefaultStringValue("Change site")
    String changeLocation();

    @DefaultStringValue("change")
    String changeTitle();

    @DefaultStringValue("Change title")
    String changeTitleDialogTitle();

    @DefaultStringValue("Type of chart")
    String chartType();

    @DefaultStringValue("Charts")
    String charts();

    @DefaultStringValue("Quickly produce various graphs that summarize your data.")
    String chartsDescription();

    @DefaultStringValue("Type of choice")
    String choiceType();

    @DefaultStringValue("Choose the administrative to display:")
    String chooseAdminLevelToMap();

    @DefaultStringValue("Choose the indicator or indicators to map")
    String chooseIndicatorsToMap();

    @DefaultStringValue("Choose Site")
    String chooseLocation();

    @DefaultStringValue("Here you can choose the location of your form submission. Use the filters below to find locations linked to existing form submissions. This will make it possible to link your form submissions to other forms inside and outside of your organization.")
    String chooseLocationDescription();

    @DefaultStringValue("Please choose a title for your report before saving")
    String chooseReportTitle();

    @DefaultStringValue("Choose the way your indicators will be displayed on the map:")
    String chooseSymbol();

    @DefaultStringValue("Click here if your download does not start automatically")
    String clickToDownload();

    @DefaultStringValue("Click to filter")
    String clickToFilter();

    @DefaultStringValue("Close")
    String close();

    @DefaultStringValue("Clustering")
    String clustering();

    @DefaultStringValue("Color")
    String color();

    @DefaultStringValue("Columns")
    String columns();

    @DefaultStringValue("Comments")
    String comments();

    @DefaultStringValue("Are you sure you want to remove this report from the dashboard?")
    String confirmRemoveFromDashboard();

    @DefaultStringValue("Unable to share report. It is blank (no indicators/fields selected.)")
    String unableToShareReport();

    @DefaultStringValue("Connection problem")
    String connectionProblem();

    @DefaultStringValue("We couldn't connect to the server, and an offline version is not available. Check your internet and retry.")
    String connectionProblemText();

    @DefaultStringValue("Oh no! Something went wrong...")
    String unexpectedException();

    @DefaultStringValue("Unfortunately things didn't go as planned and you've encountered a bug. We're going to try to send a report to the engineering team, but if you're really stuck and need a quick response, email support@activityinfo.org")
    String unexpectedExceptionExplanation();

    @DefaultStringValue("Enter the coordinates in any format, or drag the marker on the map")
    String coordinateToolTip();

    @DefaultStringValue("copy")
    String copy();

    @DefaultStringValue("Country")
    String country();

    @DefaultStringValue("New Report")
    String createNewReport();

    @DefaultStringValue("Create Project")
    String createProject();

    @DefaultStringValue("Create Target")
    String createTarget();

    @DefaultStringValue("Edit Target")
    String editTarget();

    @DefaultStringValue("Custom Date Range")
    String customDateRange();

    @DefaultStringValue("Custom Report")
    String customReport();

    @DefaultStringValue("Create a custom report from a combination of tables, charts, and maps")
    String customReportDescription();

    @DefaultStringValue("Dashboard")
    String dashboard();

    @DefaultStringValue("Data Entry")
    String dataEntry();

    @DefaultStringValue("Database")
    String database();

    @DefaultStringValue("Databases")
    String databases();

    @DefaultStringValue("Database Settings")
    String databaseSettings();

    @DefaultStringValue("Date")
    String date();

    @DefaultStringValue("The date range falls within a period that has been locked by the owner of this database.")
    String dateFallsWithinLockedPeriodWarning();

    @DefaultStringValue("Dates")
    String dates();

    @DefaultStringValue("Day of month")
    String dayOfMonth();

    @DefaultStringValue("Day of week")
    String dayOfWeek();

    @DefaultStringValue("Dashboard?")
    String defaultDashboard();

    @DefaultStringValue("Delete")
    String delete();

    @DefaultStringValue("Are you sure you want to delete this element from the report?")
    String deleteElementMessage();

    @DefaultStringValue("Delete element")
    String deleteElementTitle();

    @DefaultStringValue("Are you sure you want to delete this lock?")
    String deleteLockedPeriodQuestion();

    @DefaultStringValue("Delete Lock")
    String deleteLockedPeriodTitle();

    @DefaultStringValue("Delete")
    String deleteSite();

    @DefaultStringValue("Deleting...")
    String deleting();

    @DefaultStringValue("Description")
    String description();

    @DefaultStringValue("Design")
    String design();

    @DefaultStringValue("Create or change the activities and their indicators which are part of this database.")
    String designDescription();

    @DefaultStringValue("Details")
    String details();

    @DefaultStringValue("Dimensions")
    String dimensions();

    @DefaultStringValue("Discard Changes")
    String discardChanges();

    @DefaultStringValue("Downloading changes from server")
    String downSyncProgress();

    @DefaultStringValue("Download Ready")
    String downloadReady();

    @DefaultStringValue("A partner with this name already exists in your database.")
    String duplicatePartner();

    @DefaultStringValue("This name already exists in your database.")
    String duplicateName();

    @DefaultStringValue("E")
    String eastHemiChars();

    @DefaultStringValue("Edit")
    String edit();

    @DefaultStringValue("Email")
    String email();

    @DefaultStringValue("Email Frequency")
    String emailFrequency();

    @DefaultStringValue("Email Notification")
    String emailNotification();

    @DefaultStringValue("Email options...")
    String emailOptions();

    @DefaultStringValue("Email Subscription")
    String emailSubscription();

    @DefaultStringValue("Publish to website")
    String embed();

    @DefaultStringValue("If you don't wish to receive this email, uncheck the Email notification checkbox in your <a href=\"https://www.activityinfo.org/app#userprofile\" style=\"text-decoration: underline;\">settings</a>.")
    String digestUnsubscribeConstant();

    @DefaultStringValue("Your dashboard is empty! You can choose which reports appear on your dashboard by starring them in the reports page.")
    String emptyDashboard();

    @DefaultStringValue("(None)")
    String emptyDimensionCategory();

    @DefaultStringValue("Active")
    String enabledColumn();

    @DefaultStringValue("End Date")
    String endDate();

    @DefaultStringValue("Error")
    String error();

    @DefaultStringValue("An error occurred on the server.")
    String errorOnServer();

    @DefaultStringValue("An unexpected error occurred.")
    String errorUnexpectedOccured();

    @DefaultStringValue("Excel")
    String excel();

    @DefaultStringValue("Export")
    String export();

    @DefaultStringValue("Generating document, please wait...")
    String exportProgress();

    @DefaultStringValue("Failed loading of basemaps")
    String failBaseMapLoading();

    @DefaultStringValue("Filter")
    String filter();

    @DefaultStringValue("Filter by attribute")
    String filterByAttribute();

    @DefaultStringValue("Filter by date")
    String filterByDate();

    @DefaultStringValue("Filter by end date")
    String filterByEndDate();

    @DefaultStringValue("Filter by start date")
    String filterByStartDate();

    @DefaultStringValue("Filter by geography")
    String filterByGeography();

    @DefaultStringValue("Filter by partner")
    String filterByPartner();

    @DefaultStringValue("Filter by project")
    String filterByProject();

    @DefaultStringValue("Filter by location")
    String filterByLocation();

    @DefaultStringValue("Finish")
    String finish();

    @DefaultStringValue("from")
    String fromDate();

    @DefaultStringValue("Full Name")
    String fullName();

    @DefaultStringValue("Geography")
    String geography();

    @DefaultStringValue("Satellite Map with Streets")
    String googleHybrid();

    @DefaultStringValue("Road Map")
    String googleRoadmap();

    @DefaultStringValue("Satellite Map")
    String googleSatelliteMap();

    @DefaultStringValue("Terrain Map")
    String googleTerrainMap();

    @DefaultStringValue("Group")
    String group();

    @DefaultStringValue("Grouping")
    String grouping();

    @DefaultStringValue("History")
    String history();

    @DefaultStringValue("Horizontal axis")
    String horizontalAxis();

    @DefaultStringValue("Icon")
    String icon();

    @DefaultStringValue("Image")
    String image();

    @DefaultStringValue("The completion date must be after the start date.")
    String inconsistentDateRangeWarning();

    @DefaultStringValue("Indicator")
    String indicator();

    @DefaultStringValue("Indicators")
    String indicators();

    @DefaultStringValue("Enable offline mode")
    String installOffline();

    @DefaultStringValue("Minutes must be between 0-59.9")
    String invalidMinutes();

    @DefaultStringValue("Job title")
    String jobtitle();

    @DefaultStringValue("Preferred Language")
    String language();

    @DefaultStringValue("last edit:")
    String lastEdit();

    @DefaultStringValue("last edit: unknown")
    String lastEditUnknown();

    @DefaultStringValue("Latitude")
    String latitude();

    @DefaultStringValue("Layers")
    String layers();

    @DefaultStringValue("Legend")
    String legend();

    @DefaultStringValue("Lines")
    String lines();

    @DefaultStringValue("Link Indicators")
    String linkIndicators();

    @DefaultStringValue("List Header")
    String listHeader();

    @DefaultStringValue("Load new version now")
    String loadNewVersionNow();

    @DefaultStringValue("Loading...")
    String loading();

    @DefaultStringValue("Loading Component...")
    String loadingComponent();

    @DefaultStringValue("Loading maps...")
    String loadingMap();

    @DefaultStringValue("Site")
    String location();

    @DefaultStringValue("Your search did not match any existing sites. Make your search more general or add a new location.")
    String locationSearchNoResults();

    @DefaultStringValue("Location Type")
    String locationType();

    @DefaultStringValue("Sites")
    String locations();

    @DefaultStringValue("Manage time locks on databases, projects and activities")
    String lockPanelTitle();

    @DefaultStringValue("Lock databases, activities or projects to prevent users adding or changing data")
    String lockPeriodsDescription();

    @DefaultStringValue("Locked site")
    String lockedSiteTitle();

    @DefaultStringValue("Logout")
    String logout();

    @DefaultStringValue("Longitude")
    String longitude();

    @DefaultStringValue("Mailing List")
    String mailingList();

    @DefaultStringValue("Manage All Users")
    String manageAllUsers();

    @DefaultStringValue("Mandatory")
    String mandatory();

    @DefaultStringValue("Required")
    String required();

    @DefaultStringValue("Maps")
    String maps();

    @DefaultStringValue("Quickly produce maps of your indicators")
    String mapsDescription();

    @DefaultStringValue("Month")
    String month();

    @DefaultStringValue("Monthly")
    String monthly();

    @DefaultStringValue("Monthly Reports")
    String monthlyReports();

    @DefaultStringValue("Name")
    String name();

    @DefaultStringValue("New Form")
    String newActivity();

    @DefaultStringValue("New Attachment")
    String newAttachment();

    @DefaultStringValue("New Attribute")
    String newAttribute();

    @DefaultStringValue("New Attribute Group")
    String newAttributeGroup();

    @DefaultStringValue("New Database")
    String newDatabase();

    @DefaultStringValue("Rename Database")
    String renameDatabase();

    @DefaultStringValue("New Indicator")
    String newIndicator();

    @DefaultStringValue("Add new site")
    String newLocation();

    @DefaultStringValue("New map")
    String newMap();

    @DefaultStringValue("New partner")
    String newPartner();

    @DefaultStringValue("New Submission")
    String newSite();

    @DefaultStringValue("New")
    String newText();

    @DefaultStringValue("New user")
    String newUser();

    @DefaultStringValue("Edit user")
    String editUser();

    @DefaultStringValue("A new version of ActivityInfo has been downloaded. Do you want to reload now?")
    String newVersionChoice();

    @DefaultStringValue("No Data.")
    String noData();

    @DefaultStringValue("Coordinates must specify a hemisphere (+/-/N/S)")
    String noHemisphere();

    @DefaultStringValue("Coordinates must specify a hemisphere (+/-/E/W)")
    String noHemisphereLng();

    @DefaultStringValue("Longitude must be less than 180°")
    String longitudeOutOfBounds();

    @DefaultStringValue("Latitude must be less than 90°")
    String latitudeOutOfBounds();

    @DefaultStringValue("Coordinates must have at least one number")
    String noNumber();

    @DefaultStringValue("Some administrative levels are disabled because the boundaries are not available or have not yet been loaded.")
    String noPolygonsWarning();

    @DefaultStringValue("Undo")
    String undo();

    @DefaultStringValue("Move up")
    String moveUp();

    @DefaultStringValue("Move down")
    String moveDown();

    @DefaultStringValue("No Target")
    String noTarget();

    @DefaultStringValue("None")
    String none();

    @DefaultStringValue("N")
    String northHemiChars();

    @DefaultStringValue("You are not authorized to make this change: either your access permissions have changed in the last few minutes, or there has been an error on the server.")
    String notAuthorized();

    @DefaultStringValue("Not published")
    String notPublished();

    @DefaultStringValue("Open")
    String open();

    @DefaultStringValue("Organization")
    String organization();

    @DefaultStringValue("Administrator")
    String ownerName();

    @DefaultStringValue("Scope")
    String parentName();

    @DefaultStringValue("Partner")
    String partner();

    @DefaultStringValue("Define the partner organisations who participate in this database.")
    String partnerEditorDescription();

    @DefaultStringValue("Partners")
    String partners();

    @DefaultStringValue("PDF")
    String pdf();

    @DefaultStringValue("Customize your dashboard")
    String personalizeDashboard();

    @DefaultStringValue("Pie chart")
    String pieChart();

    @DefaultStringValue("Cross all of the dimensions of your results, including by activity, time period, partner, or geography")
    String pivotTableDescription();

    @DefaultStringValue("Pivot Tables")
    String pivotTables();

    @DefaultStringValue("Please complete the form correctly before continuing.")
    String pleaseCompleteForm();

    @DefaultStringValue("Preview")
    String preview();

    @DefaultStringValue("Print Form")
    String printForm();

    @DefaultStringValue("Project")
    String project();

    @DefaultStringValue("View, add, change and remove projects")
    String projectManagerDescription();

    @DefaultStringValue("Projects")
    String projects();

    @DefaultStringValue("You have unsaved changes. Do you want to save before continuing?")
    String promptSave();

    @DefaultStringValue("Proportional circle")
    String proportionalCircle();

    @DefaultStringValue("Published")
    String published();

    @DefaultStringValue("Built-in")
    String builtInLocationTypes();

    @DefaultStringValue("Public")
    String publicLocationTypes();

    @DefaultStringValue("Quarter")
    String quarter();

    @DefaultStringValue("Maximum Radius")
    String radiusMaximum();

    @DefaultStringValue("Minimum Radius")
    String radiusMinimum();

    @DefaultStringValue("Realized")
    String realized();

    @DefaultStringValue("Realized / Targeted")
    String realizedOrTargeted();

    @DefaultStringValue("Refresh Preview")
    String refreshPreview();

    @DefaultStringValue("A minute ago")
    String relativeTimeMinAgo();

    @DefaultStringValue("Remove")
    String remove();

    @DefaultStringValue("Remove from Dashboard")
    String removeFromDashboard();

    @DefaultStringValue("Remove partner")
    String removePartner();

    @DefaultStringValue("Once")
    String reportOnce();

    @DefaultStringValue("Reporting Frequency")
    String reportingFrequency();

    @DefaultStringValue("Reports")
    String reports();

    @DefaultStringValue("Requesting sync regions...")
    String requestingSyncRegions();

    @DefaultStringValue("Retry")
    String retry();

    @DefaultStringValue("Retrying...")
    String retrying();

    @DefaultStringValue("Rows")
    String rows();

    @DefaultStringValue("Save")
    String save();

    @DefaultStringValue("Save As")
    String saveAs();

    @DefaultStringValue("Saved")
    String saved();

    @DefaultStringValue("Changes have been saved.")
    String savedChanges();

    @DefaultStringValue("Saving changes...")
    String saving();

    @DefaultStringValue("Search")
    String search();

    @DefaultStringValue("Search for existing sites")
    String searchLocations();

    @DefaultStringValue("Matching sites")
    String searchResults();

    @DefaultStringValue("Select a database above.")
    String selectDatabaseHelp();

    @DefaultStringValue("Load failed due to error on the server.")
    String serverError();

    @DefaultStringValue("Setup")
    String setup();

    @DefaultStringValue("Shaded Polygons")
    String shadedPolygons();

    @DefaultStringValue("Share")
    String share();

    @DefaultStringValue("Share Report")
    String shareReport();

    @DefaultStringValue("This report is still empty, so it can't yet be model.")
    String emptyReportsCannotBeShared();

    @DefaultStringValue("Shared")
    String shared();

    @DefaultStringValue("Share...")
    String sharingOptions();

    @DefaultStringValue("Count")
    String siteCount();

    @DefaultStringValue("Choose the attributes of this form submission")
    String siteDialogAttributes();

    @DefaultStringValue("Add additional comments for this form submission")
    String siteDialogComments();

    @DefaultStringValue("Enter indicator results for this form submission")
    String siteDialogIndicators();

    @DefaultStringValue("Intervention Details")
    String siteDialogIntervention();

    @DefaultStringValue("Choose the project and partner implementing this intervention")
    String siteDialogInterventionDesc();

    @DefaultStringValue("Choose the location linked to this form submission")
    String siteDialogSiteDesc();

    @DefaultStringValue("Sorry, the selected form submission falls within a time period locked by the database owner and cannot be edited.")
    String siteIsLocked();

    @DefaultStringValue("form submissions(s) are missing geographic coordinates")
    String siteLackCoordiantes();

    @DefaultStringValue("site(s)")
    String sites();

    @DefaultStringValue("Sites")
    String sitesHeader();

    @DefaultStringValue("Slices")
    String slices();

    @DefaultStringValue("S")
    String southHemiChars();

    @DefaultStringValue("Start Date")
    String startDate();

    @DefaultStringValue("Starting...")
    String starting();

    @DefaultStringValue("Style")
    String style();

    @DefaultStringValue("Sum")
    String sum();

    @DefaultStringValue("There was a problem with Chrome's Application Cache. This might be related to a bug in the latest version of Chrome that is currently being fixed by Google, but in the meantime, restarting your computer may resolve the problem.")
    String syncAppCacheChrome();

    @DefaultStringValue("You have been logged out. You need to log back in to continue with synchronization.")
    String syncErrorAuth();

    @DefaultStringValue("The synchronizer was not able to connect to the server. Check your internet connection.")
    String syncErrorConnection();

    @DefaultStringValue("There is a new version of ActivityInfo available. You need to reload the page to load the new version and continue with synchronization.")
    String syncErrorReload();

    @DefaultStringValue("Synchronization Error")
    String syncError();

    @DefaultStringValue("An unexpected error occurred during synchronization.")
    String syncErrorUnexpected();

    @DefaultStringValue("Sync Now")
    String syncNow();

    @DefaultStringValue("Synchronization Complete")
    String synchronizationComplete();

    @DefaultStringValue("Target")
    String target();

    @DefaultStringValue("Define targets to be reached by your project.")
    String targetDescription();

    @DefaultStringValue("Target Value")
    String targetValue();

    @DefaultStringValue("Targeted")
    String targeted();

    @DefaultStringValue("Targets")
    String targets();

    @DefaultStringValue("Time")
    String time();

    @DefaultStringValue("Locks")
    String timeLocks();

    @DefaultStringValue("Time Period")
    String timePeriod();

    @DefaultStringValue("Title")
    String title();

    @DefaultStringValue("to")
    String toDate();

    @DefaultStringValue("Too many sites to display, please narrow your search criteria above")
    String tooManyLocationsToDisplay();

    @DefaultStringValue("Coordinates may have up to 3 numbers")
    String tooManyNumbers();

    @DefaultStringValue("Type")
    String type();

    @DefaultStringValue("Type to filter")
    String typeToFilter();

    @DefaultStringValue("Units")
    String units();

    @DefaultStringValue("You have unsaved changes. If you leave this page or close your browser, these changes will be lost.")
    String unsavedChangesWarning();

    @DefaultStringValue("Untitled Chart")
    String untitledChart();

    @DefaultStringValue("Untitled Map")
    String untitledMap();

    @DefaultStringValue("Untitled Report")
    String untitledReport();

    @DefaultStringValue("Untitled Table")
    String untitledTable();

    @DefaultStringValue("Upload")
    String upload();

    @DefaultStringValue("Use site")
    String useLocation();

    @DefaultStringValue("Use new site")
    String useNewLocation();

    @DefaultStringValue("Add users or control their access level")
    String userManagerDescription();

    @DefaultStringValue("Profile Settings")
    String userProfile();

    @DefaultStringValue("Users")
    String users();

    @DefaultStringValue("Value")
    String value();

    @DefaultStringValue("Checking for updates...")
    String versionChecking();

    @DefaultStringValue("Are you sure want to delete attachment(s)?")
    String confirmDeleteAttachment();

    @DefaultStringValue("Are you sure you want to delete this record?")
    String confirmDeleteRecord();

    @DefaultStringValue("No connection.")
    String versionConnectionProblem();

    @DefaultStringValue("You have the latest version.")
    String versionLatest();

    @DefaultStringValue("There is an update available.")
    String versionUpdateAvailable();

    @DefaultStringValue("Week (M-S)")
    String weekMon();

    @DefaultStringValue("Weekly")
    String weekly();

    @DefaultStringValue("Week")
    String weekFieldLabel();

    @DefaultStringValue("W")
    String westHemiChars();

    @DefaultStringValue("Word")
    String word();

    @DefaultStringValue("Year")
    String year();

    @DefaultStringValue("Year/Month")
    String yearMonthGrouping();

    @DefaultStringValue("Reset")
    String reset();

    @DefaultStringValue("Label")
    String labelFieldLabel();

    @DefaultStringValue("Code")
    String codeFieldLabel();

    @DefaultStringValue("Geographic coordinates")
    String geographicCoordinatesFieldLabel();

    @DefaultStringValue("Ignore this column")
    String ignoreColumnAction();

    @DefaultStringValue("Choose Field")
    String chooseFieldHeading();

    @DefaultStringValue("Ignored")
    String ignored();

    @DefaultStringValue("Label")
    String fieldLabel();

    @DefaultStringValue("Type")
    String fieldType();

    @DefaultStringValue("Quantity")
    String fieldTypeQuantity();

    @DefaultStringValue("Date")
    String fieldTypeDate();

    @DefaultStringValue("Text")
    String fieldTypeText();

    @DefaultStringValue("Geographic point")
    String fieldTypeGeographicPoint();

    @DefaultStringValue("Multi-line text")
    String fieldTypeNarrative();

    @DefaultStringValue("To begin, select the table you want to import in Excel, copy it to the clipboard, and then paste here")
    String pasteSpreadsheetInstructions();

    @DefaultStringValue("Choose the destination field.")
    String chooseDestinationField();

    @DefaultStringValue("Change")
    String change();

    @DefaultStringValue("Label")
    String label();

    @DefaultStringValue("Cardinality")
    String cardinality();

    @DefaultStringValue("Values")
    String values();

    @DefaultStringValue("« Previous")
    String previousButton();

    @DefaultStringValue("Next »")
    String nextButton();

    @DefaultStringValue("Choose columns")
    String chooseColumns();

    @DefaultStringValue("All columns")
    String allColumns();

    @DefaultStringValue("Filter by name")
    String filterByName();

    @DefaultStringValue("Add column")
    String addColumn();

    @DefaultStringValue("Double click")
    String drillDownTipHeading();

    @DefaultStringValue("Double-click a value to drill down to the individual sites")
    String drillDownTip();

    @DefaultStringValue("Save operation failed (formclass).")
    String failedToSaveClass();

    @DefaultStringValue("Alternate Name")
    String alternateName();

    @DefaultStringValue("Confirmation")
    String confirmation();

    @DefaultStringValue("Yes")
    String yes();

    @DefaultStringValue("No")
    String no();

    @DefaultStringValue("New Value")
    String changeHeaderMessage();

    @DefaultStringValue("Clear filter")
    String clearFilter();

    @DefaultStringValue("Select all")
    String selectAll();

    @DefaultStringValue("Deselect all")
    String deselectAll();

    @DefaultStringValue("Confirm Deletion")
    String confirmDeletion();

    @DefaultStringValue("Deletion Failed")
    String deletionFailed();

    @DefaultStringValue("Deletion in Progress")
    String deletionInProgress();

    @DefaultStringValue("This field is required")
    String requiredFieldMessage();

    @DefaultStringValue("This field has no options.")
    String noDataForField();

    @DefaultStringValue("Import")
    String importText();

    @DefaultStringValue("Importing...")
    String importing();

    @DefaultStringValue("Import failed")
    String importFailed();

    @DefaultStringValue("Import data from a spreadsheet")
    String importDialogTitle();

    @DefaultStringValue("Rows validation result")
    String rowsValidationResult();

    @DefaultStringValue("Correct any problems with the imported rows and fill in missing columns. Please mouse over on particular cell to see detail explanation.")
    String correctProblems();

    @DefaultStringValue("Matching references...")
    String matchingReferences();

    @DefaultStringValue("Matched references")
    String matchedReferences();

    @DefaultStringValue("Message")
    String message();

    @DefaultStringValue("Please provide valid comma separated text")
    String pleaseProvideCommaSeparatedText();

    @DefaultStringValue("Parsing rows...")
    String parsingRows();

    @DefaultStringValue("Looks great!")
    String validSchemaImport();

    @DefaultStringValue("Copy and paste into the box above to continue.")
    String schemaImportEmpty();

    @DefaultStringValue("Hmmm... Doesn't look like you've pasted a table. Copy and paste a table from Excel or another spreadsheet program, including the column headers.")
    String invalidTableData();

    @DefaultStringValue("Import anyway")
    String ignoreImportWarnings();

    @DefaultStringValue("Default value")
    String defaultValue();

    @DefaultStringValue("Allow editing ONLY by users with 'design' privileges")
    String partOfWorkflow();

    @DefaultStringValue("Line")
    String line();

    @DefaultStringValue("Set expression if you would like to calculate indicator value dynamically (otherwise leave blank). Example: [A]+[B]+([C]/[D])")
    String calculatedIndicatorExplanation();

    @DefaultStringValue("Calculation")
    String calculation();

    @DefaultStringValue("Calculation expression is invalid")
    String calculationExpressionIsInvalid();

    @DefaultStringValue("Invalid Relevance Expression")
    String relevanceExpressionIsInvalid();

    @DefaultStringValue("An (optional) short code for this form field. This code can be used to refer to the field in calculated expressions.")
    String nameInExpressionTooltip();

    @DefaultStringValue("Calculate automatically")
    String calculateAutomatically();

    @DefaultStringValue("Import schema")
    String importSchemaDialogTitle();

    @DefaultStringValue("New Location Type")
    String newLocationType();

    @DefaultStringValue("Create")
    String create();

    @DefaultStringValue("Fields")
    String fields();

    @DefaultStringValue("Properties")
    String properties();

    @DefaultStringValue("Read only")
    String readonly();

    @DefaultStringValue("Relevance")
    String relevance();

    @DefaultStringValue("Form Category")
    String formCategory();

    @DefaultStringValue("Define IF")
    String defineRelevanceLogic();

    @DefaultStringValue("Defined")
    String defined();

    @DefaultStringValue("Show in Data Entry")
    String showInDataEntry();

    @DefaultStringValue("And")
    String and();

    @DefaultStringValue("Or")
    String or();

    @DefaultStringValue("Open in the new data entry page")
    String tryNewDataEntryInterface();

    @DefaultStringValue("Always applicable")
    String relevanceEnabled();

    @DefaultStringValue("Applicable IF (to define)")
    String relevanceEnabledIf();

    @DefaultStringValue("Show this field only if")
    String relevanceShowIf();

    @DefaultStringValue("all")
    String relevanceCriteriaAll();

    @DefaultStringValue("any")
    String relevanceCriteriaAny();

    @DefaultStringValue("of the following conditions are met:")
    String relevanceConditionsMet();

    @DefaultStringValue("Invalid code. Valid code must start with a letter and contain only letters, numbers, and the underscore symbol.")
    String invalidCodeMessage();

    @DefaultStringValue("Please enter label.")
    String invalidLabel();

    @DefaultStringValue("A code with this name already exists. Please select another code.")
    String duplicateCodeMessage();

    @DefaultStringValue("Search for location to add...")
    String searchForLocationToAdd();

    @DefaultStringValue("Sorry, your permissions have changed and you are no longer authorized to perform this action.")
    String permissionChangedError();

    @DefaultStringValue("Open Form Designer")
    String openFormDesigner();

    @DefaultStringValue("Open Table")
    String openTable();

    @DefaultStringValue("Remove selected")
    String removeSelectedLocations();

    @DefaultStringValue("Classic view")
    String classicView();

    @DefaultStringValue("Try the new (BETA) form layout")
    String tryNewFormLayout();

    @DefaultStringValue("New Form (Beta)")
    String newForm();

    @DefaultStringValue("Please enter a name for the new form")
    String enterNameForNewForm();

    @DefaultStringValue("What's this?")
    String whatsThis();

    @DefaultStringValue("Create a new database")
    String createNewDatabase();

    @DefaultStringValue("Create a new empty database")
    String createEmptyDatabase();

    @DefaultStringValue("You can create your own data collection forms in the next step")
    String createEmptyDatabaseExplanation();

    @DefaultStringValue("Use an existing database as a template")
    String copyDatabase();

    @DefaultStringValue("You can create a new database that is a copy of any database to which you have access")
    String copyDatabaseExplanation();

    @DefaultStringValue("Options")
    String options();

    @DefaultStringValue("Copy data as well as forms")
    String copyDataAsWellAsForms();

    @DefaultStringValue("Copy partners")
    String copyPartners();

    @DefaultStringValue("Copy user permissions")
    String copyUserPermissions();

    @DefaultStringValue("Choose a database to copy")
    String chooseDatabaseToCopy();

    @DefaultStringValue("Creating...")
    String creating();

    @DefaultStringValue("Please enter the name")
    String enterNameWelcome();

    @DefaultStringValue("Please enter the description")
    String enterDescriptionWelcome();

    @DefaultStringValue("Please select the country")
    String selectCountryWelcome();

    @DefaultStringValue("Failed to load country list (please report issue on help@activityinfo.org)")
    String failedToLoadCountries();

    @DefaultStringValue("Failed to create database.")
    String failedToCreateDatabase();

    @DefaultStringValue("Please select the database to copy")
    String selectDbToCopyWelcome();

    @DefaultStringValue("Alert")
    String alert();

    @DefaultStringValue("New Submission")
    String newSubmission();

    @DefaultStringValue("Edit Submission")
    String editSubmission();

    @DefaultStringValue("Barcode")
    String fieldTypeBarcode();

    @DefaultStringValue("Calculated")
    String fieldTypeCalculated();

    @DefaultStringValue("Multiple selection")
    String multipleSelection();

    @DefaultStringValue("Please fill in all required fields")
    String pleaseFillInAllRequiredFields();

    @DefaultStringValue("There are no partners defined for this database, so it is not possible to make new submissions at this time.")
    String noPartners();

    @DefaultStringValue("Do you really want to delete this field? All of this field's data will be lost and cannot be recovered.")
    String deleteFormFieldConfirmation();

    @DefaultStringValue("Do you want to retry deletion?")
    String retryDeletion();

    @DefaultStringValue("Users may add new locations during data entry.")
    String openWorkflow();

    @DefaultStringValue("Users must choose from existing locations")
    String closedWorkflow();

    @DefaultStringValue("Permissions")
    String permissions();

    @DefaultStringValue("Load")
    String load();

    @DefaultStringValue("Please specify 'From' date before 'To' date.")
    String fromDateIsBeforeToDate();

    @DefaultStringValue("Please type to search for results")
    String suggestBoxPlaceholder();

    @DefaultStringValue("Information")
    String information();

    @DefaultStringValue("This field is built-in and cannot be removed from the form.")
    String notAllowedToRemoveBuiltinField();

    @DefaultStringValue("Warning")
    String warning();

    @DefaultStringValue("Please specify 'from' date")
    String pleaseSpecifyFromDate();

    @DefaultStringValue("Please specify 'to' date")
    String pleaseSpecifyToDate();

    @DefaultStringValue("Pin to dashboard")
    String pinToDashboard();

    @DefaultStringValue("Switch to page view")
    String switchToPageView();

    @DefaultStringValue("Single selection")
    String singleSelection();

    @DefaultStringValue("Please select at least one item")
    String pleaseSelectAtLeastOneItem();

    @DefaultStringValue("Perfect match.")
    String importPerfectMatchTooltip();

    @DefaultStringValue("Failed to match. Row will not be imported.")
    String failedToMatchValue();

    @DefaultStringValue("Database structure successfully imported.")
    String databaseStructureSuccessfullyImported();

    @DefaultStringValue("Oh no, there was an error importing your schema:")
    String failedToImportSchema();

    @DefaultStringValue("Do you want to import anyway?")
    String doYouWantToImportAnyway();

    @DefaultStringValue("Don't panic!")
    String dontPanic();

    @DefaultStringValue("We noticed a few issues with your import:")
    String weNoticedIssuesWithImport();

    @DefaultStringValue("The import schema wizard allows you to import and (soon) update activities, indicators, and attributes from a spreadsheet.")
    String importWelcomeText();

    @DefaultStringValue("To begin, copy your table from Excel and paste in the box below:")
    String importHelpText();

    @DefaultStringValue("Upload failed")
    String uploadFailed();

    @DefaultStringValue("Browse")
    String browse();

    @DefaultStringValue("Download")
    String download();

    @DefaultStringValue("Present")
    String present();

    @DefaultStringValue("Filter is not supported.")
    String filterIsNotSupported();

    @DefaultStringValue("Single")
    String single();

    @DefaultStringValue("Multiple")
    String multiple();

    @DefaultStringValue("Determines type of attachment (image or file)")
    String attachmentTypeDescription();

    @DefaultStringValue("Replace")
    String replace();

    @DefaultStringValue("No attachments")
    String noAttachments();

    @DefaultStringValue("No image selected.")
    String noImage();

    @DefaultStringValue("Upload file size limit is set to 10 MB.")
    String uploadFileSizeLimit();

    @DefaultStringValue("Unknown")
    String unknown();

    @DefaultStringValue("+ Add another")
    String addAnother();

    @DefaultStringValue("Tab is not selected.")
    String subFormTabNotSelected();

    @DefaultStringValue("Match each column in your spreadsheet to your form's fields on the right.")
    String columnMappingHelpLink();

    @DefaultStringValue("Show me")
    String showMe();

    @DefaultStringValue("Blank value is not allowed.")
    String blankValueIsNotAllowed();

    @DefaultStringValue("Expression")
    String expression();

    @DefaultStringValue("Example: A+B+(C/D)+[Volume A]")
    String expressionExample();

    @DefaultStringValue("Working online")
    String workingOnline();

    @DefaultStringValue("Working offline")
    String workingOffline();

    @DefaultStringValue("Me")
    String me();

    @DefaultStringValue("Which options apply?")
    String defaultCheckboxFieldLabel();

    @DefaultStringValue("Which choice would you choose?")
    String defaultRadioFieldLabel();

    @DefaultStringValue("Enter a new label for this option")
    String enterNameForOption();

    @DefaultStringValue("households")
    String defaultQuantityUnits();

    @DefaultStringValue("Offline Mode not supported")
    String offlineNotSupportedTitle();

    @DefaultStringValue("Unfortunately, offline mode is not supported on your browser. To enable offline mode, we recommend using Google Chrome.")
    String offlineNotSupported();

    @DefaultStringValue("Download Google Chrome")
    String downloadGoogleChrome();

    @DefaultStringValue("New Report Element")
    String newReportElement();

    @DefaultStringValue("ActivityInfo News")
    String activityInfoNews();

    @DefaultStringValue("Saved Reports")
    String savedReports();

    @DefaultStringValue("An indicator cannot be linked to itself")
    String indicatorCannotBeLinkedToItself();

    @DefaultStringValue("Click to unlink")
    String clickToUnlink();

    @DefaultStringValue("Click to link")
    String clickToLink();

    @DefaultStringValue("Reference")
    String reference();

    @DefaultStringValue("Choose form")
    String chooseForm();

    @DefaultStringValue("clear")
    String clear();

    @DefaultStringValue("Link created")
    String linkCreated();

    @DefaultStringValue("Link removed")
    String linkRemoved();

    @DefaultStringValue("You do not have permission to edit the design of this database.")
    String noDbDesignPermissions();

    @DefaultStringValue("Repeating Sub-Form")
    String repeatingSubform();

    @DefaultStringValue("Monthly Sub-Form")
    String monthlySubform();

    @DefaultStringValue("Weekly Sub-Form")
    String weeklySubform();

    @DefaultStringValue("Fortnightly Sub-Form")
    String fortnightlySubform();

    @DefaultStringValue("{0,number,#}W{1}-{2}")
    String fortnight();

    @DefaultStringValue("Daily Sub-Form")
    String dailySubform();

    @DefaultStringValue("Select")
    String select();

    @DefaultStringValue("Please choose the reference value by geographical location.")
    String chooseReferenceByGeoPoint();

    @DefaultStringValue("Choose reference value")
    String chooseReferenceValue();

    @DefaultStringValue("A user with this email address has already been added to the database.")
    String userExistsMessage();

    @DefaultStringValue("User exists")
    String userExistsTitle();

    @DefaultStringValue("Count")
    String count();

    @DefaultStringValue("Measures")
    String measures();

    @DefaultStringValue("Formula")
    String formula();

    @DefaultStringValue("The formula is valid.")
    String formulaValid();

    @DefaultStringValue("Percentage")
    String percentage();

    @DefaultStringValue("Median")
    String median();

    @DefaultStringValue("Min")
    String minimum();

    @DefaultStringValue("Max")
    String maximum();

    @DefaultStringValue("Statistic")
    String statistic();

    @DefaultStringValue("Total")
    String tableTotal();

    @DefaultStringValue("Input Mask")
    String inputMask();

    @DefaultStringValue("Learn more about input masks")
    String learnMoreAboutInputMasks();

    @DefaultStringValue("is")
    String operatorIs();

    @DefaultStringValue("is not")
    String operatorIsNot();

    @DefaultStringValue("is greater than")
    String operatorGreaterThan();

    @DefaultStringValue("is greater than or equal to")
    String operatorGreaterThanEqual();

    @DefaultStringValue("is less than")
    String operatorLessThan();

    @DefaultStringValue("is less than or equal to")
    String operatorLessThanEqualTo();

    @DefaultStringValue("includes")
    String operatorIncludes();

    @DefaultStringValue("does not include")
    String operatorDoesNotInclude();

    @DefaultStringValue("Learn more about serial numbers")
    String learnMoreAboutSerialNumbers();

    @DefaultStringValue("(Pending)")
    String pending();

    @DefaultStringValue("Serial Number")
    String serialNumber();

    @DefaultStringValue("Prefix Formula")
    String prefixFormula();

    @DefaultStringValue("Show as dropdown")
    String dropdownPresentation();

    @DefaultStringValue("Show as radio buttons")
    String radioButtonPresentation();

    @DefaultStringValue("Presentation")
    String presentation();

    @DefaultStringValue("Key")
    String keyField();

    @DefaultStringValue("Sync automatically")
    String syncAutomatically();

    @DefaultStringValue("Clear all offline data")
    String clearOfflineMode();

    @DefaultStringValue("Are you sure you want to remove all offline data? Any pending changes will be lost!")
    String confirmClearOfflineMode();

    @DefaultStringValue("Deleted Form")
    String deletedForm();

    @DefaultStringValue("Forbidden Form")
    String forbiddenForm();

    @DefaultStringValue("Not Found")
    String notFound();

    @DefaultStringValue("Please use our new and much improved data entry interface for viewing, adding or updating this form's records.")
    String pleaseUseNewDataEntry();

    @DefaultStringValue("Copied")
    String copied();

    @DefaultStringValue("Copied to Clipboard!")
    String copiedToClipboard();

    @DefaultStringValue("Count Distinct")
    String countDistinct();

    @DefaultStringValue("No records found.")
    String noRecords();

    @DefaultStringValue("No matching records.")
    String noMatchingRecords();

    @DefaultStringValue("Oops! Exception:")
    String exception();

    @DefaultStringValue("Invalid form reference(s)")
    String invalidReference();

    @DefaultStringValue("Download in progress...")
    String downloadInProgress();

    @DefaultStringValue("Available offline")
    String availableOffline();

    @DefaultStringValue("Make form available offline")
    String makeAvailableOffline();

    @DefaultStringValue("New Activity (Classic Form)")
    String newClassicActivity();

    @DefaultStringValue("Selected Columns")
    String selectedColumns();

    @DefaultStringValue("All")
    String allRows();

    @DefaultStringValue("Folders")
    String folders();

    @DefaultStringValue("Folder")
    String folder();

    @DefaultStringValue("New Folder")
    String newFolder();

    @DefaultStringValue("The folder is not empty. Please remove all forms before deleting.")
    String folderNotEmpty();

    @DefaultStringValue("Try our new reporting interface! (BETA)")
    String tryNewReportingInterface();

    @DefaultStringValue("This entry references a site which has been deleted. Please update the location to avoid loss of data.")
    String deletedLocation();

    @DefaultStringValue("No Filter")
    String noFilter();

    @DefaultStringValue("Current Filter")
    String currentFilter();

    @DefaultStringValue("Current column length {0} exceeds Column Export Limitation of {1} for Export Type {2}")
    String columnLimit();


    @DefaultStringValue("Search for a database, folder or form...")
    String searchPlaceholder();

    @DefaultStringValue("Settings")
    String settings();

    @DefaultStringValue("My Reports:")
    String subForms();

    @DefaultStringValue("Record details")
    String recordDetails();

    @DefaultStringValue("Go back to:")
    String goBackTo();

    @DefaultStringValue("New record")
    String newRecord();

    @DefaultStringValue("Notifications")
    String notifications();

    @DefaultStringValue("We couldn’t find the data you’re looking for.")
    String dataNotFound();

    @DefaultStringValue("Permission denied.")
    String permissionDeniedHeading();

    @DefaultStringValue("It appears you do not have the right permission to access this data.")
    String permissionDenied();

    @DefaultStringValue("Go back to database overview")
    String goBackToDatabaseOverview();

    @DefaultStringValue("Switch accounts")
    String switchAccounts();

}
