package com.btpn.sinaya.eform.custom.camera;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.utils.MTFConstants;
import com.btpn.sinaya.eform.utils.MTFSystemParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MTFCameraActivity extends Activity implements SurfaceHolder.Callback{

	private static final int ORIENTATION_PORTRAIT_NORMAL =  1;
	private static final int ORIENTATION_PORTRAIT_INVERTED =  2;
	private static final int ORIENTATION_LANDSCAPE_NORMAL =  3;
	private static final int ORIENTATION_LANDSCAPE_INVERTED =  4;
	
	private MTFCameraSurfaceView mPreview;
	private Camera mCamera;
	private FrameLayout mPreviewLayout;
	private ImageButton mCaptureButton;
	private SurfaceView cameraView;
	private SurfaceHolder holder;
	private SurfaceView transparentView;
	private SurfaceHolder holderTransparent;
	public static boolean previewing = false;
	
	private File pictureFile;
	private String requestFilePath = "";

	private PictureCallback mPictureCallback = new PictureCallback() {

	    @Override
	    public void onPictureTaken(byte[] data, Camera camera) {
            mCamera.stopPreview();
            
            if (requestFilePath.isEmpty()) {
    	        pictureFile = getOutputMediaFile();
    	        if (pictureFile == null){
    	            //Show Error Save Failed
    	            return;
    	        }
			}else{
				pictureFile = new File(requestFilePath);
			}

	        try {
	            FileOutputStream fos = new FileOutputStream(pictureFile);
	            fos.write(data);
	            fos.close();
	            
	            openPreviewActivity();
	        } catch (FileNotFoundException e) {
	        	//Show Error File not found
	        } catch (IOException e) {
	        	//Show Error Access File Failed
	        }
	    }
	};
	
	private OnClickListener mOnCaptureListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			mCamera.takePicture(null, null, mPictureCallback);
		}
	};
	
	private OnLongClickListener mOnLongCaptureListener = new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View v) {
			mCamera.autoFocus(mOnAutoFocusCallback);
			return false;
		}
	};
	
	private OnClickListener mOnClickPreviewListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			try {
				mCamera.autoFocus(mOnAutoFocusCallback);
			}catch (Exception e){
				e.printStackTrace();
			}

		}
	};
	
	private AutoFocusCallback mOnAutoFocusCallback = new AutoFocusCallback() {
		
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			//Do Nothing
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_camera);

		loadView();
		
		if (!isHaveCameraHardware()) {
			//Show Error No Camera Found
			return;
		}
		
		if (getIntent().hasExtra(MTFConstants.EXTRA_OUTPUT)) {
			requestFilePath = getIntent().getStringExtra(MTFConstants.EXTRA_OUTPUT);
		}
		
		mCamera = getCameraInstance();

		configureCameraParameters();
		configureCameraPreview();
        
        mCaptureButton.setOnClickListener(mOnCaptureListener);
        mCaptureButton.setOnLongClickListener(mOnLongCaptureListener);

		// Create first surface with his holder(holder)
		cameraView = (SurfaceView)findViewById(R.id.CameraView);

		holder = cameraView.getHolder();
		holder.setFormat(PixelFormat.TRANSPARENT);
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		// Create second surface with another holder (holderTransparent)
		transparentView = (SurfaceView)findViewById(R.id.TransparentView);

		holderTransparent = transparentView.getHolder();
		holderTransparent.setFormat(PixelFormat.TRANSPARENT);
		holderTransparent.addCallback(this);
		holderTransparent.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		if(holder.equals(this.holder)) {
			//get screen resolution
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;
			int height = size.y;

			//substract width and height with margin
			int w = width - (2 * MTFSystemParams.margin);
			int h = height - (2 * MTFSystemParams.margin);

			//new width and height divided by ratio
			int w1 = w / MTFSystemParams.width_ratio;
			int h1 = h / MTFSystemParams.height_ratio;

			//get the less one
			int result = w1;
			if(h1 < w1){
				result = h1;
			}

			//use the less one to get a new width and height (using ratio)
			w = MTFSystemParams.width_ratio * result;
			h = MTFSystemParams.height_ratio * result;

			//calculate 4 dimension for the border
			int left = (width-w) / 2;
			int top = (height-h) / 2;
			int right = left + w;
			int bottom = top + h;

			DrawFocusRect(left, top, right, bottom, 0x77000000, holder);
		}
	}

	private void DrawFocusRect(float RectLeft, float RectTop, float RectRight, float RectBottom, int color, SurfaceHolder holder)
	{
		Canvas canvas = holder.lockCanvas();
		canvas.drawColor(0, PorterDuff.Mode.CLEAR);
		//border's properties
		/*Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(color);
		paint.setStrokeWidth(10);*/

		Paint paintFill = new Paint();
		paintFill.setStyle(Paint.Style.FILL);
		paintFill.setColor(color);

		RectF above = new RectF(0, 0, RectRight, RectTop);
		RectF left = new RectF(0, RectTop, RectLeft, canvas.getHeight());
		RectF right = new RectF(RectRight, 0, canvas.getWidth(), RectBottom);
		RectF bottom = new RectF(RectLeft, RectBottom, canvas.getWidth(), canvas.getHeight());

		canvas.drawRect(above, paintFill);
		canvas.drawRect(left, paintFill);
		canvas.drawRect(right, paintFill);
		canvas.drawRect(bottom, paintFill);
		//canvas.drawRect(RectLeft, RectTop, RectRight, RectBottom, paint);

		holder.unlockCanvasAndPost(canvas);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height) {
		/*if(previewing){
			mCamera.stopPreview();
			previewing = false;
		}

		if (mCamera != null){
			try {
				mCamera.setPreviewDisplay(surfaceHolder);
				mCamera.startPreview();
				previewing = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		/*mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
		previewing = false;*/

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MTFConstants.REQUEST_CODE_CAMERA && resultCode == MTFConstants.RESULT_CODE_OK) {
			Intent intent = new Intent();
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
    @Override
    protected void onPause() {
    	if (mCaptureButton.getVisibility() == View.VISIBLE) {
        	mCamera.stopPreview();	
		}
    	
    	super.onPause();
    }
    
    @Override
    protected void onResume() {
		if (mCaptureButton.getVisibility() == View.VISIBLE) {
			mPreview.reset();
		}
    	
    	super.onResume();
    }
    
    @Override
    protected void onDestroy() {
    	mCamera.release();
    	super.onDestroy();
    }
    
    private void loadView(){
        mPreviewLayout = (FrameLayout) findViewById(R.id.camera_preview);
        mCaptureButton = (ImageButton) findViewById(R.id.capture_button);
        
    }
    
    private boolean isHaveCameraHardware() {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        } else {
            return false;
        }
    }
    
    private void configureCameraParameters(){
        Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Parameters.FOCUS_MODE_AUTO);
        mCamera.setParameters(parameters);
    }
    
    private void configureCameraPreview(){
        mPreview = new MTFCameraSurfaceView(this, mCamera);
        mPreview.setOnClickListener(mOnClickPreviewListener);
        
        mPreviewLayout.addView(mPreview);
    }
    
    private Camera getCameraInstance(){
        Camera result = null;
        try {
        	result = Camera.open(); 
        }
        catch (Exception e){
        	
        }
        return result; 
    }
    
    private File getOutputMediaFile(){
    	
    	String cameraFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/adytzs";
		File file = new File(cameraFilePath);
		//Check Directory
		if (!file.exists()) {
			file.mkdirs();
		}
		
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.US).format(new Date());
		file = new File(file,"IMG_"+timeStamp+".jpg");
		return file;
    }
    
    private void changeRotation(int orientation, int lastOrientation) {
    	int degrees = 0;
        switch (orientation) {
            case ORIENTATION_PORTRAIT_NORMAL:
            	degrees = 270;
                break;
            case ORIENTATION_LANDSCAPE_NORMAL:
            	degrees = 0;
                break;
            case ORIENTATION_PORTRAIT_INVERTED:
            	degrees = 90;
                break;
            case ORIENTATION_LANDSCAPE_INVERTED:
            	degrees = 180;
                break;
        }

        mCaptureButton.setImageBitmap(getRotatedImage(R.drawable.ic_camera, degrees));
    }
    
    private Bitmap getRotatedImage(int drawableId, int degrees) {
        Bitmap original = BitmapFactory.decodeResource(getResources(), drawableId);
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        
        Bitmap rotated = Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, false);
        return rotated;
    }
    
    private void openPreviewActivity(){
    	Intent intent = new Intent(this, MTFCameraPreviewActivity.class);
    	intent.putExtra(MTFConstants.EXTRA_OUTPUT, pictureFile.getAbsolutePath());
    	startActivityForResult(intent, MTFConstants.REQUEST_CODE_CAMERA);
    }
}
