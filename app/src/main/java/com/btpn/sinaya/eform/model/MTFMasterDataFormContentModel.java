package com.btpn.sinaya.eform.model;

import com.btpn.sinaya.eform.model.type.MTFMasterDataType;

import java.io.Serializable;


public class MTFMasterDataFormContentModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3632446216373007350L;

	private Long id;
	
	private String title;
	
	private String parent;
	
	private String code;
	
	private MTFMasterDataType masterDataType;
	
	private boolean isHighRisk;
	
	public MTFMasterDataFormContentModel() {
		id = 0L;
		title = "";
		parent = "";
		code = "";
		masterDataType = MTFMasterDataType.UNKNOWN;
		isHighRisk = false;
	}

	public MTFMasterDataFormContentModel(String title, String parent, MTFMasterDataType type) {
		this.id = 0L;
		this.title = title;
		this.parent = parent;
		this.masterDataType = type;
	}
	
	public MTFMasterDataFormContentModel(String title, String code, String parent, MTFMasterDataType type) {
		this.id = 0L;
		this.title = title;
		this.parent = parent;
		this.code = code;
		this.masterDataType = type;
		this.isHighRisk = false;
	}
	
	public MTFMasterDataFormContentModel(String title, String code, String parent, MTFMasterDataType type, boolean isHighRisk) {
		this.id = 0L;
		this.title = title;
		this.parent = parent;
		this.code = code;
		this.masterDataType = type;
		this.isHighRisk = isHighRisk;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public MTFMasterDataType getMasterDataType() {
		return masterDataType;
	}

	public void setMasterDataType(MTFMasterDataType masterDataType) {
		this.masterDataType = masterDataType;
	}
	
	public int getMasterDataTypeValue() {
		return masterDataType.ordinal();
	}

	public boolean isHighRisk() {
		return isHighRisk;
	}

	public void setHighRisk(boolean isHighRisk) {
		this.isHighRisk = isHighRisk;
	}

	
}
