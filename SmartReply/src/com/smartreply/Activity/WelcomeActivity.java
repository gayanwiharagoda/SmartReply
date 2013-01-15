package com.smartreply.Activity;

import java.util.ArrayList;

import com.smartreply.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class WelcomeActivity extends Activity {
	private ListView lvItem;
	private ArrayList<String> itemArrey;
	private ArrayAdapter<String> itemAdapter;
	
	private Spinner spinner;
	private ArrayList<String> spinnerItemArray;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_layout);
		setUpView();

	}

	private void setUpView() {
		// TODO Auto-generated method stub
//		etInput = (EditText) this.findViewById(R.id.editText_input);
//		btnAdd = (Button) this.findViewById(R.id.button_add);
		lvItem = (ListView) this.findViewById(R.id.listView1);
//
//		itemArrey = new ArrayList<String>();
//		itemArrey.clear();
//
//		itemAdapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_list_item_1, itemArrey);
//		lvItem.setAdapter(itemAdapter);
		
		
		spinnerItemArray = new ArrayList<String>();
		spinnerItemArray.add("Test1");
		spinnerItemArray.add("Test");
		spinner = (Spinner) findViewById(R.id.spinner1);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> adapter = new  ArrayAdapter<String>(this,
		        android.R.layout.simple_spinner_item, spinnerItemArray);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);

		
		itemArrey =new ArrayList<String>();
		itemArrey.add("Testing1");
		itemArrey.add("Testing2");
		itemAdapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_list_item_1, itemArrey);
		lvItem.setAdapter(itemAdapter);
		
//		btnAdd.setOnClickListener(new View.OnClickListener() {
//
//			public void onClick(View v) {
//
//				//addItemList();
//			}
//		});
//
//		etInput.setOnKeyListener(new View.OnKeyListener() {
//
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				// TODO Auto-generated method stub
//
//				if (keyCode == KeyEvent.KEYCODE_ENTER) {
//					//addItemList();
//				}
//				return true;
//			}
//		});

	}

}
