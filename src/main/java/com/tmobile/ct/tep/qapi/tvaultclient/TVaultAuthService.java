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


import com.tmobile.ct.tep.qapi.config.ApplicationProperties;
import com.tmobile.ct.tep.qapi.exceptions.ApiFailureException;
import com.tmobile.ct.tep.qapi.tvaultclient.config.Config;
import com.tmobile.ct.tep.qapi.tvaultclient.config.Settings;
import com.tmobile.ct.tep.qapi.tvaultclient.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import java.util.Properties;

@Service
@Configuration
public class TVaultAuthService {

	private static final Logger log = LoggerFactory.getLogger(TVaultAuthService.class);

	private RetroClient retroClient;
	private ServiceHelper serviceHelper;
	private Properties properties;
	private TVaultAuthInterface service;
	private String authUrl;
	private int maxRequestRetries;
	private boolean failFast;

	@Autowired
	private ApplicationProperties applicationProperties;

	public TVaultAuthService(){}

	public void initConfig() {
		this.properties = new Settings().getProperties();
		this.authUrl = applicationProperties.getTvault().getTvaultHost();
		this.retroClient = new RetroClient(this.properties);
		this.maxRequestRetries = Integer.parseInt(properties.getProperty(Config.TEP_MAX_RETRIES));
		this.failFast = Boolean.parseBoolean(properties.getProperty(Config.TEP_FAIL_FAST));
		log.debug("auth url : [{}]", authUrl);
		failFast = Boolean.parseBoolean(properties.getProperty(Config.TEP_FAIL_FAST));
		service = retroClient.init(authUrl).getRetro().create(TVaultAuthInterface.class);
		this.serviceHelper = new ServiceHelper(maxRequestRetries, failFast);

	}

	public Token auth(Credentials credentials) {
		if (null == credentials) {
			return null;
		}
		Call<Token> call = service.auth(credentials);
		return serviceHelper.handleResponse(call);
	}

	public String auth(AppRoleCredentials credentials){
		if (null == credentials)
			return null;
		Call<AppRoleResponse> call = service.auth(credentials);
		return serviceHelper.handleResponse(call).getAuth().getClient_token();
	}

	public Secrets getSecrets(String token, String safePath) {

        if (null == token || null == safePath) {
            return null;
        }
        String fetchOption = "secrets";
        Call<Secrets> call = service.getSecrets(token, safePath, fetchOption);
        Secrets secrets = serviceHelper.handleResponse(call);
        if (null == secrets) {
            throw new ApiFailureException("Invalid/expired client token or Insufficient permissions to T-Vault safe. Ensure the API has access to the safe specified.");
        }
        return secrets;
	}
}
