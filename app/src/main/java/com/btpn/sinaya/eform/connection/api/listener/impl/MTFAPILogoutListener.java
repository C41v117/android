package com.btpn.sinaya.eform.connection.api.listener.impl;

import com.btpn.sinaya.eform.connection.listener.MTFConnectionListener;
import com.btpn.sinaya.eform.utils.MTFIntentConstant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

public class MTFAPILogoutListener implements MTFConnectionListener {

	@Override
	public void onSend(Bundle bundle, Context context) {
		Intent intent = new Intent();
		intent.setAction(MTFIntentConstant.ACTION_LOGOUT);
		intent.addCategory(MTFIntentConstant.CATEGORY_MENU);
		intent.putExtras(bundle);
		
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}

}