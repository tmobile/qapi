package com.tmobile.ct.tep.qapi.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ReturnObjectTest {

    private ReturnObject returnObject;

    @Before
    public void setUp() {
        returnObject = new ReturnObject("test", HttpStatus.OK);
        returnObject.setResult(new ArrayList());
    }

    @Test
    public void getMessage() {
        Assert.assertNotNull(returnObject.getMessage());
    }

    @Test
    public void getStatus() {
        Assert.assertNotNull(returnObject.getStatus());
    }

    @Test
    public void getResult() {
        Assert.assertNotNull(returnObject.getResult());
    }
}
