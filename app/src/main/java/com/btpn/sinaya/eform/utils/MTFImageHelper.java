package com.btpn.sinaya.eform.utils;

import android.content.Context;
import android.widget.ImageView;

public class MTFImageHelper {
	
	public static final String folderName = "folder";
	
	public static void setImageViewFromCamera(Context context, String cameraFilePath, String dstFilename, int rotationOfImage, MTFTypeImage imageType){
		MTFImageHelperAsyncTaskSaver asynctask = new MTFImageHelperAsyncTaskSaver(context, cameraFilePath, dstFilename, rotationOfImage, imageType);
		asynctask.execute();
	}
	
	public static void setImageViewFromCamera(ImageView imageView, String cameraFilePath, int rotationOfImage, int failedImageId, MTFImageHelperListener listener, int width, int height){
		MTFImageHelperAsyncTaskLoaderFromUri asynctask = new MTFImageHelperAsyncTaskLoaderFromUri(imageView, cameraFilePath, rotationOfImage, failedImageId, listener, width, height);
		asynctask.execute();
	}
	
	public static void setImageViewFromFile(Context context, ImageView imageView, String srcFilename, int failedImageId, MTFImageHelperListener listener){
		MTFImageHelperAsyncTaskLoader asynctask = new MTFImageHelperAsyncTaskLoader(context, imageView, srcFilename, failedImageId, listener);
		asynctask.execute();
	}
	
}
