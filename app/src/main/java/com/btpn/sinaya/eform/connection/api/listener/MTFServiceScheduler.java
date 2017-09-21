package com.btpn.sinaya.eform.connection.api.listener;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.activity.MTFLoginActivity;
import com.btpn.sinaya.eform.connection.MTFSMPUtilities;
import com.btpn.sinaya.eform.connection.MTFSchedulerManager;
import com.btpn.sinaya.eform.database.MTFDatabaseHelper;
import com.btpn.sinaya.eform.model.MTFUserModel;
import com.btpn.sinaya.eform.preferences.MTFSharedPreference;
import com.btpn.sinaya.eform.utils.MTFAES;
import com.btpn.sinaya.eform.utils.MTFFileLogger;
import com.btpn.sinaya.eform.utils.MTFIntentConstant;
import com.btpn.sinaya.eform.utils.MTFJSONKey;
import com.btpn.sinaya.eform.utils.MTFMessageBroadcaster;
import com.btpn.sinaya.eform.utils.MTFSystemParams;

import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vaniawidjaja on 2/13/17.
 */


public class MTFServiceScheduler extends Service {

    private MTFDatabaseHelper database;

    public static final String SCHEDULER_LOGGER_TAG = "eform_scheduler";

    public static final String SCHEDULER_RUNNING_MESSAGE = "Eform sedang mengirim data";
    public static final String SCHEDULER_TOKEN_ERROR = "Please re-login";

    private static final int maxRetry = 3;
    private int retry = 0;
    private long mLastInterrupt = 0;

    private void showNotification(){
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo_bptn)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText("Please Re-Login")
                        .setAutoCancel(true);
        int NOTIFICATION_ID = 12345;

        Intent targetIntent = new Intent(this, MTFLoginActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.build();
        notification.defaults|= Notification.DEFAULT_SOUND;
        notification.defaults|= Notification.DEFAULT_LIGHTS;
        notification.defaults|= Notification.DEFAULT_VIBRATE;
        nManager.notify(NOTIFICATION_ID, notification);
    }

    private BroadcastReceiver localBroadCastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int code = bundle.getInt(MTFIntentConstant.BUNDLE_KEY_CODE, MTFIntentConstant.CODE_FAILED);
            boolean isScheduler = bundle.getBoolean(MTFIntentConstant.BUNDLE_KEY_SCHEDULER, false);
            String action = intent.getAction();
            Log.d("EFORM_ALARM", "OnReceive: "+code);
            if(isScheduler){
                Log.d("EFORM_ALARM", "OnReceive (Scheduler): "+code);
            }
        }
    };

    private BroadcastReceiver getCustomerBroadCastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
                if (action.equals(MTFIntentConstant.ACTION_CUSTOMER_LIST) && MTFSharedPreference.isSchedulerSending(getApplicationContext())){
                int code = intent.getExtras().getInt(MTFIntentConstant.BUNDLE_KEY_CODE);
                String message = intent.getExtras().getString(MTFIntentConstant.BUNDLE_KEY_MESSAGE);
                MTFFileLogger.getInstance().addString(SCHEDULER_LOGGER_TAG, "Receive scheduler Customer List: "+code+", Message" + message);
            }else if (action.equals(MTFIntentConstant.ACTION_MASTER_DATA) && MTFSharedPreference.isSchedulerSending(getApplicationContext())){
                int code = intent.getExtras().getInt(MTFIntentConstant.BUNDLE_KEY_CODE);
                if (code == MTFIntentConstant.CODE_MASTER_DATA_SUCCESS){
                    MTFSystemParams.reloadSystemParamsFromDatabase(database);
                    MTFSchedulerManager.getInstance(getApplicationContext()).cancelAlarm();
                    MTFSchedulerManager.getInstance(getApplicationContext()).initialize(MTFSystemParams.schedulerStart, MTFSystemParams.schedulerInterval);
                }
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        System.loadLibrary("stlport_shared");
        SQLiteDatabase.loadLibs(getApplicationContext());

        database = MTFDatabaseHelper.getInstance(getBaseContext());

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MTFIntentConstant.ACTION_NEW_CUSTOMER);
        intentFilter.addAction(MTFIntentConstant.ACTION_UPDATE_CUSTOMER);
        intentFilter.addAction(MTFIntentConstant.ACTION_NEW_CUSTOMER_PURNA);
        intentFilter.addAction(MTFIntentConstant.ACTION_UPDATE_CUSTOMER_PURNA);
        intentFilter.addCategory(MTFIntentConstant.CATEGORY_REGISTER_CUSTOMER);
        intentFilter.addCategory(MTFIntentConstant.CATEGORY_CUSTOMER_LIST);
        intentFilter.addCategory(MTFIntentConstant.CATEGORY_CUSTOMER_LIST_PURNA);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(localBroadCastReceiver, intentFilter);

        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(MTFIntentConstant.ACTION_MASTER_DATA);
        intentFilter2.addCategory(MTFIntentConstant.CATEGORY_LOGIN);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(getCustomerBroadCastReceiver, intentFilter2);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent==null)
            return super.onStartCommand(intent, flags, startId);

        if (intent.getAction().equals(MTFIntentConstant.ACTION_SEND_DATA_SCHEDULER)) {
            MTFFileLogger.getInstance().addString(SCHEDULER_LOGGER_TAG, "Start Service Send Data Scheduler");
            if(intent.getBooleanExtra(MTFIntentConstant.BUNDLE_KEY_IS_MASTER_DATA, false)){
                requestMasterData();
            }else{
                finishScheduler();
            }
        }else if(intent.getAction().equals(MTFIntentConstant.ACTION_INTERRUPT_SCHEDULER)) {
            MTFFileLogger.getInstance().addString(SCHEDULER_LOGGER_TAG, "Interrupt from API");
            broadcastInterruption();
//        }else if(intent.getAction().equals(MTFIntentConstant.ACTION_NEED_LOGIN_SCHEDULER)) {
//            showNotification();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void requestMasterData(){
        MTFMessageBroadcaster.broadcastMessage(getApplicationContext(), "Mengambil master data");
        MTFFileLogger.getInstance().addString(SCHEDULER_LOGGER_TAG, "Mengambil master data");
        MTFSharedPreference.setSchedulerSending(getApplicationContext(), true);
        String apkVersion = "";

        MTFUserModel userModel =  database.getActiveSession();
        String token = userModel.getToken();
        String userId = userModel.getUserId();
        String userPhoneNumber = userModel.getUserName();
        JSONArray jsonArrayVersion = database.getAllVersionOfMasterData();

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            apkVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }

        JSONObject jsonToSend = new JSONObject();
        try {
            jsonToSend.put(MTFJSONKey.KEY_APK_VERSION, apkVersion);
        } catch (JSONException e) {

        }

        String phoneNumber = userModel.getUserName();
        String password = userModel.getPin();

        try {
            MTFAES.generateKey();
            phoneNumber = MTFAES.encryptString(phoneNumber);
            password = MTFAES.encryptString(password);
        } catch (Exception e) {

        }

        MTFSMPUtilities.username = phoneNumber;
        MTFSMPUtilities.password = password;

//        MTFAPIMasterData requestMasterData = new MTFAPIMasterData(getApplicationContext(), new MTFAPIMasterDataListener());
//        requestMasterData.setData(userPhoneNumber, userId, token, jsonArrayVersion, jsonToSend);
//        requestMasterData.execute();
    }

    private void broadcastInterruption(){
        if(SystemClock.elapsedRealtime() - mLastInterrupt > 2000){
            mLastInterrupt = SystemClock.elapsedRealtime();
            MTFFileLogger.getInstance().addString(SCHEDULER_LOGGER_TAG, "Interrupt active");
            if(MTFSharedPreference.isInterruptActive(getApplicationContext())){
                finishScheduler();

                Intent intent = new Intent();
                intent.setAction(MTFIntentConstant.ACTION_INTERRUPT);
                intent.addCategory(MTFIntentConstant.CATEGORY_INTERRUPT);
                sendBroadcast(intent);
            }
        }

    }

    private void finishScheduler(){
        MTFFileLogger.getInstance().addString(SCHEDULER_LOGGER_TAG, "Scheduler Finish");
        MTFFileLogger.getInstance().writeAndReset(SCHEDULER_LOGGER_TAG);
        MTFSharedPreference.setSchedulerSending(getApplicationContext(), false);
        MTFSharedPreference.resetCounter(getApplicationContext());
        MTFMessageBroadcaster.broadcastMessage(getApplicationContext(), MTFMessageBroadcaster.MESSAGE_FINISH);
    }
}