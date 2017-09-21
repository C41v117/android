package com.btpn.sinaya.eform.connection.api.listener.impl;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.btpn.sinaya.eform.connection.MTFSchedulerManager;
import com.btpn.sinaya.eform.connection.api.listener.MTFServiceScheduler;
import com.btpn.sinaya.eform.database.MTFDatabaseHelper;
import com.btpn.sinaya.eform.model.MTFUserModel;
import com.btpn.sinaya.eform.preferences.MTFSharedPreference;
import com.btpn.sinaya.eform.utils.MTFFileLogger;
import com.btpn.sinaya.eform.utils.MTFGenerator;
import com.btpn.sinaya.eform.utils.MTFIntentConstant;
import com.btpn.sinaya.eform.utils.MTFMessageBroadcaster;
import com.btpn.sinaya.eform.utils.MTFSystemParams;

/**
 * Created by vaniawidjaja on 2/13/17.
 */

public class MTFAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            if(!MTFSystemParams.isSchedulerDisable) {
                if (MTFGenerator.isConnectionAvailable(context)) {
                    MTFDatabaseHelper database = MTFDatabaseHelper.getInstance(context);
                    //MTFUserModel user = database.getActiveSession();
                    /*if(database.getActiveSession().isOnline()){
                        MTFFileLogger.getInstance().addString(MTFServiceScheduler.SCHEDULER_LOGGER_TAG, "Agent Online and Connected to : "+MTFGenerator.getWifiName(context));
                    }else{
                        MTFFileLogger.getInstance().addString(MTFServiceScheduler.SCHEDULER_LOGGER_TAG, "Agent Offline and Connected to : "+ MTFGenerator.getWifiName(context));
                    }*/
//                    String token = user.getToken();
//                    if (token != null && !token.equals("")) {
                        if (!MTFSharedPreference.isSchedulerSending(context)) {
                            boolean isMasterData = intent.getBooleanExtra(MTFIntentConstant.BUNDLE_KEY_IS_MASTER_DATA, false);
                            Intent intentService = new Intent(context, MTFServiceScheduler.class);
                            intentService.setAction(MTFIntentConstant.ACTION_SEND_DATA_SCHEDULER);
                            intentService.putExtra(MTFIntentConstant.BUNDLE_KEY_COUNTER, 0);
                            intentService.putExtra(MTFIntentConstant.BUNDLE_KEY_IS_MASTER_DATA, isMasterData);
                            context.startService(intentService);
                        }

                        long nextMillisStart = intent.getLongExtra(MTFIntentConstant.BUNDLE_KEY_MILLIS, -1);
                        if (nextMillisStart != -1) {
                            int interval = intent.getIntExtra(MTFIntentConstant.BUNDLE_KEY_INTERVAL, MTFSystemParams.schedulerInterval);
                            MTFSchedulerManager.getInstance(context.getApplicationContext()).calculateNextAlarm(nextMillisStart, interval);
                        } else {
                            int interval = intent.getIntExtra(MTFIntentConstant.BUNDLE_KEY_INTERVAL, MTFSystemParams.schedulerInterval);
                            MTFSchedulerManager.getInstance(context.getApplicationContext()).initialize(MTFSystemParams.schedulerStart, interval);
                        }
//                    } else {
//                        /*MTFMessageBroadcaster.broadcastMessage(context.getApplicationContext(), MTFServiceScheduler.SCHEDULER_TOKEN_ERROR);
//                        MTFSharedPreference.setMessage(context, MTFServiceScheduler.SCHEDULER_TOKEN_ERROR);*/
//                        Intent intentService = new Intent(context, MTFServiceScheduler.class);
//                        intentService.setAction(MTFIntentConstant.ACTION_NEED_LOGIN_SCHEDULER);
//                        context.startService(intentService);
//                    }
                }else{
                    MTFFileLogger.getInstance().addString(MTFServiceScheduler.SCHEDULER_LOGGER_TAG, "Device not connected to any network");
                    long nextMillisStart = intent.getLongExtra(MTFIntentConstant.BUNDLE_KEY_MILLIS, -1);
                    if (nextMillisStart != -1) {
                        int interval = intent.getIntExtra(MTFIntentConstant.BUNDLE_KEY_INTERVAL, MTFSystemParams.schedulerInterval);
                        MTFSchedulerManager.getInstance(context.getApplicationContext()).calculateNextAlarm(nextMillisStart, interval);
                    } else {
                        int interval = intent.getIntExtra(MTFIntentConstant.BUNDLE_KEY_INTERVAL, MTFSystemParams.schedulerInterval);
                        MTFSchedulerManager.getInstance(context.getApplicationContext()).initialize(MTFSystemParams.schedulerStart, interval);
                    }
                }
            }
        }catch(Exception e){

        }
    }
}