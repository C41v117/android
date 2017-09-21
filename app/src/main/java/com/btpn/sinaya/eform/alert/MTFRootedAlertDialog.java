package com.btpn.sinaya.eform.alert;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.alert.listener.MTFRootedAlertDialogListener;

/**
 * @author Chris Samuel
 *
 */
public class MTFRootedAlertDialog extends AlertDialog implements OnClickListener{

	private String title;
	private String message;
	private TextView titleTextView;
	private TextView messageTextView;
	private Button okButton;
	private MTFRootedAlertDialogListener listener;

	public MTFRootedAlertDialog(Context context, String message) {
		super(context);
		this.title = title;
		this.message = message;
	}

	public MTFRootedAlertDialog(Context context, String message, MTFRootedAlertDialogListener listener) {
		super(context);
		this.message = message;
		this.listener = listener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_dialog_rooted);
		
		loadView();
		initiateDefaultValue();
	}
	
	@Override
	public void onClick(View v) {
		dismiss();
		if(listener!=null)
			listener.onOk();
	}
	
	public void setCustomMessage(String message) {
		this.message = message;
		if (messageTextView != null) {
			messageTextView.setText(message);
		}
	}
	
	private void loadView(){
		messageTextView = (TextView)findViewById(R.id.message_textview);
		okButton = (Button)findViewById(R.id.ok_button);
	}
	
	private void initiateDefaultValue(){
		setCancelable(false);
		setCanceledOnTouchOutside(false);
		messageTextView.setText(message);
		okButton.setOnClickListener(this);
	}
	
	public void setListener(MTFRootedAlertDialogListener listener) {
		this.listener = listener;
	}
	
}
