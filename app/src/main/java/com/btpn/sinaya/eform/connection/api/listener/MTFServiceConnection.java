package com.btpn.sinaya.eform.connection.api.listener;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.btpn.sinaya.eform.connection.MTFSMPUtilities;
import com.btpn.sinaya.eform.connection.api.MTFAPILogin;
import com.btpn.sinaya.eform.connection.api.MTFAPILogout;
import com.btpn.sinaya.eform.connection.api.listener.impl.MTFAPILoginListener;
import com.btpn.sinaya.eform.connection.api.listener.impl.MTFAPILogoutListener;
import com.btpn.sinaya.eform.database.MTFDatabaseHelper;
import com.btpn.sinaya.eform.model.MTFUserModel;
import com.btpn.sinaya.eform.preferences.MTFSharedPreference;
import com.btpn.sinaya.eform.utils.MTFAES;
import com.btpn.sinaya.eform.utils.MTFConstants;
import com.btpn.sinaya.eform.utils.MTFGenerator;
import com.btpn.sinaya.eform.utils.MTFIntentConstant;
import com.btpn.sinaya.eform.utils.MTFJSONKey;
import com.btpn.sinaya.eform.utils.MTFSystemParams;

import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MTFServiceConnection extends Service{

	private MTFDatabaseHelper database;
	private boolean isServiceStart = false;
	private List<String> listPhoneNumberCustomerForAction = new ArrayList<String>();
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			int code = intent.getExtras().getInt(MTFIntentConstant.BUNDLE_KEY_CODE, MTFIntentConstant.CODE_FAILED);
			String phoneNumber = intent.getExtras().getString(MTFIntentConstant.BUNDLE_KEY_PHONE_NUMBER);
			
			if (phoneNumber != null && !phoneNumber.equals("")) {
				if(code == MTFIntentConstant.CODE_FAILED 
						|| code == MTFIntentConstant.CODE_FAILED_SEND_DATA
						|| code == MTFIntentConstant.CODE_FAILED_SEND_FILE
						|| code == MTFIntentConstant.CODE_SUCCESS_SEND_ALL_DATA
						|| code == MTFIntentConstant.CODE_FAILED_TOKEN){
					
					listPhoneNumberCustomerForAction.remove(phoneNumber);
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
		
		isServiceStart = true;
		database = new MTFDatabaseHelper(getApplicationContext());
		startTimeCounterForClearData();
		registerLocalBroadcastReceiver();
	}
	
	@Override
	public void onDestroy() {
		isServiceStart = false;
		database.close();
		unregisterLocalBroadcastReceiver();
		super.onDestroy();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		if(intent==null)
			return super.onStartCommand(intent, flags, startId);
		
		if (intent.getAction().equals(MTFIntentConstant.ACTION_LOGIN_NORMAL)) {
			requestLogin(intent);
		}else if (intent.getAction().equals(MTFIntentConstant.ACTION_MASTER_DATA)) {
			requestMasterData(intent);
		}else if(intent.getAction().equals(MTFIntentConstant.ACTION_NOTIFICATION)){
			requestNotification(intent);
		}else if (intent.getAction().equals(MTFIntentConstant.ACTION_CLEAR_DATA)) {
			clearData();
		}else if (intent.getAction().equals(MTFIntentConstant.ACTION_SHOW_REMINDER)) {
			requestShowReminder();
		}
		
		return super.onStartCommand(intent, flags, startId);
	}

	private void requestLogin(Intent intent){
		MTFUserModel userModel = (MTFUserModel)intent.getSerializableExtra(MTFIntentConstant.INTENT_EXTRA_DATA);
		
		String phoneNumber = "";
		String password = "";

		try {
			MTFAES.generateKey();
			phoneNumber = MTFAES.encryptString(userModel.getUserName());
			password = MTFAES.encryptString(userModel.getPin());
			userModel.setUserName(phoneNumber);
			userModel.setPin(password);
		} catch (Exception e) {
		}
		
		MTFSMPUtilities.username = phoneNumber;
		MTFSMPUtilities.password = password;
		
		//Check Connection
		if (!MTFGenerator.isConnectionAvailable(getApplicationContext())) {
			//Login Offline
			try {
				phoneNumber = MTFAES.decryptString(phoneNumber);
				password 	= MTFAES.decryptString(password);
			} catch (Exception e) {
				
			}
			
			MTFUserModel modelFromDb = database.getSession(phoneNumber);
			Bundle bundleForBroadcase = new Bundle();
			
			Intent intentForBroadcase = new Intent(MTFIntentConstant.ACTION_LOGIN_NORMAL);
			intentForBroadcase.addCategory(MTFIntentConstant.CATEGORY_LOGIN);
			
			if (modelFromDb != null
					&& modelFromDb.getUserName().equalsIgnoreCase(phoneNumber)
					&& MTFGenerator.generateHash(password).equals(modelFromDb.getPin()) 
					&& MTFGenerator.isGreaterThanToday(modelFromDb.getLastLogin(), MTFSystemParams.maxOfflineLoginDays)) {
				if(modelFromDb.getLob().equals(MTFConstants.SINAYA_LOB)){
					if(modelFromDb.getAgentType().equals("PB")) {
						bundleForBroadcase.putInt(MTFIntentConstant.BUNDLE_KEY_CODE, MTFIntentConstant.CODE_LOGIN_SUCCESS_OFFLINE);
						bundleForBroadcase.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, "Login Offline is Allowed");
					}else{
						bundleForBroadcase.putInt(MTFIntentConstant.BUNDLE_KEY_CODE, MTFIntentConstant.CODE_LOGIN_FAILED_INVALID);
						bundleForBroadcase.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, "Tidak dapat terhubung ke E-Saving Web");
					}
				}else{
					bundleForBroadcase.putInt(MTFIntentConstant.BUNDLE_KEY_CODE, MTFIntentConstant.CODE_LOGIN_SUCCESS_OFFLINE);
					bundleForBroadcase.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, "Login Offline is Allowed");
				}
			}else{
				bundleForBroadcase.putInt(MTFIntentConstant.BUNDLE_KEY_CODE, MTFIntentConstant.CODE_LOGIN_FAILED_INVALID);
				bundleForBroadcase.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, "Tidak dapat terhubung ke E-Saving Web");
			}

			intentForBroadcase.putExtras(bundleForBroadcase);
			LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intentForBroadcase);
			
			return;
		}

        MTFAPILogin requestLogin = new MTFAPILogin(getApplicationContext(), new MTFAPILoginListener());
        requestLogin.setData(phoneNumber, userModel);
        requestLogin.execute();
	}
	
	
	private void requestMasterData(Intent intent){
        String apkVersion = "";

		MTFUserModel userModel =  database.getActiveSession();
		String token = intent.getStringExtra(MTFIntentConstant.INTENT_EXTRA_TOKEN);
		String userName = userModel.getUserName();
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
            jsonToSend.put(MTFJSONKey.KEY_APP_ID, MTFConstants.E_SAVING);
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
		
//		MTFAPIMasterData requestMasterData = new MTFAPIMasterData(getApplicationContext(), new MTFAPIMasterDataListener());
//		requestMasterData.setData(userPhoneNumber, userName, token, jsonArrayVersion, jsonToSend);
//		requestMasterData.execute();
		
	}
	
	private void requestNotification(Intent intent){
//		MTFUserModel userModel =  database.getActiveSession();
//		String userId = userModel.getUserId();
//		String token = userModel.getToken();
//		String userPhoneNumber = userModel.getUserName();
		
//		MTFAPINotification requestNotification = new MTFAPINotification(getApplicationContext(), new MTFAPINotificationListener());
//		requestNotification.setData(userPhoneNumber, userId, token);
//		requestNotification.sendRequest();
	}

	private void requestShowReminder(){
		MTFSharedPreference.setPrefKeyShowReminder(getApplicationContext(), true);
		Bundle bundleForBroadcase = new Bundle();
		bundleForBroadcase.putInt(MTFIntentConstant.BUNDLE_KEY_CODE, MTFIntentConstant.CODE_SUCCESS);

		Intent intentForBroadcase = new Intent(MTFIntentConstant.ACTION_SHOW_REMINDER);
		intentForBroadcase.addCategory(MTFIntentConstant.CATEGORY_CUSTOMER_LIST);
		intentForBroadcase.putExtras(bundleForBroadcase);
		LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intentForBroadcase);
	}

	private void requestLogout(Intent intent){
		MTFUserModel userModel =  database.getActiveSession();
		String userPhoneNumber = userModel.getUserName();
		String userId = userModel.getUserId();

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
		
		MTFAPILogout requestLogout = new MTFAPILogout(getApplicationContext(), new MTFAPILogoutListener());
		requestLogout.setData(userPhoneNumber, userId);
		requestLogout.execute();
	}
	
	private void startTimeCounterForClearData(){
		Thread dataKillerThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (isServiceStart) {
					try {
						Thread.sleep(MTFGenerator.getDiffTimeToMidNight());
						clearData();
					} catch (InterruptedException e) {
						
					} catch (Exception e) {
					}
				}
			}
		});
		
		dataKillerThread.start();
	}
	
	private void clearData(){

	}
	
	private void registerLocalBroadcastReceiver(){

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(MTFIntentConstant.ACTION_NEW_CUSTOMER);
        intentFilter.addAction(MTFIntentConstant.ACTION_NEW_CUSTOMER_PURNA);
		intentFilter.addAction(MTFIntentConstant.ACTION_UPDATE_CUSTOMER);
        intentFilter.addAction(MTFIntentConstant.ACTION_UPDATE_CUSTOMER_PURNA);
		intentFilter.addCategory(MTFIntentConstant.CATEGORY_CUSTOMER_LIST);
		intentFilter.addCategory(MTFIntentConstant.CATEGORY_CUSTOMER_LIST_PURNA);
		intentFilter.addCategory(MTFIntentConstant.CATEGORY_REGISTER_CUSTOMER);
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver, intentFilter);
	}
	
	private void unregisterLocalBroadcastReceiver(){
		LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(broadcastReceiver);
	}

}
