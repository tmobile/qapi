package com.tmobile.ct.tep.qapi.tvaultclient.domain;

public class AppRoleCredentials {

    private String role_id;
    private String secret_id;

    public AppRoleCredentials(String role_id, String secret_id){
        this.role_id = role_id;
        this.secret_id = secret_id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getSecret_id() {
        return secret_id;
    }

    public void setSecret_id(String secret_id) {
        this.secret_id = secret_id;
    }

    @Override
    public String toString() {
        return "AppRoleCredentials{" +
                "role_id='" + role_id + '\'' +
                ", secret_id='" + secret_id + '\'' +
                '}';
    }
}
