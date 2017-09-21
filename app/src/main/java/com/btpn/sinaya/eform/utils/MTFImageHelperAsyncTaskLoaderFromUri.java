package com.btpn.sinaya.eform.utils;

import java.io.IOException;
import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.widget.ImageView;

class MTFImageHelperAsyncTaskLoaderFromUri extends AsyncTask<Void, Void, Void>{

	private ImageView imageView;
	private WeakReference<Bitmap> weakRefBitmap;
	private String cameraFilePath;
	private MTFImageHelperListener listener;
	private int failedImageId;
	private int rotationOfImage;
	private int width;
	private int height;

	private int IMAGE_WIDTH = MTFSystemParams.imageWidth;
	private int IMAGE_HEIGHT = MTFSystemParams.imageHeight;
	
	public MTFImageHelperAsyncTaskLoaderFromUri(ImageView imageView, String cameraFilePath, int rotationOfImage, int failedImageId, MTFImageHelperListener listener,
												int width, int height) {
		this.imageView = imageView;
		this.cameraFilePath = cameraFilePath;
		this.listener = listener;
		this.failedImageId = failedImageId;
		this.rotationOfImage = rotationOfImage;
		this.width = width;
		this.height = height;
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
		decodeSampledBitmapFromResource(MTFSystemParams.imageWidth, MTFSystemParams.imageHeight);
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		if (weakRefBitmap.get() != null) {
			imageView.setImageBitmap(weakRefBitmap.get());
		}else{
			imageView.setImageResource(failedImageId);
		}
		
		if (listener != null) {
			listener.onProcessEnd("");
		}
		super.onPostExecute(result);
	}
	
	private void decodeSampledBitmapFromResource(int reqWidth, int reqHeight) {
		try{
		    /*BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeFile(cameraFilePath, options);
	
		    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		    options.inPreferredConfig = Config.RGB_565;
		    options.inJustDecodeBounds = false;
		    
		    Bitmap bitmap = BitmapFactory.decodeFile(cameraFilePath, options);
		    bitmap = checkRotationImage(bitmap);
		    
		    weakRefBitmap = new WeakReference<Bitmap>(bitmap);*/

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bitmap = BitmapFactory.decodeFile(cameraFilePath, options);

			Matrix matrix = new Matrix();
			matrix.postScale(1f, 1f);

			//START - calculate border by screen resolution
			int w = width - (2 * MTFSystemParams.margin);
			int h = height - (2 * MTFSystemParams.margin);

			int w1 = w / MTFSystemParams.width_ratio;
			int h1 = h / MTFSystemParams.height_ratio;

			int result = w1;
			if(h1 < w1){
				result = h1;
			}

			w = MTFSystemParams.width_ratio * result;
			h = MTFSystemParams.height_ratio * result;
			//END - calculate border by screen resolution

			//get image width and height by comparing with screen resolution
			w1 = bitmap.getWidth() * w / width;
			h1 = bitmap.getHeight() * h / height;

			//START - calculate the new width and height using the ratio
			int w2 = w1 / MTFSystemParams.width_ratio;
			int h2 = h1 / MTFSystemParams.height_ratio;

			result = w2;
			if(h2 < w2){
				result = h2;
			}

			w1 = MTFSystemParams.width_ratio * result;
			h1 = MTFSystemParams.height_ratio * result;
			//END - calculate the new width and height using the ratio

			int left = (bitmap.getWidth()-w1) / 2;
			int top = (bitmap.getHeight()-h1) / 2;

			Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, left, top, w1, h1, matrix, true);

			weakRefBitmap = new WeakReference<Bitmap>(croppedBitmap);
		}catch(OutOfMemoryError e){
			e.printStackTrace();
		}
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
		    }else {
		        m.postRotate(rotationOfImage);
		        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
		    }
		} catch (IOException e) {
			
		}
	    return bitmap;
		
	}

}
