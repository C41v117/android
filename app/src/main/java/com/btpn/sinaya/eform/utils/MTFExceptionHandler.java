package com.btpn.sinaya.eform.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.btpn.sinaya.eform.BuildConfig;
import com.btpn.sinaya.eform.connection.MTFSMPUtilities;
import com.btpn.sinaya.eform.connection.listener.MTFConnectionListener;
import com.btpn.sinaya.eform.services.MTFAPIClientLogs;
import com.sybase.persistence.DataVault;
import com.sybase.persistence.PrivateDataVault;

public class MTFExceptionHandler implements UncaughtExceptionHandler, MTFConnectionListener {

	private UncaughtExceptionHandler defaultUEH;

	private Context mContext;
	private boolean mExternalStorageAvailable = false;

	private String endpoint = "https://apptbodev02.dev.corp.btpn.co.id:8081/clientlogs/";
	private String endpointSIT = "https://meap-sit.dev.corp.btpn.co.id/smp/clientlogs/";
	private String dateFormat = "yyyy MM dd HH:mm:ss:SSS";
	private boolean mExternalStorageWriteable = false;

	public MTFExceptionHandler(Context context) {
		if (MTFSMPUtilities.port != null) {
			endpoint = MTFSMPUtilities.requestType+ MTFSMPUtilities.host+":"+MTFSMPUtilities.port+"/clientlogs/";
		}else{
			endpoint = MTFSMPUtilities.requestType+MTFSMPUtilities.host+"/clientlogs/";
		}
		this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
		mContext = context;
		checkExternalMedia();
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		e.printStackTrace(printWriter);
		String stacktrace = result.toString();
		printWriter.close();

		if (endpoint != null) {
			try{
				MTFAPIClientLogs clientLogsApi = new MTFAPIClientLogs(mContext, this, stacktrace);
				clientLogsApi.sendRequest();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}

		if(BuildConfig.DEBUG){
			writeToSDFile(stacktrace);
			MTFFileLogger.getInstance().dumpAll();
		}

		defaultUEH.uncaughtException(t, e);
	}

	@Override
	public void onSend(Bundle bundle, Context context) {

	}

	private void checkExternalMedia(){
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// Can read and write the media
			mExternalStorageAvailable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// Can only read the media
			mExternalStorageAvailable = true;
		} else {
			// Can't read or write
			mExternalStorageAvailable = false;
		}
	}

	private void writeToSDFile(String data){

		// Find the root of the external storage.
		// See http://developer.android.com/guide/topics/data/data-  storage.html#filesExternal
		File root;
		if(mExternalStorageAvailable)
			root = Environment.getExternalStorageDirectory();
		else
			root = Environment.getDataDirectory();

		// See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

		File dir = new File (root.getAbsolutePath() + "/EformKYCLog");
		dir.mkdirs();
		SimpleDateFormat completeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Date date = new Date(System.currentTimeMillis());
		File file = new File(dir, "eform_log_"+completeFormat.format(date)+".txt");

		try {
			FileOutputStream f = new FileOutputStream(file);
			PrintWriter pw = new PrintWriter(f);
			pw.println(data);
			pw.flush();
			pw.close();
			f.close();
		} catch (FileNotFoundException e) {

			Log.i("MTFExceptionHandler", "******* File not found. Did you" +
					" add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
