package com.btpn.sinaya.eform.alert;

import com.btpn.sinaya.eform.R;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

public class MTFLoadingAlertDialog extends AlertDialog{

	private TextView loadingText;
	private String titleText;
	
	public MTFLoadingAlertDialog(Context context, String titleText) {
		super(context);
		this.titleText = titleText;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_dialog_loading);
		loadView();
		initiateDefaultValue();
	}
	
	private void loadView(){
		loadingText = (TextView)findViewById(R.id.loading_text);
	}
	
	private void initiateDefaultValue(){
		loadingText.setText(titleText);
	}
	
	public void setLoadingText(String text){
		titleText = text;
		if (loadingText != null) {
			loadingText.setText(text);
		}
	}

}
