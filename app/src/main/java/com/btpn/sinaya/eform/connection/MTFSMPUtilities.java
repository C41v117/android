package com.btpn.sinaya.eform.connection;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import android.content.Context;

import com.btpn.sinaya.eform.utils.MTFConstants;
import com.btpn.sinaya.eform.utils.MTFFileLogger;
import com.btpn.sinaya.eform.utils.MTFGenerator;
import com.btpn.sinaya.eform.utils.MTFSystemParams;
import com.sap.mobile.lib.configuration.Constants;
import com.sap.mobile.lib.configuration.Preferences;
import com.sap.mobile.lib.configuration.PreferencesException;
import com.sap.mobile.lib.parser.IODataSchema;
import com.sap.mobile.lib.parser.IODataServiceDocument;
import com.sap.mobile.lib.parser.Parser;
import com.sap.mobile.lib.parser.ParserException;
import com.sap.mobile.lib.request.ConnectivityParameters;
import com.sap.mobile.lib.request.HttpChannelListeners.ISSLChallengeListener;
import com.sap.mobile.lib.request.RequestManager;
import com.sap.mobile.lib.supportability.Logger;
import com.sap.smp.rest.AppSettings;
import com.sap.smp.rest.ClientConnection;
import com.sap.smp.rest.UserManager;
import com.sybase.persistence.DataVault;
import com.sybase.persistence.PrivateDataVault;

public class MTFSMPUtilities {

	public static final int TYPE_ERROR = 0;
	public static final int TYPE_INFO = 1;
	public static final int TYPE_WARNING = 2;

//	private static final int CACERTS_FILE_SIZE = 1024 * 140;

	// Taken from User Interface in Settings screen.
//	public static String star = "a#sb$2sdv";
//	public static String moon = "Dnx3srik1zse0e";
//	public static String secConfig = "SINAYA";

	public static String SSL_PUBLIC_KEY = "";
	public static String SSL_CHECK_SUM = "";
	public static boolean isHandShake = false;
	public static int dbVer;

	public static String username = "";
	public static String password = "";
	public static String appPasscode = "a#sb$2sdv";
	public static String appSaltcode = "Dnx3srik1zse0e";

    public static Boolean isUAT = false;
	public static Boolean isHttpRequest;
	public static String requestType;
	public static String host;
	public static String port;
	public static int portHttps;

	// Create the application on the server with following details. Else change
	// the following:

	public static String secConfig = "EFORM-SINAYA";
	public static String appId = "rfb.app.eform";
	public static String appIdCheckChunk = "rfb.app.eform.checkchunk";
	public static String appIdCustomerList = "rfb.app.eform.getcustomerlist";
	public static String appIdLogin = "rfb.app.eform.login";
	public static String appIdLogout = "rfb.app.eform.logout";
	public static String appIdMasterData = "rfb.app.eform.syncversion";
	public static String appIdNewCustomer = "rfb.app.eform.registercustomer";
	public static String appIdProfile = "rfb.app.eform.getdetailprofile";
	public static String appIdSendFile = "rfb.app.eform.sendchunk";
	public static String appIdUpdateCustomer = "rfb.app.eform.updatecustomer";
	public static String appIdCustomerDetail = "rfb.app.eform.getcustomerrepairdata";
	public static String appIdCIFList = "rfb.app.eform.getcifeq";
	public static String appidDHIBList = "rfb.app.eform.getdhibeq";
	public static String appIdROCode = "rfb.app.eform.getrocode";
	public static String appIdAcctNo = "rfb.app.eform.getacctno";

    public static String appIdLoginRest1 = "eformws/rest/user/login";
    public static String appIdLogoutRest1 = "eformws/rest/user/logout";
    public static String appIdProfileRest1 = "eformws/rest/user/profile";
    public static String appIdPingRest1 = "eformws/rest/user/ping";

    public static String appIdMasterDataRest1 = "eformws/rest/masterdata/sync";

    public static String appIdNewCustomerRest1 = "eformws/rest/customers/register";
    public static String appIdUpdateCustomerRest1 = "eformws/rest/customers/update";
    public static String appIdCustomerListRest1 = "eformws/rest/customers";
    public static String appIdCustomerDetailRest1 = "eformws/rest/customers/detail/";
    public static String appIdVerifyCardRest1 = "eformws/rest/customers/updateCardPerso";
    public static String appidDHIBListRest1 = "eformws/rest/customers/checkDHIB";
    public static String appIdCIFListRest1 = "eformws/rest/customers/checkCIF";

    public static String appIdSendFileRest1 = "eformws/rest/filechunks/send";
    public static String appIdCheckChunkRest1 = "eformws/rest/filechunks/check";

    //public static String appIdLoginRest = "eformws/rest/user/login";
    public static String appIdLoginRest = "pur/login";
	public static String appIdLogoutRest = "eformusr/user/logout";
    public static String appIdProfileRest = "eformusr/user/profile";
    public static String appIdPingRest = "eformusr/user/ping";

    public static String appIdMasterDataRest = "eformdt/masterdata/sync";

    public static String appIdNewCustomerRest = "eformcust/customers/register";
    public static String appIdUpdateCustomerRest = "eformcust/customers/update";
    public static String appIdCustomerListRest = "eformcust/customers";
    public static String appIdCustomerDetailRest = "eformcust/customers/detail/";
    public static String appidDHIBListRest = "eformcust/customers/checkDHIB";
    public static String appIdCIFListRest = "eformcust/customers/checkCIF";
    public static String appIdROCodeRest = "eformcust/customers/validateROCode";
    public static String appIdAcctNoRest = "eformcust/customers/validateAcctNo";

    public static String appIdSendFileRest = "eformfile/filechunks/send";
    public static String appIdCheckChunkRest = "eformfile/filechunks/check";

    public static String appIdVerifyCardRest = "eformws/rest/card/updateCardPerso";
    public static String appIdInquiryCustomerCard = "eformws/rest/card/enrollment/inquiryCustomerCard";

	public static String appIdFindAllByNopenAndNorek = "";

	public static String appOData = "odata/applications/latest";
	public static String domain = "default";
	public static String appConnId = "";
	public static Logger logger;
	public static Preferences pref;
	public static ConnectivityParameters param;
	public static RequestManager reqMan;
	public static Parser parser;
	public static String certificateFileName = "meap-uat.cer";

	public static IODataServiceDocument serviceDoc = null;
	public static IODataSchema metaDoc = null;

	public static final String dataVaultName = "data_vault_pantara_sinaya";

	public static ClientConnection clientConnection = null;
	public static UserManager userManager = null;
	public static AppSettings appSettings = null;

	//public static String SECRET_SIGNATURE = "";
	public static String JWT = "";
	//public static String KEY = "";

	public static String getDataValultName() {
		return dataVaultName + appId + username;
	}

	public static void initializeRequestManager(final Context context) {
		if (reqMan != null) {
			return;
		}

		if (username.equals("") || password.equals("")) {
			if (PrivateDataVault.vaultExists(MTFSMPUtilities.dataVaultName)) {
				DataVault dataVault = PrivateDataVault
						.getVault(MTFSMPUtilities.dataVaultName);
				dataVault.unlock(MTFSMPUtilities.appPasscode,
						MTFSMPUtilities.appPasscode
								+ MTFSMPUtilities.appSaltcode);

				MTFSMPUtilities.username = dataVault.getString("username");
				MTFSMPUtilities.password = dataVault.getString("password");
			}
		}

		if (username.equals("") || password.equals("")) {
			return;
		}

		try {
			logger = new Logger();
			pref = new Preferences(context, logger);
			pref.setStringPreference(Preferences.CONNECTIVITY_HANDLER_CLASS_NAME,Constants.HTTP_HANDLER_CLASS);
			pref.setBooleanPreference(Preferences.REQUEST_ENABLE_SNI_TLS_FACTORY, true);
			pref.setIntPreference(Preferences.CONNECTIVITY_HTTPS_PORT,portHttps);
			pref.setBooleanPreference(Preferences.PERSISTENCE_SECUREMODE, false);
			pref.setIntPreference(Preferences.CONNECTIVITY_CONNTIMEOUT,MTFSystemParams.timeOut);
			pref.setIntPreference(Preferences.CONNECTIVITY_SCONNTIMEOUT,MTFSystemParams.timeOut);

			param = new ConnectivityParameters();
			param.setUserName(username);
			param.setUserPassword(password);
			param.enableXsrf(true);

			try {
				parser = new Parser(pref, logger);
			} catch (ParserException e) {

			}

			reqMan = new RequestManager(logger, pref, param, 1);

			if (MTFSystemParams.isUseHTTPS) {
				reqMan.setSSLChallengeListener(new ISSLChallengeListener() {
					@Override
					public boolean isServerTrusted(X509Certificate[] certificate) {
						return checkServerTrust(certificate);
					}
				});
			}
		} catch (PreferencesException e) {
			final Writer result = new StringWriter();
			final PrintWriter printWriter = new PrintWriter(result);
			e.printStackTrace(printWriter);
			String stacktrace = result.toString();
			MTFFileLogger.getInstance().addString("REQ_MAN_INIT", stacktrace);
			MTFFileLogger.getInstance().writeAndReset("REQ_MAN_INIT");
			e.printStackTrace();
		}
	}

	public static void configurationForRegistration(Context applicationContext) {
		PrivateDataVault.init(applicationContext);
		MTFConnectionManager.init();
		initializeRequestManager(applicationContext);
		if(reqMan != null){
			MTFConnectionManager.init();
			clientConnection = new ClientConnection(applicationContext, appId, domain, secConfig, reqMan);
			clientConnection.setConnectionProfile(requestType + host);
			userManager = new UserManager(clientConnection);
			appSettings = new AppSettings(clientConnection);
			reqMan = null;

			clientConnection.setConnectionProfile(isHttpRequest, host, port, null, null);
		}else{

		}
	}

	private static boolean checkServerTrust(X509Certificate[] certificate) {
		// Hack ahead: BigInteger and toString(). We know a DER encoded Public
		// Key starts with 0x30 (ASN.1 SEQUENCE and CONSTRUCTED), so there is
		// no leading 0x00 to drop.
		boolean expected = false;
		for (int i = 0; i < certificate.length; i++) {
			RSAPublicKey pubkey = (RSAPublicKey) certificate[i].getPublicKey();
			String encoded = new BigInteger(1, pubkey.getEncoded()).toString(16);
			// String checkSum = MTFGenerator.generateCheckSum(certificate[i].getSignature());
			// Pin it!
			if (MTFConstants.SSL_PUBLIC_KEY.equalsIgnoreCase(encoded)) {
				expected = true;
				break;
			}
		}
		return expected;
	}
}