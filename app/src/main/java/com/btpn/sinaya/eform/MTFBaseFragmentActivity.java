package com.btpn.sinaya.eform;

import com.btpn.sinaya.eform.alert.MTFInfoAlertDialog;
import com.btpn.sinaya.eform.alert.MTFLoadingAlertDialog;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.support.v4.app.FragmentActivity;

public class MTFBaseFragmentActivity extends FragmentActivity{
	
	private MTFLoadingAlertDialog loadingAlertDialog;
	private MTFInfoAlertDialog infoAlertDialog;
	
	public void showAlertDialog(String title, String message){
		infoAlertDialog = new MTFInfoAlertDialog(this, title, message);
		infoAlertDialog.show();
		
		MediaPlayer pd = new MediaPlayer();
		pd.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
			
			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {

			}
		});
		
		
	}
	
	public void showLoadingDialog(){
		loadingAlertDialog = new MTFLoadingAlertDialog(this, "");
		loadingAlertDialog.show();
	}
	
	public void dismisLoadingDialog(){
		loadingAlertDialog.dismiss();
	}
}
