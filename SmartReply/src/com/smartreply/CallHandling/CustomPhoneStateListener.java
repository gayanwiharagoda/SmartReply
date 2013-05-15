package com.smartreply.CallHandling;

import com.smartreply.Activity.MainActivity;
import com.smartreply.DatabaseHandling.DataProvider;
import com.smartreply.DatabaseHandling.DatabaseCreator;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CustomPhoneStateListener extends PhoneStateListener {

	Context context;
	String callState;
//	private static int previousNoOfMissCall;
	private static int previousCallState;
	private final String unknowNumberMessage = "I am not at the phone answer to call. Can you call me later."; 
	private final String contactNoneGourpedMessage = "I am currently unable to answer the phone."; 
	private final String groupDoNotHaveTemplateMessage = "I will get back to you later."; 

	public CustomPhoneStateListener(Context context) {
		super();
		this.context = context;
	}

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		super.onCallStateChanged(state, incomingNumber);

		switch (state) {
		case TelephonyManager.CALL_STATE_IDLE:
			callState = "IDEAL";
			if (previousCallState == TelephonyManager.CALL_STATE_RINGING) {
				if (MainActivity.isOn) {
					String message = "TEST";
					message = this.getMessage(incomingNumber);
					sendSMSToMissNo(incomingNumber, message);
				}
			}
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			callState = "OFFHOOK";
			break;
		case TelephonyManager.CALL_STATE_RINGING:
			callState = "RIGING";
			//previousNoOfMissCall = this.getMisscallCount();
			break;
		default:
			break;
		}
		previousCallState = state;
		Log.i(">>>Broadcast", "onCallStateChanged " + callState);
	}

	public int getMisscallCount() {
		String[] projection = { CallLog.Calls.CACHED_NAME,
				CallLog.Calls.CACHED_NUMBER_LABEL, CallLog.Calls.TYPE };
		String where = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE;

		Cursor c = context.getContentResolver().query(
				CallLog.Calls.CONTENT_URI, projection, where, null, null);
		c.moveToFirst();
		Log.d("CALL", "" + c.getCount()); // do some other operation
		return c.getCount();
	}

	private void sendSMSToMissNo(String phoneNumber, String message) {
		// if (this.validateMissCall(previousNoOfMissCall)) {
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
		// }
	}

	private boolean validateMissCall(int preNoOfMissCall) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int crtNoOfMissCall = this.getMisscallCount();

		Log.d("CALL", "" + "In validate" + crtNoOfMissCall);
		if (preNoOfMissCall == crtNoOfMissCall) {
			return false;
		}
		return true;
	}

	// creating message
	/**
	 * @param phoneNo
	 * @return
	 */
	private String getcontactId(String phoneNo) {
		String[] projection = { BaseColumns._ID};
		Cursor cursor = context.getContentResolver().query(
				Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNo)),
				projection,
				null,
				null,
				null);
		Log.d("ConcactId", "" + cursor.getCount());
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			return (cursor
					.getString(cursor
							.getColumnIndex(BaseColumns._ID)));
		}

		return null;
	}

	private String getGroupId(String contactId) {

		Cursor cursor = context
				.getContentResolver()
				.query(ContactsContract.Data.CONTENT_URI,
						new String[] {
								ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID,
								ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID },
						ContactsContract.Data.MIMETYPE + "=? AND "
								+ ContactsContract.Data.RAW_CONTACT_ID + "=?",
						new String[] {
								ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE,
								contactId }, null);

		Log.d("numberOfGroups", "" + cursor.getCount());
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			return (cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID)));
		}

		return null;

	}

	private String getTempalteId(String groupId) {

		String[] projection = { DatabaseCreator.COL_GROUP_TEMPLATE_TEMPLATE_ID };
		String where = DatabaseCreator.COL_GROUP_TEMPLATE_GROUP_ID + "=?";
		Cursor cursor = context.getContentResolver().query(
				DataProvider.CONTENT_URI_TAMPLATE_GROUP, projection, where,
				new String[] { groupId }, null);
		Log.d("numberOfTemplates", "" + cursor.getCount());
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			return (cursor
					.getString(cursor
							.getColumnIndex(DatabaseCreator.COL_GROUP_TEMPLATE_TEMPLATE_ID)));
		}
		return null;
	}

	private String getTemplate(String templateId) {
		String[] projection = { DatabaseCreator.COL_TEMPLATE_MESSAGE };
		String where = DatabaseCreator.COL_ID + "=?";
		Cursor cursor = context.getContentResolver().query(
				DataProvider.CONTENT_URI_TEMPLATES, projection, where,
				new String[] { templateId }, null);
		cursor.moveToFirst();
		return (cursor.getString(cursor
				.getColumnIndex(DatabaseCreator.COL_TEMPLATE_MESSAGE)));

	}

	private String createMessege(String template) {
		return template;
	}

	private String getMessage(String phoneNo) {
		Log.i(">>>Broadcast", "phoneNo:" + phoneNo);
		String contactId = this.getcontactId(phoneNo);
		Log.i(">>>Broadcast", "ContactId:" + contactId);
		if (contactId == null) {
			return this.unknowNumberMessage;
		} else {
			String groupId = this.getGroupId(contactId);
			Log.i(">>>Broadcast", "groupId:" + groupId);
			if (groupId == null) {
				return this.contactNoneGourpedMessage;
			} else {
				String templateId = this.getTempalteId(groupId);
				Log.i(">>>Broadcast", "templateId:" + templateId);
				if (templateId == null) {
					return this.groupDoNotHaveTemplateMessage;
				} else {
					return this.createMessege(this.getTemplate(templateId));
				}
			}
		}
	}
}
