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
