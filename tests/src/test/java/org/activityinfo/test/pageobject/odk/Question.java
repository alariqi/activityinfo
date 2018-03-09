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
package org.activityinfo.test.pageobject.odk;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import org.activityinfo.test.driver.DataEntryDriver;
import org.joda.time.LocalDate;
import org.openqa.selenium.Dimension;

import java.util.List;

public class Question implements DataEntryDriver {
    private final AppiumDriver driver;

    public Question(AppiumDriver driver) {
        this.driver = driver;
    }

    public Question forward() {
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        HashMap<String, Double> swipeObject = new HashMap<String, Double>();
//        swipeObject.put("startX", 0.95);
//        swipeObject.put("startY", 0.5);
//        swipeObject.put("endX", 0.05);
//        swipeObject.put("endY", 0.5);
//        swipeObject.put("duration", 1.8);
//        js.executeScript("mobile: swipe", swipeObject);
//        
//        HasTouchScreen touchScreen = (HasTouchScreen) driver;
//        touchScreen.getTouch().flick(-500, 0);
//        
        // swipe across the screen horizontally
//        driver.findElementById("form_forward_button").click();
//        System.out.println(driver.getPageSource());
        Dimension size = driver.manage().window().getSize();

        
        
        int fromX = size.width - 10;
        int toX = 10;

        int y = size.height / 2;

        TouchAction action = new TouchAction(driver);
        action.press(fromX, y);
        action.moveTo(toX, y);
        action.release();
        action.perform();
        return this;
        
    }

    @Override
    public boolean nextField() {
        return false;
    }

    @Override
    public void submit() throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getLabel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void fill(String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void fill(LocalDate date) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void select(String itemLabel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isValid() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isNextEnabled() {
        return false;
    }

    @Override
    public void sendKeys(CharSequence keys) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> availableValues() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException();
    }
}
