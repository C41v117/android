package com.btpn.sinaya.eform.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class MTFAES
{
    
	private static final String ALGORITHM = "AES";
    private static final byte[] semiActiveParam = "ADBSJHJS12547896".getBytes();
    private static Key keys;
    
    public static String encryptString(String valueToEnc) throws Exception {
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, keys);

        byte[] encValue = c.doFinal(valueToEnc.getBytes());
        byte[] encryptedByteValue = new Base64().encode(encValue);
        String encryptedValue = new String(encryptedByteValue);

        return encryptedValue;
    }

    public static String decryptString(String encryptedValue) throws Exception {
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, keys);
        byte[] decodedValue = new Base64().decode(encryptedValue.getBytes());
		byte[] decryptedVal = c.doFinal(decodedValue);
		String file = new String(decryptedVal);
        
        return file;
        
    }
    
    public static Key generateKey() throws Exception {
        keys = new SecretKeySpec(semiActiveParam, ALGORITHM);
        return keys;
    }
}