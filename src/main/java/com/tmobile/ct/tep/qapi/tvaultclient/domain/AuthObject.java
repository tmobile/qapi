package com.tmobile.ct.tep.qapi.tvaultclient.domain;

public class AuthObject {

    private String client_token;

    public AuthObject(){}

    public String getClient_token() {
        return client_token;
    }

    public void setClient_token(String client_token) {
        this.client_token = client_token;
    }

    @Override
    public String toString() {
        return "AuthObject{" +
                "client_token='" + client_token + '\'' +
                '}';
    }
}
