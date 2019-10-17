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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.util.concurrent.MoreExecutors;
import com.tmobile.ct.tep.qapi.tvaultclient.config.Config;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class RetroClient {

	private static final Logger logger = LoggerFactory.getLogger(RetroClient.class);

	private int READ_TIMEOUT;
	private int CONN_TIMEOUT;

	private Retrofit retrofit;
	private String baseUrl;

	public RetroClient(Properties properties) {
		this.READ_TIMEOUT = Integer.parseInt(properties.getProperty(Config.HTTP_TIMEOUT_READ));
		this.CONN_TIMEOUT = Integer.parseInt(properties.getProperty(Config.HTTP_TIMEOUT_CONN));
	}

	public Retrofit getRetro() {
		return this.retrofit;
	}

	RetroClient init(String baseUrl) {

		this.baseUrl = baseUrl;

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		this.retrofit = new Retrofit.Builder().client(getOkClient()).baseUrl(baseUrl)
				.addConverterFactory(JacksonConverterFactory.create(objectMapper)).build();

		return this;
	}

	private OkHttpClient getOkClient() {

		return getNewOkClientBuilder().build();
	}

	private Builder getNewOkClientBuilder() {

		try {
			TrustManagerFactory trustManagerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(readKeyStore());
			TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
			if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
				throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
			}
			X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[] { trustManager }, null);
			SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

			return new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, trustManager)
					.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
					.dispatcher(new Dispatcher(MoreExecutors.newDirectExecutorService()));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	private KeyStore readKeyStore() {
		char[] password = "changeit".toCharArray();
		try (InputStream stream = TVaultAuthService.class.getClassLoader()
				.getResourceAsStream("trust.jks")) {
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(stream, password);
			return ks;
		} catch (NoSuchAlgorithmException | CertificateException | IOException | KeyStoreException e) {
			// TODO Auto-generated catch block
			logger.debug(e.getMessage());
		}
		return null;
	}
}
