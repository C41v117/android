package com.btpn.sinaya.eform;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.btpn.sinaya.eform.activity.MTFLoginActivity;
import com.btpn.sinaya.eform.alert.MTFConfirmationAlertDialog;
import com.btpn.sinaya.eform.alert.MTFInfoAlertDialog;
import com.btpn.sinaya.eform.alert.MTFLoadingAlertDialog;
import com.btpn.sinaya.eform.alert.MTFRootedAlertDialog;
import com.btpn.sinaya.eform.alert.listener.MTFConfirmationAlertDialogListener;
import com.btpn.sinaya.eform.alert.listener.MTFInfoAlertDialogListener;
import com.btpn.sinaya.eform.alert.listener.MTFRootedAlertDialogListener;
import com.btpn.sinaya.eform.connection.MTFConnectionManager;
import com.btpn.sinaya.eform.connection.MTFSMPUtilities;
import com.btpn.sinaya.eform.database.MTFDatabaseHelper;
import com.btpn.sinaya.eform.model.MTFVersionModel;
import com.btpn.sinaya.eform.model.type.MTFMasterDataType;
import com.btpn.sinaya.eform.utils.MTFConstants;
import com.btpn.sinaya.eform.utils.MTFDataRawLoader;
import com.btpn.sinaya.eform.utils.MTFIntentConstant;
import com.btpn.sinaya.eform.utils.MTFSystemParams;

import net.sqlcipher.database.SQLiteDatabase;

public class MTFBaseActivity extends FragmentActivity {

    private MTFLoadingAlertDialog loadingAlertDialog;
    protected MTFInfoAlertDialog infoAlertDialog;
    protected MTFInfoAlertDialog errorTokenAlertDialog;
    private MTFConfirmationAlertDialog confirmationAlertDialog;
    private MTFRootedAlertDialog rootedAlertDialog;
    protected boolean isShowLoading;
    protected boolean isShowAlert;
    protected String title = "";
    protected String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.loadLibrary("stlport_shared");
        SQLiteDatabase.loadLibs(getApplicationContext());
        MTFConnectionManager.init();
        MTFSystemParams.reloadSystemParamsFromDatabase(new MTFDatabaseHelper(this));
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            isShowLoading = (boolean) savedInstanceState.getBoolean(MTFIntentConstant.BUNDLE_KEY_LOADING_SHOW);
            isShowAlert = (boolean) savedInstanceState.getBoolean(MTFIntentConstant.BUNDLE_KEY_ALERT_SHOW);
            message = (String) savedInstanceState.getString(MTFIntentConstant.BUNDLE_KEY_MESSAGE);
            title = (String) savedInstanceState.getString(MTFIntentConstant.BUNDLE_KEY_TITLE);
            if (isShowLoading) {
                if (isShowAlert) {
                    dismisLoadingDialog();
                    showAlertDialog(title, message);
                } else {
                    showLoadingDialog(message);
                }
            } else {
                dismisLoadingDialog();
            }

            if (isShowAlert) {
                showAlertDialog(title, message);
            }
        }

    }

    @Override
    public void onLowMemory() {
        Toast.makeText(this, "Your device is running low. Please Restart.", Toast.LENGTH_LONG);
        super.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        dismisLoadingDialog();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(MTFIntentConstant.BUNDLE_KEY_ALERT_SHOW, isShowAlert);
        outState.putBoolean(MTFIntentConstant.BUNDLE_KEY_LOADING_SHOW, isShowLoading);
        outState.putString(MTFIntentConstant.BUNDLE_KEY_MESSAGE, message);
        outState.putString(MTFIntentConstant.BUNDLE_KEY_TITLE, title);
        super.onSaveInstanceState(outState);
    }

    public void showConfirmationAlertDialog(String message, MTFConfirmationAlertDialogListener listener) {
        confirmationAlertDialog = new MTFConfirmationAlertDialog(this, listener, message);
        confirmationAlertDialog.show();
    }

    public void showAlertDialog(String title, final String message) {
        dismisLoadingDialog();
        this.title = title;
        this.message = message;
        isShowAlert = true;
        infoAlertDialog = new MTFInfoAlertDialog(this, title, message);
        infoAlertDialog.setListener(new MTFInfoAlertDialogListener() {

            @Override
            public void onOk() {
                if(message.equals("Tidak dapat terhubung ke E-Saving Web")){
                    isShowAlert = false;
                }else {
                    isShowAlert = false;
                }
            }
        });
        infoAlertDialog.show();
    }

    public void showAlertDialog(String title, String message, MTFInfoAlertDialogListener listener) {
        dismisLoadingDialog();
        this.title = title;
        this.message = message;
        isShowAlert = true;
        infoAlertDialog = new MTFInfoAlertDialog(this, title, message);
        infoAlertDialog.setListener(listener);
        infoAlertDialog.show();
    }

    public void showLoadingDialog(String loadingText) {
        dismisLoadingDialog();
        loadingAlertDialog = new MTFLoadingAlertDialog(this, loadingText);
        loadingAlertDialog.setCancelable(false);
        loadingAlertDialog.setCanceledOnTouchOutside(false);
        if (message.equals("")) {
            if (loadingText.equals(getResources().getString(R.string.persiapan_database))) {
                message = getResources().getString(R.string.persiapan_database);
            }
        } else {
            if (!loadingText.equals(getResources().getString(R.string.persiapan_database))) {
                message = loadingText;
            }
        }

        loadingAlertDialog.setLoadingText(loadingText);
        if (loadingAlertDialog.isShowing()) {
            isShowLoading = false;
            loadingAlertDialog.dismiss();
        } else {
            isShowLoading = true;
            loadingAlertDialog.show();
        }
    }

    public void showErrorTokenDialog() {
        if (errorTokenAlertDialog == null) {
            errorTokenAlertDialog = new MTFInfoAlertDialog(this, "Gagal", "Akun anda digunakan di perangkat lain, Harap login kembali");
            errorTokenAlertDialog.setCancelable(false);
            errorTokenAlertDialog.setCanceledOnTouchOutside(false);
        }
        errorTokenAlertDialog.setListener(new MTFInfoAlertDialogListener() {

            @Override
            public void onOk() {
//				errorTokenAlertDialog.dismiss();
//				ActivityCompat.finishAffinity(MTFBaseActivity.this);
                Intent intent = new Intent(MTFBaseActivity.this, MTFLoginActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(MTFIntentConstant.INTENT_EXTRA_CODE, MTFIntentConstant.CODE_FAILED_TOKEN);
                startActivity(intent);
            }
        });
        errorTokenAlertDialog.show();
    }

    public void dismisLoadingDialog() {
        isShowLoading = false;
        if (loadingAlertDialog != null) {
            if (loadingAlertDialog.isShowing()) {
                loadingAlertDialog.dismiss();
            }
        } else {
            loadingAlertDialog = new MTFLoadingAlertDialog(this, message);
            loadingAlertDialog.dismiss();
        }
    }

    protected void initiateSQLChiper() {
        AsyncTask<Void, Void, Void> asyntask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                showLoadingDialog(getResources().getString(R.string.persiapan_database));
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {

//				System.loadLibrary("stlport_shared");
//				SQLiteDatabase.loadLibs(getApplicationContext());

                MTFDatabaseHelper database = new MTFDatabaseHelper(MTFBaseActivity.this);
                if (MTFConstants.UPDATE_LOOKUP) {
                    MTFVersionModel versionModel = new MTFVersionModel("LOOKUP", MTFSMPUtilities.dbVer);
                    database.insertMasterDataVersion(versionModel);
                    database.deleteMasterDataFormBaseOnType(MTFMasterDataType.UNKNOWN);

                    MTFConstants.UPDATE_LOOKUP = false;
                }
                if (database.isMasterDataNeedUpdate()) {
                    MTFDataRawLoader.load(MTFBaseActivity.this, database);
                }
                database.close();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                dismisLoadingDialog();
                super.onPostExecute(result);
            }
        };

        asyntask.execute();
    }

    public void showRootedDialog(String message, MTFRootedAlertDialogListener listener){
        rootedAlertDialog = new MTFRootedAlertDialog(this, message, listener);
        rootedAlertDialog.setCustomMessage(message);
        rootedAlertDialog.show();
    }

}
