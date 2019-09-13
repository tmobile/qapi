package com.tmobile.ct.tep.qapi.tvaultclient.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CredentialsTest {

    private Credentials credentials;
    private static String username = "user1";
    private static String password = "pass1";

    @Before
    public void setUp() throws Exception {
        credentials = new Credentials(username,password);
    }

    @Test
    public void getUsername() {
        Assert.assertEquals(username,credentials.getUsername());
        credentials.setUsername("other");
        Assert.assertNotEquals(username,credentials.getUsername());
    }

    @Test
    public void getPassword() {
        Assert.assertEquals(password,credentials.getPassword());
        credentials.setPassword("other");
        Assert.assertNotEquals(password,credentials.getPassword());
    }
}
