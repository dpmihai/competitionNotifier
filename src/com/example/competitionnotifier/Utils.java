package com.example.competitionnotifier;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Utils {	

	public static String getNotifierUrl(Context context) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPrefs.getString("notifier_url", "");		
	}
	
	public static int getJobHour(Context context) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		int defaultHour = 14;
		try {
			defaultHour = Integer.parseInt(sharedPrefs.getString("job_hour", String.valueOf(defaultHour)));
		} catch (NumberFormatException ex) {
			// nothing to do
		}
		return defaultHour;
	}
	
	public static int getJobMinute(Context context) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		int defaultMinute = 0;
		try {
			defaultMinute = Integer.parseInt(sharedPrefs.getString("job_minute", String.valueOf(defaultMinute)));
		} catch (NumberFormatException ex) {
			// nothing to do
		}
		return defaultMinute;
	}

}
