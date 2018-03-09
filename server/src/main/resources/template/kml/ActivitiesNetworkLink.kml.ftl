<#--

    ActivityInfo
    Copyright (C) 2009-2013 UNICEF
    Copyright (C) 2014-2018 BeDataDriven Groep B.V.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

-->
<?xml version="1.0" encoding="UTF-8"?>
<kml xmlns="http://earth.google.com/kml/2.2">
    <Document>
    <#list schema.databases as db>
        <Folder>
            <name>${db.name?xml}</name>
            <open>1</open>
            <#list db.activities as activity>
                <NetworkLink>
                    <name>${activity.name?xml}</name>
                    <refreshVisibility>0</refreshVisibility>
                    <flyToView>1</flyToView>
                    <Link>
                    <href>${baseURL}${activity.id?c}</href>
                    </Link>
                </NetworkLink>
            </#list>
        </Folder>
    </#list>
    </Document>
</kml>