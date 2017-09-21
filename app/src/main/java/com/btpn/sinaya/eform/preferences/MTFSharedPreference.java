package com.btpn.sinaya.eform.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.btpn.sinaya.eform.connection.MTFSchedulerManager;

/**
 * Created by vaniawidjaja on 2/7/17.
 */

public class MTFSharedPreference {

    private static String SHARED_PREFERENCE_NAME = "com.btpn.sinaya.eform.SHARED_PREF";
    public static String PREF_KEY_KEY = "key";
    public static String PREF_KEY_IS_SCHEDULER_SENDING = "isSchedulerSendingData";
    public static String PREF_KEY_IS_MANUAL_SENDING = "isManualSendingData";
    public static String PREF_KEY_SENDER = "senderSource";
    public static String PREF_KEY_INTERRUPT = "interruptSending";
    public static String PREF_KEY_PHONE_NUMBER = "phoneNumber";
    public static String PREF_KEY_COUNTER = "counter";
    public static String PREF_KEY_MESSAGE = "message";
    public static String PREF_KEY_X_CSRF_TOKEN = "x-csrf-token";
    public static String PREF_KEY_UPGRADE_SQL_LIB = "upgrade_sql_lib";
    public static String PREF_KEY_TYPE_IMAGE = "type-image";
    public static String PREF_KEY_SHOW_REMINDER = "showReminder";
    public static String PREF_KEY_UPDATE_ACTIVATION = "update_activation";
    public static String PREF_KEY_NEED_TO_RELOAD_PARAM = "reload_param";

    public static String SENDER_SOURCE_SCHEDULER = "scheduler";
    public static String SENDER_SOURCE_MANUAL = "manual";

    public static SharedPreferences getSharedPreference(Context mContext){
        return mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
    }

    public static void setSchedulerSending(Context mContext, boolean isSending){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_KEY_IS_SCHEDULER_SENDING, isSending);
        editor.commit();
        if(!isSending){
            setIdleMessage(mContext, MTFSchedulerManager.getInstance(mContext).getNextHour());
        }
    }

    public static void setManualSending(Context mContext, boolean isSending){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_KEY_IS_MANUAL_SENDING, isSending);
        editor.commit();
    }

    public static boolean isSchedulerSending(Context mContext){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        return prefs.getBoolean(PREF_KEY_IS_SCHEDULER_SENDING, false);
    }

    public static boolean isManualSending(Context mContext){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        return prefs.getBoolean(PREF_KEY_IS_MANUAL_SENDING, false);
    }

    public static void setSendingInterrupt(Context mContext, boolean isInterrupt, String phoneNumber){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_KEY_INTERRUPT, isInterrupt);
        editor.putString(PREF_KEY_PHONE_NUMBER, phoneNumber);
        editor.commit();
    }

    public static boolean isInterruptActive(Context mContext){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        return prefs.getBoolean(PREF_KEY_INTERRUPT, false);
    }

    public static String getActiveSendingPhoneNumber(Context mContext){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        return prefs.getString(PREF_KEY_PHONE_NUMBER, "");
    }

    public static void setCounterForScheduler(Context mContext, int counter){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_KEY_COUNTER, counter);
        editor.commit();
    }

    public static int getCounterForScheduler(Context mContext){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        return prefs.getInt(PREF_KEY_COUNTER, -1);
    }

    public static void resetCounter(Context mContext){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_KEY_COUNTER, -1);
        editor.commit();
    }

    public static void setIdleMessage(Context mContext, String nextHour){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_KEY_MESSAGE, "Scheduler is idle, Next run at "+nextHour);
        editor.commit();
    }
    public static void setMessage(Context mContext, String message){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_KEY_MESSAGE, message);
        editor.commit();
    }

    public static String getMessage(Context mContext){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        return prefs.getString(PREF_KEY_MESSAGE, "");
    }

    public static void setXCSRFToken(Context mContext, String reqToken){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_KEY_X_CSRF_TOKEN, reqToken);
        editor.commit();
    }

    public static String getXCSRFToken(Context mContext){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        return prefs.getString(PREF_KEY_X_CSRF_TOKEN, "");
    }

    public static void setPrefKeyUpgradeSqlLib(Context mContext, boolean isUpgraded){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_KEY_UPGRADE_SQL_LIB, isUpgraded);
        editor.commit();
    }

    public static boolean isUpgradedSqlLib(Context mContext){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        return prefs.getBoolean(PREF_KEY_UPGRADE_SQL_LIB, false);
    }

    public static void setTypeImage(Context mContext, String typeImage){
        SharedPreferences prefs = getSharedPreference(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_KEY_TYPE_IMAGE, typeImage);
        editor.commit();
    }

    public static String getTypeImage(Context mContext){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        return prefs.getString(PREF_KEY_TYPE_IMAGE, "");
    }

    public static void setPermissionNeverAskAgain(Context mContext, String permission, boolean neverAskAgain){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(permission, neverAskAgain);
        editor.commit();
    }

    public static boolean checkPermissionNeverAskAgain(Context mContext, String permission){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        return prefs.getBoolean(permission, false);
    }

    public static void setPrefKeyShowReminder(Context mContext, boolean showReminder){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_KEY_SHOW_REMINDER, showReminder);
        editor.commit();
    }

    public static boolean isShowReminder(Context mContext){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        return prefs.getBoolean(PREF_KEY_SHOW_REMINDER, false);
    }

    public static void setPrefKeyUpdateActivation(Context mContext, boolean isUpdate){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_KEY_UPDATE_ACTIVATION, isUpdate);
        editor.commit();
    }

    public static boolean isUpdateActivation(Context mContext){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        return prefs.getBoolean(PREF_KEY_UPDATE_ACTIVATION, false);
    }

    public static void setPrefKeyNeedToReloadParam(Context mContext, boolean needReload){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_KEY_NEED_TO_RELOAD_PARAM, needReload);
        editor.commit();
    }

    public static boolean needToReloadParam(Context mContext){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        return prefs.getBoolean(PREF_KEY_NEED_TO_RELOAD_PARAM, false);
    }

    public static void setKey(Context mContext, String key){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_KEY_KEY, key);
        editor.commit();
    }

    public static String getKey(Context mContext){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_APPEND);
        return prefs.getString(PREF_KEY_KEY, "");
    }
}