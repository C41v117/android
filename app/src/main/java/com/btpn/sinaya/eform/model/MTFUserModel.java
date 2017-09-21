package com.btpn.sinaya.eform.model;

import java.io.Serializable;
import java.util.Date;

public class MTFUserModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2362804624670330845L;

	private Long id;
	private String userName;
	private String pin;
	private String token;
	private Date lastLogin;
	private String imei;
	private String userId;
	private boolean isOnline;
	private String agentType;
	private Long locationId;
	private String locationName;
    private String raCode;
	private String roCode;
    private Integer lob;
    private String division;
    private String jwt;
    private String secret;
    //for passing data apk
    private String nopen;
    private String kodePensiun;
    private String nama;
    private String tglLahir;

    public String getNopen() {
        return nopen;
    }

    public void setNopen(String nopen) {
        this.nopen = nopen;
    }

    public String getKodePensiun() {
        return kodePensiun;
    }

    public void setKodePensiun(String kodePensiun) {
        this.kodePensiun = kodePensiun;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(String tglLahir) {
        this.tglLahir = tglLahir;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public String getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getRoCode() {
		return roCode;
	}

	public void setRoCode(String roCode) {
		this.roCode = roCode;
	}

    public Integer getLob() {
        return lob;
    }

    public void setLob(Integer lob) {
        this.lob = lob;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getRaCode() {
        return raCode;
    }

    public void setRaCode(String raCode) {
        this.raCode = raCode;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
