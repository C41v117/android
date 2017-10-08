package com.btpn.sinaya.eform.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ImageView;

import com.btpn.sinaya.eform.MTFBaseActivity;
import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.activity.controller.CLVHomeController;
import com.btpn.sinaya.eform.activity.controller.CLVSearchController;

/**
 * Created by vaniawidjaja on 10/8/17.
 */

public class CLVSearchActivity extends MTFBaseActivity {
    private CLVSearchController controller;
    private ImageView icHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clv_search);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loadView();
        initiateDefaultValue();

    }

    private void loadView() {
        icHome = (ImageView)findViewById(R.id.icHome);

    }

    private void initiateDefaultValue() {
        controller = new CLVSearchController(this);
        icHome.setOnClickListener(controller);
    }
}
