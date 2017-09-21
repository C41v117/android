package com.btpn.sinaya.eform.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

class MTFImageHelperAsyncTaskSaver extends AsyncTask<Void, Void, Void>{

//	private Bitmap bitmap;
	private String dstFilename;
	private String cameraFilePath;
	private Context context;
	private MTFTypeImage imageType;
	private int rotationOfImage;
	
	public MTFImageHelperAsyncTaskSaver(Context context, String cameraFilePath, String dstFilename, int rotationOfImage, MTFTypeImage imageType) {
		this.dstFilename = dstFilename;
		this.cameraFilePath = cameraFilePath;
		this.context = context;
		this.imageType = imageType;
		this.rotationOfImage = rotationOfImage;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		decodeSampledBitmapFromResource(MTFSystemParams.imageWidth, MTFSystemParams.imageHeight);
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		Intent intent = new Intent();
		intent.addCategory(MTFIntentConstant.CATEGORY_ATTACHMENT);
		intent.setAction(MTFIntentConstant.ACTION_IMAGE_UPDATE);
		
		Bundle bundle = new Bundle();
		bundle.putInt(MTFIntentConstant.BUNDLE_KEY_IMAGE_TYPE, imageType.ordinal());
		bundle.putString(MTFIntentConstant.BUNDLE_KEY_FILE_NAME, dstFilename);
		intent.putExtras(bundle);
		
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
		super.onPostExecute(result);
	}
	
	private void decodeSampledBitmapFromResource(int reqWidth, int reqHeight) {
		try{
		    BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeFile(cameraFilePath, options);
	
		    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		    options.inPreferredConfig = Config.RGB_565;
		    options.inJustDecodeBounds = false;
		    
		    Bitmap bitmap = BitmapFactory.decodeFile(cameraFilePath, options);
            if(bitmap != null) {
                bitmap = checkRotationImage(bitmap);
                bitmap = resizeBitmap(bitmap, MTFSystemParams.imageWidth, MTFSystemParams.imageHeight);
                saveBitmapToStorageJPG(bitmap);
                deleteRawImage();

                bitmap.recycle();
                bitmap = null;
            }
		}catch(OutOfMemoryError e){
			e.printStackTrace();
		}
	}
	
	private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		int height = options.outHeight;
		int width = options.outWidth;
		int inSampleSize = 1;

	    if (height > reqHeight && width > reqWidth) {
	
	        int halfHeight = height;
	        int halfWidth = width;
	
	        while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }

	    return inSampleSize;
	}
	
	private Bitmap checkRotationImage(Bitmap bitmap){
	    Matrix m = new Matrix();
		try {
			ExifInterface exif = new ExifInterface(cameraFilePath);

		    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
		    if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
		        m.postRotate(180);
		        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
		    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
		        m.postRotate(90);
		        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
		    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
		        m.postRotate(270);
		        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
		    } else{
		        m.postRotate(rotationOfImage);
		        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
		    }
		} catch (IOException e) {
			
		}
	    return bitmap;
		
	}
	
	private void saveBitmapToStorageJPG(Bitmap bitmap){
		
		ContextWrapper cw = new ContextWrapper(context);
		File fileDir = cw.getDir(MTFImageHelper.folderName, Context.MODE_PRIVATE);
		File savedFile = new File(fileDir, dstFilename+"_temp");
		if (!savedFile.exists()) {
			try {
				savedFile.createNewFile();
			} catch (IOException e) {
				
			}
		}

		try {
			FileOutputStream fos = new FileOutputStream(savedFile);
			bitmap.compress(CompressFormat.JPEG, MTFSystemParams.imageQuality, fos);
			fos.close();
			
			secureImage(savedFile);
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			
		}
	}

	private void deleteRawImage(){
		File file = new File(cameraFilePath);
		if (file.exists()) {
			file.delete();
		}
	}
	
	private void secureImage(File sourceFile) throws FileNotFoundException, IOException{
		ContextWrapper cw = new ContextWrapper(context);
		File fileDir = cw.getDir(MTFImageHelper.folderName, Context.MODE_PRIVATE);
		File savedFile = new File(fileDir, dstFilename);
		FileOutputStream fos = new FileOutputStream(savedFile);
		FileInputStream fis = new FileInputStream(sourceFile);
		
		Random random = new Random(System.currentTimeMillis());
		for (int i = 0; i < MTFSystemParams.secureImageSize; i++) {
			fos.write(random.nextInt());
		}
		
		byte[] readFile = new byte[1024];
		int readLenght = 0;
		while(readLenght != -1){
			readLenght = fis.read(readFile);
			if (readLenght != -1) {
				fos.write(readFile, 0, readLenght);
			}
		}
		
		fos.close();
		fis.close();
		
		sourceFile.delete();
	}
	
	private Bitmap resizeBitmap(Bitmap bitmap, int reqWidth, int reqHeight) {
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();

		int targetWidth = reqWidth;
		int targetHeight = reqHeight;
		
		if(width * 1000 / height > reqWidth * 1000 / reqHeight){
			// Convert width to reqWidth
			targetHeight = height * reqWidth / width;
		}else{
			// Convert height to reqHeight
			targetWidth = width * reqHeight / height;
		}
		
		bitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false);
		return bitmap;
	}
}
