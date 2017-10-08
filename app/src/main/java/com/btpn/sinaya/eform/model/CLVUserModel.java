package com.btpn.sinaya.eform.model;

import java.io.Serializable;
import java.util.Date;

public class CLVUserModel implements Serializable {

	private static final long serialVersionUID = -819480506707870364L;

	private Long id;
	private String username;
	private String password;
	private String token;
	private Date createdDate;
	private Date lastLogInDate;
	private String email;
	private boolean verify;
	private Date verifyDate;
	private Date lockedDate;
	private Boolean lock;
	private String apkVersion;
    private String modelNumber;
	private String deviceUid;
    private String androidVersion;
    private String verificationToken;
    private String passwordToken;
    private Date pwdTokenExpire;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastLogInDate() {
		return lastLogInDate;
	}

	public void setLastLogInDate(Date lastLogInDate) {
		this.lastLogInDate = lastLogInDate;
	}

	public Boolean getVerify() {
		return verify;
	}

	public void setVerify(Boolean verify) {
		this.verify = verify;
	}

	public Date getVerifyDate() {
		return verifyDate;
	}

	public void setVerifyDate(Date verifyDate) {
		this.verifyDate = verifyDate;
	}

	public Date getLockedDate() {
		return lockedDate;
	}

	public void setLockedDate(Date lockedDate) {
		this.lockedDate = lockedDate;
	}

	public Boolean getLock() {
		return lock;
	}

	public void setLock(Boolean lock) {
		this.lock = lock;
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getVerificationToken() {
		return verificationToken;
	}

	public void setVerificationToken(String verificationToken) {
		this.verificationToken = verificationToken;
	}

	public String getPasswordToken() {
		return passwordToken;
	}

	public void setPasswordToken(String passwordToken) {
		this.passwordToken = passwordToken;
	}

	public Date getPwdTokenExpire() {
		return pwdTokenExpire;
	}

	public void setPwdTokenExpire(Date pwdTokenExpire) {
		this.pwdTokenExpire = pwdTokenExpire;
	}

}
