package com.ash.timerapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(1000);
				} catch (InterruptedException e){
					e.printStackTrace();
				} finally{
					Intent openStartingPoint = new Intent("com.ash.timerapp.TIMER");
					startActivity(openStartingPoint);
				}
			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

}
