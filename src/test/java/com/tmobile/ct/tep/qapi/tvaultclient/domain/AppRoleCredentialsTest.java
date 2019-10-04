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
