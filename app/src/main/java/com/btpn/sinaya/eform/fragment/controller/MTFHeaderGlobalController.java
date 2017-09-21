package com.btpn.sinaya.eform.fragment.controller;

import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.fragment.MTFHeaderGlobalFragment;

import android.view.View;
import android.view.View.OnClickListener;

public class MTFHeaderGlobalController implements OnClickListener{

	MTFHeaderGlobalFragment fragment;
	
	public MTFHeaderGlobalController(MTFHeaderGlobalFragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header_global_back_button:
				fragment.onBackPressed();
			break;

		default:
			break;
		}
	}

}
