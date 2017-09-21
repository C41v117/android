package com.btpn.sinaya.eform.connection.api;


import android.content.Context;
import android.os.Bundle;

import com.btpn.sinaya.eform.connection.MTFHTTPPostConnections;
import com.btpn.sinaya.eform.connection.MTFSMPUtilities;
import com.btpn.sinaya.eform.connection.listener.MTFConnectionListener;
import com.btpn.sinaya.eform.database.MTFDatabaseHelper;
import com.btpn.sinaya.eform.utils.MTFConstants;
import com.btpn.sinaya.eform.utils.MTFIntentConstant;
import com.btpn.sinaya.eform.utils.MTFJSONKey;

import org.json.JSONException;
import org.json.JSONObject;

public class MTFAPILogout extends MTFHTTPPostConnections{

	private String userId = "";
	private String logoutUrl = "";
	private MTFDatabaseHelper database;
	
	public MTFAPILogout(Context applicationContext, MTFConnectionListener listener) {
		super(applicationContext, listener);
		database = new MTFDatabaseHelper(applicationContext);
	}

	@Override
	protected Bundle generateBundleOnRequestSuccess(String responseString) {
		try {
			Bundle bundle = new Bundle();
			JSONObject jsonResponse = new JSONObject(responseString);
//			jsonResponse = jsonResponse.getJSONObject(MTFJSONKey.KEY_D);
			String result = jsonResponse.getString(MTFJSONKey.KEY_RESULT);
			
			bundle.putString(MTFIntentConstant.BUNDLE_KEY_RESULT, result);
			bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, jsonResponse.getString(MTFJSONKey.KEY_MESSAGE));
			
			if (result.equals(MTFJSONKey.VAL_SUCCESS)) {
				bundle.putInt(MTFIntentConstant.BUNDLE_KEY_CODE, MTFIntentConstant.CODE_LOGOUT_SUCCESS);
				database.updateToken(userPhoneNumber, null);
			}else{
				bundle.putInt(MTFIntentConstant.BUNDLE_KEY_CODE, MTFIntentConstant.CODE_LOGOUT_FAILED);
			}
			
			return bundle;
		} catch (JSONException e) {
			
		} 
		return null;
	}

	@Override
	protected Bundle generateBundleOnRequestFailed(String responseString) {
		Bundle bundle = new Bundle();
		bundle.putString(MTFIntentConstant.BUNDLE_KEY_RESULT, MTFJSONKey.VAL_ERROR);
		bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, MTFJSONKey.VAL_MESSAGE_FAILED);
		bundle.putInt(MTFIntentConstant.BUNDLE_KEY_CODE, MTFIntentConstant.CODE_LOGOUT_FAILED);
		return bundle;
	}

    @Override
    protected String getRestUrl() {
        String url;
        if(MTFConstants.isUseGateway) {
            if (MTFSMPUtilities.port != null) {
                url = MTFSMPUtilities.requestType + MTFSMPUtilities.host + ":" + MTFSMPUtilities.port + "/" + MTFSMPUtilities.appIdLogoutRest;
            } else {
                url = MTFSMPUtilities.requestType + MTFSMPUtilities.host + "/" + MTFSMPUtilities.appIdLogoutRest;
            }
        }else{
            if (MTFSMPUtilities.port != null) {
                url = MTFSMPUtilities.requestType + MTFSMPUtilities.host + ":" + MTFSMPUtilities.port + "/" + MTFSMPUtilities.appIdLogoutRest1;
            } else {
                url = MTFSMPUtilities.requestType + MTFSMPUtilities.host + "/" + MTFSMPUtilities.appIdLogoutRest1;
            }
        }

        return url;
    }

    @Override
	protected String getAddtionalURL() {
		int length = userPhoneNumber.length();
		String referenceNo;
		if(userPhoneNumber.length()>4){
			referenceNo = System.currentTimeMillis() + userPhoneNumber.substring(length-4)  ;
		}else{
			referenceNo = System.currentTimeMillis() + userPhoneNumber ;
		}
		logoutUrl = "Logout(id='"+userId+"',reference_no='"+referenceNo+"')";
		return logoutUrl;
	}
	
	@Override
	protected String getFullUrl() {
		String url;
		if (MTFSMPUtilities.port != null) {
			url = MTFSMPUtilities.requestType+MTFSMPUtilities.host+":"+MTFSMPUtilities.port+"/"+MTFSMPUtilities.appIdLogout +"/"+ getAddtionalURL();
		}else{
			url = MTFSMPUtilities.requestType+MTFSMPUtilities.host+"/"+MTFSMPUtilities.appIdLogout +"/"+ getAddtionalURL();
		}
		
		return url;
	}

    @Override
    protected String generateRequest(){
        try {
            int length = userPhoneNumber.length();
            String referenceNo;
            if(userPhoneNumber.length()>4){
                referenceNo = System.currentTimeMillis() + userPhoneNumber.substring(length-4)  ;
            }else{
                referenceNo = System.currentTimeMillis() + userPhoneNumber ;
            }

            JSONObject json = new JSONObject();
            json.put(MTFJSONKey.KEY_USER_ID, userId);
            json.put(MTFJSONKey.KEY_REFERENCE_NO, referenceNo);
            json.put(MTFJSONKey.KEY_APP_ID, MTFConstants.E_SAVING);

            return json.toString();
        } catch (JSONException e) {

        }
        return null;
    }

    @Override
    protected byte[] generateBodyForHttpPost() {
        try {
            int length = userPhoneNumber.length();
            String referenceNo;
            if(userPhoneNumber.length()>4){
                referenceNo = System.currentTimeMillis() + userPhoneNumber.substring(length-4)  ;
            }else{
                referenceNo = System.currentTimeMillis() + userPhoneNumber ;
            }

            JSONObject json = new JSONObject();
            json.put(MTFJSONKey.KEY_USER_ID, userId);
            json.put(MTFJSONKey.KEY_REFERENCE_NO, referenceNo);
            JSONObject jsonParent = new JSONObject();
            jsonParent.put(MTFJSONKey.KEY_D, json);
            return jsonParent.toString().getBytes();
        } catch (JSONException e) {

        }
        return null;
    }

    @Override
    protected String getGetUrl() {
        String url;
        if (MTFSMPUtilities.port != null) {
            url = MTFSMPUtilities.requestType+MTFSMPUtilities.host+":"+MTFSMPUtilities.port+"/"+MTFSMPUtilities.appIdLogout;
        }else{
            url = MTFSMPUtilities.requestType+MTFSMPUtilities.host+"/"+MTFSMPUtilities.appIdLogout;
        }

        return url;
    }

	@Override
	protected boolean isUseFileUploadUrl() {
		return false;
	}

	public void setData(String userPhoneNumber, String userId){
		this.userId = userId;
		this.userPhoneNumber = userPhoneNumber;
	}

}
