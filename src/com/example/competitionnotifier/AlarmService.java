package com.example.competitionnotifier;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AlarmService extends Service {

	  @Override
	  public int onStartCommand(Intent i, int flags, int startId) {
		Log.i("competition", "Start Service ...");
		Intent intent = new Intent(this, AlarmReceiver.class);
		intent.setAction(AlarmReceiver.COMPETITION_ACTION);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,PendingIntent.FLAG_CANCEL_CURRENT);
		Calendar calendar = Calendar.getInstance();
		int jobHour = i.getIntExtra("HOUR", 14);
		Log.i("competition", "job hour=" + jobHour);
		int jobMinute = i.getIntExtra("MINUTE", 0);
		Log.i("competition", "job minute=" + jobMinute);
		calendar.set(Calendar.HOUR_OF_DAY, jobHour);
		calendar.set(Calendar.MINUTE, jobMinute);
		calendar.set(Calendar.SECOND, 0);
		AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(pendingIntent);
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),	AlarmManager.INTERVAL_DAY, pendingIntent);
	    return Service.START_NOT_STICKY;
	  }

	  @Override
	  public IBinder onBind(Intent intent) {
	  //TODO for communication return IBinder implementation
	    return null;
	  }
	} 

