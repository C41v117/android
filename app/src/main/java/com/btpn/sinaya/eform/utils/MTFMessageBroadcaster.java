package com.btpn.sinaya.eform.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by vaniawidjaja on 2/13/17.
 */

public class MTFMessageBroadcaster {

    public static final String MESSAGE_ACTION = "com.btpn.sinaya.eform.BROADCAST_MESSAGE_ACTION";
    public static final String MESSAGE_CATEGORY = "com.btpn.sinaya.eform.BROADCAST_MESSAGE_CATEGORY";

    public static final String MESSAGE_FINISH = "finish";

    public static void broadcastMessage(Context mContext, String message){
        Intent intent = new Intent();
        intent.setAction(MESSAGE_ACTION);
        intent.putExtra(MTFIntentConstant.INTENT_EXTRA_MESSAGE, message);

        mContext.sendBroadcast(intent);
    }

    public static void registerBroadcastMessageReceiver(Context mContext, BroadcastReceiver receiver){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_ACTION);
        mContext.registerReceiver(receiver, intentFilter);
    }

    public static void unregisterBroadcastMessageReceiver(Context mContext, BroadcastReceiver receiver){
        mContext.unregisterReceiver(receiver);
    }


}