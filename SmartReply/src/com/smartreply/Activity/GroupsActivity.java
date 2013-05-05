package com.smartreply.Activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.smartreply.R;

public class GroupsActivity extends Activity {

	ListView listView = null;
	EditText editText = null;
	Cursor cursor = null;
	Context context = null;
	private static final String TAG = "ContentUserDemo";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_layout);

		context = this;
		listView = (ListView) findViewById(R.id.listGroups);

		try {
			// Create an array of Strings, for List

			ContentResolver cr = getContentResolver();
			Log.d(TAG, "Test");
			Cursor cursor = cr.query(ContactsContract.Groups.CONTENT_URI, null,
					null, null, null);

			// Let activity manage the cursor
			startManagingCursor(cursor);
			Log.d(TAG, "cursor.getCount()=" + cursor.getCount());

			// Get the list view
			String[] from = { ContactsContract.Groups.TITLE };
			int[] to = { R.id.title };
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
					R.layout.list_item, cursor, from, to);
			listView.setAdapter(adapter);

			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Toast.makeText(getApplicationContext(),
							"Click ListItem Number " + position,
							Toast.LENGTH_LONG).show();
					// selected item
					String groupId = Long.toString(id);

					// Launching new Activity on selecting single List Item
					Intent i = new Intent(getApplicationContext(),
							GroupCreatingActivity.class);
					// sending data to new activity
					i.putExtra("groupId", groupId);
					startActivity(i);
				}
			});

			Button button = (Button) findViewById(R.id.btnNewGroup);
			button.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// Launching new Activity on selecting single List Item
					Intent i = new Intent(getApplicationContext(),
							GroupCreatingActivity.class);
					startActivity(i);
				}
			});

		} catch (Exception e) {
			Log.d("**** Exception: ", e.getMessage());
		}

	}

}
