package com.tmobile.ct.tep.qapi.tvaultclient.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class SecretsTest {

    private Secrets secrets;

    @Before
    public void setUp() {
        secrets = new Secrets();
        secrets.setData(new HashMap<>());
    }

    @Test
    public void getData() {
        Assert.assertNotNull(secrets.getData());
    }
}
