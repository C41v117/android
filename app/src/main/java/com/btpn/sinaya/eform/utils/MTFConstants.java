package com.btpn.sinaya.eform.utils;

import java.util.HashMap;

public class MTFConstants {

    public static final String AUTHENTICATION_FAILED = "403 Authentication Failed";
    public static final String RESPOND_ERROR_TOKEN = "{\"message\":\"Sesi anda telah berakhir, silahkan login kembali.\",\"json\":\"\",\"result\":\"error_token\"}";

    public static final Integer E_SAVING = 1;
    public static final Integer E_LOAN = 2;

	public static final int SINAYA_LOB = 0;
	public static final int PUR_LOB = 3;

    public static final String PUR = "PUR";
    public static final String AE = "AE";

    public static String LOAN_PACKAGE = "com.btpn.purna.eformloan";
    public static final String LOAN_HOME_ACTIVITY = "com.btpn.purna.eformloan.activity.MTFAddNewLoanHomeActivity";
    public static final String LOAN_ACTIVITY = "com.btpn.purna.eformloan.activity.MTFAddNewLoanActivity";

	public static final String FONT_PATH = "RobotoCondensed-Regular.ttf";

	public static final boolean autoComplete = false;
    public static final boolean UIOnly = false;
    public static final boolean byPassCIF = false;
	public static final boolean isUseGateway = false;
    public static final boolean logging = false;

	public static final String TAG = "DEBUG";
	
	public static final String CODE = "code";
	public static final String DESC = "desc";

	public static String SSL_PUBLIC_KEY;
	public static String SSL_CHECK_SUM;

	public static final String RT = "RT";
	public static final String RW = "RW";
	public static final String VILLAGE = "VILLAGE";
	
	public static final int MENU_CUSTOMER_LIST = 0;
	public static final int MENU_CUSTOMER_LIST_PURNA = 1;
	public static final int MENU_CUSTOMER_LIST_VERIFY = 2;
	public static final int MENU_NEW_LOAN_LIST = 3;
	public static final int MENU_RENEWAL_LIST = 4;
	public static final int MENU_VERIFY_LOAN_LIST = 5;
	public static final int MENU_PROFIL_AGENT = 6;
	public static final int MENU_INFORMASI_PRODUCT = 7;
	public static final int MENU_PROFILE_BTPN = 8;
	public static final int MENU_BRANCH_LOCATION = 9;
	public static final int MENU_QUEST_ANSWER = 10;
	public static final int MENU_LOGOUT = 11;

	public static final String EXTRA_TYPE = "extra_type";
	public static final String EXTRA_OUTPUT = "extra_output";
	public static final String EXTRA_ROTATION = "extra_rotation";
	
	public static final int REQUEST_CODE_CAMERA = 101;
    public static final int REQUEST_CODE_SIGNATURE_PAD = 102;
	
	public static final int RESULT_CODE_OK = 201;
	public static final int RESULT_CODE_CANCEL = 202;
	
	public static final String FILENAME_KTP_PREFIX = "ktp_";
	public static final String FILENAME_SECOND_KTP_PREFIX = "ktp2_";
	public static final String FILENAME_NPWP_PREFIX = "npwp_";
	public static final String FILENAME_SECOND_NPWP_PREFIX = "npwp2_";
	public static final String FILENAME_SIGNATURE_PREFIX = "ttd_";
	public static final String FILENAME_SECOND_SIGNATURE_PREFIX = "ttd2_";
	public static final String FILENAME_APPLICANT_PREFIX = "nasabah_";
	public static final String FILENAME_SECOND_APPLICANT_PREFIX = "nasabah2_";
	public static final String FILENAME_DOK_PENDUKUNG_PREFIX = "dok_pendukung_";
	public static final String FILENAME_SECOND_DOK_PENDUKUNG_PREFIX = "dok_pendukung2_";
	public static final String FILENAME_KK_PREFIX = "kk_";
	public static final String FILENAME_SELFIE_PREFIX = "selfie_";

	public static final String DATE_FORMAT = "yyyy-MM-dd";

	public static Boolean UPDATE_LOOKUP = false;
	public static final int MASKING_LENGTH = 4;
	public static final String TYPE = "RSA/ECB/PKCS1Padding";

	public static final int MTF_PERMISSION_REQUEST_CAMERA = 111;
	public static final int MTF_PERMISSION_REQUEST_WRITE_STORAGE = 112;
	public static final int MTF_PERMISSION_REQUEST_READ_PHONE = 113;

	public static final int MTF_PERMISSION_REQUEST_LOGIN = 198;
	public static final int MTF_PERMISSION_REQUEST_INIT = 199;
	public static final int MTF_PERMISSION_OPEN_SETTING = 190;

    //Request Code Loan
    public static final int REQUEST_CODE_DATA_ENTRY = 1;
    public static final int REQUEST_CODE_ANGSURAN = 2;
    public static final int REQUEST_CODE_KESEHATAN = 3;
    public static final int REQUEST_CODE_DOKUMEN = 4;
    public static final int REQUEST_CODE_MEMO = 5;
    public static final int REQUEST_CODE_SIGNATURE = 6;
	public static final int REQUEST_CODE_INTERVIEW = 7;
	public static final int REQUEST_CODE_CHECKING = 8;
	public static final int REQUEST_CODE_VERIFY_HOME = 9;

    //Result Code Loan
    public static final int RESULT_CODE_DATA_ENTRY = 11;
    public static final int RESULT_CODE_ANGSURAN = 12;
    public static final int RESULT_CODE_KESEHATAN = 13;
    public static final int RESULT_CODE_DOKUMEN = 14;
    public static final int RESULT_CODE_MEMO = 15;
    public static final int RESULT_CODE_SIGNATURE = 16;
	public static final int RESULT_CODE_INTERVIEW = 17;
	public static final int RESULT_CODE_CHECKING = 18;
	public static final int RESULT_CODE_VERIFY_HOME = 19;

	//BTPN API-KEY
	public static String BTPN_API_KEY_VALUE 	= "8428c326-ae66-47c7-9b06-1797ac513911";
	public static final String COLON 				= ":";
	public static final String HMAC_256			 	= "HmacSHA256";
	public static final String HMAC_DATE_PATTERN 	= "yyyy-mm-dd'T'HH:mm:ss.SSSZ";

	//IS_EDC_CONNECTED
	public static boolean IS_EDC_CONNECTED = false;

	//EDC Response Code
	public static final String EDC_RESP_APPROVED = "00";
	public static final String EDC_RESP_MISSING_STX = "02";
	public static final String EDC_RESP_MISSING_ETX = "03";
	public static final String EDC_RESP_INCORRECT_MESSAGE_LEN = "04";
	public static final String EDC_RESP_LRC_NOT_MATCH = "05";
	public static final String EDC_RESP_INCORRECT_TAG = "61";
	public static final String EDC_RESP_INCORRECT_LEN = "62";
	public static final String EDC_RESP_FINGER_TEMPLATE_TOO_BIG = "71";
	public static final String EDC_RESP_ENROLLMENT_KO = "72";
	public static final String EDC_RESP_ABORT_BY_USER = "73";
	public static final String EDC_RESP_INVALID_LENGTH_PIN = "74";
	public static final String EDC_RESP_PIN_NOT_MATCH = "75";
	public static final String EDC_RESP_INVALID_INDEX_LABEL_FINGER = "76";
	public static final String EDC_RESP_FINGER_BAD_QUALITY = "77";
	public static final String EDC_RESP_ERR_TIMEOUT = "80";
	public static final String EDC_RESP_ERR_SAM_NOT_ACTIVED = "81";
	public static final String EDC_RESP_ERR_FAILED_READ_CARD = "82";
	public static final String EDC_RESP_ERR_INVALID_FINGER_FLAG = "83";
	public static final String EDC_RESP_ERR_CARD_RESET_FAILED = "84";
	public static final String EDC_RESP_ERR_AUTH_FAILED = "85";
	public static final String EDC_RESP_ERR_NASABAH_DOESNT_EXIST = "86";
	public static final String EDC_RESP_ERR_GET_FINGER_INDEX_03 = "87";
	public static final String EDC_RESP_ERR_GET_FINGER_INDEX_04 = "88";
	public static final String EDC_RESP_ERR_UPDATE_FINGER_01_FAILED = "89";
	public static final String EDC_RESP_ERR_UPDATE_FINGER_02_FAILED = "8A";
	public static final String EDC_RESP_PROBLEM_APDU = "8B";
	public static final String EDC_RESP_BIOMETRIC_MISMATCH = "8C";
	public static final String EDC_RESP_FAILED_WRITE_FINGER_1 = "8D";
	public static final String EDC_RESP_FAILED_WRITE_FINGER_2 = "8E";
	public static final String EDC_RESP_FAILED_LOCK_APDU = "8F";
	public static final String EDC_RESP_CANT_UDPATE_NASABAH = "90";
	public static final String EDC_RESP_MK_WK_NOT_LOADED = "96";
	public static final String EDC_RESP_VERIFICATION_FAILED = "97";
	public static final String EDC_RESP_OTHERS = "99";
	public static final String EDC_RESP_MISMATCH_NUMBER_CMS = "90";
	public static final String EDC_RESP_FAILED_UPDATE_OFFICER_CARD = "91";
	public static final String EDC_RESP_OFFICER_CARD_INVALID = "92";
	public static final String EDC_RESP_ALREADY_ACTIVATED = "93";
	
	public static final String EDC_RESP_APPROVED_STR = "APPROVED";
	public static final String EDC_RESP_MISSING_STX_STR = "Missing STX";
	public static final String EDC_RESP_MISSING_ETX_STR = "Missing ETX";
	public static final String EDC_RESP_INCORRECT_MESSAGE_LEN_STR = "Incorrect Message Len";
	public static final String EDC_RESP_LRC_NOT_MATCH_STR = "LRC Not Match";
	public static final String EDC_RESP_INCORRECT_TAG_STR = "Incorrect Tag";
	public static final String EDC_RESP_INCORRECT_LEN_STR = "Incorrect Len";
	public static final String EDC_RESP_FINGER_TEMPLATE_TOO_BIG_STR = "Enrollment, Finger Template Too Big";
	public static final String EDC_RESP_ERR_FAILED_READ_CARD_STR = "Err Card. Failed Reading Card";
	public static final String EDC_RESP_ENROLLMENT_KO_STR = "Enrollment, KO";
	public static final String EDC_RESP_ABORT_BY_USER_STR = "Abort by User";
	public static final String EDC_RESP_INVALID_LENGTH_PIN_STR = "Invalid PIN Length";
	public static final String EDC_RESP_ERR_TIMEOUT_STR = "Err Card. Timeout";
	public static final String EDC_RESP_PIN_NOT_MATCH_STR = "PIN Not Match";
	public static final String EDC_RESP_INVALID_INDEX_LABEL_FINGER_STR = "Invalid Index Label Finger";
	public static final String EDC_RESP_FINGER_BAD_QUALITY_STR = "Bad Quality Finger";
	public static final String EDC_RESP_ERR_SAM_NOT_ACTIVED_STR = "Err Card. SAM Not Activated";
	public static final String EDC_RESP_ERR_INVALID_FINGER_FLAG_STR = "Err Card. Invalid Finger Flag";
	public static final String EDC_RESP_ERR_CARD_RESET_FAILED_STR = "Err Card. Reset Failed";
	public static final String EDC_RESP_ERR_AUTH_FAILED_STR = "Err Card. Authentication Failed";
	public static final String EDC_RESP_ERR_NASABAH_DOESNT_EXIST_STR = "Err Card. Nasabah Information file doesn’t exist";
	public static final String EDC_RESP_ERR_GET_FINGER_INDEX_03_STR = "Err Card. Get Finger Index File 0003";
	public static final String EDC_RESP_ERR_GET_FINGER_INDEX_04_STR = "Err Card. Get Finger Index File 0004";
	public static final String EDC_RESP_ERR_UPDATE_FINGER_01_FAILED_STR = "Err Card. Update Finger 01 to Applet Failed";
	public static final String EDC_RESP_ERR_UPDATE_FINGER_02_FAILED_STR = "Err Card. Update Finger 02 to Applet Failed";
	public static final String EDC_RESP_PROBLEM_APDU_STR = "Err Card. Problem Execute Get Score APDU Command";
	public static final String EDC_RESP_BIOMETRIC_MISMATCH_STR = "Err Card. Biometric Finger Not Match";
	public static final String EDC_RESP_FAILED_WRITE_FINGER_1_STR = "Err Card. Failed to write Finger Template 1";
	public static final String EDC_RESP_FAILED_WRITE_FINGER_2_STR = "Err Card. Failed to write Finger Template 2";
	public static final String EDC_RESP_FAILED_LOCK_APDU_STR = "Err Card. Failed to execute Lock APDU Command";
	public static final String EDC_RESP_CANT_UDPATE_NASABAH_STR = "Err. Card. Cannot Update File Nasabah";
	public static final String EDC_RESP_MK_WK_NOT_LOADED_STR = "MK, WK Not Loaded";
	public static final String EDC_RESP_VERIFICATION_FAILED_STR = "Err Card. Verification Failed";
	public static final String EDC_RESP_OTHERS_STR = "Others";
	public static final String EDC_RESP_MISMATCH_NUMBER_CMS_STR = "Err Card. Card Number Mismatch between CMS and Card Data";
	public static final String EDC_RESP_FAILED_UPDATE_OFFICER_CARD_STR = "Err Card. Failed to update Officer Card (Tag Expiry Date)";
	public static final String EDC_RESP_OFFICER_CARD_INVALID_STR = "Err Card. This is not an Officer Card (Tag 21 Invalid)";
	public static final String EDC_RESP_ALREADY_ACTIVATED_STR = "Err Card. Main Card is already Activated (Biofinger Len is higher than 0)";

	public static final HashMap<String, String> EDC_RESPONSE_CODE_MAP = new HashMap<String, String>();
	static {
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_APPROVED, EDC_RESP_APPROVED_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_APPROVED, EDC_RESP_APPROVED_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_MISSING_STX, EDC_RESP_MISSING_STX_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_MISSING_ETX, EDC_RESP_MISSING_ETX_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_INCORRECT_MESSAGE_LEN, EDC_RESP_INCORRECT_MESSAGE_LEN_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_LRC_NOT_MATCH, EDC_RESP_LRC_NOT_MATCH_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_INCORRECT_TAG, EDC_RESP_INCORRECT_TAG_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_INCORRECT_LEN, EDC_RESP_INCORRECT_LEN_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_FINGER_TEMPLATE_TOO_BIG, EDC_RESP_FINGER_TEMPLATE_TOO_BIG_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_ENROLLMENT_KO, EDC_RESP_ENROLLMENT_KO_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_ABORT_BY_USER, EDC_RESP_ABORT_BY_USER_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_INVALID_LENGTH_PIN, EDC_RESP_INVALID_LENGTH_PIN_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_PIN_NOT_MATCH, EDC_RESP_PIN_NOT_MATCH_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_INVALID_INDEX_LABEL_FINGER, EDC_RESP_INVALID_INDEX_LABEL_FINGER_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_ERR_TIMEOUT, EDC_RESP_ERR_TIMEOUT_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_ERR_SAM_NOT_ACTIVED, EDC_RESP_ERR_SAM_NOT_ACTIVED_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_ERR_FAILED_READ_CARD, EDC_RESP_ERR_FAILED_READ_CARD_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_ERR_INVALID_FINGER_FLAG, EDC_RESP_ERR_INVALID_FINGER_FLAG_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_ERR_CARD_RESET_FAILED, EDC_RESP_ERR_CARD_RESET_FAILED_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_ERR_AUTH_FAILED, EDC_RESP_ERR_AUTH_FAILED_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_ERR_NASABAH_DOESNT_EXIST, EDC_RESP_ERR_NASABAH_DOESNT_EXIST_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_ERR_GET_FINGER_INDEX_03, EDC_RESP_ERR_GET_FINGER_INDEX_03_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_ERR_GET_FINGER_INDEX_04, EDC_RESP_ERR_GET_FINGER_INDEX_04_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_ERR_UPDATE_FINGER_01_FAILED, EDC_RESP_ERR_UPDATE_FINGER_01_FAILED_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_ERR_UPDATE_FINGER_02_FAILED, EDC_RESP_ERR_UPDATE_FINGER_02_FAILED_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_PROBLEM_APDU, EDC_RESP_PROBLEM_APDU_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_BIOMETRIC_MISMATCH, EDC_RESP_BIOMETRIC_MISMATCH_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_FAILED_WRITE_FINGER_1, EDC_RESP_FAILED_WRITE_FINGER_1_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_FAILED_WRITE_FINGER_2, EDC_RESP_FAILED_WRITE_FINGER_2_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_FAILED_LOCK_APDU, EDC_RESP_FAILED_LOCK_APDU_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_CANT_UDPATE_NASABAH, EDC_RESP_CANT_UDPATE_NASABAH_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_MK_WK_NOT_LOADED, EDC_RESP_MK_WK_NOT_LOADED_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_VERIFICATION_FAILED, EDC_RESP_VERIFICATION_FAILED_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_OTHERS, EDC_RESP_OTHERS_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_MISMATCH_NUMBER_CMS, EDC_RESP_MISMATCH_NUMBER_CMS_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_FAILED_UPDATE_OFFICER_CARD, EDC_RESP_FAILED_UPDATE_OFFICER_CARD_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_OFFICER_CARD_INVALID, EDC_RESP_OFFICER_CARD_INVALID_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_ALREADY_ACTIVATED, EDC_RESP_ALREADY_ACTIVATED_STR);
		EDC_RESPONSE_CODE_MAP.put(EDC_RESP_FINGER_BAD_QUALITY, EDC_RESP_FINGER_BAD_QUALITY_STR);
	}

	//ENROLL FINGERPRINT
	public static final String TAG_FINGER_DATA_1	= "51";
	public static final String TAG_FINGER_DATA_2	= "52";
	public static final String TAG_FINGER_SCORING_1 = "61";
	public static final String TAG_FINGER_SCORING_2 = "62";
	public static final String TAG_LEN_GT_128		= "81";
	public static final String TAG_LEN_GT_255		= "82";
	public static final String TAG_PAN_VALUE		= "91";
	public static final String TAG_OLD_PIN			= "94";
	public static final String TAG_NEW_PIN			= "95";

}
