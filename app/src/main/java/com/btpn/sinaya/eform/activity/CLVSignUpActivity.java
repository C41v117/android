package com.btpn.sinaya.eform.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.btpn.sinaya.eform.MTFBaseActivity;
import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.activity.controller.CLVLoginController;
import com.btpn.sinaya.eform.activity.controller.CLVSignUpController;

/**
 * Created by vaniawidjaja on 9/23/17.
 */

public class CLVSignUpActivity extends MTFBaseActivity{

    private EditText userNameEditText;
    private EditText passwordEditText;
    private EditText retypePassEditText;
    private EditText emailEditText;
    private Button signUpButton;
    private TextView signInTextView;
    private CLVSignUpController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clv_sign_up);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loadView();
        initiateDefaultValue();

    }
    private void loadView() {
        userNameEditText = (EditText) findViewById(R.id.login_username_edittext);
        passwordEditText = (EditText) findViewById(R.id.login_password_edittext);
        signUpButton = (Button) findViewById(R.id.signup_button);
        signInTextView = (TextView) findViewById(R.id.signInTV);
    }

    private void initiateDefaultValue() {
        controller = new CLVSignUpController(this);
        signUpButton.setOnClickListener(controller);
        signInTextView.setOnClickListener(controller);
    }
}
