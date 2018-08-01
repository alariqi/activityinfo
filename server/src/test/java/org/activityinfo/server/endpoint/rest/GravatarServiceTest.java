package org.activityinfo.server.endpoint.rest;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class GravatarServiceTest {

    @Test
    public void gravatarTest() {

        // See https://en.gravatar.com/site/implement/hash/

        assertThat(GravatarService.emailHash("MyEmailAddress@example.com "), equalTo("0bc83cb571cd1c50ba6f3e8a78ef1346"));
    }

}