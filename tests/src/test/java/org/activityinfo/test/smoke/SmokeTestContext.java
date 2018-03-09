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
package org.activityinfo.test.smoke;

import com.google.inject.Guice;
import com.google.inject.Injector;
import cucumber.runtime.java.guice.impl.ScenarioModule;
import cucumber.runtime.java.guice.impl.SequentialScenarioScope;
import org.activityinfo.test.config.ConfigProperty;
import org.activityinfo.test.driver.DriverModule;
import org.activityinfo.test.pageobject.web.ApplicationPage;
import org.activityinfo.test.pageobject.web.LoginPage;
import org.activityinfo.test.sut.SystemUnderTest;
import org.activityinfo.test.sut.UserAccount;
import org.activityinfo.test.webdriver.WebDriverModule;
import org.activityinfo.test.webdriver.WebDriverSession;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

public class SmokeTestContext extends TestWatcher {

    private static final ConfigProperty USER = new ConfigProperty("SMOKETEST_EMAIL",
            "Email of the account to use for smoke-testing on the production server");

    private static final ConfigProperty PASSWORD = new ConfigProperty("SMOKETEST_PASSWORD",
            "Password of the account to use for smoke-testing on the production server");


    private static Injector INJECTOR;

    private static final SequentialScenarioScope scope = new SequentialScenarioScope();
  
    private WebDriverSession session;


    public static synchronized Injector getInjector() {
        if(INJECTOR == null) {
            INJECTOR = Guice.createInjector(
                    new SystemUnderTest(),
                    new WebDriverModule(),
                    new DriverModule("web"),
                    new ScenarioModule(scope));
        }
        return INJECTOR;
    }

    /**
     * Provides the user account for smoke-testing the production server.
     *
     * <p>The account should have read-only access data to real users and is used to 
     * verify the deployment just before going live.</p>
     */
    public UserAccount getAccount() {
        return new UserAccount(USER.get(), PASSWORD.get());
    }

    
    public ApplicationPage login() {
        return loginPage().navigateTo().loginAs(getAccount()).andExpectSuccess();
    }
    
    public LoginPage loginPage() {
        return getInjector().getInstance(LoginPage.class);
    }

    @Override
    protected void starting(Description description) {
        scope.enterScope();
        session = getInjector().getInstance(WebDriverSession.class);
    }

    @Override
    protected void finished(Description description) {
        session.stop();
        scope.exitScope();
    }
    
    private void startProxy() {
        HttpProxyServer server =
                DefaultHttpProxyServer.bootstrap()
                        .withPort(4444)
                        .start();
    }
}
