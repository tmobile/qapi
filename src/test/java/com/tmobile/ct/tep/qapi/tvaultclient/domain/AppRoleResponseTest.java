/*
 * // =========================================================================
 * // Copyright © 2019 T-Mobile USA, Inc.
 * //
 * // Licensed under the Apache License, Version 2.0 (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at
 * //
 * //    http://www.apache.org/licenses/LICENSE-2.0
 * //
 * // Unless required by applicable law or agreed to in writing, software
 * // distributed under the License is distributed on an "AS IS" BASIS,
 * // WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * // See the License for the specific language governing permissions and
 * // limitations under the License.
 * // =========================================================================
 *
 */

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
