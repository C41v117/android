package com.btpn.sinaya.eform.custom.view;

import com.btpn.sinaya.eform.utils.MTFConstants;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

public class MTFTextView extends TextView{

	public MTFTextView(Context context) {
		super(context);
		initiateDefaultValue();
	}

	public MTFTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initiateDefaultValue();
	}
	
	public MTFTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initiateDefaultValue();
	}
	
	private void initiateDefaultValue(){
		setTypeface(Typeface.createFromAsset(getResources().getAssets(), MTFConstants.FONT_PATH));
	}

	@Override
	public void setError(CharSequence error, Drawable icon) {
		setCompoundDrawables(null, null, icon, null);
	}
}
