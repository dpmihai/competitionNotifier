package com.example.competitionnotifier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
	
	public static final String COMPETITION_ACTION = "com.example.competitionnotifier.ACTION";

	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction();
		if (COMPETITION_ACTION.equals(action)) {
			/**
			 * call method to send sms
			 */
			MainActivity.sendSms();

		}
	}

}
