package com.btpn.sinaya.eform.database;


import android.content.ContentValues;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.btpn.sinaya.eform.model.MTFMasterDataFormContentModel;
import com.btpn.sinaya.eform.model.MTFUserModel;
import com.btpn.sinaya.eform.model.MTFVersionModel;
import com.btpn.sinaya.eform.model.type.MTFMasterDataType;
import com.btpn.sinaya.eform.utils.MTFGenerator;
import com.btpn.sinaya.eform.utils.MTFJSONKey;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MTFDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pantara-sinaya.db";
    private static final String LEGACY_DATABASE_NAME = "constants-sinaya.db";
    private static final byte[] PASSPHRASE = new byte[]{65, 13, -32, 64, -89, 23, -53, -50, -80, 54, -10, -40};
//    private static final byte[] PASSPHRASE = new byte[]{77, 69, 84, 65, 77, 79, 82, 70};
    private static final int SCHEMA = 1;
    private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat regFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final String TABLE_SYSTEM_PARAM = "system_parameter";
    private static final String TABLE_SESSION = "user_session";
    private static final String TABLE_MASTER_DATA_FORM = "master_data_form";
    private static final String TABLE_VERSION = "version";

    //master data form
    private static final String FORM_ID = "form_id";
    private static final String FORM_TITLE = "form_title";
    private static final String FORM_PARENT = "form_parent";
    private static final String FORM_TYPE = "form_type";
    private static final String FORM_CODE = "form_code";
    private static final String FORM_IS_HIGHRISK = "form_is_highrisk";

    //system param
    private static final String SYSPAR_ID = "pk_sys_param";
    private static final String SYSPAR_CODE = "code";
    private static final String SYSPAR_VALUE = "value";

    //user
    private static final String SESSION_ID = "pk_session";
    private static final String SESSION_USERNAME = "user_name";
    private static final String SESSION_PIN = "pin";
    private static final String SESSION_LATEST_LOGIN = "latest_login";
    private static final String SESSION_TOKEN = "token";
    private static final String SESSION_IMEI = "imei";
    private static final String SESSION_USER_ID = "userId";
    private static final String SESSION_USER_TYPE = "userType";
    private static final String SESSION_LOCATION_ID = "locationId";
    private static final String SESSION_LOCATION_NAME = "locationName";
    private static final String SESSION_IS_ONLINE = "is_online";
    private static final String SESSION_LOB = "lob";
    private static final String SESSION_DIVISION = "division";
    private static final String SESSION_RA_CODE = "raCode";
    private static final String SESSION_RO_CODE = "roCode";
    private static final String SESSION_ACC = "accessibility";
    private static final String SESSION_JWT = "jwt_token";
    private static final String SESSION_SECRET = "secret";

    //Version
    private static final String VERSION_ID = "pk_version";
    private static final String VERSION_TYPE = "version_type";
    private static final String VERSION_VALUE = "version_value";

    private static final String QUERY_CREATE_TABLE_SESSION =
            "CREATE TABLE " + TABLE_SESSION + " (" +
                    SESSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SESSION_USERNAME + " TEXT, " +
                    SESSION_PIN + " TEXT," +
                    SESSION_TOKEN + " TEXT," +
                    SESSION_IMEI + " TEXT," +
                    SESSION_LATEST_LOGIN + " NUMERIC," +
                    SESSION_USER_ID + " TEXT, " +
                    SESSION_USER_TYPE + " TEXT, " +
                    SESSION_LOCATION_ID + " INTEGER," +
                    SESSION_LOCATION_NAME + " TEXT, " +
                    SESSION_IS_ONLINE + " NUMERIC, " +
                    SESSION_LOB + " INTEGER," +
                    SESSION_DIVISION + " TEXT," +
                    SESSION_RA_CODE + " TEXT," +
                    SESSION_RO_CODE + " TEXT," +
                    SESSION_ACC + " TEXT," +
                    SESSION_JWT + " TEXT," +
                    SESSION_SECRET + " TEXT);";

    public static final String QUERY_CREATE_TABLE_MASTER_DATA_FORM =
            "CREATE TABLE " + TABLE_MASTER_DATA_FORM + "(" +
                    FORM_ID + " INTEGER PRIMARY KEY," +
                    FORM_TITLE + " TEXT, " +
                    FORM_PARENT + " TEXT, " +
                    FORM_CODE + " TEXT, " +
                    FORM_IS_HIGHRISK + " NUMERIC," +
                    FORM_TYPE + " INTEGER);";

    private static final String QUERY_CREATE_TABLE_SYSPARAM =
            "CREATE TABLE " + TABLE_SYSTEM_PARAM + " (" +
                    SYSPAR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SYSPAR_CODE + " TEXT, " +
                    SYSPAR_VALUE + " TEXT);";

    private static final String QUERY_CREATE_TABLE_VERSION =
            "CREATE TABLE " + TABLE_VERSION + " (" +
                    VERSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    VERSION_TYPE + " TEXT, " +
                    VERSION_VALUE + " INTEGER);";

    private Context context;

    public MTFDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
        this.context = context;
    }

    private static MTFDatabaseHelper instance;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUERY_CREATE_TABLE_SESSION);
        db.execSQL(QUERY_CREATE_TABLE_SYSPARAM);
        db.execSQL(QUERY_CREATE_TABLE_MASTER_DATA_FORM);
        db.execSQL(QUERY_CREATE_TABLE_VERSION);
        insertMasterData(db);
    }

    private void injectKantorBayar(SQLiteDatabase database){
        try {
            InputStream is = context.getAssets().open("data_kantorbayar.mtf");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while (true) {
                String readLine = br.readLine();
                if (readLine == null) {
                    break;
                }
                database.execSQL(readLine);
            }

        } catch (IOException e) {

        }
    }

    private void insertMasterData(SQLiteDatabase db){
//        injectKantorBayar(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        switch (oldVer) {
            case 1:
                upgrade1(db);
                break;
            default:
                break;
        }

    }

    private void upgrade1(SQLiteDatabase db){
        String query = "DELETE FROM " + TABLE_MASTER_DATA_FORM + " WHERE " + FORM_TYPE + "=" + MTFMasterDataType.KANTOR_BAYAR.ordinal();
        db.execSQL(query);
        insertMasterData(db);
    }

    static void encrypt(Context ctxt) {
        SQLiteDatabase.loadLibs(ctxt);

        File dbFile = ctxt.getDatabasePath(DATABASE_NAME);
        File legacyFile = ctxt.getDatabasePath(LEGACY_DATABASE_NAME);

        if (!dbFile.exists() && legacyFile.exists()) {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(legacyFile,
                    "", null);

            db.rawExecSQL(String.format(
                    "ATTACH DATABASE '%s' AS encrypted KEY '%s';",
                    dbFile.getAbsolutePath(), PASSPHRASE));
            db.rawExecSQL("SELECT sqlcipher_export('encrypted')");
            db.rawExecSQL("DETACH DATABASE encrypted;");

            int version = db.getVersion();

            db.close();

            db = SQLiteDatabase.openOrCreateDatabase(dbFile, new String(PASSPHRASE), null);
            db.setVersion(version);
            db.close();

            legacyFile.delete();
        }

//        String a = new String(PASSPHRASE);
//
//        copyFile(dbFile);
    }

    private static void copyFile(File inputFile) {
        InputStream in = null;
        OutputStream out = null;
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/database.db";

            in = new FileInputStream(inputFile);
            out = new FileOutputStream(path);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        }  catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    public static MTFDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new MTFDatabaseHelper(context.getApplicationContext());
            encrypt(context);
        }
        return instance;
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    SQLiteDatabase getReadableDatabase() {
        return (super.getReadableDatabase(new String(PASSPHRASE)));
    }

    SQLiteDatabase getWritableDatabase() {
        return (super.getWritableDatabase(new String(PASSPHRASE)));
    }

    /*****************************
     * *
     * INSERT DATA TO DATABASE  *
     * *
     *****************************/
    public boolean isMasterDataNeedUpdate() {
        boolean result = false;
        String mQuery = "SELECT * FROM " + TABLE_VERSION;
        SQLiteDatabase mReadableDatabase = getReadableDatabase();
        Cursor mCursor = mReadableDatabase.rawQuery(mQuery, null);
        try {
            if (mCursor.getCount() == 0) {
                result = true;
            }
        } catch (Exception e) {

        } finally {
            mCursor.close();
        }
        return result;
    }

    public void insertSession(MTFUserModel userModel) {
        if (userModel == null)
            return;

        String hashPin = MTFGenerator.generateHash(userModel.getPin());

        ContentValues values = new ContentValues();
        values.put(SESSION_USERNAME, userModel.getUserName().toUpperCase());
        values.put(SESSION_PIN, hashPin);
        values.put(SESSION_TOKEN, userModel.getToken());
        values.put(SESSION_IMEI, userModel.getImei());
        values.put(SESSION_LATEST_LOGIN, format.format(userModel.getLastLogin()));
        values.put(SESSION_USER_ID, userModel.getUserId());
        values.put(SESSION_USER_TYPE, userModel.getAgentType());
        values.put(SESSION_LOCATION_ID, userModel.getLocationId());
        values.put(SESSION_LOCATION_NAME, userModel.getLocationName());
        values.put(SESSION_IS_ONLINE, userModel.isOnline() ? 1 : 0);
        values.put(SESSION_LOB, userModel.getLob());
        values.put(SESSION_DIVISION, userModel.getDivision());
        values.put(SESSION_RA_CODE, userModel.getRaCode());
        values.put(SESSION_RO_CODE, userModel.getRoCode());
        String accessibilty = "";
        values.put(SESSION_ACC, accessibilty);
        values.put(SESSION_JWT, userModel.getJwt());
        values.put(SESSION_SECRET, userModel.getSecret());
        insert(TABLE_SESSION, values);
    }

    public void insertSysPar(String code, String value, boolean isDeleteFirst) {
        if (code == null && value == null)
            return;

        if (isDeleteFirst) {
            deleteSysParm(code);
        }

        ContentValues values = new ContentValues();
        values.put(SYSPAR_CODE, code);
        values.put(SYSPAR_VALUE, value);

        insert(TABLE_SYSTEM_PARAM, values);
    }

    //	 insert version to database base on type of master data
    public void insertMasterDataVersion(MTFVersionModel versionData) {

        if (versionData == null)
            return;

        deleteMasterDataVersionBaseOnType(versionData.getType());

        ContentValues values = new ContentValues();

        values.put(VERSION_TYPE, versionData.getType());
        values.put(VERSION_VALUE, versionData.getVersion());

        insert(TABLE_VERSION, values);
    }

    public void insertMasterDataVersion(List<MTFVersionModel> versionDatas) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            for(MTFVersionModel versionData : versionDatas){
                db.delete(TABLE_VERSION, VERSION_TYPE + "=?", new String[]{versionData.getType()});

                ContentValues values = new ContentValues();

                values.put(VERSION_TYPE, versionData.getType());
                values.put(VERSION_VALUE, versionData.getVersion());

                db.insert(TABLE_VERSION, null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    /*****************************
     * *
     * SELECT DATA TO DATABASE  *
     * *
     *****************************/

    public String getSysParValue(String code) {
        String result = null;
        String mQuery = "SELECT * FROM " + TABLE_SYSTEM_PARAM + " WHERE " + SYSPAR_CODE + "='" + code + "'";
        SQLiteDatabase mReadableDatabase = getReadableDatabase();
        Cursor mCursor = mReadableDatabase.rawQuery(mQuery, null);
        try {
            if (mCursor.moveToFirst()) {
                result = mCursor.getString(2);
            }
        } catch (Exception e) {

        } finally {
            mCursor.close();
        }

        return result;
    }

    public MTFUserModel getSession(String phoneNumber) {
        MTFUserModel result = new MTFUserModel();
        String mQuery = "SELECT * FROM " + TABLE_SESSION + " WHERE " + SESSION_USERNAME + "='" + phoneNumber.toUpperCase() + "'";
        SQLiteDatabase mReadableDatabase = getReadableDatabase();
        Cursor mCursor = mReadableDatabase.rawQuery(mQuery, null);
        try {
            if (mCursor.moveToFirst()) {
                result.setId(mCursor.getLong(mCursor.getColumnIndex(SESSION_ID)));
                result.setUserName(mCursor.getString(mCursor.getColumnIndex(SESSION_USERNAME)));
                result.setPin(mCursor.getString(mCursor.getColumnIndex(SESSION_PIN)));
                result.setToken(mCursor.getString(mCursor.getColumnIndex(SESSION_TOKEN)));
                result.setImei(mCursor.getString(mCursor.getColumnIndex(SESSION_IMEI)));
                try {
                    result.setLastLogin(format.parse(mCursor.getString(mCursor.getColumnIndex(SESSION_LATEST_LOGIN))));
                } catch (ParseException e) {

                }
                result.setUserId(mCursor.getString(mCursor.getColumnIndex(SESSION_USER_ID)));
                result.setAgentType(mCursor.getString(mCursor.getColumnIndex(SESSION_USER_TYPE)));
                result.setLocationId(mCursor.getLong(mCursor.getColumnIndex(SESSION_LOCATION_ID)));
                result.setLocationName(mCursor.getString(mCursor.getColumnIndex(SESSION_LOCATION_NAME)));
                result.setOnline(mCursor.getInt(mCursor.getColumnIndex(SESSION_IS_ONLINE)) == 1 ? true : false);
                result.setRaCode(mCursor.getString(mCursor.getColumnIndex(SESSION_RA_CODE)));
                result.setRoCode(mCursor.getString(mCursor.getColumnIndex(SESSION_RO_CODE)));
                result.setJwt(mCursor.getString(mCursor.getColumnIndex(SESSION_JWT)));
                result.setSecret(mCursor.getString(mCursor.getColumnIndex(SESSION_SECRET)));
                result.setLob(mCursor.getInt(mCursor.getColumnIndex(SESSION_LOB)));
            } else {
                result = null;
            }
        } catch (Exception e) {

        } finally {
            mCursor.close();
        }

        return result;
    }

    public MTFUserModel getActiveSession() {
        MTFUserModel result = new MTFUserModel();
        String mQuery = "SELECT * FROM " + TABLE_SESSION;
        SQLiteDatabase mReadableDatabase = getReadableDatabase();
        Cursor mCursor = mReadableDatabase.rawQuery(mQuery, null);
        try {
            if (mCursor.moveToFirst()) {
                result.setId(mCursor.getLong(mCursor.getColumnIndex(SESSION_ID)));
                result.setUserName(mCursor.getString(mCursor.getColumnIndex(SESSION_USERNAME)));
                result.setPin(mCursor.getString(mCursor.getColumnIndex(SESSION_PIN)));
                result.setToken(mCursor.getString(mCursor.getColumnIndex(SESSION_TOKEN)));
                result.setImei(mCursor.getString(mCursor.getColumnIndex(SESSION_IMEI)));
                try {
                    result.setLastLogin(format.parse(mCursor.getString(mCursor.getColumnIndex(SESSION_LATEST_LOGIN))));
                } catch (ParseException e) {

                }
                result.setUserId(mCursor.getString(mCursor.getColumnIndex(SESSION_USER_ID)));
                result.setAgentType(mCursor.getString(mCursor.getColumnIndex(SESSION_USER_TYPE)));
                result.setLocationId(mCursor.getLong(mCursor.getColumnIndex(SESSION_LOCATION_ID)));
                result.setLocationName(mCursor.getString(mCursor.getColumnIndex(SESSION_LOCATION_NAME)));
                result.setOnline(mCursor.getInt(mCursor.getColumnIndex(SESSION_IS_ONLINE)) == 1 ? true : false);
                result.setLob(mCursor.getInt(mCursor.getColumnIndex(SESSION_LOB)));
                result.setDivision(mCursor.getString(mCursor.getColumnIndex(SESSION_DIVISION)));
                result.setRaCode(mCursor.getString(mCursor.getColumnIndex(SESSION_RA_CODE)));
                result.setRoCode(mCursor.getString(mCursor.getColumnIndex(SESSION_RO_CODE)));
                result.setJwt(mCursor.getString(mCursor.getColumnIndex(SESSION_JWT)));
                result.setSecret(mCursor.getString(mCursor.getColumnIndex(SESSION_SECRET)));
            } else {
                result = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mCursor.close();
        }

        return result;
    }

    //	select data form from database base on type of master data
    public List<String> getMasterDataFormBaseOnTypeAndParent(MTFMasterDataType type, String parent, boolean isAddFirstValue) {
        List<String> result = new ArrayList<String>();
        String mQuery = "SELECT " + FORM_TITLE + " FROM " + TABLE_MASTER_DATA_FORM + " WHERE " + FORM_TYPE + "=" + type.ordinal();

        if (parent != null && !parent.equals("")) {
            mQuery = "SELECT " + FORM_TITLE + " FROM " + TABLE_MASTER_DATA_FORM + " WHERE " + FORM_TYPE + "=" + type.ordinal() + " AND " + FORM_PARENT + " LIKE '%" + parent + "%'";
        } else {
            if (type == MTFMasterDataType.CITY_LIST) {
                return result;
            }
        }

        SQLiteDatabase mReadableDatabase = getReadableDatabase();
        Cursor mCursor = mReadableDatabase.rawQuery(mQuery, null);
        try {
            if (mCursor.moveToFirst()) {

                if (isAddFirstValue) {
                    result.add("Pilih");
                }

                do {
                    result.add(mCursor.getString(0).toUpperCase());

                } while (mCursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            mCursor.close();
        }
        return result;
    }

    //	select data form from database base on type of master data
    public List<MTFMasterDataFormContentModel> getMasterDataBaseOnTypeAndParentKantorBayar(MTFMasterDataType type, String parent, boolean isAddFirstValue, String mitra) {
        List<MTFMasterDataFormContentModel> result = new ArrayList<MTFMasterDataFormContentModel>();
        String mQuery = "SELECT * FROM " + TABLE_MASTER_DATA_FORM + " WHERE " + FORM_TYPE + "=" + type.ordinal();

        if (parent != null && !parent.equals("")) {
            mQuery = "SELECT * FROM " + TABLE_MASTER_DATA_FORM + " WHERE " + FORM_TYPE + "=" + type.ordinal() + " AND " + FORM_PARENT + " = '" + parent + "' AND" +
                    " SUBSTR(" + FORM_CODE + ",5,2) = '" + mitra + "' ORDER BY " + FORM_TITLE + " ASC";
        }

        if (isAddFirstValue) {
            MTFMasterDataFormContentModel firstValue = new MTFMasterDataFormContentModel();
            firstValue.setId(0L);
            firstValue.setTitle("Pilih".toUpperCase());
            firstValue.setMasterDataType(type);
            firstValue.setCode("0");
            firstValue.setHighRisk(false);
            result.add(firstValue);
        }

        SQLiteDatabase mReadableDatabase = getReadableDatabase();
        Cursor mCursor = mReadableDatabase.rawQuery(mQuery, null);
        try {
            if (mCursor.moveToFirst()) {
                do {
                    if (!mCursor.getString(3).equals("999")) {
                        MTFMasterDataFormContentModel formContent = new MTFMasterDataFormContentModel();
                        formContent.setId(mCursor.getLong(mCursor.getColumnIndex(FORM_ID)));
                        formContent.setTitle(mCursor.getString(mCursor.getColumnIndex(FORM_TITLE)));
                        formContent.setParent(mCursor.getString(mCursor.getColumnIndex(FORM_PARENT)));
                        formContent.setCode(mCursor.getString(mCursor.getColumnIndex(FORM_CODE)));
                        formContent.setHighRisk(mCursor.getInt(mCursor.getColumnIndex(FORM_IS_HIGHRISK)) == 1 ? true : false);
                        formContent.setMasterDataType(type);
                        result.add(formContent);
                    }
                } while (mCursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            mCursor.close();
        }
        return result;
    }

    //	select data form from database base on type of master data
    public List<MTFMasterDataFormContentModel> getMasterDataBaseOnTypeAndParent(MTFMasterDataType type, String parent, boolean isAddFirstValue) {
        List<MTFMasterDataFormContentModel> result = new ArrayList<MTFMasterDataFormContentModel>();
        String mQuery = "SELECT * FROM " + TABLE_MASTER_DATA_FORM + " WHERE " + FORM_TYPE + "=" + type.ordinal();

        if (parent != null && !parent.equals("")) {
            mQuery = "SELECT * FROM " + TABLE_MASTER_DATA_FORM + " WHERE " + FORM_TYPE + "=" + type.ordinal() + " AND " + FORM_PARENT + " = '" + parent + "'" +
                " ORDER BY " + FORM_TITLE + " ASC";
        }

        if (isAddFirstValue) {
            MTFMasterDataFormContentModel firstValue = new MTFMasterDataFormContentModel();
            firstValue.setId(0L);
            firstValue.setTitle("Pilih".toUpperCase());
            firstValue.setMasterDataType(type);
            firstValue.setCode("0");
            firstValue.setHighRisk(false);
            result.add(firstValue);
        }

        SQLiteDatabase mReadableDatabase = getReadableDatabase();
        Cursor mCursor = mReadableDatabase.rawQuery(mQuery, null);
        try {
            if (mCursor.moveToFirst()) {
                do {
                    if (!mCursor.getString(3).equals("999")) {
                        MTFMasterDataFormContentModel formContent = new MTFMasterDataFormContentModel();
                        formContent.setId(mCursor.getLong(mCursor.getColumnIndex(FORM_ID)));
                        formContent.setTitle(mCursor.getString(mCursor.getColumnIndex(FORM_TITLE)));
                        formContent.setParent(mCursor.getString(mCursor.getColumnIndex(FORM_PARENT)));
                        formContent.setCode(mCursor.getString(mCursor.getColumnIndex(FORM_CODE)));
                        formContent.setHighRisk(mCursor.getInt(mCursor.getColumnIndex(FORM_IS_HIGHRISK)) == 1 ? true : false);
                        formContent.setMasterDataType(type);
                        result.add(formContent);
                    }
                } while (mCursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            mCursor.close();
        }
        return result;
    }

    public List<String> getMasterDataFormBaseOnType(MTFMasterDataType type, boolean isAddFirstValue) {
        List<String> result = new ArrayList<String>();
        String mQuery = "SELECT " + FORM_TITLE + " FROM " + TABLE_MASTER_DATA_FORM + " WHERE " + FORM_TYPE + "=" + type.ordinal();

        SQLiteDatabase mReadableDatabase = getReadableDatabase();
        Cursor mCursor = mReadableDatabase.rawQuery(mQuery, null);
        try {
            if (mCursor.moveToFirst()) {

                if (isAddFirstValue) {
                    result.add("Pilih");
                }

                do {
                    result.add(mCursor.getString(0).toUpperCase());

                } while (mCursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            mCursor.close();
        }
        return result;
    }

    //function for get all version
    public JSONArray getAllVersionOfMasterData() {
        JSONArray result = new JSONArray();
        String mQuery = "SELECT * FROM " + TABLE_VERSION;
        SQLiteDatabase mReadableDatabase = getReadableDatabase();
        Cursor mCursor = mReadableDatabase.rawQuery(mQuery, null);
        try {
            if (mCursor.moveToFirst()) {
                do {
                    MTFVersionModel version = new MTFVersionModel();
                    version.setId(mCursor.getLong(mCursor.getColumnIndex(VERSION_ID)));
                    version.setType(mCursor.getString(mCursor.getColumnIndex(VERSION_TYPE)));
                    version.setVersion(mCursor.getInt(mCursor.getColumnIndex(VERSION_VALUE)));

                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(MTFJSONKey.KEY_MODULE_TYPE, mCursor.getString(mCursor.getColumnIndex(VERSION_TYPE)));
                        result.put(jsonObject);
                        jsonObject.put(MTFJSONKey.KEY_VERSION, mCursor.getInt(mCursor.getColumnIndex(VERSION_VALUE)));
                    } catch (JSONException e) {

                    }
                } while (mCursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            mCursor.close();
        }
        return result;
    }

    /*****************************
     * *
     * UPDATE DATA TO DATABASE  *
     * *
     *****************************/
    public void updateSession(MTFUserModel userModel) {
        if (userModel == null)
            return;

        String hashPin = MTFGenerator.generateHash(userModel.getPin());

        ContentValues values = new ContentValues();
        values.put(SESSION_USERNAME, userModel.getUserName().toUpperCase());
        values.put(SESSION_PIN, hashPin);
        values.put(SESSION_TOKEN, userModel.getToken());
        values.put(SESSION_IMEI, userModel.getImei());
        if (userModel.getLastLogin() != null) {
            values.put(SESSION_LATEST_LOGIN, format.format(userModel.getLastLogin()));
        } else {
            values.put(SESSION_LATEST_LOGIN, "");
        }
        values.put(SESSION_USER_ID, userModel.getUserId());
        values.put(SESSION_USER_TYPE, userModel.getAgentType());
        values.put(SESSION_IS_ONLINE, userModel.isOnline() ? 1 : 0);
        values.put(SESSION_LOB, userModel.getLob());
        values.put(SESSION_DIVISION, userModel.getDivision());
        values.put(SESSION_RA_CODE, userModel.getRaCode());
        values.put(SESSION_RO_CODE, userModel.getRoCode());
        values.put(SESSION_LOCATION_ID, userModel.getLocationId());
        values.put(SESSION_LOCATION_NAME, userModel.getLocationName());
        String accessibilty = "";
        values.put(SESSION_ACC, accessibilty);
        values.put(SESSION_JWT, userModel.getJwt());
        values.put(SESSION_SECRET, userModel.getSecret());
        update(TABLE_SESSION, values, SESSION_ID + " =? ", new String[]{userModel.getId() + ""});
    }

    public void updateOnlineStatus(MTFUserModel userModel) {
        if (userModel == null)
            return;

        ContentValues values = new ContentValues();
        values.put(SESSION_IS_ONLINE, userModel.isOnline() ? 1 : 0);
        update(TABLE_SESSION, values, SESSION_ID + " =? ", new String[]{userModel.getId() + ""});
    }

    public void updateToken(String userName, String token) {
        ContentValues values = new ContentValues();
        values.put(SESSION_TOKEN, token);
        values.put(SESSION_LATEST_LOGIN, format.format(new Date()));

        update(TABLE_SESSION, values, SESSION_USERNAME + " =? ", new String[]{userName.toUpperCase()});
    }

    public void updateTokenOnly(String userName, String token) {
        ContentValues values = new ContentValues();
        values.put(SESSION_TOKEN, token);

        update(TABLE_SESSION, values, SESSION_USERNAME + " =? ", new String[]{userName.toUpperCase()});
    }

    /*****************************
     * *
     * DELETE DATA TO DATABASE  *
     * *
     *****************************/

    public void deleteMasterData() {
        delete(TABLE_MASTER_DATA_FORM, null, null);
    }

    public void insertKantorBayar(SQLiteDatabase db, String fullQuery){
        if(fullQuery.indexOf(";") <= 0) return;

        List<String> query = new ArrayList<>();
        do{
            query.add(fullQuery.substring(0, fullQuery.indexOf(";")+1));
            fullQuery = fullQuery.substring(fullQuery.indexOf(";")+1, fullQuery.length());
        }while (fullQuery.indexOf(";") >= 0);

        for(int i=0;i<query.size();i++){
            try {
                db.execSQL(query.get(i));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void deleteSysParm() {
        delete(TABLE_SYSTEM_PARAM, null, null);
    }

    public void deleteSysParm(String code) {
        delete(TABLE_SYSTEM_PARAM, SYSPAR_CODE + " =? ", new String[]{code});
    }

    public void deleteSession() {
        delete(TABLE_SESSION, null, null);
    }

    //	delete data form form database base on type of master data
    public void deleteMasterDataFormBaseOnType(MTFMasterDataType type) {
        delete(TABLE_MASTER_DATA_FORM, FORM_TYPE + "=?", new String[]{type.ordinal() + ""});
    }

    public void deleteMasterDataFormBaseOnIdAndType(Long id, MTFMasterDataType type) {
        delete(TABLE_MASTER_DATA_FORM, FORM_TYPE + "=? AND " + FORM_ID + "=?", new String[]{type.ordinal() + "", id + ""});
    }

    public void deleteMasterDataFormBaseOnCodeAndType(String code, MTFMasterDataType type) {
        delete(TABLE_MASTER_DATA_FORM, FORM_TYPE + "=? AND " + FORM_CODE + "=?", new String[]{type.ordinal() + "", code + ""});
    }

    //	delete data form form database base on type of master data
    public void deleteMasterDataVersionBaseOnType(String type) {
        delete(TABLE_VERSION, VERSION_TYPE + "=?", new String[]{type});
    }

    /********************************
     * *
     * UTILITY HELPER TO DATABASE  *
     * *
     ********************************/

    private void insert(String tableName, ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            db.insert(tableName, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    private void update(String tableName, ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            db.update(tableName, values, whereClause, whereArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            db.endTransaction();
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    private void delete(String tableName, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(tableName, whereClause, whereArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            db.endTransaction();
            if (db.isOpen()) {
                db.close();
            }
        }
    }
}
