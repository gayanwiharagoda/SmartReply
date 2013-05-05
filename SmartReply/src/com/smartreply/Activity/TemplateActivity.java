package com.smartreply.Activity;

import com.smartreply.R;
import com.smartreply.DatabaseHandling.DataProvider;
import com.smartreply.DatabaseHandling.DatabaseCreator;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class TemplateActivity extends Activity {
	private static final String TAG = "SmartReply";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.template_layout);

		// Tute content provider
		String[] projection = { DatabaseCreator.COL_ID,
				DatabaseCreator.COL_TEMPLATE_TITLE };

		// Get content provider and cursor
		ContentResolver cr = getContentResolver();
		Cursor tutorials = cr.query(DataProvider.CONTENT_URI_TEMPLATES, projection, null,
				null, null);

		// Let activity manage the cursor
		startManagingCursor(tutorials);
		Log.d(TAG, "cursor.getCount()=" + tutorials.getCount());

		// Get the list view
		ListView listView = (ListView) findViewById(R.id.listView);
		String[] uiBindFrom = { DatabaseCreator.COL_TEMPLATE_TITLE };
		int[] uiBindTo = { R.id.title };
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.list_item, tutorials, uiBindFrom, uiBindTo);
		listView.setAdapter(adapter);

		// listening to single list item on click
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// selected item
				String template_id = Long.toString(id);

				// Launching new Activity on selecting single List Item
				Intent i = new Intent(getApplicationContext(),
						TemplateViewActivity.class);
				// sending data to new activity
				i.putExtra("template_id", template_id);
				startActivity(i);
			}
		});
		
		

	}
}
