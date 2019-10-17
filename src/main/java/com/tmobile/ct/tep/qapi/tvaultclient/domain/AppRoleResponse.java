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
