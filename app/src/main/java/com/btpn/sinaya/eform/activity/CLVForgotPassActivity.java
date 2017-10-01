package com.btpn.sinaya.eform.activity;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.btpn.sinaya.eform.MTFBaseActivity;
import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.activity.controller.CLVForgotPassController;
import com.btpn.sinaya.eform.activity.controller.CLVLoginController;
import com.btpn.sinaya.eform.utils.MTFConstants;
import com.btpn.sinaya.eform.utils.MTFPermissionUtil;

/**
 * Created by vaniawidjaja on 9/21/17.
 */

public class CLVForgotPassActivity extends MTFBaseActivity {
    private EditText userNameEditText;
    private Button loginButton;
    private Button sendButton;
    private CLVForgotPassController controller;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
            checkFieldsForEmptyValues();
        }
    };

    void checkFieldsForEmptyValues(){

        String s1 = userNameEditText.getText().toString();

        if(s1.equals("")){
            sendButton.setEnabled(false);
            sendButton.setBackground(getDrawable(R.drawable.button_gray_background));
        } else {
            sendButton.setEnabled(true);
            sendButton.setBackground(getDrawable(R.drawable.button_orange_background));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clv_forgotpass);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loadView();
        initiateDefaultValue();

    }

    private void loadView() {
        userNameEditText = (EditText) findViewById(R.id.login_username_edittext);
        loginButton = (Button) findViewById(R.id.login_button);
        sendButton= (Button) findViewById(R.id.send_button);
    }

    private void initiateDefaultValue() {
        controller = new CLVForgotPassController(this);
        loginButton.setOnClickListener(controller);
        sendButton.setOnClickListener(controller);
        userNameEditText.addTextChangedListener(mTextWatcher);
        checkFieldsForEmptyValues();
    }


    public void loginWithPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//            controller.submitLogin();
        } else {
            MTFPermissionUtil.requestPermission(this, Manifest.permission.READ_PHONE_STATE, MTFConstants.MTF_PERMISSION_REQUEST_LOGIN,
                    "Aplikasi E-Form membutuhkan permission 'PHONE' untuk membaca IMEI");

        }
    }
}