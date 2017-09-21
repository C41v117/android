package com.btpn.sinaya.eform.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class MTFEncryptUtil {

	static SecretKey secret;
	private static final String ALGORITHM_KEY       = "AES";
	private static final String ALGORITHM       = "AES/CBC/PKCS5Padding";
	private static final String UNICODE_FORMAT  = "UTF8";
	   
	public static SecretKey generateKey(String password) throws NoSuchAlgorithmException,
			InvalidKeySpecException, UnsupportedEncodingException {
		
		KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM_KEY);
	    SecureRandom secrand = SecureRandom.getInstance("SHA1PRNG");
	    secrand.setSeed(password.getBytes());
	    keygen.init(128, secrand);
		
	    secret = keygen.generateKey();
	    
		return secret;
	}
	
	public static SecretKey generateKey(){
		/* Store these things on disk used to derive key later: */
		String password = "15236127812";
	    int iterationCount = 1000;
	    int saltLength = 32; // bytes; should be the same size as the output
	                            // (256 / 8 = 32)
	    int keyLength = 256; // 256-bits for AES-256, 128-bits for AES-128, etc
	    byte[] salt = new byte[saltLength]; // Should be of saltLength

	    /* When first creating the key, obtain a salt with this: */
	    SecureRandom random = new SecureRandom();
	    random.nextBytes(salt);

	    /* Use this to derive the key from the password: */
//	    KeySpec keySpec = new PBEKeySpec(new String(key, Constants.CHAR_ENCODING).toCharArray(), key, iterationCount, keyLength);
		try {
			KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithSHA256And256BitAES-CBC-BC");
		    byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
		    SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
		    
		    return secretKey;
		} catch (NoSuchAlgorithmException e) {
		} catch (InvalidKeySpecException e) {
		}
		
		return null;
	}

	public static byte[] encryptMsg(String message, SecretKey secret)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidParameterSpecException,
			IllegalBlockSizeException, BadPaddingException,
			UnsupportedEncodingException, InvalidAlgorithmParameterException {
		/* Encrypt the message. */
		
		IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
		
//		byte[] rawkey = secret.getEncoded();
//		SecretKeySpec skeySpec = new SecretKeySpec(rawkey, ALGORITHM);
//		
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, secret, iv);
		byte[] cipherText = cipher.doFinal(message.getBytes(UNICODE_FORMAT));
		
		return cipherText;
	}

	public static String decryptMsg(byte[] cipherText, SecretKey secret)
			throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidParameterSpecException, InvalidAlgorithmParameterException,
			InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException, UnsupportedEncodingException {

		/*
		 * Decrypt the message, given derived encContentValues and
		 * initialization vector.
		 */

		IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
//		byte[] rawKey = secret.getEncoded();
		SecretKeySpec skeySpec = new SecretKeySpec(secret.getEncoded(), ALGORITHM_KEY);
	    Cipher cipher = Cipher.getInstance(ALGORITHM);
	    cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		String decryptString = new String(cipher.doFinal(cipherText));
		return decryptString;
	}
}
