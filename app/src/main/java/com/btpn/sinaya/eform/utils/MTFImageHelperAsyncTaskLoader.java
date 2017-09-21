package com.btpn.sinaya.eform.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

class MTFImageHelperAsyncTaskLoader extends AsyncTask<Void, Void, Void>{
	
	private ImageView imageView;
	private String srcFilename;
	private MTFImageHelperListener listener;
	private Bitmap bitmap;
	private int failedImageId;
	private Context context;
	
	public MTFImageHelperAsyncTaskLoader(Context context, ImageView imageView, String srcFilename, int failedImageId, MTFImageHelperListener listener) {
		this.context = context;
		this.imageView = imageView;
		this.srcFilename = srcFilename;
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
		loadBitmapFromFile();
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		imageView.post(new Runnable() {
			
			@Override
			public void run() {
				if (bitmap != null) {
					imageView.setImageBitmap(convert(bitmap, Config.RGB_565));
				}else{
					imageView.setImageResource(failedImageId);
				}
			}
		});
		
		if (listener != null) {
			listener.onProcessEnd(srcFilename);
		}
		
		super.onPostExecute(result);
	}
	
	private Bitmap convert(Bitmap bitmap, Bitmap.Config config) {
	    Bitmap convertedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
	    Canvas canvas = new Canvas(convertedBitmap);
	    Paint paint = new Paint();
	    paint.setColor(Color.BLACK);
	    canvas.drawBitmap(bitmap, 0, 0, paint);
	    return convertedBitmap;
	}
	
	private void loadBitmapFromFile(){
		try{
			ContextWrapper cw = new ContextWrapper(imageView.getContext());
			File fileDir = cw.getDir(MTFImageHelper.folderName, Context.MODE_PRIVATE);
			File savedFile = new File(fileDir, srcFilename);
			
			try {
				FileInputStream fis = new FileInputStream(savedFile);
				for (int i = 0; i < MTFSystemParams.secureImageSize; i++) {
					fis.read();
				}
//				bitmap = BitmapFactory.decodeStream(fis);
				
				BitmapFactory.Options options = new BitmapFactory.Options();
			    options.inJustDecodeBounds = true;
			    BitmapFactory.decodeStream(fis, null, options);

			    options.inSampleSize = calculateInSampleSize(options, 200, 200);

			    options.inJustDecodeBounds = false;
			    
			    fis = new FileInputStream(savedFile);
				for (int i = 0; i < MTFSystemParams.secureImageSize; i++) {
					fis.read();
				}
//			    Bitmap bitmap = BitmapFactory.decodeStream(fis, null, options);
			    bitmap = BitmapFactory.decodeStream(fis, null, options);
			} catch (FileNotFoundException e) {
				
			} catch (IOException e) {
				
			}
		}catch(OutOfMemoryError e){
			e.printStackTrace();
			bitmap.recycle();
			bitmap = null;
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
	
	public int dpToPx(int dp) {
	    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
	    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
	    
	    return px;
	}
}
