package com.btpn.sinaya.eform.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ImageView;

import com.btpn.sinaya.eform.MTFBaseActivity;
import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.activity.controller.CLVHomeController;

/**
 * Created by vaniawidjaja on 10/8/17.
 */

public class CLVHomeActivity extends MTFBaseActivity {
    private CLVHomeController controller;
    private ImageView icSearch;

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

    }

    private void initiateDefaultValue() {
        controller = new CLVHomeController(this);
        icSearch.setOnClickListener(controller);
    }
}
