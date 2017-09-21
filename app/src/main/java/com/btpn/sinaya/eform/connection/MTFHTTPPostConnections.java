package com.btpn.sinaya.eform.connection;


import android.content.Context;
import android.graphics.Path;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.connection.listener.MTFConnectionListener;
import com.btpn.sinaya.eform.database.MTFDatabaseHelper;
import com.btpn.sinaya.eform.model.MTFUserModel;
import com.btpn.sinaya.eform.preferences.MTFSharedPreference;
import com.btpn.sinaya.eform.utils.MTFConstants;
import com.btpn.sinaya.eform.utils.MTFFileHelper;
import com.btpn.sinaya.eform.utils.MTFHttpPostRest;
import com.btpn.sinaya.eform.utils.MTFIntentConstant;
import com.btpn.sinaya.eform.utils.MTFJSONKey;
import com.btpn.sinaya.eform.utils.MTFSystemParams;
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

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public abstract class MTFHTTPPostConnections extends AsyncTask<Void, String, String> implements ISMPUserRegistrationListener {

	protected Context applicationContext;
	protected DataVault dataVault;
	protected boolean isPendingResponse = false;
	protected boolean isCanceled = false;
	protected String userPhoneNumber = "";
	private MTFConnectionManager conMan;
	protected MTFConnectionListener listener;

	private String reqToken = "";
	private boolean isRespond = false;
	private boolean isTimerRespond = false;
	private Handler handler = null;
	private Runnable runnable;
    private MTFUserModel userModel;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        MTFDatabaseHelper database = new MTFDatabaseHelper(applicationContext);
        userModel = database.getActiveSession();
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = "";
        try {
            String url = getRestUrl();
            String request = generateRequest();
			Map<String, String> customHeaders = getCustomHeaders(url, request);

            if(MTFConstants.logging) {
                SimpleDateFormat completeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
                Date date = new Date(System.currentTimeMillis());
                MTFFileHelper.writeToSDFileLogin("START, " + url + ", " + request + ", " + completeFormat.format(date));

                result = MTFHttpPostRest.postData(url, request, customHeaders);

                Date date1 = new Date(System.currentTimeMillis());
                MTFFileHelper.writeToSDFileLogin("END, " + url + ", " + completeFormat.format(date1));
            }else{
                result = MTFHttpPostRest.postData(url, request, customHeaders);
            }
        } catch (Exception e) {
            if(e.getMessage().contains(MTFConstants.AUTHENTICATION_FAILED)){
                result = MTFConstants.RESPOND_ERROR_TOKEN;
            }
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            Bundle bundle = null;
            try {
                bundle = generateBundleOnRequestSuccess(result);
            } catch (JSONException e) {
                bundle = generateBundleOnRequestFailed(result);

                if (listener != null) {
                    listener.onSend(bundle, applicationContext);
                }
                return;
            }

            if (!isPendingResponse) {
                if (bundle == null) {
                    bundle = generateBundleOnRequestFailed(result);
                }

                if (listener != null) {
                    listener.onSend(bundle, applicationContext);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

	private void startTimerRequest() {
		if (handler == null) {
			try {
				handler = new Handler();
			} catch (Exception e) {
				handler = new Handler(Looper.getMainLooper());
			}
		}
		isRespond = false;
		isTimerRespond = false;
		handler.postDelayed(runnable, MTFSystemParams.mobileTimeOut);
	}

	protected INetListener inetListener = new INetListener() {

		@Override
		public void onSuccess(IRequest request, IResponse response) {
			if (!isTimerRespond) {
				isRespond = true;
				if (handler != null) {
					handler.removeCallbacks(runnable);
				}
				String responseString = parseSuccessResponse(response);
				if (responseString == null) {

					Bundle bundle = generateBundleOnRequestFailed(null);
					bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, applicationContext.getResources().getString(R.string.failed_to_connect));

					if (listener != null) {
						listener.onSend(bundle, applicationContext);
					}

					return;
				}

				Bundle bundle = null;
				try {
					bundle = generateBundleOnRequestSuccess(responseString);
				} catch (JSONException e) {
					bundle = generateBundleOnRequestFailed(responseString);

					if (listener != null) {
						listener.onSend(bundle, applicationContext);
					}

					return;
				}

				if (!isPendingResponse) {
					if (bundle == null) {
						bundle = generateBundleOnRequestFailed(responseString);
					}

					if (listener != null) {
						listener.onSend(bundle, applicationContext);
					}
				}
			}
		}

		@Override
		public void onError(IRequest request, IResponse response, IRequestStateElement state) {
			if (!isTimerRespond) {
				isRespond = true;
				if (handler != null) {
					handler.removeCallbacks(runnable);
				}
				int status = state.getHttpStatusCode();
				String responseString = parseErrorResponse(response);
				if (status == 401 || status == 404 || status == 403) {
					MTFSMPUtilities.appConnId = "";
					Bundle bundle = generateBundleOnRequestFailed(null);
					bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, applicationContext.getResources().getString(R.string.invalid_token));
					bundle.putInt(MTFIntentConstant.BUNDLE_KEY_CODE, MTFIntentConstant.CODE_FAILED_TOKEN);
					if (listener != null) {
						listener.onSend(bundle, applicationContext);
					}

					return;
				}

				if (responseString == null) {

					Bundle bundle = generateBundleOnRequestFailed(responseString);
					bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, applicationContext.getResources().getString(R.string.failed_to_connect));

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
		}
	};

	protected INetListener inetListenerGet = new INetListener() {

		@Override
		public void onSuccess(IRequest request, IResponse response) {
			if (!isTimerRespond) {
				isRespond = true;
				if (handler != null) {
					handler.removeCallbacks(runnable);
				}
				reqToken = response.getHeadersMap().get("X-CSRF-Token");
				MTFSharedPreference.setXCSRFToken(applicationContext, reqToken);
				sendRequest();
			}
		}

		@Override
		public void onError(IRequest request, IResponse response, IRequestStateElement state) {
			if (!isTimerRespond) {
				isRespond = true;
				if (handler != null) {
					handler.removeCallbacks(runnable);
				}
				int status = state.getHttpStatusCode();
				if (status == 401 || status == 404 || status == 403) {
					MTFSMPUtilities.appConnId = "";
					Bundle bundle = generateBundleOnRequestFailed(null);
					bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, applicationContext.getResources().getString(R.string.invalid_token));
					bundle.putInt(MTFIntentConstant.BUNDLE_KEY_CODE, MTFIntentConstant.CODE_FAILED_TOKEN);
					if (listener != null) {
						listener.onSend(bundle, applicationContext);
					}

					return;
				}
				if (response != null) {
					reqToken = response.getHeadersMap().get("X-CSRF-Token");

					if (reqToken != null && !request.equals("")) {
						MTFSharedPreference.setXCSRFToken(applicationContext, reqToken);
						sendRequest();
						return;
					}
				}

				//No Token And Bundle
				Bundle bundle = generateBundleOnRequestFailed(null);
				bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, applicationContext.getResources().getString(R.string.failed_to_connect));

				if (listener != null) {
					listener.onSend(bundle, applicationContext);
				}
			}
		}
	};

	public MTFHTTPPostConnections(Context applicationContext, MTFConnectionListener listener) {
		this.applicationContext = applicationContext;
		this.listener = listener;
	}

	private void registerLolipop() {
		conMan = new MTFConnectionManager(applicationContext);
		MTFSharedPreference.setXCSRFToken(applicationContext, "");
		if (PrivateDataVault.vaultExists(MTFSMPUtilities.getDataValultName())) {

			dataVault = PrivateDataVault.getVault(MTFSMPUtilities.getDataValultName());
			dataVault.unlock(MTFSMPUtilities.appPasscode, MTFSMPUtilities.appPasscode + MTFSMPUtilities.appSaltcode);

			MTFSMPUtilities.appConnId = dataVault.getString("appCid");
			MTFSMPUtilities.username = dataVault.getString("username");
			MTFSMPUtilities.password = dataVault.getString("password");

			sendRequest();

			return;
		} else {
			conMan.userManager.setUserRegistrationListener(this);
			conMan.onBoard();
			if (!MTFSMPUtilities.appConnId.equals("")) {
				sendRequest();
			} else {
				if (listener != null) {
					Bundle bundle = generateBundleOnRequestFailed(null);
					bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, "Gagal Register to Server");
					bundle.putInt(MTFIntentConstant.BUNDLE_KEY_CODE, MTFIntentConstant.CODE_FAILED);

					listener.onSend(bundle, applicationContext);
				}
			}
		}
	}

	private void register() {
		MTFSMPUtilities.configurationForRegistration(applicationContext);
		MTFSharedPreference.setXCSRFToken(applicationContext, "");
		if (PrivateDataVault.vaultExists(MTFSMPUtilities.getDataValultName())) {

			dataVault = PrivateDataVault.getVault(MTFSMPUtilities.getDataValultName());
			dataVault.unlock(MTFSMPUtilities.appPasscode, MTFSMPUtilities.appPasscode + MTFSMPUtilities.appSaltcode);

			MTFSMPUtilities.appConnId = dataVault.getString("appCid");
			sendRequest();

			return;
		}

		try {
			MTFSMPUtilities.userManager.setUserRegistrationListener(this);
			MTFSMPUtilities.userManager.registerUser(false);
		} catch (SMPException e) {

		}
	}

	@Override
	public void onAsyncRegistrationResult(State registrationState, ClientConnection clientConnection, int errCode, String errMsg) {
		try {
			if (registrationState == State.SUCCESS) {

				//Create DataVault
				PrivateDataVault.init(applicationContext);
				MTFSMPUtilities.initializeRequestManager(applicationContext);
				if (PrivateDataVault.vaultExists(MTFSMPUtilities.getDataValultName())) {
					dataVault = PrivateDataVault.getVault(MTFSMPUtilities.getDataValultName());
					dataVault.unlock(MTFSMPUtilities.appPasscode, MTFSMPUtilities.appPasscode + MTFSMPUtilities.appSaltcode);
				} else {
					dataVault = PrivateDataVault.createVault(MTFSMPUtilities.getDataValultName(), MTFSMPUtilities.appPasscode, MTFSMPUtilities.appPasscode + MTFSMPUtilities.appSaltcode);
				}
				if (MTFSMPUtilities.appConnId.equals("")) {
					dataVault.setString("appCid", MTFSMPUtilities.userManager.getApplicationConnectionId());
					MTFSMPUtilities.appConnId = MTFSMPUtilities.userManager.getApplicationConnectionId();
				} else {
					dataVault.setString("appCid", MTFSMPUtilities.appConnId);
				}
				dataVault.setString("username", MTFSMPUtilities.username);
				dataVault.setString("password", MTFSMPUtilities.password);
				dataVault.setString("host", MTFSMPUtilities.host);
				dataVault.setString("port", MTFSMPUtilities.port);
				dataVault.setString("isHttp", MTFSMPUtilities.isHttpRequest.toString());
				dataVault.lock();

				//Success Register then send specific request
				sendRequest();
			} else {
				if (listener != null) {
					Bundle bundle = generateBundleOnRequestFailed(null);
					bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, applicationContext.getResources().getString(R.string.failed_to_register));
					bundle.putInt(MTFIntentConstant.BUNDLE_KEY_CODE, MTFIntentConstant.CODE_FAILED);

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
				bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, "Ada masalah koneksi");//Connection Error when registration

				listener.onSend(bundle, applicationContext);
			}

		} catch (Exception e) {


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
			if (apiVersion > Build.VERSION_CODES.KITKAT_WATCH) {
				registerLolipop();
			} else {
				register();
			}
		} else if (!getAddtionalURL().startsWith("Login") && !getAddtionalURL().startsWith("Logout(")
				&& !getAddtionalURL().startsWith("VerifyOtp(") && !getAddtionalURL().startsWith("SendOtp(")) {

			if (getAddtionalURL().startsWith("clientlogs")) {
				sendHttpPostRequest();
				return;
			}
			MTFDatabaseHelper database = MTFDatabaseHelper.getInstance(applicationContext);
			MTFUserModel userModel = database.getActiveSession();
			database.close();
			String token = userModel.getToken();
			if (token == null || token.equals("") || token.equalsIgnoreCase("null")) {
				Bundle bundle = new Bundle();
				bundle.putInt(MTFIntentConstant.BUNDLE_KEY_CODE, MTFIntentConstant.CODE_FAILED_TOKEN);
				bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, "Anda belum login ke server, silakan melakukan login terlebih dahulu");

				if (listener != null) {
					listener.onSend(bundle, applicationContext);
				}
				return;
			} else if (reqToken == null || reqToken.equals("")) {
				runnable = new Runnable() {

					@Override
					public void run() {
						if (!isRespond) {
							isTimerRespond = true;
							Bundle bundle = generateBundleOnRequestFailed(null);
							bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, applicationContext.getResources().getString(R.string.failed_to_connect));

							if (listener != null) {
								listener.onSend(bundle, applicationContext);
							}
						}
					}
				};
				startTimerRequest();
				reqToken = MTFSharedPreference.getXCSRFToken(applicationContext);
				if (reqToken.equals("")) {
					sendHttpGetRequest();
				} else {
					sendHttpPostRequest();
				}
			} else {
				runnable = new Runnable() {

					@Override
					public void run() {
						if (!isRespond) {
							isTimerRespond = true;
							Bundle bundle = generateBundleOnRequestFailed(null);
							bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, applicationContext.getResources().getString(R.string.failed_to_connect));

							if (listener != null) {
								listener.onSend(bundle, applicationContext);
							}
						}
					}
				};
				startTimerRequest();
				Log.d("b","beneran send request");
				sendHttpPostRequest();
			}
		} else if (reqToken == null || reqToken.equals("")) {
			runnable = new Runnable() {

				@Override
				public void run() {
					if (!isRespond) {
						isTimerRespond = true;
						Bundle bundle = generateBundleOnRequestFailed(null);
						bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, applicationContext.getResources().getString(R.string.failed_to_connect));

						if (listener != null) {
							listener.onSend(bundle, applicationContext);
						}
					}
				}
			};
			startTimerRequest();
			reqToken = MTFSharedPreference.getXCSRFToken(applicationContext);
			if (reqToken.equals("")) {
				sendHttpGetRequest();
			} else {
				sendHttpPostRequest();
			}
		} else {
			runnable = new Runnable() {

				@Override
				public void run() {
					if (!isRespond) {
						isTimerRespond = true;
						Bundle bundle = generateBundleOnRequestFailed(null);
						bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, applicationContext.getResources().getString(R.string.failed_to_connect));

						if (listener != null) {
							listener.onSend(bundle, applicationContext);
						}
					}
				}
			};
			startTimerRequest();
			sendHttpPostRequest();
		}
	}

	private void sendHttpGetRequest() {
		try {
			BaseRequest getrequest = new BaseRequest();
			int apiVersion = Build.VERSION.SDK_INT;
			if (apiVersion > Build.VERSION_CODES.KITKAT_WATCH) {
				conMan = new MTFConnectionManager(applicationContext);
			} else {
				MTFSMPUtilities.initializeRequestManager(applicationContext);
			}
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("X-SMP-APPCID", MTFSMPUtilities.appConnId);
			headers.put("X-CSRF-Token", "FETCH");
			headers.put("Content-Type", "application/json");
			headers.put("X-Requested-With", "XMLHttpRequest");
			headers.put("Accept", "application/json");

			String url = getGetUrl();

			getrequest.setRequestUrl(url);
			getrequest.setRequestMethod(BaseRequest.REQUEST_METHOD_GET);
			getrequest.setHeaders(headers);
			getrequest.setListener(inetListenerGet);

			if (apiVersion > Build.VERSION_CODES.KITKAT_WATCH) {
				MTFConnectionManager.requestManager.makeRequest(getrequest);
			} else {
				MTFSMPUtilities.reqMan.makeRequest(getrequest);
			}
		} catch (Exception e) {

			if (listener != null) {

				Bundle bundle = generateBundleOnRequestFailed(null);
				bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, "Ada masalah Koneksi");//Connection Error when send request

				listener.onSend(bundle, applicationContext);
			}
		}
	}

	private void sendHttpPostRequest() {
		try {
			BaseRequest getrequest = new BaseRequest();

			int apiVersion = Build.VERSION.SDK_INT;
			if (apiVersion > Build.VERSION_CODES.KITKAT_WATCH) {
				conMan = new MTFConnectionManager(applicationContext);
			} else {
				MTFSMPUtilities.initializeRequestManager(applicationContext);
			}

			Map<String, String> headers = new HashMap<String, String>();
			headers.put("X-SMP-APPCID", MTFSMPUtilities.appConnId);
			headers.put("X-CSRF-Token", reqToken);
			if (getAddtionalURL().startsWith("clientlogs")) {
				headers.put("Content-Type", "multipart/form-data");
			} else {
				headers.put("Content-Type", "application/json");
				headers.put("Accept", "application/json");
			}

			String url = getFullUrl();

			getrequest.setRequestUrl(url);
			getrequest.setRequestMethod(BaseRequest.REQUEST_METHOD_POST);
			getrequest.setHeaders(headers);
			getrequest.setData(generateBodyForHttpPost());
			getrequest.setListener(inetListener);

			if (!isCanceled) {
				if (apiVersion > Build.VERSION_CODES.KITKAT_WATCH) {
					Log.d("b","makeRequest 1");
					MTFConnectionManager.requestManager.makeRequest(getrequest);
				} else {
					Log.d("b","makeRequest 2");
					MTFSMPUtilities.reqMan.makeRequest(getrequest);
				}
			}
		} catch (Exception e) {

			if (listener != null) {

				Bundle bundle = generateBundleOnRequestFailed(null);
				bundle.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, "Ada masalah koneksi");//Connection Error when send request

				listener.onSend(bundle, applicationContext);
			}
		}
	}

	private String parseSuccessResponse(IResponse response) {
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


		} catch (Exception e) {


		}

		if (responseString.equals("")) {
			return null;
		}

		return responseString;
	}

	private String parseErrorResponse(IResponse response) {
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

	protected String generateSignature(String relativeUrl, String reqBodyString, String timestamp) {
		try {
			StringBuilder sb = new StringBuilder();
				sb.append("POST").append(MTFConstants.COLON).append(relativeUrl).append(MTFConstants.COLON);
				sb.append(MTFConstants.BTPN_API_KEY_VALUE).append(MTFConstants.COLON).append(timestamp);
				sb.append(MTFConstants.COLON).append(reqBodyString);
			String sbTrim = sb.toString().replaceAll("\\s", "");
			Mac mac = Mac.getInstance(MTFConstants.HMAC_256);
			mac.init(new SecretKeySpec(userModel.getSecret().getBytes(), MTFConstants.HMAC_256));
			byte[] byteResult = mac.doFinal(sbTrim.getBytes());
			return new String(Base64.encodeBase64(byteResult));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	protected Map<String, String> getCustomHeaders(String url, String requestBody) {
		Map<String, String> map = new HashMap<>();
		String timestamp = new SimpleDateFormat(MTFConstants.HMAC_DATE_PATTERN).format(Calendar.getInstance().getTime());
			map.put(MTFJSONKey.BTPN_API_KEY, MTFConstants.BTPN_API_KEY_VALUE);
			map.put(MTFJSONKey.BTPN_TIMESTAMP, timestamp);
			map.put(MTFJSONKey.BTPN_JWT, userModel.getJwt());
			map.put(MTFJSONKey.BTPN_SIGNATURE, generateSignature(relativizeURL(url), requestBody, timestamp));
		return map;
	}

	private static String relativizeURL(String url) {
		try {
			String context = MTFSMPUtilities.requestType+MTFSMPUtilities.host+":"+MTFSMPUtilities.port;
			return url.substring(context.length(), url.length());
		} catch (Exception e) {}
		return "";
	}

	protected abstract Bundle generateBundleOnRequestSuccess(String responseString) throws JSONException;
	protected abstract Bundle generateBundleOnRequestFailed(String responseString);
	protected abstract byte[] generateBodyForHttpPost();
	protected abstract String getAddtionalURL();
	protected abstract String getFullUrl();
	protected abstract String getGetUrl();
	protected abstract boolean isUseFileUploadUrl();
    protected abstract String generateRequest();
    protected abstract String getRestUrl();
}
