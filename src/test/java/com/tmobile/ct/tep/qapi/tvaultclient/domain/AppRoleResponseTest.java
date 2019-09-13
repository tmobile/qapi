package com.tmobile.ct.tep.qapi.tvaultclient.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AppRoleResponseTest {

    private AppRoleResponse appRoleResponse;

    @Before
    public void setUp() throws Exception {
        appRoleResponse = new AppRoleResponse();
        appRoleResponse.setAuth(new AuthObject());
        appRoleResponse.setData("data");
        appRoleResponse.setLease_duration(0);
        appRoleResponse.setLease_id("lease_id");
    }

    @Test
    public void getAuth() {
        Assert.assertNotNull(appRoleResponse.getAuth());
    }

    @Test
    public void getData() {
        Assert.assertNotNull(appRoleResponse.getData());
    }

    @Test
    public void getLease_duration() {
        Assert.assertNotNull(appRoleResponse.getLease_duration());
    }

    @Test
    public void getLease_id() {
        Assert.assertNotNull(appRoleResponse.getLease_id());
    }
}
