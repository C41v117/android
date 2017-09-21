package com.btpn.sinaya.eform.custom.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Filterable;
import android.widget.ListAdapter;

import com.btpn.sinaya.eform.utils.MTFConstants;
import com.btpn.sinaya.eform.utils.MTFSystemParams;

public class MTFAutoCompleteTextView extends AutoCompleteTextView implements OnFocusChangeListener{
	
	private String currentText = "";
	private boolean isStrictModeActive = false;
	private InputMethodManager imm;
	
	public MTFAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initiateDefaultValue(context);
	}

	public MTFAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initiateDefaultValue(context);
	}

	public MTFAutoCompleteTextView(Context context) {
		super(context);
		initiateDefaultValue(context);
	}
	
	private void initiateDefaultValue(Context context){
		imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		setTypeface(Typeface.createFromAsset(getResources().getAssets(), MTFConstants.FONT_PATH));
		setOnFocusChangeListener(this);
	}
	
	@Override
	protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
		if (isStrictModeActive) {
			actionForTextChanged(text);
		}
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
	
	@Override
	public <T extends ListAdapter & Filterable> void setAdapter(T adapter) {
		super.setAdapter(adapter);
		if (isStrictModeActive && !currentText.equals("")) {
			boolean isFound = false;
			for (int i = 0; i < getAdapter().getCount(); i++) {
				String item = (String)getAdapter().getItem(i);
				if (item.toLowerCase().startsWith(currentText.toLowerCase().trim())) {
					isFound = true;
					break;
				}
			}
			
			if (!isFound) {
				currentText = "";
				setText("");
			}
		}
	}
	
	public void setStrictModeActive(boolean isStrictModeActive) {
		this.isStrictModeActive = isStrictModeActive;
	}
	
	private void actionForTextChanged(CharSequence text){
		
		if (getAdapter() != null && text.length() != 0 && !currentText.equals(text.toString().trim())) {
			boolean isFound = false;
			String selectedItem = "";
			for (int i = 0; i < getAdapter().getCount(); i++) {
				String item = (String)getAdapter().getItem(i);
				if (item.toLowerCase().contains(text.toString().toLowerCase().trim())){
					selectedItem = item;
					isFound = true;
					break;
				}
			}
			
			if(text.toString().trim().length()<currentText.trim().length()){
				currentText = text.toString().trim();
				setText(currentText.trim());
				setSelection(currentText.length());
			}else{
				if (!isFound) {
					setText(currentText.trim());
					setSelection(currentText.trim().length());
				}else{
					currentText = text.toString().trim();
					if (selectedItem.toLowerCase().equals(currentText.toLowerCase().trim())) {
						setText(selectedItem.trim());
						setSelection(selectedItem.length());
					}
				}
			}
		}else if (text.length() == 0){
			currentText = "";
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus){
			imm.showSoftInput(v, LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		}
	}

}
