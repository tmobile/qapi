package com.tmobile.ct.tep.qapi.service;

import com.tmobile.ct.tep.qapi.domain.QueryParams;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class DbServiceTest {

    @Test
    public void getParams(){
        DbService s = new DbService();
        JSONObject testbody = new JSONObject();
        testbody.put("id", "6");
        String sqlconfig = "select * FROM users where id < {id}";
        QueryParams params = s.getParams(sqlconfig,testbody);
        Assert.assertNotNull(params.getParamsNameList());
    }
}
