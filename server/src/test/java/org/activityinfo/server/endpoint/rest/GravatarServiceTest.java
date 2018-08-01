package org.activityinfo.server.endpoint.rest;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class GravatarServiceTest {

    @Test
    public void gravatarTest() {
        assertThat(GravatarService.emailHash("MyEmailAddress@example.com "), equalTo("f9879d71855b5ff21e4963273a886bfc"));
    }

}