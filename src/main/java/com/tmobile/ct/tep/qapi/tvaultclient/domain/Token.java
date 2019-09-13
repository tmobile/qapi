package com.tmobile.ct.tep.qapi.tvaultclient.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {

	@JsonProperty(value = "client_token")
	private String clientToken;

	@JsonProperty(value = "client_token")
	public String getClientToken() {
		return clientToken;
	}

	@JsonProperty(value = "client_token")
	public void setClientToken(String clientToken) {
		this.clientToken = clientToken;
	}
}
