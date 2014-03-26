package com.ash.timerapp;

import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;

public class MainTimer extends CountDownTimer{
	private boolean timerRunning;
	private int status;
	private static String answer;
	private static long currentTimeLeft;

	private MediaPlayer mPlayer;

	public MainTimer(long startTime, int vibrateLength, String message) {
		super(startTime, 1000);
		timerRunning = true;
		status = Timer.getCurrentStatus();
	}

	@Override
	//Set what happens once the timer has finished it's countdown
	public void onFinish() {
		timerRunning = false;
		Timer.vibrate(500);
		Timer.playSound();
		if(status == 0){
			Timer.startNextTimer(1);
		}

		if(status == 1){
			Timer.startNextTimer(0);
		}
		
		if(status == 2){
			Timer.editMainButton("Start");
			Timer.editDisplay("00:00:00");
		}
	}

	@Override
	//What happens on every tick in the countdown
	public void onTick(long mils) {
		currentTimeLeft = mils;
		answer = String.format("%02d:%02d:%02d", 
		TimeUnit.MILLISECONDS.toHours(mils),
		TimeUnit.MILLISECONDS.toMinutes(mils) -  
		TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(mils)),
		TimeUnit.MILLISECONDS.toSeconds(mils) - 
		TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mils)));   
		//Check if the loop has finished - Used for the pause
		if(answer.equals("00:00:00")){
			timerRunning = false;	
		}
		//Update the text field which shows the numbers counting down
		Timer.editDisplay(answer);
		
	}
	
	public static long getCurrentTimeLeft(){
		return currentTimeLeft;
	}
	
	public boolean isTimerRunning(){
		
		return timerRunning;
	}
	
}

