package com.btpn.sinaya.eform.model;

/**
 * Created by vaniawidjaja on 10/8/17.
 */

public class CLVLoginReq extends CLVReq{

    private String username;
    private String password;
    private String apkVersion;
    private String modelNumber;
    private String deviceUid;
    private String androidVersion;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getApkVersion() {
        return apkVersion;
    }
    public void setApkVersion(String apkVersion) {
        this.apkVersion = apkVersion;
    }
    public String getModelNumber() {
        return modelNumber;
    }
    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }
    public String getDeviceUid() {
        return deviceUid;
    }
    public void setDeviceUid(String deviceUid) {
        this.deviceUid = deviceUid;
    }
    public String getAndroidVersion() {
        return androidVersion;
    }
    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

}