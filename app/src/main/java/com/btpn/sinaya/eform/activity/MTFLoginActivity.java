package com.btpn.sinaya.eform.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.btpn.sinaya.eform.MTFBaseActivity;
import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.activity.controller.MTFLoginController;
import com.btpn.sinaya.eform.alert.MTFConfirmationAlertDialog;
import com.btpn.sinaya.eform.alert.MTFInfoAlertDialog;
import com.btpn.sinaya.eform.alert.listener.MTFConfirmationAlertDialogListener;
import com.btpn.sinaya.eform.alert.listener.MTFInfoAlertDialogListener;
import com.btpn.sinaya.eform.connection.MTFSMPUtilities;
import com.btpn.sinaya.eform.model.MTFUserModel;
import com.btpn.sinaya.eform.utils.MTFConstants;
import com.btpn.sinaya.eform.utils.MTFPermissionUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MTFLoginActivity extends MTFBaseActivity{
	
	private EditText userNameEditText;
	private EditText passwordEditText;
	private TextView apkVersionEditText;
	private Button loginButton;
	private MTFConfirmationAlertDialog exitConfirmationDialog;
	private MTFInfoAlertDialog exitoAlertDialog;
	private MTFLoginActivity activity;
	private InputMethodManager imm;
	private PackageInfo pInfo;

	private MTFLoginController controller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
        if (isDeviceRooted()) {//||isRooted()){
//            Toast.makeText(getApplicationContext(),"Device is rooted!", Toast.LENGTH_LONG).show();

            controller = new MTFLoginController(this);
            controller.exit();

        } else {
//            Toast.makeText(getApplicationContext(), "Device is not rooted!", Toast.LENGTH_LONG).show();
            loadView();
            initiateDefaultValue();
        }
	}

    public static boolean isDeviceRooted() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
    }

    private static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkRootMethod2() {
        String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) return true;
            return false;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }

	private void loadView(){
		userNameEditText = (EditText)findViewById(R.id.login_username_edittext);
		passwordEditText = (EditText)findViewById(R.id.login_password_edittext);
		loginButton = (Button)findViewById(R.id.login_button);
		apkVersionEditText = (TextView)findViewById(R.id.versionAPK);
	}
	
	private void initiateDefaultValue(){
		activity = this;
		try {
			pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
			apkVersionEditText.setText("saving " + pInfo.versionName);
		} catch (NameNotFoundException e) {
			
		}

        if(MTFSMPUtilities.isUAT)
            apkVersionEditText.setText("saving " + this.getString(R.string.apkVersion));

		controller = new MTFLoginController(this);
		//For Set Pin EditText to Password Mode and Input as Number;
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		loginButton.setOnClickListener(controller);
		passwordEditText.setImeActionLabel("Login", KeyEvent.KEYCODE_ENTER);
		passwordEditText.setOnKeyListener(controller);
		//initiateSQLChiper();
//		checkMultiplePermission();
		//if(MTFConstants.autoComplete){
//			setValueToLoginForm();
		//}

		checkMultiplePermission();
	}

	public void checkMultiplePermission(){
		List<String> permissionsNeeded = new ArrayList<String>();

		final List<String> permissionsList = new ArrayList<String>();
		if (addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
			permissionsNeeded.add(getResources().getString(R.string.sim_exp));
		if (addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
			permissionsNeeded.add(getResources().getString(R.string.picture_exp));
		if (addPermission(permissionsList, Manifest.permission.CAMERA))
			permissionsNeeded.add(getResources().getString(R.string.camera_exp));

		if (permissionsList.size() > 0) {
			if (permissionsNeeded.size() > 0) {
				// Need Rationale
				String message = getResources().getString(R.string.header_permission_text) +" "+ permissionsNeeded.get(0);
				for (int i = 1; i < permissionsNeeded.size(); i++)
					message = message + ", " + permissionsNeeded.get(i);
				showConfirmationAlertDialog(message, new MTFConfirmationAlertDialogListener() {
					@Override
					public void onOK() {
						ActivityCompat.requestPermissions(MTFLoginActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
								MTFConstants.MTF_PERMISSION_REQUEST_INIT);
					}

					@Override
					public void onCancel() {
						showAlertDialog("Error", getResources().getString(R.string.permission_start_warning), new MTFInfoAlertDialogListener() {
							@Override
							public void onOk() {
								initiateSQLChiper();
							}
						});
					}
				});
				return;
			}
			ActivityCompat.requestPermissions(MTFLoginActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
					MTFConstants.MTF_PERMISSION_REQUEST_INIT);
			return;
		}
		initiateSQLChiper();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case MTFConstants.MTF_PERMISSION_REQUEST_LOGIN: {
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					controller.submitLogin();
				} else {
					String permission = permissions[0];
					if(!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
						showConfirmationAlertDialog("Eform memerlukan permission 'PHONE' untuk membaca IMEI. " +
								"Silakan aktifkan permission Anda melalui pengaturan aplikasi", new MTFConfirmationAlertDialogListener() {
							@Override
							public void onOK() {
								Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
								Uri uri = Uri.fromParts("package", getPackageName(), null);
								intent.setData(uri);
								startActivityForResult(intent, MTFConstants.MTF_PERMISSION_OPEN_SETTING);
							}

							@Override
							public void onCancel() {

							}
						});
					}else {
						showAlertDialog("Error", getResources().getString(R.string.permission_login_error));
					}
				}
				return;
			}
			case MTFConstants.MTF_PERMISSION_REQUEST_INIT:
			{
				Map<String, Integer> perms = new HashMap<String, Integer>();
				// Initial
				perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
				perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
				perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
				// Fill with results
				for (int i = 0; i < permissions.length; i++)
					perms.put(permissions[i], grantResults[i]);
				// Check for ACCESS_FINE_LOCATION
				if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
						&& perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
						&& perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
					// All Permissions Granted
					initiateSQLChiper();
				} else {
					// Permission Denied
					showAlertDialog("Peringatan", getResources().getString(R.string.permission_start_warning), new MTFInfoAlertDialogListener() {
						@Override
						public void onOk() {
							initiateSQLChiper();
						}
					});
				}

			}
			break;
			// other 'case' lines to check for other
			// permissions this app might request
		}
	}

	private boolean addPermission(List<String> permissionsList, String permission) {
		if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
			permissionsList.add(permission);
			// Check for Rationale Option
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
				return true;
		}
		return false;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setContentView(R.layout.activity_login);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
	}
	
	@Override
	public void onBackPressed() {
		if (controller != null) {
			if(controller.onBackPressed()){
				ActivityCompat.finishAffinity(activity);
				System.exit(0);
			}
		}else{
			ActivityCompat.finishAffinity(activity);
			System.exit(0);
//			super.onBackPressed();
		}
	}
	
	private void setValueToLoginForm() {
		userNameEditText.setText("PB17");
		passwordEditText.setText("P@ssw0rd");
	}

	public void loginWithPermission(){
		int permissionCheck = ContextCompat.checkSelfPermission(this,
				Manifest.permission.READ_PHONE_STATE);
		if(permissionCheck == PackageManager.PERMISSION_GRANTED){
			controller.submitLogin();
		}else{
			MTFPermissionUtil.requestPermission(this, Manifest.permission.READ_PHONE_STATE, MTFConstants.MTF_PERMISSION_REQUEST_LOGIN,
					"Aplikasi E-Form membutuhkan permission 'PHONE' untuk membaca IMEI");
		}
	}

	public void showisOfflineDialog(String message){
		MTFConfirmationAlertDialog confirmation = new MTFConfirmationAlertDialog(this, new MTFConfirmationAlertDialogListener() {
			
			@Override
			public void onOK() {
				MTFUserModel user = controller.getActiveSession();
				user.setOnline(false);
				controller.updateSession(user);
				activity.finish();
			}
			
			@Override
			public void onCancel() {
				ActivityCompat.finishAffinity(activity);
				System.exit(0);
			}
		}, message);
		confirmation.setDialogMessage(message);
		confirmation.show();
	}
	
	public void showExitDialog(){
		if (exitoAlertDialog == null) {
			exitoAlertDialog = new MTFInfoAlertDialog(this, "Error", "Versi ini tidak update, tolong update applikasi terlebih dahulu");
			exitoAlertDialog.setListener(new MTFInfoAlertDialogListener() {
				
				@Override
				public void onOk() {
					//Show Google Play
//					activity.finish();
					ActivityCompat.finishAffinity(activity);
					final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
					try {
					    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
					} catch (android.content.ActivityNotFoundException e) {
					    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
					}
				}
			});
		}
		
		exitoAlertDialog.show();
	}

	public void showExitVersionDialog(){
		if (exitoAlertDialog == null) {
			exitoAlertDialog = new MTFInfoAlertDialog(this, "Error", "Sistem operasi perangkat anda tidak disupport.\n" +
					"Silahkan hubungi Customer Service.");
			exitoAlertDialog.setListener(new MTFInfoAlertDialogListener() {

				@Override
				public void onOk() {
					//Show Google Play
//					activity.finish();
//					ActivityCompat.finishAffinity(activity);
//					final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
//					try {
//						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//					} catch (android.content.ActivityNotFoundException e) {
//						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//					}
				}
			});
		}

		exitoAlertDialog.show();
	}
	
	public void showExitConfirmationDialog(){
		if (exitConfirmationDialog == null) {
			exitConfirmationDialog = new MTFConfirmationAlertDialog(activity, new MTFConfirmationAlertDialogListener() {
				
				@Override
				public void onOK() {
					//Exit app
					ActivityCompat.finishAffinity(activity);
					System.exit(0);
				}
				
				@Override
				public void onCancel() {
					//Do Nothing
				}
			},"");
			
			exitConfirmationDialog.setDialogMessage(activity.getString(R.string.alert_exit));
		}
		
		exitConfirmationDialog.show();
	}
	
	public void hideKeyBoard(){
		imm.hideSoftInputFromWindow(passwordEditText.getWindowToken(), 0);
	}
	
	public EditText getUserNameEditText() {
		return userNameEditText;
	}
	
	public EditText getPasswordEditText() {
		return passwordEditText;
	}
	
	@Override
	protected void onDestroy() {
		if (controller != null) {
			controller.onDestory();
		}
		super.onDestroy();
	}

}
