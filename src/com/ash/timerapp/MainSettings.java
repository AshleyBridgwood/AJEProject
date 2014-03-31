package com.ash.timerapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MainSettings extends Activity {

	TextView display;
	private static final int RESULT_SETTINGS = 1;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
 
        showUserSettings();
    }
	
		@Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.settings, menu);
	        return true;
	    }
	 
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	 
	        case R.id.menu_settings:
	            Intent i = new Intent(this, UserSettingActivity.class);
	            startActivityForResult(i, RESULT_SETTINGS);
	            break;
	 
	        }
	 
	        return true;
	    }
	 
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	 
	        switch (requestCode) {
	        case RESULT_SETTINGS:
	            showUserSettings();
	            break;
	 
	        }
	 
	    }
	 
	    private void showUserSettings() {
	        SharedPreferences sharedPrefs = PreferenceManager
	                .getDefaultSharedPreferences(this);
	 
	        StringBuilder builder = new StringBuilder();
	 
	        builder.append("\n Set Background: "
	                + sharedPrefs.getString("prefSetBackground", "NULL"));
	 
	        display = (TextView) findViewById(R.id.textUserSettings);
	 
	        settingsTextView.setText(builder.toString());
	    }
	 
	}
