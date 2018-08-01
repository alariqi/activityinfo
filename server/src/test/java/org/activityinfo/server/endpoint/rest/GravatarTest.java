package org.activityinfo.server.endpoint.rest;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class GravatarTest {

    @Test
    public void gravatarTest() {
        assertThat(Gravatar.getAvatar("MyEmailAddress@example.com "), equalTo("f9879d71855b5ff21e4963273a886bfc"));
    }

}