package com.btpn.sinaya.eform.connection;


import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;

import com.btpn.sinaya.eform.connection.listener.MTFConnectionListener;
import com.btpn.sinaya.eform.utils.MTFIntentConstant;
import com.sap.mobile.lib.request.BaseRequest;
import com.sap.mobile.lib.request.INetListener;
import com.sap.mobile.lib.request.IRequest;
import com.sap.mobile.lib.request.IRequestStateElement;
import com.sap.mobile.lib.request.IResponse;
import com.sap.smp.rest.ClientConnection;
import com.sap.smp.rest.SMPClientListeners.ISMPUserRegistrationListener;
import com.sap.smp.rest.SMPException;
import com.sybase.persistence.DataVault;
import com.sybase.persistence.DataVaultException;
import com.sybase.persistence.PrivateDataVault;

public abstract class MTFHTTPPutConnections implements ISMPUserRegistrationListener {

	protected Context applicationContext;
	protected DataVault dataVault;
	protected boolean isPendingResponse = false;
	protected boolean isCanceled = false;
	protected String userPhoneNumber = "";

	private MTFConnectionManager conMan;

	protected MTFConnectionListener listener;

	protected INetListener inetListener = new INetListener() {

		@Override
		public void onSuccess(IRequest request, IResponse response) {
			String responseString = parseSuccessResponse(response);
			Bundle bundle = generateBundleOnRequestSuccess(responseString);

			if (listener != null) {
				listener.onSend(bundle, applicationContext);
			}
		}

		@Override
		public void onError(IRequest request, IResponse response, IRequestStateElement state) {
			int status = state.getHttpStatusCode();
			if(status == 401){
				MTFSMPUtilities.appConnId = "";
				sendRequest();
				return;
			}
			String responseString = parseErrorResponse(response);
			if (responseString == null) {

				Bundle bundle = generateBundleOnRequestFailed(null);
				bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, "Gagal, Tidak bisa menghubungi server");

				if (listener != null) {
					listener.onSend(bundle, applicationContext);
				}

				return;
			}

			Bundle bundle = generateBundleOnRequestFailed(responseString);
			if (listener != null) {
				listener.onSend(bundle, applicationContext);
			}
		}
	};

	public MTFHTTPPutConnections(Context applicationContext, MTFConnectionListener listener) {
		this.applicationContext = applicationContext;
		this.listener = listener;
	}

	private void register(){
		MTFSMPUtilities.configurationForRegistration(applicationContext);

		if (PrivateDataVault.vaultExists(MTFSMPUtilities.getDataValultName())) {
//    		PrivateDataVault.deleteVault(Utilities.dataVaultName);

			dataVault = PrivateDataVault.getVault(MTFSMPUtilities.getDataValultName());
			dataVault.unlock(MTFSMPUtilities.appPasscode, MTFSMPUtilities.appPasscode+ MTFSMPUtilities.appSaltcode);

			MTFSMPUtilities.appConnId = dataVault.getString("appCid");
			MTFSMPUtilities.username = dataVault.getString("username");
			MTFSMPUtilities.password = dataVault.getString("password");

			sendHttpPutRequest();

			return;
		}

		try {
			MTFSMPUtilities.userManager.setUserRegistrationListener(this);
			MTFSMPUtilities.userManager.registerUser(false);
		} catch (SMPException e) {

		} catch (Exception e){

		}
	}

	@Override
	public void onAsyncRegistrationResult(State registrationState, ClientConnection clientConnection, int errCode, String errMsg) {
		try{
			if(registrationState == State.SUCCESS){

				//Create DataVault
				PrivateDataVault.init(applicationContext);

				dataVault = PrivateDataVault.createVault(MTFSMPUtilities.getDataValultName(), MTFSMPUtilities.appPasscode, MTFSMPUtilities.appPasscode+ MTFSMPUtilities.appSaltcode);
				dataVault.setString("appCid", MTFSMPUtilities.userManager.getApplicationConnectionId());
				dataVault.setString("username", MTFSMPUtilities.username);
				dataVault.setString("password", MTFSMPUtilities.password);
				dataVault.setString("host", MTFSMPUtilities.host);
				dataVault.setString("port", MTFSMPUtilities.port);
				dataVault.setString("isHttp", MTFSMPUtilities.isHttpRequest.toString());
				dataVault.lock();

				MTFSMPUtilities.appConnId = MTFSMPUtilities.userManager.getApplicationConnectionId();

				//Success Register then send specific request
				sendHttpPutRequest();
			}else{
				if (listener != null) {
					Bundle bundle = generateBundleOnRequestFailed(null);
					bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, errMsg);

					listener.onSend(bundle, applicationContext);
				}
			}
		} catch (DataVaultException e) {


			if (listener != null) {
				Bundle bundle = generateBundleOnRequestFailed(null);
				bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, "Gagal meminta data");//Local Problem, Failed to send request

				listener.onSend(bundle, applicationContext);
			}

		} catch (SMPException e) {


			if (listener != null) {
				Bundle bundle = generateBundleOnRequestFailed(null);
				bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, "Ada Masalah Koneksi");//Connection Error when registration

				listener.onSend(bundle, applicationContext);
			}

		} catch(Exception e){


			if (listener != null) {
				Bundle bundle = generateBundleOnRequestFailed(null);
				bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, "Gagal");

				listener.onSend(bundle, applicationContext);
			}
		}
	}

	public void sendRequest() {
		if (MTFSMPUtilities.appConnId.equals("")) {
			int apiVersion = Build.VERSION.SDK_INT;
			if(apiVersion > Build.VERSION_CODES.KITKAT_WATCH){
				conMan = new MTFConnectionManager(applicationContext);
				conMan.param.setUserName(MTFSMPUtilities.username);
				conMan.param.setUserPassword(MTFSMPUtilities.password);
				conMan.userManager.setUserRegistrationListener(this);
				conMan.onBoard();
				if(!MTFSMPUtilities.appConnId.equals("")){
					sendRequest();
				}else{
					if (listener != null) {
						Bundle bundle = generateBundleOnRequestFailed(null);
						bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, "Gagal Register to Server");
						bundle.putInt(MTFIntentConstant.BUNDLE_KEY_CODE, MTFIntentConstant.CODE_FAILED_TOKEN);

						listener.onSend(bundle, applicationContext);
					}
				}
			}else{
				register();
			}
		}else{
			sendHttpPutRequest();
		}
	}

	private void sendHttpPutRequest(){
		try{
			BaseRequest getrequest = new BaseRequest();

			int apiVersion = Build.VERSION.SDK_INT;
			if(apiVersion > Build.VERSION_CODES.KITKAT_WATCH){
				conMan = new MTFConnectionManager(applicationContext);
			}else{
				MTFSMPUtilities.initializeRequestManager(applicationContext);
			}

			Map<String, String> headers = new HashMap<String, String>();
			headers.put("X-SMP-APPCID", MTFSMPUtilities.appConnId);
			headers.put("XHTTP-METHOD", "MERGE");
			headers.put("Content-Type", "application/atom+xml");
			headers.put("X-Requested-With", "XMLHttpRequest");
			headers.put("Accept", "application/json");

			String url;
			if (MTFSMPUtilities.appOData != null) {
				if(MTFSMPUtilities.port ==null){
					url = MTFSMPUtilities.requestType+ MTFSMPUtilities.host+"/"+ MTFSMPUtilities.appOData +"/"+ MTFSMPUtilities.appId+"/"+ getAddtionalURL();
				}else{
					url = MTFSMPUtilities.requestType+ MTFSMPUtilities.host+":"+ MTFSMPUtilities.port+"/"+ MTFSMPUtilities.appOData +"/"+ MTFSMPUtilities.appId+"/"+ getAddtionalURL();
				}
			}else{
				url = MTFSMPUtilities.requestType+ MTFSMPUtilities.host+"/"+ MTFSMPUtilities.appOData +"/"+ MTFSMPUtilities.appId+"/"+ getAddtionalURL();
			}

			getrequest.setRequestUrl(url);
			getrequest.setRequestMethod(BaseRequest.REQUEST_METHOD_PUT);
			getrequest.setData(generateBodyForHttpPost());
			getrequest.setHeaders(headers);
			getrequest.setListener(inetListener);

			if (!isCanceled) {
				if(apiVersion > Build.VERSION_CODES.KITKAT_WATCH){
					MTFConnectionManager.requestManager.makeRequest(getrequest);
				}else{
					MTFSMPUtilities.reqMan.makeRequest(getrequest);
				}
			}
		} catch(Exception e) {

			if (listener != null) {

				Bundle bundle = generateBundleOnRequestFailed(null);
				bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, "Ada masalah koneksi");//Connection Error when send request

				listener.onSend(bundle, applicationContext);
			}
		}
	}

	private String parseSuccessResponse(IResponse response){
		String responseString = "";

		try {

			DataInputStream dis = new DataInputStream(response.getEntity().getContent());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] data = new byte[1024];
			int length = 0;
			while ((length = dis.read(data)) != -1) {
				bos.write(data, 0, length);
			}

			responseString = new String(bos.toByteArray());

		} catch (IllegalStateException e) {


		} catch (IOException e) {


		} catch(Exception e){


		}

		if (responseString.equals("")) {
			return null;
		}

		return responseString;
	}

	private String parseErrorResponse(IResponse response){
		String responseString = "";
		if (response == null) {
			return null;
		}

		try {
			DataInputStream dis = new DataInputStream(response.getEntity().getContent());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] data = new byte[1024];
			int length = 0;
			while ((length = dis.read(data)) != -1) {
				bos.write(data, 0, length);
			}

			responseString = new String(bos.toByteArray());
		} catch (IllegalStateException e) {


		} catch (IOException e) {


		}

		if (responseString.equals("")) {
			return null;
		}

		return responseString;
	}

	protected abstract Bundle generateBundleOnRequestSuccess(String responseString);
	protected abstract Bundle generateBundleOnRequestFailed(String responseString);
	protected abstract byte[] generateBodyForHttpPost();
	protected abstract String getAddtionalURL();
	protected abstract String getFullUrl();

}