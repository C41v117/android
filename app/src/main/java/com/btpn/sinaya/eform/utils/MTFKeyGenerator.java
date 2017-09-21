package com.btpn.sinaya.eform.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import javax.crypto.Cipher;

/**
 * Created by vaniawidjaja on 2/7/17.
 */

public class MTFKeyGenerator {

    /**
     * Generate key which contains a pair of private and public key using 1024
     * bytes. Store the set of keys in Prvate.key and Public.key files.
     *
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws FileNotFoundException
     */

    public static boolean isEmpty (String s) {
        boolean empty = false;
        if ((s == null) || s.trim().equals("")) {
            empty = true;
        }
        return empty;
    }

    /**
     * The method checks if the pair of public and private key has been
     * generated.
     *
     * @return flag indicating if the pair of keys were generated.
     */

    /**
     * Encrypt the plain text using public key.
     *
     * @param text
     *            : original plain text
     * @param publicKey
     *            :The public key
     * @return Encrypted text
     * @throws Exception
     */
    public static String encrypt(String text, String appender,
                                 PublicKey publicKey) throws Exception {
        byte[] cipherText = null;
        StringBuilder result = null;

        // get an RSA cipher object and print the provider
        final Cipher cipher = Cipher.getInstance(MTFConstants.TYPE);

        // encrypt the plain text using the public key
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        cipherText = cipher.doFinal(text.getBytes());
        byte[] encryptedByteValue = new Base64().encode(cipherText);
        result = new StringBuilder(new String(encryptedByteValue));

        // Appender String
        result.append(appender);

        return result.toString();
    }

    public static String appender(String nik) throws Exception{
        String result = new String();
        if(isEmpty(nik)){
            // Throw Exception if Phone Number Is Empty
            NullPointerException nullPointerException = new NullPointerException("Username for Appender is NULL");
            throw nullPointerException;
        }else{
            if(nik.length() < MTFConstants.MASKING_LENGTH){
                // Throw Exception if length shorter than masking length
                Exception exception = new Exception("Username Agent Length To Short");
                throw exception;
            }else{
                int startSub = nik.length() - MTFConstants.MASKING_LENGTH;
                result = nik.substring(startSub, nik.length());
            }
        }

        return result;
    }
}