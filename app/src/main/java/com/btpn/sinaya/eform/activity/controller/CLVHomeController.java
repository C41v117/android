package com.btpn.sinaya.eform.activity.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;

import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.activity.CLVForgotPassActivity;
import com.btpn.sinaya.eform.activity.CLVHomeActivity;
import com.btpn.sinaya.eform.activity.CLVLoginActivity;
import com.btpn.sinaya.eform.activity.CLVSearchActivity;
import com.btpn.sinaya.eform.activity.CLVSignUpActivity;
import com.btpn.sinaya.eform.alert.listener.MTFRootedAlertDialogListener;
import com.btpn.sinaya.eform.connection.MTFSchedulerManager;
import com.btpn.sinaya.eform.connection.api.listener.MTFServiceConnection;
import com.btpn.sinaya.eform.database.MTFDatabaseHelper;
import com.btpn.sinaya.eform.model.MTFUserModel;
import com.btpn.sinaya.eform.utils.MTFIntentConstant;
import com.btpn.sinaya.eform.utils.MTFSystemParams;

/**
 * Created by vaniawidjaja on 9/23/17.
 */

public class CLVHomeController implements View.OnClickListener, View.OnKeyListener {
    private int codeFailed;
    private CLVHomeActivity activity;

//    private BroadcastReceiver localBroadCastReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            if (!intent.getCategories().contains(MTFIntentConstant.CATEGORY_LOGIN)) {
//                return;
//            }
//
//            Bundle bundle = intent.getExtras();
//            if (bundle == null) {
//                return;
//            }
//
//            int code = bundle.getInt(MTFIntentConstant.BUNDLE_KEY_CODE);
//            String message = bundle.getString(MTFIntentConstant.BUNDLE_KEY_MESSAGE);
//            String action = intent.getAction();
//            if (action.equals(MTFIntentConstant.ACTION_LOGIN_NORMAL)) {
//                switch (code) {
//                    case MTFIntentConstant.CODE_LOGIN_SUCCESS:
//                        updateUserModel(intent);
//                        checkMasterDataVersion();
//                        break;
//                    case MTFIntentConstant.CODE_LOGIN_FAILED_DIFF_VER:
//                        activity.showExitDialog();
//                        break;
//                    case MTFIntentConstant.CODE_LOGIN_SUCCESS_OFFLINE:
//                        userModel = database.getActiveSession();
//                        userModel.setOnline(false);
//                        database.updateOnlineStatus(userModel);
//                        loadSystemParams();
//                        break;
//                    default:
//                        activity.dismisLoadingDialog();
//                        if(message!=null && !message.trim().equals("null")){
//                            activity.showAlertDialog("Error", message.trim());
//                        }else{
//                            activity.showAlertDialog("Error", "Gagal Login");
//                        }
//                        break;
//                }
//            }else if (action.equals(MTFIntentConstant.ACTION_MASTER_DATA)){
//                if(code == MTFIntentConstant.CODE_FAILED || code == MTFIntentConstant.CODE_MASTER_DATA_FAILED){
//                    activity.dismisLoadingDialog();
//                    activity.showAlertDialog("Error", "Gagal meminta Master Data");
//                }else if (code == MTFIntentConstant.CODE_MASTER_DATA_SUCCESS){
//                    Intent nextIntent = new Intent(activity, MTFServiceConnection.class);
//                    nextIntent.addCategory(MTFIntentConstant.CATEGORY_LOGIN);
//                    activity.dismisLoadingDialog();
//                    loadSystemParams();
//                }
//            }else if (action.equals(MTFIntentConstant.ACTION_CUSTOMER_LIST) || action.equals(MTFIntentConstant.ACTION_CUSTOMER_LIST_PURNA)
//                    || action.equals(MTFIntentConstant.ACTION_CUSTOMER_LIST_VERIFY)){
//                if(code == MTFIntentConstant.CODE_FAILED || code == MTFIntentConstant.CODE_FAILED_CUSTOMER_LIST){
//                    activity.dismisLoadingDialog();
//                    activity.showAlertDialog("Error", "Gagal meminta Master Data");
//                }else if (code == MTFIntentConstant.CODE_SUCCESS_CUSTOMER_LIST || code == MTFIntentConstant.CODE_SUCCESS_CUSTOMER_LIST_PURNA
//                        || code == MTFIntentConstant.CODE_SUCCESS_CUSTOMER_LIST_VERIFY){
//                    activity.dismisLoadingDialog();
//                    loadSystemParams();
//                }else{
//                    activity.dismisLoadingDialog();
//                    activity.showAlertDialog("Error", "Gagal meminta Master Data");
//                }
//            }else if (action.equals(MTFIntentConstant.ACTION_SEND_PROFILE)){
//                activity.dismisLoadingDialog();
//                if(code == MTFIntentConstant.CODE_FAILED || code == MTFIntentConstant.CODE_SENDER_ID_FAILED){
//                    activity.dismisLoadingDialog();
//                    activity.showAlertDialog("Error", "Gagal Login");
//                }else if (code == MTFIntentConstant.CODE_SENDER_ID_SUCCESS){
//                    activity.showLoadingDialog(activity.getResources().getString(R.string.regis_notif));
//                    String smpRegId = bundle.getString(MTFIntentConstant.BUNDLE_KEY_REG_ID);
//                    String senderId = bundle.getString(MTFIntentConstant.BUNDLE_KEY_SENDER_ID);
//                }
//            }else if (action.equals(MTFIntentConstant.ACTION_SEND_REGID)){
//                activity.dismisLoadingDialog();
//                if(code == MTFIntentConstant.CODE_FAILED || code == MTFIntentConstant.CODE_FAILED_SEND_REGID){
//                    activity.dismisLoadingDialog();
//                    activity.showAlertDialog("Error", "Gagal Login");
//                }else if (code == MTFIntentConstant.CODE_SUCCESS_SEND_REGID){
//                    startDashboardActivity();
//                }
//            }
//        }
//    };

    private TelephonyManager telephonyManager;
    private MTFUserModel userModel;
    private MTFDatabaseHelper database;

    public CLVHomeController(CLVHomeActivity activity) {
        this.activity = activity;
        this.database = new MTFDatabaseHelper(activity);
        this.codeFailed = activity.getIntent().getIntExtra(MTFIntentConstant.INTENT_EXTRA_CODE, MTFIntentConstant.CODE_FAILED);
        this.telephonyManager = (TelephonyManager)activity.getSystemService(Context.TELEPHONY_SERVICE);
        this.userModel = new MTFUserModel();

        registerLocalBroadCastReceiver();
    }

    public void registerLocalBroadCastReceiver(){
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(MTFIntentConstant.ACTION_LOGIN_NORMAL);
//        intentFilter.addAction(MTFIntentConstant.ACTION_MASTER_DATA);
//        intentFilter.addAction(MTFIntentConstant.ACTION_CUSTOMER_LIST);
//        intentFilter.addAction(MTFIntentConstant.ACTION_CUSTOMER_LIST_PURNA);
//        intentFilter.addAction(MTFIntentConstant.ACTION_CUSTOMER_LIST_VERIFY);
//        intentFilter.addAction(MTFIntentConstant.ACTION_SEND_PROFILE);
//        intentFilter.addAction(MTFIntentConstant.ACTION_SEND_REGID);
//        intentFilter.addCategory(MTFIntentConstant.CATEGORY_LOGIN);
//        intentFilter.addCategory(MTFIntentConstant.CATEGORY_CUSTOMER_LIST);
//        intentFilter.addCategory(MTFIntentConstant.CATEGORY_CUSTOMER_LIST_PURNA);
//        intentFilter.addCategory(MTFIntentConstant.CATEGORY_CUSTOMER_LIST_VERIFY);
//        LocalBroadcastManager.getInstance(activity.getApplicationContext()).registerReceiver(localBroadCastReceiver, intentFilter);
    }

//    public void unregisterLocalBroadCastReceiver(){
//        LocalBroadcastManager.getInstance(activity.getApplicationContext()).unregisterReceiver(localBroadCastReceiver);
//    }
//
//    public boolean onBackPressed(){
//        if (codeFailed == MTFIntentConstant.CODE_FAILED_TOKEN) {
//            activity.showExitConfirmationDialog();
//            return false;
//        }
//
//        return true;
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icSearch:
//                if(MTFConstants.UIOnly)
//                    this.loginUIOnly();
//                else{
//                    activity.loginWithPermission();
//                }

                startSearchActivity();

                break;
            case R.id.signUpTV:
//            case R.id.LLsignup:
//                if(MTFConstants.UIOnly)
//                    this.loginUIOnly();
//                else{
                    startDashboardActivity();
//                }

                break;
            case R.id.forgot_pass:
//            case R.id.LLsignup:
//                if(MTFConstants.UIOnly)
//                    this.loginUIOnly();
//                else{
//                }

                break;

            default:
                break;
        }
    }

    private void startSearchActivity() {

        Intent intentHome = new Intent(activity, CLVSearchActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivity(intentHome);
        activity.finish();
    }


    public void submitLogin(){
//        String agentId = activity.getUserNameEditText().getText().toString().trim().toUpperCase();
//        String password = activity.getPasswordEditText().getText().toString().trim();
//        String IMEI = null;
//        if (telephonyManager != null){
//            IMEI = telephonyManager.getDeviceId();
//        }
//        if(IMEI == null){
//            IMEI = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
//            if(IMEI == null){
//                IMEI = telephonyManager.getSubscriberId();
//            }
//        }
//
//        if (agentId == null || agentId.equals("")) {
//            activity.showAlertDialog("Gagal", "User ID dan Password harus diisi");
//            return;
//        }
//
//        if (password == null || password.equals("")) {
//            activity.showAlertDialog("Gagal", "User ID dan Password harus diisi");
//            return;
//        }
//
//        activity.hideKeyBoard();
//
//        userModel.setUserName(agentId);
//        userModel.setPin(password);
//        userModel.setImei(IMEI);
//        userModel.setOnline(true);
//
//        activity.showLoadingDialog(activity.getResources().getString(R.string.loading));
//
//        //REQUEST LOGIN
//        Intent intent = new Intent(activity, MTFServiceConnection.class);
//        intent.setAction(MTFIntentConstant.ACTION_LOGIN_NORMAL);
//        intent.addCategory(MTFIntentConstant.CATEGORY_LOGIN);
//        intent.putExtra(MTFIntentConstant.INTENT_EXTRA_DATA, userModel);
//        activity.startService(intent);
    }

    private Boolean checkOSVersion(){
        boolean flag = false;
        Integer check = 422;
        Integer os = Integer.parseInt(Build.VERSION.RELEASE.replace(".",""));

        if (os <= check) {
            flag = true;
        }

        return flag;
    }

    public void quit() {
//        activity.showExitVersionDialog();
    }

    private void requestSenderId(){
        activity.showLoadingDialog(activity.getResources().getString(R.string.loading));

        //REQUEST SENDER ID
        Intent intent = new Intent(activity, MTFServiceConnection.class);
        intent.setAction(MTFIntentConstant.ACTION_SEND_PROFILE);
        intent.addCategory(MTFIntentConstant.CATEGORY_LOGIN);
        activity.startService(intent);

    }

    private void startDashboardActivity(){
//        unregisterLocalBroadCastReceiver();
//        MTFApplicationContext application = (MTFApplicationContext)activity.getApplicationContext();
//        application.setTitleResourceId(0);
//        application.setPosition(0);
//        ActivityCompat.finishAffinity(activity);
//
//        if(database.getActiveSession().isOnline()){
//            startScheduler();
//        }

//		Intent intentDashboard = new Intent(activity, MTFHomeActivity.class);
//        intentDashboard.putExtra("FIRST_LOGIN", 1);
//		activity.startActivity(intentDashboard);
        Intent intentSignUp = new Intent(activity, CLVSignUpActivity.class);
        activity.startActivity(intentSignUp);
    }

    private void startScheduler(){
        MTFSchedulerManager schedulerManager = MTFSchedulerManager.getInstance(activity);
        schedulerManager.cancelAlarm();
        schedulerManager.initialize(MTFSystemParams.schedulerStart, MTFSystemParams.schedulerInterval);
    }

    private void updateUserModel(Intent intent){
//        Bundle bundle = intent.getExtras();
//        String token = bundle.getString(MTFIntentConstant.BUNDLE_KEY_TOKEN);
//        String userId = bundle.getString(MTFIntentConstant.BUNDLE_KEY_USERID);
//        String username = bundle.getString(MTFIntentConstant.BUNDLE_KEY_USERNAME);
//        String agentType = bundle.getString(MTFIntentConstant.BUNDLE_KEY_AGENT_TYPE);
//        String locationName = bundle.getString(MTFIntentConstant.BUNDLE_KEY_LOCATION);
//        String locationId = bundle.getString(MTFIntentConstant.BUNDLE_KEY_LOCATION_ID);
//        String json = bundle.getString(MTFIntentConstant.BUNDLE_KEY_JSON);
//        String secret = bundle.getString(MTFIntentConstant.BUNDLE_KEY_SECRET);
//        if(!json.equals("") && !json.equals("null")){
//            Gson gson = new Gson();
//            MTFLoginModel loginModel = gson.fromJson(json, MTFLoginModel.class);
//            if(loginModel != null){
//                if(loginModel.getLob() != null)
//                    userModel.setLob(loginModel.getLob());
//                if(loginModel.getDivision() != null)
//                    userModel.setDivision(loginModel.getDivision());
//                if(loginModel.getRaCode() != null)
//                    userModel.setRaCode(loginModel.getRaCode());
//                if(loginModel.getRoCode() != null)
//                    userModel.setRoCode(loginModel.getRoCode());
//            }
//        }else{
//            userModel.setLob(MTFConstants.SINAYA_LOB);
//        }
//
//        if (token != null) {
//            userModel.setToken(token);
//        }
//
//        if (username != null) {
////			userModel.setUserId(username);
//            userModel.setUserId(userId);
//            //username jgn pernah di update dari balikan server karena balikan username dari server adalah fullname
////            userModel.setUserName(username);
//        }
//
//        if (agentType != null) {
//            userModel.setAgentType(agentType);
//        }
//
//        if (locationName != null) {
//            userModel.setLocationName(locationName);
//        }
//
//        if (locationId != null) {
//            userModel.setLocationId(Long.parseLong(locationId));
//        }
//
//        if(secret != null){
//            userModel.setSecret(secret);
//        }
//
//        userModel.setJwt(MTFSMPUtilities.JWT);
//        MTFSMPUtilities.JWT = "";
//
//        MTFUserModel modelFromDatabase = database.getSession(userModel.getUserName());
//        userModel.setLastLogin(new Date());
//        userModel.setOnline(true);
//        if (modelFromDatabase != null) {
//            userModel.setId(modelFromDatabase.getId());
//            database.updateSession(userModel);
//        }else{
//            database.deleteSession();
//            database.insertSession(userModel);
//        }
//
//        if (username != null) {
//            MTFProfileModel profileModel = new MTFProfileModel();
//            profileModel.setUserId(Long.parseLong(userModel.getUserId()));
//            profileModel.setAgentName(username);
//            profileModel.setAgentPhone(username);
//        }

    }

    private void loginUIOnly(){
//        database.deleteSession();
//
//        userModel.setLob(MTFConstants.PUR_LOB);
//        userModel.setUserName("PB17");
//        userModel.setToken("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJQQjE3IiwiZXhwIjoxNDk0NjQzNDczfQ.uwQReQka8BkQjshzdNhZDjYCMqf_ugvrXbjMYOIFTYA");
//        userModel.setJwt("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJQQjE3IiwiZXhwIjoxNDk0NjQzNDczfQ.uwQReQka8BkQjshzdNhZDjYCMqf_ugvrXbjMYOIFTYA");
//        userModel.setSecret("2fc06110-fd5e-4156-a52e-f560fadf65cb");
//        userModel.setUserId("628");
//        userModel.setLastLogin(new Date());
//        userModel.setAgentType("PB");
//        userModel.setLocationName("Jakarta");
//        userModel.setLocationId(0002L);
//        userModel.setOnline(true);
//        userModel.setRaCode("RA");
//        userModel.setRoCode("RO");
//
//        database.insertSession(userModel);
//        startDashboardActivity();
    }

    public void checkMasterDataVersion() {
//        this.activity.showLoadingDialog(activity.getResources().getString(R.string.sinkron_master_data));
//        Intent intent = new Intent(activity, MTFServiceConnection.class);
//        intent.setAction(MTFIntentConstant.ACTION_MASTER_DATA);
//        intent.addCategory(MTFIntentConstant.CATEGORY_LOGIN);
//        intent.putExtra(MTFIntentConstant.INTENT_EXTRA_PHONE_NUMBER, userModel.getUserName());
//        intent.putExtra(MTFIntentConstant.INTENT_EXTRA_TOKEN, userModel.getToken());
//        activity.startService(intent);
    }

    private void loadSystemParams(){
//        AsyncTask<Void, Void, Void> executeLoader = new AsyncTask<Void, Void, Void>() {
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                activity.showLoadingDialog(activity.getResources().getString(R.string.loading));
//            }
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                MTFSystemParams.reloadSystemParamsFromDatabase(database);
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void result) {
//                super.onPostExecute(result);
//                activity.dismisLoadingDialog();
//                if(userModel.isOnline()){
//                    startDashboardActivity();
//                }else{
//                    activity.showisOfflineDialog(activity.getResources().getString(R.string.login_offline_alert));
//                }
//            }
//        };
//
//        executeLoader.execute();
    }

    public void onDestory(){
//        unregisterLocalBroadCastReceiver();
//        database.close();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        boolean result = false;
//        if(keyCode == KeyEvent.KEYCODE_ENTER){
//            if(event.getAction()==KeyEvent.ACTION_DOWN){
//                result = true;
//                submitLogin();
//            }
//        }
//
        return result;
    }

    public void exit() {
        activity.showRootedDialog(activity.getResources().getString(R.string.deviceIsRoot), new MTFRootedAlertDialogListener() {
            @Override
            public void onOk() {
                activity.finish();
            }

        });
    }

    public MTFUserModel getActiveSession(){
        return database.getActiveSession();
    }

    public void updateSession(MTFUserModel user){
        database.updateOnlineStatus(user);
    }
}
