package com.btpn.sinaya.eform.utils;

import com.btpn.sinaya.eform.database.MTFDatabaseHelper;

public class MTFSystemParams {

	private static final String SYS_PARM_MAX_OFFLINE_LOGIN = "MAX_OFFLINE_LOGIN_DAYS_MOBILE";
	private static final String SYS_PARM_CUSTOMER_DATA_DURATION = "CUSTOMER_DATA_DURATION_MOBILE";
	private static final String SYS_PARM_CUSTOMER_COMPLETE_DURATION = "CUSTOMER_COMPLETE_DURATION_MOBILE";
	private static final String SYS_PARM_IMAGE_HEIGHT = "IMAGE_HEIGHT_MOBILE";
	private static final String SYS_PARM_IMAGE_WIDTH = "IMAGE_WIDTH_MOBILE";
	private static final String SYS_PARM_IMAGE_QUALITY = "IMAGE_QUALITY_MOBILE";
	private static final String SYS_PARM_MAX_CHUNK_SIZE = "MAX_CHUNK_SIZE";
	private static final String SYS_PARM_REGISTER_GCM_STATUS = "REGISTER_GCM_STATUS";
	private static final String SYS_PARM_USE_HTTPS = "USE_HTTPS";
	private static final String SYS_PARM_MINIMUM_STORAGE_MOBILE = "MINIMUM_STORAGE_MOBILE";
	private static final String SYS_PARM_SPECIAL_CHARACTER = "SPECIAL_CHARACTER";
	private static final String SYS_PARM_MOBILE_TIME_OUT = "MOBILE_TIME_OUT";
    private static final String SYS_PARM_WHITE_LIST = "INPUT_WHITELIST";
	private static final String SYS_PARM_INPUT_CALLBACK_START = "INPUT_CALLBACK_START";
	private static final String SYS_PARM_INPUT_CALLBACK_END = "INPUT_CALLBACK_END";
	private static final String SYS_PARM_INPUT_CALLBACK_START_GAP = "INPUT_CALLBACK_START_GAP";
	private static final String SYS_PARM_INPUT_CALLBACK_END_GAP = "INPUT_CALLBACK_END_GAP";
	private static final String SYS_PARM_MAX_DEBIT_DATE = "MAX_DEBIT_DATE";
	private static final String SYS_PARM_EMAIL_ACCOUNT_STATEMENT = "EMAIL_ACCOUNT_STATEMENT";
    private static final String SYS_PARM_BORDER_MOBILE_MARGIN = "BORDER_MOBILE_MARGIN";
    private static final String SYS_PARM_BORDER_MOBILE_WIDTH_RATIO = "BORDER_MOBILE_WIDTH_RATIO";
    private static final String SYS_PARM_BORDER_MOBILE_HEIGHT_RATIO = "BORDER_MOBILE_HEIGHT_RATIO";

	public static boolean isSchedulerDisable = false;

	public static String schedulerStart = "00:00";
	public static int schedulerInterval = 5;
	public static int imageQuality = 80;
	public static int imageWidth = 600;
	public static int imageHeight = 600;
	public static int customerDataDuration = 1;
	public static int customerCompleteDuration = 10;
	public static int maxOfflineLoginDays = 3;
	public static int maxChunkSize = 18;
	public static boolean isUseHTTPS = true;
	public static int secureImageSize = 10;
	public static int minimumStorageMobile = 10;
	public static Integer timeOut = 1200000; //2 minutes
    public static String specialCharacter = "<>&'\"”‹›’‘“";
    public static String whiteList = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ 1234567890,.";
	public static int mobileTimeOut = 70000;
	public static String callbackStart = "10:30";
	public static String callbackEnd = "16:30";
	public static int callbackStartGap = 30;
	public static int callbackEndGap = 30;
	public static int maxDebitDate = 26;
	public static String emailAccStat = "03";

	public static int margin = 100;
	public static int width_ratio = 89;
	public static int height_ratio = 56;

	public static boolean isRegisteringToGCM = false;
	
	public static void reloadSystemParamsFromDatabase(MTFDatabaseHelper databaseHelper){
		String maxOfflineLoginDaysString = databaseHelper.getSysParValue(SYS_PARM_MAX_OFFLINE_LOGIN);
		String customerDataDurationString = databaseHelper.getSysParValue(SYS_PARM_CUSTOMER_DATA_DURATION);
		String customerCompleteDurationString = databaseHelper.getSysParValue(SYS_PARM_CUSTOMER_COMPLETE_DURATION);
		String imageHeightString = databaseHelper.getSysParValue(SYS_PARM_IMAGE_HEIGHT);
		String imageWidthString = databaseHelper.getSysParValue(SYS_PARM_IMAGE_WIDTH);
		String imageQualityString = databaseHelper.getSysParValue(SYS_PARM_IMAGE_QUALITY);
		String maxChunkSizeString = databaseHelper.getSysParValue(SYS_PARM_MAX_CHUNK_SIZE);
		String registerStatusToGCMString = databaseHelper.getSysParValue(SYS_PARM_REGISTER_GCM_STATUS);
		String isUseHTTPSString = databaseHelper.getSysParValue(SYS_PARM_USE_HTTPS);
		String minimumStorageMobileString = databaseHelper.getSysParValue(SYS_PARM_MINIMUM_STORAGE_MOBILE);
		String specialCharacterString = databaseHelper.getSysParValue(SYS_PARM_SPECIAL_CHARACTER);
		String mobileTimeOutString = databaseHelper.getSysParValue(SYS_PARM_MOBILE_TIME_OUT);
		String whiteListString = databaseHelper.getSysParValue(SYS_PARM_WHITE_LIST);
		String callbackStartString = databaseHelper.getSysParValue(SYS_PARM_INPUT_CALLBACK_START);
		String callbackEndString = databaseHelper.getSysParValue(SYS_PARM_INPUT_CALLBACK_END);
		String callbackStartGapString = databaseHelper.getSysParValue(SYS_PARM_INPUT_CALLBACK_START_GAP);
		String callbackEndGapString = databaseHelper.getSysParValue(SYS_PARM_INPUT_CALLBACK_END_GAP);
		String maxDebitDateString = databaseHelper.getSysParValue(SYS_PARM_MAX_DEBIT_DATE);
		String emailAccountStatement = databaseHelper.getSysParValue(SYS_PARM_EMAIL_ACCOUNT_STATEMENT);
        String marginString = databaseHelper.getSysParValue(SYS_PARM_BORDER_MOBILE_MARGIN);
        String widthString = databaseHelper.getSysParValue(SYS_PARM_BORDER_MOBILE_WIDTH_RATIO);
        String heightString = databaseHelper.getSysParValue(SYS_PARM_BORDER_MOBILE_HEIGHT_RATIO);

		if (maxOfflineLoginDaysString != null) {
			maxOfflineLoginDays = Integer.parseInt(maxOfflineLoginDaysString);
		}
		
		if (customerDataDurationString != null) {
			customerDataDuration = Integer.parseInt(customerDataDurationString);
		}
		
		if (customerCompleteDurationString != null){
			customerCompleteDuration = Integer.parseInt(customerCompleteDurationString);
		}
		
		if (imageHeightString != null) {
			imageHeight = Integer.parseInt(imageHeightString);
		}
		
		if (imageWidthString != null) {
			imageWidth = Integer.parseInt(imageWidthString);
		}
		
		if (imageQualityString != null) {
			imageQuality = Integer.parseInt(imageQualityString);
		}
		
		if (maxChunkSizeString != null) {
			maxChunkSize = Integer.parseInt(maxChunkSizeString);
		}
		
		if (registerStatusToGCMString !=null) {
			isRegisteringToGCM = registerStatusToGCMString.equals("1");
		}
		
		if (isUseHTTPSString != null) {
			isUseHTTPS = isUseHTTPSString.equals("1");
		}
		
		if (minimumStorageMobileString != null) {
			minimumStorageMobile = Integer.parseInt(minimumStorageMobileString);
		}
		
		if (specialCharacterString != null && !specialCharacterString.trim().equals("")){
			specialCharacter = specialCharacterString;
		}

        if (whiteListString != null && !whiteListString.trim().equals("")){
            whiteList = whiteListString;
        }
		
		if (mobileTimeOutString != null){
			mobileTimeOut = Integer.parseInt(mobileTimeOutString);
		}

		if (callbackStartString != null){
			callbackStart = callbackStartString;
		}

		if (callbackEndString != null){
			callbackEnd = callbackEndString;
		}

		if (callbackStartGapString != null){
			callbackStartGap = Integer.parseInt(callbackStartGapString);
		}

		if (callbackEndGapString != null){
			callbackEndGap = Integer.parseInt(callbackEndGapString);
		}

		if (maxDebitDateString != null){
			maxDebitDate = Integer.parseInt(maxDebitDateString);
		}

		if(emailAccountStatement != null){
			emailAccStat = emailAccountStatement;
		}

        if (marginString != null){
            margin = Integer.parseInt(marginString);
        }

        if (heightString != null){
            height_ratio = Integer.parseInt(heightString);
        }

        if (widthString != null){
            width_ratio = Integer.parseInt(widthString);
        }
	}
}
