package com.btpn.sinaya.eform.custom.view;

import com.btpn.sinaya.eform.utils.MTFConstants;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class MTFButton extends Button{

	public MTFButton(Context context) {
		super(context);
		initiateDefaultValue();
	}

	public MTFButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initiateDefaultValue();
	}
	
	public MTFButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initiateDefaultValue();
	}
	
	private void initiateDefaultValue(){
		setTypeface(Typeface.createFromAsset(getResources().getAssets(), MTFConstants.FONT_PATH));
	}

}
