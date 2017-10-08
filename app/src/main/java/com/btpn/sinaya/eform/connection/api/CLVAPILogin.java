package com.btpn.sinaya.eform.connection.api;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

import com.btpn.sinaya.eform.connection.MTFHTTPPostConnections;
import com.btpn.sinaya.eform.connection.MTFSMPUtilities;
import com.btpn.sinaya.eform.connection.listener.MTFConnectionListener;
import com.btpn.sinaya.eform.model.MTFUserModel;
import com.btpn.sinaya.eform.utils.MTFConstants;
import com.btpn.sinaya.eform.utils.MTFIntentConstant;
import com.btpn.sinaya.eform.utils.MTFJSONKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CLVAPILogin extends MTFHTTPPostConnections{

	private String loginUrl = "";
	private MTFUserModel userModel;

	public CLVAPILogin(Context applicationContext, MTFConnectionListener listener) {
		super(applicationContext, listener);
	}

	@Override
	protected Bundle generateBundleOnRequestSuccess(String responseString) {
		try {
			Bundle bundle = new Bundle();
			JSONObject jsonResponse = new JSONObject(responseString);
			String result = jsonResponse.getString(MTFJSONKey.KEY_RESULT);

			bundle.putString(MTFIntentConstant.BUNDLE_KEY_RESULT, result);
			bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, jsonResponse.getString(MTFJSONKey.KEY_MESSAGE));

			if (result.equals(MTFJSONKey.VAL_SUCCESS)) {
				String version = jsonResponse.getString(MTFJSONKey.KEY_VERSION);

				bundle.putString(MTFIntentConstant.BUNDLE_KEY_USERID, jsonResponse.getString(MTFJSONKey.KEY_USERID));
				bundle.putString(MTFIntentConstant.BUNDLE_KEY_USERNAME, jsonResponse.getString(MTFJSONKey.KEY_USERNAME));
				bundle.putString(MTFIntentConstant.BUNDLE_KEY_VERSION, jsonResponse.getString(MTFJSONKey.KEY_VERSION));
				bundle.putString(MTFIntentConstant.BUNDLE_KEY_TOKEN, jsonResponse.getString(MTFJSONKey.KEY_TOKEN));
				bundle.putString(MTFIntentConstant.BUNDLE_KEY_LOCATION_ID, jsonResponse.getString(MTFJSONKey.KEY_LOCATION_ID));
				bundle.putString(MTFIntentConstant.BUNDLE_KEY_LOCATION, jsonResponse.getString(MTFJSONKey.KEY_LOCATION_TITLE));
				bundle.putString(MTFIntentConstant.BUNDLE_KEY_AGENT_TYPE, jsonResponse.getString(MTFJSONKey.KEY_AGENT_TYPE));
                bundle.putString(MTFIntentConstant.BUNDLE_KEY_JSON, jsonResponse.getString(MTFJSONKey.KEY_JSON));

				if(MTFConstants.isUseGateway) {
					//set secret to constants
                    bundle.putString(MTFIntentConstant.BUNDLE_KEY_SECRET, jsonResponse.getString(MTFJSONKey.BTPN_KEY_SECRET));
				}

				//Check Version
				PackageInfo pInfo = applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), 0);
				String androidVersion = pInfo.versionName;

				boolean isVersionValid	=	false;
				List<String> versionServer	=	Arrays.asList(version.split("\\s*,\\s*"));
				for(int i = 0;i<versionServer.size();i++){
					if(versionServer.get(i).equals(androidVersion)){
						isVersionValid = true;
						i = versionServer.size()+1;
					}
				}

				if (!isVersionValid) {
					bundle.putInt(MTFIntentConstant.BUNDLE_KEY_CODE, MTFIntentConstant.CODE_LOGIN_FAILED_DIFF_VER);
				}else{
					bundle.putInt(MTFIntentConstant.BUNDLE_KEY_CODE, MTFIntentConstant.CODE_LOGIN_SUCCESS);
				}
			}else if (result.equals(MTFJSONKey.VAL_CHANGE)){
				bundle.putString(MTFIntentConstant.BUNDLE_KEY_USERID, jsonResponse.getString(MTFJSONKey.KEY_USERID));
				bundle.putString(MTFIntentConstant.BUNDLE_KEY_USERNAME, jsonResponse.getString(MTFJSONKey.KEY_USERNAME));
				bundle.putString(MTFIntentConstant.BUNDLE_KEY_VERSION, jsonResponse.getString(MTFJSONKey.KEY_VERSION));
				bundle.putInt(MTFIntentConstant.BUNDLE_KEY_CODE, MTFIntentConstant.CODE_LOGIN_CHANGE_DEVICE);
			}else{
				bundle.putInt(MTFIntentConstant.BUNDLE_KEY_CODE, MTFIntentConstant.CODE_LOGIN_FAILED_INVALID);
			}

			return bundle;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (NameNotFoundException e) {

		}

		return null;
	}

	@Override
	protected Bundle generateBundleOnRequestFailed(String responseString) {
		Bundle bundle = new Bundle();

		bundle.putString(MTFIntentConstant.BUNDLE_KEY_RESULT, MTFJSONKey.VAL_ERROR);
		bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, MTFJSONKey.VAL_MESSAGE_FAILED);
		bundle.putInt(MTFIntentConstant.BUNDLE_KEY_CODE, MTFIntentConstant.CODE_LOGIN_FAILED_INVALID);

		return bundle;
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
            json.put(MTFJSONKey.KEY_USERNAME, userModel.getUserName());
            json.put(MTFJSONKey.KEY_SIGNATURE, userModel.getPin());
            json.put(MTFJSONKey.KEY_DEVICE_UID, userModel.getImei());
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
			json.put(MTFJSONKey.KEY_USERNAME, userModel.getUserName());
			json.put(MTFJSONKey.KEY_SIGNATURE, userModel.getPin());
			json.put(MTFJSONKey.KEY_DEVICE_UID, userModel.getImei());
			json.put(MTFJSONKey.KEY_REFERENCE_NO, referenceNo);
			JSONObject jsonParent = new JSONObject();
			jsonParent.put(MTFJSONKey.KEY_D, json);
			return jsonParent.toString().getBytes();
		} catch (JSONException e) {

		}
		return null;
	}

	@Override
	protected String getAddtionalURL() {
		loginUrl = "Login";
		return loginUrl;
	}

	@Override
	protected Map<String, String> getCustomHeaders(String url, String requestBody) {
		Map<String, String> map = new HashMap<>();
			map.put(MTFJSONKey.BTPN_API_KEY, MTFConstants.BTPN_API_KEY_VALUE);
		return map;
	}

    @Override
    protected String getRestUrl() {
        String url;
        if(MTFConstants.isUseGateway) {
            if (MTFSMPUtilities.port != null) {
                url = MTFSMPUtilities.requestType + MTFSMPUtilities.host + ":" + MTFSMPUtilities.port + "/" + MTFSMPUtilities.appIdLoginRest;
            } else {
                url = MTFSMPUtilities.requestType + MTFSMPUtilities.host + "/" + MTFSMPUtilities.appIdLoginRest;
            }
        }else{
            if (MTFSMPUtilities.port != null) {
                url = MTFSMPUtilities.requestType + MTFSMPUtilities.host + ":" + MTFSMPUtilities.port + "/" + MTFSMPUtilities.appIdLoginRest1;
            } else {
                url = MTFSMPUtilities.requestType + MTFSMPUtilities.host + "/" + MTFSMPUtilities.appIdLoginRest1;
            }
        }

        return url;
    }

	@Override
	protected String getFullUrl() {
		String url;
		if (MTFSMPUtilities.port != null) {
			url = MTFSMPUtilities.requestType+MTFSMPUtilities.host+":"+MTFSMPUtilities.port+"/"+MTFSMPUtilities.appIdLogin +"/"+ getAddtionalURL();
		}else{
			url = MTFSMPUtilities.requestType+MTFSMPUtilities.host+"/"+MTFSMPUtilities.appIdLogin +"/"+ getAddtionalURL();
		}

		return url;
	}

	@Override
	protected String getGetUrl() {
		String url;
		if (MTFSMPUtilities.port != null) {
			url = MTFSMPUtilities.requestType+MTFSMPUtilities.host+":"+MTFSMPUtilities.port+"/"+MTFSMPUtilities.appIdLogin;
		}else{
			url = MTFSMPUtilities.requestType+MTFSMPUtilities.host+"/"+MTFSMPUtilities.appIdLogin;
		}
		
		return url;
	}
	
	@Override
	protected boolean isUseFileUploadUrl() {
		return false;
	}
	
	public void setData(String userPhoneNumber, MTFUserModel userModel){
		this.userPhoneNumber = userPhoneNumber;
		this.userModel = userModel;
	}
}
