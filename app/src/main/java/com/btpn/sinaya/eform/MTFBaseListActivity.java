package com.btpn.sinaya.eform;


import android.os.Bundle;
import android.widget.ListView;

import com.btpn.sinaya.eform.activity.controller.MTFBaseListController;
import com.btpn.sinaya.eform.fragment.MTFHeaderGlobalFragment;

public abstract class MTFBaseListActivity extends MTFBaseActivity{

	private ListView listView;
	private MTFBaseListController controller;
	private MTFHeaderGlobalFragment fragmentHeader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_list_layout);
		
		loadview();
		initiateDefaultValue();
	}

	@Override
	public void onBackPressed() {
		if (fragmentHeader.onBackPressed()) {
			return;
		}
		super.onBackPressed();
	}
	
	private void loadview() {
		fragmentHeader = (MTFHeaderGlobalFragment) getSupportFragmentManager().findFragmentById(R.id.global_header_fragment);
		listView = (ListView)findViewById(R.id.list_item);
	}

	protected void initiateDefaultValue() {
		controller = new MTFBaseListController(this);
		listView.setOnItemClickListener(controller);
	}
	
	public void setHeaderTitle(String string){
		fragmentHeader.setText(string);
	}
	
//	public void initiateListView(List<MTFHelpModel> listHelp){
//		MTFListHelperAdapter adapter = new MTFListHelperAdapter(this, R.layout.adapter_helper_list, listHelp );
//		listView.setAdapter(adapter);
//	}
	
}
