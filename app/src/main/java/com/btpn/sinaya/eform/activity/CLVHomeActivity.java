package com.btpn.sinaya.eform.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import com.btpn.sinaya.eform.MTFBaseActivity;
import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.activity.controller.CLVHomeController;
import com.btpn.sinaya.eform.adapter.CLVHomeListAdapter;
import com.btpn.sinaya.eform.model.CLVUserModel;

import java.util.ArrayList;

/**
 * Created by vaniawidjaja on 10/8/17.
 */

public class CLVHomeActivity extends MTFBaseActivity {

    private CLVHomeActivity activity;
    private CLVHomeController controller;
    private ImageView icSearch;
    private ListView home_list_view;
    private CLVHomeListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clv_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loadView();
        initiateDefaultValue();

    }

    private void loadView() {
        icSearch = (ImageView)findViewById(R.id.icSearch);
        home_list_view = (ListView)findViewById(R.id.home_list_view);

    }

    private void initiateDefaultValue() {
        controller = new CLVHomeController(this);
        icSearch.setOnClickListener(controller);
        adapter = new CLVHomeListAdapter(activity, R.layout.adapter_home_list, new ArrayList<CLVUserModel>());
        home_list_view.setAdapter(adapter);
    }
}
