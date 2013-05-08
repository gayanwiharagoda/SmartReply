package com.smartreply.Activity;

import java.util.ArrayList;

import com.smartreply.R;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class WelcomeActivity extends Activity {
	private ListView lvCallList;
	private ArrayList<String> CallListArrey;
	private ArrayAdapter<String> itemAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_layout);
		setUpView();

	}

	private void setUpView() {
		lvCallList = (ListView) this.findViewById(R.id.listCallList);

		CallListArrey = new ArrayList<String>();
		CallListArrey.clear();

		// get call list
		String[] projection = { CallLog.Calls.CACHED_NAME,
				CallLog.Calls.NUMBER, CallLog.Calls.TYPE };
		String where = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE;

		Cursor callListCusor = this.getContentResolver().query(
				CallLog.Calls.CONTENT_URI, projection, where, null, null);
		callListCusor.moveToFirst();
		do {
			String callerName = callListCusor.getString(callListCusor
					.getColumnIndex(CallLog.Calls.CACHED_NAME));
			String callerNumber = callListCusor.getString(callListCusor
					.getColumnIndex(CallLog.Calls.NUMBER));
			String callType = callListCusor.getString(callListCusor
					.getColumnIndex(CallLog.Calls.TYPE));
			CallListArrey.add(callerName + "\n" + callerNumber );
		} while (callListCusor.moveToNext());

		itemAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, CallListArrey);
		lvCallList.setAdapter(itemAdapter);

	}

}
