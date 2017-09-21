package com.btpn.sinaya.eform.model;

public class MTFVersionModel {
	
	private Long id ;
	
	private String type;
	
	private Integer version;

	public MTFVersionModel() {
		type = "";
		version = 0;
	}
	
	public MTFVersionModel(String moduleType, int version) {
		this.type = moduleType;
		this.version = version;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
}
