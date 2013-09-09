package org.activityinfo.server.login.model;

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

public class HostPageModel extends PageModel {
    private String appUrl;
    private boolean appCacheEnabled;
    private String mapsApiKey;

    // domain whitelabelling
    private String host;
    private String title;
    private String resourceBasePath;

    public HostPageModel(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public boolean isAppCacheEnabled() {
        return appCacheEnabled;
    }

    public void setAppCacheEnabled(boolean appCacheEnabled) {
        this.appCacheEnabled = appCacheEnabled;
    }

    public String getMapsApiKey() {
        return mapsApiKey;
    }

    public void setMapsApiKey(String mapsApiKey) {
        this.mapsApiKey = mapsApiKey;
    }

    public String getHost() {
        return (host == null || host.isEmpty()) ? "activityinfo.org" : host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getTitle() {
        return (title == null || title.isEmpty()) ? "ActivityInfo" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResourceBasePath() {
        return resourceBasePath;
    }

    public void setResourceBasePath(String resourceBasePath) {
        this.resourceBasePath = resourceBasePath;
    }
}
