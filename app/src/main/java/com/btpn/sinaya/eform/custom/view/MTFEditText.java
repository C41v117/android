package com.btpn.sinaya.eform.custom.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.btpn.sinaya.eform.utils.MTFConstants;
import com.btpn.sinaya.eform.utils.MTFSystemParams;

public class MTFEditText extends EditText implements OnFocusChangeListener{

	private InputMethodManager imm;
	
	public MTFEditText(Context context) {
		super(context);
		initiateDefaultValue(context);
	}

	public MTFEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initiateDefaultValue(context);
	}

	public MTFEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initiateDefaultValue(context);
	}
	
	private void initiateDefaultValue(Context context){
		imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		setTypeface(Typeface.createFromAsset(getResources().getAssets(), MTFConstants.FONT_PATH));
		setOnFocusChangeListener(this);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus){
			imm.showSoftInput(v, LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		}
	}
	
	@Override
	protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
		if(!text.toString().trim().equals("")){
			String[] specialChar = MTFSystemParams.specialCharacter.split("");
			for(int i=0;i<MTFSystemParams.specialCharacter.length();i++){
				if(text.toString().contains(specialChar[i])){
					text = text.toString().replaceAll(specialChar[i], "");
				}
			}
		}
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
	}
}
