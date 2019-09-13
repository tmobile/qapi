package com.tmobile.ct.tep.qapi.domain;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ReturnObject {
    private String message;
    private HttpStatus status;
    private List result;
    public ReturnObject(){}

    public ReturnObject(String m, HttpStatus status)
    {
        this.message = m;
        this.status = status;
        this.result = null;
    }

    public ReturnObject(List result, HttpStatus status)
    {
        this.result = result;
        this.status = status;
        this.message = "";
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public List getResult() {
        return result;
    }

    public void setResult(List result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "returnobject{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", result=" + result +
                '}';
    }
}
