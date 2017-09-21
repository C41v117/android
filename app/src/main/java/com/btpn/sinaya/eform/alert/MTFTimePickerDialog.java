package com.btpn.sinaya.eform.alert;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.alert.listener.MTFInputTimePickerListener;
import com.btpn.sinaya.eform.utils.MTFSystemParams;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MTFTimePickerDialog extends AlertDialog implements OnClickListener{

	private View view;
	private TextView dialogHeader;
	private TimePicker timePicker;
	private Button submitButton;
	private Button cancelButton;
	private String time;
	private String headerTitle;
	private SimpleDateFormat timeformat;
	private MTFInputTimePickerListener listener;
	private Date times;

	public MTFTimePickerDialog(Context context, MTFInputTimePickerListener listener, String headerTitle, String time) {
		super(context);
		this.listener = listener;
		this.headerTitle = headerTitle;
		this.time = time;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = LayoutInflater.from(getContext()).inflate(R.layout.alert_time_picker_dialog, null);
		setContentView(view);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		loadview();
		initiateDefaultValue();
	}

	private void loadview() {
		dialogHeader = (TextView)view.findViewById(R.id.dialogTitle);
		timePicker = (TimePicker)view.findViewById(R.id.timePicker);
		cancelButton = (Button)view.findViewById(R.id.cancelButton);
		submitButton = (Button)view.findViewById(R.id.submitButton);
	}

	private void initiateDefaultValue() {
		timePicker.setIs24HourView(true);
		submitButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		dialogHeader.setText(headerTitle);
		timeformat = new SimpleDateFormat("HH:mm", Locale.US);
		Calendar cal = Calendar.getInstance();
		if(time!=null && !time.equals("")){
			Date setTime = null;
			try {
				setTime = timeformat.parse(time);
			} catch (java.text.ParseException e) {

			}
        	cal.setTime(setTime);
        	timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
			timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
		}else{
			timePicker.setCurrentHour(0);
			timePicker.setCurrentMinute(0);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submitButton:
			/*SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);

			Calendar cal = Calendar.getInstance();
			Date time = null;
			try {
				time = timeFormat.parse(MTFSystemParams.callbackStart);
			} catch (java.text.ParseException e) {

			}
			cal.setTime(time);
			cal.set(Calendar.HOUR, timePicker.getCurrentHour());
			cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());

			Calendar minCal = Calendar.getInstance();
			Date minTime = null;
			try {
				minTime = timeFormat.parse(MTFSystemParams.callbackStart);
			} catch (java.text.ParseException e) {

			}
			minCal.setTime(minTime);
			minCal.add(Calendar.MINUTE, MTFSystemParams.callbackStartGap);

			Calendar maxCal = Calendar.getInstance();
			Date maxTime = null;
			try {
				maxTime = timeFormat.parse(MTFSystemParams.callbackEnd);
			} catch (java.text.ParseException e) {

			}
			maxCal.setTime(maxTime);
			maxCal.add(Calendar.MINUTE, -MTFSystemParams.callbackEndGap);

			if(cal.before(minCal)){
				Toast.makeText(getContext(), "Silahkan input callback time diatas jam "+timeFormat.format(minCal.getTime()),
						Toast.LENGTH_SHORT).show();
			}else if(cal.after(maxCal)){
				Toast.makeText(getContext(), "Silahkan input callback time sebelum jam "+timeFormat.format(maxCal.getTime()),
						Toast.LENGTH_SHORT).show();
			}else{*/
				listener.onSubmit();
			//}
			break;
		case R.id.cancelButton:
				listener.onCancel();
			break;
		default:
			break;
		}
	}

	public String getTime(){
		if(timePicker == null)
			return null;

		return timePicker.getCurrentHour() + ":" + (timePicker.getCurrentMinute()>10?timePicker.getCurrentMinute():"0"+timePicker.getCurrentMinute());
	}

}
