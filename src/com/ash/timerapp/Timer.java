package com.ash.timerapp;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
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
	//
	private static boolean running;
	private boolean DoNotRun;
	private boolean paused;
	
	private static TextView display;
	private static TextView totalTimerDisplay;
	private static TextView timerTextStatus;
	
	private EditText noOfMinutesRevision;
	private EditText noOfLoopRevision;
	private EditText noOfMinutesBreak;
	private EditText noOfLoopBreak;

	private static long timerValueRevision;
	private static long timerValueBreak;
	
	private static MainTimer timer;
	private static MainTimer totalTimer;
	
	private static Vibrator v;
	
	private static int numMinutesBreak;
	private static int status;
	private static int timerLimit;
	private static int timerCount;
	private int numLoopRevision;
	private int numBreak;
	private int numMinutesRevision;
	
	private static MediaPlayer mediaPlayer;
	
	private long currentTimeLeft;
	private long currentTotalTimeLeft;
	private long totalTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timer);
		
		mediaPlayer = MediaPlayer.create(this, R.raw.beep);
		//get the values from the input boxes - F: timer.xml
		mainButton = (Button) findViewById(R.id.startbtn);
		pauseButton = (Button) findViewById(R.id.pausebtn);
		display = (TextView) findViewById(R.id.display);
		totalTimerDisplay = (TextView) findViewById(R.id.TotalTime);
		timerTextStatus = (TextView) findViewById(R.id.TextStatus);
		
		//Select the values from the input boxes - F: timer.xml
		noOfMinutesRevision = (EditText) findViewById(R.id.noOfMinutesRevision);
		noOfLoopRevision = (EditText) findViewById(R.id.RevisionNum);
		noOfMinutesBreak = (EditText) findViewById(R.id.noOfMinutesBreak);
		noOfLoopBreak = (EditText) findViewById(R.id.BreakNum);
		
		//set the initial values
		running = false;
		timerCount = 0;
		
		v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		//Listener to check if the mainButton has been clicked
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
					
					totalTime = (timerValueRevision * numLoopRevision) + (timerValueBreak * (numLoopRevision - 1));
					if(numLoopRevision == 0){
						timerLimit = 1;
					} else {
						timerLimit = (numLoopRevision * 2) - 1;
					}
					
					if(totalTime == 0){
						totalTime = timerValueRevision;
					}
					
					if(timerValueRevision > 0){
						if(running == false){
							//Start the timer
							startNextTimer(0);
							startTotalTimer(totalTime);
							totalTimer.start();
							Toast.makeText(getApplicationContext(), "Timer Started", Toast.LENGTH_SHORT).show();
							clearInputBoxes();
						} else {
							timer.cancel();
							totalTimer.cancel();
							running = false;
							display.setText("00:00:00");
							totalTimerDisplay.setText("00:00:00");
						}

					} else {
						Toast.makeText(getApplicationContext(), "You have not given any values", Toast.LENGTH_LONG).show();
					}
				}//End of DoNotRun if statement - only do not run when not all values have been stated
				} else {
					//Cancel main timer
					timer.cancel();
					display.setText("00:00:00");
					//Cancel total Timer
					totalTimer.cancel();
					totalTimerDisplay.setText("00:00:00");
					//Set other parts
					mainButton.setText("Start");
					pauseButton.setText("Pause");
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
						currentTotalTimeLeft = MainTimer.getCurrentTotalTimeLeft();
						timer.cancel();
						totalTimer.cancel();
						setShortToast("Timer Paused");
						pauseButton.setText("Restart");
						paused = true;
					} else {
						startTotalTimer(currentTotalTimeLeft);
						totalTimer.start();
						setTimer(currentTimeLeft);
						
						timer.start();
						pauseButton.setText("Pause");
						setShortToast("Timer Restarted");
						paused = false;
					}
				} else {
					setShortToast("You have not started a timer yet!");
				}
			}
		});
	}
	
	//Starts the next timer, checks what timer is next to be displayed, checks if
	//the limit has been reached and displays
	public static void startNextTimer(int v){
		//0 = revision, 1 = break
		if(timerCount <= timerLimit){
			if(v == 0){
				timerCount++;
				setRevisionTimer(timerValueRevision);
				timerTextStatus.setText("Revision");
				timer.start();
				setTimerVariables(true, 1);
				mainButton.setText("Stop");
			} else if(v == 1){
				timerCount++;
				setBreakTimer(timerValueBreak);
				timerTextStatus.setText("Break");
				timer.start();
				setTimerVariables(true, 0);
				mainButton.setText("Stop");	
				
			}
		} else {

			display.setText("00:00:00");
			totalTimerDisplay.setText("00:00:00");
			mainButton.setText("Reset");
			timerCount = 0;
			timerTextStatus.setText("Timer Ended");
			setTimerVariables(false, 2);
		}
	}
	
	private static void setTimerVariables(boolean r, int s){
		running = r;
		status = s;
	}
	public static int getCurrentStatus(){
		return status;
	}
	
	/**
	 * Set the revision timer values
	 * 
	 * @param length timer value used to set the timer
	 * @param v the text to be used once the timer has finished
	 */
	private static void setRevisionTimer(long length){
		timer = new MainTimer(length, 500, 0);
	}
	
	/**
	 * Set break timer values
	 * 
	 * @param length timer value used to set the timer
	 * @param v the text to be used once the timer has finished
	 */
	private static void setBreakTimer(long length){
		timer = new MainTimer(length, 500, 0);
	}
	
	private static void setTimer(long length){
		timer = new MainTimer(length, 500, 0);
	}
	
	private void startTotalTimer(long length){
		totalTimer = new MainTimer(length, 500, 1);
	}
	
	/**
	 * Clears all of the data from the input boxes
	 * Will only clear the boxes which are specified in the method
	 */
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
	
	public static void editTimeTimeDisplay(String text){
		totalTimerDisplay.setText(text);
	}
	
	public static void vibrate(int length){
		v.vibrate(length);
	}
	
	public static void playSound(){
		mediaPlayer.start();
	}
	
}