package com.smartreply.Activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;

import com.smartreply.R;
import com.smartreply.DatabaseHandling.DataProvider;
import com.smartreply.DatabaseHandling.DatabaseCreator;

public class TemplateViewActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.template_view_layout);
		Intent i = getIntent();
		EditText txtTitel = (EditText) findViewById(R.id.txtTitel);
		EditText txtMessage = (EditText) findViewById(R.id.txtMassage);

		// getting attached intent data
		String templateId = i.getStringExtra("product");
		String projection[] = { DatabaseCreator.COL_TEMPLATE_TITLE,
				DatabaseCreator.COL_TEMPLATE_MESSAGE };

		Cursor tempalteCursor = getContentResolver().query(
				Uri.withAppendedPath(DataProvider.CONTENT_URI,
						String.valueOf(templateId)), projection, null, null,
				null);

		if (tempalteCursor.moveToFirst()) {
			String titel = tempalteCursor.getString(0);
			String message = tempalteCursor.getString(1);
			txtTitel.setText(titel);
			txtMessage.setText(message);
		}
		tempalteCursor.close();

	}
}