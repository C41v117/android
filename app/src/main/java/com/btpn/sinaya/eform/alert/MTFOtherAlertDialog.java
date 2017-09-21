package com.btpn.sinaya.eform.alert;

import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.alert.listener.MTFOtherAlertDialogListener;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MTFOtherAlertDialog extends AlertDialog implements OnClickListener{
	
	MTFOtherAlertDialogListener listener;
	private View view;
	private TextView titleTextView;
	private TextView errorTextView;
	private EditText otherEditText;
	private Button cancelButton;
	private Button okButton;
	private InputMethodManager imm;
	private OnFocusChangeListener focusListener = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			InputMethodManager imm = (InputMethodManager)getWindow().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
			
		}
	};
	
	public MTFOtherAlertDialog(Context context, MTFOtherAlertDialogListener listener) {
		super(context);
		this.listener = listener;
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = LayoutInflater.from(getContext()).inflate(R.layout.alert_dialog_others, null);
		setContentView(view);
		
		loadView();
		initDefaultValue();
	}

	private void loadView() {
		titleTextView = (TextView)view.findViewById(R.id.alert_dialog_title);
		errorTextView = (TextView)view.findViewById(R.id.input_other_error_dialog);
		otherEditText = (EditText)view.findViewById(R.id.input_other_edittext);
		cancelButton = (Button)view.findViewById(R.id.input_other_cancel_button);
		okButton = (Button)view.findViewById(R.id.input_other_ok_button);
	}

	private void initDefaultValue() {
		otherEditText.setOnFocusChangeListener(focusListener);
		imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		cancelButton.setOnClickListener(this);
		okButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		errorTextView.setVisibility(View.INVISIBLE);
		
		switch (v.getId()) {
		case R.id.input_other_cancel_button:
			imm.hideSoftInputFromWindow(otherEditText.getWindowToken(), 0);
			listener.onCancel();
			break;
		case R.id.input_other_ok_button:
			imm.hideSoftInputFromWindow(otherEditText.getWindowToken(), 0);
			listener.onSubmit();
			otherEditText.setText("");
			break;

		default:
			break;
		}
		
	}
	
	public void setHeaderTitle(String headerTitle){
		titleTextView.setText(headerTitle);
	}
	
	public String getOtherText(){
		return otherEditText.getText().toString();
	}
	
	public void showError(){
		errorTextView.setVisibility(View.VISIBLE);
	}
}
