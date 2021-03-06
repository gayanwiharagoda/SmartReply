package com.smartreply.Activity;

import com.smartreply.R;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.ToggleButton;

public class MainActivity extends TabActivity {

	ToggleButton onOfButton = null;
	public static final String SMTR_REPLY_PREFS = "smrt_reply_pref";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		// Restore preferences
		SharedPreferences settings = getSharedPreferences(SMTR_REPLY_PREFS, 0);
		boolean isOn =settings.getBoolean("isOn", true);
		
		this.onOfButton = (ToggleButton) findViewById(R.id.toggleButton1);
		this.onOfButton.setChecked(isOn);
		this.onOfButton
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton toggleButton,
							boolean isChecked) {
						SharedPreferences settings = getSharedPreferences(
								SMTR_REPLY_PREFS, 0);
						SharedPreferences.Editor editor = settings.edit();
						editor.putBoolean("isOn", isChecked);
						editor.commit();
					}
				});

		tabArranging();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void tabArranging() {

		TabHost tabHost = getTabHost();

		// Tab for Welcome
		TabSpec welcomeSpec = tabHost.newTabSpec("Welcome");
		// setting Title and Icon for the Tab
		// photospec.setIndicator("Photos",
		// getResources().getDrawable(R.drawable.icon_photos_tab));
		welcomeSpec.setIndicator("Home");
		Intent welcomeIntent = new Intent(this, WelcomeActivity.class);
		welcomeSpec.setContent(welcomeIntent);

		// Tab for Event
		// TabSpec eventSpec = tabHost.newTabSpec("Events");
		// // setting Title and Icon for the Tab
		// // photospec.setIndicator("Photos",
		// // getResources().getDrawable(R.drawable.icon_photos_tab));
		// eventSpec.setIndicator("Events");
		// Intent eventIntent = new Intent(this, EventsActivity.class);
		// eventSpec.setContent(eventIntent);

		// Tab for groups
		TabSpec groupSpec = tabHost.newTabSpec("Groups");
		// setting Title and Icon for the Tab
		// photospec.setIndicator("Photos",
		// getResources().getDrawable(R.drawable.icon_photos_tab));
		groupSpec.setIndicator("Groups");
		Intent groupIntent = new Intent(this, GroupsActivity.class);
		groupSpec.setContent(groupIntent);

		// Tab for Template
		TabSpec templateSpec = tabHost.newTabSpec("Templates");
		// setting Title and Icon for the Tab
		// photospec.setIndicator("Photos",
		// getResources().getDrawable(R.drawable.icon_photos_tab));
		templateSpec.setIndicator("Templates");
		Intent TemplateIntent = new Intent(this, TemplateActivity.class);
		templateSpec.setContent(TemplateIntent);

		// Adding all TabSpec to TabHost
		tabHost.addTab(welcomeSpec);
		// tabHost.addTab(eventSpec);
		tabHost.addTab(groupSpec);
		tabHost.addTab(templateSpec);

		// set Windows tab as default (zero based)
		tabHost.setCurrentTab(0);
	}

}
