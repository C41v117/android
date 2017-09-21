package com.btpn.sinaya.eform.services;

import android.content.Context;
import android.os.Bundle;

import com.btpn.sinaya.eform.connection.MTFHTTPPostConnections;
import com.btpn.sinaya.eform.connection.MTFSMPUtilities;
import com.btpn.sinaya.eform.connection.listener.MTFConnectionListener;
import com.btpn.sinaya.eform.database.MTFDatabaseHelper;
import com.btpn.sinaya.eform.model.MTFUserModel;
import com.btpn.sinaya.eform.utils.MTFIntentConstant;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by vaniawidjaja on 2/7/17.
 */

public class MTFAPIClientLogs extends MTFHTTPPostConnections {

    private String dateFormat = "yyyy MM dd HH:mm:ss:SSS";
    private String stacktrace;

    public MTFAPIClientLogs(Context applicationContext, MTFConnectionListener listener, String stacktrace) {
        super(applicationContext, listener);
        this.stacktrace = stacktrace;
    }

    @Override
    protected Bundle generateBundleOnRequestSuccess(String responseString) {
        Bundle bundle = new Bundle();
        bundle.putInt(MTFIntentConstant.INTENT_EXTRA_CODE, MTFIntentConstant.CODE_SUCCESS_SEND_DATA);
        return bundle;
    }

    @Override
    protected Bundle generateBundleOnRequestFailed(String responseString) {
        Bundle bundle = new Bundle();
        bundle.putInt(MTFIntentConstant.INTENT_EXTRA_CODE, MTFIntentConstant.CODE_FAILED);
        return bundle;
    }

    @Override
    protected String generateRequest() {
        long millis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String date = formatter.format(calendar.getTime());
        TimeZone tz = calendar.getTimeZone();

        MTFDatabaseHelper database = MTFDatabaseHelper.getInstance(applicationContext);
        MTFUserModel user = database.getActiveSession();

        String code = user.getUserName() + System.currentTimeMillis();

        int diff = (tz.getRawOffset() / 3600000) - 7;
        String str =  "#"+date+"#+"+diff+":00#FATAL#com.btpn.sdb.eform"+
                "#HTTP:POST#OData/Proxy#GUID-"+code+""+
                "#COID-"+code+"#sdb.app.eform#Mobile#"+user.getUserName()+"#"+
                " # "+
                "#"+stacktrace+"#";
        byte[] buffer = str.getBytes();
        database.close();
        return buffer.toString();
    }

    @Override
    protected byte[] generateBodyForHttpPost() {
        long millis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String date = formatter.format(calendar.getTime());
        TimeZone tz = calendar.getTimeZone();

        MTFDatabaseHelper database = MTFDatabaseHelper.getInstance(applicationContext);
        MTFUserModel user = database.getActiveSession();

        String code = user.getUserName() + System.currentTimeMillis();

        int diff = (tz.getRawOffset() / 3600000) - 7;
        String str =  "#"+date+"#+"+diff+":00#FATAL#com.btpn.sdb.eform"+
                "#HTTP:POST#OData/Proxy#GUID-"+code+""+
                "#COID-"+code+"#sdb.app.eform#Mobile#"+user.getUserName()+"#"+
                " # "+
                "#"+stacktrace+"#";
        byte[] buffer = str.getBytes();
        database.close();
        return buffer;
    }

    @Override
    protected String getAddtionalURL() {
        return "clientlogs/";
    }

    @Override
    protected String getRestUrl() {
        String url;
        if (MTFSMPUtilities.port != null) {
            url = MTFSMPUtilities.requestType+ MTFSMPUtilities.host+":"+ MTFSMPUtilities.port+"/"+getAddtionalURL();
        }else{
            url = MTFSMPUtilities.requestType+ MTFSMPUtilities.host+"/"+ getAddtionalURL();
        }

        return url;
    }

    @Override
    protected String getFullUrl() {
        String url;
        if (MTFSMPUtilities.port != null) {
            url = MTFSMPUtilities.requestType+ MTFSMPUtilities.host+":"+ MTFSMPUtilities.port+"/"+getAddtionalURL();
        }else{
            url = MTFSMPUtilities.requestType+ MTFSMPUtilities.host+"/"+ getAddtionalURL();
        }

        return url;
    }

    @Override
    protected String getGetUrl() {
        String url;
        if (MTFSMPUtilities.port != null) {
            url = MTFSMPUtilities.requestType+ MTFSMPUtilities.host+":"+ MTFSMPUtilities.port+"/"+getAddtionalURL();
        }else{
            url = MTFSMPUtilities.requestType+ MTFSMPUtilities.host+"/"+ getAddtionalURL();
        }

        return url;
    }

    @Override
    protected boolean isUseFileUploadUrl() {
        return false;
    }

}
