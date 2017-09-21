package com.btpn.sinaya.eform.model;

import java.io.Serializable;
import java.util.Date;

public class MTFProfileModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8275140829436112488L;

	private Long userId;
	
	private String agentName;
	
	private String agentPhone;
	
	private Date joinDate;
	
	private int acceptedCustomers;
	
	private int rejectedCustomers;
	
	private int totalCustomers;
	
	private String currentlyCustomer;
	
	private int ratioApproval;
	public MTFProfileModel() {
		userId = 0l;
		agentName = "";
		agentPhone = "";
		joinDate = new Date();
		acceptedCustomers = 0;
		rejectedCustomers = 0;
		totalCustomers = 0;
		currentlyCustomer = "";
		ratioApproval = 0;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentPhone() {
		return agentPhone;
	}

	public void setAgentPhone(String agentPhone) {
		this.agentPhone = agentPhone;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public int getAcceptedCustomers() {
		return acceptedCustomers;
	}

	public void setAcceptedCustomers(int acceptedCustomers) {
		this.acceptedCustomers = acceptedCustomers;
	}

	public int getRejectedCustomers() {
		return rejectedCustomers;
	}

	public void setRejectedCustomers(int rejectedCustomers) {
		this.rejectedCustomers = rejectedCustomers;
	}

	public int getTotalCustomers() {
		return totalCustomers;
	}

	public void setTotalCustomers(int totalCustomers) {
		this.totalCustomers = totalCustomers;
	}

	public String getCurrentlyCustomer() {
		return currentlyCustomer;
	}

	public void setCurrentlyCustomer(String currentlyCustomer) {
		this.currentlyCustomer = currentlyCustomer;
	}

	public int getRatioApproval() {
		return ratioApproval;
	}

	public void setRatioApproval(int ratioApproval) {
		this.ratioApproval = ratioApproval;
	}

}
