package com.btpn.sinaya.eform.fragment;

import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.fragment.controller.MTFHeaderGlobalController;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class MTFHeaderGlobalFragment extends MTFBaseFragment{

	private View view;
	private ImageButton backImageButton;
	private TextView titleHeader;
	private MTFHeaderGlobalController controller;
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_header_global, null);
		
		loadView();
		initiateDefaultValue();
		return view;
	}
	
	private void loadView(){
		backImageButton = (ImageButton)view.findViewById(R.id.header_global_back_button);
		titleHeader = (TextView)view.findViewById(R.id.header_global_title_textview);
	}
	
	public void setText(String textTitle){
		titleHeader.setText(textTitle);
	}
	
	private void initiateDefaultValue(){
		controller = new MTFHeaderGlobalController(this);
		
		backImageButton.setOnClickListener(controller);
	}
	
	public boolean onBackPressed(){
		getActivity().finish();
		return false;
	}
	
}
