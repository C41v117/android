package com.btpn.sinaya.eform.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.widget.ImageView;

class MTFImageHelperAsyncTaskSaverFromUri extends AsyncTask<Void, Void, Void>{

	private ImageView imageView;
	private Bitmap bitmap;
	private String dstFilename;
	private String cameraFilePath;
	private MTFImageHelperListener listener;
	private int failedImageId;
	
	public MTFImageHelperAsyncTaskSaverFromUri(ImageView imageView, String cameraFilePath, String dstFilename, int failedImageId, MTFImageHelperListener listener) {
		this.imageView = imageView;
		this.dstFilename = dstFilename;
		this.cameraFilePath = cameraFilePath;
		this.listener = listener;
		this.failedImageId = failedImageId;
	}
	
	@Override
	protected void onPreExecute() {
		if (listener != null) {
			listener.onProcessStart();
		}
		super.onPreExecute();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		bitmap = decodeSampledBitmapFromResource(MTFSystemParams.imageWidth, MTFSystemParams.imageHeight);
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		if (imageView != null) {
			imageView.setImageBitmap(bitmap);
		}else{
			imageView.setImageResource(failedImageId);
		}
		
		
		if (listener != null) {
			listener.onProcessEnd(dstFilename);
		}
		super.onPostExecute(result);
	}
	
	private Bitmap decodeSampledBitmapFromResource(int reqWidth, int reqHeight) {

	    BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(cameraFilePath, options);

	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    options.inJustDecodeBounds = false;
	    
	    Bitmap bitmap = BitmapFactory.decodeFile(cameraFilePath, options);
	    bitmap = checkRotationImage(bitmap);
		saveBitmapToStorageJPG(bitmap);
		deleteRawImage();
	    
	    return bitmap;
	}
	
	private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		int height = options.outHeight;
		int width = options.outWidth;
		int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {
	
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
		    }
		} catch (IOException e) {
			
		}
	    return bitmap;
		
	}
	
	private void saveBitmapToStorageJPG(Bitmap bitmap){
		
		ContextWrapper cw = new ContextWrapper(imageView.getContext());
		File fileDir = cw.getDir(MTFImageHelper.folderName, Context.MODE_PRIVATE);
		File savedFile = new File(fileDir, dstFilename);
		if (!savedFile.exists()) {
			try {
				savedFile.createNewFile();
			} catch (IOException e) {
				
			}
		}
		
		try {
			FileOutputStream fos = new FileOutputStream(savedFile);
			bitmap.compress(CompressFormat.JPEG, 100, fos);
			fos.close();

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

}
