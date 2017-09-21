package com.btpn.sinaya.eform.alert;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.alert.listener.MTFConfirmationAlertDialogListener;

public class MTFConfirmationAlertDialog extends AlertDialog implements OnClickListener{

	private Button cancelButton;
	private Button okButton;
	private TextView messageTextView;
	private String message = "";
	private MTFConfirmationAlertDialogListener listener;
	
	public MTFConfirmationAlertDialog(Context context, MTFConfirmationAlertDialogListener listener, String message) {
		super(context);
		this.listener = listener;
		this.message = message;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_dialog_confirm);
		
		loadView();
		initDefaultValue();
	}

	private void loadView() {
		okButton = (Button)findViewById(R.id.ok_button);
		cancelButton = (Button)findViewById(R.id.cancel_button);
		messageTextView = (TextView)findViewById(R.id.message_textview);
	}

	private void initDefaultValue() {
		setCancelable(false);
		setCanceledOnTouchOutside(false);
		okButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		
		messageTextView.setText(message);
	}
	
	public void setDialogMessage(String message){
		this.message = message;
		
		if (messageTextView != null) {
			messageTextView.setText(message);
		}
	}
	
	public void setListener(MTFConfirmationAlertDialogListener listener) {
		this.listener = listener;
	}

	@Override
	public void onClick(View v) {
		this.dismiss();
		
		switch (v.getId()) {
		case R.id.ok_button:
			listener.onOK();
			dismiss();
			break;
		case R.id.cancel_button:
			listener.onCancel();
			dismiss();
			break;
		default:
			break;
		}
	}

}
