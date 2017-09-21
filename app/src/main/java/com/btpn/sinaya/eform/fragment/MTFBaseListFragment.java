package com.btpn.sinaya.eform.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.fragment.controller.MTFBaseListController;

public abstract class MTFBaseListFragment extends MTFBaseFragment{

	private ListView listView;
	private TextView titleTextView;
	private MTFBaseListController controller;
	
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_base_list_layout, null);
		
		
		return view;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		loadview();
		initiateDefaultValue();
	}
	
	private void loadview() {
		listView = (ListView)view.findViewById(R.id.list_item);
		titleTextView = (TextView) view.findViewById(R.id.title_textview);
	}

	protected void initiateDefaultValue() {
		controller = new MTFBaseListController(this);
		listView.setOnItemClickListener(controller);
	}
	
	public ListView getListView() {
		return listView;
	}
	
	public TextView getTitleTextView(){
		return titleTextView;
	}
}
