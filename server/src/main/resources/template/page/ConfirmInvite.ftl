<#-- @ftlvariable name="" type="org.activityinfo.server.login.model.ConfirmInvitePageModel" -->
<#--
 #%L
 ActivityInfo Server
 %%
 Copyright (C) 2009 - 2013 UNICEF
 %%
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as
 published by the Free Software Foundation, either version 3 of the 
 License, or (at your option) any later version.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public 
 License along with this program.  If not, see
 <http://www.gnu.org/licenses/gpl-3.0.html>.
 #L%
-->
<#include "Scaffolding.ftl">
<@scaffolding title="${label.welcomeToActivityInfo}">

    <@content>
    <div class="row row--padded">

        <h3>${label.welcomeToActivityInfo}</h3>

        <p>${label.setupAccount}</p>

        <form action="" method="post" id="confirmForm">
            <input type="hidden" name="key" value="${user.changePasswordKey}">
            <label>
                ${label.confirmYourName}:
                <input type="text" name="name" id="nameInput" value="${user.name}" required>
            </label>

            <label>
                ${label.confirmYourPreferredLanguage}:
                <select name="locale">
                <#list availableLocales as locale>
                    <option value="${locale.code}">${locale.localizedName}</option>
                </#list>
                </select>
            </label>

            <label>
                ${label.choosePassword}:
                <input type="password" name="password" required>
            </label>

            <label>
                ${label.confirmYourPassword}:
                <input type="password" name="password2" required>
            </label>
            <label class="checkbox">
                <input type="checkbox" checked name="newsletter" value="true">
                ${label.newsletter}
            </label>

            <button type="submit">${label.continue} &raquo;</button>
        </form>
    </div>
    </@content>
    <@footer/>
    <@scripts>
    </@scripts>
</@scaffolding>
