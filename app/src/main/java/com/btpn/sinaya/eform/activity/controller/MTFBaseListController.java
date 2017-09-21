package com.btpn.sinaya.eform.activity.controller;

import com.btpn.sinaya.eform.MTFBaseListActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MTFBaseListController implements OnItemClickListener {

	MTFBaseListActivity activity;
	
	public MTFBaseListController(MTFBaseListActivity activity) {
		this.activity = activity;
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long identifier) {
		
	}
}
