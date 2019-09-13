package com.tmobile.ct.tep.qapi.tvaultclient.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    private Token token;
    private static final String tokenValue = "dummytoken";

    @Before
    public void setUp() {
        token = new Token();
        token.setClientToken(tokenValue);
    }

    @Test
    public void getClientToken() {
        Assert.assertEquals(tokenValue,token.getClientToken());
    }
}
