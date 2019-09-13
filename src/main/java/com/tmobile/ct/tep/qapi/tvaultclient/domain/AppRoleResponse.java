package com.tmobile.ct.tep.qapi.tvaultclient.domain;

public class AppRoleResponse {
    private AuthObject auth;
    private String data;
    private int lease_duration;
    private String lease_id;

    public AppRoleResponse(){}

    public AuthObject getAuth() {
        return auth;
    }

    public void setAuth(AuthObject auth) {
        this.auth = auth;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getLease_duration() {
        return lease_duration;
    }

    public void setLease_duration(int lease_duration) {
        this.lease_duration = lease_duration;
    }

    public String getLease_id() {
        return lease_id;
    }

    public void setLease_id(String lease_id) {
        this.lease_id = lease_id;
    }

    @Override
    public String toString() {
        return "AppRoleResponse{" +
                "auth=" + auth +
                ", data='" + data + '\'' +
                ", lease_duration=" + lease_duration +
                ", lease_id='" + lease_id + '\'' +
                '}';
    }
}
