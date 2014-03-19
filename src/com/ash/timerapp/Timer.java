package com.ash.timerapp;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Timer extends Activity{
	
	private static Button mainButton;
	private static Button pauseButton;
	
	private static boolean running;
	private boolean DoNotRun;
	private boolean paused;
	
	private static TextView display;
	private static TextView timerTextStatus;
	
	private EditText noOfMinutesRevision;
	private EditText noOfLoopRevision;
	private EditText noOfMinutesBreak;
	private EditText noOfLoopBreak;

	private static long timerValueRevision;
	private static long timerValueBreak;
	
	private static MainTimer timer;
	private static Vibrator v;
	
	private static int numMinutesBreak;
	private static int status;
	private static int timerLimit;
	private static int timerCount;
	private int numLoopRevision;
	private int numBreak;
	private int numMinutesRevision;
	
	private String currentTimeLeft;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timer);
		
		//get the values from the input boxes - F: timer.xml
		mainButton = (Button) findViewById(R.id.startbtn);
		pauseButton = (Button) findViewById(R.id.pausebtn);
		display = (TextView) findViewById(R.id.display);
		timerTextStatus = (TextView) findViewById(R.id.TextStatus);
		
		//Select the values from the input boxes - F: timer.xml
		noOfMinutesRevision = (EditText) findViewById(R.id.noOfMinutesRevision);
		noOfLoopRevision = (EditText) findViewById(R.id.RevisionNum);
		noOfMinutesBreak = (EditText) findViewById(R.id.noOfMinutesBreak);
		noOfLoopBreak = (EditText) findViewById(R.id.BreakNum);
		running = false;
		timerCount = 0;

		v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		mainButton.setOnClickListener(new View.OnClickListener(){
			
			public void onClick(View v) {
				//Check to make sure the input boxes are not empty
				if(running == false){
					if(isEmpty(noOfMinutesRevision) || isEmpty(noOfLoopRevision) || isEmpty(noOfMinutesBreak) || isEmpty(noOfLoopBreak) == true){
						//If any of the input boxes are empty, show error message, stop timer from being created
						Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
						DoNotRun = true;
					} else {
						//If all input boxes have been pressed, allow the timer to be created below.
						DoNotRun = false;
					}
				
				//Force close the keyboard incase the user has not	
				closeKeyboard();
				
				if(DoNotRun == false){
					//convert the minutes for both the revision and breaks into int values
					numMinutesRevision = (int)Integer.valueOf(noOfMinutesRevision.getText().toString());
					numMinutesBreak = (int)Integer.valueOf(noOfMinutesBreak.getText().toString());
					//convert the amount of times the timer needs to run into int values
					numLoopRevision = (int)Integer.valueOf(noOfLoopRevision.getText().toString());
					
			
					//Work out the value in milliseconds from the information given
					//timerValue = (((numHours * 60) * 60) + ((numMinutesRevision * 60) + numSeconds)) * 1000;
					timerValueRevision = (numMinutesRevision * 60) * 1000;
					timerValueBreak = (numMinutesBreak * 60) * 1000;
					
					if(numLoopRevision == 0){
						timerLimit = 1;
					} else {
						timerLimit = (numLoopRevision * 2) - 1;
					}
					
					if(timerValueRevision > 0){
						if(running == false){
							//Start the timer
							startNextTimer(0);
							Toast.makeText(getApplicationContext(), "Timer Started", Toast.LENGTH_SHORT).show();
							clearInputBoxes();
						} else {
							timer.cancel();
							running = false;
							display.setText("00:00:00");
						}

					} else {
						Toast.makeText(getApplicationContext(), "You have not given any values", Toast.LENGTH_LONG).show();
					}
				}//End of DoNotRun if statement - only do not run when not all values have been stated
				} else {
					timer.cancel();
					display.setText("00:00:00");
					mainButton.setText("Start");
					running = false;
				}
			}//End of onClick listener
		});
		
		//What happens when the pause button is clicked
		pauseButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v){
				if(running == true){
					if(paused == false){
						currentTimeLeft = MainTimer.getCurrentTimeLeft();
						timer.cancel();
						pauseButton.setText("Restart");
					} else {
						setShortToast("Currently not implemented - Ash");
					}
				} else {
					setShortToast("You have not started a timer yet!");
				}
			}
		});
	}
	
	public static void startNextTimer(int v){
		//0 = revision, 1 = break
		if(timerCount < timerLimit){
			if(v == 0){
				timerCount++;
				setRevisionTimer(timerValueRevision, "Revision Stopped");
				timerTextStatus.setText("Revision");
				timer.start();
				running = true;
				status = 1;
				mainButton.setText("Stop");
			} else if(v == 1){
				timerCount++;
				setBreakTimer(timerValueBreak, "Break Stopped");
				timerTextStatus.setText("Break");
				timer.start();
				running = true;
				status = 0;
				mainButton.setText("Stop");	
				
			}
		} else {

			display.setText("00:00:00");
			mainButton.setText("Start");
		}
	}
	
	/** Set the count down timer with the value */
	private void setTimer(){
		timer = new MainTimer(timerValueRevision, 500, "Timer Stopped");
	}
	
	public static int getCurrentStatus(){
		return status;
	}
	
	public static void setRevisionTimer(long length, String v){
		timer = new MainTimer(length, 500, v);
	}
	
	public static void setBreakTimer(long length, String v){
		timer = new MainTimer(length, 500, v);
	}
	
	private void clearInputBoxes(){
		noOfMinutesRevision.setText("");
		noOfLoopRevision.setText("");
		noOfMinutesBreak.setText("");
		noOfLoopBreak.setText("");
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private boolean isEmpty(EditText etText) {
	    if (etText.getText().toString().trim().length() > 0) {
	        return false;
	    } else {
	        return true;
	    }
	}
	
	/** Force closes the keyboard once called */
	public void closeKeyboard(){
		final InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	public void setShortToast(String t){
		Toast.makeText(getApplicationContext(), t, Toast.LENGTH_SHORT).show();
	}
	
	public static void editMainButton(String text){
		mainButton.setText(text);
	}
	
	public static void editDisplay(String text){
		display.setText(text);
	}
	
	public static void vibrate(int length){
		v.vibrate(length);
	}
	
}