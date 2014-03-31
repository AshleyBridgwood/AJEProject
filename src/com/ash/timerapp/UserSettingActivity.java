package com.ash.timerapp;

	import android.annotation.TargetApi;
	import android.os.Bundle;
	import android.preference.PreferenceActivity;
	import android.preference.PreferenceFragment;

	public class UserSettingActivity extends PreferenceActivity
	{
	    private static int preferences=R.xml.preferences;

	   @Override
	    protected void onCreate(final Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	        try {
	            getClass().getMethod("getFragmentManager");
	            AddResourceApi11AndGreater();
	        } catch (NoSuchMethodException e) { //Api < 11
	            AddResourceApiLessThan11();
	        }
	    }

	   @SuppressWarnings("deprecation")
	    protected void AddResourceApiLessThan11()
	    {
	        addPreferencesFromResource(preferences);
	    }

	   @TargetApi(11)
	    protected void AddResourceApi11AndGreater()
	    {
	        getFragmentManager().beginTransaction().replace(android.R.id.content,
	                new PF()).commit();
	    }

	   @TargetApi(11)
	    public static class PF extends PreferenceFragment
	    {       
	       @Override
	        public void onCreate(final Bundle savedInstanceState)
	        {
	            super.onCreate(savedInstanceState);
	            addPreferencesFromResource(R.xml.preferences); 
	            {
	            }
	        }
	    }
	}
