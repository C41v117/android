package com.btpn.sinaya.eform.connection;

import com.btpn.sinaya.eform.utils.MTFConstants;
import com.sap.mobile.lib.request.HttpChannelListeners;

import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.math.BigInteger;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * Created by vaniawidjaja on 2/7/17.
 */

public class MTFChallengeListener implements HttpChannelListeners.ISSLChallengeListener {

    private static MTFChallengeListener instance;
    private boolean isServerTrusted = false;

    private String httpsUrl = "";

    public static MTFChallengeListener getInstance(){
        if(instance == null){
            instance = new MTFChallengeListener();
        }
        return instance;
    }

    private MTFChallengeListener(){
        if (MTFSMPUtilities.port != null) {
            httpsUrl = MTFSMPUtilities.requestType+MTFSMPUtilities.host+":"+MTFSMPUtilities.port+"/"+MTFSMPUtilities.appId;
        }else{
            httpsUrl = MTFSMPUtilities.requestType+MTFSMPUtilities.host+"/"+MTFSMPUtilities.appId;
        }

        Object result = checkConnection();

        if(result == null){
            isServerTrusted = false;
        }
        else if (!(result instanceof Exception || result instanceof byte[])){
            isServerTrusted = false;
        }
        else if (result instanceof Exception) {
            isServerTrusted = false;
        }else{
            isServerTrusted = true;
        }
    }

    public boolean isServerTrusted(){
        return isServerTrusted;
    }

    @Override
    public boolean isServerTrusted(X509Certificate[] arg0) {
        isServerTrusted = checkServerTrust(arg0);
        return isServerTrusted;
    }

    private static boolean checkServerTrust(X509Certificate[] certificate){
        // Hack ahead: BigInteger and toString(). We know a DER encoded Public
        // Key starts with 0x30 (ASN.1 SEQUENCE and CONSTRUCTED), so there is
        // no leading 0x00 to drop.
        boolean expected = false;
        for (int i = 0; i < certificate.length; i++) {
            RSAPublicKey pubkey = (RSAPublicKey) certificate[i].getPublicKey();
            String encoded = new BigInteger(1, pubkey.getEncoded()).toString(16);

            if (MTFConstants.SSL_PUBLIC_KEY.equalsIgnoreCase(encoded)) {
                expected = true;
                break;
            }
        }

        return expected;
    }

    private Object checkConnection(){
        Object result;
        try {

            byte[] secret = null;

            TrustManager tm[] = { new MTFTrustManager() };
            assert (null != tm);

            SSLContext context = SSLContext.getInstance("TLS");
            assert (null != context);
            context.init(null, tm, null);

            URL url = new URL(httpsUrl);
            assert (null != url);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            assert (null != connection);

            connection.setSSLSocketFactory(context.getSocketFactory());
            InputStreamReader instream = new InputStreamReader(connection.getInputStream());
            assert (null != instream);

            StreamTokenizer tokenizer = new StreamTokenizer(instream);
            assert (null != tokenizer);

            secret = new byte[16];
            assert (null != secret);

            int idx = 0, token;
            while (idx < secret.length) {
                token = tokenizer.nextToken();
                if (token == StreamTokenizer.TT_EOF)
                    break;
                if (token != StreamTokenizer.TT_NUMBER)
                    continue;

                secret[idx++] = (byte) tokenizer.nval;
            }

            result = (Object) secret;
        } catch (Exception ex) {
            result = (Object) ex;
        }
        return result;
    }
}
