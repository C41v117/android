package com.btpn.sinaya.eform.custom.camera;

import java.io.File;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.utils.MTFConstants;
import com.btpn.sinaya.eform.utils.MTFImageHelper;
import com.btpn.sinaya.eform.utils.MTFImageHelperListener;
import com.btpn.sinaya.eform.utils.MTFSystemParams;

public class MTFCameraPreviewActivity extends Activity implements OnClickListener{
	
	private ImageButton acceptButton;
	private ImageButton rejectButton;
	private ImageView previewImageView;
	private String mFilePath;
	private int mRotationOfImage = 0;

	private int IMAGE_WIDTH = MTFSystemParams.imageWidth;
	private int IMAGE_HEIGHT = MTFSystemParams.imageHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_camera_preview);
		loadView();
		initiateDefaultValue();
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.accept_button:
			acceptImage();
			break;
		case R.id.reject_button:
			rejectImage();
			break;

		default:
			break;
		}
	}
	
	private void loadView(){
		acceptButton = (ImageButton)findViewById(R.id.accept_button);
		rejectButton = (ImageButton)findViewById(R.id.reject_button);
		previewImageView = (ImageView)findViewById(R.id.preview_imageview);
	}
	
	private void initiateDefaultValue(){
		acceptButton.setOnClickListener(this);
		rejectButton.setOnClickListener(this);
		

		
		if (getIntent().hasExtra(MTFConstants.EXTRA_ROTATION)) {
			mRotationOfImage = getIntent().getIntExtra(MTFConstants.EXTRA_ROTATION, 0);
		}
		
		if (getIntent().hasExtra(MTFConstants.EXTRA_OUTPUT)) {
			mFilePath = getIntent().getStringExtra(MTFConstants.EXTRA_OUTPUT);
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;
			int height = size.y;

			MTFImageHelper.setImageViewFromCamera(previewImageView, mFilePath, mRotationOfImage, R.drawable.x_mark, new MTFImageHelperListener() {
				
				@Override
				public void onProcessStart() {}
				
				@Override
				public void onProcessEnd(String finalFileName) {}
			}, width, height);
		}
	}
	
	private void rejectImage(){
		deleteFile();
		setResult(MTFConstants.RESULT_CODE_CANCEL);
		finish();
	}
	
	private void acceptImage(){
		setResult(MTFConstants.RESULT_CODE_OK);
		finish();
	}
	
	private void deleteFile(){
		File savedFile = new File(mFilePath);
		
		if (savedFile.exists()) {
			savedFile.delete();
		}
	}
	
}
