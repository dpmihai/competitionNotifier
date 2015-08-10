package com.example.competitionnotifier;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final int RESULT_SETTINGS = 1;
	private static final int RESULT_INFO = 2;
	
	static Context context;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context = MainActivity.this;
		
		// load preferences 
		PreferenceManager.setDefaultValues(MainActivity.this, R.xml.notifier_prefs, true);
		setContentView(R.layout.activity_main);
		
		Button btnSend = (Button) findViewById(R.id.button);		
		btnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                  sendSms();
            }
		});
		
		Button btnStartService = (Button) findViewById(R.id.button2);	
		btnStartService.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                  fireAlarmAsService();
            }
		});
		
		Button btnStopService = (Button) findViewById(R.id.button3);	
		btnStopService.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                  stopAlarmService();
            }
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent i = new Intent(this, NotifierSettingsActivity.class);
			startActivityForResult(i, RESULT_SETTINGS);
			break;
		case R.id.menu_info:
			Intent i2 = new Intent(this, AboutActivity.class);
			startActivityForResult(i2, RESULT_INFO);
			break;	
		}
		return true;
	}
	
	public void fireAlarm() {
        /**
         * call broadcost reciver for send sms
         */
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(AlarmReceiver.COMPETITION_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent,PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar calendar = Calendar.getInstance();
        int jobHour = Utils.getJobHour(context);
        Log.i("competition", "job hour="+jobHour);
        int jobMinute = Utils.getJobMinute(context);
        Log.i("competition", "job minute="+jobMinute);
        calendar.set(Calendar.HOUR_OF_DAY, jobHour); 
        calendar.set(Calendar.MINUTE, jobMinute);
        calendar.set(Calendar.SECOND, 0);
        AlarmManager alarm = (AlarmManager) MainActivity.this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pendingIntent);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);       
	}
	
	public void fireAlarmAsService() {
		Intent serviceIntent = new Intent(context, AlarmService.class);		
		int jobHour = Utils.getJobHour(context);
        Log.i("competition", "job hour="+jobHour);
        int jobMinute = Utils.getJobMinute(context);
        Log.i("competition", "job minute="+jobMinute);
        serviceIntent.putExtra("HOUR", jobHour);
        serviceIntent.putExtra("MINUTE", jobMinute);
		context.startService(serviceIntent); 
		Toast.makeText(context, "Service started.", Toast.LENGTH_LONG).show();
	}
	
	public void stopAlarmService() {
		context.stopService(new Intent(context, AlarmService.class));
		Toast.makeText(context, "Service stopped.", Toast.LENGTH_LONG).show();
	}
	
	public static void sendSms() {
		String url = Utils.getNotifierUrl(context);
		new PerformBackgroundTask().execute(url);
	}
	
	static class PerformBackgroundTask  extends AsyncTask {
		
		private static final int OK = 1;		
		private static final int FAILED = 2;

	    //private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
	    InputStream inputStream = null;
	    String result = "[]"; 
	    
	    public PerformBackgroundTask () {
			super();
		}


//	    protected void onPreExecute() {
//	        progressDialog.setMessage("Downloading notifications...");
//	        progressDialog.show();
//	        progressDialog.setOnCancelListener(new OnCancelListener() {
//	            public void onCancel(DialogInterface arg0) {
//	            	PerformBackgroundTask .this.cancel(true);
//	            }
//	        });
//	    }

	    @Override
	    protected Object doInBackground(Object... params) {

	    	String url = (String) params[0];
	       
	        ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
	        try {
	            // Set up HTTP post

	            // HttpClient is more then less deprecated. Need to change to URLConnection
	            HttpClient httpClient = new DefaultHttpClient();	            
	            Log.i("competition", "task url =" +  url);
	            HttpPost httpPost = new HttpPost(url);
	            httpPost.setEntity(new UrlEncodedFormEntity(param));
	            HttpResponse httpResponse = httpClient.execute(httpPost);
	            HttpEntity httpEntity = httpResponse.getEntity();

	            // Read content & Log
	            inputStream = httpEntity.getContent();
	            
	            BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
	            StringBuilder sBuilder = new StringBuilder();

	            String line = null;
	            while ((line = bReader.readLine()) != null) {
	                sBuilder.append(line + "\n");
	            }

	            inputStream.close();
	            result = sBuilder.toString();	           	          

	        } catch (Throwable e1) {
	            Log.e("UnsupportedEncodingException", e1.toString());
	            e1.printStackTrace();
	            return FAILED;
	        } 	       
	        return OK;
	    } 

	    @Override
	    protected void onPostExecute(Object v) {
	    	
	    	if (((Integer)v).intValue() == FAILED) {
	    		Toast.makeText(context, "Server connection : " + Utils.getNotifierUrl(context) + " failed.", Toast.LENGTH_LONG).show();
	    		return;
	    	}
	    	
	    	Log.i("postExecute-json", result);
	        
	    	//parse JSON data	    	
	    	ObjectMapper mapper = new ObjectMapper().setVisibility(PropertyAccessor.ALL, Visibility.ANY);
	    	mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    	try {
				List<MailData> list = mapper.readValue(result, new TypeReference<List<MailData>>(){});
				System.out.println(list);
				
				List<MailData> filteredList = new ArrayList<MailData>();
				for (MailData md : list) {
					if ((md.getDaysToStage() == 1) && (md.getUser().getPhone() != null)) {
						filteredList.add(md);
					}
				}
				
				if (filteredList.size() == 0) {
					Toast.makeText(context, "No sms to send.", Toast.LENGTH_LONG).show();
				} else {				
					SmsManager smsManager = SmsManager.getDefault();
					for (MailData md : filteredList) {					
						//System.out.println("  ---> found : " + md);
						String message = "Competition\n\rNu uita de scoruri.\n\rMaine e etapa\n\r" + md.getCompetitionName() + " / " + md.getStageName();
						try {
							smsManager.sendTextMessage(md.getUser().getPhone(), null, message, null, null);
							String info = "Sms\n\r\n\r" + message + "\n\r\n\r" + md.getUser().getUsername() + " (" + md.getUser().getPhone() + ")\n\rOK.";
							Log.i("competition", info);
							Toast.makeText(context, info, Toast.LENGTH_LONG).show();
							
							// save sms in sent
							ContentValues values = new ContentValues();
							values.put("address", md.getUser().getPhone());
							values.put("body", message);
							context.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
						} catch (Exception e) {
							String info = "Sms\n\r\n\r" + message + "\n\r\n\r" + md.getUser().getUsername() + " (" + md.getUser().getPhone() + ")\n\rFAILED.";
							Log.i("competition", info);
							Toast.makeText(context, info, Toast.LENGTH_LONG).show();
						} 							
					}
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        
	    } 
	}

}
