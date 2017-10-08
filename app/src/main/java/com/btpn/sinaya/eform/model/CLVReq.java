package com.btpn.sinaya.eform.model;

/**
 * Created by vaniawidjaja on 10/8/17.
 */

public class CLVReq {

    private String referenceNo;
    private String username;
    private String token;

    public String getReferenceNo() {
        return referenceNo;
    }
    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

}