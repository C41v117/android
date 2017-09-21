package com.btpn.sinaya.eform.utils;

import android.content.Context;
import android.util.Log;

import com.btpn.sinaya.eform.connection.MTFConnectionManager;
import com.btpn.sinaya.eform.database.MTFDatabaseHelper;
import com.btpn.sinaya.eform.model.MTFMasterDataFormContentModel;
import com.btpn.sinaya.eform.model.MTFVersionModel;
import com.btpn.sinaya.eform.model.type.MTFMasterDataType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MTFDataRawLoader {
	
	public static void load(Context context, MTFDatabaseHelper database){
		try {
			InputStream is1;
            StringBuilder sb = new StringBuilder();
            is1 = context.getAssets().open(MTFConnectionManager.dataMTF);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));
            while (true) {
                String readLine = br1.readLine();
                if (readLine == null) {
                    break;
                }

                sb.append(readLine);
            }

			JSONObject jsonAllData = new JSONObject(sb.toString());

            //master data insert when on create, so don't delete them
//			database.deleteMasterData();
			database.deleteSysParm();
			loadFromJson(jsonAllData, database, false);
			
			if (jsonAllData.has(MTFJSONKey.KEY_MD_CURR_VERS)) {
				updateCurrentVersoion(jsonAllData.getJSONArray(MTFJSONKey.KEY_MD_CURR_VERS), database);
			}
			
		} catch (IOException e) {
			
		} catch (JSONException e) {
			
		}
	}
	
	public static boolean loadFromJson(JSONObject jsonMasterData, MTFDatabaseHelper databaseHelper, boolean isDeleteFirst){
		boolean result = false;
		try {

			if (jsonMasterData.has(MTFJSONKey.KEY_MD_SYSTEM_PARAMS)) {
				updateSystemParametersData(jsonMasterData.getJSONArray(MTFJSONKey.KEY_MD_SYSTEM_PARAMS), databaseHelper, isDeleteFirst);
			}
			
			result = true;
		} catch (JSONException e) {
            Log.e("YOUR_APP_LOG_TAG", "I got an error", e);
		} catch (Exception e){
            e.printStackTrace();
        }
		
		return result;
	}
	
	private static void updateCurrentVersoion(JSONArray jsonArrayVersions, MTFDatabaseHelper database) throws JSONException{
		for (int i = 0; i < jsonArrayVersions.length(); i++) {
			JSONObject jsonVersion = jsonArrayVersions.getJSONObject(i);
			String modulType = jsonVersion.getString(MTFJSONKey.KEY_MODULE_TYPE);
			int version = jsonVersion.getInt(MTFJSONKey.KEY_VERSION);
			
			updateVersion(modulType, version, database);
		}
	}

	private static void updateSystemParametersData(JSONArray jsonArraySysParams, MTFDatabaseHelper database, boolean isDeleteFirst) throws JSONException{
		for (int i = 0; i < jsonArraySysParams.length(); i++) {
			JSONObject jsonSysParam = jsonArraySysParams.getJSONObject(i);
			String code = jsonSysParam.getString(MTFJSONKey.KEY_NAME);
			String value = jsonSysParam.getString(MTFJSONKey.KEY_VALUE);
			int status = jsonSysParam.getInt(MTFJSONKey.KEY_STATUS);
			if (status == MTFJSONKey.VAL_STATUS_LOOKUP_INACTIVE) {
				//Delete from Database
				database.deleteSysParm(code);
			}else{
				//Save to Database
				database.insertSysPar(code, value, isDeleteFirst);
			}
		}
	}

	private static void updateVersion(String modulType, int version, MTFDatabaseHelper database) throws JSONException {
		MTFVersionModel versionModel = new MTFVersionModel(modulType, version);
		
		database.insertMasterDataVersion(versionModel);
	}

    private static MTFMasterDataFormContentModel generateMasterDataContentModelFromPartnerCodeJson(JSONObject json) throws JSONException {
        MTFMasterDataFormContentModel result = new MTFMasterDataFormContentModel();
        //PartnerCode is different table from lookup in server, we must add the id so that it doesn't konflik with lookup
        result.setId(json.getLong(MTFJSONKey.KEY_ID)+1500000);
        result.setTitle(json.getString(MTFJSONKey.KEY_MITRA_NAME));
        result.setMasterDataType(MTFMasterDataType.PARTNER_CODE);
        result.setCode(json.getString(MTFJSONKey.KEY_MITRA_ID));

        if (json.has(MTFJSONKey.KEY_DESCRIPTION)) {
            result.setParent(json.getString(MTFJSONKey.KEY_DESCRIPTION));
        }

        if (json.has(MTFJSONKey.KEY_HIGH_RISK)){
            result.setHighRisk(json.getBoolean(MTFJSONKey.KEY_HIGH_RISK));
        }

        return result;
    }

	private static MTFMasterDataFormContentModel generateMasterDataContentModelFromLookUpJson(JSONObject json) throws JSONException {
		MTFMasterDataFormContentModel result = new MTFMasterDataFormContentModel();
		result.setId(json.getLong(MTFJSONKey.KEY_ID));
		result.setTitle(json.getString(MTFJSONKey.KEY_NAME));
		result.setMasterDataType(generateMasterDataType(json.getString(MTFJSONKey.KEY_LOOKUP_GROUP_STRING)));
		result.setCode(json.getString(MTFJSONKey.KEY_CODE));
		
		if (json.has(MTFJSONKey.KEY_DESCRIPTION)) {
			result.setParent(json.getString(MTFJSONKey.KEY_DESCRIPTION));
		}
		
		if (json.has(MTFJSONKey.KEY_HIGH_RISK)){
			result.setHighRisk(json.getBoolean(MTFJSONKey.KEY_HIGH_RISK));
		}
		
		return result;
	}
	
	private static MTFMasterDataType generateMasterDataType(String lookUpGroup){
		if(lookUpGroup.equalsIgnoreCase("PRODUCT_TYPE")){
			return MTFMasterDataType.PRODUCT_TYPE;
		}else if(lookUpGroup.equalsIgnoreCase("ACCOUNT_TYPE")){
			return MTFMasterDataType.ACCOUNT_TYPE;
		}else if(lookUpGroup.equalsIgnoreCase("LAW_RELATION")){
			return MTFMasterDataType.LAW_RELATION;
		}else if(lookUpGroup.equalsIgnoreCase("APPLICANT_TITLE")){
			return MTFMasterDataType.APPLICANT_TITLE;
		}else if(lookUpGroup.equalsIgnoreCase("APPLICANT_TYPE")){
			return MTFMasterDataType.APPLICANT_TYPE;
		}else if(lookUpGroup.equalsIgnoreCase("BRANCH_LOCATION")){
			return MTFMasterDataType.BRANCH_LOCATION;
		}else if(lookUpGroup.equalsIgnoreCase("TAX_TYPE")){
			return MTFMasterDataType.TAX_TYPE;
		}else if(lookUpGroup.equalsIgnoreCase("ID_TYPE")){
			return MTFMasterDataType.ID_TYPE;
		}else if(lookUpGroup.equalsIgnoreCase("HOUSE_STATUS")){
			return MTFMasterDataType.HOUSE_STATUS;
		}else if(lookUpGroup.equalsIgnoreCase("OCCUPATION")){
			return MTFMasterDataType.OCCUPATION;
		}else if(lookUpGroup.equalsIgnoreCase("JOB")){
			return MTFMasterDataType.JOB;
		}else if(lookUpGroup.equalsIgnoreCase("INDUSTRY")){
			return MTFMasterDataType.INDUSTRY;
		}else if(lookUpGroup.equalsIgnoreCase("GENDER")){
			return MTFMasterDataType.GENDER;
		}else if(lookUpGroup.equalsIgnoreCase("NATIONALITY")){
			return MTFMasterDataType.NATIONALITY;
		}else if(lookUpGroup.equalsIgnoreCase("RESIDENCE_STATUS")){
			return MTFMasterDataType.RESIDENCE_STATUS;
		}else if(lookUpGroup.equalsIgnoreCase("RELIGION")){
			return MTFMasterDataType.RELIGION;
		}else if(lookUpGroup.equalsIgnoreCase("MARITAL_STATUS")){
			return MTFMasterDataType.MARITAL_STATUS;
		}else if(lookUpGroup.equalsIgnoreCase("LAST_EDUCATION")){
			return MTFMasterDataType.LAST_EDUCATION;
		}else if(lookUpGroup.equalsIgnoreCase("COUNTRY")){
			return MTFMasterDataType.COUNTRY;
		}else if(lookUpGroup.equalsIgnoreCase("PROVINCE")){
			return MTFMasterDataType.PROVINCE;
		}else if(lookUpGroup.equalsIgnoreCase("CITY_LIST")){
			return MTFMasterDataType.CITY_LIST;
		}else if(lookUpGroup.equalsIgnoreCase("DISTRICT_LIST")){
			return MTFMasterDataType.DISTRICT_LIST;
		}else if(lookUpGroup.equalsIgnoreCase("VILLAGE_LIST")){
			return MTFMasterDataType.VILLAGE_LIST;
		}else if(lookUpGroup.equalsIgnoreCase("INCOME")){
			return MTFMasterDataType.INCOME;
		}else if(lookUpGroup.equalsIgnoreCase("ADDITION_INCOME")){
			return MTFMasterDataType.ADDITION_INCOME;
		}else if(lookUpGroup.equalsIgnoreCase("FORECAST_TRANSACTION")){
			return MTFMasterDataType.FORECAST_TRANSACTION;
		}else if(lookUpGroup.equalsIgnoreCase("PURPOSE_OF_ACCOUNT")){
			return MTFMasterDataType.PURPOSE_OF_ACCOUNT;
		}else if(lookUpGroup.equalsIgnoreCase("CREDIT_LIST")){
			return MTFMasterDataType.CREDIT_LIST;
		}else if(lookUpGroup.equalsIgnoreCase("SOURCE_OF_FUND")){
			return MTFMasterDataType.SOURCE_OF_FUND;
		}else if(lookUpGroup.equalsIgnoreCase("APPLICATION_SOURCE")){
			return MTFMasterDataType.APPLICATION_SOURCE;
		}else if(lookUpGroup.equalsIgnoreCase("NOTIFICATION_TYPE")){
			return MTFMasterDataType.NOTIFICATION_TYPE;
		}else if(lookUpGroup.equalsIgnoreCase("ACCOUNT_STATEMENT")){
			return MTFMasterDataType.ACCOUNT_STATEMENT;
		}else if(lookUpGroup.equalsIgnoreCase("TIPE_REKENING")){
            return MTFMasterDataType.TIPE_REKENING;
        }else if(lookUpGroup.equalsIgnoreCase("KANTOR_BAYAR")){
            return MTFMasterDataType.KANTOR_BAYAR;
        }else if(lookUpGroup.equalsIgnoreCase("JENIS_NASABAH")){
            return MTFMasterDataType.JENIS_NASABAH;
        }else if(lookUpGroup.equalsIgnoreCase("GOLONGAN")){
            return MTFMasterDataType.GOLONGAN;
        }else if(lookUpGroup.equalsIgnoreCase("HEIR_RELATIONSHIP")){
            return MTFMasterDataType.HEIR_RELATIONSHIP;
        }else if(lookUpGroup.equalsIgnoreCase("HUBUNGAN_DENGAN_NASABAH")){
            return MTFMasterDataType.HUBUNGAN_DENGAN_NASABAH;
        }else if(lookUpGroup.equalsIgnoreCase("LOR_KODE_PENSIUN"))
			return MTFMasterDataType.LOR_KODE_PENSIUN;
		else if(lookUpGroup.equalsIgnoreCase("JENIS_PENSIUN"))
			return MTFMasterDataType.JENIS_PENSIUN;
		return MTFMasterDataType.UNKNOWN;
	}
	
}
