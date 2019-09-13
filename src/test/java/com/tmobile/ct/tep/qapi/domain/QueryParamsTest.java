package com.tmobile.ct.tep.qapi.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class QueryParamsTest {

    private QueryParams queryParams;

    @Before
    public void setUp() {
        queryParams = new QueryParams();
        queryParams.setParamsDataTypeList(new ArrayList<>());
        queryParams.setParamsNameList(new ArrayList<>());
        queryParams.setParamsValueList(new ArrayList<>());
        queryParams.setSqlPreparedStatement("sqlpreparedstatement");
    }

    @Test
    public void getParamsNameList() {
        Assert.assertNotNull(queryParams.getParamsNameList());
    }

    @Test
    public void getParamsValueList() {
        Assert.assertNotNull(queryParams.getParamsValueList());
    }

    @Test
    public void getParamsDataTypeList() {
        Assert.assertNotNull(queryParams.getParamsDataTypeList());
    }

    @Test
    public void getSqlPreparedStatement() {
        Assert.assertNotNull(queryParams.getSqlPreparedStatement());
    }

}
