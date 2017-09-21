package com.btpn.sinaya.eform.connection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class MTFConnectionDetector extends BroadcastReceiver {


	/**
	 * Checking for all possible internet providers
	 * **/
	public boolean isConnectingToInternet(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		NetworkInfo mobNetInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (activeNetInfo != null) {
			Toast.makeText(context,
					"Active Network Type : " + activeNetInfo.getTypeName(),
					Toast.LENGTH_SHORT).show();
		}
		if (mobNetInfo != null) {
			Toast.makeText(context,
					"Mobile Network Type : " + mobNetInfo.getTypeName(),
					Toast.LENGTH_SHORT).show();
		}
	}
}
