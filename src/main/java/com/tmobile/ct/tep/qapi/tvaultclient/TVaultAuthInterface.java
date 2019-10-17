/*
 * =========================================================================
 * Copyright 2019 T-Mobile USA, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * See the LICENSE file for additional language around disclaimer of warranties.
 * Trademark Disclaimer: Neither the name of “T-Mobile, USA” nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * ===========================================================================
 */

package com.tmobile.ct.tep.qapi.tvaultclient;

import com.tmobile.ct.tep.qapi.tvaultclient.domain.*;
import retrofit2.Call;
import retrofit2.http.*;

public interface TVaultAuthInterface {

    @POST("/vault/v2/auth/tvault/login")
    Call<Token> auth(@Body() Credentials credentials);

    @POST("/vault/v2/auth/approle/login")
    Call<AppRoleResponse> auth(@Body() AppRoleCredentials appRoleCredentials);

    @GET("/vault/v2/safes/folders/secrets")
    Call<Secrets> getSecrets(@Header("vault-token") String vaultToken,
                             @Query("path") String path,
                             @Query("fetchOption") String fetchOption);
}
