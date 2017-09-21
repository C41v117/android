package com.btpn.sinaya.eform.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.btpn.sinaya.eform.MTFBaseActivity;
import com.btpn.sinaya.eform.R;

public class MTFSplashScreen extends MTFBaseActivity{
	
	private static int SPLASH_TIME_OUT = 3000; //3second
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		new Handler().postDelayed(new Runnable() {
			 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
            	
                Intent i = new Intent(MTFSplashScreen.this, MTFLoginActivity.class);
                startActivity(i);
 
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
	}
}
