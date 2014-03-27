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
	private static long currentTotalTimeLeft;

	private MediaPlayer mPlayer;
	
	private int timerType;

	public MainTimer(long startTime, int vibrateLength, int timer) {
		//timer 0 = revision || timer 1 = totalTimer
		super(startTime, 1000);
		timerRunning = true;
		status = Timer.getCurrentStatus();
		timerType = timer;
		
	}

	@Override
	//Set what happens once the timer has finished it's countdown
	public void onFinish() {
		timerRunning = false;
		if(timerType == 0){
			Timer.vibrate(500);
			Timer.playSound();
		}
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
		if(timerType == 0){
			currentTimeLeft = mils;
		} else if(timerType == 1){
			currentTotalTimeLeft = mils;
		}
		
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
		if(timerType == 0){
			Timer.editDisplay(answer);
		} else if(timerType == 1){
			Timer.editTimeTimeDisplay(answer);
		}
		
	}
	
	public static long getCurrentTimeLeft(){
		return currentTimeLeft;
	}
	
	public static long getCurrentTotalTimeLeft(){
		return currentTotalTimeLeft;
	}
	
	public boolean isTimerRunning(){
		
		return timerRunning;
	}
	
}

