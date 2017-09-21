package com.btpn.sinaya.eform.utils;

public class MTFIntentConstant {
	
	public static final String appName = "||WOW! Mobile";
	
	public static final String INTENT_EXTRA_DATA = "data";
    public static final String INTENT_EXTRA_DATA_USER_OTHER_APK = "dataUserOtherApk";
    public static final String INTENT_EXTRA_DATA_CUSTOMER_OTHER_APK = "dataCustomerOtherApk";
	public static final String INTENT_EXTRA_MESSAGE = "message";
	public static final String INTENT_EXTRA_PHONE_NUMBER = "phone_number";
	public static final String INTENT_EXTRA_PASSWORD = "password";
	public static final String INTENT_EXTRA_TOKEN = "token";
	public static final String INTENT_EXTRA_IMEI = "imei";
	public static final String INTENT_EXTRA_CODE = "code";
	public static final String INTENT_EXTRA_VERSION = "data";
	public static final String INTENT_EXTRA_USER_ID = "user_id";
	public static final String INTENT_EXTRA_CUSTOMER_ID = "customer_id";
	public static final String INTENT_EXTRA_CUSTOMER = "customer";
	public static final String INTENT_EXTRA_FULL_NAME = "fullname";
	public static final String INTENT_EXTRA_MOTHER_NAME = "motherName";
	public static final String INTENT_EXTRA_BIRTHDATE = "birthdate";
	public static final String INTENT_EXTRA_BIRTH_PLACE = "birth_place";
	public static final String INTENT_EXTRA_IDENTITY = "identity";
	public static final String INTENT_EXTRA_SALES_USER = "salesUserId";
	public static final String INTENT_EXTRA_SIGNATURE = "signature";
	public static final String INTENT_EXTRA_APPLICANT = "applicantModel";
	public static final String INTENT_EXTRA_CIF = "cif";
	public static final String INTENT_EXTRA_CIF_2 = "cif2";
	public static final String INTENT_EXTRA_TYPE = "type";
	public static final String INTENT_EXTRA_PARENT_ID = "parentId";
	public static final String INTENT_EXTRA_IS_DIRECT = "is_direct";
	public static final String INTENT_EXTRA_REG_ID = "reg_id";
	public static final String INTENT_EXTRA_BRANCH_NAME = "branch_name";
	public static final String INTENT_EXTRA_AGREEMENT = "agreement";
	public static final String INTENT_EXTRA_MENU = "menu";
	public static final String INTENT_EXTRA_REPAIR = "repair";
    public static final String INTENT_EXTRA_DEVIASI = "deviasi";
    public static final String INTENT_CUSTOMER_TYPE = "customerType";
	
	public static final String INTENT_SAVE_PATH = "/DCIM/Camera/";
	public static final String INTENT_TYPE = "type";
	
	public static final String ACTION_LOGIN_NORMAL = "login_normal";
	public static final String ACTION_LOGOUT = "logout";
	public static final String ACTION_CHECK_CIF = "check_cif";
	public static final String ACTION_CHECK_DHIB = "check_dhib";
	public static final String ACTION_NEW_CUSTOMER = "new_customer";
	public static final String ACTION_NEW_CUSTOMER_PURNA = "new_customer_purna";
	public static final String ACTION_UPDATE_CUSTOMER = "update_customer";
	public static final String ACTION_UPDATE_CUSTOMER_PURNA = "update_customer_purna";
	public static final String ACTION_PROFILE = "profile";
	public static final String ACTION_MASTER_DATA = "master_data";
	public static final String ACTION_NOTIFICATION = "get_notification";
    public static final String ACTION_VERIFY_CARD = "verify_card";
    public static final String ACTION_CUSTOMER_DETAIL = "get_customer_detail";
    public static final String ACTION_CUSTOMER_DETAIL_PURNA = "get_customer_detail_purna";
	public static final String ACTION_CUSTOMER_LIST = "get_customer_list";
	public static final String ACTION_CUSTOMER_LIST_PURNA = "get_customer_list_purna";
	public static final String ACTION_CUSTOMER_LIST_VERIFY = "get_customer_list_verify";
	public static final String ACTION_CUSTOMER_LIST_PENGKINIAN = "get_customer_list_pengkinian";
	public static final String ACTION_NEW_LOAN_LIST = "get_new_loan_list";
	public static final String ACTION_RENEWAL_LIST = "get_renewal_list";
	public static final String ACTION_VERIFY_LOAN_LIST = "get_verify_loan_list";
	public static final String ACTION_ACTION_CODE_LIST = "get_action_code_list";
	public static final String ACTION_IMAGE_UPDATE = "imageUpdate";
	public static final String ACTION_SEND_PROFILE = "get_sender_id";
	public static final String ACTION_START_SYNC = "start_sync";
	public static final String ACTION_SEND_REGID = "send_reg_id";
	public static final String ACTION_CLEAR_DATA = "clear_data";
	public static final String ACTION_CHECK_ROCODE = "check_rocode";
	public static final String ACTION_CHECK_ACCT_NO = "check_acct_no";
	public static final String ACTION_INTERRUPT = "interrupt_action";
	public static final String CATEGORY_INTERRUPT = "interrupt_category";
	public static final String ACTION_SHOW_REMINDER = "show_reminder";
	public static final String ACTION_FIND_ALL_BY_NOPEN_AND_NOREK = "find_all_by_nopen_and_norek";
	public static final String ACTION_INQUIRY_CUSTOMER_CARD	= "inquiry_customer_card";

	public static final String ACTION_SEND_DATA_SCHEDULER = "send_data_scheduler";
	public static final String ACTION_NEED_LOGIN_SCHEDULER = "need_login_scheduler";
	public static final String ACTION_INTERRUPT_SCHEDULER = "interrupt_scheduler";

	public static final int CODE_SUCCESS = 1;
	public static final int CODE_FAILED = 0;
	public static final int CODE_SUCCESS_SEND_DATA = 201;
	public static final int CODE_SUCCESS_SEND_FILE = 202;
	public static final int CODE_SUCCESS_SEND_ALL_DATA = 203;
    public static final int CODE_SUCCESS_RETRY_CHECK_CHUNK = 204;
//	public static final int CODE_SUCCESS_SEND_IDENTITY = 203;
//	public static final int CODE_SUCCESS_SEND_NPWP = 204;
//	public static final int CODE_SUCCESS_SEND_SIGNATURE = 205;
//	public static final int CODE_SUCCESS_SEND_APPLICANT = 206;
	public static final int CODE_FAILED_SEND_DATA = 207;
	public static final int CODE_FAILED_SEND_FILE = 208;
//	public static final int CODE_FAILED_SEND_IDENTITY = 209;
//	public static final int CODE_FAILED_SEND_NPWP = 210;
//	public static final int CODE_FAILED_SEND_SIGNATURE = 211;
//	public static final int CODE_FAILED_SEND_APPLICANT = 212;
	public static final int CODE_FAILED_SEND_REGID = 213;
	public static final int CODE_SUCCESS_SEND_REGID = 214;
	public static final int CODE_FAILED_TOKEN = 215;
	public static final int CODE_FAILED_CHECK_CIF = 216;
	public static final int CODE_FAILED_CHECK_DHIB = 217;
	public static final int CODE_SUCCESS_CHECK_CIF = 218;
	public static final int CODE_SUCCESS_CHECK_DHIB = 219;
    public static final int CODE_FAILED_FF = 220;

	public static final int CODE_LOGIN_SUCCESS = 300;
	public static final int CODE_LOGIN_SUCCESS_OFFLINE = 301;
	public static final int CODE_MASTER_DATA_SUCCESS = 302;
	public static final int CODE_LOGOUT_SUCCESS = 303;
	public static final int CODE_REQUEST_AGENT_PROFILE_SUCCESS = 304;
	public static final int CODE_SENDER_ID_SUCCESS = 305;

	public static final int CODE_LOGIN_FAILED_DIFF_VER = 401;
	public static final int CODE_LOGIN_FAILED_INVALID = 402;
	public static final int CODE_REQUEST_OTP_FAILED = 403;
	public static final int CODE_SEND_OTP_FAILED = 405;
	public static final int CODE_MASTER_DATA_FAILED = 406;
	public static final int CODE_LOGIN_CHANGE_DEVICE = 407;
	public static final int CODE_LOGOUT_FAILED = 408;
	public static final int CODE_REQUEST_AGENT_PROFILE_FAILED = 409;
	public static final int CODE_SENDER_ID_FAILED = 410;
	public static final int CODE_SUCCESS_CUSTOMER_LIST = 411;
	public static final int CODE_FAILED_CUSTOMER_LIST = 412;
	public static final int CODE_FAILED_RO_CODE = 413;
	public static final int CODE_SUCCESS_RO_CODE = 414;
	public static final int CODE_SUCCESS_ACCT_NO= 415;
	public static final int CODE_FAILED_ACCT_NO = 416;
	public static final int CODE_SUCCESS_CUSTOMER_LIST_PURNA = 417;
	public static final int CODE_SUCCESS_CUSTOMER_LIST_VERIFY = 418;
//	public static final int CODE_SUCCESS_CUSTOMER_LIST_PENGKINIAN = 419;
	public static final int CODE_SUCCESS_NEW_LOAN_LIST = 420;
	public static final int CODE_SUCCESS_RENEWAL_LIST = 421;
	public static final int CODE_SUCCESS_ACTION_CODE = 422;
	
	public static final int CODE_SUCCESS_CUSTOMER_DETAIL = 501;
	public static final int CODE_SUCCESS_UPDATE_NOTIFICATION = 502;

	public static final int CODE_FAILED_CUSTOMER_DETAIL = 601;
	public static final int CODE_FAILED_UPDATE_NOTIFICATION = 602;

	public static final int CODE_SUCCESS_FIND_ALL_BY_NOPEN_AND_NOREK = 701;
	public static final int CODE_FAILED_FIND_ALL_BY_NOPEN_AND_NOREK = 702;
	public static final int CODE_SUCCESS_INQUIRY_CUSTOMER_CARD = 711;
	public static final int CODE_FAILED_INQUIRY_CUSTOMER_CARD = 712;

	public static final int CODE_FAILED_RESPONSE_CIF = 716;
	
	public static final int FILE_TYPE_IDENTITY = 501;
	public static final int FILE_TYPE_NPWP = 502;
	public static final int FILE_TYPE_SIGNATURE = 503;
	public static final int FILE_TYPE_APPLICANT = 504;
	public static final int FILE_TYPE_UNKNOWN = 505;
	public static final int FILE_TYPE_SECOND_IDENTITY = 506;
	public static final int FILE_TYPE_SECOND_NPWP = 507;
	public static final int FILE_TYPE_SECOND_SIGNATURE = 508;
	public static final int FILE_TYPE_SECOND_APPLICANT = 509;
	public static final int FILE_TYPE_DOK_PENDUKUNG = 510;
	public static final int FILE_TYPE_SECOND_DOK_PENDUKUNG = 511;
    public static final int FILE_TYPE_KK = 512;
    public static final int FILE_TYPE_SELFIE = 513;
	
	public static final String CATEGORY_LOGIN = "login";
	public static final String CATEGORY_REGISTER_CUSTOMER = "register_customer";
	public static final String CATEGORY_CUSTOMER_LIST = "customer_list";
	public static final String CATEGORY_CUSTOMER_LIST_PURNA = "customer_list_purna";
	public static final String CATEGORY_CUSTOMER_LIST_VERIFY = "customer_list_verify";
	public static final String CATEGORY_CUSTOMER_LIST_PENGKINIAN = "customer_list_pengkinian";
	public static final String CATEGORY_NEW_LOAN_LIST = "new_loan_list";
	public static final String CATEGORY_RENEWAL_LIST = "renewal_list";
	public static final String CATEGORY_VERIFY_LOAN_LIST = "verify_loan_list";
	public static final String CATEGORY_ACTION_CODE_LIST = "action_code_list";
	public static final String CATEGORY_PROFILE_DETAIL = "profile_detail";
	public static final String CATEGORY_ATTACHMENT = "category_attachment";
	public static final String CATEGORY_MENU = "menu";
	public static final String CATEGORY_FIND_ALL_BY_NOPEN_AND_NOREK = "find_all_by_nopen_and_norek";
	public static final String CATEGORY_INQUIRY_CUSTOMER_CARD = "inquiry_customer_card";

	public static final String BUNDLE_KEY_DATA = "data";
	public static final String BUNDLE_KEY_CODE = "code";
	public static final String BUNDLE_KEY_TYPE = "type";
	public static final String BUNDLE_KEY_REG_ID = "reg_id";
	public static final String BUNDLE_KEY_SENDER_ID = "sender_id";
	public static final String BUNDLE_KEY_RESULT = "result";
	public static final String BUNDLE_KEY_USERID = "user_id";
	public static final String BUNDLE_KEY_CUSTOMERID = "customer_id";
	public static final String BUNDLE_KEY_USERNAME = "username";
	public static final String BUNDLE_KEY_USER = "user";
	public static final String BUNDLE_KEY_VERSION = "version";
	public static final String BUNDLE_KEY_TOKEN = "token";
	public static final String BUNDLE_KEY_LOCATION_ID = "locationId";
    public static final String BUNDLE_KEY_JSON = "json";
    public static final String BUNDLE_KEY_SECRET = "secret";
	public static final String BUNDLE_KEY_LOCATION = "locationName";
	public static final String BUNDLE_KEY_AGENT_TYPE = "agentType";
	public static final String BUNDLE_KEY_MESSAGE = "message";
	public static final String BUNDLE_KEY_TITLE = "title";
	public static final String BUNDLE_KEY_PHONE_NUMBER = "phone";
	public static final String BUNDLE_KEY_CIF_LIST = "cif_list";
	public static final String BUNDLE_KEY_CIF_FORM = "cif_form";
	public static final String BUNDLE_KEY_IDENTITY_FORM = "identity_form";
	public static final String BUNDLE_KEY_PROFILE_FORM = "profile_form";
	public static final String BUNDLE_KEY_JOB_FORM = "job_form";
	public static final String BUNDLE_KEY_INCOME_FORM = "income_form";
	public static final String BUNDLE_KEY_ATTACHMENT_FORM = "attachment_form";
	public static final String BUNDLE_KEY_AGREEMENT_FORM = "agreement_form";
	public static final String BUNDLE_KEY_CIF_FORM_SECOND = "cif_form_second";
	public static final String BUNDLE_KEY_IDENTITY_FORM_SECOND = "identity_form_second";
	public static final String BUNDLE_KEY_PROFILE_FORM_SECOND = "profile_form_second";
	public static final String BUNDLE_KEY_JOB_FORM_SECOND = "job_form_second";
	public static final String BUNDLE_KEY_INCOME_FORM_SECOND = "income_form_second";
	public static final String BUNDLE_KEY_ATTACHMENT_FORM_SECOND = "attachment_form_second";
	public static final String BUNDLE_KEY_AGREEMENT_FORM_SECOND = "agreement_form_second";
	public static final String BUNDLE_KEY_CUSTOMER_FORM = "customer_form";
	public static final String BUNDLE_KEY_IMAGE_TYPE = "imageType";
	public static final String BUNDLE_KEY_FILE_NAME = "file_name";
	public static final String BUNDLE_KEY_FILE_PATH = "file_path";
	public static final String BUNDLE_KEY_FILE_TYPE = "file_type";
	public static final String BUNDLE_KEY_BIRTH_DATE = "birth_date";
	public static final String BUNDLE_KEY_EXPIRE_DATE = "expire_date";
	public static final String BUNDLE_KEY_START_PERIOD = "start_period";
	public static final String BUNDLE_KEY_END_PERIOD = "end_period";
	public static final String BUNDLE_KEY_LOI_PERIOD = "loi_period";
	public static final String BUNDLE_KEY_CIF_SHOW = "cif_show";
	public static final String BUNDLE_KEY_DHIB_SHOW = "dhib_show";
	public static final String BUNDLE_KEY_SEND_DATA_SHOW = "send_data_show";
	public static final String BUNDLE_KEY_LOADING_SHOW = "loading_show";
	public static final String BUNDLE_KEY_ALERT_SHOW = "alert_show";
	public static final String BUNDLE_KEY_REQUEST_LOGIN = "request_login";
	public static final String BUNDLE_KEY_REQUEST_MASTER_DATA = "request_master_data";
	public static final String BUNDLE_KEY_REQUEST_ACC_NO= "request_account_no";
	public static final String BUNDLE_KEY_TIMER = "timer";

	public static final String BUNDLE_KEY_PENSIUN="pensiun_date";
	
	public static final String BUNDLE_KEY_IS_IDENTITY_TAKEN = "isIdentity";
	public static final String BUNDLE_KEY_IS_NPWP_TAKEN = "isNPWP";
	public static final String BUNDLE_KEY_IS_SIGNATURE_TAKEN = "isSignature";
	public static final String BUNDLE_KEY_IS_APPLICANT_TAKEN = "isApplicant";
	public static final String BUNDLE_KEY_IS_DOK_PENDUKUNG_TAKEN = "isDokPendukung";

	public static final String BUNDLE_KEY_COUNTER = "counter";
	public static final String BUNDLE_KEY_MILLIS = "millisecond";
	public static final String BUNDLE_KEY_INTERVAL = "interval";
	public static final String BUNDLE_KEY_IS_MASTER_DATA = "is_master_data";

	//Scheduler
	public static final String BUNDLE_KEY_SCHEDULER = "is_scheduler";

	public static final int REQUEST_CODE_REGISTER_CUSTOMER = 100;
	public static final int REQUEST_CODE_REGISTER_CUSTOMER_PURNA = 101;
	public static final int REQUEST_CODE_REGISTER_NEW_LOAN = 102;
    public static final int REQUEST_CODE_VERIFY_CARD = 103;
	
	public static final int RESPONSE_NO_ACTION = -1;
	public static final int RESPONSE_RELOAD_CUSTOMER_LIST = 200;
	public static final int RESPONSE_START_NEW_CUSTOMER = 201;
	public static final int RESPONSE_START_HOME_ACTIVITY = 202;
	public static final int RESPONSE_KILL_APPS = 203;
	public static final int RESPONSE_RELOAD_TAB = 204;
	public static final int RESPONSE_RELOAD_CUSTOMER_LIST_PURNA = 205;
    public static final int RESPONSE_RELOAD_VERIFY_LIST = 206;

	public static final int SECOND = 1000;
	public static final int MINUTE = 60 * SECOND;
	public static final int HOUR = 60 * MINUTE;
	public static final int DAY = 24 * HOUR;
	public static final int DAYS_KEEP_SENT_SURVEY = 1 * DAY;
	
	//Enkrip Dekrip
	public static final boolean ENCRYPT_COMM = false;
	public static final boolean DECRYPT_COMM = false;

	public static final String BUNDLE_KEY_REMINDER_NEXT = "reminder_next_schedule";
	public static final String BUNDLE_KEY_REMINDER_END = "reminder_end_schedule";
	public static final String BUNDLE_KEY_REMINDER_INTERVAL = "reminder_interval";

	public static final String BUNDLE_KEY_LOANID="loan_id";
	public static final String BUNDLE_KEY_ANGSUR_DATE ="angsur_date";
}
