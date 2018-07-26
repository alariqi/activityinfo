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
package org.activityinfo.test.pageobject.web;

import com.google.common.base.Predicate;
import org.activityinfo.test.pageobject.api.FluentElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;
import java.net.URL;

import static org.activityinfo.test.pageobject.api.XPathBuilder.containingText;

/**
 * Models the new user confirmation page
 */
public class ConfirmPage {


    public static final By NAME_INPUT = By.name("name");
    public static final By PASSWORD_INPUT = By.name("password");
    public static final By PASSWORD_CONFIRM_INPUT = By.name("password2");
    public static final By SUBMIT_BUTTON = By.xpath("//button[@type='submit']");
    
    public static final By INVALID_MESSAGE = By.xpath("//*[contains(text(), 'invalid')]");

    private final FluentElement page;

    @Inject
    public ConfirmPage(WebDriver webDriver) {
        this.page = new FluentElement(webDriver);
    }

    public ApplicationPage confirmPasswordSubmitFormAndNavigateToApplicationPage(String password) {
        page.waitUntil(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver input) {
                if (!input.findElements(INVALID_MESSAGE).isEmpty()) {
                    throw new AssertionError("Invalid confirmation link");
                }
                return !input.findElements(PASSWORD_INPUT).isEmpty();
            }
        });
        page.findElement(PASSWORD_INPUT).clear().sendKeys(password);
        page.findElement(PASSWORD_CONFIRM_INPUT).clear().sendKeys(password);
        page.findElement(SUBMIT_BUTTON).click();

        ApplicationPage applicationPage = new ApplicationPage(page);
        applicationPage.waitUntilLoaded();

        return applicationPage;
    }

    public ConfirmPage navigateTo(URL confirmationLink) {
        page.navigate().to(confirmationLink);
        return this;
    }
    
    public void assertLinkIsInvalid() {
        page.waitUntil(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver input) {
                if(!input.findElements(PASSWORD_INPUT).isEmpty()) {
                    throw new AssertionError("Expected invalid link message: found confirm password form.");
                }
                return !input.findElements(INVALID_MESSAGE).isEmpty();
            }
        });   
    }


    public void assertPasswordIsTooShort() {
        page.find().span(containingText("Password must be at least")).waitForFirst();
    }
}
