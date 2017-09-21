package com.btpn.sinaya.eform.custom.camera;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MTFCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public MTFCameraSurfaceView(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	reset();
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    	//Do Nothing
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (mHolder.getSurface() == null){
          return;
        }

        try {
            mCamera.stopPreview();
        } catch (Exception e){
        	
        }

        reset();
    }
    
    public void reset(){
    	
    	try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            
        } catch (IOException e) {
        	
        }
    }
}
