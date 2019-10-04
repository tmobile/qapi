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
