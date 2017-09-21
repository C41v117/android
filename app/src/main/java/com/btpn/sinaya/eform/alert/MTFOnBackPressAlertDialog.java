package com.btpn.sinaya.eform.alert;

import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.alert.listener.MTFOnBackPressAlertDialogListener;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MTFOnBackPressAlertDialog extends AlertDialog implements android.view.View.OnClickListener{

	private Button cancelButton;
	private Button okButton;
	private TextView messageTextView;
	private String message = "";
	private MTFOnBackPressAlertDialogListener listener;
	
	public MTFOnBackPressAlertDialog(Context context, MTFOnBackPressAlertDialogListener listener) {
		super(context);
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_dialog_confirm);
		
		onLoadView();
		initDefaultValue();
	}

	private void onLoadView() {
		cancelButton = (Button)findViewById(R.id.cancel_button);
		okButton = (Button)findViewById(R.id.ok_button);
		messageTextView = (TextView)findViewById(R.id.message_textview);
	}

	private void initDefaultValue() {
		message = "Anda Yakin untuk Keluar ?";
		messageTextView.setText(message);
		okButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel_button:
			dismiss();
			listener.onCancel();
			break;
		case R.id.ok_button:
			dismiss();
			listener.onOk();
			break;

		default:
			break;
		}
	}
}
