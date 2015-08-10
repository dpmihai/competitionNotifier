package com.example.competitionnotifier;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;

public class AboutActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.info);
		
		TextView tv = (TextView)findViewById(R.id.about);		
		String about = getResources().getString(R.string.about);
		tv.setText(Html.fromHtml(about));				
		// to have the hyperlink clickable
		tv.setMovementMethod(LinkMovementMethod.getInstance());
		
		TextView version = (TextView)findViewById(R.id.version);				
		TextView author = (TextView)findViewById(R.id.author);		
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			version.setText(getResources().getString(R.string.version) + " " + pInfo.versionName);
			ApplicationInfo aInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
			String authorName = aInfo.metaData.getString("author");
			author.setText(getResources().getString(R.string.author) + " @" + authorName);
		} catch (NameNotFoundException e) {
			Log.e("About", "Could not read version");
		}		
	}
	
}
