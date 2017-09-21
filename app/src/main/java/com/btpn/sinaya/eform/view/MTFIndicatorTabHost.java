package com.btpn.sinaya.eform.view;

import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.utils.MTFGenerator;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MTFIndicatorTabHost extends LinearLayout{

	private String tabLabel;
	private TextView textViewLabel;
	private boolean isUseFixSize;
	
	public MTFIndicatorTabHost(Context context, String tabLabel, boolean isUseFixSize) {
		super(context);
		this.tabLabel = tabLabel;
		this.isUseFixSize = isUseFixSize;
		
		loadView();
		initiateDefaultValue();
		
	}
	
	private void loadView(){
		LayoutInflater.from(getContext()).inflate(R.layout.indicator_tab_host, this);
		textViewLabel = (TextView)findViewById(R.id.indicator_tab_host_label_textview);
	}
	
	private void initiateDefaultValue(){
		if (isUseFixSize) {
			this.setLayoutParams(new LayoutParams(MTFGenerator.generatePixelsToDip(getContext(), 80), LayoutParams.MATCH_PARENT));
		}
		textViewLabel.setText(tabLabel);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		textViewLabel.setEnabled(enabled);
	}
}
