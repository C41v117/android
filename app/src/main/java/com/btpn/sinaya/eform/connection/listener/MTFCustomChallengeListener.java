package com.btpn.sinaya.eform.connection.listener;

import com.btpn.sinaya.eform.utils.MTFConstants;
import com.sap.mobile.lib.request.HttpChannelListeners;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by vaniawidjaja on 2/7/17.
 */

public class MTFCustomChallengeListener implements HttpChannelListeners.ISSLChallengeListener {
    private static boolean checkServerTrust(
            X509Certificate[] paramArrayOfX509Certificate) {
        for (int i = 0;; i++) {
            if (i >= paramArrayOfX509Certificate.length) {
                return false;
            }
            String str1 = new BigInteger(1,
                    ((RSAPublicKey) paramArrayOfX509Certificate[i]
                            .getPublicKey()).getEncoded()).toString(16);
            if (MTFConstants.SSL_PUBLIC_KEY.equalsIgnoreCase(str1)) {
                return true;
            }
        }
    }

    public boolean isServerTrusted(X509Certificate[] paramArrayOfX509Certificate) {
        return checkServerTrust(paramArrayOfX509Certificate);
    }
}