package com.smartreply.Activity;

import com.smartreply.R;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		tabArranging();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void tabArranging(){
		
	       TabHost tabHost = getTabHost();
	       
	        // Tab for Welcome
	        TabSpec welcomeSpec = tabHost.newTabSpec("Welcome");
	        // setting Title and Icon for the Tab
	        //photospec.setIndicator("Photos", getResources().getDrawable(R.drawable.icon_photos_tab));
	        welcomeSpec.setIndicator("Home");
	        Intent welcomeIntent = new Intent(this, WelcomeActivity.class);
	        welcomeSpec.setContent(welcomeIntent);
	 
	        // Tab for Event
	        TabSpec eventSpec = tabHost.newTabSpec("Events");
	        // setting Title and Icon for the Tab
	        //photospec.setIndicator("Photos", getResources().getDrawable(R.drawable.icon_photos_tab));
	        eventSpec.setIndicator("Events");
	        Intent eventIntent = new Intent(this, EventsActivity.class);
	        eventSpec.setContent(eventIntent);
	   
	        
	     // Tab for groups
	        TabSpec groupSpec = tabHost.newTabSpec("Groups");
	        // setting Title and Icon for the Tab
	        //photospec.setIndicator("Photos", getResources().getDrawable(R.drawable.icon_photos_tab));
	        groupSpec.setIndicator("Groups");
	        Intent groupIntent = new Intent(this, GroupsActivity.class);
	        groupSpec.setContent(groupIntent);
	        
	        
	     // Tab for Template
	        TabSpec templateSpec = tabHost.newTabSpec("Templates");
	        // setting Title and Icon for the Tab
	        //photospec.setIndicator("Photos", getResources().getDrawable(R.drawable.icon_photos_tab));
	        templateSpec.setIndicator("Templates");
	        Intent TemplateIntent = new Intent(this, TemplateActivity.class);
	        templateSpec.setContent(TemplateIntent);
	        
	        // Adding all TabSpec to TabHost
	        tabHost.addTab(welcomeSpec); 
	        tabHost.addTab(eventSpec); 
	        tabHost.addTab(groupSpec);
	        tabHost.addTab(templateSpec);
	        
	      //set Windows tab as default (zero based)
			tabHost.setCurrentTab(2);
	}
}
