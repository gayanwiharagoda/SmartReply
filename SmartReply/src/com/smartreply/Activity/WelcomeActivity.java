package com.smartreply.Activity;

import java.util.ArrayList;
import java.util.Date;

import com.smartreply.R;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
	
	@Override
	protected void onPostResume() {
		super.onPostResume();
		setUpView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onUserInteraction() {
		// TODO Auto-generated method stub
		//setUpView();
		super.onUserInteraction();
	}

	private void setUpView() {
		lvCallList = (ListView) this.findViewById(R.id.listCallList);
		setCallerList();
	}

	private void setCallerList() {
		CallListArrey = new ArrayList<String>();
		CallListArrey.clear();

		// get call list
		String[] projection = { CallLog.Calls.CACHED_NAME,
				CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.DATE };
		String where = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE;
		// ORDER BY DATE(updated_at) DESC, name DESC
		String sortOrder = CallLog.Calls.DEFAULT_SORT_ORDER;
		Cursor callListCusor = this.getContentResolver().query(
				CallLog.Calls.CONTENT_URI, projection, where, null, sortOrder);

		callListCusor.moveToFirst();
		if (callListCusor.moveToFirst()) {
			do {
				String callerName = callListCusor.getString(callListCusor
						.getColumnIndex(CallLog.Calls.CACHED_NAME));
				if (callerName == null)
					callerName = "Unknown";
				String callerNumber = callListCusor.getString(callListCusor
						.getColumnIndex(CallLog.Calls.NUMBER));
				String strCallDate = callListCusor.getString(callListCusor
						.getColumnIndex(CallLog.Calls.DATE));
				Date callDate = new Date(Long.valueOf(strCallDate));

				// Time callTime = new Time(Long.valueOf(strCallDate));
				// String callType = callListCusor.getString(callListCusor
				// .getColumnIndex(CallLog.Calls.TYPE));
				CallListArrey.add(callerName + "\t "
						+ callDate.toLocaleString() + "\n" + callerNumber);
			} while (callListCusor.moveToNext());

			itemAdapter = new ArrayAdapter<String>(this,
					R.layout.custom_simple_layout_1, CallListArrey);
			lvCallList.setAdapter(itemAdapter);
			itemAdapter.notifyDataSetChanged();
		}
	}
}
