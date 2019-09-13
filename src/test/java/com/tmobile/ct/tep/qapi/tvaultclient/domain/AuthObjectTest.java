package com.tmobile.ct.tep.qapi.tvaultclient.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AuthObjectTest {

    private AuthObject authObject;
    private static final String client_token = "token";
    @Before
    public void setUp() {
        authObject = new AuthObject();
        authObject.setClient_token(client_token);
    }

    @Test
    public void getClient_token() {
        Assert.assertEquals(client_token,authObject.getClient_token());
    }
}
