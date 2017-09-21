package com.btpn.sinaya.eform.alert;

import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.alert.listener.MTFInfoAlertDialogListener;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MTFInfoAlertDialog extends AlertDialog implements android.view.View.OnClickListener{
	
	private String title;
	private String message;
	private TextView titleTextView;
	private TextView messageTextView;
	private Button okButton;
	private MTFInfoAlertDialogListener listener;
	
	public MTFInfoAlertDialog(Context context, String title, String message) {
		super(context);
		this.title = title;
		this.message = message;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_dialog_info);
		
		loadView();
		initiateDefaultValue();
	}
	
	@Override
	public void onClick(View v) {
		this.dismiss();
		if(listener!=null)
			listener.onOk();
	}
	
	private void loadView(){
		titleTextView = (TextView)findViewById(R.id.alert_info_title);
		messageTextView = (TextView)findViewById(R.id.alert_info_message);
		okButton = (Button)findViewById(R.id.alert_info_button_ok);
	}
	
	private void initiateDefaultValue(){
		titleTextView.setText(title);
		messageTextView.setText(message);
		okButton.setOnClickListener(this);
	}
	
	public void setListener(MTFInfoAlertDialogListener listener) {
		this.listener = listener;
	}
	
}
