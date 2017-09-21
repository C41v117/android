package com.btpn.sinaya.eform.model;

import java.io.Serializable;
import java.util.List;

public class MTFLoginModel implements Serializable{

    private static final long serialVersionUID = -385170360778312096L;

    private Integer appId;
    private Integer lob;
    private String division;
    private List<String> appFunctionDescr;
    private String raCode;
    private String roCode;

    public MTFLoginModel(){
        super();
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
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

    public List<String> getAppFunctionDescr() {
        return appFunctionDescr;
    }

    public void setAppFunctionDescr(List<String> appFunctionDescr) {
        this.appFunctionDescr = appFunctionDescr;
    }

    public String getRaCode() {
        return raCode;
    }

    public void setRaCode(String raCode) {
        this.raCode = raCode;
    }

    public String getRoCode() {
        return roCode;
    }

    public void setRoCode(String roCode) {
        this.roCode = roCode;
    }
}
