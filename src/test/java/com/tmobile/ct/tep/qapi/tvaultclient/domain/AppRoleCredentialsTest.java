package com.tmobile.ct.tep.qapi.tvaultclient.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AppRoleCredentialsTest {

    private AppRoleCredentials appRoleCredentials;
    private static String role_id = "testrole";
    private static String secret_id = "testsecret";

    @Before
    public void setUp() {
        appRoleCredentials = new AppRoleCredentials(role_id,secret_id);
    }

    @Test
    public void getRole_id() {
        Assert.assertEquals(role_id,appRoleCredentials.getRole_id());
        appRoleCredentials.setRole_id("other");
        Assert.assertNotEquals(role_id,appRoleCredentials.getRole_id());
    }

    @Test
    public void getSecret_id() {
        Assert.assertEquals(secret_id,appRoleCredentials.getSecret_id());
        appRoleCredentials.setSecret_id("other");
        Assert.assertNotEquals(secret_id,appRoleCredentials.getSecret_id());
    }

}
