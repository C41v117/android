package com.btpn.sinaya.eform;

import android.app.Application;

import com.btpn.sinaya.eform.utils.MTFExceptionHandler;

public class MTFApplicationContext extends Application{
	
	private boolean isShowMenu = false;
	private int position = 0;
	private int tabPosition = 0;
	private int titleResourceId = 0;
	private int secondTabPosition = 0;

	public enum TypeImage {
		UNKNOWN,
		KTP,
		NPWP,
		TANDATANGAN,
		FOTONASABAH,
		DOKUMEN_PENDUKUNG,
		KTP2,
		NPWP2,
		TANDATANGAN2,
		FOTONASABAH2,
		DOKUMEN_PENDUKUNG2,
		KK,
		SELFIE
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Thread.setDefaultUncaughtExceptionHandler(new MTFExceptionHandler(this));
	}
	
	public void setShowMenu(boolean isShowMenu) {
		this.isShowMenu = isShowMenu;
	}
	
	public boolean isShowMenu() {
		return isShowMenu;
	}
	
	public void check(){
	}
	
	public int getPosition() {
		return position;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}

	public int getTabPosition() {
		return tabPosition;
	}
	
	public void setTabPosition(int tabPosition) {
		this.tabPosition = tabPosition;
	}

	public int getSecondTabPosition() {
		return secondTabPosition;
	}

	public void setSecondTabPosition(int secondTabPosition) {
		this.secondTabPosition = secondTabPosition;
	}
	
	public int getTitleResourceId() {
		return titleResourceId;
	}

	public void setTitleResourceId(int titleResourceId) {
		this.titleResourceId = titleResourceId;
	}
}
