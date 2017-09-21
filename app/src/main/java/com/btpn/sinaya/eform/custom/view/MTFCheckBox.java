package com.btpn.sinaya.eform.custom.view;

import com.btpn.sinaya.eform.utils.MTFConstants;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;

public class MTFCheckBox extends CheckBox{

	public MTFCheckBox(Context context) {
		super(context);
		initiateDefaultValue();
	}

	public MTFCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		initiateDefaultValue();
	}
	
	public MTFCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initiateDefaultValue();
	}
	
	private void initiateDefaultValue(){
		setTypeface(Typeface.createFromAsset(getResources().getAssets(), MTFConstants.FONT_PATH));
	}
}
