package com.btpn.sinaya.eform.utils;

import android.bluetooth.BluetoothAdapter;
import android.location.LocationManager;
import android.util.Log;

import com.btpn.sinaya.eform.connection.MTFSMPUtilities;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.X509TrustManager;

public class MTFHttpPostRest {
	
	/*Cara pakai :
	 * String result;
	 * List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("noLkp", noLKP));
		
		result = Tool.getServerResponse(Global.URL_GET_DETAIL_LKP, param);
	 */
	
	
	public static final String HEADER_CONTENT_TYPE_URL_ENCODED = "application/json";
	public static final String HEADER_CONTENT_TYPE_KEY = "Content-Type";
    public static final String HEADER_DTP_KEY = "X-DTPkey";
    public static final String HEADER_DTP_KEY_VALUE = "7Y3G39q/dCxXsH4/ShsPdOESnHotJF4ugVa+8h45YHw=";
	public static final String HEADER_CONTENT_LENGTH = "Content-Length";
	public static final String POST_METHOD = "POST";
    public static final String GET_METHOD = "GET";
	
	public static StringBuilder inputStreamToString(InputStream is) throws IOException {
		String line = null;
		StringBuilder result = new StringBuilder();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		while ((line = br.readLine()) != null)
			result.append(line);
		
		return result;
	}
	
	public static String getServerResponse(String postURI, List<NameValuePair> params) throws IOException {
		String result = null;

		HttpParams paramsClient = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(paramsClient, 2 * (MTFSystemParams.mobileTimeOut/1000/60));
		HttpConnectionParams.setSoTimeout(paramsClient, 1 * (MTFSystemParams.mobileTimeOut/1000/60));

		HttpClient client = new DefaultHttpClient(paramsClient);
		HttpPost post = new HttpPost(postURI);
		post.setHeader(HEADER_CONTENT_TYPE_KEY, HEADER_CONTENT_TYPE_URL_ENCODED);
		UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);

//		if (Global.ENCRYPT_COMM) {
//			InputStream is = ent.getContent();
//			String queryString = Tool.inputStreamToString(is).toString();
//			byte[] encQueryString = SAKFormatter.cipherData(queryString);
//			ByteArrayInputStream bis = new ByteArrayInputStream(encQueryString);
//			InputStreamEntity isEnt = new InputStreamEntity(bis, encQueryString.length);
//			post.setEntity(isEnt);
//		}
//		else {
		post.setEntity(ent);
//		}

		HttpResponse responsePost = client.execute(post);

		HttpEntity respEnt = responsePost.getEntity();
		int statusCode = responsePost.getStatusLine().getStatusCode();

		if (statusCode == HttpStatus.SC_OK) {
			InputStream content = respEnt.getContent();
			result = MTFHttpPostRest.inputStreamToString(content).toString();
//			if(Global.DECRYPT_COMM) {
//				result = SAKFormatter.decipherData(result);
//			}
		}
		else {
			throw new IOException("Connection to server failed: " + statusCode + " "
					+ responsePost.getStatusLine().getReasonPhrase());
		}

		return result;
	}

	public static String submitDataEn(String postURI, String data) throws Exception {
		byte[] bytes = null;
//		data = data + Global.appName;
//		if (Global.ENCRYPT_COMM) {
//			bytes = SAKFormatter.cipherData(data);
//		}
//		else {
			bytes = data.getBytes();
//		}

	    DefaultHttpClient httpclient = new DefaultHttpClient();

	    //url with the post data
	    HttpPost httpost = new HttpPost(postURI);

	    //passes the results to a string builder/entity
	    StringEntity se = new StringEntity(new String(bytes));

	    //sets the post request as the resulting string
	    httpost.setEntity(se);
	    //sets a request header so the page receving the request
	    //will know what to do with it
	    httpost.setHeader("Accept", "application/json");
	    httpost.setHeader("Content-type", "application/json");

	    //Handles what is returned from the page
	    ResponseHandler responseHandler = new BasicResponseHandler();
	    return httpclient.execute(httpost, responseHandler).toString();
	}

	public static String submitData2(String postURI, String data) throws Exception {

	    DefaultHttpClient httpclient = new DefaultHttpClient();

	    //url with the post data
	    HttpPost httpost = new HttpPost(postURI);

	    //passes the results to a string builder/entity
	    StringEntity se = new StringEntity(data);

	    //sets the post request as the resulting string
	    httpost.setEntity(se);
	    //sets a request header so the page receving the request
	    //will know what to do with it
	    httpost.setHeader("Accept", "application/json");
	    httpost.setHeader("Content-type", "application/json");

	    //Handles what is returned from the page
	    ResponseHandler responseHandler = new BasicResponseHandler();
	    return httpclient.execute(httpost, responseHandler).toString();
	}

    public static String getDataHttp(String postURI, Map<String, String> customHeader) throws Exception {
        String result = null;

        HttpURLConnection httpConn = null;
        try {
            if(MTFConstants.isUseGateway && postURI.startsWith("https")) {
                httpConn = startPinning(postURI, "GET");
            } else {
                URL url = new URL(postURI);
                httpConn = (HttpURLConnection) url.openConnection();
            }
            httpConn.setRequestProperty(HEADER_CONTENT_TYPE_KEY, HEADER_CONTENT_TYPE_URL_ENCODED);
            if (customHeader != null) {
                for(String key : customHeader.keySet()) {
                    httpConn.setRequestProperty(key, customHeader.get(key));
                }
            }

            int responseCode = httpConn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream content = httpConn.getInputStream();
                result = MTFHttpPostRest.inputStreamToString(content).toString();
            }
            else {
                throw new IOException("Connection to server failed: " + responseCode + " "
                        + httpConn.getResponseMessage());
            }
        }
        finally {
            if (httpConn != null)
                httpConn.disconnect();
        }

        return result;
    }

	public static String getData(String postURI) throws Exception {
		return getData(postURI, null);
	}

    public static String getData(String postURI, Map<String, String> customHeader) throws Exception {
        String result = null;

        HttpURLConnection httpConn = null;
        try {
			if(MTFConstants.isUseGateway && postURI.startsWith("https")) {
				httpConn = startPinning(postURI, "GET");
			} else {
				URL url = new URL(postURI);
				httpConn = (HttpURLConnection) url.openConnection();
			}
            httpConn.setRequestProperty(HEADER_CONTENT_TYPE_KEY, HEADER_CONTENT_TYPE_URL_ENCODED);
			if (customHeader != null) {
				for(String key : customHeader.keySet()) {
					httpConn.setRequestProperty(key, customHeader.get(key));
				}
			}

            int responseCode = httpConn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream content = httpConn.getInputStream();
                result = MTFHttpPostRest.inputStreamToString(content).toString();
            }
            else {
                throw new IOException("Connection to server failed: " + responseCode + " "
                        + httpConn.getResponseMessage());
            }
        }
        finally {
            if (httpConn != null)
                httpConn.disconnect();
        }

        return result;
    }

	private static HttpsURLConnection startPinning(String postURI, String httpMethod) throws Exception {
		HttpsURLConnection httpConn = null;
		X509TrustManager easyTrustManager = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				System.out.println("here");
			}
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				boolean isValid = false;
				for (int i = 0; i < chain.length; i++) {
					RSAPublicKey pubkey = (RSAPublicKey) chain[i].getPublicKey();
					String encoded = new BigInteger(1, pubkey.getEncoded()).toString(16);
					System.out.println(encoded);
					if (MTFConstants.SSL_PUBLIC_KEY.equalsIgnoreCase(encoded)) {
						isValid = true;
						break;
					}
				}
				if(!isValid) {
					throw new IllegalArgumentException("public key invalid");
				}
			}
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
		};
		try {
			SSLContext sc = SSLContext.getInstance("TLSv1");
			httpConn = makeAConnection(postURI, httpMethod);
			sc.init(null, new X509TrustManager[]{easyTrustManager}, new java.security.SecureRandom());
			httpConn.setDefaultSSLSocketFactory(sc.getSocketFactory());
			if(!MTFSMPUtilities.isHandShake) {
				httpConn.connect();
                MTFSMPUtilities.isHandShake = true;
                SSLContext sc1 = SSLContext.getInstance("TLSv1");
                httpConn = makeAConnection(postURI, httpMethod);
                sc1.init(null, new X509TrustManager[]{easyTrustManager}, new java.security.SecureRandom());
                httpConn.setDefaultSSLSocketFactory(sc1.getSocketFactory());
			}
			return httpConn;
		} catch (SSLHandshakeException ex) {
			httpConn.disconnect();
			if(!MTFSMPUtilities.isHandShake) {
				MTFSMPUtilities.isHandShake = true;
				SSLContext sc = SSLContext.getInstance("TLSv1");
				httpConn = makeAConnection(postURI, httpMethod);
				sc.init(null, new X509TrustManager[]{easyTrustManager}, new java.security.SecureRandom());
				httpConn.setDefaultSSLSocketFactory(sc.getSocketFactory());
				return httpConn;
			} else {
				throw ex;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private static HttpsURLConnection makeAConnection(String postURI, String httpMethod) throws Exception {
		URL url = new URL(postURI);
		URLConnection conn = url.openConnection();
		if(httpMethod != null && httpMethod.equalsIgnoreCase("POST")) {
			conn.setDoInput(true);
			conn.setDoOutput(true);
		}
		HttpsURLConnection httpConn = (HttpsURLConnection) conn;
		return httpConn;
	}

	private static void extractResponseHeader(HttpURLConnection httpConn) {
		try {
			if(httpConn instanceof URLConnection) {
				MTFSMPUtilities.JWT = httpConn.getHeaderField("BTPN-JWT");
				//MTFSMPUtilities.KEY = httpConn.getHeaderField("BTPN-ApiKey");
			}
		} catch (Exception e) {
			Log.e("error extract response", e.getMessage(), e);
		}
	}

	public static String postData(String postURI, String data, Map<String, String> customHeader) throws Exception {
		String result = null;
		byte[] bytesOfData = data.getBytes();
		String contentLength = String.valueOf(bytesOfData.length);
		HttpURLConnection httpConn = null;
		try {
			if(MTFConstants.isUseGateway && postURI.toString().startsWith("https")) {
				httpConn = startPinning(postURI, "POST");
			} else {
				URL url = new URL(postURI);
				URLConnection conn = url.openConnection();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				httpConn = (HttpURLConnection) conn;
                httpConn.setRequestProperty(HEADER_DTP_KEY, HEADER_DTP_KEY_VALUE);
			}
			httpConn.setRequestMethod(POST_METHOD);
			httpConn.setRequestProperty(HEADER_CONTENT_TYPE_KEY, HEADER_CONTENT_TYPE_URL_ENCODED);
			httpConn.setRequestProperty(HEADER_CONTENT_LENGTH, contentLength);
			if (MTFConstants.isUseGateway && customHeader != null) {
				for(String key : customHeader.keySet()) {
					httpConn.setRequestProperty(key, customHeader.get(key));
				}
			}
			httpConn.setConnectTimeout(MTFSystemParams.mobileTimeOut);
			httpConn.setReadTimeout(MTFSystemParams.mobileTimeOut);
			httpConn.connect();

			DataOutputStream dos = null;

			OutputStream outputStream = httpConn.getOutputStream();
			dos = new DataOutputStream(outputStream);
			dos.write(bytesOfData);
			dos.flush();
			dos.close();

			int responseCode = httpConn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream content = httpConn.getInputStream();
				result = MTFHttpPostRest.inputStreamToString(content).toString();
				httpConn.getHeaderFields();
				if(MTFConstants.isUseGateway && postURI.toString().contains(MTFSMPUtilities.appIdLoginRest)) {
					extractResponseHeader(httpConn);
				}
			}
			else {
				throw new IOException("Connection to server failed: " + responseCode + " "
						+ httpConn.getResponseMessage());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (httpConn != null)
				httpConn.disconnect();
		}
		return result;
	}

	public static String postData(String postURI, String data) throws Exception {
		return postData(postURI, data, null);
	}
	
	public static String md5(String s) {
	    try {
	        // Create MD5 Hash
	        MessageDigest digest = MessageDigest.getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();
	        
	        // Create Hex String
	        String hex = null;
	        StringBuilder hexString = new StringBuilder();
	        for (int i=0; i < messageDigest.length; i++) {
	        	hex = Integer.toHexString(0xFF & messageDigest[i]);
	        	if (hex.length()==1)
	        		hexString.append('0');
	        	hexString.append(hex);
	            
	        }
	        return hexString.toString();
	        
	    }
	    catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
	
	public static String[] split(String original, String delimeter) {
		List<String> nodes = splitToVector(original, delimeter);

		String[] result = new String[nodes.size()];
		if (nodes.size() > 0) {
			for (int loop = 0; loop < nodes.size(); loop++) {
				result[loop] = (String) nodes.get(loop);
			}
		}

		return result;
	}

	public static List<String> splitToVector(String original, String delimeter) {
		List<String> nodes = new ArrayList<String>();
		int index = original.indexOf(delimeter);

		while (index >= 0) {
			nodes.add(original.substring(0, index));
			original = original.substring(index + delimeter.length());
			index = original.indexOf(delimeter);
		}

		nodes.add(original);

		return nodes;
	}

	/**
	 * Explode string <i>str</i> with specified delimiter to array of string
	 * 
	 * @param str String
	 * @param delimiter String
	 * @return String[]
	 */
	public static String[] explode(String str, String delimiter) {
		StringTokenizer st = new StringTokenizer(str, delimiter);
		String[] result = new String[st.countTokens()];
		for (int i = 0; i < result.length; i++)
			result[i] = st.nextToken();
		return result;
	}

	/**
	 * Convert array of string to string with specified delimiter
	 * 
	 * @param arr String[] Ex: new String[] {"VAL1", "VAL2", "VAL3"}
	 * @param delimiter String
	 * @return String Ex: "VAL1,VAL2,VAL3"
	 */
	public static String implode(String[] arr, String delimiter) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			if (i > 0)
				result.append(delimiter);
			result.append(arr[i]);
		}
		return result.toString();
	}
	
	public static boolean gpsEnabled(LocationManager lm) {
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){  
		    return true;  
		}
		else {
			return false;
		}
	}
	
	public static boolean hasBluetooth() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
		    // Device does not support Bluetooth
			return false;
		}
		return true;
	}

	/**
	 * Get current system date time
	 * 
	 * @return Date
	 */
	public static Date getSystemDateTime() {
		return new Date(System.currentTimeMillis());
	}

	/**
	 * Get current system date (time part is removed/set to zero)
	 * 
	 * @return Date
	 */
	public static Date getSystemDate() {
		return truncTime(getSystemDateTime());
	}

	/**
	 * Truncate time part (hour, minute, seconds, millis) from date
	 * 
	 * @param date Date
	 * @return Date
	 */
	public static Date truncTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	public static String appendZeroForDateTime(int i, boolean plusOne) {
		String result = null;
		if (i < 10) {
			if (plusOne)
				i++;
			result = "0" + i;
		}
		else {
			if (plusOne)
				i++;
			result = String.valueOf(i);
		}
		return result;
	}
	
	public static int dpToPixel(float scale, int dps) {
		return (int) (dps * scale + 0.5f);
	}
	
	public static boolean isEmptyString(String string) {
		if (string == null || "".equals(string.trim()))
			return true;
		else
			return false;
	}

	/**
	 * Method to check whether the specified argument is parsable to Integer
	 * 
	 * @param num
	 *            String
	 * @return boolean
	 */
	public static boolean isInteger(String num) {
		boolean result = false;
		try {
		/*//	taskId.indexOf(Global.RS_NEW_ORDER) > -1
			if(num.indexOf("N~")>-1){
				result = false;
			}else{
				result = true;
			}*/
			
			Integer.parseInt(num);
			result = true;
		}
		catch (Exception e) {
			result = false;
		}
		return result;
	}
	
	public static boolean isTask(String num) {
		boolean result = false;
		try {
		//	taskId.indexOf(Global.RS_NEW_ORDER) > -1
			if(num.indexOf("N~")>-1){
				result = false;
			}else{
				result = true;
			}
			
		//	Integer.parseInt(num);
		}
		catch (Exception e) {
			result = false;
		}
		return result;
	}
	
	public static double getAspectRatio(int width, int height) {
		return width / height;
	}
	
	/*public static String getErrorCode() {
		
		System.out.println("***getErrorCode");
		
		Date date = new Date();
		String errorCode = Reader.getKeyDate(date);
		try {
			String keyId = ApplicationBean.getInstance().getUserId();
			System.out.println("keyId= " + keyId);
			char keyId2 = keyId.charAt(keyId.length()-2);
			errorCode = errorCode + keyId2;
		} catch (Exception e) {
			System.out.println("error getErrorCode");
		}
		System.out.println("getErrorCode errorCode= " + errorCode);
		return errorCode;
	}*/
	
	
}