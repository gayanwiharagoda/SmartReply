package com.smartreply.CallHandling;

import com.smartreply.DatabaseHandling.DataProvider;
import com.smartreply.DatabaseHandling.DatabaseCreator;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CustomPhoneStateListener extends PhoneStateListener {

	Context context;
	String callState;
	private static int previousNoOfMissCall;
	private static int previousCallState;

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
			if (previousCallState != TelephonyManager.CALL_STATE_IDLE) {
				String message= "TEST";
				message = this.getMessage(incomingNumber);
				sendSMSToMissNo(incomingNumber, message);
			}
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			callState = "OFFHOOK";
			break;
		case TelephonyManager.CALL_STATE_RINGING:
			callState = "RIGING";
			previousNoOfMissCall = this.getMisscallCount();
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
		if (this.validateMissCall(previousNoOfMissCall)) {
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(phoneNumber, null, message, null, null);
		}
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

	
	//creating message 
	private String getcontactId(String phoneNo) {
		String[] projection = { ContactsContract.Contacts._ID };
		String where = ContactsContract.CommonDataKinds.Phone.NUMBER + "="
				+ phoneNo;
		Cursor cursor = context.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, projection, where, null,
				null);
		cursor.moveToFirst();
		return (cursor.getString(cursor
				.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)));
	}

	private String getGroupId(String contactId) {
		String[] projection = { ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID };
		String where = ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID
				+ "=" + contactId;
		Cursor cursor = context.getContentResolver().query(
				ContactsContract.Data.CONTENT_URI, projection, where, null,
				null);
		cursor.moveToFirst();
		return (cursor
				.getString(cursor
						.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID)));

	}

	private String getTempalteId(String groupId) {

		String[] projection = { DatabaseCreator.COL_GROUP_TEMPLATE_TEMPLATE_ID };
		String where = DatabaseCreator.COL_GROUP_TEMPLATE_GROUP_ID + "="
				+ groupId;
		Cursor cursor = context.getContentResolver().query(
				DataProvider.CONTENT_URI_TAMPLATE_GROUP, projection, where,
				null, null);
		cursor.moveToFirst();
		return (cursor
				.getString(cursor
						.getColumnIndex(DatabaseCreator.COL_GROUP_TEMPLATE_TEMPLATE_ID)));
	}

	private String getTemplate(String templateId) {
		String[] projection = { DatabaseCreator.COL_TEMPLATE_MESSAGE };
		String where = DatabaseCreator.COL_ID + "=" + templateId;
		Cursor cursor = context.getContentResolver().query(
				DataProvider.CONTENT_URI_TEMPLATES, projection, where, null,
				null);
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
			return "knownNumberMessage";
		} else {
			String groupId = this.getGroupId(contactId);
			Log.i(">>>Broadcast", "ContactId:" + contactId);
			if (groupId == null) {
				return "ContactNoneGourpedMessage";
			} else {
				String templateId = this.getTempalteId(groupId);
				Log.i(">>>Broadcast", "templateId:" + templateId);
				if (templateId == null) {
					return "GroupDoNotHaveTemplate";
				} else {
					return this.createMessege(this.getTemplate(templateId));
				}
			}
		}
	}
}
