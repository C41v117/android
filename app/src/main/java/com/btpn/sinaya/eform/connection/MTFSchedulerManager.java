package com.btpn.sinaya.eform.connection;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.btpn.sinaya.eform.BuildConfig;
import com.btpn.sinaya.eform.connection.api.listener.impl.MTFAlarmReceiver;
import com.btpn.sinaya.eform.preferences.MTFSharedPreference;
import com.btpn.sinaya.eform.utils.MTFIntentConstant;
import com.btpn.sinaya.eform.utils.MTFSystemParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vaniawidjaja on 2/13/17.
 */

public class MTFSchedulerManager {

    private static MTFSchedulerManager instance;

    private Context mContext;

    private long firstMillisStart;
    private long nextMillisStart;
    private int interval;
    private long days = 24 * 60 * 60 * 1000;

    private AlarmManager alarmManager;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat completeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    private SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");

    private MTFSchedulerManager(Context mAppContext){
        this.mContext = mAppContext;
        this.alarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
    }

    public static MTFSchedulerManager getInstance(Context mContext){
        if(instance == null){
            instance = new MTFSchedulerManager(mContext);
        }
        return instance;
    }

    public void initialize(String startHour, int interval){
        long now = System.currentTimeMillis();

        Date nowDate = new Date(now);
        String startDateString = dateFormat.format(nowDate);
        startDateString = startDateString + " " + startHour;

        Date startDate;
        try {
            startDate = completeFormat.parse(startDateString);
            long startTimeMillis = startDate.getTime();

            if(startTimeMillis > now){
                startTimeMillis = startTimeMillis - days;
            }
            calculateNextAlarm(startTimeMillis, interval);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void calculateNextAlarm(long startTimeMillis, int interval){
        long currentTimeMillis = System.currentTimeMillis();
        firstMillisStart = startTimeMillis;

        //Set firstMillisStart ke hari ini
        while(firstMillisStart + days < currentTimeMillis){
            firstMillisStart = firstMillisStart + days;
        }
        nextMillisStart = firstMillisStart;
        this.interval = interval;

        //Loop interval sampai melebihi waktu sekarang
        while(nextMillisStart < currentTimeMillis){
            nextMillisStart += (interval * 60 * 1000);
        }

        //Jika next melebihi start time hari berikut nya, maka scheduler akan dimulai sesuai start time
        //Misal 00.00, interval 13 jam, maka pada jam 10 malam, next scheduler akan jalan jam 00.00, bukan jam 02.00
        if(nextMillisStart - firstMillisStart >= days){
            //a new day
            firstMillisStart += days;
            nextMillisStart = firstMillisStart;
        }

        startNextAlarm();
    }

    private void startNextAlarm(){
        Intent intentAlarm = new Intent(mContext, MTFAlarmReceiver.class);
        intentAlarm.putExtra(MTFIntentConstant.BUNDLE_KEY_MILLIS, nextMillisStart);
        intentAlarm.putExtra(MTFIntentConstant.BUNDLE_KEY_INTERVAL, interval);
        if(checkHour()){
            intentAlarm.putExtra(MTFIntentConstant.BUNDLE_KEY_IS_MASTER_DATA, true);
        }
        PendingIntent alarmIntent = PendingIntent.getBroadcast(mContext, 0, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                nextMillisStart, alarmIntent);
        MTFSharedPreference.setIdleMessage(mContext, getNextHour());
    }

    /**
     * Check apakah jam sekarang sama dengan jam start
     * @return
     */
    private boolean checkHour(){
        Date date = new Date(nextMillisStart);
        String hour = hourFormat.format(date);
        if(BuildConfig.DEBUG){
            Log.d("EFORM_ALARM", "Next alarm at "+ date.toString()+" "+ hour);
        }
        return MTFSystemParams.schedulerStart.equals(hour);
    }

    public String getNextHour(){
        Date date = new Date(nextMillisStart);
        String hour = hourFormat.format(date);
        return hour;
    }

    public void cancelAlarm(){
        Intent intentAlarm = new Intent(mContext, MTFAlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(mContext, 0, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(alarmIntent);
    }

    public void continueAlarm(long millisIntent, int intervalIntent){
        if(millisIntent > -1){
            nextMillisStart = millisIntent;
        }
        if(intervalIntent > -1){
            interval = intervalIntent;
        }
        calculateNextAlarm(nextMillisStart, interval);
    }

    public boolean isRunning(){
        boolean alarmUp = (PendingIntent.getBroadcast(mContext, 0,
                new Intent(mContext, MTFAlarmReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);

        return alarmUp;
    }
}
