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
<#include "Scaffolding.ftl">
<@scaffolding title="${label.signUpAddressExistsHeader}">

    <@content>
    <div class="row row--small">

        <h1>${label.signUpAddressExistsHeader}</h1>

        <p>${label.signUpAddressExistsDetail1}</p>

        <p>${label.signUpAddressExistsDetail2}</p>

        <#if loginError != true && emailSent != true & emailError != true>
            <form class="form-inline" action="signUpAddressExists" method="POST">
                <input type="hidden" name="email" value="${email}"/>
                <button type="submit" class="btn btn-primary">${label.signUpAddressExistsSendEmail}</button>
            </form>
        </#if>

        <#if loginError == true>
            <div class="alert alert--error">
            ${label.loginError}
            </div>
        </#if>
        <#if emailSent == true>
            <div class="alert alert--success">
            ${label.emailSent}
            </div>
        </#if>
        <#if emailError == true>
            <div class="alert alert--error">
            ${label.emailErrorAlert}
            </div>
        </#if>
    </div>
    </@content>
    <@footer/>
    <@scripts/>
</@scaffolding>
