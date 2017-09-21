package com.btpn.sinaya.eform.utils;

import com.btpn.sinaya.eform.BuildConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by vaniawidjaja on 2/7/17.
 */

public class MTFFileLogger {

    private static MTFFileLogger instance;

    public static MTFFileLogger getInstance(){
        if(instance == null){
            instance = new MTFFileLogger();
        }
        return instance;
    }

    private HashMap<String, StringBuilder> builderMap;

    private MTFFileLogger(){
        builderMap = new HashMap<String, StringBuilder>();
    }

    /**
     * Add a message to write to a specific prefix. System will create a new prefix if it never been created before.
     * @param prefix
     * @param message
     */
    public synchronized void addString(String prefix, String message){
        if(BuildConfig.DEBUG) {
            StringBuilder sb;
            if (builderMap.containsKey(prefix)) {
                sb = builderMap.get(prefix);
            } else {
                sb = new StringBuilder();
                builderMap.put(prefix, sb);
            }
            SimpleDateFormat completeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
            Date date = new Date(System.currentTimeMillis());
            sb.append(completeFormat.format(date) + ":  " + message + "\n");
        }
    }

    /**
     * Write all specific log prefix to file, the prefix then will be cleaned
     * @param prefix
     */
    public synchronized void writeAndReset(String prefix){
        StringBuilder sb;
        if(builderMap.containsKey(prefix)){
            sb = builderMap.get(prefix);
            if(BuildConfig.DEBUG){
                MTFFileHelper.writeToSDFile(sb.toString(), prefix);
            }
            sb = null;
            builderMap.remove(prefix);
        }
    }

    /**
     * Write all registered prefix to a file
     */
    public synchronized  void dumpAll(){
        for(String prefix : builderMap.keySet()){
            StringBuilder sb = builderMap.get(prefix);
            if(BuildConfig.DEBUG){
                MTFFileHelper.writeToSDFile(sb.toString(), prefix);
            }
        }
        flush();
    }

    /**
     * Clear all data log
     */
    public synchronized void flush(){
        builderMap.clear();
    }

    public synchronized void clearFile(){

    }

    void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()){
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);
        }
        fileOrDirectory.delete();

    }

}