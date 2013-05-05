package com.smartreply.Activity;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.smartreply.R;
import com.smartreply.DatabaseHandling.DataProvider;
import com.smartreply.DatabaseHandling.DatabaseCreator;
import com.smartreply.general.InteractiveArrayAdapter;
import com.smartreply.general.Model;

public class TemplateViewActivity extends Activity {
	private EditText txtTitel = null;
	private EditText txtMessage = null;
	Cursor cursor = null;
	ListView listView = null;
	Context context = null;
	String templateId = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.template_view_layout);
		Intent i = getIntent();
		context = this;
		txtTitel = (EditText) findViewById(R.id.txtTitel);
		txtMessage = (EditText) findViewById(R.id.txtMassage);
		listView = (ListView) findViewById(R.id.listGroups);

		// getting attached intent data
		templateId = i.getStringExtra("template_id");
		String projection[] = { DatabaseCreator.COL_TEMPLATE_TITLE,
				DatabaseCreator.COL_TEMPLATE_MESSAGE };

		// set current data of the template
		Cursor tempalteCursor = getContentResolver().query(
				Uri.withAppendedPath(DataProvider.CONTENT_URI_TEMPLATES,
						String.valueOf(templateId)), projection, null, null,
				null);

		if (tempalteCursor.moveToFirst()) {
			String titel = tempalteCursor.getString(0);
			String message = tempalteCursor.getString(1);
			txtTitel.setText(titel);
			txtMessage.setText(message);
		}
		tempalteCursor.close();

		// set group that have

		try {
			// Create an array of Strings, for List
			ArrayAdapter<Model> adapter = new InteractiveArrayAdapter(this,
					getModel());

			// Assign adapter to ListView
			listView.setAdapter(adapter);

			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Toast.makeText(getApplicationContext(),
							"Click ListItem Number " + position,
							Toast.LENGTH_LONG).show();
				}
			});

		} catch (Exception e) {
			Log.d("**** Exception: ", e.getMessage());
		}
	}

	private List<Model> getModel() {
		List<Model> list = new ArrayList<Model>();
		String[] projection = { ContactsContract.Groups.TITLE,
				ContactsContract.Groups._ID };

		try {
			ContentResolver cr = getContentResolver();
			cursor = cr.query(ContactsContract.Groups.CONTENT_URI, projection,
					null, null, null);

			Cursor templateGroupCursor = cr
					.query(Uri.withAppendedPath(
							DataProvider.CONTENT_URI_TAMPLATE_GROUP,
							String.valueOf(templateId)),
							new String[] { DatabaseCreator.COL_GROUP_TEMPLATE_GROUP_ID },
							null, null, null);
			cursor.moveToFirst();
			if (cursor.moveToFirst()) {
				do {
					String name = cursor.getString(cursor
							.getColumnIndex(ContactsContract.Groups.TITLE));
					// String number = cursor
					// .getString(cursor
					// .getColumnIndex(ContactsContract.Contacts.NUMBER));
					String s = name;
					Boolean select = false;

					// check that group is selected
					templateGroupCursor.moveToFirst();
					if (templateGroupCursor.moveToFirst()) {
						do {
							if ((cursor
									.getString(cursor
											.getColumnIndex(ContactsContract.Groups._ID)))
									.equals(templateGroupCursor.getString(templateGroupCursor
											.getColumnIndex(DatabaseCreator.COL_GROUP_TEMPLATE_GROUP_ID))))
								select = true;
						} while (templateGroupCursor.moveToNext());
					}

					list.add(get(s, select));
					s = null;
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			Log.d("???????? Error in Contacts Read: ", "" + e.getMessage());
		}

		return list;
	}

	private Model get(String s, boolean selected) {
		return new Model(s, selected);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAddGroupToTemplate:
			String s = "";
			// for (int i = 0; i < InteractiveArrayAdapter.list.size(); i++) {
			// if (InteractiveArrayAdapter.list.get(i).isSelected()) {
			// s = s + i + " ";
			// }
			// }
			// String s1 = null;
			// s1 = editText.getText().toString();

			// Check the edittext is empty or not
			// if (s1.equals("")) {
			// Toast.makeText(TemplateViewActivity.this,
			// "Please Enter Any Text", Toast.LENGTH_SHORT).show();
			// return;
			// }

			// Check the Group is available or not
			// Cursor groupCursor = null;
			// String[] GROUP_PROJECTION = new String[] {
			// ContactsContract.Groups._ID, ContactsContract.Groups.TITLE };
			// groupCursor = this.managedQuery(
			// ContactsContract.Groups.CONTENT_URI, GROUP_PROJECTION,
			// ContactsContract.Groups.TITLE + "=?", new String[] { s1 },
			// ContactsContract.Groups.TITLE + " ASC");
			// Log.d("*** Here Counts: ", "** " + groupCursor.getCount());
			//
			// if (groupCursor.getCount() > 0) {
			// Toast.makeText(TemplateViewActivity.this,
			// "Group is already available", Toast.LENGTH_SHORT)
			// .show();
			// return;
			// } else {
			// Toast.makeText(TemplateViewActivity.this, "Not available",
			// Toast.LENGTH_SHORT).show();

			// Here we create a new Group
			// try {
			// ContentValues groupValues = null;
			// ContentResolver cr = this.getContentResolver();
			// groupValues = new ContentValues();
			// groupValues.put(ContactsContract.Groups.TITLE, s1);
			// cr.insert(ContactsContract.Groups.CONTENT_URI, groupValues);
			// Log.d("########### Group Creation Finished :", "###### Success");
			// } catch (Exception e) {
			// Log.d("########### Exception :", "" + e.getMessage());
			// }
			// Toast.makeText(TemplateViewActivity.this, "Created Successfully",
			// Toast.LENGTH_SHORT).show();
			// // }
			//
			// groupCursor.close();
			// groupCursor = null;

			// Log.d(" **** Contacts add to Groups...", "**** Fine");

			// String groupID = null;
			// Cursor getGroupID_Cursor = null;
			// getGroupID_Cursor = this.managedQuery(
			// ContactsContract.Groups.CONTENT_URI, GROUP_PROJECTION,
			// ContactsContract.Groups.TITLE + "=?", new String[] { s1 },
			// null);
			// Log.d("**** Now Empty Cursor size:",
			// "** " + getGroupID_Cursor.getCount());
			// getGroupID_Cursor.moveToFirst();
			// groupID = (getGroupID_Cursor.getString(getGroupID_Cursor
			// .getColumnIndex("_id")));
			// Log.d(" **** Group ID is: ", "** " + groupID);
			//
			// getGroupID_Cursor.close();
			// getGroupID_Cursor = null;

			ContentValues values = new ContentValues();
			values.put(DatabaseCreator.COL_TEMPLATE_MESSAGE, txtMessage
					.getText().toString());
			values.put(DatabaseCreator.COL_TEMPLATE_TITLE, txtTitel
					.getText().toString());
			
			Log.d("TEST", "* :" + templateId);
			if (Integer.parseInt(this.templateId) > -1) {
				// update template value
				Log.d("TEST", "* :" + templateId);
				this.context
						.getContentResolver()
						.update(Uri.withAppendedPath(
								DataProvider.CONTENT_URI_TEMPLATES,
								String.valueOf(templateId)), values, null, null);
			} else {
				//add new template			
				Uri newUri =  this.context.getContentResolver().insert(
				 DataProvider.CONTENT_URI_TEMPLATES, values);
				this.templateId = newUri.getLastPathSegment();
			}

			// delete current record in groupTemplate table
			this.context.getContentResolver().delete(
					Uri.withAppendedPath(
							DataProvider.CONTENT_URI_TAMPLATE_GROUP,
							String.valueOf(templateId)), null, null);

			// Add record to groupTemplate table
			for (int i = 0; i < InteractiveArrayAdapter.list.size(); i++) {
				if (InteractiveArrayAdapter.list.get(i).isSelected()) {
					cursor.moveToPosition(i);

					long groupId = Long.parseLong(cursor.getString(cursor
							.getColumnIndex(ContactsContract.Groups._ID)));
					int templateId_int = Integer.parseInt(templateId);

					addToTemplateGroup(templateId_int, groupId);

					String name = cursor.getString(cursor
							.getColumnIndex(ContactsContract.Groups.TITLE));
					Log.d(" **** Group Added: ", "* :" + name);
					Toast.makeText(TemplateViewActivity.this,
							name + " Added Successfully", Toast.LENGTH_SHORT)
							.show();
				}
			}

			break;
		}
	}

	public Uri addToTemplateGroup(int templateId, long groupId) {
		Log.d("TEST", templateId + " " + groupId);
		ContentValues values = new ContentValues();
		values.put(DatabaseCreator.COL_GROUP_TEMPLATE_GROUP_ID, groupId);
		values.put(DatabaseCreator.COL_GROUP_TEMPLATE_TEMPLATE_ID, templateId);

		return this.context.getContentResolver().insert(
				DataProvider.CONTENT_URI_TAMPLATE_GROUP, values);

	}

}