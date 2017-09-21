package com.btpn.sinaya.eform.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vaniawidjaja on 2/7/17.
 */

public class MTFFileHelper {

    private static boolean checkExternalMedia(){
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            return true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            return true;
        } else {
            // Can't read or write
            return false;
        }
    }

    public static void writeBitmapToFile(Bitmap bitmap, String filename){

        File root;
        if(checkExternalMedia())
            root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        else
            root = Environment.getDataDirectory();

        // See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

        File dir = new File (root.getAbsolutePath() + "/EformImage");
        dir.mkdirs();
        SimpleDateFormat completeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = new Date(System.currentTimeMillis());
        File file = new File(dir, filename+"_"+completeFormat.format(date)+"_"+MTFSystemParams.imageWidth+"x"+MTFSystemParams.imageHeight+".jpeg");

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeToSDFile(String data, String filePrefix){

        // Find the root of the external storage.
        // See http://developer.android.com/guide/topics/data/data-  storage.html#filesExternal
        File root;
        if(checkExternalMedia())
            root = Environment.getExternalStorageDirectory();
        else
            root = Environment.getDataDirectory();

        // See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

        File dir = new File (root.getAbsolutePath() + "/EformKYCLog");
        dir.mkdirs();
        SimpleDateFormat completeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        Date date = new Date(System.currentTimeMillis());
        File file = new File(dir, filePrefix+"_"+completeFormat.format(date)+".txt");

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

        }
    }

    public static void writeToSDFileLogin(String data){

        // Find the root of the external storage.
        // See http://developer.android.com/guide/topics/data/data-  storage.html#filesExternal
        File root;
        if(checkExternalMedia())
            root = Environment.getExternalStorageDirectory();
        else
            root = Environment.getDataDirectory();

        // See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

        File dir = new File (root.getAbsolutePath() + "/PurnaLog");
        dir.mkdirs();
        File file = new File(dir, "Log_saving.txt");

        try {
            FileWriter fw = new FileWriter(file, true);
            fw.append(data+"\n");
            fw.flush();
            fw.close();
        } catch (FileNotFoundException e) {

            Log.i("MTFExceptionHandler", "******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
        } catch (IOException e) {

        }
    }

}