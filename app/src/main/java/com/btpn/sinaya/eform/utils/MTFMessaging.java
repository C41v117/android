package com.btpn.sinaya.eform.utils;

import com.google.gson.Gson;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class MTFMessaging {

	private static final String STX = "02";
	private static final String ETX = "03";
	private static final String TAG_RESPONSE_CODE = "39";
	private static final String WHITE_SPACE = "\\s";

	public static final String ENROLL_FINGERPRINT = "80";
	public static final String PERSO_CARD = "81";
	public static final String AKTIVASI = "41";
	public static final String VERIFIKASI = "46";

	public static final String KEY_EDC_RESPONSE_CODE = "respCode";
	public static final String KEY_EDC_RESPONSE_VALUE = "respValue";
	public static final String KEY_EDC_ERROR_MESSAGE = "errorMessage";

	public static void printHashValue(Hashtable<String, String> hashHexData) {
		try {
			for (Map.Entry<String, String> entry : hashHexData.entrySet() ) {
			    String key = entry.getKey();
			    String value = entry.getValue();
			    System.out.println ("TAG = " + key + ", VALUE = " + value);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static byte[] sendMessage(Hashtable<String, String> hashHexData) throws Exception {
		StringBuilder sbMain = new StringBuilder();
		StringBuilder sbData = new StringBuilder();
		String strData = null;
		
		try {
			/* STX */
			sbMain.append(STX);
			//build data
			strData = setData(hashHexData);
			/* LENGTH */
			sbData.append(setLengthHeader(strData.length()));
			sbMain.append(sbData.toString());
			/* DATA */
			sbData.append(strData);
			sbMain.append(strData);
			/* ETX */
			sbMain.append(ETX);
			/* CRC */
			sbMain.append(setLRC(sbData.toString()));

			return hexStringToBytes(sbMain.toString());
		} catch (Exception e) {
			throw e;
		}
		
	}

	public static byte[] hexStringToBytes(String hex) {
		int l = hex.length();
		byte[] data = new byte[l/2];
		for (int i = 0; i < l; i += 2) {
			data[i/2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
					+ Character.digit(hex.charAt(i+1), 16));
		}
		return data;
	}
	
	private static String setLengthHeader(int lenData) throws Exception {
		StringBuilder sb = new StringBuilder();
		String strLenData = null;
		try {
			strLenData = String.format("%0"+(4-Integer.toHexString(lenData / 2).length())+"d%s", 0, Integer.toHexString(lenData / 2).toUpperCase());
			sb.append(strLenData.substring(0,2));
			sb.append(strLenData.substring(2,4));
			return sb.toString();
		} catch (Exception e) {
			throw e;
		}
	}
	
	private static String setData(Hashtable<String, String> hashData) throws Exception {
		StringBuilder sb = new StringBuilder();
		String tmpValue = null;
		
		try {
			for (Map.Entry<String, String> entry : hashData.entrySet() ) {
				/* TAG */
				sb.append(entry.getKey());
				/* LENGTH */
				tmpValue = entry.getValue();
				//padding if length mod 2 != 0
				sb.append(countLengthValue(2, tmpValue));
				/* VALUE */
				for (int i = 0; i < tmpValue.length(); i = i+2) {
					sb.append(tmpValue.substring(i, i+2));
				}
			}
			return sb.toString();
		} catch (Exception e) {
			throw e;
		}
	}

	public static String countLengthValueReq(int padding, String entryValue) {
		int length = 0;
		String prefix = "";
		if (entryValue.length() % 2 == 0) {
			length = entryValue.length() / 2;
		} else {
			entryValue += "0";
			length = entryValue.length() / 2;
		}
		if(length > 128) {
			prefix = "81";
		} else if (length > 255) {
			prefix = "82";
			return prefix + String.format("%04d%s", 0, Integer.toHexString(length)).toUpperCase();
		}
		int paddingMinLength = (padding-Integer.toHexString(length).length());
		if (paddingMinLength < 1) {
			String lengthHex = Integer.toHexString(length).toUpperCase();
			if (prefix != null && prefix != "") {
				return prefix + lengthHex;
			} else {
				return prefix + lengthHex;
			}
		} else {
			String lengthHex = String.format("%0"+paddingMinLength+"d%s", 0, Integer.toHexString(length)).toUpperCase();
			if (prefix != null && prefix != "") {
				return prefix + lengthHex;
			} else {
				return prefix + lengthHex;
			}
		}
	}

	 public static String countLengthValue(int padding, String entryValue) {
		int length = 0;
		if (entryValue.length() % 2 == 0) {
			length = entryValue.length() / 2;
		} else {
			entryValue += "0";
			length = entryValue.length() / 2;
		}
		int paddingMinHex = (padding-Integer.toHexString(length).length());
		if(paddingMinHex == 0) {
			return Integer.toHexString(length).toUpperCase();
		} else if(paddingMinHex < 0) {
			int lengthOfHex = Integer.toHexString(length).length();
			if(lengthOfHex % 2 == 0) {
				return Integer.toHexString(length).toUpperCase();
			} else {
				lengthOfHex++;
				return String.format("%0"+(lengthOfHex-Integer.toHexString(length).length())+"d%s", 0, Integer.toHexString(length)).toUpperCase();
			}
		} else {
			return String.format("%0"+paddingMinHex+"d%s", 0, Integer.toHexString(length)).toUpperCase();
		}
	}
	
	private static String setLRC(String strData) {
		char result = 0;
		try {
			for (int i = 0; i < strData.length(); i=i+2) {
				result = (char) (result ^ (char)Integer.parseInt(strData.substring(i, i + 2), 16));
			}
			return String.format("%02x", (int) result);
		} catch (Exception e) {
			return "";
		}
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String writeValueAsString(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return addWhitespaceBetweenHexChar(new String(hexChars));
	}

	public static String addWhitespaceBetweenHexChar(String input) {
		StringBuilder sb = new StringBuilder();
		boolean isEven = false;
		for(Character c : input.toCharArray()) {
			sb.append(c);
			if (isEven) {
				sb.append(" ");
			}
			isEven = !isEven;
		}
		return sb.toString();
	}

	public static Map<String, String> parseMessage(String responseString, String operation, Boolean isTwoBytes) throws Exception {
		Map<String, String> resultMap = new HashMap<>();
		try {
			checkingProcess: {
				String[] respArrays = responseString.split(WHITE_SPACE);
				//check whether response format is started & ended with specific hexvalue that has been defined before
				if (!respArrays[0].equals(STX)) {
					throw new Exception("response isn't started with " + STX);
				} else {
					int lengthOfResponse = 0;
					lengthOfResponse = convertStringToDecimal(respArrays[1] + respArrays[2]);
					if (!respArrays[lengthOfResponse + 3].equals(ETX)) {
						throw new Exception("response isn't ended with " + ETX);
					}
					respArrays = Arrays.copyOfRange(respArrays, 3, (lengthOfResponse + 3));
				}

				//check whether command code of the response is equals with request (if valid getting response code)
				if (!respArrays[0].equals(operation)) {
					throw new Exception("response's command isn't same with request");
				} else {
					int lengthOfResponse = 0;
					if(isTwoBytes) {
						lengthOfResponse = convertHexToDecimal(respArrays[1] + respArrays[2]);
						respArrays = Arrays.copyOfRange(respArrays, 3, (lengthOfResponse + 3));
					} else {
						lengthOfResponse = convertHexToDecimal(respArrays[1]);
						respArrays = Arrays.copyOfRange(respArrays, 2, (lengthOfResponse + 2));
					}
				}

				//check response code
				if (!respArrays[0].equals(TAG_RESPONSE_CODE)) {
					throw new Exception("response code tag is not found");
				} else {
					int rcLength = 0;
					rcLength = convertHexToDecimal(respArrays[1]);
					if (!resolveResponseCode(respArrays, rcLength).equals(MTFConstants.EDC_RESP_APPROVED)) {
						resultMap.put(KEY_EDC_RESPONSE_CODE, resolveResponseCode(respArrays, rcLength));
						resultMap.put(KEY_EDC_ERROR_MESSAGE, MTFConstants.EDC_RESPONSE_CODE_MAP.get(resolveResponseCode(respArrays, rcLength)));
						break checkingProcess;
					}
					respArrays = Arrays.copyOfRange(respArrays, 3, respArrays.length);
				}
				resultMap.put(KEY_EDC_RESPONSE_VALUE, extractResponse(respArrays, operation));
				resultMap.put(KEY_EDC_RESPONSE_CODE, MTFConstants.EDC_RESP_APPROVED);
			}
		} catch (Exception e) {
			resultMap.put(KEY_EDC_RESPONSE_CODE, "99");
			resultMap.put(KEY_EDC_ERROR_MESSAGE, e.getMessage());
		}
		return resultMap;
	}

	private static String extractResponse(String[] respArrays, String operation) throws Exception {
		switch(operation) {
			case ENROLL_FINGERPRINT:
				return extractEnrollFingerprint(respArrays);
			case PERSO_CARD:
				return extractPersoCard(respArrays);
			case AKTIVASI:
				return extractActivation(respArrays);
			default:
				return "{\"value\": \"error\"}";
		}
	}

	private static String extractPersoCard(String[] respArrays) throws Exception {
		//dihandle di response code
        return "";
	}

	private static String extractActivation(String[] respArrays) throws Exception {
		Map<String, String> resultMap = new HashMap<>();
		//pan value
		if(!respArrays[0].equals(MTFConstants.TAG_PAN_VALUE)) {
			throw new Exception("pan value tag is not found");
		} else {
			String panValue = null;
			int length = convertHexToDecimal(respArrays[1]);
			panValue = byteArrayToByteString(Arrays.copyOfRange(respArrays, 2, length+2));
			respArrays = Arrays.copyOfRange(respArrays, length + 2, respArrays.length);
			resultMap.put(MTFConstants.TAG_PAN_VALUE, new String(hexStringToBytes(panValue.replaceAll(WHITE_SPACE, "")), StandardCharsets.ISO_8859_1));
		}

		//old pin
		if(!respArrays[0].equals(MTFConstants.TAG_OLD_PIN)) {
			throw new Exception("old pin tag is not found");
		} else {
			String oldPin = null;
			int length = convertHexToDecimal(respArrays[1]);
			oldPin = byteArrayToByteString(Arrays.copyOfRange(respArrays, 2, length+2));
			respArrays = Arrays.copyOfRange(respArrays, length + 2, respArrays.length);
//			resultMap.put(MTFConstants.TAG_OLD_PIN, new String(hexStringToBytes(oldPin.replaceAll(WHITE_SPACE, "")), StandardCharsets.ISO_8859_1));
            resultMap.put(MTFConstants.TAG_OLD_PIN, oldPin.replaceAll(WHITE_SPACE, ""));
        }

		//new pin
		if(!respArrays[0].equals(MTFConstants.TAG_NEW_PIN)) {
			throw new Exception("new pin tag is not found");
		} else {
			String newPin = null;
			int length = convertHexToDecimal(respArrays[1]);
			newPin = byteArrayToByteString(Arrays.copyOfRange(respArrays, 2, length + 2));
			respArrays = Arrays.copyOfRange(respArrays, length + 2, respArrays.length);
//			resultMap.put(MTFConstants.TAG_NEW_PIN, new String(hexStringToBytes(newPin.replaceAll(WHITE_SPACE, "")), StandardCharsets.ISO_8859_1));
            resultMap.put(MTFConstants.TAG_NEW_PIN, newPin.replaceAll(WHITE_SPACE, ""));
		}

		return new Gson().toJson(resultMap);
	}

	private static String extractEnrollFingerprint(String[] respArrays) throws Exception {
		Map<String, String> resultMap = new HashMap<>();
		//first finger
		if(!respArrays[0].equals(MTFConstants.TAG_FINGER_DATA_1)) {
			throw new Exception("first finger tag is not found");
		} else {
			String finger1 = null; int length = 0;
			if(respArrays[1].equals(MTFConstants.TAG_LEN_GT_128)) {
				length = convertHexToDecimal(respArrays[2]);
				finger1 = byteArrayToByteString(Arrays.copyOfRange(respArrays, 3, length+3));
				respArrays = Arrays.copyOfRange(respArrays, length+3, respArrays.length);
			} else if (respArrays[1].equals(MTFConstants.TAG_LEN_GT_255)) {
				length = convertHexToDecimal(respArrays[2]+respArrays[3]);
				finger1 = byteArrayToByteString(Arrays.copyOfRange(respArrays, 4, length+4));
				respArrays = Arrays.copyOfRange(respArrays, length+4, respArrays.length);
			} else {
				length = convertHexToDecimal(respArrays[1]);
				finger1 = byteArrayToByteString(Arrays.copyOfRange(respArrays, 2, length+2));
				respArrays = Arrays.copyOfRange(respArrays, length+2, respArrays.length);
			}
			resultMap.put(MTFConstants.TAG_FINGER_DATA_1, encodeToBase64Format(finger1));
		}

		//second finger
		if(!respArrays[0].equals(MTFConstants.TAG_FINGER_DATA_2)) {
			throw new Exception("second finger tag is not found");
		} else {
			String finger2 = null; int length = 0;
			if(respArrays[1].equals(MTFConstants.TAG_LEN_GT_128)) {
				length = convertHexToDecimal(respArrays[2]);
				finger2 = byteArrayToByteString(Arrays.copyOfRange(respArrays, 3, length+3));
				respArrays = Arrays.copyOfRange(respArrays, length+3, respArrays.length);
			} else if (respArrays[1].equals(MTFConstants.TAG_LEN_GT_255)) {
				length = convertHexToDecimal(respArrays[2]+respArrays[3]);
				finger2 = byteArrayToByteString(Arrays.copyOfRange(respArrays, 4, length+4));
				respArrays = Arrays.copyOfRange(respArrays, length+4, respArrays.length);
			} else {
				length = convertHexToDecimal(respArrays[1]);
				finger2 = byteArrayToByteString(Arrays.copyOfRange(respArrays, 2, length+2));
				respArrays = Arrays.copyOfRange(respArrays, length+2, respArrays.length);
			}
			resultMap.put(MTFConstants.TAG_FINGER_DATA_2, encodeToBase64Format(finger2));
		}

		//score finger 1
		if(!respArrays[0].equals(MTFConstants.TAG_FINGER_SCORING_1)) {
			throw new Exception("first index finger tag is not found");
		} else {
			String indexFinger = "";
			int length = convertHexToDecimal(respArrays[1]);
			for (int i=0;i<length;i++) {
				indexFinger += respArrays[i+2];
			}
			respArrays = Arrays.copyOfRange(respArrays, length+2, respArrays.length);
			resultMap.put(MTFConstants.TAG_FINGER_SCORING_1, indexFinger);
		}

		//score finger 2
		if(!respArrays[0].equals(MTFConstants.TAG_FINGER_SCORING_2)) {
			throw new Exception("second index finger tag is not found");
		} else {
			String indexFinger = "";
			int length = convertHexToDecimal(respArrays[1]);
			for (int i=0;i<length;i++) {
				indexFinger += respArrays[i+2];
			}
			respArrays = Arrays.copyOfRange(respArrays, length+2, respArrays.length);
			resultMap.put(MTFConstants.TAG_FINGER_SCORING_2, indexFinger);
		}
		return new Gson().toJson(resultMap);
	}

	private static int convertStringToDecimal(String decimalString) {
		if(decimalString != null && !decimalString.equals("")) {
			return Integer.parseInt(decimalString);
		}
		return 0;
	}

	private static int convertHexToDecimal(String hxValue) {
		if(hxValue != null) {
			return Integer.parseInt(hxValue, 16);
		}
		return 0;
	}

	private static String resolveResponseCode(String[] respArrays, int length) {
		int startIdx = 2;
		String responseCode = "";
		for(int i=0;i<length;i++) {
			responseCode += respArrays[startIdx+i];
		}
		return responseCode;
	}

	public static String byteArrayToByteString(String[] params) {
		StringBuilder sb = new StringBuilder();boolean isFirstIndex = true;
		for(String s : params) {
			if(isFirstIndex) {
				sb.append(s);
				isFirstIndex = false;
			} else {
				sb.append(" "+s);
			}
		}
		return sb.toString();
	}

	public static String encodeToBase64Format(String input) {
		String toByteString = new String(hexStringToBytes(input.replaceAll(WHITE_SPACE, "")), StandardCharsets.ISO_8859_1);
		return new String(Base64.encodeBase64(toByteString.getBytes(StandardCharsets.ISO_8859_1)));
	}

	public static String decodeBase64(String input) {
		return new String(Base64.decodeBase64(input.getBytes()), StandardCharsets.ISO_8859_1);
	}

}
