package com.smartreply.CallHandling;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CustomPhoneStateListener extends PhoneStateListener {

	Context context;
	String callState;
	private static boolean wasRiging;
	private static int previousNoOfMissCall;

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
			if (wasRiging) {
				sendSMSToMissNo(incomingNumber, "Test");
			}
			wasRiging = false;
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			callState = "OFFHOOK";
			break;
		case TelephonyManager.CALL_STATE_RINGING:
			callState = "RIGING";
			previousNoOfMissCall = this.getMisscallCount();
			wasRiging = true;
			break;
		default:
			break;
		}

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
		//if (this.validateMissCall(previousNoOfMissCall)) {
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(phoneNumber, null, message, null, null);
		//}
	}

	private boolean validateMissCall(int preNoOfMissCall) {
		int crtNoOfMissCall = this.getMisscallCount();
		if (preNoOfMissCall == crtNoOfMissCall) {
			return false;
		}
		return true;
	}
}
