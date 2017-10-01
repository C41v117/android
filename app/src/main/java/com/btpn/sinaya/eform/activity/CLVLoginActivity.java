package com.btpn.sinaya.eform.activity;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.btpn.sinaya.eform.MTFBaseActivity;
import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.activity.controller.CLVLoginController;
import com.btpn.sinaya.eform.activity.controller.MTFLoginController;
import com.btpn.sinaya.eform.utils.MTFConstants;
import com.btpn.sinaya.eform.utils.MTFPermissionUtil;

/**
 * Created by vaniawidjaja on 9/21/17.
 */

public class CLVLoginActivity extends MTFBaseActivity {
    private EditText userNameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signUpTextView;
    private TextView forgotTextView;
    private CLVLoginController controller;

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
        String s2 = passwordEditText.getText().toString();

        if(s1.equals("")|| s2.equals("")){
            loginButton.setEnabled(false);
            loginButton.setBackground(getDrawable(R.drawable.button_gray_background));
        } else {
            loginButton.setEnabled(true);
            loginButton.setBackground(getDrawable(R.drawable.button_orange_background));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clv_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loadView();
        initiateDefaultValue();

    }

    private void loadView() {
        userNameEditText = (EditText) findViewById(R.id.login_username_edittext);
        passwordEditText = (EditText) findViewById(R.id.login_password_edittext);
        loginButton = (Button) findViewById(R.id.login_button);
        signUpTextView = (TextView) findViewById(R.id.signUpTV);
        forgotTextView = (TextView) findViewById(R.id.forgot_pass);
    }

    private void initiateDefaultValue() {
        controller = new CLVLoginController(this);
        loginButton.setOnClickListener(controller);
        signUpTextView.setOnClickListener(controller);
        forgotTextView.setOnClickListener(controller);
        userNameEditText.addTextChangedListener(mTextWatcher);
        passwordEditText.addTextChangedListener(mTextWatcher);
        checkFieldsForEmptyValues();
    }


    public void loginWithPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            controller.submitLogin();
        } else {
            MTFPermissionUtil.requestPermission(this, Manifest.permission.READ_PHONE_STATE, MTFConstants.MTF_PERMISSION_REQUEST_LOGIN,
                    "Aplikasi E-Form membutuhkan permission 'PHONE' untuk membaca IMEI");

        }
    }
}