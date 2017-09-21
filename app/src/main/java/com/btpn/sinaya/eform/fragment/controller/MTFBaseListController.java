package com.btpn.sinaya.eform.fragment.controller;

import com.btpn.sinaya.eform.fragment.MTFBaseListFragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MTFBaseListController implements OnItemClickListener {

	MTFBaseListFragment fragment;
	
	public MTFBaseListController(MTFBaseListFragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long identifier) {
		
	}
}
