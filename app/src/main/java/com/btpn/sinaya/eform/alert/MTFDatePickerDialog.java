package com.btpn.sinaya.eform.alert;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.alert.listener.MTFInputDatePickerListener;

public class MTFDatePickerDialog extends AlertDialog implements OnClickListener{

	private View view;
	private TextView dialogHeader;
	private DatePicker datePicker;
	private Button submitButton;
	private Button cancelButton;
	private String date;
	private String headerTitle;
	private SimpleDateFormat dateformat;
	private MTFInputDatePickerListener listener;
	private Date dates;
	
	public MTFDatePickerDialog(Context context, MTFInputDatePickerListener listener, String headerTitle, String date) {
		super(context);
		this.listener = listener;
		this.headerTitle = headerTitle;
		this.date = date;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = LayoutInflater.from(getContext()).inflate(R.layout.alert_dialog_date_picker, null);
		setContentView(view);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		
		loadview();
		initiateDefaultValue();
	}

	private void loadview() {
		dialogHeader = (TextView)view.findViewById(R.id.dialogTitle);
		datePicker = (DatePicker)view.findViewById(R.id.datePicker);
		cancelButton = (Button)view.findViewById(R.id.cancelButton);
		submitButton = (Button)view.findViewById(R.id.submitButton);
	}

	private void initiateDefaultValue() {
		initDateToday();
		submitButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		dialogHeader.setText(headerTitle);
		dateformat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
		Calendar cal = Calendar.getInstance();
		if(date!=null && !date.equals("")){
			Date setDate = null;
			try {
				setDate = dateformat.parse(date);
			} catch (java.text.ParseException e) {
				
			}
        	cal.setTime(setDate);
        	datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		}else{
			if(headerTitle.equals(getContext().getResources().getString(R.string.tanggal_lahir))){
		    	cal.set(Calendar.YEAR,datePicker.getYear()-17);
				cal.set(Calendar.MONTH, datePicker.getMonth());
				cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
		    	datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			}else if(headerTitle.equals(getContext().getResources().getString(R.string.masa_berlaku))){
		    	cal.set(Calendar.YEAR,datePicker.getYear());
				cal.set(Calendar.MONTH, datePicker.getMonth());
				cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth()+1);
		    	datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			}else{
				cal.set(Calendar.YEAR,datePicker.getYear());
				cal.set(Calendar.MONTH, datePicker.getMonth());
				cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
				datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			}
		}
	}

	private void initDateToday() {
		if(headerTitle.equals(getContext().getResources().getString(R.string.tanggal_lahir))){
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR,datePicker.getYear()-17);
			cal.set(Calendar.MONTH, datePicker.getMonth());
			cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
			dates = cal.getTime();
		}else if(headerTitle.equals(getContext().getResources().getString(R.string.masa_berlaku))){
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR,datePicker.getYear());
			cal.set(Calendar.MONTH, datePicker.getMonth());
			cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth()+1);
			dates = cal.getTime();
		}else{
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR,datePicker.getYear());
			cal.set(Calendar.MONTH, datePicker.getMonth());
			cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
			dates = cal.getTime();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submitButton:
			datePicker.getFocusedChild().clearFocus();
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR,datePicker.getYear());
			cal.set(Calendar.MONTH, datePicker.getMonth());
			cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
			Date date = cal.getTime();
			if(headerTitle.equals(getContext().getResources().getString(R.string.tanggal_lahir))){
				if(dates.after(date)){
					listener.onSubmit();
				}else{
					Toast.makeText(getContext(), "Umur Nasabah Minimal 17 Tahun", Toast.LENGTH_SHORT).show();
				}
			}else if(headerTitle.equals(getContext().getResources().getString(R.string.masa_berlaku))){
				if(dates.after(date)){
					Toast.makeText(getContext(), "Identitas Anda Sudah Tidak Aktif", Toast.LENGTH_SHORT).show();
				}else{
					listener.onSubmit();
				}
			}else if(headerTitle.equals(getContext().getResources().getString(R.string.loi_period))){
				if(dates.after(date)){
					Toast.makeText(getContext(), "LOI Anda Sudah Tidak Aktif", Toast.LENGTH_SHORT).show();
				}else{
					listener.onSubmit();
				}
			}else{
				listener.onSubmit();
			}
			break;
		case R.id.cancelButton:
				listener.onCancel();
			break;
		default:
			break;
		}
	}
	
	public String getDate(){
		datePicker.clearFocus();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,datePicker.getYear());
		cal.set(Calendar.MONTH, datePicker.getMonth());
		cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
		Date date = cal.getTime();
		String dateText = dateformat.format(date);
		return dateText;
	}

}
