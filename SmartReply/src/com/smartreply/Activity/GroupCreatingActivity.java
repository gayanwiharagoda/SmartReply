package com.smartreply.Activity;

import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.smartreply.R;
import com.smartreply.DatabaseHandling.DataProvider;
import com.smartreply.general.InteractiveArrayAdapter;
import com.smartreply.general.Model;

public class GroupCreatingActivity extends Activity {

	ListView listView = null;
	EditText editText = null;
	Cursor cursor = null;
	Context context = null;
	String groupId = null;
	Uri groupUri;

	// Uri contactUri = ContactsContract.Contacts.CONTENT_URI;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_creating_layout);
		Intent i = getIntent();
		groupId = i.getStringExtra("groupId");
		context = this;
		listView = (ListView) findViewById(R.id.listGroupsMembers);
		editText = (EditText) findViewById(R.id.txtGroupName);

		// set title of the group
		if (this.groupId != null) {
			String[] GROUP_PROJECTION = new String[] {
					ContactsContract.Groups._ID, ContactsContract.Groups.TITLE };
			Cursor getGroupTitle_Cursor = this.managedQuery(Uri
					.withAppendedPath(ContactsContract.Groups.CONTENT_URI,
							String.valueOf(groupId)), null, null, null, null);
			if (getGroupTitle_Cursor.moveToFirst()) {
				String groupTitle = getGroupTitle_Cursor
						.getString(getGroupTitle_Cursor
								.getColumnIndex(ContactsContract.Groups.TITLE));
				editText.setText(groupTitle);
			}
		}else{
			editText.setHint("Enter new group name...");
		}
			

		try {
			ArrayAdapter<Model> adapter = new InteractiveArrayAdapter(this,
					getModel());
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

		try {
			ContentResolver cr = getContentResolver();
			// cursor = cr.query(contactUri, null, null, null,
			// ContactsContract.Contacts.DISPLAY_NAME +
			// "COLLATE LOCALIZED ASC");
			cursor = getContacts();

			Log.d("TEST_CONTACT", "" + cursor.getCount());
			Cursor groupContactCusor = null;
			if (this.groupId != null) {
				// get contact of the current group
				groupContactCusor = this
						.managedQuery(
								ContactsContract.Data.CONTENT_URI,
								null,
								ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID
										+ "=" + this.groupId, null, null);
				Log.d("TEST_SELECT_COUNT", "" + groupContactCusor.getCount());

				if (this.groupId != null) {
					groupContactCusor.moveToFirst();
					if (groupContactCusor.moveToFirst()) {
						do {
							Log.d("TEST_groupContactCusorDisplayname",
									""
											+ groupContactCusor
													.getString(groupContactCusor
															.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID))
											+ ",");

						} while (groupContactCusor.moveToNext());
					}
				}
			}

			cursor.moveToFirst();
			if (cursor.moveToFirst()) {
				do {
					String name = cursor
							.getString(cursor
									.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					String contactId = cursor.getString(cursor
							.getColumnIndex(ContactsContract.Contacts._ID));
					boolean seletced = false;
					if (this.groupId != null) {
						// tick already selected contacts
						groupContactCusor.moveToFirst();
						if (groupContactCusor.moveToFirst()) {
							do {
								if (contactId
										.equals(groupContactCusor.getString(groupContactCusor
												.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID)))) {
									Log.d("TEST_TEMP", "in");
									seletced = true;
									break;
								}
							} while (groupContactCusor.moveToNext());
						}
					}
					String s = name;
					list.add(get(s, seletced));
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
		case R.id.btnSave:
			String s = "";
			for (int i = 0; i < InteractiveArrayAdapter.list.size(); i++) {
				if (InteractiveArrayAdapter.list.get(i).isSelected()) {
					s = s + i + " ";
				}
			}
			String s1 = null;
			s1 = editText.getText().toString();

			// Check the edittext is empty or not
			if (s1.equals("")) {
				Toast.makeText(GroupCreatingActivity.this,
						"Please Enter Any Text", Toast.LENGTH_SHORT).show();
				return;
			}

			// Check the Group is available or not
			Cursor groupCursor = null;
			String[] GROUP_PROJECTION = new String[] {
					ContactsContract.Groups._ID, ContactsContract.Groups.TITLE };
			groupCursor = this.managedQuery(
					ContactsContract.Groups.CONTENT_URI, GROUP_PROJECTION,
					ContactsContract.Groups.TITLE + "=?", new String[] { s1 },
					ContactsContract.Groups.TITLE + " ASC");
			Log.d("*** Here Counts: ", "** " + groupCursor.getCount());

			if (this.groupId == null) {
				if (groupCursor.getCount() > 0) {
					Toast.makeText(GroupCreatingActivity.this,
							"Group is already available", Toast.LENGTH_SHORT)
							.show();
					return;
				} else {
					Toast.makeText(GroupCreatingActivity.this, "Not available",
							Toast.LENGTH_SHORT).show();

					// Here we create a new Group
					try {
						ContentValues groupValues = null;
						ContentResolver cr = this.getContentResolver();
						groupValues = new ContentValues();
						groupValues.put(ContactsContract.Groups.TITLE, s1);
						cr.insert(ContactsContract.Groups.CONTENT_URI,
								groupValues);
						Log.d("########### Group Creation Finished :",
								"###### Success");
					} catch (Exception e) {
						Log.d("########### Exception :", "" + e.getMessage());
					}
					Toast.makeText(GroupCreatingActivity.this,
							"Created Successfully", Toast.LENGTH_SHORT).show();
				}

				groupCursor.close();
				groupCursor = null;

				Log.d(" **** Contacts add to Groups...", "**** Fine");

				String groupID = null;
				Cursor getGroupID_Cursor = null;
				getGroupID_Cursor = this.managedQuery(
						ContactsContract.Groups.CONTENT_URI, GROUP_PROJECTION,
						ContactsContract.Groups.TITLE + "=?",
						new String[] { s1 }, null);
				Log.d("**** Now Empty Cursor size:",
						"** " + getGroupID_Cursor.getCount());
				getGroupID_Cursor.moveToFirst();
				this.groupId = (getGroupID_Cursor.getString(getGroupID_Cursor
						.getColumnIndex("_id")));
				getGroupID_Cursor.close();
				getGroupID_Cursor = null;
			}

			Log.d(" **** Group ID is: ", "** " + this.groupId);

			//remove all contact for this group
			this.getContentResolver()
					.delete(ContactsContract.Data.CONTENT_URI,
							ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID
									+ "=? AND "
									+ ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE
									+ "=?",
							new String[] {
									this.groupId,
									ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE });

			
			for (int i = 0; i < InteractiveArrayAdapter.list.size(); i++) {
				if (InteractiveArrayAdapter.list.get(i).isSelected()) {
					cursor.moveToPosition(i);
					Log.d("TEST_ADD_CONTACT",
							""
									+ cursor.getString(cursor
											.getColumnIndex(ContactsContract.Contacts._ID))
									+ ","
									+ cursor.getString(cursor
											.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
					String contactID = cursor.getString(cursor
							.getColumnIndex(ContactsContract.Contacts._ID));

					long contact = Long.parseLong(contactID);
					long group = Long.parseLong(this.groupId);
					String name = cursor
							.getString(cursor
									.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					// check exists if not add contact
					// if (!checkContactAvalability(contact, group)) {
					addToGroup(contact, group);

					Log.d(" **** Contact Added: ", "* :" + name);
					Toast.makeText(GroupCreatingActivity.this,
							name + " Added Successfully", Toast.LENGTH_SHORT)
							.show();
					// } else {
					// Toast.makeText(GroupCreatingActivity.this,
					// name + " Already added", Toast.LENGTH_SHORT)
					// .show();
					// }
				}
			}

			break;

		}
	}

	public Uri addToGroup(long personId, long groupId) {

		ContentValues values = new ContentValues();
		values.put(
				ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID,
				personId);
		values.put(
				ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID,
				groupId);
		values.put(
				ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE,
				ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE);

		return this.context.getContentResolver().insert(
				ContactsContract.Data.CONTENT_URI, values);

	}

	private boolean checkContactAvalability(long personId, long groupId) {
		ContentResolver cr = getContentResolver();
		String[] projection = new String[] {
				ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID,
				ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID };
		String selection = ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID
				+ "="
				+ personId
				+ " AND "
				+ ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID
				+ "=" + groupId;
		Cursor contactCheckCusor = cr.query(ContactsContract.Data.CONTENT_URI,
				projection, selection, null, null);
		if (contactCheckCusor.getCount() > 0) {
			return true;
		} else {
			return false;
		}

	}

	private Cursor getContacts() {
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[] { PhoneLookup._ID,
				PhoneLookup.DISPLAY_NAME };
		// String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP +
		// " = '"(mShowInvisible ? "0" : "1") + "'";
		String[] selectionArgs = null;
		
		String sortOrder = PhoneLookup.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

		return managedQuery(uri, projection, null, selectionArgs, sortOrder);
	}
}
