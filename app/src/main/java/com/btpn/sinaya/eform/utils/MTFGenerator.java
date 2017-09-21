package com.btpn.sinaya.eform.utils;


import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.btpn.sinaya.eform.model.MTFMasterDataFormContentModel;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MTFGenerator {
	
//	private static final String BANANA = "$1dsgG35@3t96!@12";
	
	private static final String BANANA = "1371298371927391";
	public static final String seedValue = "THIS IS SPARTA";
	
	public static int generatePixelsToDip(Context context, float dipValue) {
	    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
	    return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
	}

	public static int getIndexFromListOfString(List<String> listItems, String key){
		for (int i = 0; i < listItems.size(); i++) {
			if(listItems.get(i).equalsIgnoreCase(key))
				return i;
		}
		
		return -1; 
	}
	
	public static int getIndexFromListOfModel(List<MTFMasterDataFormContentModel> listItems, String key){
		for (int i = 0; i < listItems.size(); i++) {
			if(listItems.get(i).getTitle().equals(key))
				return i;
		}
		
		return -1; 
	}
	
	public static int getIndexFromListOfModelBasedOnCode(List<MTFMasterDataFormContentModel> listItems, String code){
		for (int i = 0; i < listItems.size(); i++) {
			if(listItems.get(i).getCode().equals(code))
				return i;
		}
		
		return -1; 
	}
	
	public static int getIndexFromListOfModelBasedOnTitle(List<MTFMasterDataFormContentModel> listItems, String title){
		for (int i = 0; i < listItems.size(); i++) {
			if(listItems.get(i).getTitle().equalsIgnoreCase(title))
				return i;
		}
		
		return -1; 
	}
	
	public static String getCodeFromListOfModelBasedForOthers(List<MTFMasterDataFormContentModel> listItems){
		for (int i = 0; i < listItems.size(); i++) {
			if(listItems.get(i).getTitle().equalsIgnoreCase("others")||
					listItems.get(i).getTitle().equalsIgnoreCase("lainnya"))
				return listItems.get(i).getCode();
		}
		
		return ""; 
	}
	
	@SuppressWarnings("deprecation")
	public static boolean isSDCardHaveMoreSpace(){
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		long bytesAvailable = (long)stat.getFreeBlocks()*stat.getBlockSize();
		long megAvailable = bytesAvailable / 1048576;
		
		if (megAvailable > MTFSystemParams.minimumStorageMobile) {
			return true;
		}
		
		return false;
	}
	
	public static long getDiffTimeToMidNight(){
		Calendar cal = Calendar.getInstance();
		long start = cal.getTimeInMillis();
		
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return (cal.getTimeInMillis() - start);
	}
	
	public static boolean isGreaterThanToday(Date date, int difDay){
		Calendar calCurrent = Calendar.getInstance();
		calCurrent.set(Calendar.HOUR_OF_DAY, 0);
		calCurrent.set(Calendar.MINUTE, 0);
		calCurrent.set(Calendar.SECOND, 1);
		
		Calendar reqCal = Calendar.getInstance();
		reqCal.setTime(date);
		reqCal.add(Calendar.DAY_OF_MONTH, difDay);
		reqCal.set(Calendar.HOUR_OF_DAY, 0);
		reqCal.set(Calendar.MINUTE, 0);
		reqCal.set(Calendar.SECOND, 1);
		
		if (reqCal.getTimeInMillis() >= calCurrent.getTimeInMillis()) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isDataGreaterThanToday(Date date, int difDay){
		Calendar calCurrent = Calendar.getInstance();
		calCurrent.set(Calendar.HOUR_OF_DAY, 0);
		calCurrent.set(Calendar.MINUTE, 0);
		calCurrent.set(Calendar.SECOND, 1);
		
		Calendar reqCal = Calendar.getInstance();
		reqCal.setTime(date);
		reqCal.add(Calendar.DAY_OF_MONTH, difDay);
		reqCal.set(Calendar.HOUR_OF_DAY, 0);
		reqCal.set(Calendar.MINUTE, 0);
		reqCal.set(Calendar.SECOND, 1);
		
		if (reqCal.getTimeInMillis() <= calCurrent.getTimeInMillis()) {
			return true;
		}
		
		return false;
	}
	
	public static String generateHash(String data){
		//Create Some Logic for Generator
		String hashPassword = new String(Hex.encodeHex(DigestUtils.sha(data+BANANA)));
		return hashPassword;
	}
	
	public static String convertByteToString(byte[] data){
		StringBuilder dataStr = new StringBuilder();
    	for (int i = 0; i < data.length; i++) {
			char c = (char)data[i];
			dataStr.append(c);
		}
    	
    	return dataStr.toString();
	}
	
	public static byte[] convertStringToByte(String data){
		byte[] result = new byte[data.length()];
		for (int i = 0; i < data.length(); i++) {
			char c = data.charAt(i);
			result[i] = (byte)c;
		}
		
		return result;
	}
	
	public static boolean isConnectionAvailable(Context context){
		ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		if (i == null) {
			return false;
		} 
		
		if (!i.isConnected()) {
		    return false;
		}
		
		if (!i.isAvailable()){
		    return false;
		}
		
		return true;
	}

	public static byte[] getChunk(Context context, String filename, int part){
		ContextWrapper cw = new ContextWrapper(context);
		File fileDir = cw.getDir(MTFImageHelper.folderName, Context.MODE_PRIVATE);
		File file = new File(fileDir, filename);
		int chunkSize = MTFSystemParams.maxChunkSize * 1024;
		byte[] result = new byte[chunkSize];

		try {
			if (file.exists()) {
				RandomAccessFile f = new RandomAccessFile(file, "r");
				f.seek((part-1)*chunkSize + MTFSystemParams.secureImageSize);
				int realSize = f.read(result);
				f.close();

				if (realSize != chunkSize) {
					byte[] newResult = new byte[realSize];
					for (int i = 0; i < realSize; i++) {
						newResult[i] = result[i];
					}
					return newResult;
				}else{
					return result;
				}
			}
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			
		}
		
		return null;
	}
	
	public static int getCurrentPart(String successParts, int totalPart){
		if (!successParts.equals("")) {
			String[] splitString = successParts.split(",");
			for (int i = 1; i <= totalPart; i++) {
				boolean found = false;
				for (int j = 0; j < splitString.length; j++) {
					if(splitString[j].equals(i+"")){
						found = true;
					}
				}
				
				if (!found) {
					return i;
				}
			}
		}
		
		return 1;
	}
	
	public static int getTotalPart(Context context, String filename){
		ContextWrapper cw = new ContextWrapper(context);
		File fileDir = cw.getDir(MTFImageHelper.folderName, Context.MODE_PRIVATE);
		File file = new File(fileDir, filename);
		int totalPart = (int)((file.length() - MTFSystemParams.secureImageSize) / (MTFSystemParams.maxChunkSize * 1024));
		if (file.length() % (MTFSystemParams.maxChunkSize * 1024) != 0) {
			totalPart++;
		}
		return totalPart;
	}
	
	public static void deleteFile(Context context, String filename){
		if (filename == null) {
			return;
		}
		
		ContextWrapper cw = new ContextWrapper(context);
		File fileDir = cw.getDir(MTFImageHelper.folderName, Context.MODE_PRIVATE);
		File file = new File(fileDir, filename);
		
		if (file.exists()) {
			file.delete();
		}
	}
	
	public static void resetFolderForTempFile(Context context){
		ContextWrapper cw = new ContextWrapper(context);
		File fileDir = cw.getDir(MTFImageHelper.folderName, Context.MODE_PRIVATE);
		
		if (fileDir.exists()) {
			fileDir.delete();
		}
	}

	public static boolean isEmailValid(String email){
         String regExpn =
             "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                 +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                   +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                   +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                   +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                   +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

	     CharSequence inputStr = email;
	
	     Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
	     Matcher matcher = pattern.matcher(inputStr);
	
	     if(matcher.matches())
	        return true;
	     else
	        return false;
	}
	
	public static String getOTPPassword(String message){
		String[] listMessage = message.split(" ");
		for (int i = 0; i < listMessage.length; i++) {
			String partOfMessage = listMessage[i];
			partOfMessage = partOfMessage.replace(",", "");
			partOfMessage = partOfMessage.replace(".", "");
			String result = ""+Integer.parseInt(partOfMessage);
			if (result.length() == 6) {
				return result;
			}
		}
		return null;
	}
	
	public static String getRTRWNumber(String message){
		String[] listMessage = message.split(" ");
		for (int i = 0; i < listMessage.length; i++) {
			String partOfMessage = listMessage[i];
			partOfMessage = partOfMessage.replace(",", "");
			partOfMessage = partOfMessage.replace(".", "");
			String result = ""+Integer.parseInt(partOfMessage);
			if (result.length() == 3) {
				return result;
			}
		}
		return null;
	}
	
	public static HashMap<String, String> splitRTRW(String data){
		HashMap<String, String> resultMap = new HashMap<String, String>();
		
		String rt = null;
		String rw = null;
		
		if(!data.isEmpty() && !data.equals("") && !data.equals(" ")){
			int splitterPosition = data.indexOf('/');
			if(splitterPosition > 0){
				rt = data.substring(0,splitterPosition);
				rw = data.substring(splitterPosition+1, data.length());
			}
		}		
		
		resultMap.put(MTFConstants.RT, rt);
		resultMap.put(MTFConstants.RW, rw);
		
		return resultMap;
	}
	
	public static HashMap<String, String> splitRTRWVillage(String data) throws Exception{
		HashMap<String, String> resultMap = new HashMap<String, String>();
		
		String rt = null;
		String rw = null;
		String village = null;
		
		if(!data.isEmpty() && !data.equals("") && !data.equals(" ")){
			String[] listMessage = data.split(" ");
			for (int i = 0; i < listMessage.length; i++) {
				String partOfMessage = listMessage[i];
				partOfMessage = partOfMessage.replace(",", "");
				partOfMessage = partOfMessage.replace(".", "");
				String result = partOfMessage;
				if (result.length() == 3) {
					if(rt==null){
						rt = result;
					}
				}
				if (result.length() == 4) {
					if(rw==null){
						rw = result;
					}
				}
			}
			if(listMessage.length>1){
				village = data.substring(data.indexOf(rw)+rw.length(), data.length()-1);
			}
		}
		
		resultMap.put(MTFConstants.RT, rt);
		resultMap.put(MTFConstants.RW, rw);
		resultMap.put(MTFConstants.VILLAGE, village);
		return resultMap;
	}
	
	public static HashMap<String, String> splitCodeAndName(String data){
		HashMap<String, String> resultMap = new HashMap<String, String>();
		
		String code = null;
		String descr = null;
		
		if(!data.isEmpty() && !data.equals("") && !data.equals(" ")){
			int splitterPosition = data.indexOf('-');
			if(splitterPosition > 0){
				code = data.substring(0,splitterPosition);
				descr = data.substring(splitterPosition+1, data.length());
			}else{
				descr = data;
			}
		}		
		resultMap.put(MTFConstants.CODE, code);
		resultMap.put(MTFConstants.DESC, descr);
		
		return resultMap;
	}
	
	public static String generateCheckSum(byte[] dataForCreateCheckSum){
		CRC32 crc32 = new CRC32();
		crc32.reset();
		crc32.update(dataForCreateCheckSum);
		
		return crc32.getValue()+"";
	}
	
	public static String encrypt(String seed)
            throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] result = encrypt(rawKey, BANANA.getBytes());
        return toHex(result);
    }

    public static String decrypt(String seed)
            throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] enc = toByte(BANANA);
        byte[] result = decrypt(rawKey, enc);
        return new String(result);
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
        kgen.init(128, sr); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted)
            throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }
	
    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";

        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }

        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(BANANA.charAt((b >> 4) & 0x0f)).append(BANANA.charAt(b & 0x0f));
    }
    
    public static byte[] encryptRaw(byte[] textBytes) 
			throws java.io.UnsupportedEncodingException, 
				NoSuchAlgorithmException,
				NoSuchPaddingException,
				InvalidKeyException,
				InvalidAlgorithmParameterException,
				IllegalBlockSizeException,
				BadPaddingException {
    	IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
		AlgorithmParameterSpec ivSpec = iv;
    	SecretKeySpec newKey = new SecretKeySpec(MTFEncryptUtil.generateKey().getEncoded(), "AES");
    	Cipher cipher = null;
		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
		return cipher.doFinal(textBytes);
	}

    public static boolean getTotalSentPart(String successParts, int totalPart){
        if (!successParts.equals("")) {
            String[] splitString = successParts.split(",");
            return splitString.length==totalPart;
        }
        return false;
    }

	public static String getWifiName(Context context){
		WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

		return wifiInfo.getSSID();
	}
}
